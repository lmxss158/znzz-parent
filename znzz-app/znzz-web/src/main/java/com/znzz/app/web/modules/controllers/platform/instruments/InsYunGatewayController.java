package com.znzz.app.web.modules.controllers.platform.instruments;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_YunGateway;
import com.znzz.app.instrument.modules.service.InsYunGatewayService;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.YunDeviceBean;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
/**
 * 云网管理
 * @classname InsYunGatewayController.java
 * @author chenzhongliang
 * @date 2017年12月19日
 */
@IocBean
@At("/yun/configure/yungateway")
public class InsYunGatewayController {
	
	private static final Log log = Logs.get();
	
	@Inject
	InsYunGatewayService yunGatewayService;
	@Inject
	SysUnitService unitService;
    @Inject
    ImportService importService;
	
	
	//首页
	@At("")
	@Ok("beetl:/platform/monitor/yungateway/index.html")	//跳转页面
	@RequiresPermissions("yun.configure.yungateway")	//权限标识
	public void index() {

	}
	
	//条件搜索
	@At
    @Ok("json:full")
    @RequiresPermissions("yun.configure.yungateway")
    public Object data(@Param("device_code")String deviceCode,@Param("borrowDepart")String borrowDepart,@Param("device_id")String deviceId, @Param("gateway_name")String gatewayName,@Param("accesskey")String accesskey,@Param("gateway_id")String gatewayId,@Param("createTime")String createTime,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Sql sql = Sqls.create("select a.id,a.device_code,b.deviceName,b.deviceVersion,c.name,a.device_id,a.gateway_id,a.gateway_name,a.accesskey,a.createTime "+
    			              "from ins_yun_gateway a LEFT JOIN ins_device_info b on a.device_code = b.deviceCode "+
    						  "LEFT JOIN sys_unit c on b.borrowDepart = c.id $condition ");
		Cnd cnd = Cnd.NEW();
    	// 对传入日期进行处理
        if (createTime != null && !"".equals(createTime)) {
        	// 对传入的时间进行处理
        	String[] list = createTime.split("-");
        	String startTime = list[0].trim();
        	String endTime = list[1].trim();
        	SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	Date startDate = null;
        	Date endDate = null;
        	// 转换成日期类型进行比较
        	try {
        		startDate = sdf.parse(startTime);
    			endDate = sdf.parse(endTime);
    		} catch (ParseException e) {
    			log.error("startTime和endTime:"+startTime+","+endTime+",时间转换异常");
    			e.printStackTrace();
    		}
        	
			cnd = Cnd.where("a.createTime", "between", new Object[]{startDate,endDate});
		}
        if (!Strings.isBlank(deviceCode)) {
			//cnd.and("device_code", "like", "%" + deviceCode + "%");
			cnd.or("b.deviceCode", "like", "%" + deviceCode.trim() + "%");
        	cnd.or("b.deviceName", "like", "%" + deviceCode.trim() + "%");
        	cnd.or("b.deviceVersion", "like", "%" + deviceCode.trim() + "%");
		}
        if (!Strings.isBlank(deviceId)) {
        	cnd.and("a.device_id", "like", "%" + deviceId.trim() + "%");
        }
        if (!Strings.isBlank(gatewayName)) {
        	cnd.and("a.gateway_name", "like", "%" + gatewayName.trim() + "%");
        }
        if (!Strings.isBlank(gatewayId)) {
        	cnd.and("a.gateway_id", "like", "%" + gatewayId.trim() + "%");
        }
        if (!Strings.isBlank(accesskey)) {
        	cnd.and("a.accesskey", "like", "%" + accesskey.trim() + "%");
        }
        if(!Strings.isBlank(borrowDepart)){
        	String list = unitService.getChildList(borrowDepart);
        	cnd.and("b.borrowDepart","in",list);
        }
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), or.getDir());
            }
        }
        
        sql.setCondition(cnd);
    	NutMap gatewayData = yunGatewayService.data(length, start, draw, sql, sql);
        return gatewayData;
    }
    
	//新增
	@At
    @Ok("beetl:/platform/monitor/yungateway/add.html")
    @RequiresPermissions("yun.configure.yungateway")
    public void add() {
    }
    
	//新增数据写入
    @At
    @Ok("json")
    @SLog(tag="新建网关",msg = "网关名称:${args[0].gateway_name}")
    @RequiresPermissions("yun.configure.yungateway.add")
    public Object addDo(@Param("..")Ins_YunGateway ins_yunGateway,HttpServletRequest request) {
    	try {
    		ins_yunGateway.setCreateTime(new Date());
			yunGatewayService.insert(ins_yunGateway);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }
    
    //查看
    @At("/detail/?")
    @Ok("beetl:/platform/monitor/yungateway/detail.html")
    @RequiresPermissions("yun.configure.yungateway")
    public Object detail(Integer id) {
    	Ins_YunGateway ins_yunGateway = yunGatewayService.fetch(id);
        return ins_yunGateway;
    }
    
    
    //修改
    @At("/edit/?")
    @Ok("beetl:/platform/monitor/yungateway/edit.html")
    @RequiresPermissions("yun.configure.yungateway")
    public Object edit(Integer id) {
        Ins_YunGateway ins_yunGateway = yunGatewayService.fetch(id);
        return ins_yunGateway;
    }
    
    
    //修改数据写入
    @At
    @Ok("json")
    @RequiresPermissions("yun.configure.yungateway.edit")
    @SLog(tag = "编辑网关", msg = "网关名称:${args[0].gateway_name}")
    public Object editDo(@Param("..") Ins_YunGateway yunGateway, HttpServletRequest req) {
        try {
        	yunGateway.setOpBy(StringUtil.getUid());
        	yunGateway.setOpAt((int) (System.currentTimeMillis() / 1000));
            yunGatewayService.updateIgnoreNull(yunGateway);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    //删除
    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions("yun.configure.yungateway.delete")
    public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
            	yunGatewayService.delete(ids);
                req.setAttribute("id", StringUtils.toString(ids));
            } else {
            	yunGatewayService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
    
    //校验统一编号
    @At("/cherkCode")
    @Ok("json:full")
    @RequiresPermissions("yun.configure.yungateway")
    public Object cherkCode(@Param("code")String code) {
    	// 根据编号从云网表里获取集合
    	List<String> yunList = yunGatewayService.getCodeList(code);
    	if (yunList != null && yunList.size() > 0) {
			return 1;		//该编号在云网表里已存在
		}
    	//根据编号从设备表里获取
    	List<String> deviceList = yunGatewayService.getDeviceCodeList(code);
    	if(deviceList.isEmpty() && deviceList.size()==0){
    		return 2; 		//在设备表里没有该编号
    	}
    	Ins_DeviceInfo idi = yunGatewayService.findDevicebyCode(code);
    	return idi;
    }
    
 
    @At("/cherkID")
    @Ok("json:full")
    @RequiresPermissions("yun.configure.yungateway")
    public Object cherkID(@Param("code")String code,@Param("id")String id) {
    	// 根据编号从云网表里获取集合
    	List<String> yunList = yunGatewayService.checkID(code,id);
    	if (yunList != null && yunList.size() > 0) {
			return 1;		//该编号在云网表里已存在
		}
    	//根据编号从设备表里获取
    	List<String> deviceList = yunGatewayService.getDeviceCodeList(code);
    	if(deviceList.isEmpty() && deviceList.size()==0){
    		return 2; 		//在设备表里没有该编号
    	}
    	Ins_DeviceInfo idi = yunGatewayService.findDevicebyCode(code);
    	return idi;
    }
    
    @At("/assetCodeListSelect")
    @Ok("json:full")
    @RequiresPermissions("yun.configure.yungateway")
    public Object assetCodeListSelect(){
    	NutMap nt = yunGatewayService.assetCodeListSelect();
    	return nt;
    }
    @At("/assetCodeListSelect2")
    @Ok("json:full")
    @RequiresPermissions("yun.configure.yungateway")
    public Object assetCodeListSelect2(String id){
    	NutMap nt = yunGatewayService.assetCodeListSelect2(id);
    	return nt;
    }
    
    
    @At
    @Ok("json")
    @AdaptBy(type = UploadAdaptor.class)
    public Object importJson(@Param("jsonFile")File file,HttpServletRequest request,HttpServletResponse response) throws Exception{
//    	Result result=null;
//    	if(!file.exists()){
//    		response.setContentType("text/html");
//    		result = Result.error("导入失败");
//    	}
//    	try {
//    		
//    		YunDeviceBean yun = Json.fromJsonFile(YunDeviceBean.class, file);
//    		
//    		String flag = yunGatewayService.insertOrUpdateFromJson(yun);
//    		if(!"0".equals(flag)){
//    			response.setContentType("text/html");
//        		result = Result.error(flag);
//    		}else{
//    			response.setContentType("text/html");
//    			result = Result.success("导入成功");
//    			
//    		}
//        	
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setContentType("text/html");
//    		result = Result.error("发生未知异常，导入失败");
//		}
        Result result = Result.error("导入失败");
        List<ExcelImportResult<Ins_YunGateway>> importFile = importService.importFile(request, file, "Ins_YunGateway_import");
        List<Ins_YunGateway> failList = importFile.get(0).getFailList();
        if (failList.size() == 0){
            //获取数据集
            List<Ins_YunGateway> importFileList = importFile.get(0).getList();
            //excel非空校验
            if (importFileList.size() == 0){
                return Result.error("模板中数据为空,请填写数据后再进行导入!");
            }

            //校验excel中数据重复问题
            if(importFileList.size() >0){
                for (int i = 0; i < importFileList.size(); i++) {
                	Ins_YunGateway yunGateway = importFileList.get(i);
                	if(yunGateway != null){
                		yunGateway.setCreateTime(new Date());
                		yunGatewayService.insertOrUpdate(yunGateway);
                	}
                }
            }
        }
        
        if(!failList.isEmpty() || failList.size() >0 ){
            return result;
        }

        response.setContentType("text/html");
        result = Result.success("导入成功");
    	
    	return result;
    }
    
}
