package com.znzz.app.web.commons.services.easypoi;


public class MergeExcelEntity {
	
	/**
	 * 合并类型（默认纵向合并）：colMerge 纵向合并 rowMerge 横向合并 
	 */
	private String mergeType;
	
	/**
	 * Sheet号
	 */
	private Integer sheetNo;
	
	/**
	 * 起始行
	 */
	private Integer startRow ;
	
	/**
	 * 结束行（默认sheet的最后一行）
	 */
	private Integer endRow ;
	
	/**
	 * 纵向合并时，对应需要合并的列
	 */
	private int [] cols;
	
	/**
	 * 起始列
	 */
	private Integer startCol ;
	
	
	/**
	 * 结束列
	 */
	private Integer endCol ;
	
	
	public int[] getCols() {
		return cols;
	}

	public void setCols(int[] cols) {
		this.cols = cols;
	}

	public String getMergeType() {
		if(mergeType==null || mergeType.isEmpty()){
			mergeType = "colMerge"	;
		}
		if(mergeType!="rowMerge" && mergeType!="colMerge"){
			mergeType = "colMerge"	;
		}
		return mergeType;
	}

	public void setMergeType(String mergeType) {
		this.mergeType = mergeType;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public Integer getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(Integer sheetNo) {
		this.sheetNo = sheetNo;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public Integer getStartCol() {
		return startCol;
	}

	public void setStartCol(Integer startCol) {
		this.startCol = startCol;
	}

	public Integer getEndCol() {
		return endCol;
	}

	public void setEndCol(Integer endCol) {
		this.endCol = endCol;
	}


	
	
}
