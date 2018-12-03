package com.znzz.app.instrument.modules.models;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table(value = "ins_device_facility")
@Comment("设备使用状况表")
public class Ins_DeviceFacility {
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
	@Comment("统计月份")
	@ColDefine(type = ColType.DATETIME)
	private Date month;

	@Column
	@Comment("使用时长")
	@ColDefine(type = ColType.INT, width = 10, notNull = true)
	private Integer duration;

	@Column
	@Comment("应用项目")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String projectCode;

	@Column
	@Comment("父级id")
	@ColDefine(type = ColType.INT, notNull = true)
	private Integer pid;

	@Column
	@Comment("子使用时长开始时间")
	@ColDefine(type = ColType.DATETIME)
	private Date chTimeStart;

	@Column
	@Comment("子使用时长结束时间")
	@ColDefine(type = ColType.DATETIME)
	private Date chTimeEnd;

	@Column
	@Comment("操作时间")
	@ColDefine(type = ColType.DATETIME)
	private Date operateTime;

	@Column
	@Comment("离线使用时长")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer offLineTime;

	@Column
	@Comment("备用字段1")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer ext1;

	@Column
	@Comment("备用字段2")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String ext2;

	public Date getChTimeStart() {
		return chTimeStart;
	}

	public void setChTimeStart(Date chTimeStart) {
		this.chTimeStart = chTimeStart;
	}

	public Date getChTimeEnd() {
		return chTimeEnd;
	}

	public void setChTimeEnd(Date chTimeEnd) {
		this.chTimeEnd = chTimeEnd;
	}

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

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getOffLineTime() {
		return offLineTime;
	}

	public void setOffLineTime(Integer offLineTime) {
		this.offLineTime = offLineTime;
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
