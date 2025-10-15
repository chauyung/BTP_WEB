package nccc.btp.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 範本助理
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
public class ExcelUtil {
	
	public static final Integer	BUFFER_SIZE	= 102400;
	
	/**
	 * 生成儲存格樣式
	 * @param wb - Excel(WorkBook)
	 * @param ha - Excel(HorizontalAlignment)
	 * @return - 儲存格樣式
	 */
	public CellStyle cellStyle(XSSFWorkbook wb, HorizontalAlignment ha) {
	    CellStyle result = wb.createCellStyle();
	    result.setBorderTop(BorderStyle.THIN);
	    result.setBorderBottom(BorderStyle.THIN);
	    result.setBorderLeft(BorderStyle.THIN);
	    result.setBorderRight(BorderStyle.THIN);
	    result.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    result.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	    result.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	    result.setRightBorderColor(IndexedColors.BLACK.getIndex());
	    result.setVerticalAlignment(VerticalAlignment.CENTER);
	    result.setAlignment(ha);
	    return result;
	}
	
	/**
	 * 取得(Excel)範本
	 * @param fileName - (Excel)範本檔名
	 * @return - 輸出串流
	 */
	public InputStream templateBy(String fileName) {
		ByteArrayInputStream inputStream = null;
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			File templateFile = new File(getClass().getClassLoader().getResource("template/" + fileName).getFile());
			if (null != templateFile) {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(templateFile), BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				int size = -1;
				while ((size = bufferedInputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, size);
				}
				bufferedInputStream.close();
				inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	
	/**
	 * 輸出資料至儲存格
	 * @param row - 儲存列
	 * @param colIndex - 儲存欄(索引)
	 * @param value - 資料值
	 * @param style - 儲存樣式
	 */
	public void createCell(org.apache.poi.ss.usermodel.Row row, int colIndex, Object value, CellStyle style) {
	    Cell cell = row.getCell(colIndex);
	    if (cell == null) {
	        cell = row.createCell(colIndex);
	    }

	    if (value instanceof Number) {
	        cell.setCellValue(((Number) value).doubleValue());
	    } else if (value != null) {
	        cell.setCellValue(value.toString());
	    } else {
	        cell.setCellValue("");
	    }

	    cell.setCellStyle(style);
	}
}
