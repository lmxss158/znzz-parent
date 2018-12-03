package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.framework.base.service.BaseService;

public interface InsDeviceInfoService extends BaseService<Ins_DeviceInfo>{
	
	/**
	 * excel资产导入时往device_info里插入数据
	 * @param deviceInfoList
	 */
	void insertList(List<Ins_DeviceInfo> deviceInfoList);

	/**
	 * 更新或导入device_info信息
	 * @param deviceInfoList
	 */
	void saveOrUpdate(List<Ins_DeviceInfo> deviceInfoList);

	/**
	 * 根据deviceCode获取device_info信息
	 * @param deviceCode
	 * @return
	 */
	Ins_DeviceInfo getDeviceInfo(String deviceCode);
	/**
	 * 查询获取使用单位和责任人
	 * @param deviceCode
	 * @return
	 */
	Map<String,String> getDepartAndChargePerson(String deviceCode) ;


	void updateList(List<Ins_DeviceInfo> deviceList);
}
