package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityDay;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
/**
 * 设备使用时长接口
 * @author wangqiang
 *
 */
public interface InsDeviceFacilityService extends BaseService<Ins_DeviceFacility> {

	/**
	 * 查询设备使用状况list
	 * @param deviceCode
	 * @return
	 */
	NutMap findDeviceFacilityList(String deviceCode,String time,int length, int start, int draw,List<DataTableOrder> order,List<DataTableColumn> columns);
	
	/**
	 * 批量插入数据
	 * @param list
	 */
	void insertDeviceFacility(List<Ins_DeviceFacility> list) ;
	
	
	/**
	 * 获取子设备使用状况分配list
	 * @return
	 */
	NutMap findChildDeviceFacilityList(Integer Pid,int length, int start, int draw,List<DataTableOrder> order, List<DataTableColumn> columns) ;
	
	/**
	 * 获取总时长
	 */
	
	Map<String,Integer> findDurtaionById(Integer pid) ;
	
	/**
	 * 删除
	 * @param Id
	 */
	void delFacilityById(Integer Id) ;
	
	/**
	 * 获取当月list
	 * @return
	 */
	List<Ins_DeviceFacility> findCurMonthList() ;

	
	/**
	 * 插入或更新
	 * @param countMap
	 * @param dfMap
	 */
	void insertOrUpdate(Map<String, Integer> countMap, Map<String, Ins_DeviceFacility> dfMap);
	
	/**
	 * 获取指定单位下的设备使用时长
	 * @param month
	 * @param unitIds
	 * @return
	 */
	Map<String,String> findUnitDeviceUseTimeListByMonth(String month,String unitIds) ;
	
	
	/**
	 * 批量插入Ins_DeviceFacilityDay
	 * @param map
	 * @return
	 */
	void insertDeviceFacilityDay(Map<String,Integer> map) ;
	
	/**
	 * 获取最近三十天日使用时长
	 * @param deviceCode
	 * @return
	 */
	Map<String,Object> getDeviceFacilityDayLastThirtyDays(String deviceCode) ;
	
	/**
	 * 获取最近一年的 月使用时长
	 */
	
	Map<String,Object> getDeviceFacilityYear(String deviceCode) ;
	
	/**
	 * 获取最近时间,设备再各个项目中的使用时长
	 */
	List<JSONObject> getDeviceFacilityUseTimeOfProject(String deviceCode,Integer type) ;
	
	/**
	 * 更新设备离线使用时长
	 * @param deviceCode
	 * @param month
	 * @param time
	 */
	void updateDeviceFacilityOffLineTime(String deviceCode,String month,int time) ;
	
	/**
	 * 获取当月日使用时长列表
	 * @return
	 */
	Map<String,Integer> getCurnMonthList() ;
	
}
