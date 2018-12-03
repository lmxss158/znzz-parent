package com.znzz.app.web.modules.controllers.platform.instruments.formBean;

public class SwitchingFlowForm {
	private String deviceCode;
	private Integer outField;
	private String borrowDepart;
	private String chargePerson;
	private String powerOnTime;
	private String powerOffTime;
	private int start;
	private int length;
	private int draw;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
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

	public String getPowerOnTime() {
		return powerOnTime;
	}

	public void setPowerOnTime(String powerOnTime) {
		this.powerOnTime = powerOnTime;
	}

	public String getPowerOffTime() {
		return powerOffTime;
	}

	public void setPowerOffTime(String powerOffTime) {
		this.powerOffTime = powerOffTime;
	}
}
