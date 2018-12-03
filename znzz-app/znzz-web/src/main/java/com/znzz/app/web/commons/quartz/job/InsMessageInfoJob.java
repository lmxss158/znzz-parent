package com.znzz.app.web.commons.quartz.job;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.instrument.modules.service.InsMessageInfoService;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.util.Configer;

/**
 * @author chenzhongliang
 * 定时任务：统计单位下的设备使用率
 */
@IocBean
public class InsMessageInfoJob implements Job{
	private static final Log log = Logs.get();
	
	@Inject
    private InsHomeService homeService ;
    @Inject
    private SysUnitService unitService ;
	@Inject
	private InsMessageInfoService messageService;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~~设备使用率统提醒任务开始~~~~~~~~~~~~~~~~~");
		//存在受监控设备的单位 对应设备使用信息
		List<Ins_MessageInfo> messageInfo = getMessageInfo();
		//若设备使用率低于标准，则插入数据库（ins_message_info表）
		messageService.quartzInsert(messageInfo);
		
		log.info("~~~~~~~~~~~设备使用率提醒任务结束~~~~~~~~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
	
	
	//处理数据，若某单位下存在受监控设备，拿到对应单位id,单位名称，设备使用率，监控数量
	public List<Ins_MessageInfo> getMessageInfo(){
		//拿到单位树结构 Map（单位id,该单位下子级单位集合）
        Map<String, String> unitAndParentId = unitService.getUnitIdAndParentId() ;
        Map<String, List<String>> unitTreeGroup = unitService.getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
        //顶级单位id
        String unitId = unitService.getTopUnitId(unitAndParentId, unitService.getAllUnitid().get(0)) ;
       
        List<String> list = new ArrayList<String>();
        list.add(unitId) ;
        unitService.getChildUnitList(unitTreeGroup, unitId, list);
        //该单位下级所有单位ids
        String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;

        List<Ins_MessageInfo> infoList = statusData(unitTreeGroup,unitId,list);
        
        Map<String, Integer> unitLinkCollectNum = homeService.getUnitLinkCollectNum(allUnitds) ;
        
        List<Ins_MessageInfo> messageList = new ArrayList<Ins_MessageInfo>();
        //遍历集合，添加单位下受监控设备数量
        for(Ins_MessageInfo info:infoList){
        	Integer num = unitLinkCollectNum.get(info.getUnitid());
        	if(null!=num){
        		info.setUnitDeviceNum(Integer.toString(num));
        		messageList.add(info);
        	}
        }

        //读取配置文件
        String rate = (String) Configer.getInstance().get("rate");
		Float deviceRate = Float.parseFloat(rate)*100;
		
		List<Ins_MessageInfo> insertList = new ArrayList<Ins_MessageInfo>();
		for (Ins_MessageInfo info : messageList) {
			//设备使用率
			Float powerOnRate = Float.parseFloat(info.getPowerOnRate());
			if(powerOnRate<deviceRate){
				insertList.add(info);
			}
		}
        
        
        return insertList;
	}
	
	 //获取单位设备使用率
	 public List<Ins_MessageInfo> statusData(Map<String, List<String>> unitTreeGroup,String topUnitId,List<String> li){
         
         List<Ins_MessageInfo> infoList = new ArrayList<Ins_MessageInfo>();
		 
         if(li.size()>0){
       	  String allUnitds = StringUtils.strip(Json.toJson(li),"[]") ;
       	  //查询每个单位最近七天使用时长大于60分钟的设置为开机数
       	  Map<String,Integer> homeAllCount = homeService.getHomePowerOnCountByDepartFrDayDuration(allUnitds) ;
       	  //查询各个单位的接入设备数量
       	  Map<String, Integer> unitLinkCollectNum = homeService.getUnitLinkCollectNum(allUnitds) ;
       	  //获取各个单位的名称
       	  Map<String, String> unitAndName = unitService.getUnitAndName(allUnitds) ;
       	  
       	  HashMap<String,Float> map = new HashMap<String,Float>();  
          ValueComparator bvc =  new ValueComparator(map);  

          for(int i=0;i<li.size() ;i++){
           	  	 String key = li.get(i) ;
            	 ArrayList<String> list = new ArrayList<String>();
            	 list.add(key) ;
            	 unitService.getChildUnitList(unitTreeGroup, key, list);
                String unitName = unitAndName.get(key);
                float pon =0 ;
                float lnm =0 ;
                for(int s=0;s<list.size();s++){
               	 String ccode = list.get(s) ;
               	 Integer sre = homeAllCount.get(ccode) ;
               	 Integer lre = unitLinkCollectNum.get(ccode) ;
               	 if(sre!=null){
               		 pon+=sre ;
               	 }
               	 if(lre!=null){
               		 lnm+=lre ;
               	 }
               	 
               	 
                }
                float i1 = 0 ;
                if(lnm!=0){
               	 i1 = (pon/(lnm*7))*100 ;
               	 i1=i1>100?100:i1 ;
                }
                
				 //map.put(unitName, i1);
				 Ins_MessageInfo info = new Ins_MessageInfo();
                 info.setUnitid(key);
                 info.setUname(unitName);
                 info.setPowerOnRate(String.format("%.2f", i1));
                 infoList.add(info);
             }
       	  
         }
         return infoList;
         
    }
	
	 class ValueComparator implements Comparator<String> {  
		  
		    Map<String, Float> base;  
		    //这里需要将要比较的map集合传进来
		    public ValueComparator(Map<String, Float> base) {  
		        this.base = base;  
		    }  
		  
		    // Note: this comparator imposes orderings that are inconsistent with equals.    
		    //比较的时候，传入的两个参数应该是map的两个key，根据上面传入的要比较的集合base，可以获取到key对应的value，然后按照value进行比较   
		    public int compare(String a, String b) {  
		        if (base.get(a) >= base.get(b)) {  
		            return -1;  
		        } else {  
		            return 1;  
		        } // returning 0 would merge keys  
		    }  
		}  
	 
	 
}
