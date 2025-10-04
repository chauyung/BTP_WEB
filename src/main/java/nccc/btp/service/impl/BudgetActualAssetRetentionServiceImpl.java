package nccc.btp.service.impl;

import nccc.btp.dto.BudgetActualAssetRetentionRequest;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.entity.SyncUser;
import nccc.btp.repository.BpmPoMD1Repository;
import nccc.btp.repository.BpmPrMD1Repository;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.repository.NcccAccountingListRepository.AccName;
import nccc.btp.repository.NcccBudgetReserveRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.repository.SyncUserRepository;
import nccc.btp.service.BudgetActualAssetRetentionService;
import nccc.btp.service.NcccBudgetService;
import nccc.btp.vo.BudgetActualAssetRetentionRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BudgetActualAssetRetentionServiceImpl implements BudgetActualAssetRetentionService {

    private final NcccBudgetReserveRepository reserveRepo;
    private final BpmPoMD1Repository poRepo;
    private final BpmPrMD1Repository prRepo;
    private final SyncOURepository ouRepo;
    private final NcccAccountingListRepository accRepo;
    private final SyncUserRepository userRepo;

    @Autowired(required = false)
    private NcccBudgetService budgetService;

    public BudgetActualAssetRetentionServiceImpl(NcccBudgetReserveRepository reserveRepo,
                                                 BpmPoMD1Repository poRepo,
                                                 BpmPrMD1Repository prRepo,
                                                 SyncOURepository ouRepo,
                                                 NcccAccountingListRepository accRepo,
                                                 SyncUserRepository userRepo) {
        this.reserveRepo = reserveRepo;
        this.poRepo = poRepo;
        this.prRepo = prRepo;
        this.ouRepo = ouRepo;
        this.accRepo = accRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Optional<byte[]> exportExcelMaybe(BudgetActualAssetRetentionRequest req) {
        validate(req);

        String yearStr = trim(req.getYear());
        List<String> ouList = clean(req.getOuCodes()).stream()
                .map(String::toUpperCase).collect(Collectors.toList());
        boolean ouEmpty = ouList.isEmpty();

        // 1) 取保留金額聚合
        List<NcccBudgetReserveRepository.ReserveAgg> reserves =
                reserveRepo.findReserveAgg(yearStr, ouList, ouEmpty);
        if (reserves.isEmpty()) return Optional.empty();

        // key: (年度, 申請OU, 預算科目)
        class Key {
            final String y, o, a;
            Key(String y, String o, String a) { this.y = y; this.o = o; this.a = a; }
            @Override public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof Key)) return false;
                Key k = (Key) obj;
                return Objects.equals(y, k.y) && Objects.equals(o, k.o) && Objects.equals(a, k.a);
            }
            @Override public int hashCode() { return Objects.hash(y, o, a); }
        }

        Map<Key, BigDecimal> reserveMap = new HashMap<>();
        Set<String> ouSet = new LinkedHashSet<>();
        Set<String> accSet = new LinkedHashSet<>();

        for (NcccBudgetReserveRepository.ReserveAgg r : reserves) {
            Key k = new Key(nzStr(r.getYear()), nzStr(r.getOuCode()), nzStr(r.getAccounting()));
            reserveMap.put(k, nz(r.getAmt()));
            if (hasText(k.o)) ouSet.add(k.o);
            if (hasText(k.a)) accSet.add(k.a);
        }

        
        Map<Key, String> reqNoMap = new HashMap<>(); // 混合欄位（保留）
        Map<Key, String> purposeMap = new HashMap<>();
        Map<Key, String> handlerMap = new HashMap<>();
        for (NcccBudgetReserveRepository.ReqInfoAgg r :
                reserveRepo.findReqInfoAgg(yearStr, ouList, ouEmpty)) {
            Key k = new Key(nzStr(r.getYear()), nzStr(r.getOuCode()), nzStr(r.getAccounting()));
            reqNoMap.put(k, nzStr(r.getReqNo()));
            purposeMap.put(k, nzStr(r.getPurpose()));
            handlerMap.put(k, nzStr(r.getHandler()));
        }

        Map<Key, String> poNoMap = new HashMap<>();
        Map<Key, String> bargainMap = new HashMap<>();
        for (NcccBudgetReserveRepository.PoPickAgg r :
                reserveRepo.findPoPickAgg(yearStr, ouList, ouEmpty)) {
            Key k = new Key(nzStr(r.getYear()), nzStr(r.getOuCode()), nzStr(r.getAccounting()));
            poNoMap.put(k, nzStr(r.getPoNo()));
            bargainMap.put(k, nzStr(r.getBargainDocNo()));
        }

        Set<String> poNos = poNoMap.values().stream().filter(BudgetActualAssetRetentionServiceImpl::hasText)
                .map(BudgetActualAssetRetentionServiceImpl::std)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> prNos = new LinkedHashSet<>();
        for (Map.Entry<Key, String> e : reqNoMap.entrySet()) {
            Key k = e.getKey();
            String poNo = nzStr(poNoMap.get(k));
            String maybePr = nzStr(e.getValue());
            if (!hasText(poNo) && hasText(maybePr)) {
                prNos.add(std(maybePr));
            }
        }
        class PoAggWrap {
            final BigDecimal total, tax;
            final String ou;
            PoAggWrap(BigDecimal total, BigDecimal tax, String ou) { this.total = total; this.tax = tax; this.ou = ou; }
        }
        Map<String, PoAggWrap> poAggMap = new HashMap<>();
        Set<String> budgetOuFromPo = new LinkedHashSet<>();
        if (!poNos.isEmpty()) {
            for (BpmPoMD1Repository.PoAgg a : poRepo.findPoAgg(poNos)) {
                String poKey  = std(a.getPoNo());
                String accKey = std(a.getAccounting());
                String poOu   = nzStr(a.getOuCode());
                poAggMap.put(poKey + "|" + accKey, new PoAggWrap(nz(a.getBpTotalSum()), nz(a.getBpTaxSum()), poOu));
                if (hasText(poOu)) budgetOuFromPo.add(poOu);
            }
        }

        class PrAggWrap {
            final BigDecimal total;
            final String ou;
            PrAggWrap(BigDecimal t, String ou){ this.total = t; this.ou = ou; }
        }
        Map<String, PrAggWrap> prAggMap = new HashMap<>();
        Set<String> budgetOuFromPr = new LinkedHashSet<>();
        Set<String> accFromPr      = new LinkedHashSet<>();
        if (!prNos.isEmpty()) {
            for (BpmPrMD1Repository.PrAgg a : prRepo.findPrAgg(prNos)) {
                String prKey  = std(a.getPrNo());
                String accKey = std(a.getAccounting());
                String prOu   = nzStr(a.getOuCode());
                prAggMap.put(prKey + "|" + accKey, new PrAggWrap(nz(a.getTotalSum()), prOu));
                if (hasText(prOu)) budgetOuFromPr.add(prOu);
                if (hasText(accKey)) accFromPr.add(accKey);
            }
        }

        ouSet.addAll(budgetOuFromPo);
        ouSet.addAll(budgetOuFromPr);
        accSet.addAll(accFromPr);

        Map<String, String> ouName = loadDeptNames(ouSet); 
        Map<String, String> accountNames = loadAccountNames(accSet);
        Set<String> allHandlerRaw = new LinkedHashSet<>(handlerMap.values());
        Map<String, String> displayByAccount = loadHandlerDisplayNamesByAccount(allHandlerRaw);

        List<BudgetActualAssetRetentionRow> rows = new ArrayList<>();
        for (Map.Entry<Key, BigDecimal> e : reserveMap.entrySet()) {
            Key k = e.getKey();
            String purpose    = nzStr(purposeMap.get(k));
            String handlerRaw = nzStr(handlerMap.get(k));
            String poNoRaw    = nzStr(poNoMap.get(k));
            String reqNoRaw   = nzStr(reqNoMap.get(k)); 

            BigDecimal reserveAmt = e.getValue();
            BigDecimal purchaseAmt = null, taxAmt = null, total = null;
            String bargain = "";

            // 預算部門：預設用申請 OU（若有 PO/PR 明細 OU 再覆蓋）
            String budgetOuName = nameOfOu(ouName, k.o);
            String accKey = std(k.a);

            if (hasText(poNoRaw)) {
                PoAggWrap agg = poAggMap.get(std(poNoRaw) + "|" + accKey);
                if (agg != null) {
                    purchaseAmt = toNullIfZero(agg.total);
                    taxAmt = toNullIfZero(agg.tax);
                    total = (purchaseAmt == null && taxAmt == null) ? null : nz(agg.total).add(nz(agg.tax));
                    budgetOuName = nameOfOu(ouName, agg.ou); // 用 PO 明細 OU 覆蓋
                }
                bargain = nzStr(bargainMap.get(k)); // 有 PO 時才顯示議價文號
            } else if (hasText(reqNoRaw)) {
            	PrAggWrap agg = prAggMap.get(std(reqNoRaw) + "|" + accKey);
                if (agg != null) {
                	reserveAmt = toNullIfZero(agg.total);  // 預算保留金額 = PR TOTAL
                    taxAmt = BigDecimal.ZERO;              // 稅額 = 0
                    total  = BigDecimal.ZERO;              // 採購總額 = 0
                    budgetOuName = nameOfOu(ouName, agg.ou); // 用 PR 明細 OU 覆蓋
                }
            }

            // 負責經辦部門 = 申請 OU（需求）
            String handlerOuName = nameOfOu(ouName, k.o);

            String handlerAlias = nzStr(displayByAccount.get(handlerRaw));
            if (handlerAlias.isEmpty()) handlerAlias = aliasOf(handlerRaw);

            BudgetActualAssetRetentionRow row = new BudgetActualAssetRetentionRow();
            row.year = k.y;
            row.ouCode = k.o;            
            row.deptName = budgetOuName; 
            row.accounting = k.a;
            row.accountingName = nzStr(accountNames.get(k.a));
            row.reqNo = reqNoRaw;                
            row.poNo = poNoRaw;
            row.purpose = purpose;
            row.reserveAmt = reserveAmt;
            row.purchaseAmt = purchaseAmt;
            row.taxAmt = taxAmt;
            row.purchaseTotal = total;
            row.bargainDocNo = bargain;
            row.handler = handlerAlias;
            row.handlerDept = handlerOuName;
            rows.add(row);
        }

        // 依「預算部門代號 → 預算科目代號」排序
        rows.sort((x, y) -> {
            int c1 = nzStr(x.ouCode).compareTo(nzStr(y.ouCode));
            if (c1 != 0) return c1;
            return nzStr(x.accounting).compareTo(nzStr(y.accounting));
        });

        return Optional.of(buildExcel(req, rows));
    }

    // ======================= 名稱/顯示名載入 =======================

    private Map<String, String> loadHandlerDisplayNamesByAccount(Collection<String> handlerRawValues) {
        Map<String, String> result = new HashMap<>();
        if (handlerRawValues == null || handlerRawValues.isEmpty()) return result;

        for (String acc : handlerRawValues) {
            if (!hasText(acc)) continue;
            String disp = null;

            // 1) 原樣
            SyncUser u = userRepo.findByAccount(acc);
            if (u != null && hasText(u.getDisplayName())) disp = u.getDisplayName().trim();

            // 2) 小寫
            if (disp == null) {
                String lower = acc.toLowerCase();
                if (!lower.equals(acc)) {
                    u = userRepo.findByAccount(lower);
                    if (u != null && hasText(u.getDisplayName())) disp = u.getDisplayName().trim();
                }
            }

            // 3) 大寫
            if (disp == null) {
                String upper = acc.toUpperCase();
                if (!upper.equals(acc)) {
                    u = userRepo.findByAccount(upper);
                    if (u != null && hasText(u.getDisplayName())) disp = u.getDisplayName().trim();
                }
            }

            if (disp != null) result.put(acc, disp);
        }
        return result;
    }

    private Map<String, String> loadDeptNames(Set<String> ouCodes) {
        Map<String, String> names = new LinkedHashMap<>();
        if (ouCodes == null || ouCodes.isEmpty()) return names;

        // 標準化：去空白 + 大寫 + 去重
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
                            code = c == null ? null : String.valueOf(c);
                            name = n == null ? ""  : String.valueOf(n);
                        }
                        if (code != null) {
                            String key = code.trim().toUpperCase();
                            if (keys.contains(key)) {
                                if (name != null && !name.trim().isEmpty()) {
                                    names.putIfAbsent(key, name);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) { }

        // 無論 API 有沒有資料，都把「缺的」用 DB 補齊
        Set<String> missing = new LinkedHashSet<>(keys);
        missing.removeAll(names.keySet());
        if (!missing.isEmpty()) {
            List<Object[]> list = ouRepo.findNames(missing); // 保持原 Repo 簽名
            for (Object[] r : list) {
                String code = r[0] == null ? null : String.valueOf(r[0]);
                String name = r[1] == null ? ""  : String.valueOf(r[1]);
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

    private Map<String, String> loadAccountNames(Set<String> accountCodes) {
        Map<String, String> names = new LinkedHashMap<>();
        if (accountCodes == null || accountCodes.isEmpty()) return names;

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getBudgetAccount();
                if (resp != null && resp.getData() instanceof List) {
                    List<?> rows = (List<?>) resp.getData();
                    for (Object row : rows) {
                        if (!(row instanceof Map)) continue;
                        Map<?, ?> m = (Map<?, ?>) row;
                        Object c = m.get("accounting"); if (c == null) c = m.get("ACCOUNTING");
                        if (c == null) c = m.get("accountCode");
                        if (c == null) c = m.get("ACCOUNT_CODE");
                        if (c == null) c = m.get("code");
                        Object n = m.get("accountingName"); if (n == null) n = m.get("ACCOUNTING_NAME");
                        if (n == null) n = m.get("name"); if (n == null) n = m.get("NAME");
                        String code = c == null ? null : String.valueOf(c);
                        String name = n == null ? "" : String.valueOf(n);
                        if (code != null && accountCodes.contains(code)) names.put(code, name);
                    }
                }
            }
        } catch (Exception ignore) { }

        if (names.isEmpty()) {
            List<AccName> list = accRepo.findNames(accountCodes);
            for (AccName a : list) {
                String code = a.getSubject();
                String name = a.getName();
                if (code != null) names.put(code, name == null ? "" : name);
            }
        }
        return names;
    }

    // ======================= Excel =======================

    private byte[] buildExcel(BudgetActualAssetRetentionRequest req, List<BudgetActualAssetRetentionRow> rows) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            DataFormat fmt = wb.createDataFormat();

            // 標題樣式
            CellStyle title = wb.createCellStyle();
            Font f1 = wb.createFont(); f1.setBold(true); f1.setFontHeightInPoints((short) 14);
            title.setFont(f1); title.setAlignment(HorizontalAlignment.CENTER);

            Sheet sh = wb.createSheet(sanitizeSheetName("資產保留總表"));
            int r = 0, totalCols = 14;

            Row t1 = sh.createRow(r++); cell(t1, 0, "財團法人聯合信用卡處理中心", title);
            sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));
            Row t2 = sh.createRow(r++); cell(t2, 0, "資產保留總表", title);
            sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

            r++;
            Row h = sh.createRow(r++);
            String[] heads = {"預算年度","預算部門","預算代號","預算科目名稱",
                    "請購單單號","採購單單號","請購目的",
                    "預算保留金額","採購金額","稅額","採購總額",
                    "議價文號","負責經辦","負責經辦部門"};
            for (int i = 0; i < heads.length; i++) h.createCell(i).setCellValue(heads[i]);

            // 只合計：預算保留金額
            BigDecimal sumReserve = BigDecimal.ZERO;

            CellStyle num = wb.createCellStyle();
            num.setDataFormat(fmt.getFormat("#,##0"));

            for (BudgetActualAssetRetentionRow x : rows) {
                Row row = sh.createRow(r++);
                int i = 0;
                row.createCell(i++).setCellValue(nzStr(x.year));
                row.createCell(i++).setCellValue(nzStr(x.deptName));
                row.createCell(i++).setCellValue(nzStr(x.accounting));
                row.createCell(i++).setCellValue(nzStr(x.accountingName));
                row.createCell(i++).setCellValue(nzStr(x.reqNo));           
                row.createCell(i++).setCellValue(nzStr(x.poNo));
                row.createCell(i++).setCellValue(nzStr(x.purpose));
                number(row, i++, x.reserveAmt, num);
                number(row, i++, x.purchaseAmt, num);
                number(row, i++, x.taxAmt, num);
                number(row, i++, x.purchaseTotal, num);
                row.createCell(i++).setCellValue(nzStr(x.bargainDocNo));
                row.createCell(i++).setCellValue(nzStr(x.handler));
                row.createCell(i++).setCellValue(nzStr(x.handlerDept));

                if (x.reserveAmt != null) sumReserve = sumReserve.add(x.reserveAmt);
            }

            Row ft = sh.createRow(r++);
            ft.createCell(0).setCellValue("合計");
            number(ft, 7, sumReserve, num);  // 只合計預算保留金額

            int[] widths = {10, 18, 12, 24, 16, 16, 32, 14, 14, 12, 14, 16, 14, 20};
            for (int k = 0; k < widths.length; k++) sh.setColumnWidth(k, widths[k] * 256);

            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("匯出失敗: " + e.getMessage(), e);
        }
    }

    private static void cell(Row r, int c, String v, CellStyle st) {
        Cell x = r.createCell(c);
        x.setCellValue(v == null ? "" : v);
        x.setCellStyle(st);
    }
    private static void number(Row r, int c, BigDecimal v, CellStyle st) {
        Cell x = r.createCell(c);
        if (v == null) x.setBlank(); else x.setCellValue(v.doubleValue());
        x.setCellStyle(st);
    }


    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static List<String> clean(List<String> src) {
        if (src == null) return Collections.emptyList();
        return src.stream().filter(Objects::nonNull)
                .map(String::trim).filter(v -> !v.isEmpty())
                .distinct().collect(Collectors.toList());
    }
    private static String nzStr(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
    private static BigDecimal toNullIfZero(BigDecimal v) { return (v == null || v.compareTo(BigDecimal.ZERO) == 0) ? null : v; }
    private static boolean hasText(String s) { return s != null && !s.trim().isEmpty(); }
    private static String std(String s) { return s == null ? "" : s.trim().toUpperCase(); }

    private void validate(BudgetActualAssetRetentionRequest req) {
        String y = trim(req.getYear());
        if (!StringUtils.hasText(y) || !y.matches("\\d{4}"))
            throw new IllegalArgumentException("年度為必填，且需為西元 4 碼。");
        if (Integer.parseInt(y) > 2099)
            throw new IllegalArgumentException("年度不可大於 2099。");
    }

    private static String sanitizeSheetName(String s){
        return (s == null || s.isEmpty()) ? "sheet" : s.replaceAll("[\\\\/\\?\\*\\n\\r\\t\\[\\]:]", " ");
    }

    private static String aliasOf(String s) {
        String v = nzStr(s);
        if (v.isEmpty()) return v;
        if (containsHan(v)) return v;
        int p1 = v.indexOf('('), p2 = v.indexOf(')');
        if (p1 >= 0 && p2 > p1) {
            String in = v.substring(p1 + 1, p2).trim();
            if (!in.isEmpty()) return in;
        }
        int at = v.indexOf('@');
        if (at > 0) v = v.substring(0, at);
        v = takeLastSegment(v);
        return v;
    }
    private static boolean containsHan(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.UnicodeScript.of(s.charAt(i)) == Character.UnicodeScript.HAN) return true;
        }
        return false;
    }
    private static String takeLastSegment(String s) {
        int cut = Math.max(s.lastIndexOf('\\'), Math.max(s.lastIndexOf('/'), s.lastIndexOf('|')));
        return (cut >= 0 && cut + 1 < s.length()) ? s.substring(cut + 1) : s;
    }

    private static String nameOfOu(Map<String, String> ouNameMap, String code) {
        if (code == null) return "";
        String key = code.trim().toUpperCase();
        String n = ouNameMap.get(key);
        return (n == null || n.trim().isEmpty()) ? key : n.trim();
    }
}
