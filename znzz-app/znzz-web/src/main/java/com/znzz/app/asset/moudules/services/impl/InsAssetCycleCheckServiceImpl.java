package com.znzz.app.asset.moudules.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.znzz.app.web.commons.util.OrderByUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import com.znzz.app.asset.moudules.models.Ins_Asset_CycleCheck;
import com.znzz.app.asset.moudules.services.InsAssetCycleCheckService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * @author chenzhongliang
 * @classname InsAssetCycleCheckServiceImpl.java
 * @date 2017年11月30日
 */
@IocBean(args = {"refer:dao"})
public class InsAssetCycleCheckServiceImpl extends BaseServiceImpl<Ins_Asset_CycleCheck> implements InsAssetCycleCheckService {
    private static final Log log = Logs.get();

    public InsAssetCycleCheckServiceImpl(Dao dao) {
        super(dao);
    }

    @Override
    public NutMap getList(Ins_Asset_CycleCheck assetCheck, String module, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns) {
        Sql sql = Sqls.create("select * from ins_asset_cyclecheck $condition");
        Cnd cnd = Cnd.NEW();

        if ("thismonth".equalsIgnoreCase(module) || "nextmonth".equalsIgnoreCase(module)) {
            if (assetCheck.getEffectiveDate2() != null && !"".equals(assetCheck.getEffectiveDate2())) {
                String[] list = assetCheck.getEffectiveDate2().split("-");
                String startTime = list[0].trim();
                String endTime = list[1].trim();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = sdf.parse(startTime);
                    endDate = sdf.parse(endTime);
                } catch (ParseException e) {
                    log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
                    e.printStackTrace();
                }
                cnd.where().and("effectiveDate", "between", new Object[]{startDate, endDate});
            }
        } else {
            if (assetCheck.getEffectiveDate2() != null && !"".equals(assetCheck.getEffectiveDate2())) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                int year = Integer.parseInt(assetCheck.getEffectiveDate2().split("-")[0]);
                int month = Integer.parseInt(assetCheck.getEffectiveDate2().split("-")[1]);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String endTime = df.format(cal.getTime());

                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DATE, 1);
                String startTime = df.format(cal.getTime());
                cnd.where().and("effectiveDate", "between", new Object[]{startTime, endTime});
            }
        }
        if (assetCheck.getSentCheckDate2() != null && !"".equals(assetCheck.getSentCheckDate2())) {
            String[] list = assetCheck.getSentCheckDate2().split("-");
            String startTime = list[0].trim();
            String endTime = list[1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
            } catch (ParseException e) {
                log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
                e.printStackTrace();
            }
            cnd.where().and("sentCheckDate", "between", new Object[]{startDate, endDate});
        }
        if (assetCheck.getCheckDate2() != null && !"".equals(assetCheck.getCheckDate2())) {
            String[] list = assetCheck.getCheckDate2().split("-");
            String startTime = list[0].trim();
            String endTime = list[1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
            } catch (ParseException e) {
                log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
                e.printStackTrace();
            }
            cnd.where().and("checkDate", "between", new Object[]{startDate, endDate});
        }
        if (assetCheck.getGetDate2() != null && !"".equals(assetCheck.getGetDate2())) {
            String[] list = assetCheck.getGetDate2().split("-");
            String startTime = list[0].trim();
            String endTime = list[1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
            } catch (ParseException e) {
                log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
                e.printStackTrace();
            }
            cnd.where().and("getDate", "between", new Object[]{startDate, endDate});
        }
        if (assetCheck.getFilDate2() != null && !"".equals(assetCheck.getFilDate2())) {
            String[] list = assetCheck.getFilDate2().split("-");
            String startTime = list[0].trim();
            String endTime = list[1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
            } catch (ParseException e) {
                log.error("startTime和endTime:" + startTime + "," + endTime + ",时间转换异常");
                e.printStackTrace();
            }
            cnd.where().and("filDate", "between", new Object[]{startDate, endDate});
        }

        if (!Strings.isBlank(assetCheck.getAssetCode())) {
            SqlExpressionGroup expressionGroup = Cnd.exps("assetCode", "like", "%" + assetCheck.getAssetCode().trim() + "%")
                    .or("assetName", "like", "%" + assetCheck.getAssetCode().trim() + "%")
                    .or("assetModel", "like", "%" + assetCheck.getAssetCode().trim() + "%")
                    .or("serialNumber", "like", "%" + assetCheck.getAssetCode().trim() + "%");
            cnd.where().and(expressionGroup);
            /*cnd.or("assetCode", "like", "%" + assetCheck.getAssetCode().trim() + "%");
                cnd.or("assetName", "like", "%" + assetCheck.getAssetCode().trim() + "%");
				cnd.or("assetModel", "like", "%" + assetCheck.getAssetCode().trim() + "%");
				cnd.or("serialNumber", "like", "%" + assetCheck.getAssetCode().trim() + "%");*/
        }

        if (!Strings.isBlank(assetCheck.getTestDepartment())) {
            cnd.where().and("testDepartment", "like", "%" + assetCheck.getTestDepartment().trim() + "%");
        }
        if (!Strings.isBlank(assetCheck.getRespMan())) {
            cnd.where().and("respMan", "like", "%" + assetCheck.getRespMan().trim() + "%");
        }
        if (!Strings.isBlank(assetCheck.getUserMan())) {
            cnd.where().and("userMan", "like", "%" + assetCheck.getUserMan().trim() + "%");
        }
        if (!Strings.isBlank(assetCheck.getUseDepartment())) {
            cnd.where().and("useDepartment", "like", "%" + assetCheck.getUseDepartment().trim() + "%");
        }
        if (!Strings.isBlank(assetCheck.getTestResult())) {
            cnd.where().and("testResult", "like", "%" + assetCheck.getTestResult().trim() + "%");
        }
        //本月计划
        if ("thismonth".equalsIgnoreCase(module)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            int year = Integer.parseInt(df.format(date).split("-")[0]);
            int month = Integer.parseInt(df.format(date).split("-")[1]);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = df.format(cal.getTime());

            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, 1);
            String startTime = df.format(cal.getTime());

            cnd.where().and("effectiveDate", "between", new Object[]{startTime, endTime});
        }
        if ("nextmonth".equalsIgnoreCase(module)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            int year = Integer.parseInt(df.format(date).split("-")[0]);
            int month = Integer.parseInt(df.format(date).split("-")[1]);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = df.format(cal.getTime());

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DATE, 1);
            String startTime = df.format(cal.getTime());

            cnd.where().and("effectiveDate", "between", new Object[]{startTime, endTime});
        }

        //进行排序
        if (order != null && order.size() > 0) {
            // 添加中文排序
            cnd = OrderByUtil.orderByWithChinese(cnd, order, columns);
            /*for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                //获取排序的字段
                String name = col.getData();
                if ("assetname".equals(name)){
                    name = " CONVERT( `assetname` USING gbk)";
                }else if ("testdepartment".equals(name)){
                    name = " CONVERT( `testdepartment` USING gbk)";
                }else if ("usedepartment".equals(name)){
                    name = " CONVERT( `usedepartment` USING gbk)";
                }else if ("userman".equals(name)){
                    name = " CONVERT( `userman` USING gbk)";
                }else if ("respman".equals(name)){
                    name = " CONVERT( `respman` USING gbk)";
                }else if ("testresult".equals(name)){
                    name = " CONVERT( `testresult` USING gbk)";
                }else if ("remark".equals(name)){
                    name = " CONVERT( `remark` USING gbk)";
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
            }*/
        }

        sql.setCondition(cnd);
        NutMap data = data(length, start, draw, sql, sql);
        return data;
    }

    /*
     * 把excel中的list插入数据库,具体逻辑如下：
     * 同一月份下，统一编号已存在，执行update，反之insert
     */
    @Override
    public void insertList(List<Ins_Asset_CycleCheck> assetList) {
        //根据统一编号和有效日期（到月份），进行更新或新增操作
        List<Ins_Asset_CycleCheck> insertList = new ArrayList<Ins_Asset_CycleCheck>();
        List<Ins_Asset_CycleCheck> updateList = new ArrayList<Ins_Asset_CycleCheck>();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Ins_Asset_CycleCheck check : assetList) {
            String assetCode = check.getAssetCode();

            // 对有效日期字段进行处理,拿到当月的时间范围
            Date date = check.getEffectiveDate();
            int year = Integer.parseInt(df.format(date).split("-")[0]);
            int month = Integer.parseInt(df.format(date).split("-")[1]);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = df.format(cal.getTime());

            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, 1);
            String startTime = df.format(cal.getTime());

            //查询ins_asset_cyclecheck表里统一月中是否存在相同统一编号
            Cnd cnd = Cnd.NEW();
            cnd.where().and("assetCode", "=", assetCode);
            cnd.where().and("effectiveDate", "between", new Object[]{startTime, endTime});
            Ins_Asset_CycleCheck iac = dao().fetch(Ins_Asset_CycleCheck.class, cnd);
            if (iac != null) {
                check.setId(iac.getId());
                updateList.add(check);
            } else {
                insertList.add(check);
            }


        }

        dao().update(updateList);
        dao().insert(insertList);
    }

}
