package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import com.znzz.app.asset.moudules.models.Ins_Asset_SamplingCheck;
import com.znzz.app.asset.moudules.services.InsSamplingCheckService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.CycleCheckUtil;
import com.znzz.app.web.commons.util.SamplingUtil;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 抽样检定模块Controller
 * @classname InsSamplingCheckController.java
 * @author chenzhongliang
 * @date 2017年12月13日
 */
@IocBean
@At("/asset/sampling")
public class InsSamplingCheckController {
	private static final Log log = Logs.get();
	private static final Logger log2 = Logger.getLogger("scadaLogger");	//scada日志
	
	@Inject
	InsSamplingCheckService insSamplingcheckService;
	@Inject
	InsbcardService bcardService;
	@Inject
	ScadaDeviceServcie  scadaDeviceServcie;
	
	// 首页
	@At("/thisquarter")
	@Ok("beetl:/platform/asset/sampling/index.html")
	@RequiresPermissions("asset.sampling.thisquarter")
	public void index() { 
	}
	@At("/nextquarter")
	@Ok("beetl:/platform/asset/sampling/index.html")
	@RequiresPermissions("asset.sampling.nextquarter")
	public void index2() { 
	}
	
	@At("/samprecord")
	@Ok("beetl:/platform/asset/sampling/index.html")
	@RequiresPermissions("asset.sampling.samprecord")
	public void index3() { 
	}
	
	//搜索
    @At
    @Ok("json:full")
    @RequiresPermissions(value={"asset.sampling.thisquarter","asset.sampling.nextquarter","asset.sampling.samprecord"},logical=Logical.OR)	//只要满足任意一个权限即可
    public Object data(@Param("..")Ins_Asset_SamplingCheck assetCheck,@Param("module")String module,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
    	NutMap data = insSamplingcheckService.getList(assetCheck,module,length,start,draw,order,columns);
    	return data;
    }
    
    @At("/test1/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.thisquarter.sentcheck")
    public Object sentCheck(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "thisquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("operating", "送检");
    	return map;
    }
    @At("/test2/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.thisquarter.check")
    public Object check(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "thisquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("operating", "检定");
    	return map;
    }
    @At("/test3/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.thisquarter.get")
    public Object get(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "thisquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("getDate", check.getGetDate());
    	map.put("remark", check.getRemark());
    	map.put("operating", "领取");
    	return map;
    }
    @At("/test4/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.thisquarter.fil")
    public Object fil(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "thisquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("getDate", check.getGetDate());
    	map.put("remark", check.getRemark());
    	map.put("filDate", check.getFilDate());
    	map.put("operating", "归档");
    	return map;
    }
    
    
    /*****************下季度计划（权限控制）************************/
    @At("/test5/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.nextquarter.sentcheck")
    public Object sentCheck2(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "nextquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("operating", "送检");
    	return map;
    }
    @At("/test6/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.nextquarter.check")
    public Object check2(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "nextquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("operating", "检定");
    	return map;
    }
    @At("/test7/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.nextquarter.get")
    public Object get2(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "nextquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("getDate", check.getGetDate());
    	map.put("remark", check.getRemark());
    	map.put("operating", "领取");
    	return map;
    }
    @At("/test8/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.nextquarter.fil")
    public Object fil2(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "nextquarter");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("getDate", check.getGetDate());
    	map.put("remark", check.getRemark());
    	map.put("filDate", check.getFilDate());
    	map.put("operating", "归档");
    	return map;
    }
    
    
    /*****************检定履历************************/
    @At("/test9/?")
    @Ok("beetl:/platform/asset/sampling/testinfo.html")
    @RequiresPermissions("asset.sampling.samprecord.read")
    public Object read(Integer id){
    	Ins_Asset_SamplingCheck check = insSamplingcheckService.fetch(id);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("module", "samprecord");
    	map.put("id", id);
    	map.put("assetCode", check.getAssetCode());
    	map.put("assetName", check.getAssetName());
    	map.put("samplingDate", check.getSamplingDate());
    	map.put("sentCheckDate", check.getSentCheckDate());
    	map.put("checkDate", check.getCheckDate());
    	map.put("testResult", check.getTestResult());
    	map.put("getDate", check.getGetDate());
    	map.put("remark", check.getRemark());
    	map.put("filDate", check.getFilDate());
    	map.put("operating", "查看");
    	return map;
    }
    
    
    
    @At("/savecheck")
    @Ok("json")
    @RequiresPermissions(value={"asset.sampling.thisquarter","asset.sampling.nextquarter","asset.sampling.samprecord"},logical=Logical.OR)
    @SLog(tag = "抽样检定", msg = "统一编号:${args[0].assetCode}")
    public Object saveDo(@Param("..")Ins_Asset_SamplingCheck check,@Param("operating")String operating){
    	try {
    	Integer id = check.getId();
    	String msg="操作成功";
    	if("送检".equals(operating)){
    		Chain chain = Chain.make("sentCheckDate", check.getSentCheckDate());
    		insSamplingcheckService.update(chain, Cnd.where("id", "=", id));
    	}
    	if("检定".equals(operating)){
    		 msg= checkAndtestScard(check);	//调用scard，修改检定数据，返回操作信息
    	}
    	if("领取".equals(operating)){
    		Chain chain = Chain.make("getDate", check.getGetDate());
    		Chain chain2 = Chain.make("remark", check.getRemark());
    		insSamplingcheckService.update(chain, Cnd.where("id", "=", id));
    		insSamplingcheckService.update(chain2, Cnd.where("id", "=", id));
    	}
    	if("归档".equals(operating)){
    		Chain chain = Chain.make("filDate", check.getFilDate());
    		insSamplingcheckService.update(chain, Cnd.where("id", "=", id));
    	}
    	
    	if(!"操作成功".equalsIgnoreCase(msg)){
    		return Result.error(msg);
    	}
    	return Result.success(msg);
      } catch (Exception e) {
			return Result.error("操作失败");
	}
   }

	private String checkAndtestScard(Ins_Asset_SamplingCheck check) {
		String msg = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> map = bcardService.getOrignCodeAndDeviceCode();
		String deviceid = map.get(check.getAssetCode());
		String result="";
		if(deviceid!=null){
			result = scadaDeviceServcie.getResult(new String[]{deviceid,sdf.format(check.getCheckDate())}, Globals.SETTESTCYCLE);
		}else{
			log.info("统一编号"+check.getAssetCode()+"未绑定对应生产编号");
			log2.info("统一编号"+check.getAssetCode()+"未绑定对应生产编号");
		}
		 //打印错误日志
		 if(null == result){
			 log.info("scada连接失败，失败的统一编号为:"+check.getAssetCode());
			 log2.info("scada连接失败，失败的统一编号为:"+check.getAssetCode());
		 }
		 if("0".equals(result)){
			 log.info("scada调用失败，返回结果为0，失败的统一编号为:"+check.getAssetCode());
			 log2.info("scada调用失败，返回结果为0，失败的统一编号为:"+check.getAssetCode());
		 }
		 msg="操作成功";
		 //更新数据库
		 Chain m1 = Chain.make("checkDate", check.getCheckDate());
		 Chain m2 = Chain.make("testResult", check.getTestResult());
		 insSamplingcheckService.update(m1, Cnd.where("id", "=",  check.getId()));
		 insSamplingcheckService.update(m2, Cnd.where("id", "=",  check.getId()));
		 //若同一编号存在于资产信息表，则更新ins_assets_info中的检定时间字段
		 int count = insSamplingcheckService.count("ins_assets_info", Cnd.NEW().and("assetCode","=",check.getAssetCode()));
		 if(count>0){
			 insSamplingcheckService.update("ins_assets_info", Chain.make("examineDate", check.getCheckDate()), Cnd.NEW().and("assetCode","=",check.getAssetCode()));
		 }
		return msg;
	}
	
	
	 /**
	  * 调用工具类，拿到excel数据，遍历list，根据统一编号取到对应生产编号（若生产编号存在调用scada，反之不调用）
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@At
	 @Ok("json:full")
	 @RequiresPermissions("asset.sampling.nextquarter.import")
	 @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
	 @SuppressWarnings({ "unchecked", "unused" })
	 public Object importfile(@Param("examineFile") File file,HttpServletRequest request,HttpServletResponse response){
		Result result = null;
		try {
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			SamplingUtil util = new SamplingUtil();
			Object object = util.readExcel(path);
			
			if (object instanceof  HashMap) {
				Map<String, String> map = (HashMap<String, String>)object;
				String errorMsg = map.get("error");
				response.setContentType("text/html");
				result = Result.error(errorMsg);
			}else{
				List<Ins_Asset_SamplingCheck> assetList = (List<Ins_Asset_SamplingCheck>) object;
				//迭代集合，调用scada
				for (Ins_Asset_SamplingCheck a : assetList) {
					 testScard(a.getAssetCode(), a.getCheckDate());
					
				}
				// 插入或更新数据库
				insSamplingcheckService.insertList(assetList);
				
				response.setContentType("text/html");
				result = Result.success("导入成功");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("周期检定导入失败...");
			log2.info("周期检定导入失败...");
			response.setContentType("text/html");
		    result = Result.error("出现未知异常，导入失败...");
		}
		
    	return result;
	}
	
	/**
	 * 找到统一编号对应设备id，调用scada
	 * @param assetCode
	 * @param time
	 * @return
	 */
	public void testScard(String assetCode,Date time){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> map = bcardService.getOrignCodeAndDeviceCode();
		String deviceid = map.get(assetCode);
		if(deviceid==null){
			log.info("统一编号"+assetCode+"未绑定对应生产编号");
			log2.info("统一编号"+assetCode+"未绑定对应生产编号");
		}else{
			String result="";
			if(null!=time){
				result = scadaDeviceServcie.getResult(new String[]{deviceid,sdf.format(time)}, Globals.SETTESTCYCLE);
			}else{
				result = scadaDeviceServcie.getResult(new String[]{deviceid,""}, Globals.SETTESTCYCLE);
			}
			
			//打印错误日志
			 if(null == result){
				 log.info("scada连接失败，失败的统一编号为:"+assetCode);
				 log2.info("scada连接失败，失败的统一编号为:"+assetCode);
			 }
			 if("0".equals(result)){
				 log.info("scada调用失败，返回结果为0，失败的统一编号为:"+assetCode);
				 log2.info("scada调用失败，返回结果为0，失败的统一编号为:"+assetCode);
			 }
		}
		
		 
	}
	
	
	/**
	 * 抽样检定模板下载
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@At()
    @Ok("raw")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("samplingcheck_name");
    	filename = new String(filename.getBytes("UTF-8"),"iso-8859-1");
    	// 告诉浏览器要下载文件
    	response.setHeader("Content-Disposition", "attachment;filename="+filename);
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

