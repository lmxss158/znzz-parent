package com.znzz.app.web.modules.controllers.platform.apply;


import com.znzz.app.asset.moudules.models.Ins_Assets;
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
import java.util.Date;
import java.util.List;

@IocBean
@At("/asset/apply/approve")
public class ApprovalController {

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
    @Ok("beetl:/platform/apply/approval/index.html")
    @RequiresPermissions("asset.apply.approve")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.approve")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns,
                       @Param("assetInfo") String assetInfo, @Param("applyState") String applyState, @Param("applyId") String applyId,
                       @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("chargeMan") String chargeMan) {
        Cnd cnd = Cnd.NEW();
        NutMap map = new NutMap();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 预约审批(除掉"已取消","已归还"外所有)
            cnd.and("a.applyState", "!=", 3).and("a.applyState", "!=", 4);
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
            if (!Strings.isBlank(chargeMan)) {
                cnd.and("a.proposer", "=", chargeMan);
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
            //cnd.asc("a.applyState");
            map = applyService.queryApprovalInfos(length, start, draw, order, columns, cnd);
        } catch (Exception e) {
            LOG.error("Query data error..", e);
        }
        return map;
    }

    /**
     * 跳转确认领取页面
     *
     * @param applyId
     * @param req
     * @return
     */
    @At("/acquire/?")
    @Ok("beetl:/platform/apply/approval/acquire.html")
    @RequiresPermissions("asset.apply.approve.acquire")
    public Object applyAcquire(String applyId, HttpServletRequest req) {
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);
            vo.setEntryNumber(user.getEntryNumber());
            vo.setOperator(user.getUsername());
            vo.setOperatorId(user.getId());
            vo.setCancelPhone(user.getTelephone());
        } catch (Exception e) {
            LOG.error("Go to acquire page wrong..", e);
        }
        return vo;
    }

    /**
     * 确认领取
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.approve.acquire")
    public Object doAcquire(@Param("..") Ins_apply_record vo) throws Exception {
        // 先判断当前资产是否被其他人领取占用
        Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", vo.getAssetCode()));
        Integer x = 1;
        if (asset != null && x != asset.getIsOrder()) {
            // 确认领取
            int count = applyService.update(Chain.make("applyState", 1)
                            .add("approver", StringUtil.getUid()).add("lendDate", ApplyUtils.pureDate()),
                    Cnd.where("id", "=", vo.getId()));
            // 记录当前台账状态-已领取：1，已归还：2（isOrder）
            assetsService.update(Chain.make("isOrder", 1), Cnd.where("assetCode", "=", vo.getAssetCode()));
            // 更改剩余使用时间
            applyService.update(Chain.make("deadline", ApplyUtils.differentDays(new Date(), vo.getReturnDate())), Cnd.where("applyId", "=", vo.getApplyId()));
            // 记录操作(确认领取)到履历表
            vo.setApprover(StringUtil.getUid());//审批人
            vo.setOperator(StringUtil.getUid());
            vo.setOperateState(1);
            vo.setLendDate(ApplyUtils.pureDate());//履历中的借用时间也修改
            vo.setOperateTime(new Date());
            reordService.insert(vo);
            if (count > 0) {
                return Result.success("system.success");
            } else {
                return Result.error("system.error");
            }
        } else {
            return Result.error("当前资产已被其他人领取,无法进行此操作！");
        }
    }

    /**
     * 确认归还跳转
     *
     * @param applyId
     * @param req
     * @return
     */
    @At("/return/?")
    @Ok("beetl:/platform/apply/approval/return.html")
    @RequiresPermissions("asset.apply.approve.return")
    public Object applyReturn(String applyId, HttpServletRequest req) {
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);

            vo.setEntryNumber(user.getEntryNumber());
            vo.setOperator(user.getUsername());
            vo.setOperatorId(user.getId());
            vo.setCancelPhone(user.getTelephone());
            // 领取信息
            Ins_apply_record record = reordService.fetch(Cnd.where("applyId", "=", applyId).and("operateState", "=", 1));
            if (record != null) {
                Sys_user u = userService.fetch(record.getOperator());
                if (u != null) {
                    vo.setGetMan(u.getUsername());
                }
                vo.setGetManId(record.getOperator());
                vo.setGetNumber(record.getNumber());
                vo.setGetAccess(record.getAccessary());
                vo.setGetRemark(record.getRemark());
            }
        } catch (Exception e) {
            LOG.error("Go to return page wrong..", e);
        }
        return vo;
    }

    /**
     * 确认归还
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.approve.return")
    public Object doReturn(@Param("..") Ins_apply_record vo) throws Exception {
        // 确认归还(修改状态bing修改这条申请的归还日期)
        int count = applyService.update(Chain.make("applyState", 4)
                        .add("returnDate", ApplyUtils.pureDate())
                        .add("approver", StringUtil.getUid()).add("number", vo.getNumber()),
                Cnd.where("id", "=", vo.getId()));

        vo.setOperator(StringUtil.getUid());
        vo.setOperateState(4);
        vo.setReturnDate(ApplyUtils.pureDate());//归还时间也修改
        vo.setOperateTime(new Date());
        // 记录操作(撤销申请)到履历表
        reordService.insert(vo);

        // 确认延期记录归还的同时还要修改原记录*(不需要)
//        if (vo.getApplyId().length() > 15) {
//            String originApplyId = vo.getApplyId().substring(0, 15);
//            applyService.update(Chain.make("applyState", 4).add("returnDate", new Date())
//                    .add("approver", StringUtil.getUid()), Cnd.where("applyId", "=", originApplyId));
//            // 履历
//            vo.setApplyId(originApplyId);
//            reordService.insert(vo);
//        }

        // 记录当前台账状态-已领取：1，已归还：2（isOrder）
        assetsService.update(Chain.make("isOrder", 2), Cnd.where("assetCode", "=", vo.getAssetCode()));
        // 更改剩余使用时间
        applyService.update(Chain.make("deadline", null), Cnd.where("applyId", "=", vo.getApplyId()));
        if (count > 0) {
            return Result.success("system.success");
        } else {
            return Result.error("system.error");
        }
    }

    @At("/applyCancel/?")
    @Ok("beetl:/platform/apply/approval/cancel.html")
    @RequiresPermissions("asset.apply.approve.cancel")
    public Object applyCancel(String applyId, HttpServletRequest req) {
        ApplyAssetVo vo = new ApplyAssetVo();
        Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
        try {
            vo = Strings.isBlank(applyId) ? null : applyService.queryMyDelay(applyId);
            vo.setEntryNumber(user.getEntryNumber());
            vo.setOperator(user.getUsername());
            vo.setOperatorId(user.getId());
            vo.setCancelPhone(user.getTelephone());

        } catch (Exception e) {
            LOG.error("Go to cancel page wrong..", e);
        }
        return vo;
    }

    /**
     * 取消申请
     *
     * @param vo
     * @return
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.approve.cancel")
    public Object doCancel(@Param("..") Ins_apply_record vo) {
        int cancel = 3;
        try {
            // 取消申请
            int sum = applyService.update(Chain.make("applyState", cancel)
                            .add("approver", StringUtil.getUid())
                            .add("number", vo.getNumber())
                            .add("remark", vo.getRemark()),
                    Cnd.where("id", "=", vo.getId()));
            // 记录操作(取消申请)到履历表
            vo.setOperator(StringUtil.getUid());
            vo.setOperateState(cancel);
            vo.setOperateTime(new Date());
            reordService.insert(vo);
            // 更改剩余使用时间
            applyService.update(Chain.make("deadline", null), Cnd.where("applyId", "=", vo.getApplyId()));

            // 撤销了延期申请的情况
            ApplyUtils.backToState(vo, applyService);
            if (sum > 0) {
                return Result.success("system.success");
            } else {
                return Result.error("system.error");
            }
        } catch (Exception e) {
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
    @Ok("beetl:/platform/apply/approval/detail.html")
    @RequiresPermissions("asset.apply.approve.detail")
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
    @RequiresPermissions("asset.apply.approve.detail")
    public Object detail(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                         @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns, @Param("applyId") String applyId) {
        return reordService.queryDetail(length, start, draw, order, columns, applyId);
    }
}
