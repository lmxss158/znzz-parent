package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_plugin;
import com.znzz.app.sys.modules.services.SysPluginService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 插件service实现类
 */
@IocBean(args = {"refer:dao"})
public class SysPluginServiceImpl extends BaseServiceImpl<Sys_plugin> implements SysPluginService {
    public SysPluginServiceImpl(Dao dao) {
        super(dao);
    }
}