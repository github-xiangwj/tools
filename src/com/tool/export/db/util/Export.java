package com.tool.export.db.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tool.export.db.bean.Col;
import com.tool.export.style.FontStyle;

/**
 * ����-������
 * @author xiangwj
 *
 */
public class Export {
	
	protected static Logger logger = LoggerFactory.getLogger(Export.class);
	
	private static String DB_CONFIG_FILE = "export.db.properties";
	
	public static void main(String[] args) {
		exportDbTables();
	}
	
	/**
	 * �������ݿ��ṹ
	 * @throws Exception 
	 */
	public static void exportDbTables(){
		Connection connection = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fos = null;
		try {
			//��ȡ���ݿ�������Ϣ
			Properties p = new Properties();
			p.load(new InputStreamReader(Export.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE),"UTF-8"));
			String dbType = p.getProperty("db.type");
			
			String driver = p.getProperty(dbType +".driver");
			String url = p.getProperty(dbType +".url");
			String username = p.getProperty(dbType +".username");
			String password = p.getProperty(dbType +".password");
			
			Class.forName(driver);
			
			connection = DriverManager.getConnection(url, username, password);
		
			Statement statement = connection.createStatement();
			
			//1.��ѯ�û������еı���
			String tableSql = p.getProperty(dbType+".tableSql");
			
			logger.info("��ѯ���ݿ��sql��"+tableSql);
			ResultSet tableRs = statement.executeQuery(tableSql);
			List<String> tables = new ArrayList<String>();
			if(tableRs != null){
				while(tableRs.next()){
					String tab = tableRs.getString("tableName");
					logger.info("��ѯ���� "+tab);
					tables.add(tab);
				}
			}else{
				logger.error(url.split("@")[1] +"���ݿ��"+username+"�û�û�б�!��ȷ��!");
				return;
			}
			
			//2.��ѯ���ݿ���
			String colsSql = p.getProperty(dbType+".colsSql");

			logger.info("��ѯ���ݿ������Ϣsql��"+colsSql);
			ResultSet colsRs = statement.executeQuery(colsSql);
			List<Col> tableCols = new ArrayList<Col>();

			while(colsRs.next()){
				Col col = new Col();
				col.setTableName(colsRs.getString("tableName"));
				col.setTableComment(colsRs.getString("tableComment"));
				col.setColName(colsRs.getString("colName"));
				col.setColComment(colsRs.getString("colComment"));
				col.setDataType(colsRs.getString("dataType"));
				col.setDataLen(colsRs.getString("dataLen"));
				col.setNullable(colsRs.getString("nullable"));
				
				logger.info("��ѯ������Ϣ"+col);
				tableCols.add(col);
			}
			
			//3.����excel
			workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			HSSFRow row = null;
			HSSFCell cell = null;
			sheet.setDefaultColumnWidth(20);
			//�������ͷ
			int m = 0;
			row = sheet.createRow(m++);
			CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
			sheet.addMergedRegion(range);
			cell = row.createCell(0);
			cell.setCellStyle(FontStyle.titleStyle(workbook));
			cell.setCellValue(username+"�û���ṹ");

			row = sheet.createRow(m++);
			cell = row.createCell(0);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("����");
			cell = row.createCell(1);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("�б�ע");
			cell = row.createCell(2);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("����");
			cell = row.createCell(3);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("����");
			cell = row.createCell(4);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("�Ƿ��Ϊ��");
			//��������
			int j = 0;
			//�Ƿ������˱�����ʶ
			boolean hasWriteTable = false;
			for(int i = 0 ; i < tableCols.size() && j < tables.size() ; i++){
				//��ȡ��
				Col col = tableCols.get(i);
				//�ж�һ�����Ƿ������
				if(!col.getTableName().equals(tables.get(j))){
					i --;
					j ++;
					hasWriteTable = false;
					continue ;
				}
				if(!hasWriteTable){
					//���ɱ����֮��ļ����
					row = sheet.createRow(i+m++);
					//���ɱ���
					row = sheet.createRow(i+m++);
					cell = row.createCell(0);
					cell.setCellStyle(FontStyle.smallTitleStyle(workbook));
					cell.setCellValue(col.getTableName());
					row = sheet.createRow(i+m++);
					cell = row.createCell(0);
					cell.setCellStyle(FontStyle.smallTitleStyle(workbook));
					cell.setCellValue(col.getTableComment());
					hasWriteTable = true;
				}
				if(col.getTableName().equals("CCPMS_ETL_DATE")){
					logger.info("");
				}
				//���ɱ���
				logger.info("�����������ݱ�["+tables.get(j)+"]��"+ col.getColName());
				//����ǰ�����ı���ֶ���Ϣд���ļ�
				row = sheet.createRow(i+m);
				cell = row.createCell(0);
				cell.setCellStyle(FontStyle.textStyle(workbook));
				cell.setCellValue(col.getColName());
				cell = row.createCell(1);
				cell.setCellStyle(FontStyle.textStyle(workbook));
				cell.setCellValue(col.getColComment());
				cell = row.createCell(2);
				cell.setCellStyle(FontStyle.textStyle(workbook));
				cell.setCellValue(col.getDataType());
				cell = row.createCell(3);
				cell.setCellStyle(FontStyle.textStyle(workbook));
				cell.setCellValue(col.getDataLen());
				cell = row.createCell(4);
				cell.setCellStyle(FontStyle.textStyle(workbook));
				cell.setCellValue(col.getNullable());
			}
			//���ᴰ��
			sheet.createFreezePane(0, 2);
			//������ļ���
			String filename = p.getProperty("export.file.name");
			
			if(filename == null || filename.equals("")){
				//Ĭ�ϱ����ļ���
				filename = "D:\\"+username+"��ṹ.xls";
			}
			
			fos = new FileOutputStream(new File(filename));
			workbook.write(fos);
			fos.flush();
			logger.info("�ɹ����� "+url+" ���ݿ� "+username+" ��ṹ");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
