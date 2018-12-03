package com.znzz.app.web.modules.controllers.platform.instruments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * 技改项目
 * 
 * @classname InsJgProjectInfoController.java
 * @author chenzhongliang
 * @date 2017年12月19日
 */
@IocBean
@At("/instrument/monitor/jgprojectinfo")
public class InsJgProjectInfoController {

	private static final Log log = Logs.get();

	@Inject
	InsProjectService projectInfoService;

	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/jgprojectinfo/index.html")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public void index() {
	}

	// 搜索
	@At
	@Ok("json:full")
	@RequiresPermissions(value = { "instrument.monitor.jgprojectinfo", "asset.auth.info" }, logical = Logical.OR)
	public Object data(@Param("code") String code, @Param("name") String name, @Param("type") String type,
			@Param("createTime") String createTime, @Param("length") int length, @Param("start") int start,
			@Param("draw") int draw, @Param("::order") List<DataTableOrder> order,
			@Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
		// 对传入日期进行处理
		if (createTime != null && !"".equals(createTime)) {
			// 对传入的时间进行处理
			String[] list = createTime.split("-");
			String startTime = list[0].trim();
			String endTime = list[1].trim();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date startDate = null;
			Date endDate = null;
			// 转换成日期类型进行比较
			try {
				startDate = sdf.parse(startTime);
				endDate = sdf.parse(endTime);
			} catch (ParseException e) {
				log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
				e.printStackTrace();
			}

			cnd = Cnd.where("createTime", "between", new Object[] { startDate, endDate });
		}
		if (!Strings.isBlank(code)) {
			cnd.and("code", "like", "%" + code.trim() + "%");
		}
		if (!Strings.isBlank(name)) {
			cnd.and("name", "like", "%" + name.trim() + "%");
		}
		if (!Strings.isBlank(type)) {
			cnd.and("type", "like", "%" + type.trim() + "%");
		}
		cnd.and("type", "=", "0"); // 项目类型为技改

		NutMap gatewayData = projectInfoService.data(length, start, draw, order, columns, cnd, null);
		return gatewayData;
	}

	@At
	@Ok("beetl:/platform/monitor/jgprojectinfo/add.html")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public void add() {

	}

	@At
	@Ok("json")
	@SLog(tag = "新建项目", msg = "项目名称:${args[0].name}")
	@RequiresPermissions("instrument.monitor.jgprojectinfo.add")
	public Object addDo(@Param("..") Ins_ProjectInfo project_info) {
		try {
			project_info.setCreateTime(new Date());
			project_info.setType("0");
			projectInfoService.insert(project_info);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At("/detail/?")
	@Ok("beetl:/platform/monitor/projectinfo/detail.html")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public Object detail(Integer id) {
		Ins_ProjectInfo project_info = projectInfoService.fetch(id);
		return project_info;
	}

	@At("/edit/?")
	@Ok("beetl:/platform/monitor/jgprojectinfo/edit.html")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public Object edit(Integer id) {
		Ins_ProjectInfo project_info = projectInfoService.fetch(id);
		return project_info;
	}

	@At
	@Ok("json")
	@RequiresPermissions("instrument.monitor.jgprojectinfo.edit")
	@SLog(tag = "编辑项目", msg = "项目名称:${args[0].name}")
	public Object editDo(@Param("..") Ins_ProjectInfo project) {
		try {
			project.setOpBy(StringUtil.getUid());
			project.setOpAt((int) (System.currentTimeMillis() / 1000));
			projectInfoService.updateIgnoreNull(project);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At({ "/delete", "/delete/?" })
	@Ok("json")
	@RequiresPermissions("instrument.monitor.jgprojectinfo.delete")
	public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
		try {
			if (ids != null && ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					boolean flag = projectInfoService.ExistInAsset(Integer.parseInt(ids[i]));
					if (flag) {
						return Result.error("当前选中第"+ (i+1) +"个项目下存在资产关联,无法删除!");
					}
				}

				projectInfoService.delete(ids);
				req.setAttribute("id", StringUtils.toString(ids));
			} else {
				boolean flag = projectInfoService.ExistInAsset(id);
				if (flag) {
					return Result.error("当前选中项目下存在资产关联，无法删除!");
				} else {
					projectInfoService.delete(id);
					req.setAttribute("id", id);
				}
			}
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At("/codeList/?")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public Object collectCodeList(@Param("code") String code) {
		// 获取所有项目编号
		List<String> list = projectInfoService.getCodeList(code, "0");
		if (list != null && list.size() > 0) {
			return Result.error("system.error");
		}
		return Result.success("system.success");
	}

	@At("/nameList/?")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.jgprojectinfo")
	public Object nameList(@Param("code") String name) {
		// 获取所有项目编号
		List<String> list = projectInfoService.getNameList(name, "0");
		if (list != null && list.size() > 0) {
			return Result.error("system.error");
		}
		return Result.success("system.success");
	}

	@At("/checkID")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.projectinfo")
	public Object checkID(@Param("name") String name, @Param("id") String id) {
		List<String> list = projectInfoService.checkID(id, name);
		if (list != null && list.size() > 0) {
			return Result.error("system.error");
		}
		return Result.success("system.success");
	}


	@At("/jgData")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.projectinfo")
	public Object jgData() {
		NutMap nutMap = new NutMap();
		List<Ins_ProjectInfo> list = projectInfoService.getCodeAndName();
		nutMap.addv("data",list);
		return  nutMap;
	}
}
