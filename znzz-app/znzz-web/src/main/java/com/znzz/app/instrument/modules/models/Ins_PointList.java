package com.znzz.app.instrument.modules.models;

import java.io.Serializable;

public class Ins_PointList implements Serializable {
	
	private static final long serialVersionUID = 2548365793118156282L;
	private String point;
    private String value;
    
	public Ins_PointList(String point, String value, String alarm) {
		super();
		this.point = point;
		this.value = value;
	}
	
	public Ins_PointList() {
		
	}
	
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "PointList [point=" + point + ", value=" + value + "]";
	}
	;
	
	
}
