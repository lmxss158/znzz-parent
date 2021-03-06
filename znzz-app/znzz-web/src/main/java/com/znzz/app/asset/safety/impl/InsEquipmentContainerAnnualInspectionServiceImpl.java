package com.znzz.app.asset.safety.impl;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Container_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentContainerAnnualInspectionService;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import java.text.SimpleDateFormat;
import java.util.List;

@IocBean(args = {"refer:dao"})
public class InsEquipmentContainerAnnualInspectionServiceImpl extends BaseServiceImpl<Ins_Equipment_Container_Annual_Inspection> implements InsEquipmentContainerAnnualInspectionService {
    public InsEquipmentContainerAnnualInspectionServiceImpl(Dao dao) {
        super(dao);
    }
    private static String CONTAINER_ANNUAL_DATA_SQL = " SELECT " +
            " a.id, " +
            " a.checkNo, " +
            " a.containerCode, " +
            " ( " +
            "  concat_ws('-', c.`name`, e.`name`) " +
            " ) containerSite, " +
            " c.`name` floorNo, " +
            " e.`name` roomNo, " +
            " d.username chargePerson, " +
            " a.telephone, " +
            " a.annualInspectionDate, " +
            " a.`medium`, " +
            " a.pressure, " +
            " a.tankSize, " +
            " " +
            " IF ( " +
            " TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()) > 0, " +
            " TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()), " +
            " 0 " +
            " ) lefttime, " +
            " a.nextAnnualInspectionDate, " +
            " a.daysNotice " +
            " FROM " +
            " ins_equipment_container_annual_inspection a " +
            " LEFT JOIN sys_dict c ON a.floorNo = c.`id` " +
            " LEFT JOIN sys_dict e ON a.roomNo = e.`id` " +
            " LEFT JOIN sys_user d ON a.chargePerson = d.id $condition " ;
    @Override
    public NutMap getContainerAnnualInspectionData(Ins_Equipment_Container_Annual_Inspection containerAnnualInspection, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Object o, Object o1) {
        //查询条件
        Cnd cnd = Cnd.NEW();
        //检测号
        if (Strings.isNotBlank(containerAnnualInspection.getCheckNo())){
            cnd.and("a.checkNo","like","%" + containerAnnualInspection.getCheckNo().trim() + "%");
        }
        //容器编号
        if (Strings.isNotBlank(containerAnnualInspection.getContainerCode())){
            cnd.and("a.containerCode","like","%" + containerAnnualInspection.getContainerCode().trim() + "%");
        }
        //负责人
        if(Strings.isNotBlank(containerAnnualInspection.getChargePerson())){
            cnd.and("a.chargePerson","like","%" + containerAnnualInspection.getChargePerson().trim() + "%");
        }
        //检索周期
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        //年检检索开始日期
        String annualInspectionCycleBeginTime = containerAnnualInspection.getInspectionCycleBeginTime();
        //年检检索结束日期
        String annualInspectionCycleEndTime = containerAnnualInspection.getInspectionCycleEndTime();
        if (Strings.isNotBlank(annualInspectionCycleBeginTime) && Strings.isBlank(annualInspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate",">=",annualInspectionCycleBeginTime);
        }
        if (Strings.isBlank(annualInspectionCycleBeginTime) && Strings.isNotBlank(annualInspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate","<=",annualInspectionCycleEndTime);
        }
        if (Strings.isNotBlank(annualInspectionCycleBeginTime) && Strings.isNotBlank(annualInspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate","between", new Object[]{annualInspectionCycleBeginTime,annualInspectionCycleEndTime});
        }

        Sql sql = Sqls.create(CONTAINER_ANNUAL_DATA_SQL);
        //排序
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {

                DataTableColumn col = columns.get(order.getColumn());

                String name = col.getData();
                if ("medium".equalsIgnoreCase(name)){
                    name = " CONVERT(a.medium USING gbk)";
                }
                if ("chargePerson".equalsIgnoreCase(name)){
                    name = " CONVERT(a.chargePerson USING gbk)";
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        sql.setCondition(cnd);

        return data(length,start,draw,sql,sql);
    }

    @Override
    public boolean checkContainerCode(String containerCode, Cnd cnd) {
        List<Record> query = dao().query("ins_equipment_container_annual_inspection",cnd.and("containerCode","=",containerCode.trim()));
        if (query.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public Ins_Equipment_Container_Annual_Inspection getAnnualInspectionEditData(Integer id) {
        Cnd cnd = Cnd.NEW();
        Sql sql = Sqls.create(CONTAINER_ANNUAL_DATA_SQL);
        sql.setVar("order",cnd.and("a.id","=",id));
        sql.setCondition(cnd);
        Entity<Ins_Equipment_Container_Annual_Inspection> entity = dao().getEntity(Ins_Equipment_Container_Annual_Inspection.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);

        return sql.getObject(Ins_Equipment_Container_Annual_Inspection.class);
    }

    @Override
    public List<Ins_Equipment_Container_Annual_Inspection> getContainerAnnualInspectionNoticeData() {
        Sql sql = Sqls.create(" SELECT  " +
          "   c.id,  " +
          "   c.containerCode,  " +
          "   c.checkNo,  " +
          "   (  " +
          "    concat_ws('-', d.`name`, e.`name`)  " +
          "   ) containerSite,  " +
          "   c.chargePerson  " +
          "   FROM  " +
          "   ins_equipment_container_annual_inspection c  " +
          "   LEFT JOIN sys_dict d ON c.floorNo = d.`id`  " +
          "   LEFT JOIN sys_dict e ON c.roomNo = e.`id`  " +
          "   WHERE  " +
          " TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()) = daysNotice ");
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao().getEntity(Ins_Equipment_Container_Annual_Inspection.class));
        dao().execute(sql);
        List<Ins_Equipment_Container_Annual_Inspection> containerAnnualInspectionList = sql.getList(Ins_Equipment_Container_Annual_Inspection.class);

        return containerAnnualInspectionList;
    }
}
