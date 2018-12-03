package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;

import com.znzz.app.instrument.modules.models.Ins_Gateway;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.framework.base.service.BaseService;

public interface InsbcardService extends BaseService<Ins_DeviceBcard>{

	/**
	 * 检验网关编号是否存在
	 * @param gatewayCode
	 * @param cnd
	 * @return
	 */
	boolean checkGatewayCode(String gatewayCode, Cnd cnd);
	/**
	 * 批量插入b卡
	 */
	void insertList(List<Ins_DeviceBcard> Ins_DeviceBcardList);
	boolean checkbcardCode(String bcardCode, String bcardCode2, Cnd cnd);

	boolean checkorignCode(String id, String orignCode2, Cnd cnd);
	boolean checkbcardCode_add(String bcardCode, Cnd cnd);

	boolean checkorignCode_add(String orignCode, Cnd cnd);

	
	/**
	 * 获取生产编号所对应的B绑定的设备统一code
	 * @return
	 */
	Map<String,String> getOrignCodeAndDeviceCode() ;
	/**
	 * 删除生产编号对应设备统一编号的key
	 */
	void delOrignCodeAndDeviceCodeKey();
	
	/**
	 * 将统一编号与采集器编号的关系转为统一编号与生产编号的关系
	 */
	Map<String,String> convertClollectCodeAndDeviceCode(List<CollectBindDeviceBean> list);
	

}
