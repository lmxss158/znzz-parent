package com.znzz.app.web.modules.controllers.platform.sys;

import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysEmployeeService;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.SysEmployeeUtil;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/sys/employee")
public class SysEmployeeController {
    private static final Log log = Logs.get();
    @Inject
    private SysEmployeeService employeeService;
    @Inject
    private SysUnitService unitService;
    @Inject
    private Dao dao;

    @At("")
    @Ok("beetl:/platform/sys/employee/index.html")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public void index() {

    }

    @At
    @Ok("beetl:/platform/sys/employee/add.html")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object add(@Param("unitid") String unitid) {
        return Strings.isBlank(unitid) ? null : unitService.fetch(unitid);
    }

    @At
    @Ok("json")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    @SLog(tag = "新建职工", msg = "用户名:${args[0].loginname}")
    public Object addDo(@Param("..") Sys_user em, HttpServletRequest req) {
        try {
            employeeService.insert(em);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/employee/edit.html")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object edit(String id) {
        return employeeService.fetchLinks(employeeService.fetch(id), "unit");
    }

    @At
    @Ok("json")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object editDo(@Param("..") Sys_user em) {
        try {
            em.setOpBy(StringUtil.getUid());
            em.setOpAt((int) (System.currentTimeMillis() / 1000));
            employeeService.updateIgnoreNull(em);
            // 然后修改关联的所有单位
            updateOfRelation(em);
        } catch (Exception e) {
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    /**
     * 修改职工单位关联
     *
     * @param em
     */
    private void updateOfRelation(Sys_user em) {
        try {
            int a = dao.update("ins_apply_record", Chain.make("lenderUnit", em.getUnitid()),
                    Cnd.where("proposer", "=", em.getId()));
            int b = dao.update("ins_asset_apply", Chain.make("lenderUnit", em.getUnitid()),
                    Cnd.where("proposer", "=", em.getId()));
            int c = dao.update("ins_assets_info", Chain.make("borrowDepart", em.getUnitid()),
                    Cnd.where("chargePerson", "=", em.getId()));
            int d = dao.update("ins_assets_repair", Chain.make("applyDepart", em.getUnitid()),
                    Cnd.where("applyPreson", "=", em.getId()));
            int e = dao.update("ins_assets_unit", Chain.make("chargeDepart", em.getUnitid()).add("userDepart", em.getUnitid()),
                    Cnd.where("chargeMan", "=", em.getId()));
            int f = dao.update("ins_assets_unit_record", Chain.make("chargeDepart", em.getUnitid()).add("userDepart", em.getUnitid()),
                    Cnd.where("chargeMan", "=", em.getId()));
            int g = dao.update("ins_device_info", Chain.make("borrowDepart", em.getUnitid()),
                    Cnd.where("chargePerson", "=", em.getId()));

            System.out.println(a + b + c + d + e + f + g);
        } catch (Exception e) {
            log.error("Error when update of related unit-person", e);
        }
    }

    @At
    @Ok("json")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = unitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        if (Strings.isBlank(pid)) {
            obj.put("id", "root");
            obj.put("text", "所有职工");
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


    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object delete(String id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    boolean flag = employeeService.existDeviceinfo(ids[i]);
                    if (flag) {
                        return Result.error("当前选中第" + i + "个职工绑定有设备信息，无法离职");
                    }
                }
                //employeeService.delete(ids);
                employeeService.update(Chain.make("userStatus", "4"), Cnd.where("i d", "id", ids));
                req.setAttribute("id", StringUtils.toString(ids));
            } else {
                try {
                    boolean flag = employeeService.existDeviceinfo(id);
                    if (flag) {
                        return Result.error("删除失败！该职工绑定有设备信息，无法离职");
                    }
                    //employeeService.delete(id);
                    employeeService.update(Chain.make("userStatus", "4"), Cnd.where("id", "=", id));
                    req.setAttribute("id", id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/employee/detail.html")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object detail(String id) {

        return employeeService.fetchLinks(employeeService.fetch(id), "unit");
    }

    @At
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    public Object data(@Param("..") Sys_user employee, @Param("unitid") String unitid, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(unitid) && !"root".equals(unitid)) {
            cnd.and("unitid", "=", unitid.trim());
        }
        if (!Strings.isBlank(employee.getUsername())) {
            cnd.and("username", "like", "%" + employee.getUsername().trim() + "%");
        }
        if (!Strings.isBlank(employee.getIdNumber())) {
            cnd.and("idNumber", "like", "%" + employee.getIdNumber().trim() + "%");
        }
        if (!Strings.isBlank(employee.getEntryNumber())) {
            cnd.and("entryNumber", "like", "%" + employee.getEntryNumber().trim() + "%");
        }
        if (!Strings.isBlank(employee.getTelephone())) {
            cnd.and("telephone", "like", employee.getTelephone().trim());
        }
        if (employee.getIs_service() != null) {
            cnd.and("is_service", "=", employee.getIs_service());
        }
        if (employee.getUserStatus() != null) {
            Integer status = employee.getUserStatus();
            if (status == 4) {
                cnd.and("userStatus", "=", "4");
            } else if (status == 5) {
                cnd.and("userStatus", "=", "5");
            } else {
                cnd.and("userStatus", "!=", "4");
                cnd.and("userStatus", "!=", "5");
            }
        }
        cnd.and("userStatus", "!=", "1");
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String name = col.getData();    //获取列名
                if ("orderby".equalsIgnoreCase(name)) {
                    name = "orderby";
                    cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
                } else {
                    name = "CONVERT(" + name + " USING GBK)";        //设置中文编码实现中文排序功能
                    cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
                }

            }
        }


        return employeeService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    public Object findList() {
        List<Sys_user> emlist = new ArrayList<>();
        List<Sys_user> list = employeeService.query("unitid");
        for (Sys_user em : list) {
            Sys_user obj = employeeService.fetchLinks(em, null);
            emlist.add(obj);
        }
        return emlist;
    }

    // 导入功能
    @At
    @Ok("json:full")
    @RequiresPermissions(value = {"sys.manager.employee", "sys.manager_bak.employee"}, logical = Logical.OR)
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload"})
    @SuppressWarnings("unchecked")
    public Object importfile(@Param("employeeFile") File file, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Result result = null;
        try {
            // 获取文件的绝对路径
            String path = file.getAbsolutePath();
            SysEmployeeUtil excelUtil = new SysEmployeeUtil();
            Object object = excelUtil.readExcel(path);

            if (object instanceof HashMap) {
                Map<String, String> map = (HashMap<String, String>) object;
                String errorMsg = map.get("error");
                response.setContentType("text/html");
                return Result.error(errorMsg);
            } else {
                List<Sys_user> employeeList = (List<Sys_user>) object;

                // 插入数据库当中
                String flag = employeeService.insertList(employeeList);
                if ("success".equals(flag)) {
                    // 设置响应头信息
                    response.setContentType("text/html");
                    result = Result.success("导入成功");

                } else {
                    response.setContentType("text/html");
                    return Result.error(flag);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 职工excel模板下载
    @At()
    @Ok("raw")
    public Object downLoad(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 通过配置文件获取文件名称
        String filename = (String) Configer.getInstance().get("employee_name");
        filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
        // 告诉浏览器要下载文件
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        // 告诉浏览器要下载文件而不是直接打开文件
        response.setContentType("application/-download");
        response.setCharacterEncoding("UTF-8");
        // 获取项目路径
        String appRoot = Globals.AppRoot;
        String templatePath = (String) Configer.getInstance().get("bcard_template");
        appRoot = appRoot + templatePath;
        File file = new File(appRoot + filename);

        return file;
    }

    @At
    @Ok("json")
    public int checkIDnumber(@Param("idNumber") String idNumber) {
        Cnd cnd = Cnd.NEW();
        int count = 0;
        if (!Strings.isBlank(idNumber)) {
            cnd.where().and("idNumber", "=", idNumber);
            Sys_user u = employeeService.fetch(cnd);
            if (null != u) {
                count = 1;
            }
            ;
        }
        return count;

    }

    @At
    @Ok("json")
    public int checkUsername(@Param("username") String username, @Param("unitid") String unitid) {
        Cnd cnd = Cnd.NEW();
        int count = 0;
        if (!Strings.isBlank(username) && !Strings.isBlank(unitid)) {
            cnd.where().and("username", "=", username).and("unitid", "=", unitid);
            Sys_user u = employeeService.fetch(cnd);
            if (null != u) {
                count = 1;
            }
            ;
        }
        return count;
    }

    @At
    @Ok("json")
    public int checkIDonUpdate(@Param("idNumber") String idNumber, @Param("id") String id) {
        Cnd cnd = Cnd.NEW();
        int count = 0;
        // 对传入日期进行处理
        if (!Strings.isBlank(idNumber)) {
            cnd.where().and("idNumber", "=", idNumber);
            cnd.where().and("id", "!=", id);
            Sys_user u = employeeService.fetch(cnd);
            if (null != u) {
                count = 1;
            }
            ;
        }
        return count;

    }

    @At
    @Ok("json")
    public int checkUsernameOnUpdate(@Param("id") String id, @Param("username") String username, @Param("unitid") String unitid) {
        Cnd cnd = Cnd.NEW();
        int count = 0;
        if (!Strings.isBlank(username) && !Strings.isBlank(unitid)) {
            cnd.where().and("username", "=", username).and("unitid", "=", unitid).and("id", "!=", id);
            Sys_user u = employeeService.fetch(cnd);
            if (null != u) {
                count = 1;
            }
            ;
        }
        return count;
    }
} 
