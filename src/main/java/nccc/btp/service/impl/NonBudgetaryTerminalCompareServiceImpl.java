package nccc.btp.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nccc.btp.repository.EmsMtermModelRepository;
import nccc.btp.repository.EmsMtermModelRepository.EmsTerminalCompareRow;
import nccc.btp.repository.NcccEmsMgmRepository;
import nccc.btp.repository.NcccEmsMgmRepository.BtpTerminalCompareRow;
import nccc.btp.service.NonBudgetaryTerminalCompareService;
import nccc.btp.util.DateUtil;
import nccc.btp.util.ExcelUtil;

/**
 * 報表管理：非預算 報表-端末機差異比較表(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@RequiredArgsConstructor
@Service
public class NonBudgetaryTerminalCompareServiceImpl implements NonBudgetaryTerminalCompareService{
	
	// 輸出檔櫠本(Excel)
	private static String TEMPLATE_FILE = "NonBudgetaryTerminalCompareTemplate.xlsx";
	
	// 端末機管理(DAO)
	private final NcccEmsMgmRepository ncccEmsMgmRepo;
	
	// 端末機管理(DAO)
	private final EmsMtermModelRepository emsMtermModelRepo;
	
	// Excel 範本助理
	private final ExcelUtil excelUtil;
	
	
	/**
	 * 報表管理：非預算 報表-端末機差異比較表(產 Excel 檔)
	 */
	@Override
	public Optional<byte[]> exportExcel() {
		
		// STEP_01: 取得 BTP(資料庫).資料表(NCCC_EMS_MGM)資料
		List<BtpTerminalCompareRow> btpList = 
				ncccEmsMgmRepo.fetchNonBudgetaryTerminalCompare();
		
		// STEP_02: 取得 EMS(資料庫).資料表(EMS_MEQ_ITEM、EMS_MEQ_PUR、EMS_MLOOKUP、EMS_MTERM_MODEL)資料
		List<EmsTerminalCompareRow> emsList = 
				emsMtermModelRepo.fetchNonBudgetaryTerminalCompare();
		
		// STEP_03: 比對 BTP/EMS 之相同[機型名稱]有差異的資料
		Map<String, MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow>> compareResult = 
				this.compare(emsList, btpList);
		
		// STEP_04: 將資料寫入 Excel
        try (XSSFWorkbook wb = new XSSFWorkbook(excelUtil.templateBy(TEMPLATE_FILE));
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        	
        	// STEP_04.1: 調整頁籤名稱
        	Sheet sheet = wb.getSheet("YYYYMMDD");
        	String sheetName = DateUtil.toYyyyMmDdString(LocalDate.now());
        	wb.setSheetName(wb.getSheetIndex(sheet), sheetName);
        	
        	// STEP_04.2: 輸出資料
        	this.prepareSheet(wb, sheet, compareResult);
        	
        	// STEP_04.3: 轉 Byte[] 回傳
    		wb.write(bos);
    		return Optional.of(bos.toByteArray());
        } catch (Exception e) {
            return Optional.empty();
        }
	}
	
	/**
	 * 比對 BTP/EMS 之相同[機型名稱], 列出有差異的資料
	 * @param emsList - MS(資料庫).資料表(EMS_MEQ_ITEM、EMS_MEQ_PUR、EMS_MLOOKUP、EMS_MTERM_MODEL)資料
	 * @param btpList - BTP(資料庫).資料表(NCCC_EMS_MGM)資料
	 * @return - 相同[機型名稱]有差異的資料
	 */
	private Map<String, MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow>> compare(
			List<EmsTerminalCompareRow> emsList,
			List<BtpTerminalCompareRow> btpList){
		
		// STEP_01: 備妥差異清單(result)
		Map<String, MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow>> result = new HashMap<>();
		
		// STEP_02: 逐筆 EMS(Record) 比對 BTP(Record), 不同即放入 result
		for(EmsTerminalCompareRow emsRec: emsList) {
			
			// STEP_02.1: 以『EMS財產編號＋EMS設備代號』 為鍵值(dataKey)
			String dataKey = emsRec.getKey01() + ":" + emsRec.getKey02();

			// STEP_02.2: 以『EMS財產編號＋EMS設備代號』與『BTP財產編號＋BTP設備代號』關連，比對二者的『庫存狀態』
			BtpTerminalCompareRow btpMeth = null;
			for(BtpTerminalCompareRow btpRec: btpList) {
				if ((btpRec.getKey01().equals(emsRec.getKey01())) && (btpRec.getKey02().equals(emsRec.getKey02()))) {
					btpMeth = btpRec;
					break;
				}
			}
			if (btpMeth != null) {
				btpList.remove(btpMeth);
				if (btpMeth.getData02().equals(emsRec.getData02())) {
					continue;
				}
			}
			
			// STEP_02.3: 如果 BTP 與 EMS 之 『庫存狀態』 值不同, 則放入差異清單(result)中
			MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow> row = MutablePair.of(emsRec, btpMeth);
			result.put(dataKey, row);
		}
		
		// STEP_03: 剩下未與 EMS 比對到的 BTP(Reocrd), 亦放入差異清單(result)中
		for(BtpTerminalCompareRow btpRec: btpList) {
			
			// STEP_03.1: 以『BTP財產編號＋BTP設備代號』 為鍵值(dataKey)
			String dataKey = btpRec.getKey01() + ":" + btpRec.getKey02();
			
			// STEP_03.2: 放入差異清單(result)中
			MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow> row = MutablePair.of(null, btpRec);
			result.put(dataKey, row);
		}
		return result;
	}
	
	/**
	 * 將資料寫入 Excel
	 * @param wb - Excel(WorkBook)
	 * @param sheet - Excel(Sheet)
	 * @param yyyymm - 年月
	 * @param dataSet - 資料集
	 */
	private void prepareSheet(XSSFWorkbook wb, Sheet sheet, 
			Map<String, MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow>> compareResult) {
		
		// STEP_01: 建立共用儲存格樣式(細黑邊框)
		CellStyle style01 = excelUtil.cellStyle(wb, HorizontalAlignment.LEFT);
		
		// STEP_02: 資料排序
	    List<String> sortedKeys = compareResult.keySet().stream()
	            .sorted()   // 預設為字母升冪排序，如需反序可用 .sorted(Comparator.reverseOrder())
	            .collect(Collectors.toList());
	    
		// STEP_02: 從第 2 列開始（假設第 1 列是表頭）
	    int startRowIndex = 2;
	    Integer sn = 0;
	    for (String key : sortedKeys) {
	    		MutablePair<EmsTerminalCompareRow, BtpTerminalCompareRow> rec = 
	    				compareResult.get(key);
	    		
	    		sn++;
	    		org.apache.poi.ss.usermodel.Row row = sheet.getRow(startRowIndex + sn);
	        if (row == null) {
	            row = sheet.createRow(startRowIndex + sn);
	        }
	        
	        // 依序寫入各欄位
	        excelUtil.createCell(row, 0, sn, style01);                         // 項次
	        excelUtil.createCell(row, 1, rec.getLeft().getData01(), style01);  // EMS 機型名稱
	        excelUtil.createCell(row, 2, rec.getLeft().getKey01(), style01);   // EMS 財產編號
	        excelUtil.createCell(row, 3, rec.getLeft().getKey02(), style01);   // EMS 設備代號
	        excelUtil.createCell(row, 4, rec.getLeft().getData02(), style01);  // EMS 庫存狀態
	        excelUtil.createCell(row, 5, rec.getRight().getData01(), style01); // BTP 機型名稱
	        excelUtil.createCell(row, 6, rec.getRight().getKey01(), style01);  // BTP 財產編號
	        excelUtil.createCell(row, 7, rec.getRight().getKey02(), style01);  // BTP 設備代號
	        excelUtil.createCell(row, 8, rec.getRight().getData02(), style01); // BTP 狀態
	    }
	}
}
