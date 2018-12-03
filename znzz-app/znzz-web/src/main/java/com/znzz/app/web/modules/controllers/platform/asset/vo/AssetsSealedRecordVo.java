package com.znzz.app.web.modules.controllers.platform.asset.vo;

import java.util.Date;

public class AssetsSealedRecordVo {
	private int length;
	private int start;
	private int draw;
	
	private String assetCode;
	private Integer assetType;
	private String borrowDepart;
	private String operator;
	private Integer sealStatus;
	private Date operateTime;
	
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public Integer getAssetType() {
		return assetType;
	}
	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}
	public String getBorrowDepart() {
		return borrowDepart;
	}
	public void setBorrowDepart(String borrowDepart) {
		this.borrowDepart = borrowDepart;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getSealStatus() {
		return sealStatus;
	}
	public void setSealStatus(Integer sealStatus) {
		this.sealStatus = sealStatus;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	

}
