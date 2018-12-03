package com.znzz.app.instrument.modules.service;

import com.znzz.app.asset.moudules.models.Ins_Asset_Inventory_Record;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetInventorySearchForm;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface InsAssetInventoryRecordService extends BaseService<Ins_Asset_Inventory_Record> {
    //履历列表页面结果集
    NutMap getInventoryRecordData(AssetInventorySearchForm assetInventorySearchForm, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, String object2) throws ParseException;

    //获取最新位置
    String getNewInventorySite(String assetCode, String jobNumber);

    //删除两月内同一工号、同一资产盘点记录
    void deleteExcludeMaxInventoryDate(String assetCode, String jobNumber, Date inventoryDate2Go, Date inventoryDate);

    //
    List<Ins_Asset_Inventory_Record> exportInventoryRecordInfo(HttpServletRequest request, HttpServletResponse response, Map<String,String> exportMenu, Ins_Asset_Inventory_Record vo);
}
