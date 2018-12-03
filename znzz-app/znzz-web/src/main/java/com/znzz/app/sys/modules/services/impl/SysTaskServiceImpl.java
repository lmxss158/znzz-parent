package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.services.SysTaskService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 任务service实现类.
 */
@IocBean(args = {"refer:dao"})
public class SysTaskServiceImpl extends BaseServiceImpl<Sys_task> implements SysTaskService {
    public SysTaskServiceImpl(Dao dao) {
        super(dao);
    }
}
