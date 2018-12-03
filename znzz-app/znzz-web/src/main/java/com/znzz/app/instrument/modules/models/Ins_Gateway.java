package com.znzz.app.instrument.modules.models;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import com.znzz.framework.base.model.BaseModel;

@Table("ins_gateway")
@Comment("网关表-A卡")
public class Ins_Gateway extends BaseModel implements Serializable{

	private static final long serialVersionUID = 3838708928926028026L;

	@Id
	@Column
	private Integer id;
	
	@Comment("网关编号")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String gatewayCode;
	
	@Comment("网关名称")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String gatewayName;
	
	@Comment("位置")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String gatewayLocation;
	
	@Comment("ip")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String ip;
	
	@Comment("描述")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String remark;
	
	@Comment("录入时间")
	@Column
	@ColDefine(type = ColType.DATETIME)
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGatewayCode() {
		return gatewayCode;
	}

	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public String getGatewayLocation() {
		return gatewayLocation;
	}

	public void setGatewayLocation(String gatewayLocation) {
		this.gatewayLocation = gatewayLocation;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date  getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date  createTime) {
		this.createTime = createTime;
	}
	
	
	
}
