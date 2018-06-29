package com.tool.export.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 导出字体样式
 * @author xiangwj
 *
 */
public class FontStyle {
	
	/**
	 * 标题字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle titleStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
		//设置表框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		headerFont.setFontHeightInPoints((short)20);//字体大小
		style.setFont(headerFont);
		
		return style;
	}

	/**
	 * 行头字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle headStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		headerFont.setFontHeightInPoints((short)12);//字体大小
		headerFont.setColor(HSSFColor.ORANGE.index);
		style.setFont(headerFont);
		
		return style;
	}
	
	
	/**
	 * 小标题字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle smallTitleStyle(HSSFWorkbook workbook) {
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
		headerFont.setFontHeightInPoints((short)12);//字体大小
		headerFont.setColor(HSSFColor.BLACK.index);
		style.setFont(headerFont);
		
		return style;
	}
	
	/**
	 * 正文字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle textStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
		//设置表框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		HSSFFont headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short)12);//字体大小
		headerFont.setColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFont(headerFont);
		
		return style;
	}


}
