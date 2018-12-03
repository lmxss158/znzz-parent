package com.znzz.app.asset.safety;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

public interface InsEquipmentLiftAnnualInspectionService extends BaseService<Ins_Equipment_Lift_Annual_Inspection> {

    //获取电梯设备年检信息
    NutMap getEquipmentLiftData(Ins_Equipment_Lift_Annual_Inspection ins_equipment_lift_annual_inspection, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Object o, Object o1);

    //获取电梯设备中该到期检定提醒的设备数据
    List<Ins_Equipment_Lift_Annual_Inspection> getEquipmentLiftNoticeData();

    //查询符合统一编号的电梯数据
    boolean checkLiftCode(String liftCode, Cnd cnd);

    //根据id查询设备信息回显
    Ins_Equipment_Lift_Annual_Inspection getEquipmentLiftEditData(Integer id);
}
