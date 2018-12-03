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
import com.znzz.app.instrument.modules.service.InsDeviceService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.DeviceForm;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

@IocBean
@At("/instrument/monitor/device")
public class InsDeviceController {
	@Inject
	SysUnitService unitService ;
	@Inject private InsDeviceService insDeviceService ;
	@At("/list")
	@Ok("json:full")
	public Object getDeviceList(@Param("..")DeviceForm form,@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
		
		if(StringUtils.isBlank(form.getBorrowDepart())){
			Subject subject = SecurityUtils.getSubject();
	        Sys_user user = (Sys_user) subject.getPrincipal();
	        Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
	        String topUnitId = unitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;
	        form.setBorrowDepart(topUnitId);
		}else{}
		
		NutMap nutMap = insDeviceService.findDeviceList(form.getDeviceCode(),form.getBorrowDepart(),form.getChargePerson(),form.getState() ,form.getOutField(),form.getPowerState(),form.getLocation(),form.getStart() ,form.getLength(),form.getDraw(),order,columns) ;
		return nutMap ;
		
	}
	
	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/device/index.html")
	@RequiresPermissions("instrument.monitor.device")
	public void index() {

	}
	
}
