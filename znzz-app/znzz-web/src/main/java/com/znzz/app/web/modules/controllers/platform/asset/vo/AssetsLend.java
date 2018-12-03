package com.znzz.app.web.modules.controllers.platform.asset.vo;

import java.io.Serializable;

import java.util.Date;

/**
 * 
 * @ClassName: AssetsLend
 * @Description (封装仪器借调vo)
 * @author fengguoxin
 * @date 2017年9月5日 下午2:09:10
 */
public class AssetsLend implements Serializable {

	private static final long serialVersionUID = 520959978326008247L;
	private String assetCode;//统一编号
	private String assetName;//资产名称
	private String serialNumber;//出厂编号
	private String deviceVersion;//型号
	private String ggName;//规格
	private Date returnDate;
	private String returnPerson;
	private String applyPerson;
	private String applyDepartment;
	private String actionType;
	private Integer assetType;// 资产种类:(0全部1设备2仪器3工量具)
	private String remark;
	private String str1;

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getApplyPerson() {
		return applyPerson;
	}

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}

	public String getApplyDepartment() {
		return applyDepartment;
	}

	public void setApplyDepartment(String applyDepartment) {
		this.applyDepartment = applyDepartment;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String string) {
		this.actionType = string;
	}

	public String getReturnPerson() {
		return returnPerson;
	}

	public void setReturnPerson(String returnPerson) {
		this.returnPerson = returnPerson;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getGgName() {
		return ggName;
	}

	public void setGgName(String ggName) {
		this.ggName = ggName;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date date) {
		this.returnDate = date;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
