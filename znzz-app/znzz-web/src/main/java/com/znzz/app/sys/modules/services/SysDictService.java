package com.znzz.app.sys.modules.services;

import java.util.List;
import java.util.Map;

import com.znzz.app.sys.modules.models.Sys_dict;
import com.znzz.framework.base.service.BaseService;


public interface SysDictService extends BaseService<Sys_dict> {
    String getNameByCode(String code);
    String getNameById(String id);
    List<Sys_dict> getSubListByPath(String path);
    List<Sys_dict> getSubListById(String id);
    List<Sys_dict> getSubListByCode(String code);
    Map getSubMapByPath(String path);
    Map getSubMapById(String id);
    Map getSubMapByCode(String code);
    void save(Sys_dict dict, String pid);
    void deleteAndChild(Sys_dict dict);
    
    void deleteDictRedis();
    String getDictFromRedis(String key);
}
