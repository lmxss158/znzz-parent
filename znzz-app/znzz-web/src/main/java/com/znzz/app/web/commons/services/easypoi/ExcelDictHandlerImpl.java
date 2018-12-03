package com.znzz.app.web.commons.services.easypoi;

import java.util.List;
import java.util.Map;

import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import com.znzz.app.sys.modules.services.SysDictService;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;

@IocBean
public class ExcelDictHandlerImpl implements IExcelDictHandler {
	@Inject
	private SysDictService dictService;
	/* 
	 * 获取字典对应Desc
	 */
	@Override
	public String toName(String dict, Object obj, String name, Object value) {
		if(value == null){
			return null;
		}
		String dictJson = dictService.getDictFromRedis(dict);
		if(dictJson != null && !"".equals(dictJson)) {
			List<Map<String,Object>> dictList = (List<Map<String,Object>>) Json.fromJson(dictJson);
			for (Map<String,Object> map : dictList) {
				if(value.toString().equals(map.get("code"))){
					return map.get("name").toString();
				}
			}
			
		}
		return null;
	}
	/* 
	 * 获取字典对应code
	 */
	@Override
	public String toValue(String dict, Object obj, String name, Object value) {
		if(value==null){
			return null;
		}
		String dictJson = dictService.getDictFromRedis(dict);
		if(dictJson != null && !"".equals(dictJson)) {
			
			List<Map<String,Object>> dictList = (List<Map<String,Object>>)Json.fromJson(dictJson);
			for (Map<String,Object> map : dictList) {
				
				if(value.toString().equals(map.get("name"))){
					return map.get("code").toString();
				}
			}
			
		}
		return null;
	}
	
}
