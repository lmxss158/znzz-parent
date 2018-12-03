package com.znzz.app.instrument.modules.service;

import java.util.List;
import java.util.Map;

import org.nutz.dao.Cnd;

import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.instrument.modules.models.Ins_Gateway;
import com.znzz.framework.base.service.BaseService;

public interface InsGatewayService extends BaseService<Ins_Gateway>{

	/**
	 * 检验网关编号是否存在
	 * @param gatewayCode
	 * @param cnd
	 * @return
	 */
	boolean checkGatewayCode(String gatewayCode, Cnd cnd);
	int deleteByIp(String ip);
	
	String insertList(List<Ins_Gateway> Ins_GatewayLIST);
	/**
	 * 查询ip级对应的位置
	 * @return
	 */
	Map<String,String> getIpAndLocation() ;
}
