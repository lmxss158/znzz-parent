package com.znzz.app.sys.modules.services;

import java.util.List;

import com.znzz.app.sys.modules.models.Sys_employee;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.framework.base.service.BaseService;

/**
 * 用户service接口
 * @author lizhihong
 *
 */
public interface SysEmployeeService extends BaseService<Sys_user> {

	String insertList(List<Sys_user> employeeList);

	boolean existDeviceinfo(String id);

	
}
