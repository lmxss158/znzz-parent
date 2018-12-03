package com.znzz.app.asset.moudules.services.apply;

import com.znzz.app.asset.moudules.models.apply.Ins_apply_record;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface ApplyReordService extends BaseService<Ins_apply_record> {

    /**
     * 审批履历记录
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @param cnd
     * @param o
     * @return
     */
    NutMap queryRecords(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd);

    /**
     * 操作履历
     *
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    NutMap queryDetail(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, String applyId);
}
