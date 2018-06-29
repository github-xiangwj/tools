package com.tool.export.style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * ����������ʽ
 * @author xiangwj
 *
 */
public class FontStyle {
	
	/**
	 * ����������ʽ
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle titleStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//����
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ
		//���ñ��
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//�Ӵ�
		headerFont.setFontHeightInPoints((short)20);//�����С
		style.setFont(headerFont);
		
		return style;
	}

	/**
	 * ��ͷ������ʽ
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle headStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//�Ӵ�
		headerFont.setFontHeightInPoints((short)12);//�����С
		headerFont.setColor(HSSFColor.ORANGE.index);
		style.setFont(headerFont);
		
		return style;
	}
	
	
	/**
	 * С����������ʽ
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle smallTitleStyle(HSSFWorkbook workbook) {
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//�Ӵ�
		headerFont.setFontHeightInPoints((short)12);//�����С
		headerFont.setColor(HSSFColor.BLACK.index);
		style.setFont(headerFont);
		
		return style;
	}
	
	/**
	 * ����������ʽ
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle textStyle(HSSFWorkbook workbook) {

		HSSFCellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ
		//���ñ��
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		HSSFFont headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short)12);//�����С
		headerFont.setColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFont(headerFont);
		
		return style;
	}


}
