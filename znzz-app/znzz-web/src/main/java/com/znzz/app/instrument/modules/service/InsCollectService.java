package com.znzz.app.instrument.modules.service;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.DeviceForm;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

public interface InsCollectService extends BaseService<Ins_Collect>{
	
	
	/**
	 * 采集器表,设备表分页查询
	 * @param length
	 * @param start
	 * @param draw
	 * @param order
	 * @param columns
	 * @param cnd
	 * @param object2
	 * @return
	 */
	NutMap getCollectDataWith(DeviceForm deviceForm,int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns,
			Cnd cnd, String object2);

	/**
	 * 获得所有采集器编号
	 * @param collectCode
	 * @return
	 */
	List<String> getCollectCodeList(String collectCode);

	/**
	 * 获得所有的设备标号
	 * @param deviceCode
	 * @return
	 */
	List<String> getDeviceCodeList(String deviceCode);

	/**
	 * 获得所有设备信息
	 * @param deviceCode
	 * @return
	 */
	List<CollectBindDeviceBean> getDeviceInfo(String deviceCode);

	/**
	 * 批量插入采集器数据
	 * @param collect_devicetList
	 */
	String insertList(List<CollectBindDeviceBean> collect_devicetList);

	/**
	 * 导出所有的采集器数据
	 * @return
	 */
	List<CollectBindDeviceBean> getExportList();

	/**
	 * 获得B卡的编号(即采集器编号)
	 * @return
	 */
	NutMap getBCardList();

	/**
	 * 获得所有的统一编号
	 * @param deviceCode
	 * @return
	 */
	NutMap getDeviceCodeList();

	/**
	 * 测是否绑定
	 * @param deviceCode
	 * @return
	 */
	boolean isBinding(String deviceCode) ;
	/**
	 * 获取所有受监控的设备统一编码
	 * @return
	 */
	List<String> getMonitorDeviceCodeList() ;
}
