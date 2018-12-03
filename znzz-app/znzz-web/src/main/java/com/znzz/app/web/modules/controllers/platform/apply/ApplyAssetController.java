package com.znzz.app.web.modules.controllers.platform.apply;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.asset.moudules.services.apply.ApplyReordService;
import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.app.web.commons.util.DateUtils;
import com.znzz.app.web.modules.controllers.platform.apply.vo.ApplyAssetVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@IocBean
@At("/asset/apply/sign")
public class ApplyAssetController {

    private static final Log LOG = Logs.get();

    @Inject
    private ApplyAssetService applyService;

    @Inject
    private InsAssetsService assetsService;

    @Inject
    private ApplyReordService reordService;

    @Inject
    private SysUserService userService;

    @Inject
    private Dao dao;

    @At("")
    @Ok("beetl:/platform/apply/index.html")
    @RequiresPermissions("asset.apply.sign")
    public void index(HttpServletRequest request) {
        // 查询当前登陆用户是否有逾期未还的资产
        int count = applyService.count(Cnd.where("proposer", "=", StringUtil.getUid()).and("deadline", "<", 0));
        if (count > 0) {
            request.setAttribute("delay", 1);
        } else {
            request.setAttribute("delay", 0);
        }
    }

    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.sign")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns,
                       @Param("assetInfo") String assetInfo, @Param("assetState") String assetState,
                       @Param("chargeMan") String chargeMan) {

        Cnd cnd = Cnd.NEW();
        NutMap map = new NutMap();
        try {
            // 固定条件
            cnd.and(Cnd.exps("a.manageStatus", "=", 0).or("a.manageStatus", "is", null))
                    .and(Cnd.exps("a.repairState", "<>", 0).or("a.repairState", "is", null))
                    .and(Cnd.exps("a.scrapState", "<>", 1).or("a.scrapState", "is", null))
                    .and(Cnd.exps("a.isLend", "<>", 0).or("a.isLend", "is", null));

            if (!Strings.isBlank(assetInfo)) {
                cnd.and(Cnd.exps("a.assetCode", "like", "%" + assetInfo + "%").or("b.assetName", "like", "%" + assetInfo + "%")
                        .or("a.deviceVersion", "like", "%" + assetInfo + "%").or("a.serialNumber", "like", "%" + assetInfo + "%"));
            }
//            if (!Strings.isBlank(assetState)) {
//                cnd.and("assetState", "=", assetState);
//            }
            if (!Strings.isBlank(chargeMan)) {
                cnd.and("d.approver", "=", chargeMan);
            }
            cnd.groupBy("a.assetCode");
            map = applyService.queryApplyInfos(length, start, draw, order, columns, cnd, assetState);
        } catch (Exception e) {
            LOG.error("Query data error..", e);
        }
        return map;
    }

    /**
     * 跳至新建申请页面
     *
     * @param id
     * @param req
     * @return
     */
    @At("/addApply/?")
    @Ok("beetl:/platform/apply/add.html")
    @RequiresPermissions("asset.apply.sign.add")
    public Object add(String id, HttpServletRequest req) {
        String appTitle = "ZCYY";
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(id) ? null : applyService.queryAddApplys(id);
            vo.setProposer(user.getUsername());
            vo.setProposerId(user.getId());
            vo.setEntryNumber(user.getEntryNumber());
            vo.setPhoneNumber(user.getTelephone());
            // 生成编号
            String applyPrefix = appTitle + sim.format(new Date());
            String appNo = createApplyNo(applyPrefix);
            vo.setApplyId(applyPrefix + appNo);

            // 该资产的预约申请记录
            List<Ins_Asset_Apply> list = applyService.query(Cnd.where("assetCode", "=", id)
                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate())//当日之后的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate()))//包含当日的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1))//逾期未还
                    ));
            // 占用日期
            StringBuilder myApps = new StringBuilder();
            StringBuilder otherApps = new StringBuilder();
            // 是否有禁用
            Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", id));
            ApplyUtils.forDateShow(req, sdf, user, list, myApps, otherApps, id, asset);
        } catch (Exception e) {
            LOG.error("Add apply wrong..", e);
        }
        return vo;
    }

    /**
     * 生成申请编号
     *
     * @param applyPrefix
     * @return
     */
    private String createApplyNo(String applyPrefix) {
        String result = "";
        List<Ins_Asset_Apply> applys = applyService.query(Cnd.where("applyId", "like", applyPrefix + "%")
                .and("delFlag", "<>", 1).desc("createTime"));
        DecimalFormat df = new DecimalFormat("000");
        if (applys != null && !applys.isEmpty()) {
            String str = applys.get(0).getApplyId().substring(12, 15);
            result = df.format(Integer.parseInt(str) + 1);
        } else {
            result = df.format(1);
        }
        return result;
    }

    /**
     * 新建申请入库操作
     *
     * @param apply
     * @return
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.sign.add")
    public Object addDo(@Param("..") Ins_Asset_Apply apply) {
        Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
        try {
            apply.setLenderUnit(user.getUnitid());
            apply.setApplyState(0);// 未领取：0，已领取：1，已延期：2，已取消：3，已归还：4
            apply.setCreateTime(new Date());// 生成时间
            applyService.insert(apply);
            // 记录履历
            Ins_apply_record record = new Ins_apply_record();
            ApplyUtils.doRecord(record, apply);
            record.setOperateState(0);//未领取
            record.setAccessary(apply.getAccessary());
            record.setRemark(apply.getRemark());
            reordService.insert(record);
        } catch (Exception e) {
            LOG.error("Insert wrong..", e);
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    /**
     * 悬浮提示
     *
     * @param assetCode
     * @param date
     * @return
     * @throws Exception
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.sign.add")
    public Object getCurrentInfo(@Param("assetCode") String assetCode, @Param("date") String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        Cnd cnd = Cnd.NEW();
        if (date != null) {
//            cnd.and("lendDate", "<=", sdf.parse(date))
//                    .and("returnDate", ">=", sdf.parse(date))
//                    .and("assetCode", "=", assetCode)
//                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate()).or("lendDate", "<=", ApplyUtils.pureDate()))
//                    .and("returnDate", ">=", ApplyUtils.pureDate())
//                    .and("applyState", "<>", 3); //已取消
            //.and("applyState", "<>", 4); //已归还
            cnd.and("assetCode", "=", assetCode)
                    .and(
                            Cnd.exps(
                                    Cnd.exps(
                                            //所选日期在申请的借用归还日期内
                                            Cnd.exps("lendDate", "<=", sdf.parse(date)).and("returnDate", ">=", sdf.parse(date)).and(
                                                    Cnd.exps(
                                                            Cnd.exps(
                                                                    //未领取，申请包含当前日期(不考虑禁止预约情况，因为禁止预约时对于未领取的预约，申请状态会变为已取消)
                                                                    Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate())
                                                            )
                                                                    //未领取，借用日期在当前日期以后
                                                                    .or("lendDate", ">", ApplyUtils.pureDate())
                                                    ).and("applyState", "=", 0))
                                                    //已延期，借用日期小于等于所选日期，归还日期大于所选日期(没有等于，因为延期申请到期的时候会自动变为已归还)
                                                    .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">", ApplyUtils.pureDate()).and("applyState", "=", 2))
                                    )
                            )
                                    .or(
                                            Cnd.exps(
                                                    //已领取，借用日期小于等于所选日期和当前日期，并且归还日期要大于大于等于所选日期，考虑到逾期未还的情况，归还日期可以小于所选日期但所选日期必须大于等于当前日期
                                                    Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1)
                                            )
                                                    .and("lendDate", "<=", sdf.parse(date))
                                                    .and(Cnd.exps("returnDate", ">=", sdf.parse(date))
                                                            .or(Cnd.exps("returnDate", "<", sdf.parse(date)).and("'" + DateUtils.getDateStrFromDate(sdf.parse(date)) + "'", "<=", ApplyUtils.pureDate())
                                                            )
                                                    )
                                    )
                    );


            Ins_Asset_Apply vo = applyService.fetch(cnd);
            if (vo == null) {
                return Result.error("system.error");
            } else {
                Sys_user uu = userService.fetch(vo.getProposer());
                if (uu != null) {
                    vo.setProposer(uu.getUsername());
                }
                return Result.success("system.success", vo);
            }
        } else {
            return Result.error("system.error");
        }
    }

    /**
     * 校验申请日期是否重叠
     *
     * @param lendDate
     * @param returnDate
     * @param assetCode
     * @return
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.sign.add")
    public Object checkOverlap(@Param("lendDate") Date lendDate, @Param("returnDate") Date returnDate, @Param("assetCode") String assetCode) {
        if (lendDate != null && returnDate != null) {
            int count = applyService.countConflict(assetCode, lendDate, returnDate);
            if (count > 0) {
                return Result.error("system.error");
            } else {
                return Result.success("system.success");
            }
        }
        return Result.error("system.error");
    }

    /**
     * 跳转管理页面
     *
     * @param id
     * @param req
     * @return
     */
    @At("/assetManage/?")
    @Ok("beetl:/platform/apply/manage.html")
    @RequiresPermissions("asset.apply.sign.manage")
    public Object manage(@Param("id") String id, HttpServletRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(id) ? null : applyService.queryAddApplys(id);
            vo.setProposer(user.getUsername());
            vo.setProposerId(user.getId());
            vo.setEntryNumber(user.getEntryNumber());
            vo.setPhoneNumber(user.getTelephone());

            // 该资产的预约申请记录
            List<Ins_Asset_Apply> list = applyService.query(Cnd.where("assetCode", "=", id)
                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate())//当日之后的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate()))//包含当日的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1))//逾期未还
                    ));

            // 占用日期
            StringBuilder myApps = new StringBuilder();
            StringBuilder otherApps = new StringBuilder();

            // 是否有禁用
            Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", id));
            ApplyUtils.forDateShow(req, sdf, user, list, myApps, otherApps, id, asset);
        } catch (Exception e) {
            LOG.error("Manage apply wrong..", e);
        }
        return vo;
    }

    /**
     * 禁止预约
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.apply.sign.manage")
    public Object forbidApply(@Param("..") Ins_apply_record vo) throws Exception {
        // 设置禁用日期
        int count = assetsService.update(Chain.make("forbidDate", vo.getForbidDate()), Cnd.where("assetCode", "=", vo.getAssetCode()));
        // 记录操作(禁止预约，取消预约)到履历表
        vo.setOperator(StringUtil.getUid());
        vo.setOperateState(-5);//-5:禁止预约 -6：取消禁止
        vo.setOperateTime(new Date());
        reordService.insert(vo);
        // 如果禁用时间往后有预约记录，全部取消(改状态，记履历)
        ApplyUtils au = new ApplyUtils();
        au.cancelAccordForbid(vo.getAssetCode(), vo.getForbidDate(), dao, "禁止预约");
        // 发送通知到被取消预约的账户
        List<Ins_Asset_Apply> list = dao.query(Ins_Asset_Apply.class, Cnd.where("assetCode", "=", vo.getAssetCode()).and("lendDate", ">=", vo.getForbidDate()));
        if (list != null && !list.isEmpty()) {
            for (Ins_Asset_Apply apply : list) {
                // 发送领取通知给申请人
                Ins_MessageInfo info = new Ins_MessageInfo();
                info.setReceiveId(apply.getProposer());
                info.setSend_time(new Date());
                info.setSendId("10000");
                info.setDelFlag(null);
                info.setMessageContext("由于资产：" + apply.getAssetCode() + "被禁止预约,因此您的申请：" + apply.getApplyId() + "已被取消！");
                dao.fastInsert(info);
            }
        }

        if (count > 0) {
            return Result.success("system.success");
        } else {
            return Result.error("system.error");
        }
    }

    /**
     * 取消禁止
     *
     * @param assetCode
     * @return
     * @throws Exception
     */
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.sign.manage")
    public Object cancelForbid(@Param("assetCode") String assetCode, @Param("remark") String remark) throws Exception {
        // 取消禁止
        int count = assetsService.update(Chain.make("forbidDate", null), Cnd.where("assetCode", "=", assetCode));
        // 记录操作(禁止预约，取消预约)到履历表
        Ins_apply_record vo = new Ins_apply_record();
        vo.setOperator(StringUtil.getUid());
        vo.setOperateState(-6);//-5:禁止预约 -6：取消禁止
        vo.setRemark(remark);
        vo.setOperateTime(new Date());
        vo.setAssetCode(assetCode);
        reordService.insert(vo);
        if (count > 0) {
            return Result.success("system.success");
        } else {
            return Result.error("system.error");
        }
    }

    /**
     * 详情
     *
     * @param id
     * @param req
     * @return
     */
    @At("/detail/?")
    @Ok("beetl:/platform/apply/detail.html")
    @RequiresPermissions("asset.apply.sign.detail")
    public Object detail(String id, HttpServletRequest req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ApplyAssetVo vo = new ApplyAssetVo();
        try {
            Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
            vo = Strings.isBlank(id) ? null : applyService.queryAddApplys(id);

            // 该资产的预约申请记录
            List<Ins_Asset_Apply> list = applyService.query(Cnd.where("assetCode", "=", id)
                    .and(Cnd.exps("lendDate", ">=", ApplyUtils.pureDate())//当日之后的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("returnDate", ">=", ApplyUtils.pureDate()))//包含当日的申请
                            .or(Cnd.exps("lendDate", "<=", ApplyUtils.pureDate()).and("applyState", "=", 1))//逾期未还
                    ));
            // 占用日期
            StringBuilder myApps = new StringBuilder();
            StringBuilder otherApps = new StringBuilder();
            // 是否有禁用
            Ins_Assets asset = assetsService.fetch(Cnd.where("assetCode", "=", id));
            ApplyUtils.forDateShow(req, sdf, user, list, myApps, otherApps, id, asset);
        } catch (Exception e) {
            LOG.error("Go to detail page wrong..", e);
        }
        return vo;
    }

    /**
     * 逾期未还列表(我的)
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.sign")
    public Object delay(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                        @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        cnd.and("proposer", "=", StringUtil.getUid()).and("deadline", "<", 0).asc("deadline");
        return applyService.data(length, start, draw, order, columns, cnd, null);
    }
}
