package com.znzz.app.web.modules.controllers.platform.instruments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.instrument.modules.models.Ins_DeviceFacility;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 使用情况下的设备状态
 * @author wangqiang
 *
 */
@IocBean
@At("/instrument/monitor/devicefacility")
public class InsDeviceFacilityController {
	private static final Log log = Logs.get();
	@Inject
	private InsDeviceFacilityService insDeviceFacilityService ;
	@Inject
	private InsProjectService projectService ;
	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/devicefacility/index.html")
	@RequiresPermissions("instrument.monitor.devicefacility")
	public void index() {

	}
	/**
	 * 设备使用情况list
	 * @param deviceCode
	 * @param start
	 * @param length
	 * @param draw
	 * @return
	 */
	@At("/list")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.devicefacility")
	public NutMap getDeviceList(@Param("deviceCode")String deviceCode,@Param("selectDate") String selectDate ,@Param("start")int start,@Param("length")int length,@Param("draw")int draw,@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
		return insDeviceFacilityService.findDeviceFacilityList(deviceCode,selectDate ,length, start, draw,order,columns);
	}
	/**
	 * 弹出选择项目页面
	 */
	@At("/addHtml")
	@Ok("beetl:/platform/monitor/devicefacility/add.html")
	@RequiresPermissions("instrument.monitor.devicefacility")
	public void addHtml(){}
	
	/**
	 * 添加分配子用时
	 * @param liststr
	 * @return
	 */
	@At("/add")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.devicefacility")
	public Object add(@Param("list")String liststr){
		boolean flag =true ;
		try {
			List<Ins_DeviceFacility> list = Json.fromJsonAsList(Ins_DeviceFacility.class, liststr) ;
			Integer count =0 ;
			//校验参数
			for(int i=0;i<list.size();i++){
				Ins_DeviceFacility facility = list.get(i) ;
				if(!validateParam(facility)){
					return Result.error("请校验参数是否正确.") ;
				}
				
				String code = facility.getProjectCode() ;
				int num = projectService.isExist(code) ;
				if(num==0){//项目不存在
					return Result.error("项目编号为 "+code+" 的项目已不存在,请刷新页面") ;
				}
				count+= list.get(i).getDuration() ;
			}
			
			Map<String, Integer> map = insDeviceFacilityService.findDurtaionById(list.get(0).getPid()) ;
			if((map.get("assignNum")+count)>map.get("allNum")){
				return Result.error("分配的时长已超出未分配的时长.") ;
			}
			
			if(flag){
				insDeviceFacilityService.insertDeviceFacility(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag =false ;
		}
		
		return flag==true?Result.success("添加成功."):Result.error("添加失败.");
	}
	
	/**
	 * 校验参数是否有空
	 * @throws ParseException 
	 */
	
	public boolean validateParam(Ins_DeviceFacility facility) throws ParseException{
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy/MM/dd" );
		if(StringUtils.isBlank(facility.getDeviceCode())||StringUtils.isBlank(facility.getExt2())||StringUtils.isBlank(facility.getProjectCode())||facility.getDuration()==null||facility.getPid()==null){
			return false ;
		}
		String[] time = facility.getExt2().split("-") ;
		facility.setChTimeStart(sdf.parse(time[0]));
		facility.setChTimeEnd(sdf.parse(time[1]));
		facility.setExt2(null);
		facility.setOperateTime(new Date());
		return true ;
	}
	
	/**
	 * 根据pid获取子列表
	 * @param pid
	 * @param start
	 * @param length
	 * @param draw
	 * @return
	 */
	@At("/findChildList")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.devicefacility")
	public NutMap getChildList(@Param("pid")Integer pid,@Param("start")int start,@Param("length")int length,@Param("draw")int draw,@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
		return	insDeviceFacilityService.findChildDeviceFacilityList(pid,length,start,draw,order,columns);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@At("/del")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.devicefacility")
	public Object delFacilityById(@Param("id")Integer id){
		
		boolean flag = false ;
		
		try {
			if(id!=null){
				insDeviceFacilityService.delFacilityById(id);
				flag =true ;
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
        return flag ==true ? Result.success("删除成功."):Result.error("删除失败.") ;		
	}
	
	
}
