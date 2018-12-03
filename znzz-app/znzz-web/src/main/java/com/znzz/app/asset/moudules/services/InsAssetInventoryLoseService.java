package com.znzz.app.asset.moudules.services;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.lang.util.NutMap;

import java.text.ParseException;
import java.util.List;

public interface InsAssetInventoryLoseService extends BaseService<Ins_Assets> {
    //获取资产台账数据
    NutMap getAssetsLoseData(AssetsForm assets,String createTimeLose, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Object o, Object o1, Object o2) throws ParseException;
}
