package com.znzz.app.asset.moudules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * @classname Ins_Asset_SamplingCheck.java
 * @author chenzhongliang
 * @date 2017年12月11日
 */
@Table("ins_asset_samplingcheck")
@Comment("抽样检定表")
public class Ins_Asset_SamplingCheck {
	@Id
	@Column
	@Comment("id")
	private Integer id;
	
	@Column
	@Comment("统一编号")
	@ColDefine(type = ColType.VARCHAR, width = 60, notNull=true)
	private String assetCode;
	
	@Column
	@Comment("资产名称")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String assetName;
	
	@Column
	@Comment("型号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String assetModel;
	
	@Column
	@Comment("出厂编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String serialNumber;
	
	@Column
	@Comment("检定部门")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String testDepartment;
	
	@Column
	@Comment("使用部门")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String useDepartment;
	
	@Column
	@Comment("使用人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String userMan;
	
	@Column
	@Comment("抽检日期")
	@ColDefine(type = ColType.DATE)
	private Date samplingDate;
	
	private String samplingDate2;	//页面向后台传值的容器
	
	@Column
	@Comment("责任人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String respMan;
	
	@Column
	@Comment("送检日期")
	@ColDefine(type = ColType.DATE)
	private Date sentCheckDate;
	
	private String sentCheckDate2;
	
	@Column
	@Comment("检定日期")
	@ColDefine(type = ColType.DATE)
	private Date checkDate;
	
	private String checkDate2;
	
	@Column
	@Comment("检定结果")
	@ColDefine(type = ColType.VARCHAR, width = 300)
	private String testResult;
	
	@Column
	@Comment("领取日期")
	@ColDefine(type = ColType.DATE)
	private Date getDate;
	
	private String getDate2;
	
	@Column
	@Comment("归档日期")
	@ColDefine(type = ColType.DATE)
	private Date filDate;

	private String filDate2;
	
	
	@Column
	@Comment("备注")
	@ColDefine(type = ColType.VARCHAR, width = 300)
	private String remark;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public String getAssetModel() {
		return assetModel;
	}


	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
	}


	public String getSerialNumber() {
		return serialNumber;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public String getTestDepartment() {
		return testDepartment;
	}


	public void setTestDepartment(String testDepartment) {
		this.testDepartment = testDepartment;
	}


	public String getUseDepartment() {
		return useDepartment;
	}


	public void setUseDepartment(String useDepartment) {
		this.useDepartment = useDepartment;
	}


	public String getUserMan() {
		return userMan;
	}


	public void setUserMan(String userMan) {
		this.userMan = userMan;
	}


	public Date getSamplingDate() {
		return samplingDate;
	}


	public void setSamplingDate(Date samplingDate) {
		this.samplingDate = samplingDate;
	}


	public String getSamplingDate2() {
		return samplingDate2;
	}


	public void setSamplingDate2(String samplingDate2) {
		this.samplingDate2 = samplingDate2;
	}


	public String getRespMan() {
		return respMan;
	}


	public void setRespMan(String respMan) {
		this.respMan = respMan;
	}


	public Date getSentCheckDate() {
		return sentCheckDate;
	}


	public void setSentCheckDate(Date sentCheckDate) {
		this.sentCheckDate = sentCheckDate;
	}


	public String getSentCheckDate2() {
		return sentCheckDate2;
	}


	public void setSentCheckDate2(String sentCheckDate2) {
		this.sentCheckDate2 = sentCheckDate2;
	}


	public Date getCheckDate() {
		return checkDate;
	}


	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}


	public String getCheckDate2() {
		return checkDate2;
	}


	public void setCheckDate2(String checkDate2) {
		this.checkDate2 = checkDate2;
	}


	public String getTestResult() {
		return testResult;
	}


	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}


	public Date getGetDate() {
		return getDate;
	}


	public void setGetDate(Date getDate) {
		this.getDate = getDate;
	}


	public String getGetDate2() {
		return getDate2;
	}


	public void setGetDate2(String getDate2) {
		this.getDate2 = getDate2;
	}


	public Date getFilDate() {
		return filDate;
	}


	public void setFilDate(Date filDate) {
		this.filDate = filDate;
	}


	public String getFilDate2() {
		return filDate2;
	}


	public void setFilDate2(String filDate2) {
		this.filDate2 = filDate2;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	
}
