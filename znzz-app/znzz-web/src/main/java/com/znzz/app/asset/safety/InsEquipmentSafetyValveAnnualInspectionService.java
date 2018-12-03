package com.znzz.app.asset.safety;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Safety_Valve_Annual_Inspection;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;
import java.util.List;

public interface InsEquipmentSafetyValveAnnualInspectionService extends BaseService<Ins_Equipment_Safety_Valve_Annual_Inspection> {

    //获取安全阀设备年检信息
    NutMap getEquipmentValveData(Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Object o, Object o1);

    //根据安全阀设备统一编号查询设备信息
    boolean checkValveCode(String safetyValveCode, Cnd cnd);

    //根据id查询设备信息回显
    Ins_Equipment_Safety_Valve_Annual_Inspection getEquipmentValveEditData(Integer id);

    //获取安全阀设备需提醒数据
    List<Ins_Equipment_Safety_Valve_Annual_Inspection> getEquipmentValveNoticeData();
}
