package com.znzz.app.asset.moudules.services.impl;

import com.znzz.app.asset.moudules.models.Ins_Asset_Inventory_Record;
import com.znzz.app.instrument.modules.service.InsAssetInventoryRecordService;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetInventorySearchForm;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@IocBean(args = {"refer:dao"})
public class InsAssetInventoryRecordServiceImpl extends BaseServiceImpl<Ins_Asset_Inventory_Record> implements InsAssetInventoryRecordService {

    public InsAssetInventoryRecordServiceImpl(Dao dao) {
        super(dao);
    }

    private static String $SQL = "SELECT " +
            "  a.id, " +
            "  a.assetCode, " +
            "  a.inventorySite inventorySite, " +
            "  f.username inventoryChecker, " +
            "  a.jobNumber, " +
            "  a.inventoryDate, " +
            "  c.deviceVersion assetModel, " +
            "  b.serialNumber serialNumber, " +
            "  b.locationInfo assetOriginalSite, " +
            "  b.country country," +
            "  b.manufactor manufactor, " +
            "  b.factoryTime factoryTime, " +
            "  b.lendDate lendDate, " +
            "  b.returnDate returnDate, " +
            "  c.assetName assetName, " +
            "  d.username chargePerson, " +
            "  e.name useDepartment, " +
            "  g.name assetType, " +
            "  h.name inventoryCheckerDepartment " +
            "  FROM " +
            "  ins_asset_inventory_record a " +
            "  LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode " +
            "  LEFT JOIN ins_assets_version c ON b.deviceVersion = c.deviceVersionOrg " +
            "  LEFT JOIN sys_unit e on b.borrowDepart = e.id " +
            "  LEFT JOIN sys_user d ON d.id = b.chargePerson " +
            "  LEFT JOIN sys_user f on a.jobNumber = f.entryNumber " +
            "  LEFT JOIN sys_dict g on b.assetType = g.code " +
            "  LEFT JOIN sys_unit h on f.unitid = h.id $condition";

    @Override
    public NutMap getInventoryRecordData(AssetInventorySearchForm assetInventorySearchForm, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd2, String linkName) throws ParseException {


        //资产信息进行处理
        Cnd cnd = Cnd.NEW();

        if (Strings.isNotBlank(assetInventorySearchForm.getAssetCode())){
            SqlExpressionGroup expressionGroup = Cnd.exps("a.assetCode","like","%" + assetInventorySearchForm.getAssetCode().trim() + "%")
                    .or("c.assetName","like","%" + assetInventorySearchForm.getAssetCode().trim() + "%")
                    .or("c.deviceVersion","like","%" + assetInventorySearchForm.getAssetCode().trim() + "%")
                    .or("b.serialNumber","like","%" + assetInventorySearchForm.getAssetCode().trim() + "%");
            cnd.where().and(expressionGroup);
        }
        //盘点人
        if (Strings.isNotBlank(assetInventorySearchForm.getInventoryChecker())){
            cnd.and("f.id","like","%" + assetInventorySearchForm.getInventoryChecker().trim() + "%");
        }
        //盘点位置
        if (Strings.isNotBlank(assetInventorySearchForm.getInventorySite())){
            cnd.and("a.inventorySite","like","%" + assetInventorySearchForm.getInventorySite().trim() + "%");
        }
        //盘点时间
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String inventoryBeginTime = assetInventorySearchForm.getInventoryBeginTime();
        String inventoryEndTime = assetInventorySearchForm.getInventoryEndTime();
        if (Strings.isNotBlank(inventoryBeginTime) && Strings.isBlank(inventoryEndTime)){
            //long inventoryBeginTimeL = sim.parse(inventoryBeginTime).getTime() / 1000;
            cnd.and("a.inventoryDate", ">=", inventoryBeginTime);
        }
        if (Strings.isBlank(inventoryBeginTime) && Strings.isNotBlank(inventoryEndTime)){
            //long inventoryEndTimeL = sim.parse(inventoryEndTime).getTime() / 1000;
            inventoryEndTime = assetInventorySearchForm.getInventoryEndTime();
            cnd.and("a.inventoryDate", "<=", inventoryEndTime);
        }
        if (Strings.isNotBlank(inventoryBeginTime) && Strings.isNotBlank(inventoryEndTime)){
            //long inventoryBeginTimeL = sim.parse(inventoryBeginTime).getTime() / 1000;
            //long inventoryEndTimeL = sim.parse(inventoryEndTime).getTime() / 1000;
            cnd.and("a.inventoryDate", "between", new Object[]{inventoryBeginTime,inventoryEndTime});
        }

        Sql sql = Sqls.create($SQL);
        //添加排序功能
        if (order != null && order.size() > 0){
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String name = col.getData();
                if ("assetName".equals(name)){
                    name = " CONVERT(c.assetName USING gbk)";
                }else if ("assetModel".equals(name)){
                    name = "CONVERT(c.deviceVersion USING gbk)";
                }else if ("serialNumber".equals(name)){
                    name = "CONVERT(b.serialNumber USING gbk)";
                }else if ("useDepartment".equals(name)){
                    name = "CONVERT(e.name USING gbk)";
                }else if ("chargePerson".equals(name)){
                    name = "CONVERT(d.username USING gbk)";
                }else if ("assetOriginalSite".equals(name)){
                    name = "CONVERT(b.locationInfo USING gbk)";
                }else if ("inventorySite".equals(name)){
                    name = "CONVERT(a.inventorySite USING gbk)";
                }else if ("inventoryCheckerDepartment".equals(name)){
                    name = "CONVERT(h.name USING gbk)";
                }else if ("inventoryChecker".equals(name)){
                    name = "CONVERT(f.username USING gbk)";
                }
                cnd.orderBy(Sqls.escapeFieldValue(col.getData()).toString(), or.getDir());
            }
        }
        sql.setVar("order",cnd);
        sql.setCondition(cnd);
        return  data(length,start,draw,sql,sql);
    }

    @Override
    public String getNewInventorySite(String assetCode, String jobNumber) {
        Sql sql = Sqls.create("select max(inventoryDate) from ins_asset_inventory_record where assetCode=@assetCode and jobNumber=@jobNumber");
        sql.setParam("assetCode", assetCode);
        sql.setParam("jobNumber", jobNumber);
        sql.setCallback(Sqls.callback.str());
        dao().execute(sql);
        return sql.getString();
    }

    @Override
    public void deleteExcludeMaxInventoryDate(String assetCode, String jobNumber, Date inventoryDate2Go, Date inventoryDate ) {
        /*Sql sql = Sqls.create("delete from ins_asset_inventory_record where inventoryDate NOT in"
                + "  select a.inventoryDate from"
                + "  select max(inventoryDate) inventoryDate from ins_asset_inventory_record a where EXISTS "
                + "  select 1 from ins_asset_inventory_record b where a.assetCode=b.assetCode group by assetCode HAVING count(1)>1 "
                + "  group by assetCode"
                + "  ) a"
                + ")");*/
        Sql sql = Sqls.create("delete from ins_asset_inventory_record where assetCode=@assetCode and jobNumber=@jobNumber and inventoryDate >= @inventoryDate2Go and inventoryDate <=@inventoryDateL");

        sql.setParam("assetCode", assetCode);
        sql.setParam("jobNumber", jobNumber);
        sql.setParam("inventoryDate2Go",inventoryDate2Go);
        sql.setParam("inventoryDateL", inventoryDate);
        sql.setCallback(Sqls.callback.str());
        dao().execute(sql);

    }

    @Override
    public List<Ins_Asset_Inventory_Record> exportInventoryRecordInfo(HttpServletRequest request, HttpServletResponse response, Map<String, String> exportMenu, Ins_Asset_Inventory_Record vo) {
        return null;
    }

}