package com.znzz.app.asset.safety.impl;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentLiftAnnualInspectionService;
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
public class InsEquipmentLiftAnnualInspectionServiceImpl extends BaseServiceImpl<Ins_Equipment_Lift_Annual_Inspection> implements InsEquipmentLiftAnnualInspectionService {
    public InsEquipmentLiftAnnualInspectionServiceImpl(Dao dao) {
        super(dao);
    }
    private static String EQUIPMENT_LIFT_DATA_SQL = " SELECT " +
            " a.id,   " +
            " a.checkNo,   " +
            " a.liftCode,   " +
            "  (  " +
            "  concat_ws('-', c.`name`, e.`name`)   " +
            "  ) liftSite,   " +
            " c.`name` floorNo, " +
            " e.`name` liftNo, " +
            " d.username chargePerson, " +
            " a.telephone, " +
            " a.annualInspectionDate,  " +
            "  IF ( " +
            "  TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()) > 0, " +
            "  TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()), " +
            "  0 " +
            " ) lefttime," +
            " a.nextAnnualInspectionDate, " +
            " a.daysNotice " +
            " FROM " +
            " ins_equipment_lift_annual_inspection a " +
            " LEFT JOIN sys_dict c ON a.floorNo = c.`id` " +
            " LEFT JOIN sys_dict e ON a.liftNo = e.`id` " +
            " LEFT JOIN sys_user d on a.chargePerson=d.id $condition ";
    @Override
    public NutMap getEquipmentLiftData(Ins_Equipment_Lift_Annual_Inspection ins_equipment_lift_annual_inspection, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Object o, Object o1) {
        //查询条件
        Cnd cnd =Cnd.NEW();
        //检测号
        if (Strings.isNotBlank(ins_equipment_lift_annual_inspection.getCheckNo())){
            cnd.and("a.checkNO","like","%" + ins_equipment_lift_annual_inspection.getCheckNo().trim() + "%");
        }
        //电梯编号
        if (Strings.isNotBlank(ins_equipment_lift_annual_inspection.getLiftCode())){
            cnd.and("a.liftCode","like","%" + ins_equipment_lift_annual_inspection.getLiftCode().trim() + "%");
        }
        //负责人
        if (Strings.isNotBlank(ins_equipment_lift_annual_inspection.getChargePerson())){
            cnd.and("a.chargePerson","like","%" + ins_equipment_lift_annual_inspection.getChargePerson().trim() + "%");
        }
        //年检周期
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        //年检日期开始时间
        String inspectionCycleBeginTime = ins_equipment_lift_annual_inspection.getInspectionCycleBeginTime();
        //年检日期结束时间
        String inspectionCycleEndTime = ins_equipment_lift_annual_inspection.getInspectionCycleEndTime();

        if (Strings.isNotBlank(inspectionCycleBeginTime) && Strings.isBlank(inspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate", ">=", inspectionCycleBeginTime);
        }
        if (Strings.isBlank(inspectionCycleBeginTime) && Strings.isNotBlank(inspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate", "<=", inspectionCycleEndTime);
        }
        if (Strings.isNotBlank(inspectionCycleBeginTime) && Strings.isNotBlank(inspectionCycleEndTime)){
            cnd.and("a.annualInspectionDate", "between", new Object[]{inspectionCycleBeginTime,inspectionCycleEndTime});
        }

        Sql sql = Sqls.create(EQUIPMENT_LIFT_DATA_SQL);

        //排序
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {

                DataTableColumn col = columns.get(order.getColumn());

                String name = col.getData();
                if ("chargePerson".equals(name)){
                    name = " CONVERT(a.chargePerson USING gbk)";
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        sql.setCondition(cnd);
        return data(length, start, draw, sql, sql);
    }

    @Override
    public boolean checkLiftCode(String liftCode, Cnd cnd) {
        List<Record> query = dao().query("ins_equipment_lift_annual_inspection",cnd.and("liftCode","=",liftCode.trim()));
        //如果有重复则返回false
        if (query.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public Ins_Equipment_Lift_Annual_Inspection getEquipmentLiftEditData(Integer id) {
        Cnd cnd =Cnd.NEW();
        Sql sql = Sqls.create(EQUIPMENT_LIFT_DATA_SQL);
        sql.setVar("order",cnd.and("a.id","=",id));
        sql.setCondition(cnd);
        Entity<Ins_Equipment_Lift_Annual_Inspection> entity = dao().getEntity(Ins_Equipment_Lift_Annual_Inspection.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getObject(Ins_Equipment_Lift_Annual_Inspection.class);

    }

    /**
     * 获取电梯设备中该到期检定提醒的设备数据
     * @return liftAnnualInspectionList
     */
    @Override
    public List<Ins_Equipment_Lift_Annual_Inspection> getEquipmentLiftNoticeData() {
        Sql sql = Sqls.create("SELECT " +
                " c.id, " +
                " c.liftCode, " +
                " c.checkNo, " +
                " ( " +
                "  concat_ws('-', d.`name`, e.`name`) " +
                " ) liftSite, " +
                " c.chargePerson " +
                " FROM " +
                " ins_equipment_lift_annual_inspection c " +
                " LEFT JOIN sys_dict d ON c.floorNo = d.`id` " +
                " LEFT JOIN sys_dict e ON c.liftNo = e.`id` " +
                " WHERE " +
                " TO_DAYS(nextAnnualInspectionDate) - TO_DAYS(NOW()) = daysNotice ");
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao().getEntity(Ins_Equipment_Lift_Annual_Inspection.class));
        dao().execute(sql);
        List<Ins_Equipment_Lift_Annual_Inspection> liftAnnualInspectionList = sql.getList(Ins_Equipment_Lift_Annual_Inspection.class);

        return liftAnnualInspectionList;
    }
}
