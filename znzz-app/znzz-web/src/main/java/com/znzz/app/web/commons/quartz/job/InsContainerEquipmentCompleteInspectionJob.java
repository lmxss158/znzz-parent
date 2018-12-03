package com.znzz.app.web.commons.quartz.job;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Container_Complete_Inspection;
import com.znzz.app.asset.safety.InsEquipmentContainerCompleteInspectionService;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.sys.modules.models.Sys_task;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IocBean
public class InsContainerEquipmentCompleteInspectionJob implements Job {
    private static final org.nutz.log.Log log = Logs.get();
    @Inject
    private InsEquipmentContainerCompleteInspectionService containerCompleteInspectionService;
    @Inject
    protected Dao dao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("--------容器全检设备到期检定提醒定时任务开始--------");
        //发消息
        sendToMessageInfo();

        log.info("~~~~~~~~~~~容器全检设备到期检定提醒定时任务结束~~~~~~~~~~~~~~~~~");
        String taskId = context.getJobDetail().getKey().getName();
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=",taskId));

    }

    private void sendToMessageInfo() {
        //获取容器全检设备中该到期提醒的数据集
        List<Ins_Equipment_Container_Complete_Inspection> containerCompleteInspectionList = containerCompleteInspectionService.getContainerCompleteInspectionNoticeData();

        //封装数据
        List<Ins_MessageInfo> messageInfoList = new ArrayList<>();
       if (containerCompleteInspectionList.size() > 0 && !containerCompleteInspectionList.isEmpty()){
           for (Ins_Equipment_Container_Complete_Inspection containerCompleteInspection : containerCompleteInspectionList) {
                Ins_MessageInfo insMessageInfo = new Ins_MessageInfo();
                String context = containerCompleteInspection.getContainerSite()+"统一编号为："+ containerCompleteInspection.getContainerCode()+ "的资产即将待检,请做好相关准备!";
                insMessageInfo.setSendId("10000");
                insMessageInfo.setMessageContext(context);
                insMessageInfo.setReceiveId(containerCompleteInspection.getChargePerson());
                insMessageInfo.setSend_time(new Date());
               // insMessageInfo.setStr1(containerCompleteInspection.getContainerCode());
                messageInfoList.add(insMessageInfo);
           }
       }
       dao.fastInsert(messageInfoList);
    }
}
