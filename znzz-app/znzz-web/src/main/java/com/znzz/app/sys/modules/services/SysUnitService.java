package com.znzz.app.sys.modules.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Record;

import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.framework.base.service.BaseService;

/**
 * 单位service接口
 */
public interface SysUnitService extends BaseService<Sys_unit> {
    void save(Sys_unit unit, String pid);

    void deleteAndChild(Sys_unit unit);
    
    /**
     * 获取单位id与名称
     * @return
     */
    List<Record> getUnitIdAndUnitNameList(String unitIds) ;
    
    /**
     * 获取每个单位id&父级id
     */
    Map<String,String> getUnitIdAndParentId() ;
    
    /**
     * 获取单位所属顶级的单位id
     * @param untiId
     * @return
     */
    String getTopUnitId(Map<String,String> map,String untiId) ;
    
    /**
     * 转换map为 顶级 下级关系
     * @param map
     * @return
     */
    Map<String,List<String>> getTopUnitGroup(Map<String ,String> map) ;
    
    /**
     * 转换为父子级
     * @param map
     * @return
     */
    Map<String,List<String>> getUnitTreeGroup(Map<String,String> map) ;
    
    /**
     * 获取当级所有下属id
     * map为  父子级
     */
    
    void getChildUnitList(Map<String,List<String>> map ,String unitId,List<String> list) ;

	void insertList(List<Sys_unit> unitList);
	
	/**
	 * 根据机构编码修改id字段
	 * */
	/*
	void updateUnit(Sys_unit unit);*/
    /**
     * 获取父级下所有子级
     * @param parentId
     * @return
     */
	String getChildList(String parentId);

	/**
	 * 判断当前级单位以及子级单位下是否存在职工
	 * @param id
	 * @return
	 */
	boolean hashEmployee(String id);

	/**
	 * 所有单位id
	 * @return
	 */
	List<String> getAllUnitid();

	String getUnitName(String key);
	/**
	 * 获取map形式的单位名称与id
	 * @param unitIds
	 * @return
	 */
	Map<String,String> getUnitAndName(String unitIds) ;
}
