package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_export_table;
import com.znzz.app.sys.modules.services.SysExportTableService;
import com.znzz.framework.base.service.BaseServiceImpl;

@IocBean(args = {"refer:dao"})
public class SysExportTableServiceImpl extends BaseServiceImpl<Sys_export_table> implements SysExportTableService{

	public SysExportTableServiceImpl(Dao dao) {
		super(dao);
	}

}
