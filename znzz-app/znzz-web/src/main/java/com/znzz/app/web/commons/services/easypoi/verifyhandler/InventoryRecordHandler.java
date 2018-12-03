package com.znzz.app.web.commons.services.easypoi.verifyhandler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

import java.util.Map;

public class InventoryRecordHandler implements IExcelVerifyHandler<Map> {


    @Override
    public ExcelVerifyHandlerResult verifyHandler(Map obj) {
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult();
        //封装错误信息
        StringBuilder sb = new StringBuilder();
//
//        if (inventoryRecordImportForm.getAssetCode() == null || "".equals(inventoryRecordImportForm.getAssetCode())){
//            excelVerifyHandlerResult.setMsg("资产编码不能为空");
//            excelVerifyHandlerResult.setSuccess(false);
//            return excelVerifyHandlerResult;
//        }
        excelVerifyHandlerResult.setSuccess(true);
        return excelVerifyHandlerResult;
    }
}
