package com.znzz.app.instrument.modules.models;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("ins_device_facility_hour")
@Comment("设备24小时运行统计")
public class Ins_DeviceFacilityHour {
	@Id
	@Column
	@Comment("id")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer id;

	@Column
	@Comment("日期(整点)")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer dateHour;

	@Column
	@Comment("运行台数")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer powerOnNum;

	@Column
	@Comment("监控台数")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer collectNum;

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

	public Integer getDateHour() {
		return dateHour;
	}

	public void setDateHour(Integer dateHour) {
		this.dateHour = dateHour;
	}

	public Integer getPowerOnNum() {
		return powerOnNum;
	}

	public void setPowerOnNum(Integer powerOnNum) {
		this.powerOnNum = powerOnNum;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
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
