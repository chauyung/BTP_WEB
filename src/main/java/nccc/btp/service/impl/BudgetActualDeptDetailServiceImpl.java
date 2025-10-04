package nccc.btp.service.impl;

import nccc.btp.dto.BudgetActualDeptDetailRequest;
import nccc.btp.dto.SourceMode;
import nccc.btp.repository.NcccAccountingLevelRepository;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.repository.BudgetActualRepository;
import nccc.btp.repository.DetailRowView;
import nccc.btp.service.BudgetActualDeptDetailService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BudgetActualDeptDetailServiceImpl implements BudgetActualDeptDetailService {

    private final BudgetActualRepository budgetActualRepository;
    private final NcccAccountingListRepository accListRepository;
    private final NcccAccountingLevelRepository accLevelRepository;

    public BudgetActualDeptDetailServiceImpl(BudgetActualRepository budgetActualRepository,
                                             NcccAccountingListRepository accListRepository,
                                             NcccAccountingLevelRepository accLevelRepository) {
        this.budgetActualRepository = budgetActualRepository;
        this.accListRepository = accListRepository;
        this.accLevelRepository = accLevelRepository;
    }

    public Optional<byte[]> exportExcelMaybe(BudgetActualDeptDetailRequest req) {
        req.validate();
        List<DetailRow> base = queryActuals(req);
        if (base.isEmpty()) return Optional.empty();

        ExpandedData expanded = expandAndAggregate(base, req);
        XSSFWorkbook wb = new XSSFWorkbook();
        Styles styles = new Styles(wb);

        List<String> deptOrder = new ArrayList<String>(expanded.deptNameByCode.keySet());
        Collections.sort(deptOrder);

        List<SourceMode> sheetSources = new ArrayList<SourceMode>();
        if (req.getSourceMode() == SourceMode.BEFORE) sheetSources.add(SourceMode.BEFORE);
        else if (req.getSourceMode() == SourceMode.AFTER) sheetSources.add(SourceMode.AFTER);
        else { sheetSources.add(SourceMode.BEFORE); sheetSources.add(SourceMode.AFTER); }

        for (String dept : deptOrder) {
            for (SourceMode src : sheetSources) {
                String deptName = expanded.deptNameByCode.containsKey(dept) ? expanded.deptNameByCode.get(dept) : dept;
                String sheetName = (src == SourceMode.BEFORE ? "分攤前-" : "分攤後-") + safeSheetName(deptName);
                Sheet sh = wb.createSheet(sheetName);
                writeSheet(sh, styles, req, expanded, dept, src);
                autosize(sh, expanded.columnOrder.size() + 3);
            }
        }

        for (SourceMode src : sheetSources) {
            Sheet sh = wb.createSheet(src == SourceMode.BEFORE ? "分攤前-總表" : "分攤後-總表");
            writeSummarySheet(sh, styles, req, expanded, src);
            autosize(sh, expanded.columnOrder.size() + 3);
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            wb.write(bos);
            return Optional.of(bos.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException("Export workbook failed", ex);
        }
    }

    private List<DetailRow> queryActuals(BudgetActualDeptDetailRequest req) {
        List<String> deptCodesUpper = CollectionUtils.isEmpty(req.getDeptCodes())
                ? Collections.<String>emptyList()
                : req.getDeptCodes().stream()
                    .filter(StringUtils::hasText)
                    .map(new Function<String, String>() { public String apply(String s) { return s.trim().toUpperCase(); }})
                    .collect(Collectors.<String>toList());

        String ap = req.getSourceMode() == SourceMode.BOTH ? null : req.getSourceMode().getDbValue();

        List<DetailRowView> rs = budgetActualRepository.findDeptDetailRows(
                req.getStartYm(),
                req.getEndYm(),
                deptCodesUpper.isEmpty() ? 1 : 0,
                deptCodesUpper,
                StringUtils.hasText(req.getAccountFrom()) ? req.getAccountFrom() : null,
                StringUtils.hasText(req.getAccountTo()) ? req.getAccountTo() : null,
                ap
        );

        List<DetailRow> list = new ArrayList<DetailRow>(rs.size());
        for (DetailRowView r : rs) {
            list.add(new DetailRow(
                    r.getYymm(), r.getVersion(), r.getApproation(),
                    r.getOuCode(), r.getOuName(),
                    r.getAccounting(), r.getSubject(),
                    r.getOperateItemCode(), r.getOperateItem(),
                    r.getAmount() == null ? BigDecimal.ZERO : r.getAmount()
            ));
        }
        return list;
    }

    private ExpandedData expandAndAggregate(List<DetailRow> base, BudgetActualDeptDetailRequest req) {
        ExpandedData ed = new ExpandedData();

        for (DetailRow r : base) {
            if (!ed.deptNameByCode.containsKey(r.ouCode)) {
                ed.deptNameByCode.put(r.ouCode, StringUtils.hasText(r.ouName) ? r.ouName : r.ouCode);
            }
            ed.columnOrder.add(r.itemCol());
        }
        ed.columnOrder = ed.columnOrder.stream().distinct().sorted().collect(Collectors.<String>toList());

        List<Integer> lvls = new ArrayList<Integer>(req.getExpandLevels());
        Collections.sort(lvls);

        final Function<String, Map<Integer, String>> cutter = new Function<String, Map<Integer, String>>() {
            public Map<Integer, String> apply(String code) { return cutToLevels(code, lvls); }
        };

        Set<String> needNames = new LinkedHashSet<String>();

        for (DetailRow r : base) {
            Map<Integer, String> parts = cutter.apply(r.accounting);
            for (Map.Entry<Integer, String> e : parts.entrySet()) {
                int lvl = e.getKey(); String acct = e.getValue();
                String k = key(r.ouCode, r.approation, r.itemCol(), lvl, acct);
                mergeAmt(ed.amtByKey, k, r.amount);
                needNames.add(acct);
            }
        }
        for (DetailRow r : base) {
            Map<Integer, String> parts = cutter.apply(r.accounting);
            for (Map.Entry<Integer, String> e : parts.entrySet()) {
                int lvl = e.getKey(); String acct = e.getValue();
                String k = key("__ALL__", r.approation, r.itemCol(), lvl, acct);
                mergeAmt(ed.amtByKey, k, r.amount);
            }
        }

        // 先用明細的 subject 直接帶入 8 碼名稱（優先）
        Map<String, String> direct8 = new HashMap<String, String>();
        for (DetailRow r : base) {
            if (r.accounting != null && r.accounting.length() == 8 && StringUtils.hasText(r.subject)) {
                String code8 = r.accounting.trim();
                if (!direct8.containsKey(code8)) {
                    direct8.put(code8, sanitize(r.subject));
                }
            }
        }

        // 其餘名稱從資料表補齊
        Map<String, String> nameMap = fetchAcctNames(needNames);

        // 明細 subject 覆蓋 8 碼名稱
        nameMap.putAll(direct8);

        for (Integer lvl : lvls) ed.acctNames.put(lvl, new HashMap<String, String>());
        for (String acct : needNames) {
            int lvl = acct.length();
            Map<String, String> m = ed.acctNames.get(lvl);
            if (m != null) m.put(acct, nameMap.containsKey(acct) ? nameMap.get(acct) : "");
        }
        return ed;
    }

    private Map<String, String> fetchAcctNames(Collection<String> codes) {
        Map<String, String> map = new HashMap<String, String>();
        if (codes == null || codes.isEmpty()) return map;

        Set<String> uniq = new LinkedHashSet<String>();
        for (String c : codes) if (c != null && c.length() > 0) uniq.add(c.trim());

        List<String> codes8 = new ArrayList<String>();
        List<String> codesX = new ArrayList<String>();
        for (String c : uniq) { if (c.length() == 8) codes8.add(c); else codesX.add(c); }

        if (!codes8.isEmpty()) {
            final int batch = 900;
            for (int i = 0; i < codes8.size(); i += batch) {
                List<String> part = codes8.subList(i, Math.min(i + batch, codes8.size()));
                List<NcccAccountingListRepository.AccName> rows = accListRepository.findNamesBySubjectIn(part);
                for (NcccAccountingListRepository.AccName r : rows) {
                    String code = r.getSubject();
                    if (code != null && code.length() > 0) map.put(code.trim(), sanitize(r.getName()));
                }
            }
        }
        if (!codesX.isEmpty()) {
            List<NcccAccountingLevelRepository.AccName> rows = accLevelRepository.findLevelNames(codesX);
            for (NcccAccountingLevelRepository.AccName r : rows) {
                if (r.getCode() != null) map.put(r.getCode().trim(), sanitize(r.getName()));
            }
        }
        return map;
    }

    private static void mergeAmt(Map<String, BigDecimal> m, String key, BigDecimal add) {
        BigDecimal cur = m.get(key);
        m.put(key, (cur == null ? BigDecimal.ZERO : cur).add(add == null ? BigDecimal.ZERO : add));
    }

    private void writeSheet(Sheet sh, Styles st, BudgetActualDeptDetailRequest req,
                            ExpandedData ed, String dept, SourceMode src) {
        int rowIdx = 0;
        int totalCols = ed.columnOrder.size() + 3;

        Row t1 = sh.createRow(rowIdx++); Cell tc1 = t1.createCell(0);
        tc1.setCellValue("財團法人聯合信用卡處理中心"); tc1.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

        Row t2 = sh.createRow(rowIdx++); Cell tc2 = t2.createCell(0);
        tc2.setCellValue("部門實支明細表"); tc2.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

        Row cond0 = sh.createRow(rowIdx++); cond0.createCell(0).setCellValue("部 門 別: " + (ed.deptNameByCode.containsKey(dept) ? ed.deptNameByCode.get(dept) : dept));
        Row cond1 = sh.createRow(rowIdx++); cond1.createCell(0).setCellValue("製表日期: " + formatRocDateToday());
        Row cond2 = sh.createRow(rowIdx++); cond2.createCell(0).setCellValue("期 間 : " + formatRocYmToCn(req.getStartYm()) + " ~ " + formatRocYmToCn(req.getEndYm()));

        rowIdx++;

        Row head = sh.createRow(rowIdx++); int col = 0;
        writeHeadCell(head, col++, "科目代號", st.th);
        writeHeadCell(head, col++, "會計科目", st.th);
        for (String itemCol : ed.columnOrder) writeHeadCell(head, col++, itemCol, st.thCenter);
        writeHeadCell(head, col++, "小計", st.thRight);

        Set<String> acc8Set = new HashSet<String>();
        for (String k : ed.amtByKey.keySet()) {
            if (k.startsWith(dept + "|" + src.getDbValue() + "|")) {
                String tail = k.substring(k.lastIndexOf('|') + 1);
                if (tail.startsWith("8:")) acc8Set.add(tail.substring(2));
            }
        }
        Map<Integer, Set<String>> lvSets = deriveLevelSetsFromAcc8(acc8Set);
        List<Integer> lvls = new ArrayList<Integer>(req.getExpandLevels()); Collections.sort(lvls);

        List<String> order = makeHierarchicalOrder(
                lvSets.containsKey(1) ? lvSets.get(1) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(4) ? lvSets.get(4) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(6) ? lvSets.get(6) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(8) ? lvSets.get(8) : java.util.Collections.<String>emptySet()
        );

        for (String acct : order) {
            int lvl = acct.length();
            if (!lvls.contains(lvl)) continue;

            Row r = sh.createRow(rowIdx++); int j = 0; BigDecimal rowSum = BigDecimal.ZERO;
            writeBodyCell(r, j++, acct, st.td);
            String name = ""; Map<String, String> nm = ed.acctNames.get(lvl);
            if (nm != null && nm.containsKey(acct)) name = nm.get(acct);
            writeBodyCell(r, j++, name, st.td);
            for (String itemCol : ed.columnOrder) {
                String key = key(dept, src.getDbValue(), itemCol, lvl, acct);
                BigDecimal v = ed.amtByKey.containsKey(key) ? ed.amtByKey.get(key) : BigDecimal.ZERO;
                rowSum = rowSum.add(v);
                writeAmount(r, j++, v, st.num);
            }
            writeAmount(r, j++, rowSum, st.numBold);
        }

        if (req.getExpandLevels().size() == 1) {
            int lvl = req.getExpandLevels().get(0);
            Row total = sh.createRow(rowIdx++);
            writeBodyCell(total, 0, "合計", st.total);
            writeBodyCell(total, 1, "", st.total);

            BigDecimal grand = BigDecimal.ZERO; int j = 2;
            Map<String, String> nm = ed.acctNames.get(lvl);
            Set<String> codes = nm != null ? nm.keySet() : java.util.Collections.<String>emptySet();
            for (String itemCol : ed.columnOrder) {
                BigDecimal colSum = BigDecimal.ZERO;
                for (String acct : codes) {
                    String key = key(dept, src.getDbValue(), itemCol, lvl, acct);
                    colSum = colSum.add(ed.amtByKey.containsKey(key) ? ed.amtByKey.get(key) : BigDecimal.ZERO);
                }
                grand = grand.add(colSum);
                writeAmount(total, j++, colSum, st.numBold);
            }
            writeAmount(total, j, grand, st.numBold);
        }
    }

    private void writeSummarySheet(Sheet sh, Styles st, BudgetActualDeptDetailRequest req,
                                   ExpandedData ed, SourceMode src) {
        int rowIdx = 0;
        int totalCols = ed.columnOrder.size() + 3;

        Row t1 = sh.createRow(rowIdx++); Cell tc1 = t1.createCell(0);
        tc1.setCellValue("財團法人聯合信用卡處理中心"); tc1.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

        Row t2 = sh.createRow(rowIdx++); Cell tc2 = t2.createCell(0);
        tc2.setCellValue("部門實支明細表 - 總表"); tc2.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

        Row cond1 = sh.createRow(rowIdx++); cond1.createCell(0).setCellValue("製表日期: " + formatRocDateToday());
        Row cond2 = sh.createRow(rowIdx++); cond2.createCell(0).setCellValue("期 間 : " + formatRocYmToCn(req.getStartYm()) + " ~ " + formatRocYmToCn(req.getEndYm()));
        Row cond3 = sh.createRow(rowIdx++); cond3.createCell(0).setCellValue("來源碼: " + (src == SourceMode.BEFORE ? "分攤前" : "分攤後"));

        rowIdx++;

        Row head = sh.createRow(rowIdx++); int col = 0;
        writeHeadCell(head, col++, "科目代號", st.th);
        writeHeadCell(head, col++, "會計科目", st.th);
        for (String itemCol : ed.columnOrder) writeHeadCell(head, col++, itemCol, st.thCenter);
        writeHeadCell(head, col++, "小計", st.thRight);

        Set<String> acc8Set = new HashSet<String>();
        for (String k : ed.amtByKey.keySet()) {
            if (k.startsWith("__ALL__|" + src.getDbValue() + "|")) {
                String tail = k.substring(k.lastIndexOf('|') + 1);
                if (tail.startsWith("8:")) acc8Set.add(tail.substring(2));
            }
        }
        Map<Integer, Set<String>> lvSets = deriveLevelSetsFromAcc8(acc8Set);
        List<Integer> lvls = new ArrayList<Integer>(req.getExpandLevels()); Collections.sort(lvls);

        List<String> order = makeHierarchicalOrder(
                lvSets.containsKey(1) ? lvSets.get(1) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(4) ? lvSets.get(4) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(6) ? lvSets.get(6) : java.util.Collections.<String>emptySet(),
                lvSets.containsKey(8) ? lvSets.get(8) : java.util.Collections.<String>emptySet()
        );

        for (String acct : order) {
            int lvl = acct.length();
            if (!lvls.contains(lvl)) continue;

            Row r = sh.createRow(rowIdx++); int j = 0; BigDecimal rowSum = BigDecimal.ZERO;
            writeBodyCell(r, j++, acct, st.td);
            String name = ""; Map<String, String> nm = ed.acctNames.get(lvl);
            if (nm != null && nm.containsKey(acct)) name = nm.get(acct);
            writeBodyCell(r, j++, name, st.td);
            for (String itemCol : ed.columnOrder) {
                String key = key("__ALL__", src.getDbValue(), itemCol, lvl, acct);
                BigDecimal v = ed.amtByKey.containsKey(key) ? ed.amtByKey.get(key) : BigDecimal.ZERO;
                rowSum = rowSum.add(v);
                writeAmount(r, j++, v, st.num);
            }
            writeAmount(r, j, rowSum, st.numBold);
        }
    }

    private static class DetailRow {
        String yymm; String version; String approation; String ouCode; String ouName;
        String accounting; String subject; String operateItemCode; String operateItem; BigDecimal amount;
        DetailRow(String y,String v,String a,String d,String dn,String ac,String sub,String ic,String in,BigDecimal am){
            this.yymm=y;this.version=v;this.approation=a;this.ouCode=d;this.ouName=dn;
            this.accounting=ac;this.subject=sub;this.operateItemCode=ic;this.operateItem=in;this.amount=am;
        }
        String itemCol(){
            return operateItemCode + (StringUtils.hasText(operateItem) ? (" " + operateItem) : "");
        }
    }

    private static class ExpandedData {
        List<String> columnOrder = new ArrayList<String>();
        Map<String, String> deptNameByCode = new LinkedHashMap<String, String>();
        Map<String, BigDecimal> amtByKey = new HashMap<String, BigDecimal>();
        Map<Integer, Map<String, String>> acctNames = new HashMap<Integer, Map<String, String>>();
    }

    private static class Styles {
        final CellStyle title, section, th, thCenter, thRight, td, num, numBold, total;
        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();

            CellStyle titleStyle = wb.createCellStyle();
            XSSFFont f1 = wb.createFont();
            f1.setFontHeightInPoints((short)14);
            f1.setBold(true);
            titleStyle.setFont(f1);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            this.title = titleStyle;

            CellStyle sectionStyle = wb.createCellStyle();
            XSSFFont f2 = wb.createFont(); f2.setBold(true);
            sectionStyle.setFont(f2);
            this.section = sectionStyle;

            CellStyle thStyle = wb.createCellStyle();
            thStyle.setBorderBottom(BorderStyle.THIN); thStyle.setBorderTop(BorderStyle.THIN);
            thStyle.setBorderLeft(BorderStyle.THIN);  thStyle.setBorderRight(BorderStyle.THIN);
            XSSFFont f3 = wb.createFont(); f3.setBold(true); thStyle.setFont(f3);
            this.th = thStyle;

            CellStyle thCenterStyle = wb.createCellStyle(); thCenterStyle.cloneStyleFrom(thStyle);
            thCenterStyle.setAlignment(HorizontalAlignment.CENTER);
            this.thCenter = thCenterStyle;

            CellStyle thRightStyle = wb.createCellStyle(); thRightStyle.cloneStyleFrom(thStyle);
            thRightStyle.setAlignment(HorizontalAlignment.RIGHT);
            this.thRight = thRightStyle;

            CellStyle tdStyle = wb.createCellStyle();
            tdStyle.setBorderBottom(BorderStyle.THIN); tdStyle.setBorderLeft(BorderStyle.THIN); tdStyle.setBorderRight(BorderStyle.THIN);
            this.td = tdStyle;

            CellStyle numStyle = wb.createCellStyle(); numStyle.cloneStyleFrom(tdStyle);
            numStyle.setDataFormat(fmt.getFormat("#,##0")); numStyle.setAlignment(HorizontalAlignment.RIGHT);
            this.num = numStyle;

            CellStyle numBoldStyle = wb.createCellStyle(); numBoldStyle.cloneStyleFrom(numStyle);
            XSSFFont nb = wb.createFont(); nb.setBold(true); numBoldStyle.setFont(nb);
            this.numBold = numBoldStyle;

            CellStyle totalStyle = wb.createCellStyle(); totalStyle.cloneStyleFrom(thRightStyle);
            this.total = totalStyle;
        }
    }

    private static void writeHeadCell(Row r, int col, String v, CellStyle st) {
        Cell c = r.createCell(col); c.setCellValue(v); c.setCellStyle(st);
    }
    private static void writeBodyCell(Row r, int col, String v, CellStyle st) {
        Cell c = r.createCell(col); c.setCellValue(v); c.setCellStyle(st);
    }
    private static void writeAmount(Row r, int col, BigDecimal v, CellStyle st) {
        Cell c = r.createCell(col);
        c.setCellValue((v == null ? BigDecimal.ZERO : v).doubleValue());
        c.setCellStyle(st);
    }

    private static void autosize(Sheet sh, int cols) {
        for (int i = 0; i < cols; i++) {
            try { sh.autoSizeColumn(i, true); } catch (Exception ignore) { sh.autoSizeColumn(i); }
        }
        double widenFactor = 1.60;
        int excelMax = 255 * 256;

        for (int i = 0; i < cols; i++) {
            int width = sh.getColumnWidth(i);
            int widened = (int) Math.min(excelMax, Math.round(width * widenFactor));

            int minChars;
            if (i == 0) minChars = 18;
            else if (i == 1) minChars = 48;
            else if (i == cols - 1) minChars = 18;
            else minChars = 24;

            int minWidth = minChars * 256;
            sh.setColumnWidth(i, Math.max(widened, minWidth));
        }
    }

    private static byte[] emptyWorkbook(String message) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("BUDAR11");
            sh.createRow(0).createCell(0).setCellValue(message);
            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static String safeSheetName(String s) {
        if (s == null) return "Sheet";
        String cleaned = s.replaceAll("[\\\\/\\?\\*\\n\\r\\t\\[\\]:]", " ");
        if (cleaned.length() > 31) cleaned = cleaned.substring(0, 31);
        return cleaned;
    }

    private static String sanitize(String s) {
        return s == null ? "" : s.replace("\r", "").replace("\n", "");
    }

    private static String formatRocDateToday() {
        LocalDate d = LocalDate.now();
        return String.format("%03d/%02d/%02d", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }

    private static String formatRocYmToCn(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) return "";
        int y = Integer.parseInt(yyyymm.substring(0, 4));
        int m = Integer.parseInt(yyyymm.substring(4, 6));
        return y + "年" + m + "月";
    }

    private static String key(String dept, String approation, String itemCol, int lvl, String acct) {
        return dept + "|" + approation + "|" + itemCol + "|" + (lvl + ":" + acct);
    }

    private static Map<Integer, String> cutToLevels(String code8, List<Integer> lvls) {
        Map<Integer, String> m = new LinkedHashMap<Integer, String>();
        for (Integer lv : lvls) {
            switch (lv) {
                case 1: m.put(1, code8.substring(0, 1)); break;
                case 4: m.put(4, code8.substring(0, 4)); break;
                case 6: m.put(6, code8.substring(0, 6)); break;
                case 8: m.put(8, code8); break;
                default: throw new IllegalArgumentException("Unsupported level: " + lv);
            }
        }
        return m;
    }

    private static Map<Integer, Set<String>> deriveLevelSetsFromAcc8(Collection<String> acc8s) {
        Set<String> lv8 = new HashSet<String>();
        Set<String> lv6 = new HashSet<String>();
        Set<String> lv4 = new HashSet<String>();
        Set<String> lv1 = new HashSet<String>();
        for (String a8 : acc8s) {
            if (a8 == null || a8.length() < 8) continue;
            lv8.add(a8);
            lv6.add(a8.substring(0, 6));
            lv4.add(a8.substring(0, 4));
            lv1.add(a8.substring(0, 1));
        }
        Map<Integer, Set<String>> m = new HashMap<Integer, Set<String>>();
        m.put(1, lv1); m.put(4, lv4); m.put(6, lv6); m.put(8, lv8);
        return m;
    }

    private static List<String> makeHierarchicalOrder(
            Set<String> lv1, Set<String> lv4, Set<String> lv6, Set<String> lv8) {
        List<String> out = new ArrayList<String>();
        TreeSet<String> s1 = new TreeSet<String>(lv1);
        TreeSet<String> s4 = new TreeSet<String>(lv4);
        TreeSet<String> s6 = new TreeSet<String>(lv6);
        TreeSet<String> s8 = new TreeSet<String>(lv8);
        for (String c1 : s1) {
            out.add(c1);
            for (String c4 : s4.tailSet(c1, true)) {
                if (!c4.startsWith(c1)) break;
                out.add(c4);
                for (String c6 : s6.tailSet(c4, true)) {
                    if (!c6.startsWith(c4)) break;
                    out.add(c6);
                    for (String c8 : s8.tailSet(c6, true)) {
                        if (!c8.startsWith(c6)) break;
                        out.add(c8);
                    }
                }
            }
        }
        return out;
    }
}
