package com.znzz.app.web.modules.controllers.platform.instruments.formBean;

import java.util.Date;

/**
 * 
 * <p> Description:操作excel表格批量插入 </p>
 * <p> Company: www.htfudao.com.cn </p>
 * @Package com.znzz.app.web.modules.controllers.platform.instruments.formBean
 * @author changzheng
 * @date 2017年8月31日 上午10:27:21
 * @version V1.0
 */
public class CollectBindDeviceBean {

	private Integer id;
	private String collectCode; // 采集器编号
	private String collectName; // 采集器名称
	
	private String deviceCode; // 统一编号
	private String deviceName; // 设备名称
	private Date createTime; // 录入时间
	private String borrowDepart;//使用单位
	private String chargePerson;//责任人
	private String deviceVersion;//设备型号
	
	
	
	
	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCollectCode() {
		return collectCode;
	}

	public void setCollectCode(String collectCode) {
		this.collectCode = collectCode;
	}

	public String getCollectName() {
		return collectName;
	}

	public void setCollectName(String collectName) {
		this.collectName = collectName;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
