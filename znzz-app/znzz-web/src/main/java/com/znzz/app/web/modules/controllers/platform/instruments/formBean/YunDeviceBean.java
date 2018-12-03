package com.znzz.app.web.modules.controllers.platform.instruments.formBean;

import java.util.List;

import org.nutz.json.JsonField;

public class YunDeviceBean {
	
	@JsonField("iotmsg")
	private Iotmsg iotmsg;
	
	private Integer equipmentcount;
	
	/*@JsonField("colldatatypeDict")
	private ColldatatypeDict colldatatypeDict;
	
	@JsonField("readorwriteDict")
	private ReadorwriteDict readorwriteDict;*/
	
	@JsonField("equipmentmasg")
	private List<Equipment> equipmentmasg;
	
	private String dataurl;
	
	private String stateurl;
	
	
	

/*	@Override
	public String toString() {
		return "json文件数据已取出！！！！！！！！！！！！！！！！";
	}
*/
	public Iotmsg getIotmsg() {
		return iotmsg;
	}

	public void setIotmsg(Iotmsg iotmsg) {
		this.iotmsg = iotmsg;
	}

	public Integer getEquipmentcount() {
		return equipmentcount;
	}

	public void setEquipmentcount(Integer equipmentcount) {
		this.equipmentcount = equipmentcount;
	}

	public List<Equipment> getEquipmentmasg() {
		return equipmentmasg;
	}

	public void setEquipmentmasg(List<Equipment> equipmentmasg) {
		this.equipmentmasg = equipmentmasg;
	}

	public String getDataurl() {
		return dataurl;
	}

	public void setDataurl(String dataurl) {
		this.dataurl = dataurl;
	}

	public String getStateurl() {
		return stateurl;
	}

	public void setStateurl(String stateurl) {
		this.stateurl = stateurl;
	}
	
	
	
}
