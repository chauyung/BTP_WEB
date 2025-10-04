package nccc.btp.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualBudgetSummaryRequest;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.repository.NcccAccountingListRepository.AccName;
import nccc.btp.repository.NcccAccountingLevelRepository;
import nccc.btp.repository.BudgetActualRepository;
import nccc.btp.repository.BudgetActualRepository.ActualPostedAgg;
import nccc.btp.repository.NcccBudgetMRepository;
import nccc.btp.repository.NcccBudgetMRepository.BudgetLatestAgg;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.service.BudgetActualBudgetSummaryService;
import nccc.btp.service.NcccBudgetService;
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
import java.time.YearMonth;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BudgetActualBudgetSummaryServiceImpl implements BudgetActualBudgetSummaryService {

    private final NcccBudgetMRepository budgetMRepository;
    private final BudgetActualRepository budgetActualRepository;
    private final NcccAccountingListRepository accountingListRepository;
    private final NcccAccountingLevelRepository accLevelRepository;
    private final SyncOURepository syncOuRepository;
    private final NcccBudgetService budgetService;

    public BudgetActualBudgetSummaryServiceImpl(NcccBudgetMRepository budgetMRepository,
                                                BudgetActualRepository budgetActualRepository,
                                                NcccAccountingListRepository accountingListRepository,
                                                NcccAccountingLevelRepository accLevelRepository,
                                                SyncOURepository syncOuRepository,
                                                NcccBudgetService budgetService) {
        this.budgetMRepository = budgetMRepository;
        this.budgetActualRepository = budgetActualRepository;
        this.accountingListRepository = accountingListRepository;
        this.accLevelRepository = accLevelRepository;
        this.syncOuRepository = syncOuRepository;
        this.budgetService = budgetService;
    }

    @Data
    private static class BudgetLatest {
        String ou; String acc8;
        BigDecimal original = BigDecimal.ZERO;
        BigDecimal reserve = BigDecimal.ZERO;
        BigDecimal allocIn = BigDecimal.ZERO;
        BigDecimal allocOut = BigDecimal.ZERO;
        BigDecimal occupy = BigDecimal.ZERO;
        BigDecimal useAmt = BigDecimal.ZERO;
        BigDecimal consume = BigDecimal.ZERO;
        BigDecimal available = BigDecimal.ZERO;
    }

    @Data
    private static class ActualAgg {
        String ou; String acc8;
        BigDecimal posted = BigDecimal.ZERO;
    }

    @Data
    private static class RowOut {
        String deptCode; String deptName;
        String accCode; int level; String accName;
        BigDecimal original = BigDecimal.ZERO;
        BigDecimal keep = BigDecimal.ZERO;
        BigDecimal inAmt = BigDecimal.ZERO;
        BigDecimal outAmt = BigDecimal.ZERO;
        BigDecimal available = BigDecimal.ZERO;
        BigDecimal occupy = BigDecimal.ZERO;
        BigDecimal useAmt = BigDecimal.ZERO;
        BigDecimal posted = BigDecimal.ZERO;
    }

    @Override
    public Optional<byte[]> exportExcelMaybe(BudgetActualBudgetSummaryRequest req) {
        req.validate();

        SortedSet<String> years = coveredYears(req.getStartYm(), req.getEndYm());
        List<String> ouFilter = req.getDeptCodes();
        int hasOu = CollectionUtils.isEmpty(ouFilter) ? 0 : 1;
        String accF = StringUtils.hasText(req.getAccountFrom()) ? req.getAccountFrom() : null;
        String accT = StringUtils.hasText(req.getAccountTo()) ? req.getAccountTo() : null;
        if (ouFilter == null) ouFilter = Collections.<String>emptyList();

        List<BudgetLatestAgg> latestAgg =
                budgetMRepository.aggregateLatest(years, ouFilter, hasOu, accF, accT);
        List<ActualPostedAgg> actualAgg =
                budgetActualRepository.aggregatePosted(req.getStartYm(), req.getEndYm(), ouFilter, hasOu, accF, accT);

        if (latestAgg.isEmpty()) return Optional.empty();

        List<BudgetLatest> latest = new ArrayList<BudgetLatest>(latestAgg.size());
        for (BudgetLatestAgg r : latestAgg) {
            BudgetLatest x = new BudgetLatest();
            x.ou = r.getOu(); x.acc8 = r.getAcc8();
            x.original = nvl(r.getOriginal());
            x.reserve = nvl(r.getReserve());
            x.allocIn = nvl(r.getAllocIn());
            x.allocOut = nvl(r.getAllocOut());
            x.occupy = nvl(r.getOccupy());
            x.useAmt = nvl(r.getUseAmt());
            x.consume = nvl(r.getConsume());
            x.available = x.original.add(x.reserve).add(x.allocIn).subtract(x.allocOut);
            latest.add(x);
        }

        List<ActualAgg> actual = new ArrayList<ActualAgg>(actualAgg.size());
        for (ActualPostedAgg r : actualAgg) {
            ActualAgg x = new ActualAgg();
            x.ou = r.getOu(); x.acc8 = r.getAcc8();
            x.posted = nvl(r.getPosted());
            actual.add(x);
        }

        SortedSet<String> ouSets = new TreeSet<String>();
        Set<String> accCodes8 = new LinkedHashSet<String>();
        for (BudgetLatest b : latest) { ouSets.add(b.ou); accCodes8.add(b.acc8); }
        for (ActualAgg a : actual) { ouSets.add(a.ou); accCodes8.add(a.acc8); }

        Map<String, String> deptNameMap = loadDeptNames(ouSets);

        Set<String> needCodes = includeParents(accCodes8, req.getExpandLevels());
        Map<String, String> accNameMap = loadAccountNames(needCodes);

        // 建 latestMap
        Map<String, BudgetLatest> latestMap = new LinkedHashMap<String, BudgetLatest>();
        for (BudgetLatest x : latest) latestMap.put(key(x.ou, x.acc8), x);

        // 建 actualMap
        Map<String, ActualAgg> actualMap = new LinkedHashMap<String, ActualAgg>();
        for (ActualAgg x : actual) actualMap.put(key(x.ou, x.acc8), x);

        SortedSet<String> keys = new TreeSet<String>(latestMap.keySet());
        keys.addAll(actualMap.keySet());

        List<RowOut> rows8 = new ArrayList<RowOut>();
        for (String k : keys) {
            String[] p = k.split("\\|", -1);
            String ou = p[0], acc8 = p[1];
            BudgetLatest b = latestMap.get(k);
            ActualAgg a = actualMap.get(k);

            RowOut r = new RowOut();
            r.deptCode = ou;
            r.deptName = deptNameMap.getOrDefault(ou, ou);
            r.accCode = acc8;
            r.level = 8;
            r.accName = accNameMap.getOrDefault(acc8, "");
            if (b != null) {
                r.original = nvl(b.original);
                r.keep = nvl(b.reserve);
                r.inAmt = nvl(b.allocIn);
                r.outAmt = nvl(b.allocOut);
                r.available = nvl(b.available);
                r.occupy = nvl(b.occupy);
                r.useAmt = nvl(b.useAmt);
            }
            if (a != null) r.posted = nvl(a.posted);
            rows8.add(r);
        }

        SortedSet<Integer> expand = new TreeSet<Integer>(req.getExpandLevels());
        List<RowOut> allRows = rollup(rows8, expand, accNameMap);

        Collections.sort(allRows, new Comparator<RowOut>() {
            public int compare(RowOut x, RowOut y) {
                int c = nvlStr(x.deptCode, "").compareTo(nvlStr(y.deptCode, ""));
                if (c != 0) return c;
                return accHierKey(x).compareTo(accHierKey(y));
            }
        });

        return Optional.of(buildWorkbook(req, allRows, deptNameMap));
    }

    private Set<String> includeParents(Set<String> acc8Codes, Collection<Integer> expandLevels) {
        Set<String> out = new LinkedHashSet<String>();
        if (acc8Codes == null) return out;
        boolean need1 = expandLevels != null && expandLevels.contains(Integer.valueOf(1));
        boolean need4 = expandLevels != null && expandLevels.contains(Integer.valueOf(4));
        boolean need6 = expandLevels != null && expandLevels.contains(Integer.valueOf(6));
        for (String c : acc8Codes) {
            if (c == null) continue;
            String t = c.trim();
            if (t.length() >= 8) out.add(t.substring(0, 8));
            if (need6 && t.length() >= 6) out.add(t.substring(0, 6));
            if (need4 && t.length() >= 4) out.add(t.substring(0, 4));
            if (need1 && t.length() >= 1) out.add(t.substring(0, 1));
        }
        return out;
    }

    private Map<String, String> loadAccountNames(Set<String> accountCodes) {
        Map<String, String> names = new LinkedHashMap<String, String>();
        if (accountCodes == null || accountCodes.isEmpty()) return names;

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getBudgetAccount();
                if (resp != null && resp.getData() instanceof java.util.List) {
                    java.util.List<?> rows = (java.util.List<?>) resp.getData();
                    for (Object row : rows) {
                        String code = null;
                        String name = "";
                        if (row instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) row;
                            Object c = m.get("accounting"); if (c == null) c = m.get("ACCOUNTING");
                            if (c == null) c = m.get("accountCode");
                            if (c == null) c = m.get("ACCOUNT_CODE");
                            if (c == null) c = m.get("code");
                            Object n = m.get("accountingName"); if (n == null) n = m.get("ACCOUNTING_NAME");
                            if (n == null) n = m.get("name"); if (n == null) n = m.get("NAME");
                            code = c == null ? null : String.valueOf(c);
                            name = n == null ? "" : String.valueOf(n);
                        }
                        if (code != null && accountCodes.contains(code)) names.put(code, name);
                    }
                }
            }
        } catch (Exception ignore) { }

        if (names.size() < accountCodes.size()) {
            List<String> codes8 = new ArrayList<String>();
            List<String> codesX = new ArrayList<String>();
            for (String c : accountCodes) {
                if (c == null) continue;
                String t = c.trim();
                if (t.length() == 8) codes8.add(t); else codesX.add(t);
            }

            if (!codes8.isEmpty()) {
                List<AccName> list = accountingListRepository.findNames(codes8);
                for (AccName a : list) {
                    String code = a.getSubject();
                    String name = a.getName();
                    if (code != null) names.put(code, name == null ? "" : name);
                }
            }
            if (!codesX.isEmpty()) {
                List<NcccAccountingLevelRepository.AccName> list2 = accLevelRepository.findLevelNames(codesX);
                for (NcccAccountingLevelRepository.AccName a : list2) {
                    if (a.getCode() != null) names.put(a.getCode(), a.getName() == null ? "" : a.getName());
                }
            }
        }
        return names;
    }

    private Map<String, String> loadDeptNames(Set<String> ouCodes) {
        Map<String, String> map = new HashMap<String, String>();
        if (ouCodes == null || ouCodes.isEmpty()) return map;

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getSourceDepartment();
                if (resp != null && resp.getData() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<?> rows = (List<?>) resp.getData();
                    for (Object row : rows) {
                        String code = null, name = "";
                        if (row instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) row;
                            Object c = m.get("code"); if (c == null) c = m.get("CODE");
                            Object n = m.get("name"); if (n == null) n = m.get("NAME");
                            code = c == null ? null : String.valueOf(c);
                            name = n == null ? "" : String.valueOf(n);
                        } else {
                            code = tryReadString(row, "getCode");
                            if (code == null) code = tryReadField(row, "code");
                            name = nvlStr(tryReadString(row, "getName"), tryReadField(row, "name"));
                        }
                        if (code != null && ouCodes.contains(code)) map.put(code, name == null ? "" : name);
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("loadDeptNames via service failed: {}", ex.getMessage());
        }

        if (map.size() < ouCodes.size()) {
            List<Object[]> rs = syncOuRepository.findNames(ouCodes);
            for (Object[] r : rs) {
                String code = str(r[0]);
                String name = str(r[1]);
                if (code != null) map.put(code, name == null ? "" : name);
            }
        }
        return map;
    }

    private static String key(String ou, String acc8) {
        return (ou == null ? "" : ou) + "|" + (acc8 == null ? "" : acc8);
    }

    private List<RowOut> rollup(List<RowOut> rows8, SortedSet<Integer> expand, Map<String, String> accNameMap) {
        Map<String, Map<Integer, Map<String, RowOut>>> byDept = new LinkedHashMap<String, Map<Integer, Map<String, RowOut>>>();
        for (RowOut r : rows8) putAgg(byDept, r.deptCode, 8, r.accCode, r, accNameMap);
        for (RowOut r : rows8) {
            String a8 = r.accCode; if (a8 == null) continue;
            if (a8.length() >= 6 && expand.contains(Integer.valueOf(6))) putAgg(byDept, r.deptCode, 6, a8.substring(0, 6), r, accNameMap);
            if (a8.length() >= 4 && expand.contains(Integer.valueOf(4))) putAgg(byDept, r.deptCode, 4, a8.substring(0, 4), r, accNameMap);
            if (a8.length() >= 1 && expand.contains(Integer.valueOf(1))) putAgg(byDept, r.deptCode, 1, a8.substring(0, 1), r, accNameMap);
        }
        List<RowOut> out = new ArrayList<RowOut>();
        for (Map.Entry<String, Map<Integer, Map<String, RowOut>>> e : byDept.entrySet()) {
            Map<Integer, Map<String, RowOut>> lvMap = e.getValue();
            for (Integer lv : Arrays.asList(Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(8))) {
                if (!expand.contains(lv)) continue;
                Map<String, RowOut> m = lvMap.get(lv);
                if (m != null) out.addAll(m.values());
            }
        }
        return out;
    }

    private void putAgg(Map<String, Map<Integer, Map<String, RowOut>>> byDept, String dept, int level, String accCode, RowOut src, Map<String, String> accNameMap) {
        Map<Integer, Map<String, RowOut>> lvMap = byDept.get(dept);
        if (lvMap == null) {
            lvMap = new LinkedHashMap<Integer, Map<String, RowOut>>();
            byDept.put(dept, lvMap);
        }
        Map<String, RowOut> codeMap = lvMap.get(level);
        if (codeMap == null) {
            codeMap = new LinkedHashMap<String, RowOut>();
            lvMap.put(level, codeMap);
        }
        RowOut tgt = codeMap.get(accCode);
        if (tgt == null) {
            tgt = new RowOut();
            tgt.deptCode = dept; tgt.deptName = src.deptName;
            tgt.accCode = accCode; tgt.level = level;
            tgt.accName = accNameMap.getOrDefault(accCode, "");
            codeMap.put(accCode, tgt);
        }
        tgt.original = tgt.original.add(nvl(src.original));
        tgt.keep = tgt.keep.add(nvl(src.keep));
        tgt.inAmt = tgt.inAmt.add(nvl(src.inAmt));
        tgt.outAmt = tgt.outAmt.add(nvl(src.outAmt));
        tgt.available = tgt.available.add(nvl(src.available));
        tgt.occupy = tgt.occupy.add(nvl(src.occupy));
        tgt.useAmt = tgt.useAmt.add(nvl(src.useAmt));
        tgt.posted = tgt.posted.add(nvl(src.posted));
    }

    private byte[] buildWorkbook(BudgetActualBudgetSummaryRequest req, List<RowOut> rows, Map<String, String> deptNameMap) {
        // 分組 by 部門（不用 Collectors）
        Map<String, List<RowOut>> byDept = new LinkedHashMap<String, List<RowOut>>();
        for (RowOut r : rows) {
            List<RowOut> list = byDept.get(r.deptCode);
            if (list == null) { list = new ArrayList<RowOut>(); byDept.put(r.deptCode, list); }
            list.add(r);
        }

        XSSFWorkbook wb = new XSSFWorkbook();
        Styles st = new Styles(wb);

        Set<String> used = new LinkedHashSet<String>();
        boolean singleLevel = req.getExpandLevels() != null && req.getExpandLevels().size() == 1;

        List<String> deptOrder = new ArrayList<String>(byDept.keySet());
        Collections.sort(deptOrder);

        for (String dept : deptOrder) {
            List<RowOut> rs = byDept.get(dept);
            if (rs == null || rs.isEmpty()) continue;
            List<RowOut> ordered = orderRows(rs);
            String deptName = deptNameMap.getOrDefault(dept, dept);
            String sheetName = uniqueSheetName(sanitizeSheetName(deptName), used);
            used.add(sheetName);
            Sheet sh = wb.createSheet(sheetName);
            writeDeptSheet(sh, st, req, deptName, ordered, singleLevel);
        }

        String sumName = uniqueSheetName(sanitizeSheetName("總表"), used);
        used.add(sumName);

        List<RowOut> allOrdered = new ArrayList<RowOut>(rows);
        Collections.sort(allOrdered, new java.util.Comparator<RowOut>() {
            public int compare(RowOut a, RowOut b) {
                int c = nvlStr(a.deptCode, "").compareTo(nvlStr(b.deptCode, ""));
                if (c != 0) return c;
                return accHierKey(a).compareTo(accHierKey(b));
            }
        });

        Sheet sum = wb.createSheet(sumName);
        writeSummarySheet(sum, st, req, allOrdered);

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Export workbook failed", ex);
        }
    }

    private void writeDeptSheet(Sheet sh, Styles st, BudgetActualBudgetSummaryRequest req, String deptName, List<RowOut> rows, boolean singleLevel) {
        final int totalCols = 10; int r = 0;
        Row t1 = sh.createRow(r++); Cell tc1 = t1.createCell(0);
        tc1.setCellValue("財團法人聯合信用卡處理中心"); tc1.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

        Row t2 = sh.createRow(r++); Cell tc2 = t2.createCell(0);
        tc2.setCellValue("經費預算與實支彙總表"); tc2.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

        Row cond0 = sh.createRow(r++); cond0.createCell(0).setCellValue("部 門 別: " + deptName);
        Row cond1 = sh.createRow(r++); cond1.createCell(0).setCellValue("製表日期: " + formatRocDateToday());
        Row cond2 = sh.createRow(r++); cond2.createCell(0).setCellValue("期  間 : " + formatRocYmToCn(req.getStartYm()) + " ~ " + formatRocYmToCn(req.getEndYm()));
        r++;

        Row head = sh.createRow(r++); int c = 0;
        cell(head, c++, "會計科目代號", st.thRight);
        cell(head, c++, "會計科目名稱", st.thCenter);
        cell(head, c++, "原始預算", st.thRight);
        cell(head, c++, "保留預算", st.thRight);
        cell(head, c++, "勻入", st.thRight);
        cell(head, c++, "勻出", st.thRight);
        cell(head, c++, "可用預算", st.thRight);
        cell(head, c++, "占用數", st.thRight);
        cell(head, c++, "動用數", st.thRight);
        cell(head, c++, "帳列費用(AR11分攤前)", st.thRight);

        BigDecimal sOriginal = BigDecimal.ZERO, sKeep = BigDecimal.ZERO, sIn = BigDecimal.ZERO, sOut = BigDecimal.ZERO,
                sAvail = BigDecimal.ZERO, sOcc = BigDecimal.ZERO, sUse = BigDecimal.ZERO, sPosted = BigDecimal.ZERO;

        for (RowOut x : rows) {
            Row row = sh.createRow(r++); int j = 0;
            cell(row, j++, x.accCode, st.tdRight);
            cell(row, j++, nvlStr(x.accName, ""), st.td);
            num(row, j++, x.original, st.num);
            num(row, j++, x.keep, st.num);
            num(row, j++, x.inAmt, st.num);
            num(row, j++, x.outAmt, st.num);
            num(row, j++, x.available, st.num);
            num(row, j++, x.occupy, st.num);
            num(row, j++, x.useAmt, st.num);
            num(row, j++, x.posted, st.num);

            sOriginal = sOriginal.add(nvl(x.original));
            sKeep = sKeep.add(nvl(x.keep));
            sIn = sIn.add(nvl(x.inAmt));
            sOut = sOut.add(nvl(x.outAmt));
            sAvail = sAvail.add(nvl(x.available));
            sOcc = sOcc.add(nvl(x.occupy));
            sUse = sUse.add(nvl(x.useAmt));
            sPosted = sPosted.add(nvl(x.posted));
        }

        if (singleLevel) {
            Row total = sh.createRow(r++);
            cell(total, 0, deptName + " 小計", st.total);
            sh.addMergedRegion(new CellRangeAddress(total.getRowNum(), total.getRowNum(), 0, 1));
            int j = 2;
            num(total, j++, sOriginal, st.numBold);
            num(total, j++, sKeep, st.numBold);
            num(total, j++, sIn, st.numBold);
            num(total, j++, sOut, st.numBold);
            num(total, j++, sAvail, st.numBold);
            num(total, j++, sOcc, st.numBold);
            num(total, j++, sUse, st.numBold);
            num(total, j++, sPosted, st.numBold);
        }

        autosize(sh, totalCols);
    }

    private void writeSummarySheet(Sheet sh, Styles st, BudgetActualBudgetSummaryRequest req, List<RowOut> rows) {
        final int totalCols = 11; int r = 0;
        Row t1 = sh.createRow(r++); Cell tc1 = t1.createCell(0);
        tc1.setCellValue("財團法人聯合信用卡處理中心"); tc1.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

        Row t2 = sh.createRow(r++); Cell tc2 = t2.createCell(0);
        tc2.setCellValue("經費預算與實支彙總表"); tc2.setCellStyle(st.title);
        sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

        Row cond0 = sh.createRow(r++); cond0.createCell(0).setCellValue("部 門 別: 總表");
        Row cond1 = sh.createRow(r++); cond1.createCell(0).setCellValue("製表日期: " + formatRocDateToday());
        Row cond2 = sh.createRow(r++); cond2.createCell(0).setCellValue("期  間 : " + formatRocYmToCn(req.getStartYm()) + " ~ " + formatRocYmToCn(req.getEndYm()));
        r++;

        Row head = sh.createRow(r++); int c = 0;
        cell(head, c++, "部門", st.thCenter);
        cell(head, c++, "會計科目代號", st.thRight);
        cell(head, c++, "會計科目名稱", st.thCenter);
        cell(head, c++, "原始預算", st.thRight);
        cell(head, c++, "保留預算", st.thRight);
        cell(head, c++, "勻入", st.thRight);
        cell(head, c++, "勻出", st.thRight);
        cell(head, c++, "可用預算", st.thRight);
        cell(head, c++, "占用數", st.thRight);
        cell(head, c++, "動用數", st.thRight);
        cell(head, c++, "帳列費用(AR11分攤前)", st.thRight);

        for (RowOut x : rows) {
            Row row = sh.createRow(r++); int j = 0;
            cell(row, j++, nvlStr(x.deptName, x.deptCode), st.td);
            cell(row, j++, x.accCode, st.tdRight);
            cell(row, j++, nvlStr(x.accName, ""), st.td);
            num(row, j++, x.original, st.num);
            num(row, j++, x.keep, st.num);
            num(row, j++, x.inAmt, st.num);
            num(row, j++, x.outAmt, st.num);
            num(row, j++, x.available, st.num);
            num(row, j++, x.occupy, st.num);
            num(row, j++, x.useAmt, st.num);
            num(row, j++, x.posted, st.num);
        }

        autosize(sh, totalCols);
    }

    private List<RowOut> orderRows(List<RowOut> rows) {
        NavigableSet<String> lv1 = new TreeSet<String>();
        NavigableSet<String> lv4 = new TreeSet<String>();
        NavigableSet<String> lv6 = new TreeSet<String>();
        NavigableSet<String> lv8 = new TreeSet<String>();
        Map<String, RowOut> byCode = new LinkedHashMap<String, RowOut>();

        for (RowOut r : rows) {
            byCode.put(r.accCode, r);
            if (r.level == 1) lv1.add(r.accCode);
            else if (r.level == 4) lv4.add(r.accCode);
            else if (r.level == 6) lv6.add(r.accCode);
            else if (r.level == 8) lv8.add(r.accCode);

            if (r.accCode != null && r.accCode.length() >= 8) {
                lv1.add(r.accCode.substring(0, 1));
                lv4.add(r.accCode.substring(0, 4));
                lv6.add(r.accCode.substring(0, 6));
                lv8.add(r.accCode.substring(0, 8));
            }
        }

        List<RowOut> out = new ArrayList<RowOut>();
        for (String c1 : lv1) {
            appendIfPresent(out, byCode, c1);
            for (String c4 : lv4.tailSet(c1, true)) {
                if (!c4.startsWith(c1)) break;
                appendIfPresent(out, byCode, c4);
                for (String c6 : lv6.tailSet(c4, true)) {
                    if (!c6.startsWith(c4)) break;
                    appendIfPresent(out, byCode, c6);
                    for (String c8 : lv8.tailSet(c6, true)) {
                        if (!c8.startsWith(c6)) break;
                        appendIfPresent(out, byCode, c8);
                    }
                }
            }
        }
        return out;
    }

    private void appendIfPresent(List<RowOut> out, Map<String, RowOut> byCode, String code) {
        RowOut r = byCode.get(code);
        if (r != null) out.add(r);
    }

    private static class Styles {
        final CellStyle title, th, thCenter, thRight, td, tdRight, num, numBold, total;
        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();

            title = wb.createCellStyle();
            XSSFFont f1 = wb.createFont(); f1.setFontHeightInPoints((short) 14); f1.setBold(true);
            title.setFont(f1); title.setAlignment(HorizontalAlignment.CENTER);

            CellStyle border = wb.createCellStyle();
            border.setBorderBottom(BorderStyle.THIN);
            border.setBorderTop(BorderStyle.THIN);
            border.setBorderLeft(BorderStyle.THIN);
            border.setBorderRight(BorderStyle.THIN);

            th = wb.createCellStyle(); th.cloneStyleFrom(border);
            XSSFFont fth = wb.createFont(); fth.setBold(true); th.setFont(fth);
            thCenter = wb.createCellStyle(); thCenter.cloneStyleFrom(th); thCenter.setAlignment(HorizontalAlignment.CENTER);
            thRight = wb.createCellStyle(); thRight.cloneStyleFrom(th); thRight.setAlignment(HorizontalAlignment.RIGHT);

            td = wb.createCellStyle(); td.cloneStyleFrom(border);
            tdRight = wb.createCellStyle(); tdRight.cloneStyleFrom(td); tdRight.setAlignment(HorizontalAlignment.RIGHT);

            num = wb.createCellStyle(); num.cloneStyleFrom(tdRight); num.setDataFormat(fmt.getFormat("#,##0"));
            numBold = wb.createCellStyle(); numBold.cloneStyleFrom(num);
            XSSFFont fb = wb.createFont(); fb.setBold(true); numBold.setFont(fb);

            total = wb.createCellStyle(); total.cloneStyleFrom(thRight);
        }
    }

    private static void cell(Row row, int col, String v, CellStyle cs) { Cell c = row.createCell(col); c.setCellValue(v == null ? "" : v); c.setCellStyle(cs); }
    private static void num(Row row, int col, BigDecimal v, CellStyle cs) { Cell c = row.createCell(col); c.setCellValue(nvl(v).doubleValue()); c.setCellStyle(cs); }

    private static void autosize(Sheet sh, int cols) {
        for (int i = 0; i < cols; i++) { try { sh.autoSizeColumn(i, true); } catch (Exception ignore) { sh.autoSizeColumn(i); } }
        final double widenFactor = 1.60; final int excelMax = 255 * 256;
        for (int i = 0; i < cols; i++) {
            int width = sh.getColumnWidth(i);
            int widened = (int) Math.min(excelMax, Math.round(width * widenFactor));
            int minChars = (i == 0 || i == 2) ? 24 : (i == 1 ? 18 : 14);
            sh.setColumnWidth(i, Math.max(widened, minChars * 256));
        }
    }

    private static String sanitizeSheetName(String s) {
        String v = (s == null ? "sheet" : s).replaceAll("[\\\\/\\?\\*\\n\\r\\t\\[\\]:]", " ").trim();
        if (v.isEmpty()) v = "sheet";
        return v.length() > 31 ? v.substring(0, 31) : v;
    }

    private static String uniqueSheetName(String base, Set<String> used) {
        if (!used.contains(base)) return base;
        for (int i = 2; i < 1000; i++) {
            String c = base.length() > 28 ? base.substring(0, Math.max(1, 28)) : base;
            String s = c + "(" + i + ")";
            if (!used.contains(s)) return s;
        }
        return base + "_" + System.currentTimeMillis();
    }

    private static String formatRocDateToday() {
        LocalDate d = LocalDate.now();
        return String.format("%03d/%02d/%02d", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }

    private static String formatRocYmToCn(String yyyymm) {
        if (yyyymm == null || yyyymm.length() != 6) return yyyymm;
        int y = Integer.parseInt(yyyymm.substring(0, 4));
        int m = Integer.parseInt(yyyymm.substring(4, 6));
        return y + "年" + m + "月";
    }

    private static SortedSet<String> coveredYears(String startYm, String endYm) {
        YearMonth s = YearMonth.of(Integer.parseInt(startYm.substring(0, 4)), Integer.parseInt(startYm.substring(4, 6)));
        YearMonth e = YearMonth.of(Integer.parseInt(endYm.substring(0, 4)), Integer.parseInt(endYm.substring(4, 6)));
        SortedSet<String> ys = new TreeSet<String>();
        for (YearMonth cur = s; !cur.isAfter(e); cur = cur.plusMonths(1)) ys.add(String.valueOf(cur.getYear()));
        return ys;
    }

    private static String nvlStr(String v, String def) { return (v == null || v.isEmpty()) ? def : v; }
    private static String str(Object o) { return o == null ? null : String.valueOf(o); }
    private static BigDecimal nvl(BigDecimal x) { return x == null ? BigDecimal.ZERO : x; }

    private static String tryReadString(Object bean, String getterName) {
        try { java.lang.reflect.Method m = bean.getClass().getMethod(getterName); Object v = m.invoke(bean); return v == null ? null : String.valueOf(v); }
        catch (Exception ignore) { return null; }
    }

    private static String tryReadField(Object bean, String fieldName) {
        try { java.lang.reflect.Field f = bean.getClass().getDeclaredField(fieldName); f.setAccessible(true); Object v = f.get(bean); return v == null ? null : String.valueOf(v); }
        catch (Exception ignore) { return null; }
    }

    private String accHierKey(RowOut r) {
        String a = (r.accCode == null) ? "" : r.accCode;
        String p1 = a.length() >= 1 ? a.substring(0, 1) : "";
        String p4 = a.length() >= 4 ? a.substring(0, 4) : p1 + "\u0000\u0000\u0000";
        String p6 = a.length() >= 6 ? a.substring(0, 6) : p4 + "\u0000\u0000";
        String p8 = a.length() >= 8 ? a.substring(0, 8) : p6 + "\u0000\u0000";
        int levelOrder = (r.level == 1 ? 0 : r.level == 4 ? 1 : r.level == 6 ? 2 : 3);
        return String.format("%s|%s|%s|%s|%02d", p1, p4, p6, p8, levelOrder);
    }
}
