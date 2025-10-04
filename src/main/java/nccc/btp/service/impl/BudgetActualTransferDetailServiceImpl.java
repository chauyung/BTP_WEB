package nccc.btp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetResponse;
import nccc.btp.dto.BudgetActualTransferDetailRequest;
import nccc.btp.dto.TransferRowTuple;
import nccc.btp.repository.NcccAccountingListRepository;
import nccc.btp.repository.NcccAccountingListRepository.AccName;
import nccc.btp.repository.NcccBudgetAdjustQueryRepository;
import nccc.btp.repository.SyncOURepository;
import nccc.btp.service.BudgetActualTransferDetailService;
import nccc.btp.service.NcccBudgetService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetActualTransferDetailServiceImpl implements BudgetActualTransferDetailService {

    private final NcccBudgetAdjustQueryRepository queryRepository;
    private final SyncOURepository syncOURepository;
    private final NcccAccountingListRepository accountingListRepository;

    @Autowired(required = false)
    private NcccBudgetService budgetService;

    @Override
    public Optional<byte[]> exportExcelMaybe(BudgetActualTransferDetailRequest req) {
        ensureAtLeastOneCondition(req);
        validateRanges(req);

        LocalDate dateStart = StringUtils.hasText(req.getTransferDateStart()) ? LocalDate.parse(req.getTransferDateStart()) : null;
        LocalDate dateEnd = StringUtils.hasText(req.getTransferDateEnd()) ? LocalDate.parse(req.getTransferDateEnd()) : null;
        List<String> departments = CollectionUtils.isEmpty(req.getDeptCodes()) ? null : req.getDeptCodes();
        String biStart = StringUtils.hasText(req.getBudgetItemCodeStart()) ? req.getBudgetItemCodeStart() : null;
        String biEnd = StringUtils.hasText(req.getBudgetItemCodeEnd()) ? req.getBudgetItemCodeEnd() : null;

        List<TransferRowTuple> rows = new ArrayList<>(queryRepository.search(
                n(req.getYear()),
                n(req.getVersion()),
                dateStart,
                dateEnd,
                departments,
                biStart,
                biEnd,
                n(req.getTransferOrderNo())
        ));

        applySequencePerOrder(rows);

        Set<String> ouCodes = new LinkedHashSet<>();
        Set<String> accCodes = new LinkedHashSet<>();
        for (TransferRowTuple r : rows) {
            if (StringUtils.hasText(r.getFromDeptCode())) ouCodes.add(r.getFromDeptCode());
            if (StringUtils.hasText(r.getToDeptCode())) ouCodes.add(r.getToDeptCode());
            if (StringUtils.hasText(r.getOutAccounting())) accCodes.add(r.getOutAccounting());
            if (StringUtils.hasText(r.getInAccounting())) accCodes.add(r.getInAccounting());
        }

        Map<String, String> deptNames = loadDeptNames(ouCodes);
        Map<String, String> accountNames = loadAccountNames(accCodes);

        if (rows.isEmpty()) return Optional.empty();

        return Optional.of(buildExcel(rows, req, deptNames, accountNames));
    }

    private String n(String s) {
        return StringUtils.hasText(s) ? s : null;
    }

    private void ensureAtLeastOneCondition(BudgetActualTransferDetailRequest req) {
        boolean hasAny =
                StringUtils.hasText(req.getYear()) ||
                StringUtils.hasText(req.getVersion()) ||
                StringUtils.hasText(req.getTransferDateStart()) ||
                StringUtils.hasText(req.getTransferDateEnd()) ||
                (req.getDeptCodes() != null && !req.getDeptCodes().isEmpty()) ||
                StringUtils.hasText(req.getBudgetItemCodeStart()) ||
                StringUtils.hasText(req.getBudgetItemCodeEnd()) ||
                StringUtils.hasText(req.getTransferOrderNo());
        if (!hasAny) throw new IllegalArgumentException("請至少輸入一組查詢條件");
    }

    private void validateRanges(BudgetActualTransferDetailRequest req) {
        if (StringUtils.hasText(req.getTransferDateStart()) &&
            StringUtils.hasText(req.getTransferDateEnd()) &&
            req.getTransferDateStart().compareTo(req.getTransferDateEnd()) > 0) {
            throw new IllegalArgumentException("調撥日期起不得大於迄");
        }
        if (StringUtils.hasText(req.getBudgetItemCodeStart()) &&
            StringUtils.hasText(req.getBudgetItemCodeEnd()) &&
            req.getBudgetItemCodeStart().compareTo(req.getBudgetItemCodeEnd()) > 0) {
            throw new IllegalArgumentException("科目代號區間起值不可大於迄值");
        }
    }

    private void applySequencePerOrder(List<TransferRowTuple> rows) {
        Map<String, Integer> seqMap = new LinkedHashMap<>();
        for (TransferRowTuple r : rows) {
            String key = r.getTransferOrderNo() == null ? "" : r.getTransferOrderNo();
            int next = seqMap.getOrDefault(key, 0) + 1;
            seqMap.put(key, next);
            r.setSeq(next);
        }
    }

    private Map<String, String> loadDeptNames(Set<String> ouCodes) {
        Map<String, String> names = new LinkedHashMap<>();
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
                        if (code != null && ouCodes.contains(code)) names.put(code, name);
                    }
                }
            }
        } catch (Exception ignore) { }

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

    private Map<String, String> loadAccountNames(Set<String> accountCodes) {
        Map<String, String> names = new LinkedHashMap<>();
        if (accountCodes == null || accountCodes.isEmpty()) return names;

        try {
            if (budgetService != null) {
                BudgetResponse resp = budgetService.getBudgetAccount();
                if (resp != null && resp.getData() instanceof List) {
                    List<?> rows = (List<?>) resp.getData();
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

        if (names.isEmpty()) {
            // 批次查避免 ORA-01795
            final int BATCH = 900;
            List<String> codes = new ArrayList<>(accountCodes);
            List<AccName> list = new ArrayList<>();
            for (int i = 0; i < codes.size(); i += BATCH) {
                List<String> sub = codes.subList(i, Math.min(i + BATCH, codes.size()));
                list.addAll(accountingListRepository.findNames(new LinkedHashSet<>(sub)));
            }
            for (AccName a : list) {
                String code = a.getSubject();
                String name = a.getName();
                if (code != null) names.put(code, name == null ? "" : name);
            }
        }
        return names;
    }

    private byte[] buildExcel(List<TransferRowTuple> rows,
                              BudgetActualTransferDetailRequest req,
                              Map<String, String> deptNames,
                              Map<String, String> accountNames) {
        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            Sheet sh = wb.createSheet("預算調撥明細表");
            Styles st = new Styles(wb);
            int totalCols = 13; // 0..12
            int rowIdx = 0;

            Row t1 = sh.createRow(rowIdx++);
            Cell tc1 = t1.createCell(0);
            tc1.setCellValue("財團法人聯合信用卡處理中心");
            tc1.setCellStyle(st.title);
            sh.addMergedRegion(new CellRangeAddress(t1.getRowNum(), t1.getRowNum(), 0, totalCols - 1));

            Row t2 = sh.createRow(rowIdx++);
            Cell tc2 = t2.createCell(0);
            tc2.setCellValue("預算調撥明細表");
            tc2.setCellStyle(st.title);
            sh.addMergedRegion(new CellRangeAddress(t2.getRowNum(), t2.getRowNum(), 0, totalCols - 1));

            Row cond0 = sh.createRow(rowIdx++);
            cond0.createCell(0).setCellValue("部 門 別: " + buildDeptText(req.getDeptCodes()));
            Row cond1 = sh.createRow(rowIdx++);
            cond1.createCell(0).setCellValue("製表日期: " + formatRocDateToday());
            Row cond2 = sh.createRow(rowIdx++);
            cond2.createCell(0).setCellValue("調撥日期 : " + buildRocPeriod(req.getTransferDateStart(), req.getTransferDateEnd()));
            rowIdx++;

            Row head = sh.createRow(rowIdx++);
            int c = 0;
            writeHead(head, c++, "年度", st.th);
            writeHead(head, c++, "版次", st.th);
            writeHead(head, c++, "單號", st.th);
            writeHead(head, c++, "序號", st.th);
            writeHead(head, c++, "勻出部門", st.th);
            writeHead(head, c++, "調撥日期", st.th);
            writeHead(head, c++, "勻出部門預算項目代號", st.thRight);
            writeHead(head, c++, "勻出部門預算項目名稱", st.th);
            writeHead(head, c++, "勻入部門", st.th);
            writeHead(head, c++, "勻入部門預算項目代號", st.thRight);
            writeHead(head, c++, "勻入部門預算項目名稱", st.th);
            writeHead(head, c++, "勻出部門預算總額", st.thRightNum);
            writeHead(head, c++, "調整金額", st.thRightNum);

            sh.setColumnWidth(0, 12 * 256);
            sh.setColumnWidth(1, 8 * 256);
            sh.setColumnWidth(2, 18 * 256);
            sh.setColumnWidth(3, 8 * 256);
            sh.setColumnWidth(4, 18 * 256);
            sh.setColumnWidth(5, 14 * 256);
            sh.setColumnWidth(6, 22 * 256);
            sh.setColumnWidth(7, 24 * 256);
            sh.setColumnWidth(8, 18 * 256);
            sh.setColumnWidth(9, 22 * 256);
            sh.setColumnWidth(10, 24 * 256);
            sh.setColumnWidth(11, 18 * 256);
            sh.setColumnWidth(12, 18 * 256);

            Map<String, List<TransferRowTuple>> byDept = rows.stream()
                    .collect(Collectors.groupingBy(TransferRowTuple::getFromDeptCode, LinkedHashMap::new, Collectors.toList()));

            BigDecimal grand = BigDecimal.ZERO;

            for (Map.Entry<String, List<TransferRowTuple>> e : byDept.entrySet()) {
                List<TransferRowTuple> list = e.getValue();
                BigDecimal sub = BigDecimal.ZERO;

                for (TransferRowTuple it : list) {
                    Row r = sh.createRow(rowIdx++);
                    int col = 0;
                    writeCell(r, col++, nv(it.getYear()), st.td);
                    writeCell(r, col++, nv(it.getVersion()), st.td);
                    writeCell(r, col++, nv(it.getTransferOrderNo()), st.td);
                    writeCell(r, col++, it.getSeq() == null ? "" : String.valueOf(it.getSeq()), st.tdRight);

                    String fromName = nv(deptNames.get(nv(it.getFromDeptCode())));
                    String toName = nv(deptNames.get(nv(it.getToDeptCode())));
                    writeCell(r, col++, fromName, st.td);
                    writeCell(r, col++, nv(formatDate(it.getTransferDate())), st.td);

                    writeCell(r, col++, nv(it.getOutAccounting()), st.tdCodeRight);
                    writeCell(r, col++, nv(accountNames.get(nv(it.getOutAccounting()))), st.td);

                    writeCell(r, col++, toName, st.td);
                    writeCell(r, col++, nv(it.getInAccounting()), st.tdCodeRight);
                    writeCell(r, col++, nv(accountNames.get(nv(it.getInAccounting()))), st.td);

                    Cell outBudget = r.createCell(col++);
                    outBudget.setCellStyle(st.num);
                    outBudget.setCellValue(it.getOutBalance() == null ? 0 : it.getOutBalance().doubleValue());

                    Cell adj = r.createCell(col++);
                    adj.setCellStyle(st.num);
                    adj.setCellValue(it.getAdjustAmount() == null ? 0 : it.getAdjustAmount().doubleValue());

                    sub = sub.add(it.getAdjustAmount() == null ? BigDecimal.ZERO : it.getAdjustAmount());
                }

                // 小計列：建滿 0..12，合併 0..10 並補邊框
                Row subRow = sh.createRow(rowIdx++);
                for (int i = 0; i < totalCols; i++) {
                    Cell ccell = subRow.createCell(i);
                    ccell.setCellStyle(i == 12 ? st.numBold : st.bold);
                    ccell.setCellValue("");
                }
                subRow.getCell(0).setCellValue("小計");
                subRow.getCell(12).setCellValue(sub.doubleValue());

                CellRangeAddress subMerge = new CellRangeAddress(subRow.getRowNum(), subRow.getRowNum(), 0, 10);
                sh.addMergedRegion(subMerge);
                RegionUtil.setBorderTop(BorderStyle.THIN, subMerge, sh);
                RegionUtil.setBorderBottom(BorderStyle.THIN, subMerge, sh);
                RegionUtil.setBorderLeft(BorderStyle.THIN, subMerge, sh);
                RegionUtil.setBorderRight(BorderStyle.THIN, subMerge, sh);

                grand = grand.add(sub);
                rowIdx++; // 空白行
            }

            // 合計列：建滿 0..12，合併 0..10 並補邊框
            Row totalRow = sh.createRow(rowIdx++);
            for (int i = 0; i < totalCols; i++) {
                Cell ccell = totalRow.createCell(i);
                ccell.setCellStyle(i == 12 ? st.numBold : st.bold);
                ccell.setCellValue("");
            }
            totalRow.getCell(0).setCellValue("合計");
            totalRow.getCell(12).setCellValue(grand.doubleValue());

            CellRangeAddress totalMerge = new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 10);
            sh.addMergedRegion(totalMerge);
            RegionUtil.setBorderTop(BorderStyle.THIN, totalMerge, sh);
            RegionUtil.setBorderBottom(BorderStyle.THIN, totalMerge, sh);
            RegionUtil.setBorderLeft(BorderStyle.THIN, totalMerge, sh);
            RegionUtil.setBorderRight(BorderStyle.THIN, totalMerge, sh);

            try {
                sh.setDisplayGridlines(false);
            } catch (Exception ignore) { }

            wb.write(bos);
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("匯出失敗", ex);
        }
    }

    private String formatDate(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.trim().isEmpty()) return "";
        LocalDate date = LocalDate.parse(yyyymmdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return formatDate(date);
    }

    private String nv(String s) {
        return s == null ? "" : s;
    }

    private String formatRocDateToday() {
        return toRocYmdCn(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

    private String buildDeptText(List<String> departments) {
        return CollectionUtils.isEmpty(departments) ? "全部" : String.join(",", departments);
    }

    private String buildRocPeriod(String start, String end) {
        if (!StringUtils.hasText(start) && !StringUtils.hasText(end)) return "全部";
        String s = StringUtils.hasText(start) ? toRocYmdCn(start) : "";
        String e = StringUtils.hasText(end) ? toRocYmdCn(end) : "";
        return s + " ~ " + e;
    }

    private String toRocYmdCn(String ymd) {
        try {
            LocalDate d = LocalDate.parse(ymd, DateTimeFormatter.ISO_DATE);
            return String.format("%d年%02d月%02d日", d.getYear(), d.getMonthValue(), d.getDayOfMonth());
        } catch (Exception ex) {
            return ymd == null ? "" : ymd;
        }
    }

    private String formatDate(LocalDate d) {
        return d == null ? "" : d.format(DateTimeFormatter.ISO_DATE);
    }

    private static class Styles {
        final CellStyle title;
        final CellStyle th;
        final CellStyle thRight;
        final CellStyle thRightNum;
        final CellStyle td;
        final CellStyle tdRight;
        final CellStyle tdCodeRight;
        final CellStyle num;
        final CellStyle numBold;
        final CellStyle bold;

        Styles(XSSFWorkbook wb) {
            DataFormat fmt = wb.createDataFormat();

            Font fTitle = wb.createFont();
            fTitle.setBold(true);
            fTitle.setFontHeightInPoints((short) 14);
            title = wb.createCellStyle();
            title.setFont(fTitle);
            title.setAlignment(HorizontalAlignment.CENTER);

            Font fTh = wb.createFont();
            fTh.setBold(true);
            th = wb.createCellStyle();
            th.setFont(fTh);
            th.setBorderBottom(BorderStyle.THIN);
            th.setBorderTop(BorderStyle.THIN);
            th.setBorderLeft(BorderStyle.THIN);
            th.setBorderRight(BorderStyle.THIN);

            thRight = wb.createCellStyle();
            thRight.cloneStyleFrom(th);
            thRight.setAlignment(HorizontalAlignment.RIGHT);

            thRightNum = wb.createCellStyle();
            thRightNum.cloneStyleFrom(th);
            thRightNum.setAlignment(HorizontalAlignment.RIGHT);
            thRightNum.setDataFormat(fmt.getFormat("#,##0"));

            td = wb.createCellStyle();
            td.setBorderBottom(BorderStyle.THIN);
            td.setBorderTop(BorderStyle.THIN);
            td.setBorderLeft(BorderStyle.THIN);
            td.setBorderRight(BorderStyle.THIN);

            tdRight = wb.createCellStyle();
            tdRight.cloneStyleFrom(td);
            tdRight.setAlignment(HorizontalAlignment.RIGHT);

            tdCodeRight = wb.createCellStyle();
            tdCodeRight.cloneStyleFrom(td);
            tdCodeRight.setAlignment(HorizontalAlignment.RIGHT);

            num = wb.createCellStyle();
            num.cloneStyleFrom(td);
            num.setAlignment(HorizontalAlignment.RIGHT);
            num.setDataFormat(fmt.getFormat("#,##0"));

            Font fb = wb.createFont();
            fb.setBold(true);
            numBold = wb.createCellStyle();
            numBold.cloneStyleFrom(num);
            numBold.setFont(fb);

            Font fb2 = wb.createFont();
            fb2.setBold(true);
            bold = wb.createCellStyle();
            bold.cloneStyleFrom(td);
            bold.setFont(fb2);
        }
    }

    private void writeHead(Row r, int col, String text, CellStyle st) {
        Cell c = r.createCell(col);
        c.setCellValue(text);
        c.setCellStyle(st);
    }

    private void writeCell(Row r, int col, String text, CellStyle st) {
        Cell c = r.createCell(col);
        c.setCellValue(text == null ? "" : text);
        c.setCellStyle(st);
    }
}
