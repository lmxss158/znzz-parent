package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_import_table;
import com.znzz.app.sys.modules.services.SysImportTableService;
import com.znzz.framework.base.service.BaseServiceImpl;

@IocBean(args = {"refer:dao"})
public class SysImportTableServiceImpl extends BaseServiceImpl<Sys_import_table> implements SysImportTableService{

	public SysImportTableServiceImpl(Dao dao) {
		super(dao);
	}

}
