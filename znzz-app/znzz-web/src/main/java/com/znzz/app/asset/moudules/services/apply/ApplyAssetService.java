package com.znzz.app.asset.moudules.services.apply;

import com.znzz.app.asset.moudules.models.apply.Ins_Asset_Apply;
import com.znzz.app.web.modules.controllers.platform.apply.vo.ApplyAssetVo;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.Date;
import java.util.List;

public interface ApplyAssetService extends BaseService<Ins_Asset_Apply> {

    /**
     * 查询台账中适合预约申请的资产
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @param cnd
     * @return
     */
    NutMap queryApplyInfos(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, String assetState);

    /**
     * 新建申请回显信息
     *
     * @param assetCode
     * @return
     */
    ApplyAssetVo queryAddApplys(String assetCode);

    /**
     * @param applyId
     * @return
     */
    ApplyAssetVo queryMyDelay(String applyId);

    /**
     * 预约审批列表
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @param cnd
     * @return
     */
    NutMap queryApprovalInfos(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd);

    /**
     * 我的预约
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @param cnd
     * @return
     */
    NutMap queryMyApply(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd);

    /**
     * 重复校验
     *
     * @param assetCode
     * @param lendDate
     * @param returnDate
     * @return
     */
    int countConflict(String assetCode, Date lendDate, Date returnDate);
}
