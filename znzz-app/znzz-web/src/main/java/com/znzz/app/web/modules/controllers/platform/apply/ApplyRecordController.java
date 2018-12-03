package com.znzz.app.web.modules.controllers.platform.apply;

import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.asset.moudules.services.apply.ApplyReordService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.modules.controllers.platform.apply.vo.ApplyAssetVo;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.text.SimpleDateFormat;
import java.util.List;

@IocBean
@At("asset/apply/record")
public class ApplyRecordController {

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
    @Ok("beetl:/platform/apply/record/index.html")
    @RequiresPermissions("asset.apply.record")
    public void index() {

    }

    @At
    @Ok("json:full")
    @RequiresPermissions("asset.apply.record")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns,
                       @Param("assetInfo") String assetInfo, @Param("applyState") String applyState, @Param("applyId") String applyId,
                       @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("chargeMan") String chargeMan) {
        Cnd cnd = Cnd.NEW();
        NutMap map = new NutMap();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 审批履历
            cnd.and("c.operateState", ">=", 0);
            if (!Strings.isBlank(applyId)) {
                cnd.and("b.applyId", "like", "%" + applyId + "%");
            }
            if (!Strings.isBlank(assetInfo)) {
                cnd.and(Cnd.exps("c.assetName", "like", "%" + assetInfo + "%")
                        .or("c.deviceVersion", "like", "%" + assetInfo + "%")
                        .or("c.serialNumber", "like", "%" + assetInfo + "%"));
            }
            if (!Strings.isBlank(applyState)) {
                cnd.and("c.operateState", "=", applyState);
            }
            if (!Strings.isBlank(chargeMan)) {
                cnd.and("c.proposer", "=", chargeMan);
            }
            if (!Strings.isBlank(startDate) && Strings.isBlank(endDate)) {
                cnd.and("c.returnDate", ">=", sim.parse(startDate));
            }
            if (Strings.isBlank(startDate) && !Strings.isBlank(endDate)) {
                cnd.and(Cnd.exps("c.lendDate", "<=", sim.parse(endDate)).and("c.returnDate", ">=", sim.parse(endDate)));
            }
            if (!Strings.isBlank(startDate) && !Strings.isBlank(endDate)) {
                cnd.and(Cnd.exps(Cnd.exps("c.lendDate", "<=", sim.parse(startDate)).and("c.returnDate", ">=", sim.parse(startDate))
                        .or(Cnd.exps("c.lendDate", "<=", sim.parse(endDate)).and("c.returnDate", ">=", sim.parse(endDate)))));
            }
            map = reordService.queryRecords(length, start, draw, order, columns, cnd);
        } catch (Exception e) {
            LOG.error("Query data error..", e);
        }
        return map;
    }

    /**
     * 详情
     *
     * @param applyId
     * @param req
     * @return
     */
    @At("/detail/?")
    @Ok("beetl:/platform/apply/record/detail.html")
    @RequiresPermissions("asset.apply.record.detail")
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
    @RequiresPermissions("asset.apply.record.detail")
    public Object detail(@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                         @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns, @Param("applyId") String applyId) {
        return reordService.queryDetail(length, start, draw, order, columns, applyId);
    }
}
