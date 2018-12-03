package com.znzz.app.instrument.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 周期检定实体类
 * 
 * @author chenzhongliang
 *
 */

@Table("ins_examine_record")
@Comment("周期检定记录表")
public class Ins_CycleExamine {

	@Id
	@Column
	@Comment("主键ID")
	@ColDefine(type = ColType.INT)
	private Integer id;

	@Column
	@Comment("资产编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String assetCode;

	@Column
	@Comment("资产名称")
	@ColDefine(type = ColType.VARCHAR, width = 60, notNull = true)
	private String assetName;

	@Column
	@Comment("使用单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String borrowDepart;

	@Column
	@Comment("责任人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String chargePerson;

	@Column
	@Comment("启用时间")
	@ColDefine(type = ColType.DATE)
	private Date enableTime;

	@Column
	@Comment("检定人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String examinePerson;

	@Column
	@Comment("检定结论:0合格，1不合格，2无法确定")
	@ColDefine(type = ColType.INT, width = 1)
	private Integer conclusion;

	@Column
	@Comment("检定日期")
	@ColDefine(type = ColType.DATE)
	private Date examineDate;

	@Column
	@Comment("到期检定日期")
	@ColDefine(type = ColType.DATE)
	private Date dueDate;

	@Column
	@Comment("过期原因")
	@ColDefine(type = ColType.VARCHAR, width = 300)
	private String overdueReaspn;
	@Column
	@Comment("备用字段1")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str1;

	@Column
	@Comment("备用字段2")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str2;

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

	public String getBorrowDepart() {
		return borrowDepart;
	}

	public void setBorrowDepart(String borrowDepart) {
		this.borrowDepart = borrowDepart;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public Date getEnableTime() {
		return enableTime;
	}

	public void setEnableTime(Date enableTime) {
		this.enableTime = enableTime;
	}

	public String getExaminePerson() {
		return examinePerson;
	}

	public void setExaminePerson(String examinePerson) {
		this.examinePerson = examinePerson;
	}

	public Integer getConclusion() {
		return conclusion;
	}

	public void setConclusion(Integer conclusion) {
		this.conclusion = conclusion;
	}

	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getOverdueReaspn() {
		return overdueReaspn;
	}

	public void setOverdueReaspn(String overdueReaspn) {
		this.overdueReaspn = overdueReaspn;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

}
