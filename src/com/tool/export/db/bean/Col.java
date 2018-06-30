package com.tool.export.db.bean;

public class Col {
	
	private String tableName;
	private String tableComment;
	private String colName;
	private String colComment;
	private String dataType;
	private String dataLen;
	private String nullable;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableComment() {
		return tableComment;
	}
	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColComment() {
		return colComment;
	}
	public void setColComment(String colComment) {
		this.colComment = colComment;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataLen() {
		return dataLen;
	}
	public void setDataLen(String dataLen) {
		this.dataLen = dataLen;
	}
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	
	@Override
	public String toString() {
		return "Col [tableName=" + tableName + ", tableComment=" + tableComment + ", colName=" + colName
				+ ", colComment=" + colComment + ", dataType=" + dataType + ", dataLen=" + dataLen + ", nullable="
				+ nullable + "]";
	}
	
}
