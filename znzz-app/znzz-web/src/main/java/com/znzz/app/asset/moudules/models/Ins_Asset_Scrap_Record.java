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
@Table("ins_assets_scrap_record")
@Comment("报废记录表")
public class Ins_Asset_Scrap_Record extends BaseModel implements Serializable{

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
	@Comment("报废:0初始状态/1待报废/2报废")
	@ColDefine(type = ColType.INT, width = 1)
	private Integer scrapState;
	
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

	public Integer getScrapState() {
		return scrapState;
	}

	public void setScrapState(Integer scrapState) {
		this.scrapState = scrapState;
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
