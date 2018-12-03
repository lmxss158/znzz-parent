package com.znzz.app.web.modules.controllers.platform.sys;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.sys.modules.models.Sys_employee;
import com.znzz.app.sys.modules.models.Sys_menu;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysMenuService;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/sys/user")
public class SysUserController {
    private static final Log log = Logs.get();
    @Inject
    private SysUserService userService;
    @Inject
    private SysMenuService menuService;
    @Inject
    private SysUnitService unitService;

    @At("")
    @Ok("beetl:/platform/sys/user/index.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public void index() {

    }

    @At
    @Ok("beetl:/platform/sys/user/add.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object add(@Param("unitid") String unitid) {
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "新建用户", msg = "用户名:${args[0].loginname}")
    public Object addDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(user.getPassword(), salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            user.setLoginPjax(true);
            user.setLoginCount(0);
            user.setLoginAt(0);
            
            userService.updateIgnoreNull(user);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/user/edit.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object edit(String id) {
    	Sys_user user = userService.fetchLinks(userService.fetch(id), "unit");
    	//Sys_employee employee = userService.fetchLinks(userService.fetch(id), "employee").getEmployee();
    	//user.setEmployee(employee);
        return user;
    }

    @At
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "修改用户", msg = "用户名:${args[1]}->${args[0].loginname}")
    public Object editDo(@Param("..") Sys_user user, HttpServletRequest req) {
        try {
            user.setOpBy(StringUtil.getUid());
            user.setOpAt((int) (System.currentTimeMillis() / 1000));
            userService.updateIgnoreNull(user);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/resetPwd/?")
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "重置密码", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object resetPwd(String id, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(id);
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("ET922", salt, 1024).toBase64();
            userService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", id));
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success", "ET922");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "删除用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object delete(String userId, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(userId);
            if ("superadmin".equals(user.getLoginname())) {
                return Result.error("system.not.allow");
            }
            userService.deleteById(userId);
            req.setAttribute("loginname", user.getLoginname());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete")
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "批量删除用户", msg = "用户ID:${args[1].getAttribute('ids')}")
    public Object deletes(@Param("ids") String[] userIds, HttpServletRequest req) {
        try {
            Sys_user user = userService.fetch(Cnd.where("loginname", "=", "superadmin"));
            StringBuilder sb = new StringBuilder();
            for (String s : userIds) {
                if (s.equals(user.getId())) {
                    return Result.error("system.not.allow");
                }
                sb.append(s).append(",");
            }
            userService.deleteByIds(userIds);
            req.setAttribute("ids", sb.toString());
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/enable/?")
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "启用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object enable(String userId, HttpServletRequest req) {
        try {
            req.setAttribute("loginname", userService.fetch(userId).getLoginname());
            userService.update(Chain.make("disabled", false), Cnd.where("id", "=", userId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/disable/?")
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    @SLog(tag = "禁用用户", msg = "用户名:${args[1].getAttribute('loginname')}")
    public Object disable(String userId, HttpServletRequest req) {
        try {
            String loginname = userService.fetch(userId).getLoginname();
            if ("superadmin".equals(loginname)) {
                return Result.error("system.not.allow");
            }
            req.setAttribute("loginname", loginname);
            userService.update(Chain.make("disabled", true), Cnd.where("id", "=", userId));
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/user/detail.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object detail(String id) {
        if (!Strings.isBlank(id)) {
            Sys_user user = userService.fetchLinks(userService.fetch(id), "roles");
            //Sys_employee employee = userService.fetchLinks(userService.fetch(id), "employee").getEmployee();
            //user.setEmployee(employee);
            return user;
        }
        return null;
    }

    @At("/menu/?")
    @Ok("beetl:/platform/sys/user/menu.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object menu(String id, HttpServletRequest req) {
        Sys_user user = userService.fetch(id);
        List<Sys_menu> menus = userService.getMenusAndButtons(id);
        List<Sys_menu> datas = userService.getDatas(id);
        List<Sys_menu> firstMenus = new ArrayList<>();
        List<Sys_menu> secondMenus = new ArrayList<>();
        for (Sys_menu menu : menus) {
            for (Sys_menu bt : datas) {
                if (menu.getPath().equals(bt.getPath().substring(0, bt.getPath().length() - 4))) {
                    menu.setHasChildren(true);
                    break;
                }
            }
            if (menu.getPath().length() == 4) {
                firstMenus.add(menu);
            } else {
                secondMenus.add(menu);
            }
        }
        req.setAttribute("userFirstMenus", firstMenus);
        req.setAttribute("userSecondMenus", secondMenus);
        req.setAttribute("jsonSecondMenus", Json.toJson(secondMenus));
        return user;
    }

    @At
    @Ok("json:{locked:'password|salt',ignoreNull:false}") // 忽略password和createAt属性,忽略空属性的json输出
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object data(@Param("unitid") String unitid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid))
            cnd.and("unitid", "=", unitid);
        
        cnd.and("userStatus", "=", "2");
        /*if (!Strings.isBlank(loginname))
            cnd.and("loginname", "like", "%" + loginname + "%");
        if (!Strings.isBlank(username))
            cnd.and("username", "like", "%" + username + "%");*/
        return userService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (Strings.isBlank(pid)) {
            obj.put("id", "root");
            obj.put("text", "所有用户");
            obj.put("children", false);
            tree.add(obj);
        }
        for (Sys_unit unit : list) {
            obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @At("/pass")
    @Ok("beetl:/platform/sys/user/pass.html")
    //@RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    //@RequiresAuthentication
    public void pass() {
    	int i=1;
    	i+=1;
    	System.out.println(i);
    	System.out.println(123);
    }
    
    /*@At
    @Ok("beetl:/platform/sys/user/add.html")
    @RequiresPermissions(value={"sys.manager.user","sys.manager_bak.user"},logical=Logical.OR)
    public Object add(@Param("unitid") String unitid) {
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }*/
    
    @At
    @Ok("beetl:/platform/sys/user/custom.html")
    @RequiresAuthentication
    public void custom() {

    }

    @At
    @Ok("beetl:/platform/sys/user/mode.html")
    @RequiresAuthentication
    public void mode() {

    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object modeDo(@Param("mode") String mode, HttpServletRequest req) {
        try {
            userService.update(Chain.make("loginPjax", "true".equals(mode)), Cnd.where("id", "=", req.getAttribute("uid")));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if ("true".equals(mode)) {
                user.setLoginPjax(true);
            } else {
                user.setLoginPjax(false);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }


    @At
    @Ok("json")
    @RequiresAuthentication
    public Object customDo(@Param("ids") String ids, HttpServletRequest req) {
        try {
            userService.update(Chain.make("customMenu", ids), Cnd.where("id", "=",StringUtil.getUid()));
            Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            if (!Strings.isBlank(ids)) {
                user.setCustomMenu(ids);
                user.setCustomMenus(menuService.query(Cnd.where("id", "in", ids.split(","))));
            } else {
                user.setCustomMenu("");
                user.setCustomMenus(new ArrayList<>());
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At
    @Ok("json")
    @RequiresAuthentication
    public Object doChangePassword(@Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword, HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        String old = new Sha256Hash(oldPassword, user.getSalt(), 1024).toBase64();
        if (old.equals(user.getPassword())) {
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash(newPassword, salt, 1024).toBase64();
            user.setSalt(salt);
            user.setPassword(hashedPasswordBase64);
            userService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("id", "=", user.getId()));
            return Result.success("修改成功");
        } else {
            return Result.error("原密码不正确");
        }
    }
    
    
    @At
    @Ok("json:full")
    public Object getUserIdAndNameList(@Param("unitid") String unitid,@Param("type") Integer type) {
    	String getChildUnitId ="" ;
    	Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;//获取父子级关系
    	if(StringUtils.isBlank(unitid)){
    		Subject subject = SecurityUtils.getSubject();
            Sys_user user = (Sys_user) subject.getPrincipal();
            getChildUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;//获取其最顶级单位id
            
    	}else{
    		getChildUnitId = unitid ;
    	}
    	
    	ArrayList<String> list = new ArrayList<String>() ;
        list.add(getChildUnitId) ;
        Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
       
        unitService.getChildUnitList(unitTreeGroup, getChildUnitId, list);
        //顶级下所有单位ids
        unitid = StringUtils.strip(Json.toJson(list),"[]") ;
    	
    	
    	
    	return userService.getUserIdAndUserName(unitid,type) ;
    }
    
    @At
    @Ok("json:full")
    public Object getEmployeeList(@Param("unitid") String unitid) {
    	/*return userService.getEmployeeList(unitid) ;*/
    	Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;//获取父子级关系
    	List<String> list = new ArrayList<String>() ;
    	if(StringUtils.isBlank(unitid)){
    		list = unitService.getAllUnitid();
    		
    	}else{
    		list.add(unitid);
    		Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
    		unitService.getChildUnitList(unitTreeGroup, unitid, list);
    	}
    	
    	unitid = StringUtils.strip(Json.toJson(list),"[]") ;
    	return userService.getUserIdAndUserName(unitid,null);
    }
    
    @At
    @Ok("json:full")
    public int checkEmployee(@Param("empId") String empId){
    	int count=0;
    	if(!Strings.isBlank(empId)){
    		Cnd cnd = Cnd.NEW(); 
    		cnd.where().and("employeeid","=",empId);
    		Sys_user user = userService.fetch(cnd);
    		if(null!=user){
    			count=1;
    		}
    	}
    	return count;
    }
    
    @At
    @Ok("json:full")
    public int checkLoginname(@Param("loginname") String loginname){
    	int count=0;
    	if(!Strings.isBlank(loginname)){
    		Cnd cnd = Cnd.NEW(); 
    		cnd.where().and("loginname","=",loginname);
    		cnd.where().and("userStatus","=","2");
    		Sys_user user = userService.fetch(cnd);
    		if(null!=user){
    			count=1;
    		}
    	}
    	return count;
    }
    @At
    @Ok("json:full")
    public int checkLogin(@Param("loginname") String loginname,@Param("id")String id){
    	int count=0;
    	if(!Strings.isBlank(loginname)){
    		Cnd cnd = Cnd.NEW(); 
    		cnd.where().and("loginname","=",loginname);
    		cnd.where().and("userStatus","=","2");
    		cnd.where().and("id","!=",id);
    		Sys_user user = userService.fetch(cnd);
    		if(null!=user){
    			count=1;
    		}
    	}
    	return count;
    }
    
    /**
     * 用户使用说明书下载
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @At("/userManual")
    @Ok("raw")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("userManual_name");
    	//filename = new String(filename.getBytes("UTF-8"),"utf-8");
    	// 告诉浏览器要下载文件(浏览器识别的编码是iso-8859-1)
    	response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(),"iso-8859-1"));
    	// 告诉浏览器要下载文件而不是直接打开文件
    	response.setContentType("application/-download");
    	response.setCharacterEncoding("UTF-8");
    	// 获取项目路径
    	String appRoot = Globals.AppRoot;
    	String templatePath = (String) Configer.getInstance().get("template");
    	appRoot = appRoot + templatePath;
    	File file = new File(appRoot+filename);
    	
    	return file;
    }
}
