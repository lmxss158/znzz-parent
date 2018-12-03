package com.znzz.app.sys.modules.services;

import java.util.List;

import com.znzz.app.sys.modules.models.Sys_menu;
import com.znzz.app.sys.modules.models.Sys_role;
import com.znzz.framework.base.service.BaseService;

/**
 * 菜单service接口
 */
public interface SysRoleService extends BaseService<Sys_role> {
    List<Sys_menu> getMenusAndButtons(String roleId);

    List<Sys_menu> getHeadMenus(String roleId);

    List<Sys_menu> getLoginMenus(String roleId);

    List<Sys_menu> getAllMenus(String roleId);

    List<Sys_menu> getDatas(String roleId);

    List<Sys_menu> getDatas();

    List<String> getPermissionNameList(Sys_role role);

    void del(String roleid);

    void del(String[] roleids);
}
