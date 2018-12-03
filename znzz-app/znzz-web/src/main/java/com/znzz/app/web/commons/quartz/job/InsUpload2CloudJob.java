package com.znzz.app.web.commons.quartz.job;

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
import com.znzz.app.instrument.modules.service.InsYunGatewayService;
import com.znzz.app.sys.modules.models.Sys_task;

@IocBean
public class InsUpload2CloudJob implements Job{
	
	 private static final Log log = Logs.get();
	 
	 @Inject
	 private InsYunGatewayService yunGatewayService;
	 
	 @Inject
	 protected Dao dao;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("~~~~~~~~~~实时数据上传云网任务开始~~~~~~~~~~");
		yunGatewayService.uploadCloudbyRealTime();
		log.info("~~~~~~~~~~实时数据上传云网任务结束~~~~~~~~~~");
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));
	}

}
