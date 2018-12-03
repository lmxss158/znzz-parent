package com.znzz.app.sys.modules.services;

import java.util.List;

import com.znzz.app.sys.modules.models.Sys_config;
import com.znzz.framework.base.service.BaseService;


public interface SysConfigService extends BaseService<Sys_config> {
    List<Sys_config> getAllList();
}
