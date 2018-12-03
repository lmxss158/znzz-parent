package com.znzz.app.web.modules.controllers.platform.instruments;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.SwitchingFlowForm;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

@IocBean
@At("/instrument/monitor/switchingFlow") 
public class InsSwitchingFlowController {
	@Inject
	SysUnitService unitService ;
	@Inject private InsSwitchingFlowService flowService ;
	
	@At("")
	@Ok("beetl:/platform/monitor/switchFlow/index.html")
	@RequiresPermissions("instrument.monitor.switch")
	public void index(){}
	
	@At("/list")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.switch")
	public NutMap getSwitchFlowList(@Param("..") SwitchingFlowForm form,@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
		
		if(StringUtils.isBlank(form.getBorrowDepart())){
			Subject subject = SecurityUtils.getSubject();
	        Sys_user user = (Sys_user) subject.getPrincipal();
	        Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
	        String topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
	        form.setBorrowDepart(topUnitId);
		}else{}
		
		return flowService.findSwitchFlowList(form,order,columns) ;
	}
	
	@At("/detail")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.switch")
	public NutMap getSwitchFlowDetail(@Param("..") SwitchingFlowForm form){
		return flowService.findSwitchFlowByDeviceCode(form) ;
	}
	
}
