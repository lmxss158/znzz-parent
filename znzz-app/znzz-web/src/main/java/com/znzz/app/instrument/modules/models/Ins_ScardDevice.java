package com.znzz.app.instrument.modules.models;

import java.io.Serializable;
import java.util.List;

public class Ins_ScardDevice implements Serializable {
	private static final long serialVersionUID = -6187799907967830305L;
	private String deviceid;
	private String ip;
	private List<Ins_PointList> pointList;

	public Ins_ScardDevice() {
	}

	@Override
	public String toString() {
		return "Device [deviceid=" + deviceid + ", ip=" + ip + ", pointList=" + pointList + "]";
	}

	public String getDeviceId() {
		return deviceid;
	}

	public void setDeviceId(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<Ins_PointList> getPointList() {
		return pointList;
	}

	public void setPointList(List<Ins_PointList> pointList) {
		this.pointList = pointList;
	}

	public Ins_ScardDevice(String deviceId, String ip, List<Ins_PointList> pointList) {
		super();
		this.deviceid = deviceid;
		this.ip = ip;
		this.pointList = pointList;
	}

}
