package com.znzz.app.web.commons.quartz.job.apply;

import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.app.asset.moudules.services.apply.ApplyReordService;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.instrument.modules.service.InsMessageInfoService;
import com.znzz.app.sys.modules.models.Sys_task;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.util.ApplyUtils;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 更新剩余使用时间(逾期未还状态)
 */
@IocBean
public class ApplyUpdateJob implements Job {

    private static final Log LOG = Logs.get();

    @Inject
    private Dao dao;

    @Inject
    private SysUserService userService;

    @Inject
    private ApplyReordService recordService;

    @Inject
    private InsMessageInfoService messageService;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String taskId = context.getJobDetail().getKey().getName();
        excuteOfUpdate();
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000))
                .add("exeResult", "success"), Cnd.where("id", "=", taskId));
    }

    /**
     * 操作(更新剩余使用时间)
     */
    private void excuteOfUpdate() {
        try {
            // 取所有申请
            List<Ins_Asset_Apply> list = dao.query(Ins_Asset_Apply.class, Cnd.orderBy().asc("createTime"));
            if (list != null && !list.isEmpty()) {
                for (Ins_Asset_Apply apply : list) {
                    Integer left = 0;
                    // 已归还，已取消的没有剩余使用时间（Damn add 0）
                    if (apply.getApplyState() == 3 || apply.getApplyState() == 4 || apply.getApplyState() == 0) {
                        left = null;
                    } else if (apply.getApplyState() == 2) {
                        // 如果延期了
                        List<Ins_Asset_Apply> delays = dao.query(Ins_Asset_Apply.class, Cnd.where("applyId", "like", apply.getApplyId() + "-%").desc("returnDate"));
                        if (delays != null && !delays.isEmpty()) {
                            String delayAppId = delays.get(0).getApplyId();
                            //Date delayReturnDate = delays.get(0).getReturnDate();
                            Date delayLendDate = delays.get(0).getLendDate();
                            left = ApplyUtils.differentDays(new Date(), apply.getReturnDate());
                            // 延期的申请到了延期申请开始时间
                            updateOfState(delayLendDate, delayAppId, apply.getApplyId());
                        }

                    } else {
                        // 未延期
                        left = ApplyUtils.differentDays(new Date(), apply.getReturnDate());
                        // 领取通知
                        if (apply.getApplyState() == 0) {
                            noticeToGet(apply);
                            // 催促领取
                            urgeNoticeToGet(apply);
                        }
                    }
                    // 更新剩余使用时间
                    dao.update("ins_asset_apply", Chain.make("deadline", left), Cnd.where("applyId", "=", apply.getApplyId()));
                }
            }
        } catch (Exception e) {
            LOG.error("Wrong when update of deadline..", e);
        }

    }

    /**
     * 修改延期申请记录的状态为：已领取, 审批人为：系统
     * 同时补全原有申请归还记录,且将延期申请加上领取记录
     *
     * @param delayLendDate
     * @param delayAppId
     */
    private void updateOfState(Date delayLendDate, String delayAppId, String applyId) {
        int count = ApplyUtils.differentDays(delayLendDate, new Date());
        if (count == 0) {
            // 修改延期记录状态为已领取,审批人为：系统
            dao.update("ins_asset_apply", Chain.make("applyState", 1)
                            .add("approver", "10000")
                            .add("lendDate", ApplyUtils.pureDate()),//领取时间
                    Cnd.where("applyId", "=", delayAppId));

            // 修改原申请状态
            dao.update("ins_asset_apply", Chain.make("applyState", 4)
                            .add("approver", "10000")
                            .add("returnDate", ApplyUtils.pureDate()) //归还时间
                            .add("deadline", null),//已归还的剩余时间为空
                    Cnd.where("applyId", "=", applyId));

            // 补全记录(原申请)
            List<Ins_apply_record> list = dao.query(Ins_apply_record.class, Cnd.where("applyId", "=", applyId).desc("operateTime"));
            if (list != null && !list.isEmpty()) {
                Ins_apply_record orgApply = list.get(0);
                orgApply.setOperateTime(new Date());
                orgApply.setOperateState(4);//已归还
                orgApply.setReturnDate(ApplyUtils.pureDate()); // 归还时间
                orgApply.setOperator("10000");
                orgApply.setEntryNumber("");//系统无工号
                orgApply.setRemark("延期生效");
                recordService.insert(orgApply);
            }

            // 补全记录(延期申请)
            List<Ins_apply_record> delayList = dao.query(Ins_apply_record.class, Cnd.where("applyId", "=", delayAppId).desc("operateTime"));
            if (delayList != null && !delayList.isEmpty()) {
                Ins_apply_record delayApply = delayList.get(0);
                delayApply.setOperator("10000");
                delayApply.setOperateState(1);//已领取
                delayApply.setOperateTime(new Date());
                delayApply.setLendDate(ApplyUtils.pureDate()); //领取时间
                recordService.insert(delayApply);
            }
        }
    }

    /**
     * 领取通知
     *
     * @param apply
     */
    private void noticeToGet(Ins_Asset_Apply apply) {
        // 判断是否到了预约借出日期当天
        int count = ApplyUtils.differentDays(apply.getLendDate(), new Date());
        if (count == 0) {
            // 发送领取通知给申请人
            Ins_MessageInfo info = new Ins_MessageInfo();
            info.setSendId("10000");
            info.setReceiveId(apply.getProposer());// 接受者为当前申请人
            info.setSend_time(new Date());
            info.setDelFlag(null);
            info.setMessageContext("您预约的资产:" + apply.getAssetCode() + "借出时间为今天,请及时办理领取！");
            //messageService.insert(info);
            dao.fastInsert(info);
        }
    }

    /**
     * 催促领取
     *
     * @param apply
     */
    private void urgeNoticeToGet(Ins_Asset_Apply apply) {
        int count = ApplyUtils.differentDays(apply.getLendDate(), new Date());
        if (count == 1) {
            // 通知申请人和仪器室全员
            List<Ins_MessageInfo> infos = new ArrayList<Ins_MessageInfo>();

            // 申请人
            //messageService.insert(defineInfo(apply, apply.getProposer()));
            dao.fastInsert(defineInfo(apply, apply.getProposer()));

            // 仪器室全员(100039)
            List<Sys_user> users = userService.query(Cnd.where("unitid", "=", "100039").and("userStatus", "=", "2"));
            for (Sys_user user : users) {
                infos.add(defineInfo(apply, user.getId()));
            }
            // 发通知
            dao.fastInsert(infos);
        }
    }

    /**
     * 定义消息内容
     *
     * @param apply
     * @param receiveId
     * @return
     */
    private Ins_MessageInfo defineInfo(Ins_Asset_Apply apply, String receiveId) {
        Ins_MessageInfo info = new Ins_MessageInfo();
        info.setSendId("10000");
        info.setDelFlag(null);
        info.setSend_time(new Date());
        info.setMessageContext("资产:" + apply.getAssetCode() + "已经过了借出时间,请尽快办理领取！");
        info.setReceiveId(receiveId);
        return info;
    }

}
