package com.znzz.app.sys.modules.services;

import com.znzz.app.sys.modules.models.Sys_menu;
import com.znzz.framework.base.service.BaseService;

/**
 * 菜单service接口
 */
public interface SysMenuService extends BaseService<Sys_menu> {
    void save(Sys_menu menu, String pid);
    void deleteAndChild(Sys_menu unit);
}
