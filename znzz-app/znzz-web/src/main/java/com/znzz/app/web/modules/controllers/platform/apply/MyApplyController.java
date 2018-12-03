package com.znzz.app.web.modules.controllers.platform.apply;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.asset.moudules.services.apply.ApplyReordService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.app.web.modules.controllers.platform.apply.vo.ApplyAssetVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@IocBean
@At("/asset/apply/mine")
public class MyApplyController {

    private static final Log LOG = Logs.get();

    @Inject
    private ApplyAssetService applyService;

    @Inject
    private InsAssetsService assetsService;

    @Inject
    private ApplyReordService reordService;

    @Inject
    private SysUserService userService;

    @At("")
    @Ok("beetl:/platform/apply/mine/index.html")
    @RequiresPermissions("asset.apply.mine")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.mine")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns,
                       @Param("assetInfo") String assetInfo, @Param("applyState") String applyState, @Param("applyId") String applyId,
                       @Param("startDate") String startDate, @Param("endDate") String endDate) {
        Cnd cnd = Cnd.NEW();
        NutMap map = new NutMap();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 我的预约(超管看所有)
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            if (!"superadmin".equals(user.getLoginname())) {
                cnd.and("a.proposer", "=", StringUtil.getUid());
            }
            if (!Strings.isBlank(applyId)) {
                cnd.and("a.applyId", "like", "%" + applyId + "%");
            }
            if (!Strings.isBlank(assetInfo)) {
                cnd.and(Cnd.exps("a.assetName", "like", "%" + assetInfo + "%")
                        .or("a.deviceVersion", "like", "%" + assetInfo + "%")
                        .or("a.serialNumber", "like", "%" + assetInfo + "%"));
            }
            if (!Strings.isBlank(applyState)) {
                cnd.and("a.applyState", "=", applyState);
            }
            if (!Strings.isBlank(startDate) && Strings.isBlank(endDate)) {
                cnd.and("a.returnDate", ">=", sim.parse(startDate));
            }
            if (Strings.isBlank(startDate) && !Strings.isBlank(endDate)) {
                cnd.and(Cnd.exps("a.lendDate", "<=", sim.parse(endDate)).and("a.returnDate", ">=", sim.parse(endDate)));
            }
            if (!Strings.isBlank(startDate) && !Strings.isBlank(endDate)) {
                cnd.and(Cnd.exps(Cnd.exps("a.lendDate", "<=", sim.parse(startDate)).and("a.returnDate", ">=", sim.parse(startDate))
                        .or(Cnd.exps("a.lendDate", "<=", sim.parse(endDate)).and("a.returnDate", ">=", sim.parse(endDate)))));
            }
            //cnd.asc("a.deadline");
            map = applyService.queryMyApply(length, start, draw, order, columns, cnd);
        } catch (Exception e) {
            LOG.error("Query data error..", e);
        }
        return map;
    }

    @At("/applyDelay/?")
    @Ok("beetl:/platform/apply/mine/delay.html")
    @RequiresPermissions("asset.apply.mine.delay")
    public Object applyDelay(String applyId, HttpServletRequest req) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);
            vo.setProposer(user.getUsername());
            vo.setProposerId(user.getId());
            vo.setEntryNumber(user.getEntryNumber());
            Calendar cal = Calendar.getInstance();
            // 逾期未还处理
//            if (ApplyUtils.pureDate().getTime() > vo.getReturnDate().getTime()) {
//                // 原申请--已归还
//                applyService.update(Chain.make("applyState", 4), Cnd.where("applyId", "=", applyId));// 4:已归还
//                // 记录履历
//                Ins_apply_record record = new Ins_apply_record();
//                Ins_Asset_Apply apply = applyService.fetch(Cnd.where("applyId", "=", applyId));
//                ApplyUtils.doRecord(record, apply);
//                record.setOperator("10000");
//                record.setOperateState(4);
//                reordService.insert(record);// 入库
//            }
            cal.setTime(vo.getReturnDate());
            cal.add(Calendar.DATE, 1);
            vo.setLendDate(cal.getTime());
            // 生成延期编号
            vo.setApplyId(delayApplyId(vo.getApplyId()));

            // 该资产的预约申请记录
            List<Ins_Asset_Apply> list = applyService.query(Cnd.where("assetCode", "=", vo.getAssetCode())
                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate())//当日之后的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate()))//包含当日的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1))//逾期未还
                    ));
            // 占用日期
            StringBuilder myApps = new StringBuilder();
            StringBuilder otherApps = new StringBuilder();

            // 是否有禁用
            Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", vo.getAssetCode()));
            // 设置月历回显
            ApplyUtils.forDateShow(req, sdf, user, list, myApps, otherApps, vo.getAssetCode(), asset);
        } catch (Exception e) {
            LOG.error("Go to delay page wrong..", e);
        }
        return vo;
    }

    /**
     * 延期申请编号
     *
     * @param applyId
     * @return
     */
    private String delayApplyId(String applyId) {
        String delayId = "";
        int count = applyService.count(Cnd.where("applyId", "like", applyId + "-%"));
        // 延期的延期
        if (applyId.length() > 15) {
            String[] apps = applyId.split("-");
            Integer x = Integer.parseInt(apps[apps.length - 1]) + 1;
            delayId = applyId.substring(0, applyId.length() - 1) + x;
        } else {
            if (count > 0) {
                delayId = applyId + "-" + (count + 1);
            } else {
                delayId = applyId + "-1";
            }
        }

        return delayId;
    }

    /**
     * 申请延期
     *
     * @param apply
     * @return
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.mine.delay")
    public Object doDelay(@Param("..") Ins_Asset_Apply apply) {
        Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
        try {
            // 未领取：0，已领取：1，已延期：2，已取消：3，已归还：4
            apply.setLenderUnit(user.getUnitid());
            Ins_Asset_Apply orgApply = applyService.fetch(apply.getId());// 原申请
            Ins_apply_record orgRecord = new Ins_apply_record(); // 原记录
            ApplyUtils.doRecord(orgRecord, orgApply);

            // 延期申请履历
            Ins_apply_record record = new Ins_apply_record(); // 延期记录
            ApplyUtils.doRecord(record, apply);
            record.setAccessary(apply.getAccessary());
            record.setRemark(apply.getRemark());

            // 如果原申请已逾期,做逾期处理
            if (ApplyUtils.pureDate().getTime() > orgApply.getReturnDate().getTime()) {
                applyService.update(Chain.make("applyState", 4), Cnd.where("id", "=", apply.getId()));
                // 记录履历
                orgRecord.setOperator("10000");
                orgRecord.setOperateState(4);// 已归还
                reordService.insert(orgRecord);
                // 延期申请入库
                apply.setCreateTime(new Date());
                apply.setApplyState(1);// 已领取
                applyService.insert(apply);
                // 延期申请履历
                record.setOperateState(1);// 延期申请记录状态为：已领取
                reordService.insert(record);
            } else {
                // 未逾期处理
                applyService.update(Chain.make("applyState", 2), Cnd.where("id", "=", apply.getId()));
                // 添加原申请延期履历
                orgRecord.setOperateState(2);// 原申请记录履历已延期
                reordService.insert(orgRecord);
                // 延期申请入库
                apply.setCreateTime(new Date());
                apply.setApplyState(0);
                applyService.insert(apply);
                // 延期申请履历
                record.setOperateState(0);// 延期申请记录状态为：未领取
                reordService.insert(record);
            }

        } catch (Exception e) {
            LOG.error("Delay insert wrong..", e);
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    /**
     * 跳转撤销页面
     *
     * @param applyId
     * @param req
     * @return
     */
    @At("/applyCancel/?")
    @Ok("beetl:/platform/apply/mine/cancel.html")
    @RequiresPermissions("asset.apply.mine.cancel")
    public Object applyCancel(String applyId, HttpServletRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);

            vo.setEntryNumber(user.getEntryNumber());
            vo.setOperator(user.getUsername());
            vo.setOperatorId(user.getId());
            vo.setCancelPhone(user.getTelephone());

            // 该资产的预约申请记录
            List<Ins_Asset_Apply> list = applyService.query(Cnd.where("assetCode", "=", vo.getAssetCode())
                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate())//当日之后的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate()))//包含当日的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1))//逾期未还
                    ));


            // 占用日期
            StringBuilder myApps = new StringBuilder();
            StringBuilder otherApps = new StringBuilder();

            // 是否有禁用
            Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", vo.getAssetCode()));
            // 设置月历回显
            ApplyUtils.forDateShow(req, sdf, user, list, myApps, otherApps, vo.getAssetCode(), asset);
        } catch (Exception e) {
            LOG.error("Go to cancel page wrong..", e);
        }
        return vo;
    }

    /**
     * 撤销申请
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.mine.cancel")
    public Object doCancel(@Param("..") Ins_apply_record vo) throws Exception {
        // 撤销申请
        int count = applyService.update(Chain.make("applyState", 3)
                        .add("approver", StringUtil.getUid())
                        .add("number", vo.getNumber())
                        .add("remark", vo.getRemark()),
                Cnd.where("id", "=", vo.getId()));
        // 记录操作(撤销申请)到履历表
        vo.setOperator(StringUtil.getUid());
        vo.setOperateState(3);
        vo.setOperateTime(new Date());
        reordService.insert(vo);
        // 更改剩余使用时间
        applyService.update(Chain.make("deadline", null), Cnd.where("applyId", "=", vo.getApplyId()));
        // 撤销了延期申请的情况
        ApplyUtils.backToState(vo, applyService);

        if (count > 0) {
            return Result.success("system.success");
        } else {
            return Result.error("system.error");
        }
    }

    /**
     * 详情
     *
     * @param applyId
     * @param req
     * @return
     */
    @At("/detail/?")
    @Ok("beetl:/platform/apply/mine/detail.html")
    @RequiresPermissions("asset.apply.mine.detail")
    public Object detail(String applyId, HttpServletRequest req) {
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);
        } catch (Exception e) {
            LOG.error("Go to detail page wrong..", e);
        }
        return vo;
    }

    /**
     * 操作履历
     *
     * @return
     */
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.mine.detail")
    public Object detail(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                         @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns, @Param("applyId") String applyId) {
        return reordService.queryDetail(length, start, draw, order, columns, applyId);
    }
}
