package com.znzz.app.web.modules.controllers.platform.instruments;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import com.znzz.app.instrument.modules.models.Ins_Gateway;
import com.znzz.app.instrument.modules.service.InsGatewayService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.ReadExcel_Acard;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * A卡控制器
 * 收集器管理
 */
@IocBean
@At("/instrument/monitor/gateway")
public class InsGatewayController {

	private static final Log log = Logs.get();
	private static final org.apache.log4j.Logger SCADA_LOG = org.apache.log4j.Logger.getLogger("scadaLogger");

	@Inject
	InsGatewayService gatewayService;
	@Inject
	ScadaDeviceServcie scadaDeviceServcie;
	@Inject
	InsbcardService insbcardService;
	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/gateway/index.html")
	@RequiresPermissions("instrument.monitor.gateway")
	public void index() {

	}

	@At
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object data(@Param("gatewayCode") String gatewayCode, @Param("gatewayName") String gatewayName,
			@Param("gatewayLocation") String gatewayLocation, @Param("createTime") String createTime,
			@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
			@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
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
		if (!Strings.isBlank(gatewayCode)) {
			cnd.and("gatewayCode", "like", "%" + gatewayCode + "%");
		}
		if (!Strings.isBlank(gatewayName)) {
			cnd.and("gatewayName", "like", "%" + gatewayName + "%");
		}
		if (!Strings.isBlank(gatewayLocation)) {
			cnd.and("gatewayLocation", "like", "%" + gatewayLocation + "%");
		}

		NutMap gatewayData = gatewayService.data(length, start, draw, order, columns, cnd, null);
		return gatewayData;
	}

	@At
	@Ok("beetl:/platform/monitor/gateway/add.html")
	@RequiresPermissions("instrument.monitor.gateway")
	public void add() {

	}

	@At
	@Ok("json")
	@SLog(tag = "新建网关", msg = "网关名称:${args[0].gatewayName}")
	@RequiresPermissions("instrument.monitor.gateway.add")
	public Object addDo(@Param("..") Ins_Gateway ins_Gateway, HttpServletRequest request) {
		try {
			String[] param = { ins_Gateway.getIp() };
			String result = scadaDeviceServcie.getResult(param, Globals.SETACARD);
			/*
			 * if(result.equals("1")){//scada接口返回值为1才插入数据库
			 * gatewayService.insert(ins_Gateway); }
			 */
			// scada接口返回值为1才插入数据库
			if ("1".equals(result)) {
				gatewayService.insert(ins_Gateway);
				SCADA_LOG.info("创建收集器");
				return Result.success("操作成功");
			} else if (result == null) {
				return Result.error("SCADA数据为空~");
			} else {
				SCADA_LOG.info("创建收集器失败");
				return Result.error("设置收集器失败,ip地址可能已经存在,请尝试其他ip地址");
			}
		} catch (Exception e) {
			SCADA_LOG.info("创建收集器失败");
			return Result.error("调用SCADA失败...设置收集器失败~");
		}
	}

	// 详情
	@At("/detail/?")
	@Ok("beetl:/platform/monitor/gateway/detail.html")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object detail(Integer id) {
		Ins_Gateway ins_Gateway = gatewayService.fetch(id);
		return ins_Gateway;

	}

	// 检测
	@At("/check")
	@Ok("json")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object check(Integer id) {
		Ins_Gateway ins_Gateway = gatewayService.fetch(id);
		// String gatewayCode = ins_Gateway.getGatewayCode();
		String ip = ins_Gateway.getIp();
		try {
			// =====sacada接口操作=========
			String[] param = { ins_Gateway.getIp() };
			String result = scadaDeviceServcie.getResult(param, Globals.TESTACARD);
			if ("1".equals(result)) {
				SCADA_LOG.info("检测收集器成功");
				return Result.success("ip为:" + ip + ",检测成功");
			} else if ("0".equals(result)) {
				SCADA_LOG.info("检测收集器失败");
				return Result.error("ip为:" + ip + ",检测失败");
			} else {
				return Result.error("检测ip为:" + ip + ",SCADA数据为空");
			}
		} catch (Exception e) {
			SCADA_LOG.info("调用SCADA接口失败,检测收集器失败");
			return Result.error("ip为:" + ip + ",检测失败");
		}

	}

	// 重启
	@At("/restart")
	@Ok("json")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object restart(Integer id) {
		Ins_Gateway ins_Gateway = gatewayService.fetch(id);
		String ip = ins_Gateway.getIp();
		try {
			// =====sacada接口操作=========
			String[] param = { ins_Gateway.getIp() };
			String result = scadaDeviceServcie.getResult(param, Globals.RESTARTACARD);
			if ("1".equals(result)) {
				SCADA_LOG.info("重启收集器成功,ip地址为: " + ip);
				return Result.success("ip为:" + ip + ",重启成功");
			} else if ("0".equals(result)) {
				SCADA_LOG.info("重启收集器失败,ip地址为: " + ip);
				return Result.error("ip为:" + ip + ",重启失败");
			} else {
				return Result.error("检测ip为:" + ip + ",SCADA数据为空");
			}
		} catch (Exception e) {
			SCADA_LOG.info("调用SCADA接口失败,重启收集器失败");
			return Result.error("ip为:" + ip + ",重启失败");
		}

	}

	@At("/edit/?")
	@Ok("beetl:/platform/monitor/gateway/edit.html")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object edit(Integer id) {
		Ins_Gateway ins_Gateway = gatewayService.fetch(id);
		return ins_Gateway;
	}

	@At
	@Ok("json")
	@RequiresPermissions("instrument.monitor.gateway.edit")
	@SLog(tag = "编辑网关", msg = "网关名称:${args[0].gatewayName}")
	public Object editDo(@Param("..") Ins_Gateway gateway, HttpServletRequest req) {
		try {

			// 参数： 旧ip ,新IP,方法名
			// String oldIP = gatewayService.fetch(gateway.getId()).getIp();
			List<Ins_Gateway> queryList = gatewayService.query(Cnd.where("gatewayCode", "=", gateway.getGatewayCode()));
			String oldIP = null;
			Integer id = null;

			if (queryList.size() == 1 && queryList != null) {
				// 获取原始IP
				oldIP = queryList.get(0).getIp();
				// 设置ID
				id = queryList.get(0).getId();
			}
			String result = "1";
			if (!gateway.getIp().equals(oldIP)) {
				String[] param = { oldIP, gateway.getIp() };
				SCADA_LOG.info("更新收集器成功,ip地址为: " + gateway.getIp());
				try {
					result = scadaDeviceServcie.getResult(param, Globals.UPDATEACARD);
				} catch (Exception e) {
					e.printStackTrace();
					SCADA_LOG.info("调用SCADA失败,更新收集器失败");
					return Result.error("调用SCADA失败~");
				}
			}
			if ("1".equals(result)) {// scada接口返回值为1后才修改数据库
				gateway.setId(id);
				gateway.setOpBy(StringUtil.getUid());
				gateway.setOpAt((int) (System.currentTimeMillis() / 1000));
				// =====sacada接口操作=========
				// 更新之前获取旧ip
				gatewayService.updateIgnoreNull(gateway);
				SCADA_LOG.info("更新收集器成功~");
				return Result.success("更新收集器成功~");
			} else if ("0".equals(result)) {
				SCADA_LOG.info("调用SCADA成功,更新收集器失败");
				return Result.error("更新收集器失败~");
			} else {
				return Result.error("SCADA数据为空~");
			}
		} catch (Exception e) {
			return Result.error("调用SCADA失败~");
		}

	}

	@At({ "/delete", "/delete/?" })
	@Ok("json")
	@RequiresPermissions("instrument.monitor.gateway.delete")
	public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
		Result re = null;
		String ips = "";
		if (ids != null && ids.length > 0) {
			for (String iD : ids) {
				String ip = gatewayService.fetch(Integer.valueOf(iD)).getIp();
				ips += ip + ",";
			}
			org.apache.commons.lang3.StringUtils.strip(ips, ",");
			String[] param = { ips };
			//String result = scadaDeviceServcie.getResult(param, Globals.DELETEACARDBATCH);
			String result	=scadaDeviceServcie.invoke(param, Globals.DELETEACARDBATCH);
			/** 解析返回json字符串 **/
			JSONObject object = new JSONObject(result);
			JSONArray jsonArray = object.getJSONArray("ipList");
			String errorCode = "";
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				if (0 == obj.getInt("result")) {
					errorCode += obj.getString("ip") + ",";
				}
				if (1 == obj.getInt("result")) {
					
					String ip = obj.getString("ip") ;
					gatewayService.deleteByIp(ip);
				}
			}
			errorCode = org.apache.commons.lang3.StringUtils.strip(errorCode, ",");
			if (org.apache.commons.lang3.StringUtils.isNotBlank(errorCode)) {
				SCADA_LOG.info("收集器为:" + errorCode + "删除失败");
				re = Result.error("收集器为:" + errorCode + "删除失败");
			} else{
				re = Result.success("调用SCADA成功,删除收集器成功~");
				 SCADA_LOG.info("调用SCADA成功,删除收集器成功~");
			}
		}else {
			 //=====sacada接口操作=========
            String ip = gatewayService.fetch(id).getIp();
            String[] param = {ip};
            String result = scadaDeviceServcie.getResult(param, Globals.DELETEACARD);
   			if("1".equals(result)){//返回值为0，表示失败
   			   	gatewayService.delete(id);
                req.setAttribute("id", id);
                SCADA_LOG.info("调用SCADA成功,删除收集器成功~");
                return Result.success("删除收集器成功~");
   			}else if("0".equals(result)){//返回值为1表示成功
   				SCADA_LOG.error("调用SCADA成功,删除收集器失败~");
   				return Result.error("删除收集器失败~");
   			}else{
   				return Result.error("SCADA数据为空~");
   			}
		}
		return re;
	}

	@At()
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object search(String gatewayCode, String gatewayName, String gatewayLocation, String createTime,
			@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
			@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		String[] list = createTime.split("-");
		for (String string : list) {
			System.out.println(string.trim());
		}
		Cnd cnd = Cnd.NEW();
		String linkName = null;
		NutMap gatewayData = gatewayService.data(length, start, draw, order, columns, cnd, linkName);

		Cnd subCnd = null;
		NutMap nutMap = gatewayService.data(length, start, draw, order, columns, cnd, linkName, subCnd);
		return gatewayData;
	}

	@At("/checkGatewayCode/?")
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.gateway")
	public Object checkGatewayCode(@Param("gatewayCode") String gatewayCode) {
		Cnd cnd = Cnd.NEW();
		boolean flag = gatewayService.checkGatewayCode(gatewayCode, cnd);
		if (flag) {
			return Result.success();
		}
		return Result.error("网关编号重复");
	}

	@At("/checkIP/**")
	@Ok("json:full")
	public Object checkIP(@Param("ipValue") String ipValue) {
		List<Ins_Gateway> list = gatewayService.query(Cnd.where("ip", "=", ipValue));
		if (list.size() > 0) {
			return Result.error("IP重复");
		}
		return Result.success();
	}

    // 模板下载
    @At()
    @Ok("raw")
    @RequiresPermissions("instrument.monitor.gateway")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("acard_name");
    	filename = new String(filename.getBytes("UTF-8"),"iso-8859-1");
    	// 告诉浏览器要下载文件
    	response.setHeader("Content-Disposition", "attachment;filename="+filename);
    	// 告诉浏览器要下载文件而不是直接打开文件
    	response.setContentType("application/-download");
    	response.setCharacterEncoding("UTF-8");
    	// 获取项目路径
    	String appRoot = Globals.AppRoot;
    	String templatePath = (String) Configer.getInstance().get("acard_template");
    	appRoot = appRoot + templatePath;
    	File file = new File(appRoot+filename);
    	
    	/*String contextPath = request.getServletContext().getRealPath("/")+"WEB-INF/download/";
    		File file = new File(appRoot+filename);
    	*/
    	return file;
    }
    
   // 导入功能
    @At
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.gateway")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
    @SuppressWarnings("unchecked")
    public Object upload( @Param("acardFile") File file,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
//    	File f = tf.getFile();                        	// 这个是保存的临时文件
//        FieldMeta meta = tf.getMeta();                // 这个原本的文件信息
//        String oldName = meta.getFileLocalName();     // 这个时原本的文件名称
    	Result result = null;
    	java.util.Date time=null;
		try {
			 
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time= sdf.parse(sdf.format(new Date()));
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			ReadExcel_Acard excelUtil = new ReadExcel_Acard();
			// 将excel表格中的数据存放到list当中
			// List<CollectBindDeviceBean> collect_devicetList = excelUtil.readExcel(path);
			Object object = excelUtil.readExcel(path);
			if (object instanceof  HashMap) {
				Map<String, String> map = (HashMap<String, String>)object;
				String errorMsg = map.get("error");
				response.setContentType("text/html");
				return Result.error(errorMsg);
			}else{
				List<Ins_Gateway> collect_devicetList = (List<Ins_Gateway>) object;
			     for(Ins_Gateway card : collect_devicetList){
			    	 card.setCreateTime(time);
			     }
				// 插入数据库当中
			     String re = gatewayService.insertList(collect_devicetList);
				// 设置响应头信息
				response.setContentType("text/html");
				
				if(!"".equals(re)&&null!=re){
					result = Result.error(re) ;
					SCADA_LOG.info(re);
				}else{
					SCADA_LOG.info("导入成功");
					result = Result.success("导入成功");	  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return result;
    }
	

}
