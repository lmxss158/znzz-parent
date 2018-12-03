package com.znzz.app.web.commons.quartz.job;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Safety_Valve_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentSafetyValveAnnualInspectionService;
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
public class InsSafetyValveEquipmentAnnualInspectionJob implements Job {
    private static final org.nutz.log.Log log = Logs.get();
    @Inject
    private InsEquipmentSafetyValveAnnualInspectionService valveAnnualInspectionService;
    @Inject
    protected Dao dao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("--------安全阀设备到期检定提醒定时任务开始--------");
        //发消息
        sendToMessageInfo();

        log.info("~~~~~~~~~~~电梯设备到期检定提醒定时任务结束~~~~~~~~~~~~~~~~~");
        String taskId = context.getJobDetail().getKey().getName();
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=",taskId));


    }

    private void sendToMessageInfo() {
        //获取安全阀设备中该到期检定提醒的设备数据集
        List<Ins_Equipment_Safety_Valve_Annual_Inspection> equipmentValveNoticeData = valveAnnualInspectionService.getEquipmentValveNoticeData();

        //封装数据
        List<Ins_MessageInfo> messageInfos = new ArrayList<>();
        if (equipmentValveNoticeData.size() > 0 && !equipmentValveNoticeData.isEmpty()){
            for (Ins_Equipment_Safety_Valve_Annual_Inspection equipmentValveNoticeDatum : equipmentValveNoticeData) {
                Ins_MessageInfo ins_messageInfo = new Ins_MessageInfo();
                String context = equipmentValveNoticeDatum.getSafetyValveSite()+"统一编号为：" + equipmentValveNoticeDatum.getSafetyValveCode()+"的资产即将待检,请做好相关准备!";
                ins_messageInfo.setSendId("10000");
                ins_messageInfo.setMessageContext(context);
                ins_messageInfo.setReceiveId(equipmentValveNoticeDatum.getChargePerson());
                ins_messageInfo.setSend_time(new Date());
               // ins_messageInfo.setStr1(equipmentValveNoticeDatum.getSafetyValveCode());
                messageInfos.add(ins_messageInfo);
            }
        }
             dao.fastInsert(messageInfos);
        }
}
