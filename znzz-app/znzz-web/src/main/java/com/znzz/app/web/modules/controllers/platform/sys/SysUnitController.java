package com.znzz.app.web.modules.controllers.platform.sys;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;

import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.ReadExcel_Bcard;
import com.znzz.app.web.commons.util.ReadExcel_Unit;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * Created by wizzer on 2016/6/24.
 */
@IocBean
@At("/platform/sys/unit")
public class SysUnitController {
	
    private static final Log log = Logs.get();
    @Inject
    private SysUnitService sysUnitService;

//    @At("")
//    @Ok("beetl:/platform/sys/unit/index.html")
//    @RequiresPermissions("sys.manager.unit")
//    public Object index() {
//        return sysUnitService.query(Cnd.where("parentId", "=", "").or("parentId", "is", null).asc("path"));
//    }

    // 首页
    @At("")
    @Ok("beetl:/platform/sys/unit/index.html")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public void index() {

    }

    @At
    @Ok("json:full")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
  
     Cnd.where("parentId", "=", "").or("parentId", "is", null);
     return sysUnitService.data(length, start, draw, order, columns, Cnd.where("parentId", "=", "").or("parentId", "is", null), null);
    }
    
    @At
    @Ok("beetl:/platform/sys/unit/add.html")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object add(@Param("pid") String pid) {
        return Strings.isBlank(pid) ? null : sysUnitService.fetch(pid);
    }
    
    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.add")
    @SLog(tag = "新建单位", msg = "单位名称:${args[0].name}")
    public Object addDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
        	
            /*	Sys_unit sys_unit = sysUnitService.fetch(Cnd.where("parentId", "=", parentId));
            	String unitcode = sys_unit.getUnitcode();*/
            	
                sysUnitService.save(unit, parentId);
                return Result.success("system.success");
            } catch (Exception e) {
                return Result.error("system.error");
            }
    }
    
    @At("/child/?")
    @Ok("beetl:/platform/sys/unit/child.html")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object child(String id) {
        return sysUnitService.query(Cnd.where("parentId", "=", id).asc("path"));
    }

    @At("/detail/?")
    @Ok("beetl:/platform/sys/unit/detail.html")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object detail(String id) {
    	Sys_unit fetch = sysUnitService.fetch(id);
        return fetch;
    }

    @At("/edit/?")
    @Ok("beetl:/platform/sys/unit/edit.html")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object edit(String id, HttpServletRequest req) {
        Sys_unit unit = sysUnitService.fetch(id);
        if (unit != null) {
        	if(unit.getParentId()==null){
        		unit.setParentId("");
        	}
            req.setAttribute("parentUnit", sysUnitService.fetch(unit.getParentId()));
        }
        return unit;
    }

    @At
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.edit")
    @SLog(tag = "编辑单位", msg = "单位名称:${args[0].name}")
    public Object editDo(@Param("..") Sys_unit unit, @Param("parentId") String parentId, HttpServletRequest req) {
        try {
            unit.setOpBy(StringUtil.getUid());
            unit.setOpAt((int) (System.currentTimeMillis() / 1000));
            
            String uid = sysUnitService.fetch(unit.getId()).getId();
            sysUnitService.updateIgnoreNull(unit);
            /*if(uid.equals(unit.getUnitcode())){
            	//机构编码没变
            }else{
            	//unitcode机构编码发生改变，需要同步修改相应id字段
            	sysUnitService.updateUnit(unit);
            }*/
            
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/delete/?")
    @Ok("json")
    @RequiresPermissions("sys.manager.unit.delete")
    @SLog(tag = "删除单位", msg = "单位名称:${args[1].getAttribute('name')}")
    public Object delete(String id, HttpServletRequest req) {
        try {
            Sys_unit unit = sysUnitService.fetch(id);
            req.setAttribute("name", unit.getName());
            if ("0001".equals(unit.getPath())) {
                return Result.error("system.not.allow");
            }
            boolean flag = sysUnitService.hashEmployee(id);
            if(flag){
            	return Result.error("删除失败！该单位或它的子级单位下存在职工或用户，无法删除");
            }
            sysUnitService.deleteAndChild(unit);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    
    @At
    @Ok("json")
     @RequiresPermissions(value={"sys.manager.unit","sys.manager_bak.unit"},logical=Logical.OR)
    public Object tree(@Param("pid") String pid) {
        List<Sys_unit> list = sysUnitService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Sys_unit unit : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", unit.getId());
            obj.put("text", unit.getName());
            obj.put("children", unit.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }
    @At
    @Ok("json:full")
  
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
    @SuppressWarnings("unchecked")
    public Object upload( @Param("unitFile") File file,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
//    	File f = tf.getFile();                        	// 这个是保存的临时文件
//        FieldMeta meta = tf.getMeta();                // 这个原本的文件信息
//        String oldName = meta.getFileLocalName();     // 这个时原本的文件名称
    	Result result = null;
    	
		try {
			 
		 
	
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			ReadExcel_Unit excelUtil = new ReadExcel_Unit();
			// 将excel表格中的数据存放到list当中
			// List<CollectBindDeviceBean> collect_devicetList = excelUtil.readExcel(path);
			Object object = excelUtil.readExcel(path);
			if (object instanceof  HashMap) {
				Map<String, String> map = (HashMap<String, String>)object;
				String errorMsg = map.get("error");
				response.setContentType("text/html");
				return Result.error(errorMsg);
			}else{
				List<Sys_unit> unitList = (List<Sys_unit>) object;
			   
				// 插入数据库当中
			     sysUnitService.insertList(unitList);
				// 设置响应头信息
				response.setContentType("text/html");
				result = Result.success("导入成功");	  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return result;
    }
    // 模板下载
    @At()
    @Ok("raw")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("unit_name");
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
	

    @At
    @Ok("json:full")
    @RequiresAuthentication
    public Object getUnitJsonList(){
    	Subject subject = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) subject.getPrincipal();
        Map<String, String> unitAndParentId = sysUnitService.getUnitIdAndParentId() ;//获取父子级关系
        String topUnitId = sysUnitService.getTopUnitId(unitAndParentId, user.getUnitid()) ;//获取其最顶级单位id
        Map<String, List<String>> unitTreeGroup = sysUnitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
        ArrayList<String> list = new ArrayList<String>() ;
        list.add(topUnitId) ;
        sysUnitService.getChildUnitList(unitTreeGroup, topUnitId, list);
        //顶级下所有单位ids
        String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;
    	
    	
    	return sysUnitService.getUnitIdAndUnitNameList(allUnitds) ;
    }
    
    @At
    @Ok("json")
    public int cherkUnitCode(@Param("unitcode")String unitcode){
    	Cnd cnd = Cnd.NEW();
		int count=0;
		// 对传入日期进行处理
		if (!Strings.isBlank(unitcode)) {
			cnd.where().and("unitcode", "=", unitcode);
			Sys_unit u=sysUnitService.fetch(cnd);
			if(null!=u){
				count=1;
			};
		}
		return count;
    	
    }
    
}
