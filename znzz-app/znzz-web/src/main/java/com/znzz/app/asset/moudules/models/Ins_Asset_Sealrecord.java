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
 * 封存记录实体类
 * @classname Ins_Asset_Sealrecord.java
 * @author chenzhongliang
 * @date 2017年9月5日
 */
@Table("ins_assets_seal_record")
@Comment("封存启封记录表")
public class Ins_Asset_Sealrecord extends BaseModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;
	
	@Column
	@Comment("资产编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String assetCode;
	
	@Column
	@Comment("封存状态:0在用/1封存/2闲置/3禁用")
	@ColDefine(type = ColType.INT, width = 1)
	private Integer sealStatus;
	
	@Column
	@Comment("操作人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String operator;
	
	@Column
	@Comment("操作时间")
	@ColDefine(type = ColType.DATETIME)
	private Date operateTime;

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

	public Integer getSealStatus() {
		return sealStatus;
	}

	public void setSealStatus(Integer sealStatus) {
		this.sealStatus = sealStatus;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	

}
