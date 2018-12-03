package com.znzz.app.asset.moudules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 资产维修表实体类
 * 
 * @author wangqiang
 *
 */
@Table("ins_assets_repair")
@Comment("维修管理表")
public class Ins_Asset_Repair implements Serializable {
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;

	@Column
	@Comment("统一编号")
	@ColDefine(type = ColType.VARCHAR, width = 60, notNull = true)
	private String assetCode;

	@Column
	@Comment("申办单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String applyDepart;

	@Column
	@Comment("办理单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String operatorDepart;

	@Column
	@Comment("申办人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String applyPreson;

	@Column
	@Comment("办理人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String operatorPerson;

	@Column
	@Comment("联系人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String linkMan;

	@Column
	@Comment("联系电话")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String linkPhone;

	@Column
	@Comment("维修状态/0待审批1待维修2维修中3待验收4计量中5待领取6待报废7驳回8完结")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer status;

	@Column
	@Comment("备注")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String remark;

	@Column
	@Comment("操作时间")
	@ColDefine(type = ColType.DATETIME)
	private Date operatorTime;

	@Column
	@Comment("父级id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer pid;

	@Column
	@Comment("备用单位字段")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String remarkDepart;

	@Column
	@Comment("备用操作人字段")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String remarkMan;

	@Column
	@Comment("维修内容")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String repairContent;

	@Column
	@Comment("维修费用")
	@ColDefine(type = ColType.FLOAT, width = 60)
	private Double repairCost; 

	@Column
	@Comment("备用字段1")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer ext1;

	@Column
	@Comment("备用字段2")
	@ColDefine(type = ColType.VARCHAR, width = 200)
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

	public String getApplyDepart() {
		return applyDepart;
	}

	public void setApplyDepart(String applyDepart) {
		this.applyDepart = applyDepart;
	}

	public String getOperatorDepart() {
		return operatorDepart;
	}

	public void setOperatorDepart(String operatorDepart) {
		this.operatorDepart = operatorDepart;
	}

	public String getApplyPreson() {
		return applyPreson;
	}

	public void setApplyPreson(String applyPreson) {
		this.applyPreson = applyPreson;
	}

	public String getOperatorPerson() {
		return operatorPerson;
	}

	public void setOperatorPerson(String operatorPerson) {
		this.operatorPerson = operatorPerson;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkDepart() {
		return remarkDepart;
	}

	public void setRemarkDepart(String remarkDepart) {
		this.remarkDepart = remarkDepart;
	}

	public String getRemarkMan() {
		return remarkMan;
	}

	public void setRemarkMan(String remarkMan) {
		this.remarkMan = remarkMan;
	}

	public String getRepairContent() {
		return repairContent;
	}

	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}

	

	public Double getRepairCost() {
		return repairCost;
	}

	public void setRepairCost(Double repairCost) {
		this.repairCost = repairCost;
	}

	public Date getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
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
