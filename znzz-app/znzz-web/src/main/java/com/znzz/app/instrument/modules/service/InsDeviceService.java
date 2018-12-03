package com.znzz.app.instrument.modules.service;


import java.util.List;
import java.util.Map;

import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

public interface InsDeviceService extends BaseService<Ins_DeviceState>{
	/**
	 * 设备管理列表查询
	 * @param deviceCode
	 * @param borrowDepart
	 * @param chargePerson
	 * @param state
	 * @param outField
	 * @param powerState
	 * @param page
	 * @param rows
	 * @param draw
	 * @return
	 */
	NutMap findDeviceList(String deviceCode,String borrowDepart,String chargePerson,Integer state ,Integer outField,Integer powerState ,String location,int page ,int rows,int draw,List<DataTableOrder> order,List<DataTableColumn> columns );
	/**
	 * 更新设备的状态
	 * @param deviceCode
	 * @param state
	 * @param powerState
	 * @param locationIp
	 */
	void updateDeviceState(Ins_DeviceState inDeviceState) ;
	/**
	 * 获取设备状态
	 * @return
	 */
	List<Ins_DeviceState> getDeviceStateList() ;
	/**
	 * 获取在线且开机的设备状态
	 * @return
	 */
	List<Ins_DeviceState> getBootDeviceStateList() ;
	
	/**
	 * 删除指定设备状态
	 * @param deviceCode
	 */
	int delDeviceState(String[] deviceCode) ;
	
	/**
	 * 判断使用单位和责任人是否为空
	 * @param deviceCode
	 * @return
	 */
	boolean checkDeviceInfoDepartAndPerson(String deviceCode);
	
}
