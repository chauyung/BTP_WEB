package nccc.btp.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import nccc.btp.dto.NonBudgetaryTerminalSummaryRequest;
import nccc.btp.repository.NcccEmsMgmRepository;
import nccc.btp.repository.NcccEmsMgmRepository.TerminalSummaryRow;
import nccc.btp.service.NonBudgetaryTerminalSummaryService;
import nccc.btp.util.ExcelUtil;

/**
 * 報表管理：非預算 報表-端末機帳列數報表(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NonBudgetaryTerminalSummaryServiceImpl implements NonBudgetaryTerminalSummaryService {
	
	// 輸出檔櫠本(Excel)
	private static String TEMPLATE_FILE = "NonBudgetaryTerminalSummaryTemplate.xlsx";

	// 端末機管理(DAO)
	private final NcccEmsMgmRepository ncccEmsMgmRepo;
	
	// Excel 範本助理
	private final ExcelUtil excelUtil;

	
	/**
	 * 報表管理：非預算 報表-端末機帳列數報表(產 Excel 檔)
	 */
	@Override
	public Optional<byte[]> exportExcel(NonBudgetaryTerminalSummaryRequest req) {
		// STEP_01: 參數查核
		this.validate(req);

		// STEP_02: 取得輸出資料(By SQL)
		List<TerminalSummaryRow> dataSet = 
				ncccEmsMgmRepo.fetchNonBudgetaryTerminalSummary(req.getYearMonth());
		
		// STEP_03: 將資料寫入 Excel
        try (XSSFWorkbook wb = new XSSFWorkbook(excelUtil.templateBy(TEMPLATE_FILE));
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        	
        	// STEP_03.1: 調整頁籤名稱
        	Sheet sheet = wb.getSheet("YYYYMM");
        	String sheetName = req.getYearMonth();
        	wb.setSheetName(wb.getSheetIndex(sheet), sheetName);
        	
        	// STEP_03.2: 輸出資料
        	this.prepareSheet(wb, sheet, sheetName, dataSet);
        	
        	// STEP_03.3: 轉 Byte[] 回傳
    		wb.write(bos);
    		return Optional.of(bos.toByteArray());
        } catch (Exception e) {
            return Optional.empty();
        }
	}
	
	/**
	 * 將資料寫入 Excel
	 * @param wb - Excel(WorkBook)
	 * @param sheet - Excel(Sheet)
	 * @param yyyymm - 年月
	 * @param dataSet - 資料集
	 */
	private void prepareSheet(XSSFWorkbook wb, Sheet sheet, String yyyymm, List<TerminalSummaryRow> dataSet) {

	    // STEP_01: 建立共用儲存格樣式(細黑邊框)
		CellStyle style01 = excelUtil.cellStyle(wb, HorizontalAlignment.CENTER);
		CellStyle style02 = excelUtil.cellStyle(wb, HorizontalAlignment.LEFT);
		CellStyle style03 = excelUtil.cellStyle(wb, HorizontalAlignment.RIGHT);

	    // STEP_02: 從第 3 列開始（假設第 1-2 列是表頭）
	    int startRowIndex = 3;
	    for (int i = 0; i < dataSet.size(); i++) {
	        TerminalSummaryRow rowData = dataSet.get(i);
	        org.apache.poi.ss.usermodel.Row row = sheet.getRow(startRowIndex + i);
	        if (row == null) {
	            row = sheet.createRow(startRowIndex + i);
	        }

	        // 依序寫入各欄位
	        excelUtil.createCell(row, 0, rowData.getSn(), style01);      // SN
	        excelUtil.createCell(row, 1, rowData.getData01(), style02);  // 機型
	        excelUtil.createCell(row, 2, rowData.getSum01(), style03);   // 當月出售
	        excelUtil.createCell(row, 3, rowData.getSum02(), style03);   // 當月報廢
	        excelUtil.createCell(row, 4, rowData.getSum03(), style03);   // 當月減損
	        excelUtil.createCell(row, 5, rowData.getSum04(), style03);   // 當月新增
	        excelUtil.createCell(row, 6, rowData.getSum05(), style03);   // 累積列帳
	        excelUtil.createCell(row, 7, rowData.getSum06(), style03);   // 累積列管
	        excelUtil.createCell(row, 8, rowData.getSum07(), style03);   // 使用中合計
	    }

	    // STEP_03: 自動調整欄寬
	    for (int i = 0; i < 8; i++) {
	        sheet.autoSizeColumn(i);
	    }
	}

	/**
	 * 參數查核
	 * @param req - 報表管理：非預算 報表-端末機帳列數報表(API-Request)
	 */
	private void validate(NonBudgetaryTerminalSummaryRequest req) {
		String yyyymm = req.getYearMonth();
		if (!StringUtils.hasText(yyyymm) || !yyyymm.matches("\\d{4}((0[1-9]{1})|(11)|(12))")) {
			throw new IllegalArgumentException("年月為必填，且格式為西元年月(YYYYMM) 6 碼。");
		}
	}
}
