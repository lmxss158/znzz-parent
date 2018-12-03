package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_log;
import com.znzz.app.sys.modules.services.SysLogService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 日志service实现类
 * @author lizhihong
 *
 */
@IocBean(args = {"refer:dao"})
public class SysLogServiceImpl extends BaseServiceImpl<Sys_log> implements SysLogService {
    public SysLogServiceImpl(Dao dao) {
        super(dao);
    }
}
