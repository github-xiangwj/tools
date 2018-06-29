package com.tool.export.oracle.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tool.export.oracle.bean.Col;
import com.tool.export.style.FontStyle;

/**
 * 导出-工具类
 * @author xiangwj
 *
 */
public class Export {
	
	protected static Logger logger = LoggerFactory.getLogger(Export.class);
	
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
			String url="jdbc:oracle:thin:@192.168.253.1:1521/orcl";
			
			String user="admin";
			
			String password="forms123";
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			connection = DriverManager.getConnection(url, user, password);
		
			Statement statement = connection.createStatement();
			
			//1.查询用户下所有的表名
			String tableSql = "select table_name as tab from user_tables order by table_name";
			
			logger.info("查询数据库表sql："+tableSql);
			ResultSet tableRs = statement.executeQuery(tableSql);
			List<String> tables = new ArrayList<String>();
			if(tableRs != null){
				while(tableRs.next()){
					String tab = tableRs.getString("tab");
					logger.info(tab);
					tables.add(tab);
				}
			}else{
				logger.error(url.split("@")[1] +"数据库的"+user+"用户没有表!请确认!");
				return;
			}
			
			//2.查询数据库列
			String colsSql = " select "
							+"  t.table_name as tableName,"
							+"  c.comments as tableComment,"
							+"  t.column_name as colName,"
							+"  d.comments as colComment,"
							+"  t.data_type as dataType,"
							+"  t.data_length as dataLen,"
							+"  t.nullable as nullable"
							+" from user_tab_columns t"
							+" join user_tab_comments c on t.TABLE_NAME = c.table_name"
							+" join user_col_comments d on t.TABLE_NAME = d.table_name and t.COLUMN_NAME = d.column_name"
							+" order by t.table_name, t.column_id";

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
			cell.setCellValue(user+"用户表结构");

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
			
			sheet.createFreezePane(0, 2);
			
			fos = new FileOutputStream(new File("D:\\11.xls"));
			workbook.write(fos);
			fos.flush();
			logger.info("成功导出数据库表结构");
			
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
		}
		
		
	}

}
