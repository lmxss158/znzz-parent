package com.znzz.app.asset.moudules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;


/**
 * 单位资产表实体类
 * @author wangqiang
 *
 */
@Table("ins_assets_unit")
@Comment("单位资产表") 
public class Ins_Asset_Unit {

	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;

	@Name
	@Column
	@Comment("统一编号")
	@ColDefine(type = ColType.VARCHAR, width = 60, notNull = true)
	private String assetCode;

	@Column
	@Comment("责任单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String chargeDepart;

	@Column
	@Comment("责任人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String chargeMan;

	@Column
	@Comment("使用单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String userDepart;

	@Column
	@Comment("使用人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String userMan;

	@Column
	@Comment("利用率")
	@ColDefine(type = ColType.VARCHAR, width = 10)
	private String rate;

	@Column
	@Comment("当前位置")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String position;

	@Column
	@Comment("操作时间")
	@ColDefine(type = ColType.DATETIME)
	private Date operateTime;

	@Column
	@Comment("是否可审批;1:已审批 0:可审批")
	@ColDefine(type = ColType.INT, width = 10 ,notNull=true)
	private Integer status;

	@Column
	@Comment("备用字段1")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer ext1;

	@Column
	@Comment("备用字段2")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String ext2;

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

	public String getChargeDepart() {
		return chargeDepart;
	}

	public void setChargeDepart(String chargeDepart) {
		this.chargeDepart = chargeDepart;
	}

	public String getChargeMan() {
		return chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	public String getUserDepart() {
		return userDepart;
	}

	public void setUserDepart(String userDepart) {
		this.userDepart = userDepart;
	}

	public String getUserMan() {
		return userMan;
	}

	public void setUserMan(String userMan) {
		this.userMan = userMan;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getExt1() {
		return ext1;
	}

	public void setExt1(Integer ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

}
