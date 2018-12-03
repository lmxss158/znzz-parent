package com.znzz.app.asset.moudules.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.znzz.app.web.commons.util.OrderByUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import com.znzz.app.asset.moudules.models.Ins_Asset_SamplingCheck;
import com.znzz.app.asset.moudules.services.InsSamplingCheckService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 周期检定的实现类
 * @classname InsAssetCycleCheckServiceImpl.java
 * @author chenzhongliang
 * @date 2017年11月30日
 */
@IocBean(args = { "refer:dao" })
public class InsSamplingCheckServiceImpl extends BaseServiceImpl<Ins_Asset_SamplingCheck> implements InsSamplingCheckService {
	private static final Log log = Logs.get();
	public InsSamplingCheckServiceImpl(Dao dao){
		super(dao);
	}

	@Override
	public NutMap getList(Ins_Asset_SamplingCheck assetCheck,  String module,int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns) {
		 Sql sql = Sqls.create("select * from ins_asset_samplingcheck  $condition $var $order");
    	 Cnd cnd = Cnd.NEW();
    	 	
    	 if("thisquarter".equalsIgnoreCase(module) || "nextquarter".equalsIgnoreCase(module)){
    		 if (assetCheck.getSamplingDate2() != null && !"".equals(assetCheck.getSamplingDate2())) {
 	        	String[] list = assetCheck.getSamplingDate2().split("-");
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
 	        	cnd.where().and("samplingDate", "between", new Object[]{startDate,endDate});
 			}
    	 }else{
	        if (assetCheck.getSamplingDate2() != null && !"".equals(assetCheck.getSamplingDate2())) {
	        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    			int year = Integer.parseInt(assetCheck.getSamplingDate2().split("-")[0]);
    			int month = Integer.parseInt(assetCheck.getSamplingDate2().split("-")[1]);
    			
    			Calendar cal = Calendar.getInstance();
    			cal.set(Calendar.YEAR,year);
    			cal.set(Calendar.MONTH, month-1);
    			cal.set(Calendar.DATE,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    			String endTime = df.format(cal.getTime());
    			
    			cal.set(Calendar.MONTH, month-1);
    			cal.set(Calendar.DATE,1);
    			String startTime = df.format(cal.getTime());
	        	cnd.where().and("samplingDate", "between", new Object[]{startTime,endTime});
			}
    	 }
	        if (assetCheck.getSentCheckDate2() != null && !"".equals(assetCheck.getSentCheckDate2())) {
	        	String[] list = assetCheck.getSentCheckDate2().split("-");
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
	        	cnd.where().and("sentCheckDate", "between", new Object[]{startDate,endDate});
			}
	        if (assetCheck.getCheckDate2() != null && !"".equals(assetCheck.getCheckDate2())) {
	        	String[] list = assetCheck.getCheckDate2().split("-");
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
	        	cnd.where().and("checkDate", "between", new Object[]{startDate,endDate});
			}
	        if (assetCheck.getGetDate2() != null && !"".equals(assetCheck.getGetDate2())) {
	        	String[] list = assetCheck.getGetDate2().split("-");
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
	        	cnd.where().and("getDate", "between", new Object[]{startDate,endDate});
			}
	        if (assetCheck.getFilDate2() != null && !"".equals(assetCheck.getFilDate2())) {
	        	String[] list = assetCheck.getFilDate2().split("-");
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
	        	cnd.where().and("filDate", "between", new Object[]{startDate,endDate});
			}

	        if (!Strings.isBlank(assetCheck.getTestDepartment())) {
	        	cnd.where().and("testDepartment", "like", "%" + assetCheck.getTestDepartment().trim() + "%");
	        }
	        if (!Strings.isBlank(assetCheck.getRespMan())) {
	        	cnd.where().and("respMan", "like","%"+assetCheck.getRespMan().trim()+"%");
	        }
	        if (!Strings.isBlank(assetCheck.getUserMan())) {
	        	cnd.where().and("userMan", "like", "%" + assetCheck.getUserMan().trim() + "%");
	        }
	        if (!Strings.isBlank(assetCheck.getUseDepartment())) {
	        	cnd.where().and("useDepartment", "like", "%" + assetCheck.getUseDepartment().trim() + "%");
	        }
	        if (!Strings.isBlank(assetCheck.getTestResult())) {
	        	cnd.where().and("testResult", "like", "%" + assetCheck.getTestResult().trim() + "%");
	        }


	      //本季度计划
    	 if("thisquarter".equalsIgnoreCase(module)){
    		 	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    		 	Date date = new Date();
    		 	Date sTime = getFirstDayOfQuarter(date);
    		 	Date eTime = getLastDayOfQuarter(date);
    		 	String startTime = df.format(sTime);
    		 	String endTime = df.format(eTime);
    		 	
    			cnd.where().and("samplingDate", "between",new Object[]{startTime,endTime});
    	 }
    	 //下季度计划
    	 if("nextquarter".equalsIgnoreCase(module)){
    		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
 		 	Date date = new Date();
 		 	Date sTime = getFirstDayOfNextQuarter(date);
 		 	Date eTime = getLastDayOfNextQuarter(date);
 		 	String startTime = df.format(sTime);
 		 	String endTime = df.format(eTime);
 		 	
 			cnd.where().and("samplingDate", "between",new Object[]{startTime,endTime});
    	 }
    	 
    	 if (order != null && order.size() > 0) {
    	 	Cnd cndt =  Cnd.NEW() ;
    	 	//添加中文排序
    	 	cndt = OrderByUtil.orderByWithChinese(cndt, order, columns);
             /*for (DataTableOrder or : order) {
                 DataTableColumn col = columns.get(or.getColumn());
				 //获取排序的字段
				 String name = col.getData();
				 if ("assetname".equals(name)){
					 name = " CONVERT( `assetname` USING gbk)";
				 }else if ("testdepartment".equals(name)){
					 name = " CONVERT( `testdepartment` USING gbk)";
				 }else if ("usedepartment".equals(name)){
					 name = " CONVERT( `usedepartment` USING gbk)";
				 }else if ("userman".equals(name)){
					 name = " CONVERT( `userman` USING gbk)";
				 }else if ("respman".equals(name)){
					 name = " CONVERT( `respman` USING gbk)";
				 }else if ("testresult".equals(name)){
					 name = " CONVERT( `testresult` USING gbk)";
				 }else if ("remark".equals(name)){
					 name = " CONVERT( `remark` USING gbk)";
				 }
				 cndt.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
             }*/

             sql.setVar("order",cndt) ;
 	     }
		//查统一编号,名称等信息
		if (!Strings.isBlank(assetCheck.getAssetCode())) {
			String code = assetCheck.getAssetCode() ;

			String str = "%"+code.trim()+"%" ;
			String par="" ;
			if(StringUtils.isBlank(cnd.toString())){
				par="where" ;
			}else{
				par="and" ;
			}
			Sql sql2 = Sqls.create(" $par (assetCode like @str or assetName like @str or assetModel like @str or serialNumber like @str)") ;
			sql2.setParam("str", str) ;
			sql2.setVar("par", par) ;
			sql.setVar("var", sql2) ;
		}
    	 sql.setCondition(cnd);
    	 NutMap data = data(length, start, draw, sql, sql);
    	 return data;
	}

	/* 
	 * 把excel中的list插入数据库,具体逻辑如下：
	 * 同一月份下，统一编号已存在，执行update，反之insert
	 */
	@Override
	public void insertList(List<Ins_Asset_SamplingCheck> assetList) {
		//根据统一编号和有效日期（到月份），进行更新或新增操作
		List<Ins_Asset_SamplingCheck> insertList = new ArrayList<Ins_Asset_SamplingCheck>();
		List<Ins_Asset_SamplingCheck> updateList = new ArrayList<Ins_Asset_SamplingCheck>();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		for(Ins_Asset_SamplingCheck check:assetList){
			String assetCode = check.getAssetCode();
			
			// 对抽检日期字段进行处理,拿到当前季度的时间范围
			Date date = check.getSamplingDate();
			Date stime = getFirstDayOfQuarter(date);
			Date etime = getLastDayOfQuarter(date);
			String startTime = df.format(stime);
			String endTime = df.format(etime);
			
			//查询ins_asset_cyclecheck表里同一季度中是否存在相同统一编号
			Cnd cnd = Cnd.NEW();
			cnd.where().and("assetCode","=",assetCode);
			cnd.where().and("samplingDate", "between",new Object[]{startTime,endTime});
			Ins_Asset_SamplingCheck iac = dao().fetch(Ins_Asset_SamplingCheck.class, cnd);
			if(iac!=null){
				check.setId(iac.getId());
				updateList.add(check);
			}else{
				insertList.add(check);
			}
			
			
		}
		
		dao().update(updateList);
		dao().insert(insertList);
	}
	
	 /** 
     * 得到本季度第一天的日期 
     * @Methods Name getFirstDayOfQuarter 
     * @return Date 
     */  
    public Date getFirstDayOfQuarter(Date date)   {     
        Calendar cDay = Calendar.getInstance();     
        cDay.setTime(date);  
        int curMonth = cDay.get(Calendar.MONTH);  
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
            cDay.set(Calendar.MONTH, Calendar.JANUARY);  
        }  
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
            cDay.set(Calendar.MONTH, Calendar.APRIL);  
        }  
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {    
            cDay.set(Calendar.MONTH, Calendar.JULY);  
        }  
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.OCTOBER);  
        }  
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return cDay.getTime();     
    }  
    /** 
     * 得到本季度最后一天的日期 
     * @Methods Name getLastDayOfQuarter 
     * @return Date 
     */  
    public Date getLastDayOfQuarter(Date date)   {     
        Calendar cDay = Calendar.getInstance();     
        cDay.setTime(date);  
        int curMonth = cDay.get(Calendar.MONTH);  
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
            cDay.set(Calendar.MONTH, Calendar.MARCH);  
        }  
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
            cDay.set(Calendar.MONTH, Calendar.JUNE);  
        }  
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {    
            cDay.set(Calendar.MONTH, Calendar.AUGUST);  
        }  
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.DECEMBER);  
        }  
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return cDay.getTime();     
    }  
    /** 
     * 得到下季度第一天的日期 
     * @Methods Name getFirstDayOfQuarter 
     * @return Date 
     */  
    public Date getFirstDayOfNextQuarter(Date date)   {     
        Calendar cDay = Calendar.getInstance();     
        cDay.setTime(date);  
        int curMonth = cDay.get(Calendar.MONTH);  
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
            cDay.set(Calendar.MONTH, Calendar.APRIL);  
        }  
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
            cDay.set(Calendar.MONTH, Calendar.JULY);  
        }  
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.OCTOBER);  
        }  
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.DECEMBER+1);  
        }  
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return cDay.getTime();     
    }  
    /** 
     * 得到下季度最后一天的日期 
     * @Methods Name getLastDayOfQuarter 
     * @return Date 
     */  
    public Date getLastDayOfNextQuarter(Date date)   {     
        Calendar cDay = Calendar.getInstance();     
        cDay.setTime(date);  
        int curMonth = cDay.get(Calendar.MONTH);  
        if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
            cDay.set(Calendar.MONTH, Calendar.JUNE);  
        }  
        if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
            cDay.set(Calendar.MONTH, Calendar.SEPTEMBER);  
        }  
        if (curMonth >= Calendar.JULY && curMonth <= Calendar.SEPTEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.DECEMBER);  
        }  
        if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
            cDay.set(Calendar.MONTH, Calendar.DECEMBER+3);  
        }  
        cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return cDay.getTime();     
    }


	
}
