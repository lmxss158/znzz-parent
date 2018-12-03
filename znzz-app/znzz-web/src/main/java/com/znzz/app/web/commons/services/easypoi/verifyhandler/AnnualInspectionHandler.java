package com.znzz.app.web.commons.services.easypoi.verifyhandler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;

public class AnnualInspectionHandler implements IExcelVerifyHandler<Ins_Equipment_Lift_Annual_Inspection> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(Ins_Equipment_Lift_Annual_Inspection liftAnnualInspection) {
        ExcelVerifyHandlerResult verifyHandlerResult = new ExcelVerifyHandlerResult();
        if (liftAnnualInspection.getLiftCode() == null || "".equals(liftAnnualInspection.getLiftCode())){
            verifyHandlerResult.setMsg("统一编码为空");
            return verifyHandlerResult;
        }
        verifyHandlerResult.setSuccess(true);
        return verifyHandlerResult;
    }
}
