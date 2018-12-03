package com.znzz.app.web.modules.controllers.platform.asset.common;

public enum AssetUnitEnum {
	
	/**
	 * 位置变更
	 */
	POSITION ,
	/**
	 * 申请借入
	 */
	APPROVEIN ,
	/**
	 * 归还
	 */
	REVERT ;
	
	
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
		
		if (this.equals(POSITION)) { return "位置变更"; }
		if (this.equals(APPROVEIN)) { return "借入申请"; }
		if (this.equals(REVERT)) { return "归还"; }
		
		return "";
	}
	
	/**
	 * 获取对应值
	 * @param value
	 * @return
	 */
	public static AssetUnitEnum valueOf(Integer value) {
		return AssetUnitEnum.values()[value];
	}	
}
