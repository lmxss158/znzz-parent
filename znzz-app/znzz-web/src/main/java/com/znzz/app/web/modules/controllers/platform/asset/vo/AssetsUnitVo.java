package com.znzz.app.web.modules.controllers.platform.asset.vo;

/**
 * 
 * @author wangqiang 封装前端传来参数 单位资产表
 */
public class AssetsUnitVo {
	private String assetCode;
	private String position;
	private String chargeDepart;
	private String useDepart;
	private String useMan;
	private String chargeMan;
	private String operateTime;
	private Integer opType;
	private String status;
	private Integer mark;
	private int length;
	private int start;
	private int draw;

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getChargeDepart() {
		return chargeDepart;
	}

	public void setChargeDepart(String chargeDepart) {
		this.chargeDepart = chargeDepart;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public String getUseDepart() {
		return useDepart;
	}

	public void setUseDepart(String useDepart) {
		this.useDepart = useDepart;
	}

	public String getUseMan() {
		return useMan;
	}

	public void setUseMan(String useMan) {
		this.useMan = useMan;
	}

	public String getChargeMan() {
		return chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}
}
