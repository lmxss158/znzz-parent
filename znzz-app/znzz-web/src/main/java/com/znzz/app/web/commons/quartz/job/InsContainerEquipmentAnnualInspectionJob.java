package com.znzz.app.web.commons.quartz.job;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Container_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentContainerAnnualInspectionService;
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
public class InsContainerEquipmentAnnualInspectionJob implements Job {
    private static final org.nutz.log.Log log = Logs.get();
    @Inject
    private InsEquipmentContainerAnnualInspectionService containerAnnualInspectionService;
    @Inject
    protected Dao dao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("--------容器年检设备到期检定提醒定时任务开始--------");
        //发消息
        sendToMessageInfo();

        log.info("~~~~~~~~~~~容器年检设备到期检定提醒定时任务结束~~~~~~~~~~~~~~~~~");
        String taskId = context.getJobDetail().getKey().getName();
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=",taskId));

    }

    private void sendToMessageInfo() {
        //获取容器年检设备中该到期提醒的数据集
        List<Ins_Equipment_Container_Annual_Inspection> containerAnnualInspectionList = containerAnnualInspectionService.getContainerAnnualInspectionNoticeData();

        //封装数据
        List<Ins_MessageInfo> messageInfoList = new ArrayList<>();
        if (containerAnnualInspectionList.size() >0 && !containerAnnualInspectionList.isEmpty()){
            for (Ins_Equipment_Container_Annual_Inspection containerAnnualInspection : containerAnnualInspectionList) {
                Ins_MessageInfo insMessageInfo = new Ins_MessageInfo();
                String context = containerAnnualInspection.getContainerSite()+"统一编号为：" + containerAnnualInspection.getContainerCode()+"的资产即将待检,请做好相关准备!";
                insMessageInfo.setSendId("10000");
                insMessageInfo.setMessageContext(context);
                insMessageInfo.setReceiveId(containerAnnualInspection.getChargePerson());
                insMessageInfo.setSend_time(new Date());
                //insMessageInfo.setStr1(containerAnnualInspection.getContainerCode());
                messageInfoList.add(insMessageInfo);
            }
        }
            dao.fastInsert(messageInfoList);
    }
}
