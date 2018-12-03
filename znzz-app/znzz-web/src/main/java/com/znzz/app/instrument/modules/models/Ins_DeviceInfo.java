package com.znzz.app.instrument.modules.models;

import org.nutz.dao.entity.annotation.*;

@Table("ins_device_info")
@Comment("设备信息表")
@TableIndexes({@Index(name="INDEX_DEVICE_INFO_DEVICECODE",fields={"deviceCode"},unique = true)})
public class Ins_DeviceInfo {
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
	@Comment("设备名称")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String deviceName;

	@Column
	@Comment("设备型号")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String deviceVersion;

	@Column
	@Comment("是否外场,0:否,1:是")
	@ColDefine(type = ColType.INT, width = 10)
	private Integer outField;

	@Column
	@Comment("使用单位")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String borrowDepart;

	@Column
	@Comment("责任人")
	@ColDefine(type = ColType.VARCHAR, width = 60)
	private String chargePerson;
	
	@Column
	@Comment("资产种类:（0全部1设备2仪器）")
	@ColDefine(type = ColType.INT, width = 1)
	private Integer assetType;
	
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

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public Integer getOutField() {
		return outField;
	}

	public void setOutField(Integer outField) {
		this.outField = outField;
	}

	public String getBorrowDepart() {
		return borrowDepart;
	}

	public void setBorrowDepart(String borrowDepart) {
		this.borrowDepart = borrowDepart;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
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
