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

@Table("ins_collect")
@Comment("采集器表")
public class Ins_Collect extends BaseModel implements Serializable{
	private static final long serialVersionUID = 7404475755392625615L;

	@Id
	@Comment("主键id")
	@Column
	@ColDefine(type = ColType.INT)
	private Integer id;

	@Column
	@Comment("采集器编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String collectCode;

	@Column
	@Comment("采集器名称")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String collectName;
	
	@Column
	@Comment("统一编号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String deviceCode;
	
	@Column
	@Comment("录入时间")
	@ColDefine(type = ColType.DATETIME)
	protected Date createTime;

	
	
	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getCollectName() {
		return collectName;
	}

	public void setCollectName(String collectName) {
		this.collectName = collectName;
	}

	public String getCollectCode() {
		return collectCode;
	}

	public void setCollectCode(String collectCode) {
		this.collectCode = collectCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

}
