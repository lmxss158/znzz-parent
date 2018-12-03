package com.znzz.app.asset.moudules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import com.znzz.framework.base.model.BaseModel;


/**
 * @ClassName: Ins_Asset_lend_record
 * @Description: (仪器室设备借调记录)
 * @author fengguoxin
 * @date 2017年8月16日 下午5:27:49
 */ 
@Table("ins_asset_lend_record")
@Comment("仪器室设备借调记录")
public class Ins_Asset_lend_record extends BaseModel implements Serializable{

	
	    private static final long serialVersionUID = -5976790506952896061L;

		@Id
		@Column
		@Comment("id")
		@ColDefine(type = ColType.INT, width = 10)
		private Integer id;

		@Column
		@Comment("统一编号")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String assetCode;
		
		@Column
		@Comment("办理单位")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String originalDepart;
		
		@Column
		@Comment("原使用单位")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String borrowDepart;
		
		@Column
		@Comment("原使用人")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String chargePerson;

		@Column
		@Comment("现使用单位")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String applyDepart;

		@Column
		@Comment("现使用人")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String applyPerson;
		
		@Column
		@Comment("办理人")
		@ColDefine(type = ColType.VARCHAR, width = 60)
		private String handlePerson;
		
		@Column
		@Comment("事件类型，0借出1归还(仪器室角度),2转账")
		@ColDefine(type = ColType.INT, width = 1)
		private Integer actionType;
		
		@Comment("办理日期")
		@Column
		@ColDefine(type = ColType.DATETIME)
		private Date oprateTime;

		@Comment("转账日期")
		@Column
		@ColDefine(type = ColType.DATETIME)
		private Date transferAccountsTime;

		@Column
		@Comment("备注，记录原因")
		@ColDefine(type = ColType.VARCHAR, width = 300)
		private String remark;
		
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

		public String getOriginalDepart() {
			return originalDepart;
		}

		public void setOriginalDepart(String originalDepart) {
			this.originalDepart = originalDepart;
		}

		public String getApplyDepart() {
			return applyDepart;
		}

		public void setApplyDepart(String applyDepart) {
			this.applyDepart = applyDepart;
		}

		public String getApplyPerson() {
			return applyPerson;
		}

		public void setApplyPerson(String applyPerson) {
			this.applyPerson = applyPerson;
		}

		public String getHandlePerson() {
			return handlePerson;
		}

		public void setHandlePerson(String handlePerson) {
			this.handlePerson = handlePerson;
		}

		public Integer getActionType() {
			return actionType;
		}

		public void setActionType(Integer actionType) {
			this.actionType = actionType;
		}

		public Date getOprateTime() {
			return oprateTime;
		}

		public void setOprateTime(Date oprateTime) {
			this.oprateTime = oprateTime;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
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

	public Date getTransferAccountsTime() {
		return transferAccountsTime;
	}

	public void setTransferAccountsTime(Date transferAccountsTime) {
		this.transferAccountsTime = transferAccountsTime;
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
}
