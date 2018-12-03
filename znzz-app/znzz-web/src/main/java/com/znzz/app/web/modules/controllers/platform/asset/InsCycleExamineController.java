package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
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
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsCycleExamineService;
import com.znzz.app.instrument.modules.models.Ins_CycleExamine;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.ExamineUtil;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;

/**
 * 周期检定
 * @author chenzhongliang
 *
 */
@IocBean
@At("/cycle/examine")
public class InsCycleExamineController {
		private static final Log log = Logs.get();
		private static final Logger log2 = Logger.getLogger("scadaLogger");	//scada日志
	
		@Inject
		InsCycleExamineService cycleExamineService;
		@Inject
		ScadaDeviceServcie  scadaDeviceServcie;
		@Inject
		InsbcardService bcardService;
		@Inject
		SysUnitService unitService;
	
	    //首页
		@At("")
		@Ok("beetl:/platform/asset/examine/index.html")	//跳转页面
		@RequiresPermissions("cycle.examine")	//权限标识
		public void index() {

		}
		
		//条件搜索
		@At
	    @Ok("json:full")
	    @RequiresPermissions("cycle.examine")
	    public Object data(@Param("assetCode")String assetCode,@Param("dueDate")String dueDate,@Param("isOverdue")String isOverdue, @Param("borrowDepart")String borrowDepart,@Param("chargePerson")String chargePerson,@Param("examineDate")String examineDate,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
			//Criteria cnd = Cnd.cri();
			Cnd cnd = Cnd.NEW();
			Sql sql = Sqls.create("select distinct(i.assetCode),i.id,v.assetName,c.name as borrowdepart,d.username as chargeperson,i.isOverdue,i.enableTime,i.examineDate,i.dueDate "+
								  "from ins_assets_info i left join ins_assets_version v on i.deviceVersion=v.deviceVersion "+
								  "left join sys_unit c on i.borrowDepart=c.id "+
								  "left join sys_user d on i.chargePerson=d.id  $condition ");
	    	// 对传入日期进行处理
	        if (examineDate != null && !"".equals(examineDate)) {
	        	// 对传入的时间进行处理
	        	String[] list = examineDate.split("-");
	        	String startTime = list[0].trim();
	        	String endTime = list[1].trim();
	        	SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd");
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
	        	cnd.where().and("i.examineDate", "between", new Object[]{startDate,endDate});
				//cnd = Cnd.where("examineDate", "between", new Object[]{startDate,endDate});
			}
	        if (dueDate != null && !"".equals(dueDate)) {
	        	String[] list = dueDate.split("-");
	        	String startTime = list[0].trim();
	        	String endTime = list[1].trim();
	        	SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd");
	        	Date startDate = null;
	        	Date endDate = null;
	        	try {
	        		startDate = sdf.parse(startTime);
	    			endDate = sdf.parse(endTime);
	    		} catch (ParseException e) {
	    			log.error("startTime和endTime:"+startTime+","+endTime+",时间转换异常");
	    			e.printStackTrace();
	    		}
	        	cnd.where().and("i.dueDate", "between", new Object[]{startDate,endDate});
			}
	        if (!Strings.isBlank(assetCode)) {
				cnd.where().and("i.assetCode", "like", "%" + assetCode.trim() + "%");
			}
	        if (!Strings.isBlank(isOverdue)) {
	        	cnd.where().and("i.isOverdue", "like", "%" + isOverdue.trim() + "%");
	        }
	        if (!Strings.isBlank(borrowDepart)) {
	        	String list = unitService.getChildList(borrowDepart);
	        	cnd.where().and("i.borrowDepart", "in", list);
	        }
	        if (!Strings.isBlank(chargePerson)) {
	        	cnd.where().and("i.chargePerson", "like", "%" + chargePerson.trim() + "%");
	        }
	        cnd.where().and("i.assetType","=","2"); 	//页面只显示类型为仪器的资产
	        
	        if (order != null && order.size() > 0) {
	            for (DataTableOrder or : order) {
	                DataTableColumn col = columns.get(or.getColumn());
	                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), or.getDir());
	            }
	        }
	        
	        sql.setCondition(cnd);
	        
	    	NutMap gatewayData = cycleExamineService.data(length, start, draw, sql, sql);
	        return gatewayData;
	    }
		
		
		/**
		 * 检定(sql查询，回显相应字段)
		 * @param id
		 * @return
		 */
		@At("/test/?")
		@Ok("beetl:/platform/asset/examine/test.html")
		@RequiresPermissions("cycle.examine")
		public Object test(Integer id){
			Sql sql = Sqls.create("select distinct(i.assetCode),i.id,v.assetName,i.enableTime,i.examineDate,i.dueDate,r.conclusion,r.overdueReaspn,r.id recordid "+
								  "from ins_assets_info i left join ins_assets_version v on i.deviceVersion=v.deviceVersion "+
								  "LEFT JOIN ins_examine_record r on i.assetCode = r.assetCode and v.assetName = r.assetName "+
								  "where i.id='"+id+"' and r.id=(select MAX(e.id) from ins_examine_record e where e.assetCode = i.assetCode and e.assetName = v.assetName)");
			//Ins_Assets insAssets = cycleExamineService.fetch(id);
			List<Object> list = cycleExamineService.findExaminebyId(sql);
			if(list.size()==0 || list.isEmpty() ){
				Sql sql2 = Sqls.create("select distinct(i.assetCode),i.id,v.assetName,i.enableTime,i.examineDate,i.dueDate "+
						"from ins_assets_info i left join ins_assets_version v on i.deviceVersion=v.deviceVersion "+
						"where i.id='"+id+"'");
				List<Object> list2 = cycleExamineService.findExaminebyId2(sql2);
				list2.add("test");
				return list2;
			}else{
				list.add("test");
			}
			return list;
		}
		
		/**
		 * 修改
		 * @param id
		 * @return
		 */
		@At("/update/?")
		@Ok("beetl:/platform/asset/examine/test.html")
		@RequiresPermissions("cycle.examine")
		public Object update(Integer id){
			Sql sql = Sqls.create("select distinct(i.assetCode),i.id,v.assetName,i.enableTime,i.examineDate,i.dueDate,r.conclusion,r.overdueReaspn,r.id recordid "+
								  "from ins_assets_info i left join ins_assets_version v on i.deviceVersion=v.deviceVersion "+
								  "LEFT JOIN ins_examine_record r on i.assetCode = r.assetCode and v.assetName = r.assetName "+
								  "where i.id='"+id+"' and r.id=(select MAX(e.id) from ins_examine_record e where e.assetCode = i.assetCode and e.assetName = v.assetName)");
			//Ins_Assets insAssets = cycleExamineService.fetch(id);
			List<Object> list = cycleExamineService.findExaminebyId(sql);
			if(list.size()==0 || list.isEmpty() ){	//当前资产  在记录表里没有 记录
				Sql sql2 = Sqls.create("select distinct(i.assetCode),i.id,v.assetName,i.enableTime,i.examineDate,i.dueDate "+
						"from ins_assets_info i left join ins_assets_version v on i.deviceVersion=v.deviceVersion "+
						"where i.id='"+id+"'");
				List<Object> list2 = cycleExamineService.findExaminebyId2(sql2);
				list2.add("update");
				return list2;
			}else{
				list.add("update");
			}
			return list;
		}
		
		@At
	    @Ok("json")
	    @RequiresPermissions("cycle.examine.test")
	    @SLog(tag = "周期检定", msg = "统一编号:${args[0].assetCode}")
		public Object testDo(@Param("..") Ins_Assets insAssets,String wherefor,String recordid,String assetName, String conclusion,String overdueReaspn,HttpServletRequest req) {
	        try {
	        	String result = testScard(insAssets.getAssetCode(),insAssets.getExamineDate());
	        if("1".equals(result)){
	        	
	        	insAssets.setOpBy(StringUtil.getUid());
	        	insAssets.setOpAt((int) (System.currentTimeMillis() / 1000));
	        	//修改资产表检定日期，到期检定日期两个字段
	        	cycleExamineService.updateAssetInfo(insAssets.getId(),insAssets.getExamineDate(),insAssets.getDueDate());
	        	
	        	if("检定".equals(wherefor)){
	        		Ins_CycleExamine insCycleExamine = new Ins_CycleExamine();
	        		insCycleExamine.setAssetCode(insAssets.getAssetCode());	//统一编号
	        		insCycleExamine.setAssetName(assetName); //资产名称
	        		insCycleExamine.setEnableTime(insAssets.getEnableTime()); //启用时间
	        		insCycleExamine.setExamineDate(insAssets.getExamineDate()); //检定时间
	        		insCycleExamine.setExaminePerson(StringUtil.getUsername()); //检定人
	        		if(!"".equals(conclusion)){
	        			insCycleExamine.setConclusion(Integer.parseInt(conclusion)); //检定结论
	        		}
	        		insCycleExamine.setDueDate(insAssets.getDueDate()); //到期检定时间
	        		insCycleExamine.setOverdueReaspn(overdueReaspn); //过期结论
	        		//写入周检记录表
		        	cycleExamineService.insertExamineRecord(insCycleExamine);
	        	}else if("修改".equals(wherefor)){
	        		Ins_CycleExamine insCycleExamine = new Ins_CycleExamine();
	        		insCycleExamine.setAssetCode(insAssets.getAssetCode());	//统一编号
	        		insCycleExamine.setEnableTime(insAssets.getEnableTime()); //启用时间
	        		insCycleExamine.setAssetName(assetName); //资产名称
	        		insCycleExamine.setExamineDate(insAssets.getExamineDate()); //检定时间
	        		insCycleExamine.setDueDate(insAssets.getDueDate()); //到期检定时间
	        		insCycleExamine.setOverdueReaspn(overdueReaspn); //过期结论
	        		insCycleExamine.setExaminePerson(StringUtil.getUsername()); //检定人
	        		if(!"".equals(conclusion)){
	        			insCycleExamine.setConclusion(Integer.parseInt(conclusion)); //检定结论
	        		}
	        		if(!Strings.isBlank(recordid)){
	        			insCycleExamine.setId(Integer.parseInt(recordid));
	        			cycleExamineService.update(insCycleExamine);
	        		}else{
	        			cycleExamineService.insertExamineRecord(insCycleExamine);
	        		}
	        		
	        		
	        	}
	        	
	            return Result.success("检测周期设定成功");
	        }else if(result == null){
	        	log2.info("scada连接无效，返回为空");
				return Result.error("SCADA传入数据为空");
			}else if("-1".equals(result)){
				log2.info("调用scada时,找不到当前资产统一编号对应的生产编号");
				return Result.error("调用scada时,找不到当前统一编号对应的生产编号");
			}else if("0".equals(result)){
				log2.info("scada数据设置失败，返回0");
				return Result.error("SCADA数据设置失败，返回结果为0");
			}else{
				log2.info("检测周期设定失败...");
				return Result.error("检测周期设定失败...");
			}
	        
	        } catch (Exception e) {
	        	log2.info("出现预料之外的异常...");
	            return Result.error("检测周期设定失败~");
	        }
	    }
		
		/**
		 * 找到统一编号对应设备id，调用scada
		 * @param assetCode
		 * @param time
		 * @return
		 */
		public String testScard(String assetCode,Date time){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, String> map = bcardService.getOrignCodeAndDeviceCode();
			String deviceid = map.get(assetCode);
			if(deviceid==null){
				return "-1";
			}
			String result = scadaDeviceServcie.getResult(new String[]{deviceid,sdf.format(time)}, Globals.SETTESTCYCLE);
			return result;
		}
		
		
		/**
		 * 检定记录
		 * @param assetcode
		 * @param length
		 * @param start
		 * @param draw
		 * @return
		 */
		@At("/record/?")
		@Ok("json:full")
		@RequiresPermissions("cycle.examine")
		public Object record(@Param("assetcode")String assetcode,@Param("assetname")String assetname,@Param("length") int length, @Param("start") int start, @Param("draw") int draw){
			NutMap list = cycleExamineService.findList(assetcode,length, start, draw);
			return list;
		}
		
		
		
		/**
		 * 统计资产表中过期数量
		 * @return
		 */
		public int countOver(){
			return cycleExamineService.countOver();
		}
		
		
		/**
		 * 根据设置好的时间栈，判断资产表中即将过期的资产数量
		 * @return
		 */
		public int countOverByTime(){
			String dayTime="10";	//设置 10天
			
			//处理时间，拿到从当前时间到10天后的时间栈
			Date startTime = new Date();
			long et = startTime.getTime()+Integer.parseInt(dayTime)*(1000*60*60*24);
			Date endTime = new Date(et);
			//统计该时间段过期资产数目
			int count = cycleExamineService.countOverByTime(startTime,endTime);
			return count;
		}
		
		 @At
		 @Ok("json:full")
		 @RequiresPermissions("cycle.examine")
		 @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
		 @SuppressWarnings("unchecked")
		 public Object importfile(@Param("examineFile") File file,HttpServletRequest request,HttpServletResponse response){
			 Result result = null;
				try {
					// 获取文件的绝对路径
					String path = file.getAbsolutePath();
					ExamineUtil util = new ExamineUtil();
					Object object = util.readExcel(path);
					
					if (object instanceof  HashMap) {
						Map<String, String> map = (HashMap<String, String>)object;
						String errorMsg = map.get("error");
						response.setContentType("text/html");
						result = Result.error(errorMsg);
					}else{
						List<Ins_Assets> assetList = (List<Ins_Assets>) object;
						String errormsg="";
						String code = "";
						for (Ins_Assets a : assetList) {
							String r = testScard(a.getAssetCode(), a.getExamineDate());
							if(!"1".equals(r)){
								//return Result.error("调取SCADA接口失败，失败的统一编号是："+a.getAssetCode());
								errormsg = r;
								code = a.getAssetCode();
								break;
							}
						}
						if(errormsg == null){
				        	log2.info("导入统一编号"+code+"时，scada连接无效，返回为空");
				        	response.setContentType("text/html");
							result=Result.error("导入统一编号"+code+"时，scada连接无效，返回为空");
						}else if("-1".equals(errormsg)){
							log2.info("调用scada时,找不到统一编号"+code+"对应的生产编号");
							response.setContentType("text/html");
							result= Result.error("调用scada时,找不到统一编号"+code+"对应的生产编号");
						}else if("0".equals(errormsg)){
							log2.info("导入统一编号"+code+"时,scada数据设置失败，返回0");
							response.setContentType("text/html");
							result= Result.error("导入统一编号"+code+"时,scada数据设置失败，返回0");
						}else{
							// 插入或更新数据库
							cycleExamineService.insertList(assetList);
							
							response.setContentType("text/html");
							result = Result.success("导入成功");
						}
						
						
					}
				} catch (Exception e) {
					log2.info("检测周期设定失败...");
					response.setContentType("text/html");
				    result = Result.error("出现未知异常，导入失败...");
				}
				
		    	return result;
		}

}
