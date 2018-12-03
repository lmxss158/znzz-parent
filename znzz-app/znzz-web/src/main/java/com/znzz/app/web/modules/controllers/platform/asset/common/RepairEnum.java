package com.znzz.app.web.modules.controllers.platform.asset.common;
/**
 * 维修状态枚举
 * @author wangqiang
 *
 */
public enum RepairEnum {
	
	/**
	 * 待审批
	 */
	APPROVE,
	/**
	 * 待维修
	 */
	REPAIR,
	/**
	 * 维修中
	 */
	REPAIRING,
	/**
	 * 待验收
	 */
	ACCEPTING,
	/**
	 * 计量中
	 */
	METERING,
	/**
	 * 待领取
	 */
	RECEIVE,
	/**
	 * 报废
	 */
	SCRAPPED,
	/**
	 * 驳回
	 */
	REFUSE,
	/**
	 * 完结
	 */
	FINISH,
	
	/**
	 * 设备
	 */
	DEVICE, 
	
	/**
	 * 仪器
	 */
	INSTRUMENT ,
	
	/**
	 * 仪器室code
	 */
	YQCODE ,
	/**
	 * 返回维修
	 */
	BACKREPARING ;
	
	
	/**
	 * 获取枚举类型数值
	 */
	public Integer toValue() {
	
		return this.ordinal();
	}
	/**
	 * 获取枚举类型名称
	 */
	public String toString() {
		
		if (this.equals(APPROVE)) { return "待接收"; }
		if (this.equals(REPAIR)) { return "等待维修"; }
		if (this.equals(REPAIRING)) { return "维修中"; }
		if (this.equals(ACCEPTING)) { return "待验收"; }
		if (this.equals(METERING)) { return "计量中"; }
		if (this.equals(RECEIVE)) { return "待领取"; }
		if (this.equals(SCRAPPED)) { return "无法维修"; }
		if (this.equals(REFUSE)) { return "驳回"; }
		if (this.equals(FINISH)) { return "完结"; }
		if (this.equals(DEVICE)) { return "设备"; }
		if (this.equals(INSTRUMENT)) { return "仪器"; }
		if (this.equals(YQCODE)) { return "YQ"; }
		if (this.equals(BACKREPARING)) { return "无法计量"; }
		return "";
	}
	
	/**
	 * 获取对应值
	 * @param value
	 * @return
	 */
	public static RepairEnum valueOf(Integer value) {
		return RepairEnum.values()[value];
	}
}
