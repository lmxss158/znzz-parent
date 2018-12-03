package com.znzz.app.instrument.modules.models;

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
 * @author chenzhongliang
 *
 */
@Table("ins_yun_gateway")
@Comment("云网网关")
public class Ins_YunGateway extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@Comment("主键id")
	private Integer id;

	@Comment("网关id")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String gateway_id;

	@Comment("网关名称")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 300)
	private String gateway_name;

	@Comment("accesskey")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String accesskey;

	@Comment("设备编号")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String device_code;

	@Comment("设备id")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String device_id;

	@Comment("录入时间")
	@Column
	@ColDefine(type = ColType.DATETIME)
	private Date createTime;

	@Comment("备用字段1")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str1;

	@Comment("备用字段2")
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String str2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGateway_id() {
		return gateway_id;
	}

	public void setGateway_id(String gateway_id) {
		this.gateway_id = gateway_id;
	}

	public String getGateway_name() {
		return gateway_name;
	}

	public void setGateway_name(String gateway_name) {
		this.gateway_name = gateway_name;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
