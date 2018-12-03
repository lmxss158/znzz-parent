package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHistory;
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHour;
import com.znzz.app.instrument.modules.models.Ins_DevicePower;
import com.znzz.app.instrument.modules.models.Ins_DeviceUseTrend;
/**
 * 首页统计接口
 * @author wangqiang
 *
 */
public interface InsHomeService {
	/**
	 * 统计首页饼状图各种数据
	 * @return
	 */
	Map getHomeAllCount(String unitIds) ;
	
	/**
	 * 更新开机数与采集数
	 * @return
	 */
	void updateInsDeviceFacilityHourBean(Ins_DeviceFacilityHour facilityHour) ;
	/**
	 * 根据时间点获取当前的bean
	 * @param hour
	 * @return
	 */
	Ins_DeviceFacilityHour getDeviceFacilityHourBean(Integer hour) ;
	
	
	/**
	 * 获取设备24小时运行状况列表
	 * @return
	 */
	Map<String,String> getDeviceFacilityHourList() ;
	
	
	/**
	 * 获取每个单位的采集数量
	 */
	Map<String ,Integer> getUnitCollectNum() ;
	
	/**
	 * 获取每个单位开机数量
	 * @return
	 */
	Map<String,Integer> getUnitPowerOnNum() ;
	
	/**
	 * 获取今日单位设备历史记录
	 * @param unitId
	 * @return
	 */
	Map<String,Ins_DeviceFacilityHistory> getTodayUnitHistory(String unitId) ;
	
	/**
	 * 获取今日单位设备使用趋势
	 * @param unitId
	 * @return
	 */
	public Map<String, Ins_DeviceUseTrend> getTodayUnitUseTrend(String unitId);
	
	
	/**
	 * 插入list
	 * @param list
	 */
	void insertListHistory(List<Ins_DeviceFacilityHistory> list) ;
	/**
	 * 更新list
	 * @param list
	 */
	void updateListHistory(List<Ins_DeviceFacilityHistory> list) ;
	/**
	 * 插入list
	 * @param list
	 */
	void insertListUseTrend(List<Ins_DeviceUseTrend> list) ;
	/**
	 * 更新list
	 * @param list
	 */
	void updateListUseTrend(List<Ins_DeviceUseTrend> list) ;
	/**
	 * 首页展示单位设备数据数据
	 * @return
	 */
	JSONObject getHomeUnitHistory(String unitId,String time) ;
	/**
	 * 首页24小时使用趋势数据
	 * @param unitId
	 * @param time
	 * @return
	 */
	JSONObject getHomeUseTrend(String unitId, String time);
	/**
	 * 获取顶级下所有单位近六月的平均使用时长,并且map中带月份
	 */
	List<Map<String,String>> getUseTimeNum(String unitIds) ;
	
	/**
	 * 获取顶级下所有单位各类设备当月使用率
	 */
	List<Map> getCurDeviceUseRate(String unitIds,int page,int rows) ;
	
	
	/**
	 * 查询单位下受监控的总台数 
	 */
	Map<String,String> getUnitDeviceNum(String unitIds) ;
	
	/**
	 * 查询该单位下的设备列表
	 * @param unitiIds
	 * @return
	 */
	NutMap  getUnitDeviceList(String unitiIds,String assettype,String deviceState,int pageNo,int pageSize) ;
	
	
	/**
	 * 获取单位与受监控设备对应关系
	 * @return
	 */
	Map<String,List<String>> getUnitDevice() ;
	
	/**
	 * 转换为顶级单位与设备列表关系
	 * 顶级与子级关系Map
	 * 单位与设备Map
	 * @return
	 */
    public Map<String,List<String>> converteMap(Map<String,List<String>> tMap ,Map<String,List<String>> unitDeviceMap) ;
    
    /**
     * 获取今日单位功率记录
     * @param unitId
     * @return
     */
    public Map<String, Ins_DevicePower> getTodayUnitPower(String unitId) ;
    
    /**
     * 插入或更新单位功率
     * @param insertPower
     * @param updatePower
     */
    public void insertOrUpdateUnitPower(List<Ins_DevicePower> inserList,List<Ins_DevicePower> updateList) ;
    
    /**
     * 获取每五分钟的单位功率数据
     */
    
    public Map<String,String> getUnitPower(String topUnitId,String time) ;
	/**
	 * 获取前一个小时时间段内的开机数
	 * @return
	 */
	Map<String,Integer> getLastHourPowerOnNum() ;
	
	/**
	 * 查询各个单位的开机离线数量
	 * @param unitIds
	 * @return
	 */
	Map getHomeAllCountByDepart(String unitIds) ;
	/**
	 * 根据传入单位id,查询该单位最近七天日使用时长大于60分钟的设备数量
	 * @param unitIds
	 * @return
	 */
	Map getHomePowerOnCountByDepartFrDayDuration(String unitIds) ;
	
	/**
	 * 获取每个单位的接入数量
	 */
	Map<String ,Integer> getUnitLinkCollectNum(String unitIds) ;
}
