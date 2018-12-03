package com.znzz.app.instrument.modules.service;

import java.util.List;

import org.nutz.lang.util.NutMap;

import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_YunGateway;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.YunDeviceBean;
import com.znzz.framework.base.service.BaseService;

public interface InsYunGatewayService extends BaseService<Ins_YunGateway>{
	
	/**
	 * 根据编号从云网表里获取集合
	 * @param code
	 * @param  
	 * @return
	 * */
	List<String> getCodeList(String code);

	/**
	 * 根据编号从云网表里获取集合
	 * @param code
	 * @return
	 */
	List<String> getDeviceCodeList(String code);

	/**根据编号查出设备名称，设备型号
	 * @param code
	 * @return
	 */
	Ins_DeviceInfo findDevicebyCode(String code);

	/**
	 * 插入或更新云网表，根据从json中拿出的数据
	 * @param yun
	 * @return 
	 */
	String insertOrUpdateFromJson(YunDeviceBean yun);
	
	void insertOrUpdate(Ins_YunGateway iyg);
	
	/**
	 * scada实时数据上传到云网
	 * */
	void uploadCloudbyRealTime();
	/**
	 * scada状态数据上传云网
	 * */
	void uploadCloudbyState();

	List<String> checkID(String code, String id);

	NutMap assetCodeListSelect();

	NutMap assetCodeListSelect2(String id);

}
