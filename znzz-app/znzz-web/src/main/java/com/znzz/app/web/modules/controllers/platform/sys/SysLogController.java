package com.znzz.app.web.modules.controllers.platform.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.sys.modules.services.SysLogService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * Created by wizzer on 2016/6/29.
 */
@IocBean
@At("/platform/sys/log")
public class SysLogController {
    private static final Log log = Logs.get();
    @Inject
    private SysLogService sysLogService;

    @At("")
    @Ok("beetl:/platform/sys/log/index.html")
    @RequiresPermissions("sys.manager.log")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.log")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return sysLogService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.log.delete")
    @SLog(tag = "清除日志", msg = "清除日志")
    public Object delete(HttpServletRequest req) {
        try {
            sysLogService.clear();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
}
