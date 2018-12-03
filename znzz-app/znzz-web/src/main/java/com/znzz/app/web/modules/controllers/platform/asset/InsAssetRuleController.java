package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
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
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.ImageBinaryUtil;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * 
 * @ClassName: InsAssetRuleController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author pengmantai
 * @date 2017年8月16日 下午2:54:29
 */
@IocBean
@At("/asset/rule")
public class InsAssetRuleController {
	private static final Log log = Logs.get();

	@Inject
	InsAssertRuleService ruleService;

	// 首页
	@At("")
	@Ok("beetl:/platform/asset/rule/index.html")
	@RequiresPermissions("asset.auth.rule")
	public void index() {
		System.out.println("进入首页");
	}

	@At("/detail/?")
	@Ok("json")
	@RequiresPermissions("asset.auth.rule")
	public Object detail(Integer id) { 
		Ins_Asset_Rule rule = ruleService.fetch(id);
		String code = new ImageBinaryUtil().getImageBinary(rule.getUrlImage());
		if(null!=code){
			rule.setUrlImage(code);
		}else{
			rule.setUrlImage("");
		}
		return rule;
	}

	@At("/edit/?")
	@Ok("beetl:/platform/asset/rule/edit.html")
	@RequiresPermissions("asset.auth.rule")
	public Object edit(Integer id) {
		Ins_Asset_Rule rule = ruleService.fetch(id);
		String code = new ImageBinaryUtil().getImageBinary(rule.getUrlImage());
		if(null!=code){
			rule.setUrlImage(code);
		}else{
			rule.setUrlImage("");
		}
		return rule;
	}

	@At
	@Ok("json")
	@RequiresPermissions("asset.auth.rule.edit")
	@SLog(tag = "编辑型号规格", msg = "型号名称:${args[0].assetName}")
	public Object editDo(@Param("..") Ins_Asset_Rule rule, HttpServletRequest req) {
		try {
			rule.setOpBy(StringUtil.getUid());
			rule.setOpAt((int) (System.currentTimeMillis() / 1000));
			if(rule.getUrlImage().length()>300){
				rule.setUrlImage(null);
			}
			//ruleService.updateIgnoreNull(rule);
			ruleService.updateAll(rule);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At
	@Ok("beetl:/platform/asset/rule/add.html")
	@RequiresPermissions("asset.auth.rule")
	public void add() {

	}

	@At
	@Ok("json")
	@SLog(tag = "新建型号", msg = "型号名称:${args[0].assetName}")
	@RequiresPermissions("asset.auth.rule.add")
	public Object addDo(@Param("..") Ins_Asset_Rule rule, HttpServletRequest request) {
		//System.out.println(urlImage+"~~~~~");

		try {
			rule.setDeviceVersionOrg(rule.getDeviceVersion());
			rule.setCreateTime(new Date());
			ruleService.insert(rule);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At({ "/delete", "/delete/?" })
	@Ok("json")
	@RequiresPermissions("asset.auth.rule.delete")
	public Object delete( Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
		try {
			if (ids != null && ids.length > 0) {
				ruleService.delete(ids);
				req.setAttribute("id", StringUtils.toString(ids));
			} else {
				ruleService.delete(id);
				req.setAttribute("id", id);
			}
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
	}

	@At()
	@Ok("json:full")
	@RequiresPermissions("asset.auth.rule")
	public Object data(String deviceVersion, String assetName, String createTime, @Param("length") int length,
			@Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order,
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
		if (!Strings.isBlank(deviceVersion)) {
			cnd.and("deviceVersion", "like", "%" + deviceVersion.trim() + "%");
		}
		if (!Strings.isBlank(assetName)) {
			cnd.and("assetName", "like", "%" + assetName.trim() + "%");
		}
		cnd.and("deviceVersion","NOT IS",null);
		cnd.and("deviceVersion","!=","");
		if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), or.getDir());
            }
        }
		
		NutMap ruleData = ruleService.data(length, start, draw, order, columns, cnd, null);
		List<Ins_Asset_Rule> list = ruleData.getList("data", Ins_Asset_Rule.class);
		for (Ins_Asset_Rule r : list) {
			String image = r.getUrlImage();
			if(!"".equals(image) && null!=image){
				//截取文件名
				String imagename = image.substring(image.lastIndexOf("/")+1);
				r.setUrlImage(imagename);
			}
		}
		
		return ruleData;
	}
	
	@At("/checkVersion")
	@Ok("json")
	public int check(@Param("deviceVersion")String deviceVersion) {
		Cnd cnd = Cnd.NEW();
		int count=0;
		// 对传入日期进行处理
		if (!Strings.isBlank(deviceVersion)) {
			cnd.where().and("deviceVersion", "=", deviceVersion);
			Ins_Asset_Rule rule=ruleService.fetch(cnd);
			if(null!=rule){
				count=1;
			};
		}
		return count;
	}
	@At("/checkID")
	@Ok("json")
	public int checkID(@Param("deviceVersion")String deviceVersion,@Param("id")String id) {
		Cnd cnd = Cnd.NEW();
		int count=0;
		// 对传入日期进行处理
		if (!Strings.isBlank(deviceVersion)) {
			cnd.where().and("deviceVersion", "=", deviceVersion);
			cnd.where().and("id","!=",id);
			Ins_Asset_Rule rule=ruleService.fetch(cnd);
			if(null!=rule){
				count=1;
			};
		}
		return count;
	}
	
	/**
	 * @param tmpFile
	 * @param req
	 * @param context
	 * @return
	 * @throws IOException
	 */
	@At("/upImage")
	@AdaptBy(type = UploadAdaptor.class,args = { "/uploadTemp", "8192", "UTF-8", "10" }) 	//临时文件夹，缓冲区大小，编码，临时文件个数限制
	public String upImage(@Param("file")TempFile tmpFile,HttpServletRequest req,ServletContext context) throws IOException{
		/* System.out.println(context.getRealPath("/")); //项目绝对路径
		 System.out.println(req.getServletPath()); //所在页面路径
		 System.out.println(tmpFile.getSubmittedFileName());//文件名
         System.out.println(tmpFile.getSize());
         */
         String[] postfix = tmpFile.getSubmittedFileName().split("\\.");	//postfix[0]是图片名，[1]是图片格式
         postfix[0]+=UUID.randomUUID();	//控制图片名不重复，图片名后加uuid
         String path = Configer.getInstance().getProperty("imageUploadPath");
         File temp = new File( path );
         if(!temp.exists()){
             temp.mkdirs();
         }
         tmpFile.write( temp.getPath() + "/" + postfix[0] + "." + postfix[postfix.length - 1] );
         tmpFile.delete();
		
         return temp.getPath()+"/"+postfix[0] + "." + postfix[postfix.length - 1];
	}
	
}
