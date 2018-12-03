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
import org.apache.shiro.util.StringUtils;
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
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.instrument.modules.service.InsCollectService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.ReadExcel_Bcard;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

@IocBean
@At("/instrument/monitor/bCard")
public class InsBcardController {

	private static final Log log = Logs.get();

	@Inject
	InsbcardService bcardService;
	@Inject
	private InsCollectService collectService;
	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/bcard/index.html")
	@RequiresPermissions("instrument.monitor.bCard")
	public void index() {

	}
	//查询
    @At
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object data(@Param("bcardCode")String bcardCode,@Param("orignCode")String orignCode,@Param("operateTime")String operateTime,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
    	Cnd cnd = Cnd.NEW();
    	// 对传入日期进行处理
        if (operateTime != null && !"".equals(operateTime)) {
        	// 对传入的时间进行处理
        	String[] list = operateTime.split("-");
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
        	
			cnd = Cnd.where("operateTime", "between", new Object[]{startDate,endDate});
		}
        if (!Strings.isBlank(bcardCode)) {
			cnd.and("bcardCode", "like", "%" + bcardCode + "%");
		}
        if (!Strings.isBlank(orignCode)) {
        	cnd.and("orignCode", "like", "%" + orignCode + "%");
        }
     
        
    	NutMap bcardData = bcardService.data(length, start, draw, order, columns, cnd, null);
        return bcardData;
    }
    //跳转增加页面
    @At
    @Ok("beetl:/platform/monitor/bcard/add.html")
    @RequiresPermissions("instrument.monitor.bCard")
    public void add() {
    	
    }
    //增加B卡
    @At
    @Ok("json")
    @SLog(tag="新建B卡",msg = "网关名称:${args[0].orignCode}")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object addDo(@Param("..")Ins_DeviceBcard ins_bcard,HttpServletRequest request) {
    	try {
			bcardService.insert(ins_bcard);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }
    
    @At("/detail/?")
   // @Ok("beetl:/platform/monitor/bcard/detail.html")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object detail(Integer id) {
    	Ins_DeviceBcard ins_bcard = bcardService.fetch(id);
        return ins_bcard;
    }
    //跳转编辑B卡
    @At("/edit/?")
    @Ok("beetl:/platform/monitor/bcard/edit.html")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object edit(Integer id) {
    	
        Ins_DeviceBcard ins_bcard = bcardService.fetch(id);
        
        return ins_bcard;
    }
    
    //编辑B卡
    @At
    @Ok("json")
    @RequiresPermissions("instrument.monitor.bCard.modify")
    @SLog(tag = "编辑B卡", msg = "网关名称:${args[0].orignCode}")
    public Object editDo(@Param("..") Ins_DeviceBcard bcard, HttpServletRequest req) {
        try {
        	
        	bcard.setOpBy(StringUtil.getUid());
        	bcard.setOpAt((int) (System.currentTimeMillis() / 1000));
            bcardService.updateIgnoreNull(bcard);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    /*@SLog(tag = "删除网关", msg = "网关名称:${args[1].orignCode}")
    //@At("/delete/?")
    @Ok("json")
    @RequiresPermissions("instrument.monitor.bcard.delete")
    public Object delete1(Integer id, HttpServletRequest req) {
        try {
            bcardService.delete(id);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
    */
    //删除
    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions("instrument.monitor.bCard.del")
    public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
            	bcardService.delete(ids);
                req.setAttribute("id", StringUtils.toString(ids));
            } else {
            	bcardService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
    //校验是否绑定
    @At({"/checkBangding","/checkBangding/?"})
    @Ok("json")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object checkBangding(@Param("id")Integer id,@Param("ids") String[] ids) {
    	try {
    		Integer flag = 0;
    		 if (ids != null && ids.length > 0) {
    			  for(String iD :ids){
    			     String collectCode = bcardService.fetch(Integer.valueOf(iD)).getBcardCode();
    		         List<String> list = collectService.getCollectCodeList(collectCode);
    		         if (list != null && list.size() > 0) { //批量删除中存在的绑定，跳出循环
    		        	 flag = 1;
    		        	 break;
    		         }
    			  }
    			  if(flag==1){
    				  return Result.error("已绑定的采集器需要解绑后才能进行此操作!");  
    			  }else{
    				  return Result.success("system.success");
    			  }
             } else{
            	 String collectCode = bcardService.fetch(id).getBcardCode();
         		List<String> list = collectService.getCollectCodeList(collectCode);
             	if (list != null && list.size() > 0) {
             		return Result.error("已绑定的采集器需要解绑后才能进行此操作!");
             	}else{return Result.success("system.success");}
             }
    		
    	} catch (Exception e) {
    		return Result.error("system.error");
    	}
    }
    
    @At()
	@Ok("json:full")
	@RequiresPermissions("instrument.monitor.bCard")
	public Object search(String bcardCode,String orignCode, String operateTime,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		String[] list = operateTime.split("-");
		for (String string : list) {
			System.out.println(string.trim());
		}
		Cnd cnd = Cnd.NEW();
    	String linkName = null;
		NutMap bcardData = bcardService.data(length, start, draw, order, columns, cnd, linkName );
		
		Cnd subCnd = null;
		NutMap nutMap = bcardService.data(length, start, draw, order, columns, cnd, linkName, subCnd );
		
        return bcardData;
	}
    
    @At("/checkbcardCode_add")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object checkbcardCode_add(String bcardCode) {     	
    	Cnd cnd = Cnd.NEW();
    	boolean flag = bcardService.checkbcardCode_add(bcardCode,cnd);
    	if (flag) {
			return Result.success();
		}
    	return Result.error("采集器编号重复");
    }
    //校验生产编号编号
    @At("/checkorignCode_add/?")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object checkorignCode_add(@Param("orignCode")String orignCode) {
    	Cnd cnd = Cnd.NEW();
    	boolean flag = bcardService.checkorignCode_add(orignCode,cnd);
    	if (flag) {
			return Result.success();
		}
    	return Result.error("生产编号重复");
    }
    
    
    @At("/checkbcardCode/?")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object checkbcardCode(@Param("data")String data) {
    	String[] split = data.split("=");
    	String id = split[0];
    	String bcardCode = split[1];
    	String bcardCode1 = bcardService.fetch(Integer.valueOf(id)).getBcardCode();
    	Cnd cnd = Cnd.NEW();
    	boolean flag = bcardService.checkbcardCode(bcardCode1,bcardCode,cnd);
    	if (flag) {
    		return Result.error("采集器编号重复");
			
		}else{
			return Result.success();
		}
    	
    }
    //校验生产编号编号
    @At("/checkorignCode/?")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object checkorignCode(@Param("data")String data) {
    	Cnd cnd = Cnd.NEW();
    	String[] split = data.split("=");
    	String id = split[0];
    	String orignCode = split[1];
    	String orignCode1 = bcardService.fetch(Integer.valueOf(id)).getOrignCode();
    	
    	boolean flag = bcardService.checkorignCode(orignCode1,orignCode,cnd);
    	if (flag) {
    		return Result.error("采集器生产编号重复");
		}else{
			return Result.success();
		}
    	
    }
    
 
    // 模板下载
    @At()
    @Ok("raw")
    @RequiresPermissions("instrument.monitor.bCard")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("bcard_name");
    	filename = new String(filename.getBytes("UTF-8"),"iso-8859-1");
    	// 告诉浏览器要下载文件
    	response.setHeader("Content-Disposition", "attachment;filename="+filename);
    	// 告诉浏览器要下载文件而不是直接打开文件
    	response.setContentType("application/-download");
    	response.setCharacterEncoding("UTF-8");
    	// 获取项目路径
    	String appRoot = Globals.AppRoot;
    	String templatePath = (String) Configer.getInstance().get("bcard_template");
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
    @RequiresPermissions("instrument.monitor.bCard.importExcel")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
    @SuppressWarnings("unchecked")
    public Object upload( @Param("bcardFile") File file,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
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
			ReadExcel_Bcard excelUtil = new ReadExcel_Bcard();
			// 将excel表格中的数据存放到list当中
			// List<CollectBindDeviceBean> collect_devicetList = excelUtil.readExcel(path);
			Object object = excelUtil.readExcel(path);
			if (object instanceof  HashMap) {
				Map<String, String> map = (HashMap<String, String>)object;
				String errorMsg = map.get("error");
				response.setContentType("text/html");
				return Result.error(errorMsg);
			}else{
				List<Ins_DeviceBcard> collect_devicetList = (List<Ins_DeviceBcard>) object;
			     for(Ins_DeviceBcard card : collect_devicetList){
			    	 card.setOperateTime(time);
			     }
				// 插入数据库当中
				bcardService.insertList(collect_devicetList);
				// 设置响应头信息
				response.setContentType("text/html");
				result = Result.success("导入成功");	  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return result;
    }
	
}
