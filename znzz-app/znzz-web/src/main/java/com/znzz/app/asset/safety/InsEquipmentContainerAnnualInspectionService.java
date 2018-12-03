package com.znzz.app.asset.safety;

import com.znzz.app.asset.moudules.models.Ins_Equipment_Container_Annual_Inspection;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;
import java.util.List;

public interface InsEquipmentContainerAnnualInspectionService extends BaseService<Ins_Equipment_Container_Annual_Inspection> {

    //获取电梯设备年检信息
    NutMap getContainerAnnualInspectionData(Ins_Equipment_Container_Annual_Inspection containerAnnualInspection, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Object o, Object o1);

    //查询符合统一编号的容器数据
    boolean checkContainerCode(String containerCode, Cnd cnd);

    //根据id获取容器设备信息回显数据
    Ins_Equipment_Container_Annual_Inspection getAnnualInspectionEditData(Integer id);

    //获取容器年检设备中该到期提醒的数据集
    List<Ins_Equipment_Container_Annual_Inspection> getContainerAnnualInspectionNoticeData();
}
