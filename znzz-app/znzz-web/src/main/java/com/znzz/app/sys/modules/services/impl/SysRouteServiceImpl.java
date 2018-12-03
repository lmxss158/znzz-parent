package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_route;
import com.znzz.app.sys.modules.services.SysRouteService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 路由service实现类
 */
@IocBean(args = {"refer:dao"})
public class SysRouteServiceImpl extends BaseServiceImpl<Sys_route> implements SysRouteService {
    public SysRouteServiceImpl(Dao dao) {
        super(dao);
    }
}