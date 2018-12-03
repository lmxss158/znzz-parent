package com.znzz.app.sys.modules.services.impl;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_import_column;
import com.znzz.app.sys.modules.services.SysImportColumnService;
import com.znzz.framework.base.service.BaseServiceImpl;

@IocBean(args = {"refer:dao"})
public class SysImportColumnServiceImpl extends BaseServiceImpl<Sys_import_column> implements SysImportColumnService{

	public SysImportColumnServiceImpl(Dao dao) {
		super(dao);
	}

}
