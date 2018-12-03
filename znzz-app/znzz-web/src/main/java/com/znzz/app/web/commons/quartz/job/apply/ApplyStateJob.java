package com.znzz.app.web.commons.quartz.job.apply;

import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.sys.modules.models.Sys_task;
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

import java.util.Date;
import java.util.List;

/**
 * 定时任务，每分钟执行，用于及时更新剩余使用时间
 */
@IocBean
public class ApplyStateJob implements Job {

    private static final Log LOG = Logs.get();

    @Inject
    private Dao dao;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String taskId = context.getJobDetail().getKey().getName();
        stateHandler();
        dao.update(Sys_task.class, Chain.make("exeAt", (int) (System.currentTimeMillis() / 1000))
                .add("exeResult", "ok"), Cnd.where("id", "=", taskId));
    }

    private void stateHandler() {
        try {
            List<Ins_Asset_Apply> list = dao.query(Ins_Asset_Apply.class, null);
            if (list != null && !list.isEmpty()) {
                for (Ins_Asset_Apply apply : list) {
                    Integer left = 0;
                    if (apply.getApplyState() == 0 || apply.getApplyState() == 3 || apply.getApplyState() == 4) {
                        left = null;
                    } else {
                        left = ApplyUtils.differentDays(new Date(), apply.getReturnDate());
                    }
                    // 更新剩余使用时间
                    dao.update("ins_asset_apply", Chain.make("deadline", left), Cnd.where("applyId", "=", apply.getApplyId()));
                }
            }
        } catch (Exception e) {
            LOG.error("Wrong when update of deadline..", e);
        }
    }
}
