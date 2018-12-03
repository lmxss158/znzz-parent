package com.znzz.app.asset.moudules.services;

import java.util.List;

import org.nutz.lang.util.NutMap;

import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.framework.base.service.BaseService;
/**
 * 
 * @ClassName: InsAssertRuleService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author pengmantai
 * @date 2017年8月17日 上午10:56:05
 */
public interface InsAssertRuleService extends BaseService<Ins_Asset_Rule> {
	/**
	 * 检验型号是否存在
	 * @param deviceVersion
	 * @return
	 */
	List<Ins_Asset_Rule> getDeviceVersionList(String deviceVersion);

	void insertList(List<Ins_Asset_Rule> rulessList);

	/**
	 * 级联更新相关表（ins_assets_info,ins_device_info）
	 * @param rule
	 */
	void updateAll(Ins_Asset_Rule rule);

	/**
	 * 更新或者导入型号信息
	 * @param rulessList
	 */
	void saveOrUpdate(List<Ins_Asset_Rule> rulessList);

	/**
	 * 根据deviceVersion获取名称
	 * @param deviceVersion
	 * @return
	 */
	String getDeviceName(String deviceVersion);

	void updateList(List<Ins_Asset_Rule> ruleList);
}
