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
	
	public static HSSFCellStyle TITLE_STYLE;
	public static HSSFCellStyle HEAD_STYLE;
	public static HSSFCellStyle SMALL_TITLE_STYLE;
	public static HSSFCellStyle TEXT_STYLE;
	
	/**
	 * 标题字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle titleStyle(HSSFWorkbook workbook) {
		if(TITLE_STYLE == null){
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
			TITLE_STYLE = style;
		}
		return TITLE_STYLE;
	}

	/**
	 * 行头字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle headStyle(HSSFWorkbook workbook) {
		if(HEAD_STYLE == null){
			HSSFCellStyle style = workbook.createCellStyle();
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
			HSSFFont headerFont = workbook.createFont();
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
			headerFont.setFontHeightInPoints((short)12);//字体大小
			headerFont.setColor(HSSFColor.ORANGE.index);
			style.setFont(headerFont);
			HEAD_STYLE = style;
		}
		
		return HEAD_STYLE;
	}
	
	
	/**
	 * 小标题字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle smallTitleStyle(HSSFWorkbook workbook) {
		if(SMALL_TITLE_STYLE == null){
			HSSFCellStyle style = workbook.createCellStyle();
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直
			HSSFFont headerFont = workbook.createFont();
			headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
			headerFont.setFontHeightInPoints((short)12);//字体大小
			headerFont.setColor(HSSFColor.BLACK.index);
			style.setFont(headerFont);
			SMALL_TITLE_STYLE = style;
		}
		
		return SMALL_TITLE_STYLE;
	}
	
	/**
	 * 正文字体样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle textStyle(HSSFWorkbook workbook) {
		if(TEXT_STYLE == null){
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
			TEXT_STYLE = style;
		}
		
		return TEXT_STYLE;
	}


}
