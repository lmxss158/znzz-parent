package com.znzz.app.instrument.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("ins_switching_flow")
@Comment("开关机流水")
public class Ins_SwitchingFlow {
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;

	@Column
	@Comment("统一编号")
	@ColDefine(type = ColType.VARCHAR, width = 60, notNull = true)
	private String deviceCode;

	@Column
	@Comment("开机时间")
	@ColDefine(type = ColType.DATETIME)
	private Date powerOnTime;

	@Column
	@Comment("关机时间")
	@ColDefine(type = ColType.DATETIME)
	private Date powerOffTime;

	@Column
	@Comment("离线时间")
	@ColDefine(type = ColType.DATETIME)
	private Date outLineTime;

	@Column
	@Comment("使用时长(精确到分)")
	@ColDefine(type = ColType.INT)
	private Integer userTime;

	@Column
	@Comment("位置信息")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String locationInfo;

	@Column
	@Comment("是否外场,0:否,1:是")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer outField;

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

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public Date getPowerOnTime() {
		return powerOnTime;
	}

	public void setPowerOnTime(Date powerOnTime) {
		this.powerOnTime = powerOnTime;
	}

	public Date getPowerOffTime() {
		return powerOffTime;
	}

	public void setPowerOffTime(Date powerOffTime) {
		this.powerOffTime = powerOffTime;
	}

	public Date getOutLineTime() {
		return outLineTime;
	}

	public void setOutLineTime(Date outLineTime) {
		this.outLineTime = outLineTime;
	}

	public Integer getUserTime() {
		return userTime;
	}

	public void setUserTime(Integer userTime) {
		this.userTime = userTime;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Integer getOutField() {
		return outField;
	}

	public void setOutField(Integer outField) {
		this.outField = outField;
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
