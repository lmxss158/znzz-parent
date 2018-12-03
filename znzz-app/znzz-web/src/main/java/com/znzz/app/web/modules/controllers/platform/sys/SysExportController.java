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

import com.znzz.app.sys.modules.models.Sys_export_column;
import com.znzz.app.sys.modules.models.Sys_export_table;
import com.znzz.app.sys.modules.services.SysExportColumnService;
import com.znzz.app.sys.modules.services.SysExportTableService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;


@IocBean
@At("/platform/sys/export")
public class SysExportController {
    private static final Log log = Logs.get();
    @Inject
    private SysExportTableService sysExportService;
    @Inject
    private SysExportColumnService sysExportService2;

    @At("")
    @Ok("beetl:/platform/sys/export/index.html")
    @RequiresPermissions("sys.manager.export")
    public void index() {

    }
    
    @At
    @Ok("json:full") 
    @RequiresPermissions("sys.manager.export")
    public Object data( @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
    	
        return sysExportService.data(length, start, draw, order, columns, Cnd.NEW(), null);
    }
    
    @At
    @Ok("json:full") 
    @RequiresPermissions("sys.manager.export")
    public Object data2(@Param("id") String id, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
    	Cnd cnd = Cnd.where("table_id", "=", id);
        return sysExportService2.data(length, start, draw, order, columns, cnd, null);
    }
    
	// 跳转到增加页面
	@At
	@Ok("beetl:/platform/sys/export/add.html")
	@RequiresPermissions("sys.manager.export")
	public void add() {

	}
	
	// 跳转到增加页面
	@At("/addColumn/?")
	@Ok("beetl:/platform/sys/export/add2.html")
	@RequiresPermissions("sys.manager.export")
	public void addColumn(String table_id ,HttpServletRequest req) {
		req.setAttribute("table_id", table_id);
	}
    
	@At({"/delete", "/delete/?"})
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export")
	public Object delete(String id ,  HttpServletRequest req) {
		try {
			if (id != null && !"".equals(id)) {
				sysExportService.delete(Integer.parseInt(id));
				//级联删除
				List<Sys_export_column> list = sysExportService2.query(Cnd.NEW().and("table_id", "=", id));
				for (Sys_export_column column : list) {
					sysExportService2.delete(column.getId());
				}
            	return Result.success("删除成功");
            }else {
            	return Result.error("删除失败");
            }
		} catch (Exception e) {
			return Result.error("删除失败");
		}
	}
	@At({"/delete2", "/delete2/?"})
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export")
	public Object delete2(String id ,  HttpServletRequest req) {
		try {
			if (id != null && !"".equals(id)) {
				sysExportService2.delete(Integer.parseInt(id));
            	return Result.success("删除成功");
            }else {
            	return Result.error("删除失败");
            }
		} catch (Exception e) {
			return Result.error("删除失败");
		}
	}
	
	@At("/addDo")
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export.add")
	@SLog(tag="新增报表",msg="报表编号:${args[0].code}")
	public Object addDo(@Param("..")Sys_export_table exportTable, HttpServletRequest request){
		try {
			sysExportService.insert(exportTable);
			return Result.success("新增成功");
		} catch (Exception e) {
			log.error("新增失败");
			e.printStackTrace();
			return Result.error("新增失败");
		}
		
	}
	@At("/addDo2")
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export.add")
	@SLog(tag="新增column",msg="报表编号:${args[0].map_key}")
	public Object addDo2(@Param("..")Sys_export_column exportColumn, HttpServletRequest request){
		try {
			sysExportService2.insert(exportColumn);
			return Result.success("新增成功");
		} catch (Exception e) {
			log.error("新增失败");
			e.printStackTrace();
			return Result.error("新增失败");
		}
		
	}
	
	
	@At("/edit/?")
    @Ok("beetl:/platform/sys/export/edit.html")
    @RequiresPermissions("sys.manager.export.edit")
    public Object edit(Integer id) {
		Sys_export_table table = sysExportService.fetch(id);
		return table;
	}
	@At("/edit2/?")
    @Ok("beetl:/platform/sys/export/edit2.html")
    @RequiresPermissions("sys.manager.export.edit")
    public Object edit2(Integer id) {
		Sys_export_column table = sysExportService2.fetch(id);
		return table;
	}
	
	@At("/editDo")
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export.edit")
	@SLog(tag="修改报表",msg="报表编号:${args[0].code}")
	public Object editDo(@Param("..")Sys_export_table exportTable, HttpServletRequest request){
		try {
			sysExportService.update(exportTable);
			return Result.success("修改成功");
		} catch (Exception e) {
			log.error("修改失败");
			e.printStackTrace();
			return Result.error("修改失败");
		}
		
	}
	@At("/editDo2")
	@Ok("json:full")
	@RequiresPermissions("sys.manager.export.edit")
	@SLog(tag="修改column",msg="报表编号:${args[0].map_key}")
	public Object editDo2(@Param("..")Sys_export_column exportColumn, HttpServletRequest request){
		try {
			sysExportService2.update(exportColumn);
			return Result.success("修改成功");
		} catch (Exception e) {
			log.error("修改失败");
			e.printStackTrace();
			return Result.error("修改失败");
		}
		
	}

}
