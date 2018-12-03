package com.znzz.app.asset.moudules.services.impl;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetInventoryLoseService;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
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
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

@IocBean(args = {"refer:dao"})
public class InsAssetInventoryLoseServiceImpl extends BaseServiceImpl<Ins_Assets> implements InsAssetInventoryLoseService {
    public InsAssetInventoryLoseServiceImpl(Dao dao) {
        super(dao);
    }

    @Override
    public NutMap getAssetsLoseData(AssetsForm assets,String createTimeLose, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Object o, Object o1, Object o2) throws ParseException {

        NutMap re = new NutMap();
        Cnd cnd = Cnd.NEW();

        //资产信息
        if (Strings.isNotBlank(assets.getAssetCode())){
            SqlExpressionGroup expressionGroup = Cnd.exps("a.assetCode","like","%" + assets.getAssetCode().trim() + "%")
                    .or("c.assetName","like","%" + assets.getAssetCode().trim() + "%")
                    .or("c.deviceVersion","like","%" + assets.getAssetCode().trim() + "%")
                    .or("b.serialNumber","like","%" + assets.getAssetCode().trim() + "%");
            cnd.and(expressionGroup);
        }

        //责任人
        if (Strings.isNotBlank(assets.getChargePerson())){
            cnd.and("g.id","like","%" + assets.getChargePerson().trim() + "%");
        }

        //年度
        String createTimeSH = "";
        String createTime = createTimeLose +"-01-01 00:00:00";
        if (createTimeLose == null || "".equals(createTimeLose)){
            Calendar cale = null;
            cale = Calendar.getInstance();
            String currentYear = cale.get(Calendar.YEAR) +"-12-31 23:59:59";
            createTimeSH = currentYear;
        }else {
            createTimeSH = createTimeLose +"-12-31 23:59:59";
        }

        //查询盘点履历表本年度最大日期
        Sql sqlMaxDate = Sqls.create("select max(inventoryDate) from ins_asset_inventory_record where YEAR(inventoryDate) =@createTimeLose");
        sqlMaxDate.setParam("createTimeLose", createTimeLose);
        sqlMaxDate.setCallback(Sqls.callback.str());
        dao().execute(sqlMaxDate);
        String maxDateString =sqlMaxDate.getString();
        String maxDateStringa =null;

        //年度最大日期判空处理
        if (maxDateString == null){
            maxDateString = createTimeSH;
            maxDateStringa = maxDateString;
        }else {
            maxDateString = sqlMaxDate.getString();
            maxDateStringa = maxDateString;
        }

        //获取盘亏资产数据
        Sql inventoryLoseDataSql = Sqls.create("SELECT " +
                " b.id id, " +
                " h.NAME assetType, " +
                " a.assetCode, " +
                " c.assetName assetName, " +
                " b.ggName ggName, " +
                " b.country country, " +
                " b.manufactor manufactor, " +
                " b.factoryTime factoryTime, " +
                " c.deviceVersion deviceVersion, " +
                " b.serialNumber serialNumber, " +
                " b.lendDate lendDate, " +
                " b.returnDate returnDate, " +
                " d.`name` borrowDepart, " +
                " g.username chargePerson, " +
                " b.locationInfo locationInfo, " +
                " g.telephone telephone " +
                " FROM " +
                " ( " +
                "  SELECT " +
                "   assetCode " +
                "  FROM " +
                "   ins_assets_info e " +
                "  WHERE " +
                "   ( " +
                "    SELECT " +
                "     " +
                "   count(1) AS num " +
                "    FROM " +
                "     ins_asset_inventory_record f " +
                "    WHERE " +
                "     e.assetCode = f.assetCode  " +
                "     and YEAR(f.inventoryDate) =@createTimeLose " +
                "   ) = 0 " +
                " AND e.createTime <= @maxDateStringa " +
                " AND ( " +
                "       e.scrapState IS NULL " +
                        " OR e.scrapState <> 1 " +
                "      ) " +
                " ) a " +
                " LEFT JOIN ins_assets_info b ON a.assetCode = b.assetCode " +
                " LEFT JOIN ins_assets_version c ON c.deviceVersionOrg = b.deviceVersion " +
                " LEFT JOIN sys_unit d ON d.id = b.borrowDepart " +
                " LEFT JOIN sys_user g ON g.id = b.chargePerson " +
                " LEFT JOIN sys_dict h ON b.assetType = h.`code` " +
                "  $order" );

        inventoryLoseDataSql.setParam("maxDateStringa", maxDateStringa);
        inventoryLoseDataSql.setParam("createTimeLose", createTimeLose);
        inventoryLoseDataSql.setCallback(Sqls.callback.str());

        //添加排序功能
        if (order != null && order.size() > 0){
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String name = col.getData();
                if ("assetName".equals(name)){
                    name = " CONVERT(c.assetName USING gbk)";
                }else if ("deviceVersion".equals(name)){
                    name = "CONVERT(c.deviceVersion USING gbk)";
                }else if ("ggName".equals(name)){
                    name = "CONVERT(b.ggName USING gbk)";
                }else if ("serialNumber".equals(name)){
                    name = "CONVERT(b.serialNumber USING gbk)";
                }else if ("borrowDepart".equals(name)){
                    name = "CONVERT(d.`name` USING gbk)";
                }else if ("chargePerson".equals(name)){
                    name = "CONVERT(g.username USING gbk)";
                }else if ("locationInfo".equals(name)){
                    name = "CONVERT(b.locationInfo USING gbk)";
                }
                cnd.orderBy(Sqls.escapeFieldValue(col.getData()).toString(), or.getDir());
            }
        }
        inventoryLoseDataSql.setVar("order",cnd);
        //报废资产除外
       // cnd.and("b.scrapState", "<>", 1);
        inventoryLoseDataSql.setCondition(cnd);

        return  data(length,start,draw,inventoryLoseDataSql,inventoryLoseDataSql);
    }

}