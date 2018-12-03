package com.znzz.app.asset.moudules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 单位资产表记录实体类
 * 
 * @author wangqiang
 *
 */
@Table("ins_assets_unit_record")
@Comment("单位资产记录表")
public class Ins_Asset_Unit_Record {

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
	@Comment("操作状态 0:位置变更  1:申请 2:归还")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer operateType;

	@Column
	@Comment("位置")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String position;

	@Column
	@Comment("状态 0:申请中  1:同意 2:驳回")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer status;

	@Column
	@Comment("操作时间")
	@ColDefine(type = ColType.DATETIME)
	private Date operateTime;

	@Column
	@Comment("备注")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String remark;

	@Column
	@Comment("父级id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer pid;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
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

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column
	@Comment("备用字段1")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer ext1;

	@Column
	@Comment("备用字段2")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String ext2;

}
