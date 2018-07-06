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
 * 导出-工具类
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
	 * 导出数据库表结构
	 * @throws Exception 
	 */
	public static void exportDbTables(){
		Connection connection = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fos = null;
		try {
			//获取数据库配置信息
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
			
			//1.查询用户下所有的表名
			String tableSql = p.getProperty(dbType+".tableSql");
			
			logger.info("查询数据库表sql："+tableSql);
			ResultSet tableRs = statement.executeQuery(tableSql);
			List<String> tables = new ArrayList<String>();
			if(tableRs != null){
				while(tableRs.next()){
					String tab = tableRs.getString("tableName");
					logger.info("查询到表 "+tab);
					tables.add(tab);
				}
			}else{
				logger.error(url.split("@")[1] +"数据库的"+username+"用户没有表!请确认!");
				return;
			}
			
			//2.查询数据库列
			String colsSql = p.getProperty(dbType+".colsSql");

			logger.info("查询数据库表列信息sql："+colsSql);
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
				
				logger.info("查询到列信息"+col);
				tableCols.add(col);
			}
			
			//3.导出excel
			workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			HSSFRow row = null;
			HSSFCell cell = null;
			sheet.setDefaultColumnWidth(20);
			//先输出行头
			int m = 0;
			row = sheet.createRow(m++);
			CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
			sheet.addMergedRegion(range);
			cell = row.createCell(0);
			cell.setCellStyle(FontStyle.titleStyle(workbook));
			cell.setCellValue(username+"用户表结构");

			row = sheet.createRow(m++);
			cell = row.createCell(0);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("列名");
			cell = row.createCell(1);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("列备注");
			cell = row.createCell(2);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("类型");
			cell = row.createCell(3);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("长度");
			cell = row.createCell(4);
			cell.setCellStyle(FontStyle.headStyle(workbook));
			cell.setCellValue("是否可为空");
			//遍历表集合
			int j = 0;
			//是否生成了表名标识
			boolean hasWriteTable = false;
			for(int i = 0 ; i < tableCols.size() && j < tables.size() ; i++){
				//获取列
				Col col = tableCols.get(i);
				//判断一个表是否遍历完
				if(!col.getTableName().equals(tables.get(j))){
					i --;
					j ++;
					hasWriteTable = false;
					continue ;
				}
				if(!hasWriteTable){
					//生成表与表之间的间隔行
					row = sheet.createRow(i+m++);
					//生成表名
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
				//生成表列
				logger.info("正在生成数据表["+tables.get(j)+"]列"+ col.getColName());
				//将当前遍历的表的字段信息写入文件
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
			//冻结窗口
			sheet.createFreezePane(0, 2);
			//保存的文件名
			String filename = p.getProperty("export.file.name");
			
			if(filename == null || filename.equals("")){
				//默认保存文件名
				filename = "D:\\"+username+"表结构.xls";
			}
			
			fos = new FileOutputStream(new File(filename));
			workbook.write(fos);
			fos.flush();
			logger.info("成功导出 "+url+" 数据库 "+username+" 表结构");
			
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
