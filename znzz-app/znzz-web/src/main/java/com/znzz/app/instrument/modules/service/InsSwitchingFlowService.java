package com.znzz.app.instrument.modules.service;

import java.util.List;

import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.instrument.modules.models.Ins_SwitchingFlow;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.SwitchingFlowForm;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

public interface InsSwitchingFlowService {
	/**
	 * 获取状态履历列表
	 * @param form
	 * @return
	 */
	NutMap findSwitchFlowList(SwitchingFlowForm form,List<DataTableOrder> order, List<DataTableColumn> columns) ;
	/**
	 * 根据deviceCode 获取该设备状态履历
	 * @param form
	 * @return
	 */
	NutMap findSwitchFlowByDeviceCode(SwitchingFlowForm form);
	/**
	 * 插入状态履历
	 * @param form
	 */
	void insertSwitchFlow(Ins_SwitchingFlow form) ;
	/**
	 * 更新状态履历
	 */
	void updateSwitchFlow(Ins_SwitchingFlow form);
	
	
	/**
	 * 根据deviceCode 获取最近的一条记录
	 */
	List<Ins_SwitchingFlow> fetchSwitchFlow(String deviceCode) ;
	
	/**
	 * 根据ip 获取位置名称
	 */
	String fetchLocationInfo(String ip) ;
	/**
	 * 删除履历表
	 * @param deviceCode
	 * @return
	 */
	int delSwitchFlow(String[] deviceCode) ;
	
	/**
	 * 查询当月的履历记录
	 * @return
	 */
	List<Ins_SwitchingFlow> findCurMonthList() ;
	
	
	/**
	 * 查询近三十天的履历记录
	 */
	
	List<Ins_SwitchingFlow> findCurThrityDaysList() ;
	
	/**
	 * 查询昨日一天的履历记录
	 * @return
	 */
	List<Ins_SwitchingFlow> findYesterDayList() ;
	/**
	 * 设置设置履历结束
	 * @param deviceCode
	 */
	void setSwitchFlowEnd(String deviceCode) ;
	
	/**
	 * 清除重复的开机履历数据
	 */
	void clearRepeateFlowData(String deviceCode) ;
}
