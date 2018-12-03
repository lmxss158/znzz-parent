package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 
 * @ClassName: InsAssertRuleServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author pengmantai
 * @date 2017年8月17日 上午10:55:59
 */
@IocBean(args = { "refer:dao" })
public class InsAssertRuleServiceImpl extends BaseServiceImpl<Ins_Asset_Rule> implements InsAssertRuleService {

	public InsAssertRuleServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public List<Ins_Asset_Rule> getDeviceVersionList(String deviceVersion) {
		/*String sqltest = "SELECT DISTINCT a.deviceVersion,a.assetName assetName,a.ggName ggName,a.accuracyClass accuracyClass,a.manufactor manufactor,a.originalValue originalValue FROM ins_assets_version a WHERE deviceVersion = "
				+ "\'"+deviceVersion+"\'";
		System.out.println(sqltest);*/
		Sql sql = Sqls.create("SELECT DISTINCT a.deviceVersion,a.assetName,a.id,a.urlImage ,a.createTime from ins_assets_version a WHERE a.deviceVersionOrg =  @deviceVersion");
		sql.setParam("deviceVersion", deviceVersion);
		Entity<Ins_Asset_Rule> entity = dao().getEntity(Ins_Asset_Rule.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		List<Ins_Asset_Rule> list = sql.getList(Ins_Asset_Rule.class);
		
		/*Sql sql = Sqls
				.create("SELECT DISTINCT a.deviceVersion,a.assetName assetName, FROM ins_assets_version a WHERE deviceVersion = "
						+ "\'"+deviceVersion+"\'")
				.setCallback(new SqlCallback() {
					List<Object> list = new ArrayList<>();

					@Override
					public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
						while (rs.next()) {
							Ins_Asset_Rule rule = new Ins_Asset_Rule();
							rule.setDeviceVersion(rs.getString("deviceVersion"));
							rule.setAssetName(rs.getString("AssetName"));
							rule.setGgName(rs.getString("ggName"));
							rule.setAccuracyClass(rs.getInt("accuracyClass"));
							rule.setManufactor(rs.getString("manufactor"));
							rule.setOriginalValue(rs.getInt("originalValue"));
							list.add(rule);
						}
						return list;
					}
				});
		List<Ins_Asset_Rule> list = dao().execute(sql).getList(Ins_Asset_Rule.class);*/

		return list;
	}

	@Override
	public void insertList(List<Ins_Asset_Rule> rulessList) {
		for (Ins_Asset_Rule asset_rule : rulessList) {
			if (asset_rule.getId() == null){
				dao().insert(asset_rule);
			}
		}
	}
	
	/**
	 * 级联更新相关表（ins_assets_info,ins_device_info）
	 * @param rule
	 */
	@Override
	public void updateAll(Ins_Asset_Rule rule) {
		Ins_Asset_Rule oldRule = this.fetch(rule.getId());
		/*if(oldRule.getDeviceVersion().equals(rule.getDeviceVersion()) && oldRule.getAssetName().equals(rule.getAssetName())){
			//型号、资产名称都没改变

		}else*/ 
		if(!oldRule.getDeviceVersion().equals(rule.getDeviceVersion())){
			//型号发生改变
			dao().update("ins_assets_info", Chain.make("deviceVersion", rule.getDeviceVersion()),Cnd.where("deviceVersion", "=",oldRule.getDeviceVersion()));
			dao().update("ins_device_info", Chain.make("deviceVersion", rule.getDeviceVersion()),Cnd.where("deviceVersion", "=",oldRule.getDeviceVersion()));
			dao().update("ins_device_info", Chain.make("deviceName", rule.getAssetName()),Cnd.where("deviceVersion", "=",oldRule.getDeviceVersion()));
		}else if(!oldRule.getAssetName().equals(rule.getAssetName())){
			//资产名称发生改变
			dao().update("ins_device_info", Chain.make("deviceVersion", rule.getDeviceVersion()),Cnd.where("deviceVersion", "=",oldRule.getDeviceVersion()));
			dao().update("ins_device_info", Chain.make("deviceName", rule.getAssetName()),Cnd.where("deviceVersion", "=",oldRule.getDeviceVersion()));
		}
		this.updateIgnoreNull(rule);
		
	}

	@Override
	public void saveOrUpdate(List<Ins_Asset_Rule> rulessList) {
		//遍历进行插入或者更新操作
		for (Ins_Asset_Rule rule : rulessList) {
			//id不为null,进行更新操作
			if(rule.getId() != null){
				dao().updateIgnoreNull(rule);
			}else {//进行插入操作
				dao().insert(rule);
			}
		}
	}

	@Override
	public String getDeviceName(String deviceVersion) {
		Ins_Asset_Rule rule = dao().fetch(Ins_Asset_Rule.class, Cnd.where("deviceVersion","=",deviceVersion));
		String assetName = rule.getAssetName();
		return assetName;
	}

	@Override
	public void updateList(List<Ins_Asset_Rule> ruleList) {
		for (Ins_Asset_Rule assetRule : ruleList) {
			if (assetRule.getId() != null){
				dao().updateIgnoreNull(assetRule);
			}else {
				dao().insert(assetRule);
			}
		}
	}

}
