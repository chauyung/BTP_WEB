package nccc.btp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualUsageDetailRequest;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.repository.BudgetActualUsageDetailRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.service.BudgetActualUsageDetailService;
import nccc.btp.service.NcccBudgetService;
import nccc.btp.vo.BudgetActualUsageDetailVo.TxnRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetActualUsageDetailServiceImpl implements BudgetActualUsageDetailService {

    private final BudgetActualUsageDetailRepository repository;
    private final SyncOURepository syncOURepository;
    private NcccBudgetService budgetService;
    @Override
    public Optional<byte[]> exportExcelMaybe(BudgetActualUsageDetailRequest req) {
        validate(req);

        boolean deptEmpty = CollectionUtils.isEmpty(req.getDeptCodes());
        String accFrom = StringUtils.hasText(req.getAccountFrom()) ? req.getAccountFrom() : null;
        String accTo = StringUtils.hasText(req.getAccountTo()) ? req.getAccountTo() : null;

        YearMonth startYm = YearMonth.of(
                Integer.parseInt(req.getStartYm().substring(0, 4)),
                Integer.parseInt(req.getStartYm().substring(4, 6)));
        YearMonth endYm = YearMonth.of(
                Integer.parseInt(req.getEndYm().substring(0, 4)),
                Integer.parseInt(req.getEndYm().substring(4, 6)));

        LocalDate startDate = startYm.atDay(1);
        LocalDate endDate = endYm.atEndOfMonth();
        String year = String.valueOf(startYm.getYear());

        List<TxnRow> rows = repository.findTxnRows(
                year, req.getVersion(),
                deptEmpty,
                deptEmpty ? Collections.<String>emptyList() : req.getDeptCodes(),
                accFrom, accTo,
                startDate, endDate
        );

        boolean noData = rows.isEmpty();
        if (noData) {
            return Optional.empty();
        }
	        
        return Optional.of(buildExcel(rows));
    }
    
    private byte[] buildExcel(List<TxnRow> list) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Set<String> ouCodes = (list == null) ? Collections.emptySet()
                    : list.stream().map(TxnRow::getOuCode).filter(s -> s != null && !s.isEmpty())
                          .collect(Collectors.toCollection(LinkedHashSet::new));
            Map<String,String> deptNameMap = loadDeptNames(ouCodes);

            DataFormat fmt = wb.createDataFormat();

            CellStyle title = wb.createCellStyle();
            Font fTitle = wb.createFont();
            fTitle.setBold(true); fTitle.setFontHeightInPoints((short)14);
            title.setFont(fTitle);
            title.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            title.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

            CellStyle info = wb.createCellStyle();
            Font fInfo = wb.createFont();
            fInfo.setBold(true);
            info.setFont(fInfo);
            info.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);

            CellStyle th   = border(wb);
            Font boldFont  = wb.createFont(); boldFont.setBold(true);
            th.setFont(boldFont);
            th.setAlignment(HorizontalAlignment.CENTER);
            th.setVerticalAlignment(VerticalAlignment.CENTER);
            th.setWrapText(true);

            CellStyle td      = border(wb);
            CellStyle tdC     = border(wb); tdC.setAlignment(HorizontalAlignment.CENTER);
            CellStyle tdBold  = border(wb); tdBold.setFont(boldFont);
            CellStyle tdBoldC = border(wb); tdBoldC.setFont(boldFont); tdBoldC.setAlignment(HorizontalAlignment.CENTER);
            CellStyle num     = border(wb); num.setAlignment(HorizontalAlignment.RIGHT); num.setDataFormat(fmt.getFormat("#,##0"));

            Sheet sheet = wb.createSheet("預算使用明細表");
            final int totalCols = 12;
            int[] widths = {14,14,20,14,12,12,14,36,20,16,18,28};
            for (int i = 0; i < widths.length; i++) sheet.setColumnWidth(i, widths[i] * 256);

            int r = 0;

            Row t1 = sheet.createRow(r++); t1.setHeightInPoints(22);
            set(t1, 0, "財團法人聯合信用卡處理中心", title);
            sheet.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

            Row t2 = sheet.createRow(r++); t2.setHeightInPoints(20);
            set(t2, 0, "預算使用明細表", title);
            sheet.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            Row t3 = sheet.createRow(r++);
            set(t3, 0, "製表日期：" + LocalDate.now().format(df), info);
            sheet.addMergedRegion(new CellRangeAddress(t3.getRowNum(), t3.getRowNum(), 0, totalCols - 1));

            r++;

            Map<String, List<TxnRow>> byBudget = new LinkedHashMap<>();
            if (list != null && !list.isEmpty()) {
                Collections.sort(list,
                        Comparator.comparing(TxnRow::getOuCode, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(TxnRow::getAccounting, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(TxnRow::getTranscationDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(TxnRow::getTranscationNo, Comparator.nullsFirst(Comparator.naturalOrder()))
                );
                for (TxnRow t : list) {
                    String key = n(t.getYear()) + "|" + n(t.getVersion()) + "|" + n(t.getOuCode()) + "|" + n(t.getAccounting());
                    byBudget.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
                }
            }

            final boolean multiDept = ouCodes.size() > 1;
            boolean headerPrinted = false;   // <== 新增：單一部門時只印一次表頭
            String lastOu = null;
            BigDecimal deptTotal = BigDecimal.ZERO;

            for (Map.Entry<String, List<TxnRow>> e : byBudget.entrySet()) {
                List<TxnRow> rows = e.getValue();
                if (rows == null || rows.isEmpty()) continue;

                TxnRow head = rows.get(0);
                String currOu = n(head.getOuCode());

                if (lastOu != null && !lastOu.equals(currOu)) {
                    Row subtotal = getOrCreateRow(sheet, r++);
                    for (int i = 0; i < 3; i++) set(subtotal, i, "", tdBold);
                    set(subtotal, 3, "小計：", tdBold);     // 第4欄
                    set(subtotal, 4, "", tdBoldC);
                    setNumber(subtotal, 5, deptTotal, num); // 金額在第6欄
                    for (int i = 6; i <= 11; i++) set(subtotal, i, "", tdBold);
                    deptTotal = BigDecimal.ZERO;

                    r++;
                }

                if (multiDept && (lastOu == null || !lastOu.equals(currOu))) {
                    Row deptRow = getOrCreateRow(sheet, r++);
                    String code = currOu;
                    String name = deptNameMap.getOrDefault(code, "");
                    set(deptRow, 0, "部門：" + code + (name.isEmpty() ? "" : " " + name), info);
                    sheet.addMergedRegion(new CellRangeAddress(deptRow.getRowNum(), deptRow.getRowNum(), 0, totalCols - 1));

                    Row h1 = getOrCreateRow(sheet, r++);
                    String[] H1 = {"預算代號","交易日期","交易單號","原預算","交易類別","金額","預算餘額","摘要","簽呈號碼","來源名稱","廠商代號","廠商名稱"};
                    for (int i = 0; i < H1.length; i++) set(h1, i, H1[i], th);

                    Row h2 = getOrCreateRow(sheet, r++);
                    String[] H2 = {"預算名稱","","","","","","","備註","","","",""};
                    for (int i = 0; i < H2.length; i++) set(h2, i, H2[i], th);

                    headerPrinted = true; // 這個部門已印表頭
                }

                if (!multiDept && !headerPrinted) {
                	Row deptRow = getOrCreateRow(sheet, r++);
                    String code = currOu;
                    String name = deptNameMap.getOrDefault(code, "");
                    set(deptRow, 0, "部門：" + code + (name.isEmpty() ? "" : " " + name), info);
                    sheet.addMergedRegion(new CellRangeAddress(deptRow.getRowNum(), deptRow.getRowNum(), 0, totalCols - 1));
                    
                    Row h1 = getOrCreateRow(sheet, r++);
                    String[] H1 = {"預算代號","交易日期","交易單號","原預算","交易類別","金額","預算餘額","摘要","簽呈號碼","來源名稱","廠商代號","廠商名稱"};
                    for (int i = 0; i < H1.length; i++) set(h1, i, H1[i], th);

                    Row h2 = getOrCreateRow(sheet, r++);
                    String[] H2 = {"預算名稱","","","","","","","備註","","","",""};
                    for (int i = 0; i < H2.length; i++) set(h2, i, H2[i], th);

                    headerPrinted = true;
                }

                lastOu = currOu;

                BigDecimal remain = safe(head.getOriginalBudget())
                        .add(safe(head.getReserveBudget()))
                        .add(safe(head.getAllocIncreaseAmt()))
                        .subtract(safe(head.getAllocReduseAmt()))
                        .subtract(safe(head.getOccupyAmt()))
                        .subtract(safe(head.getUseAmt()));

                Row mr = getOrCreateRow(sheet, r++);
                int c = 0;
                set(mr, c++, n(head.getAccounting()), tdBold);
                set(mr, c++, n(head.getAccountingName()), tdBold); // 會科中文（由查詢帶入）
                set(mr, c++, "", tdBold);
                setNumber(mr, c++, safe(head.getOriginalBudget()).add(safe(head.getReserveBudget())), num);
                set(mr, c++, "", tdBoldC);
                set(mr, c++, "", num);
                setNumber(mr, c++, remain, num);
                set(mr, c++, "", tdBold);
                set(mr, c++, "", tdBold);
                set(mr, c++, "", tdBold);
                set(mr, c++, "", tdBold);
                set(mr, c++, "", tdBold);

                for (TxnRow t : rows) {
                    if (t.getTranscationDate() == null && t.getTranscationNo() == null && t.getAmount() == null) {
                        continue;
                    }
                    Row tr = getOrCreateRow(sheet, r++);
                    int cc = 0;
                    set(tr, cc++, "　", td);
                    set(tr, cc++, date(t.getTranscationDate()), tdC);
                    set(tr, cc++, n(t.getTranscationNo()), td);
                    set(tr, cc++, "", td);
                    set(tr, cc++, n(t.getTranscationType()), tdC);
                    setNumber(tr, cc++, t.getAmount(), num);
                    set(tr, cc++, "", num);
                    set(tr, cc++, "", td);
                    set(tr, cc++, n(t.getDocNo()), td);
                    set(tr, cc++, n(t.getTranscationSource()), td);
                    set(tr, cc++, n(t.getBpNo()), td);
                    set(tr, cc++, n(t.getBpName()), td);

                    if (t.getAmount() != null) {
                        deptTotal = deptTotal.add(t.getAmount().abs());
                    }
                }
            }

            if (lastOu != null) {
                Row subtotal = getOrCreateRow(sheet, r++);
                for (int i = 0; i < 3; i++) set(subtotal, i, "", tdBold);
                set(subtotal, 3, "小計：", tdBold);
                set(subtotal, 4, "", tdBoldC);
                setNumber(subtotal, 5, deptTotal, num);
                for (int i = 6; i <= 11; i++) set(subtotal, i, "", tdBold);
            }

            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("匯出失敗", e);
        }
    }

    
    private Map<String, String> loadDeptNames(Set<String> ouCodes) {
        Map<String, String> names = new LinkedHashMap<String, String>();
        if (ouCodes == null || ouCodes.isEmpty()) return names;

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getSourceDepartment();
                if (resp != null && resp.getData() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<?> rows = (List<?>) resp.getData();
                    for (Object row : rows) {
                        String code = null;
                        String name = "";
                        if (row instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) row;
                            Object c = m.get("code");
                            if (c == null) c = m.get("CODE");
                            Object n = m.get("name");
                            if (n == null) n = m.get("NAME");
                            code = c == null ? null : String.valueOf(c);
                            name = n == null ? "" : String.valueOf(n);
                        }
                        if (code != null && ouCodes.contains(code)) {
                            names.put(code, name);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }

        if (names.isEmpty()) {
        	List<Object[]> list = syncOURepository.findNames(ouCodes);
        	for (Object[] r : list) {
        	    String code = r[0] == null ? null : String.valueOf(r[0]);
        	    String name = r[1] == null ? "" : String.valueOf(r[1]);
        	    if (code != null) names.put(code, name);
        	}
        }
        return names;
    }

    private static void setNumber(Row r, int c, BigDecimal v, CellStyle st){
        Cell cell = r.createCell(c); if (st != null) cell.setCellStyle(st);
        cell.setCellValue(v == null ? 0d : v.doubleValue());
    }
    private static String n(String s){ return s==null? "":s; }
    private static String date(java.time.LocalDate d){ return d==null? "": d.toString().replace("-","/"); }
    private static java.math.BigDecimal safe(java.math.BigDecimal v){ return v==null?java.math.BigDecimal.ZERO:v; }
    private static CellStyle border(Workbook wb){
        CellStyle s = wb.createCellStyle();
        s.setBorderBottom(BorderStyle.THIN); s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);  s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private void validate(BudgetActualUsageDetailRequest req) {
        if (req == null) throw new IllegalArgumentException("請求不可為空");
        if (!StringUtils.hasText(req.getStartYm()) || !StringUtils.hasText(req.getEndYm()))
            throw new IllegalArgumentException("年月起訖為必填");
        if (!req.getStartYm().matches("^[0-9]{6}$") || !req.getEndYm().matches("^[0-9]{6}$"))
            throw new IllegalArgumentException("年月請輸入YYYYMM六碼");
        if (!req.getStartYm().substring(0, 4).equals(req.getEndYm().substring(0, 4)))
            throw new IllegalArgumentException("年月起訖需同年度");
        if (req.getStartYm().compareTo(req.getEndYm()) > 0)
            throw new IllegalArgumentException("年月起值不可大於迄值");
        if (StringUtils.hasText(req.getAccountFrom()) && StringUtils.hasText(req.getAccountTo())
                && req.getAccountFrom().compareTo(req.getAccountTo()) > 0)
            throw new IllegalArgumentException("預算科目代號起值不可大於迄值");
        if (!StringUtils.hasText(req.getVersion())) throw new IllegalArgumentException("版次為必填");
        if (!req.getVersion().matches("^[0-9]{1}$")) throw new IllegalArgumentException("版次僅能輸入0-9的一位數字");
    }

    private static Row getOrCreateRow(Sheet s, int i){ return s.getRow(i)!=null?s.getRow(i):s.createRow(i); }
    private static void set(Row r, int c, String v, CellStyle st){ Cell cell=r.createCell(c); cell.setCellStyle(st); cell.setCellValue(v==null?"":v); }
   
}
