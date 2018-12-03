package com.znzz.app.web.commons.quartz.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.znzz.app.instrument.modules.models.Ins_DeviceFacilityHour;
import com.znzz.app.instrument.modules.service.InsHomeService;
import com.znzz.app.sys.modules.models.Sys_task;

@IocBean
public class InsDeviceFacilityHourJob implements Job{
	private static final Log log = Logs.get();
	@Inject
	private InsHomeService homeService ;
	@Inject
	protected Dao dao;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("统计设备开机数&采集总数~~~~~~~~~~~~~~~~~~~~");
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Ins_DeviceFacilityHour facilityHourBean = homeService.getDeviceFacilityHourBean(Integer.parseInt(sdf.format(new Date()))) ;
		homeService.updateInsDeviceFacilityHourBean(facilityHourBean);
		String taskId = context.getJobDetail().getKey().getName();
		dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=", taskId));

	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");

		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss") ;
		try {
			Date parse = format.parse("2017-01-03 0:22:3") ;
			String curtime = sdf.format(parse);
			System.out.println(Integer.parseInt(curtime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
