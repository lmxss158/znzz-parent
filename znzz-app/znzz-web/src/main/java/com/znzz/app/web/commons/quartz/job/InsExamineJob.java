package com.znzz.app.web.commons.quartz.job;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsCycleExamineService;
import com.znzz.app.sys.modules.models.Sys_task;

@IocBean
public class InsExamineJob implements Job{
	private static final Log log = Logs.get();
	
	@Inject
	private InsCycleExamineService cycleExamineService;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~更新资产表过期信息任务开始~~~~~~~~~~");
		List<Ins_Assets>  examineList= cycleExamineService.getDuedateList();	//获取dueDate字段集合(未过期的记录)
		Date date = new Date();
		if(!examineList.isEmpty() || examineList.size()>0){
			for (int i = 0; i < examineList.size(); i++) {
				Integer id = examineList.get(i).getId();
				Integer isOverdue = examineList.get(i).getIsOverdue();
				Date dueDate = examineList.get(i).getDueDate();
				if(dueDate==null){
					return;
				}
				if(dueDate.getTime() <= date.getTime() ){	//过期了
					if(isOverdue!=0)
					cycleExamineService.updateIsover(id,0);	//更新是否过期字段
				}else{
					if(isOverdue==0)
					cycleExamineService.updateIsover(id,1);	
				}
			}
		}
		
		log.info("~~~~~~~~~~更新资产表过期信息任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}

}
