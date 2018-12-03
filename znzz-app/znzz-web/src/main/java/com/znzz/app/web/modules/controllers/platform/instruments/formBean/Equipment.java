package com.znzz.app.web.modules.controllers.platform.instruments.formBean;

import java.util.List;

public class Equipment {
	
	private String name;
	
	private String model;
	
	private String byname;
	
	private String devnum;
	
	private String equipment;
	
	private List<Keyname> keynames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getByname() {
		return byname;
	}

	public void setByname(String byname) {
		this.byname = byname;
	}

	public String getDevnum() {
		return devnum;
	}

	public void setDevnum(String devnum) {
		this.devnum = devnum;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public List<Keyname> getKeynames() {
		return keynames;
	}

	public void setKeynames(List<Keyname> keynames) {
		this.keynames = keynames;
	}
	
	
	
	
}
