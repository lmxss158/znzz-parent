package com.znzz.app.web.commons.quartz.job;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentLiftAnnualInspectionService;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.instrument.modules.service.InsMessageInfoService;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.services.SysUnitService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.*;

/**
 * 电梯设备到期检定提醒定时任务
 */
@IocBean
public class InsLiftEquipmentAnnualInspectionJob implements Job{
    private static final Log log = Logs.get();
    @Inject
    private  InsEquipmentLiftAnnualInspectionService insEquipmentLiftAnnualInspectionService;
    @Inject
    private InsMessageInfoService messageService;
    @Inject
    private SysUnitService unitService ;
    @Inject
    protected Dao dao;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("--------电梯设备到期检定提醒定时任务开始--------");

        // 发消息
        sendToMessageInfo();

        log.info("~~~~~~~~~~~电梯设备到期检定提醒定时任务结束~~~~~~~~~~~~~~~~~");
        String taskId = context.getJobDetail().getKey().getName();
        dao.update(Sys_task .class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000)).add("exeResult", "执行成功"), Cnd.where("id", "=",taskId));

    }

    public void sendToMessageInfo() {
        //获取电梯设备中该到期检定提醒的设备数据集
        List<Ins_Equipment_Lift_Annual_Inspection> list =  insEquipmentLiftAnnualInspectionService.getEquipmentLiftNoticeData();

        //封装数据
        List<Ins_MessageInfo> messageInfos  = new ArrayList<Ins_MessageInfo>();
        if (list.size() > 0 && !list.isEmpty()){
            for (Ins_Equipment_Lift_Annual_Inspection vo : list) {
                Ins_MessageInfo insert = new Ins_MessageInfo();
                String context = vo.getLiftSite()+"统一编号为：" + vo.getLiftCode()+"的资产即将待检,请做好相关准备!";
                insert.setSendId("10000");
                insert.setMessageContext(context);
                insert.setReceiveId(vo.getChargePerson());
                insert.setSend_time(new Date());
                //insert.setStr1(vo.getLiftCode());
                messageInfos.add(insert);
            }
        }
        dao.fastInsert(messageInfos);
    }

}
