package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_export_column;
import com.znzz.app.sys.modules.services.SysExportColumnService;
import com.znzz.framework.base.service.BaseServiceImpl;

@IocBean(args = {"refer:dao"})
public class SysExportColumnServiceImpl extends BaseServiceImpl<Sys_export_column> implements SysExportColumnService{

	public SysExportColumnServiceImpl(Dao dao) {
		super(dao);
	}

}
