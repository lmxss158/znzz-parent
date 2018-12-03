package com.znzz.app.sys.modules.services;

import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Record;

import com.znzz.app.sys.modules.models.Sys_menu;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.framework.base.service.BaseService;

/**
 * 用户service接口
 * @author lizhihong
 *
 */
public interface SysUserService extends BaseService<Sys_user> {
    List<String> getRoleCodeList(Sys_user user);

    List<Sys_menu> getMenusAndButtons(String userId);

    List<Sys_menu> getDatas(String userId);

    void fillMenu(Sys_user user);

    void deleteById(String userId);

    void deleteByIds(String[] userIds);
    
    /**
     * 获取用户名称,用户id,单位id,单位名称列表
     * @param unitId
     * @return
     */
    List<Record> getUserIdAndUserName(String unitId,Integer type) ;

	/**
	 * 获取人员列表
	 * @param unitid
	 * @return
	 */
	List<Record> getEmployeeList(String unitid);

	//List<Record> getEmployeeSelect(String unitid);
	/**
	 * 获取用户权限data
	 * @param userId
	 * @return
	 */
	 public List<String> getUserPermissionData(String userId);

	/**
	 * 加载用户快捷菜单
	 * @param roleIds
	 * @return
	 */
	 public List<Sys_menu> getUserFastIntoMenu(List<Sys_menu> allMenu , String roleIds) ;

	/**
	 * 加载用户登录跳转页
	 * @param roleId
	 * @return
	 */
	 public Sys_menu getLoginMenu(String roleId);

}
