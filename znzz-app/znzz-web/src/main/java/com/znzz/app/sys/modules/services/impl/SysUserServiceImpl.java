package com.znzz.app.sys.modules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import com.znzz.app.sys.modules.models.Sys_menu;
import com.znzz.app.sys.modules.models.Sys_role;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysMenuService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.util.StringUtil;

/**
 * 用户service实现类
 */
@IocBean(args = {"refer:dao"})
public class SysUserServiceImpl extends BaseServiceImpl<Sys_user> implements SysUserService {
    public SysUserServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private SysMenuService sysMenuService;
    /**
     * 查询用户角色code列表
     *
     * @param user
     * @return
     */
    public List<String> getRoleCodeList(Sys_user user) {
        dao().fetchLinks(user, "roles");
        List<String> roleNameList = new ArrayList<String>();
        for (Sys_role role : user.getRoles()) {
            if (!role.isDisabled())
                roleNameList.add(role.getCode());
        }
        return roleNameList;
    }

    /**
     * 获取用户菜单
     * @param user
     */
    public void fillMenu(Sys_user user) {
        user.setMenus(getMenus(user.getId()));
        //计算左侧菜单
        List<Sys_menu> firstMenus = new ArrayList<>();
        Map<String, List<Sys_menu>> secondMenus = new HashMap<>();
        for (Sys_menu menu : user.getMenus()) {
            if (menu.getPath().length() > 4) {
                List<Sys_menu> s = secondMenus.get(StringUtil.getParentId(menu.getPath()));
                if (s == null) s = new ArrayList<>();
                s.add(menu);
                secondMenus.put(StringUtil.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            }
        }
        user.setFirstMenus(firstMenus);
        user.setSecondMenus(secondMenus);
        if (!Strings.isBlank(user.getCustomMenu())) {
            user.setCustomMenus(sysMenuService.query(Cnd.where("id", "in", user.getCustomMenu().split(","))));
        }
    }

    /**
     * 查询用户菜单权限
     *
     * @param userId
     * @return
     */
    public List<Sys_menu> getMenus(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f and a.isShow=@t and a.type='menu' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f",false);
        sql.params().set("t",true);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 查询用户菜单和按钮权限
     *
     * @param userId
     * @return
     */
    public List<Sys_menu> getMenusAndButtons(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f",false);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }
    /**
     * 查询用户拥有的data按钮
     *
     * @param userId
     * @return
     */
    public List<String> getUserPermissionData(String userId) {
        Sql sql = Sqls.create("SELECT c.permission FROM	sys_user_role a,	sys_role_menu b,	sys_menu c WHERE	a.roleId = b.roleId AND  b.menuId = c.id  AND c.type='data'  AND userId = @userId");
        sql.params().set("userId", userId);
     // 设置回调函数
 		sql = sql.setCallback(new SqlCallback() {

 					@Override
 					public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
 						List<String> list = new ArrayList<>();
 						while (rs.next()) {
 							list.add(rs.getString("permission"));
 						}
 						return list;
 					}
 				});
 		List<String> list = dao().execute(sql).getList(String.class); 
 		return list ;
    }

    /**
     * 加载用户快捷菜单
     * @param roleIds
     * @return
     */
    @Override
    public List<Sys_menu> getUserFastIntoMenu(List<Sys_menu> allMenu ,String roleIds) {

        List<Sys_menu> list = null ;
        if(StringUtils.isNotBlank(roleIds)){
            Sql sql = Sqls.create("SELECT b.id,b.href from sys_role_headmenu a LEFT JOIN sys_menu b ON a.menuId = b.id  $condition");
            Cnd cnd = Cnd.NEW();
            cnd.where().andIn("a.roleId", roleIds);
            sql.setCondition(cnd) ;
            // 设置回调函数
            sql = sql.setCallback(new SqlCallback() {

                @Override
                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                    List<Sys_menu> list = new ArrayList<>();
                    while (rs.next()) {
                        Sys_menu menu = new Sys_menu();
                           menu.setId(rs.getString("id"));
                            menu.setHref(rs.getString("href"));
                            list.add(menu);
                    }
                    return list;
                }
            });
            list = dao().execute(sql).getList(Sys_menu.class);

        }

        return list;
    }

    /**
     * 获取登录跳转菜单
     * @param roleId
     * @return
     */
    @Override
    public Sys_menu getLoginMenu(String roleId) {
        Sql sql = Sqls.create("SELECT b.* from sys_role_loginmenu a ,sys_menu b WHERE a.menuId = b.id AND a.roleId =@roleId");
        sql.setParam("roleId",roleId);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        List<Sys_menu> list = sql.getList(Sys_menu.class);
        Sys_menu menu = null ;
        if(list.size()>0){
            menu = list.get(0) ;
        }

        return menu;

    }


    /**
     * 查询用户按钮权限
     *
     * @param userId
     * @return
     */
    public List<Sys_menu> getDatas(String userId) {
        Sql sql = Sqls.create("select distinct a.* from sys_menu a,sys_role_menu b where a.id=b.menuId  and " +
                " b.roleId in(select c.roleId from sys_user_role c,sys_role d where c.roleId=d.id and c.userId=@userId and d.disabled=@f) and a.disabled=@f and a.type='data' order by a.location ASC,a.path asc");
        sql.params().set("userId", userId);
        sql.params().set("f",false);
        Entity<Sys_menu> entity = dao().getEntity(Sys_menu.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Sys_menu.class);
    }

    /**
     * 删除一个用户
     *
     * @param userId
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteById(String userId) {
        dao().clear("sys_user_unit", Cnd.where("userId", "=", userId));
        dao().clear("sys_user_role", Cnd.where("userId", "=", userId));
        //dao().clear("sys_user", Cnd.where("id", "=", userId));
        //Chain m1 = Chain.make("loginname", "");
        Chain m2 = Chain.make("password", "");
        Chain m3 = Chain.make("userStatus", "3");
        //dao().update("sys_user", m1, Cnd.where("id", "=", userId));
        dao().update("sys_user", m2, Cnd.where("id", "=", userId));
        dao().update("sys_user", m3, Cnd.where("id", "=", userId));
    }

    /**
     * 批量删除用户
     *
     * @param userIds
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteByIds(String[] userIds) {
        dao().clear("sys_user_unit", Cnd.where("userId", "in", userIds));
        dao().clear("sys_user_role", Cnd.where("userId", "in", userIds));
        //dao().clear("sys_user", Cnd.where("id", "in", userIds));
        //Chain m1 = Chain.make("loginname", "");
        Chain m2 = Chain.make("password", "");
        Chain m3 = Chain.make("userStatus", "3");
        //dao().update("sys_user", m1, Cnd.where("id", "in", userIds));
        dao().update("sys_user", m2, Cnd.where("id", "in", userIds));
        dao().update("sys_user", m3, Cnd.where("id", "in", userIds));
    }

    /**
     * 加载用户id,用户名称,单位id,单位名称
     */
	@Override
	public List<Record> getUserIdAndUserName(String unitId,Integer type) {
		
		
		
		Sql sql = Sqls.create("SELECT a.id, a.username,a.unitid,b.`name` from sys_user a, sys_unit b WHERE a.unitid = b.id $var $userStatus") ;
		String str="" ;
		String userStatus="" ;
		if(StringUtils.isNotBlank(unitId)){
			str=" and a.unitid in ("+unitId+")" ;
			sql.setVar("var", str) ;
		}
		
		if(type!=null){//查非管理员的
			userStatus=" and a.userStatus !=1" ;
		}else{
			userStatus=" and a.userStatus =3" ;
		}
		sql.setVar("userStatus", userStatus) ;
		
		sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Record.class);
	}
	

	/** 
	 *加载某单位及其下子单位 所有下职工信息
	 */
	@Override
	public List<Record> getEmployeeList(String unitid) {
	/*	Sql sql = Sqls.create("select a.id,a.`name` as username,a.unitid,b.`name` "
							+ "from sys_employee a,sys_unit b where a.unitid=b.id $var") ;*/
		Sql sql = Sqls.create("select se.id,se.username,se.unitid,su.`name`  from sys_user se, "+
                              "(select distinct f.id,f.`name` from sys_unit a " + 
                              "inner join sys_unit b on a.id=b.parentId or a.id=b.id "+
                              "inner join sys_unit c on b.id=c.parentId or b.id=c.id "+
                              "inner join sys_unit d on b.id=d.parentId or c.id=d.id "+
		                      "inner join sys_unit e on b.id=e.parentId or d.id=e.id "+
		                      "inner join sys_unit f on b.id=f.parentId or e.id=f.id $var) su "+
                              "where se.unitid=su.id and se.userStatus='3'");
		String str="";
		if(StringUtils.isNotBlank(unitid)){
			str=" where a.id='"+unitid+"'" ;
			sql.setVar("var", str) ;
			//sql.setParam("id", unitid);
		}
		sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    System.out.println(1234567);
	    return sql.getList(Record.class);
	}

	

}
