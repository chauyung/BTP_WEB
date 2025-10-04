package nccc.btp.service.impl;

import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualCenterSummaryRequest;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.dto.SourceMode;
import nccc.btp.entity.NcccBudgetActual;
import nccc.btp.repository.BudgetActualRepository;
import nccc.btp.repository.NcccAccountingLevelRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.service.BudgetActualCenterSummaryService;
import nccc.btp.service.NcccBudgetService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BudgetActualCenterSummaryServiceImpl implements BudgetActualCenterSummaryService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private BudgetActualRepository budgetActualRepository;
    @Autowired
    private NcccAccountingLevelRepository ncccAccountingLevelRepository;
    @Autowired
    private NcccBudgetService budgetService;
    @Autowired
    private SyncOURepository syncOuRepository;

    @Override
    public Optional<byte[]> exportExcelMaybe(BudgetActualCenterSummaryRequest req) {
        req.validate();

        List<String> deptCodesUpper = CollectionUtils.isEmpty(req.getDeptCodes())
                ? Collections.emptyList()
                : req.getDeptCodes().stream()
                .filter(StringUtils::hasText)
                .map(s -> s.trim().toUpperCase())
                .collect(Collectors.toList());

        Map<String, String> accNameMap = loadNamesFromAccountingLevel();
        List<SheetSpec> sheets = prepareSheets(req);
        SortedSet<Integer> levelSet = new TreeSet<>(req.getExpandLevels());
        List<Integer> levels = new ArrayList<>(levelSet);

        String ap = req.getSourceMode() == SourceMode.BOTH ? null : req.getSourceMode().getDbValue();

        XSSFWorkbook wb = new XSSFWorkbook();
        Styles st = new Styles(wb);

        boolean anyData = false;

        for (SheetSpec sp : sheets) {
            List<NcccBudgetActual> rawData = budgetActualRepository.queryActuals(
                    req.getStartYm(),
                    req.getEndYm(),
                    deptCodesUpper.isEmpty() ? 1 : 0,
                    deptCodesUpper,
                    req.getAccountFrom(),
                    req.getAccountTo(),
                    ap
            );

            if (!rawData.isEmpty()) anyData = true;

            List<Raw> raw = rawData.stream().map(r -> {
                Raw x = new Raw();
                x.ou   = nvlStr(r.getId().getOuCode(), "").trim().toUpperCase();
                x.acc8 = nvlStr(r.getId().getAccounting(), "").trim();
                x.amt  = r.getAmount() == null ? BigDecimal.ZERO : r.getAmount();
                return x;
            }).collect(Collectors.toList());

            List<String> allOuCols = raw.stream()
                    .map(rr -> rr.ou)
                    .filter(s -> s != null && !s.isEmpty())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            Map<String, String> selectedDeptNames = loadDeptNames(new LinkedHashSet<>(deptCodesUpper));
            String deptLabel = buildDeptLabel(deptCodesUpper, selectedDeptNames);

            Sheet sh = wb.createSheet(sp.title);
            int totalCols = buildHeader(sh, deptLabel, req, allOuCols, st);

            Map<Integer, Pivot> pivots = buildPivots(raw, levels, allOuCols);

            populateSheet(sh, accNameMap, pivots, allOuCols, levels, st, totalCols);
        }

        if (!anyData) return Optional.empty();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            wb.write(bos);
            return Optional.of(bos.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException("Export workbook failed", ex);
        }
    }

    private List<SheetSpec> prepareSheets(BudgetActualCenterSummaryRequest req) {
        List<SheetSpec> sheets = new ArrayList<>();
        if (req.getSourceMode() == SourceMode.BOTH) {
            sheets.add(spec("分攤前", "BEFORE"));
            sheets.add(spec("分攤後", "AFTER"));
        } else {
            String ap = req.getSourceMode().name();
            sheets.add(spec(req.getSourceMode() == SourceMode.BEFORE ? "分攤前" : "分攤後", ap));
        }
        return sheets;
    }

    /** 建表頭並回傳本表總欄數（供 autosize/合併範圍一致使用） */
    private int buildHeader(Sheet sh, String deptLabel, BudgetActualCenterSummaryRequest req,
                            List<String> allOuCols, Styles st) {
        int totalCols = 2 + Math.max(allOuCols.size(), 1) + 1; // 代號+名稱 + 部門們 + 合計
        int rowIdx = 0;

        Row t1 = sh.createRow(rowIdx++);
        Cell tc1 = t1.createCell(0);
        tc1.setCellValue("財團法人聯合信用卡處理中心");
        tc1.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

        Row t2 = sh.createRow(rowIdx++);
        Cell tc2 = t2.createCell(0);
        tc2.setCellValue("中心各部實支彙總表");
        tc2.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

        Row cond0 = sh.createRow(rowIdx++);
        cond0.createCell(0).setCellValue("部 門 別: " + deptLabel);

        Row cond1 = sh.createRow(rowIdx++);
        cond1.createCell(0).setCellValue("製表日期: " + formatDateToday());

        Row cond2 = sh.createRow(rowIdx++);
        cond2.createCell(0).setCellValue("期 間 : " + formatYm(req.getStartYm()) + " ~ " + formatYm(req.getEndYm()));

        // 空一列
        rowIdx++;

        Map<String, String> deptNameMap = loadDeptNames(new LinkedHashSet<>(allOuCols));

        Row head = sh.createRow(rowIdx++);
        int c = 0;
        // ← 表頭改用靠右
        cell(head, c++, "會計科目代號", st.thRight);
        cell(head, c++, "會計科目名稱", st.thCenter);

        for (String ou : allOuCols) {
            String label = nvlStr(deptNameMap.get(ou), ou);
            cell(head, c++, label, st.thRight);
        }
        cell(head, c++, "合計", st.thRight);

        return totalCols;
    }

    private Map<Integer, Pivot> buildPivots(List<Raw> raw, List<Integer> levels, List<String> allOuCols) {
        Map<Integer, Pivot> pivots = new HashMap<>();
        for (Integer lv : Arrays.asList(1, 4, 6, 8)) {
            if (levels.contains(lv)) {
                pivots.put(lv, expandAndAggregate(raw, lv, allOuCols));
            }
        }
        return pivots;
    }

    private void populateSheet(Sheet sh,
                               Map<String, String> accNameMap,
                               Map<Integer, Pivot> pivots,
                               List<String> allOuCols,
                               List<Integer> levels,
                               Styles st,
                               int totalCols) {

        int rowIdx = sh.getLastRowNum() + 1;

        // 收集所有層級的 row key 並排序
        Set<String> codes = new LinkedHashSet<>();
        for (Map.Entry<Integer, Pivot> e : pivots.entrySet()) {
            codes.addAll(e.getValue().table.keySet());
        }
        List<String> order = new ArrayList<>(codes);
        Collections.sort(order);

        for (String code : order) {
            int lvl = code.length();
            if (!levels.contains(lvl)) continue;

            Pivot pv = pivots.get(lvl);
            if (pv == null) continue;

            boolean isSection = (lvl == 1 || lvl == 4 || lvl == 6);

            // 名稱欄縮排樣式
            CellStyle nameStyle =
                    (lvl == 1) ? st.nameL1 :
                    (lvl == 4) ? st.nameL4 :
                    (lvl == 6) ? st.nameL6 : st.nameL8;

            // ★ 會科代碼欄靠右：章節用粗體、明細用一般
            CellStyle codeStyle = isSection ? st.sectionRight : st.tdRight;
            // 金額欄：章節粗體、明細一般
            CellStyle numStyle  = isSection ? st.numBold : st.num;

            Row rr = sh.createRow(rowIdx++);
            int j = 0;

            // 代碼（靠右）
            cell(rr, j++, code, codeStyle);
            // 名稱（1/4/6/8 向上截短匹配）
            cell(rr, j++, findAccName(accNameMap, code), nameStyle);

            // 各 OU 欄 + 合計
            BigDecimal rowSum = BigDecimal.ZERO;
            for (String ou : allOuCols) {
                BigDecimal v = getVal(pv.table, code, ou);
                rowSum = rowSum.add(v);
                num(rr, j++, v, numStyle);
            }
            num(rr, j++, rowSum, st.numBold);
        }
        
        if (levels != null && levels.size() == 1) {
            Integer onlyLvl = levels.get(0);
            Pivot pv = pivots.get(onlyLvl);
            if (pv != null) {
                Row totalRow = sh.createRow(rowIdx++);
                int j = 0;

                // 「合計」標籤 + 空白名稱欄
                cell(totalRow, j++, "合計", st.total);
                cell(totalRow, j++, "",      st.total);

                BigDecimal grand = BigDecimal.ZERO;

                // 逐 OU 欄加總
                for (String ou : allOuCols) {
                    BigDecimal colSum = BigDecimal.ZERO;
                    // 累加該層級所有科目的該 OU 金額
                    for (Map.Entry<String, Map<String, BigDecimal>> e : pv.table.entrySet()) {
                        Map<String, BigDecimal> ouMap = e.getValue();
                        if (ouMap != null) {
                            BigDecimal v = ouMap.get(ou);
                            if (v != null) colSum = colSum.add(v);
                        }
                    }
                    grand = grand.add(colSum);
                    num(totalRow, j++, colSum, st.numBold);
                }

                // 合計欄
                num(totalRow, j, grand, st.numBold);
            }
        }
        autosize(sh, totalCols);
    }

    // ====== DTOs / helpers ======
    private static class Raw {
        String ou;
        String acc8;
        BigDecimal amt;
    }

    private static class Pivot {
        List<String> rows;
        List<String> cols;
        Map<String, Map<String, BigDecimal>> table; // rowKey -> (OU -> amt)
    }

    private static class SheetSpec {
        String title;
        String approation;
    }

    private static class Styles {
        final CellStyle title, th, thCenter, thRight;
        final CellStyle td, tdRight, num, numBold, total;
        final CellStyle sectionRight;
        final CellStyle nameL1, nameL4, nameL6, nameL8;

        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();

            // ========= 共用字型 =========
            XSSFFont boldFont = wb.createFont(); boldFont.setBold(true);
            XSSFFont bold14   = wb.createFont(); bold14.setBold(true); bold14.setFontHeightInPoints((short)14);

            // ========= 基底：四邊框 =========
            CellStyle base = wb.createCellStyle();
            base.setBorderTop(BorderStyle.THIN);
            base.setBorderBottom(BorderStyle.THIN);
            base.setBorderLeft(BorderStyle.THIN);
            base.setBorderRight(BorderStyle.THIN);

            // ========= 標題 =========
            title = wb.createCellStyle();
            title.setFont(bold14);
            title.setAlignment(HorizontalAlignment.CENTER);

            // ========= 表頭 =========
            th = wb.createCellStyle();
            th.cloneStyleFrom(base);
            th.setFont(boldFont);

            thCenter = wb.createCellStyle();
            thCenter.cloneStyleFrom(th);
            thCenter.setAlignment(HorizontalAlignment.CENTER);

            thRight = wb.createCellStyle();
            thRight.cloneStyleFrom(th);
            thRight.setAlignment(HorizontalAlignment.RIGHT);

            // ========= 一般資料格 =========
            td = wb.createCellStyle();
            td.cloneStyleFrom(base);

            tdRight = wb.createCellStyle();
            tdRight.cloneStyleFrom(base);
            tdRight.setAlignment(HorizontalAlignment.RIGHT);

            // ========= 數字欄 =========
            num = wb.createCellStyle();
            num.cloneStyleFrom(base);
            num.setAlignment(HorizontalAlignment.RIGHT);
            num.setDataFormat(fmt.getFormat("#,##0"));

            numBold = wb.createCellStyle();
            numBold.cloneStyleFrom(num);
            numBold.setFont(boldFont);

            // ========= 章節代碼（粗體 + 右對齊 + 四邊框） =========
            sectionRight = wb.createCellStyle();
            sectionRight.cloneStyleFrom(base);
            sectionRight.setFont(boldFont);
            sectionRight.setAlignment(HorizontalAlignment.RIGHT);

            // ========= 名稱欄（縮排 + 框線） =========
            nameL1 = wb.createCellStyle();
            nameL1.cloneStyleFrom(base);
            nameL1.setFont(boldFont);
            nameL1.setAlignment(HorizontalAlignment.LEFT);
            nameL1.setIndention((short)0);

            nameL4 = wb.createCellStyle();
            nameL4.cloneStyleFrom(nameL1);
            nameL4.setIndention((short)1);

            nameL6 = wb.createCellStyle();
            nameL6.cloneStyleFrom(nameL1);
            nameL6.setIndention((short)2);

            nameL8 = wb.createCellStyle();
            nameL8.cloneStyleFrom(base);
            nameL8.setAlignment(HorizontalAlignment.LEFT);
            nameL8.setIndention((short)3);

            // ========= 合計欄（表頭右對齊） =========
            total = wb.createCellStyle();
            total.cloneStyleFrom(thRight);
        }
    }

    private String buildDeptLabel(List<String> deptCodesUpper, Map<String, String> deptNameMap) {
        if (deptCodesUpper == null || deptCodesUpper.isEmpty()) return "全部";
        if (deptCodesUpper.size() == 1) {
            String code = deptCodesUpper.get(0);
            if (!StringUtils.hasText(code)) return "全部";
            return deptNameMap.getOrDefault(code, code);
        }
        return "多部門";
    }

    private String formatDateToday() {
        LocalDate d = LocalDate.now();
        return String.format("%03d/%02d/%02d", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }

    private String formatYm(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) return yyyymm;
        int y = Integer.parseInt(yyyymm.substring(0, 4));
        int m = Integer.parseInt(yyyymm.substring(4, 6));
        return y + "年" + m + "月";
    }

    private Map<String, String> loadDeptNames(Set<String> ouCodes) {
        Map<String, String> names = new LinkedHashMap<>();
        if (ouCodes == null || ouCodes.isEmpty()) return names;

        Set<String> keys = ouCodes.stream()
                .filter(Objects::nonNull)
                .map(s -> s.trim().toUpperCase())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getSourceDepartment();
                if (resp != null && resp.getData() instanceof List) {
                    for (Object row : (List<?>) resp.getData()) {
                        String code = null;
                        String name = "";
                        if (row instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) row;
                            Object c = m.get("code"); if (c == null) c = m.get("CODE");
                            Object n = m.get("name"); if (n == null) n = m.get("NAME");
                            code = (c == null) ? null : String.valueOf(c);
                            name = (n == null) ? ""  : String.valueOf(n).trim();
                        }
                        if (code != null) {
                            String key = code.trim().toUpperCase();
                            if (keys.contains(key)) {
                                names.putIfAbsent(key, name);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) { }

        Set<String> missing = new LinkedHashSet<>(keys);
        missing.removeAll(names.keySet());
        if (!missing.isEmpty()) {
            List<Object[]> list = syncOuRepository.findNames(missing);
            for (Object[] r : list) {
                String code = r[0] == null ? null : String.valueOf(r[0]);
                String name = r[1] == null ? ""  : String.valueOf(r[1]).trim();
                if (code != null) {
                    String key = code.trim().toUpperCase();
                    if (missing.contains(key)) {
                        names.put(key, name);
                    }
                }
            }
        }
        return names;
    }

    private String findAccName(Map<String, String> accNameMap, String code) {
        if (!StringUtils.hasText(code)) return "";
        int[] lens = new int[]{8, 6, 4, 1};
        for (int len : lens) {
            if (code.length() >= len) {
                String k = code.substring(0, len);
                String name = accNameMap.get(k);
                if (StringUtils.hasText(name)) return name;
            }
        }
        return "";
    }

    public Map<String, String> loadNamesFromAccountingLevel() {
        Map<String, String> map = new HashMap<>();
        List<NcccAccountingLevelRepository.AccName> rows = ncccAccountingLevelRepository.findLevelNames();
        for (NcccAccountingLevelRepository.AccName r : rows) {
            if (r.getCode() != null) map.put(r.getCode(), r.getName());
        }
        return map;
    }

    private static BigDecimal getVal(Map<String, Map<String, BigDecimal>> t, String row, String col) {
        Map<String, BigDecimal> r = t.get(row);
        if (r == null) return BigDecimal.ZERO;
        BigDecimal v = r.get(col);
        return v == null ? BigDecimal.ZERO : v;
    }

    private static void cell(Row row, int col, String v, CellStyle cs) {
        Cell c = row.createCell(col);
        c.setCellValue(v == null ? "" : v);
        c.setCellStyle(cs);
    }

    private static void num(Row row, int col, BigDecimal v, CellStyle cs) {
        Cell c = row.createCell(col);
        c.setCellValue(v == null ? 0d : v.doubleValue());
        c.setCellStyle(cs);
    }

    private static void autosize(Sheet sh, int totalCols) {
        for (int i = 0; i < totalCols; i++) {
            try { sh.autoSizeColumn(i, true); } catch (Exception ignore) { sh.autoSizeColumn(i); }
        }
        final double widenFactor = 1.60;
        final int EXCEL_MAX = 255 * 256;
        for (int i = 0; i < totalCols; i++) {
            int width = sh.getColumnWidth(i);
            int widened = (int) Math.min(EXCEL_MAX, Math.round(width * widenFactor));
            int minChars = (i == 0 || i == totalCols - 1) ? 18 : (i == 1 ? 48 : 24);
            sh.setColumnWidth(i, Math.max(widened, minChars * 256));
        }
    }

    private static SheetSpec spec(String title, String approation) {
        SheetSpec s = new SheetSpec();
        s.title = title;
        s.approation = approation;
        return s;
    }

    private static String nvlStr(String value, String def) {
        return (value == null || value.isEmpty()) ? def : value;
    }

    /** 依層級「前綴」聚合；例如 level=4 時，51010101 會累加到 5101 */
    private Pivot expandAndAggregate(List<Raw> raw, Integer level, List<String> allOuCols) {
        Pivot pivot = new Pivot();
        pivot.rows = new ArrayList<>();
        pivot.cols = allOuCols;
        pivot.table = new HashMap<>();

        Set<String> rowKeys = new LinkedHashSet<>();
        for (Raw r : raw) {
            if (r.acc8 == null) continue;
            if (r.acc8.length() < level) continue;
            String key = r.acc8.substring(0, level);
            rowKeys.add(key);

            Map<String, BigDecimal> ouMap = pivot.table.computeIfAbsent(key, k -> new HashMap<>());
            ouMap.merge(r.ou, r.amt, BigDecimal::add);
        }

        List<String> sorted = new ArrayList<>(rowKeys);
        Collections.sort(sorted);
        pivot.rows.addAll(sorted);
        return pivot;
    }
}
