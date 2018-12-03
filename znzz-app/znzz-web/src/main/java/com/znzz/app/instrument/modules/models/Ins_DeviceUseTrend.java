package com.znzz.app.instrument.modules.models;


import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("ins_device_use_trend")
@Comment("24小时设备使用趋势表")
public class Ins_DeviceUseTrend {
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;

	@Column
	@Comment("单位ID")
	@ColDefine(type = ColType.VARCHAR, width = 32)
	private String unitId;

	@Column
	@Comment("日期(天)")
	@ColDefine(type = ColType.DATETIME)
	private Date date;

	@Column
	@Comment("每小时数据记录")
	@ColDefine(type = ColType.VARCHAR, width = 500)
	private String historyData;

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


	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHistoryData() {
		return historyData;
	}

	public void setHistoryData(String historyData) {
		this.historyData = historyData;
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

