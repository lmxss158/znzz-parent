package com.znzz.app.web.modules.controllers.platform.asset.inventory.record;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.Ins_Asset_Inventory_Record;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.service.InsAssetInventoryRecordService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.TemplateDownUtils;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetInventorySearchForm;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.UploadAdaptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@At("/asset/inventory/record")
public class InsAssetInventoryRecordController {

    @Inject
    ImportService importService;
    @Inject
    private InsAssetInventoryRecordService insAssetInventoryRecordService;
    @Inject
    InsAssetsService assetsService;
    @Inject
    ExportService exportService;

    private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
    private Dao dao = ioc_my.get(Dao.class);

    @At("")
    @Ok("beetl:/platform/asset/inventory/record/index.html")
    @RequiresPermissions("assets.inventory.record")
    public void index(){

    }

    @At
    @Ok("json:full")
    public Object data(@Param("..") AssetInventorySearchForm assetInventorySearchForm, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        // 获取盘点履历信息
        return insAssetInventoryRecordService.getInventoryRecordData(assetInventorySearchForm, length, start, draw, order, columns, null, null);
    }

    /**
     * 盘点履历模板下载
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    // 下载模板
    @At("/templateDownload")
    @Ok("raw")
    @RequiresPermissions("assets.inventory.record")
    public Object templateDownload(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
        // 得到下载的文件
        File templateFile = TemplateDownUtils.getTemplateFile("inventoryRecord_template", "template", response);
        return templateFile ;
    }

    // 导入功能
    @At
    @Ok("json:full")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload/assets" })
    @RequiresPermissions("assets.inventory.record")
    @SuppressWarnings("unchecked")
    public Object upload( @Param("recordFile") File file,HttpServletRequest request,HttpServletResponse response) throws Exception{
        Result result = Result.error("导入失败");
        List<ExcelImportResult<Ins_Asset_Inventory_Record>> importFile = importService.importFile(request, file, "ins_asset_inventory_record");
        List<Ins_Asset_Inventory_Record> failList = importFile.get(0).getFailList(); //失败的数据集合
        if (failList.size() == 0){
            //获取数据集
            List<Ins_Asset_Inventory_Record> importFileList = importFile.get(0).getList();
            // excel表格非空校验
            if (importFileList.size() == 0){
                return Result.error("模板中数据为空,请填写数据后再进行导入!");
            }
            //错误追踪
            StringBuilder sb = new StringBuilder();
            if (importFileList.size() > 0 && !importFileList.isEmpty()){
                for (int i = 0; i < importFileList.size(); i++) {
                    String tempAssetCode = importFileList.get(i).getAssetCode();
                    Date tempInventoryDate = importFileList.get(i).getInventoryDate();
                    String tempJobNumber = importFileList.get(i).getJobNumber();
                    int countTempJobNumber = dao.count(Sys_user.class,Cnd.where("entryNumber","=",tempJobNumber));
                    if (countTempJobNumber == 0){
                        sb.append("第" + (i+1) + "行," + "工号" + tempJobNumber + "在系统数据库用户表中不存在,请检查!</br>");
                    }
                    if (tempAssetCode == null || "".equals(tempAssetCode)){
                        sb.append("第" + (i+1) +"行," + "统一编号为空!</br>");
                    }
                    if (tempInventoryDate == null || "".equals(tempInventoryDate)){
                        sb.append("第" + (i+1) +"行," + "盘点日期为空或格式不正确,请检查!</br>");
                    }
                }
            }
            response.setContentType("text/html");
            if (sb.length() != 0){
                return Result.error(sb.toString());
            }else if(importFileList.size() > 0 && !importFileList.isEmpty()) {
                //两个月内同一资产同一盘点人重复问题
                for (Ins_Asset_Inventory_Record ins_Asset_Inventory_Record : importFileList) {
                    String assetCode = ins_Asset_Inventory_Record.getAssetCode();
                    String inventorySite = ins_Asset_Inventory_Record.getInventorySite();
                    String jobNumber = ins_Asset_Inventory_Record.getJobNumber();

                    //获取日期计算两个月内日期
                    Date inventoryDate = ins_Asset_Inventory_Record.getInventoryDate();
                    Calendar calendar = Calendar.getInstance();  //得到日历
                    calendar.setTime(inventoryDate);            //把当前时间赋给日历
                    String startYear = calendar.get(Calendar.YEAR) + "-01-01 00:00:00";
                    String endYear = calendar.get(Calendar.YEAR) + "-12-31 23:59:59";
                    //年初、年末时间转化为Date类型
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateParseStartYear = sdf.parse(startYear);
                    Date dateParseEndYear = sdf.parse(endYear);
                    //年初、年末时间转化为long类型
                    long startYearTimeL = dateParseStartYear.getTime();
                    long endYearTimeL = dateParseEndYear.getTime();

                    calendar.add(calendar.MONTH, -2);                       //设置为前2月
                    Date inventoryDate2Go = calendar.getTime();                     //获取2个月前的时间
                    long inventoryDate2GoTimeL = inventoryDate2Go.getTime();        //2个月前的时间转化为long类型

                    calendar.add(calendar.MONTH, 4);                        //设置为后2月
                    Date inventoryDate2After = calendar.getTime();                  //获取2个月后的时间
                    long inventoryDate2AfterTimeL = inventoryDate2After.getTime();  //2个月后的时间转化为long类型

                    //如果两个月前的时间超过本年年初则取本年年初时间
                    if (inventoryDate2GoTimeL < startYearTimeL){
                        inventoryDate2Go = dateParseStartYear;
                    }
                    //如果两个月后的时间超过本年年末则取本年年末时间
                    if (inventoryDate2AfterTimeL > endYearTimeL){
                        inventoryDate2After = dateParseEndYear;
                    }
                    //根据资产编号、工号、盘点时间查询两月内同一资产盘点次数
                    int countL = dao.count(Ins_Asset_Inventory_Record.class, Cnd.where("assetCode", "=", assetCode).and("jobNumber", "=", jobNumber).and("inventoryDate", "between", new Object[]{inventoryDate2Go,inventoryDate}));
                    int countH= dao.count(Ins_Asset_Inventory_Record.class, Cnd.where("assetCode", "=", assetCode).and("jobNumber", "=", jobNumber).and("inventoryDate", "between", new Object[]{inventoryDate,inventoryDate2After}));
                    if (countL >= 1) {

                        //获取最新时间的盘点记录
                        //String inventoryDateMax = insAssetInventoryRecordService.getNewInventorySite(assetCode, jobNumber);
                        //删除两月内同一资产盘点记录
                        insAssetInventoryRecordService.deleteExcludeMaxInventoryDate(assetCode, jobNumber, inventoryDate2Go, inventoryDate);

                        //根据资产编号查询资产表得到该资产信息
                        Ins_Assets insAsset = assetsService.getInsAssetBean(assetCode);
                        //获取资产名称、型号、国别、厂家、出厂日期、出厂编号、使用单位、责任人、借用日期、归还日期、资产位置
                        Ins_Asset_Inventory_Record inventoryRecord = new Ins_Asset_Inventory_Record();
                        inventoryRecord.setAssetCode(assetCode);
                        inventoryRecord.setInventorySite(inventorySite);
                        inventoryRecord.setJobNumber(jobNumber);
                        inventoryRecord.setInventoryDate(inventoryDate);

                        //插入盘点履历表
                        insAssetInventoryRecordService.insert(inventoryRecord);

                    }else if (countH >=1){
                        continue;
                    }
                    else {

                        //根据资产编号查询资产表得到该资产信息
                        Ins_Assets insAsset = assetsService.getInsAssetBean(assetCode);
                        Ins_Asset_Inventory_Record inventoryRecord = new Ins_Asset_Inventory_Record();
                        inventoryRecord.setAssetCode(assetCode);
                        inventoryRecord.setInventorySite(inventorySite);
                        inventoryRecord.setJobNumber(jobNumber);
                        inventoryRecord.setInventoryDate(inventoryDate);

                        //插入盘点履历表
                        insAssetInventoryRecordService.insert(inventoryRecord);
                    }
                }
            }
        }

        if(!failList.isEmpty() || failList.size()>0 ){
            response.setContentType("text/html");
            return result;
        }

        response.setContentType("text/html");
        result = Result.success("导入成功");
        return result;
    }

    //导出
    @At
    @Ok("json")
    @RequiresPermissions("assets.inventory.record")
    public String exportInventoryRecordInfo(HttpServletRequest request, HttpServletResponse response, @Param("..") AssetInventorySearchForm assetInventorySearchForm, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        NutMap re = insAssetInventoryRecordService.getInventoryRecordData(assetInventorySearchForm, 100000, 0, 10000, order, columns, null, null);
        List<Ins_Asset_Inventory_Record> list = (List<Ins_Asset_Inventory_Record>) re.get("data");
        List<Class<?>> voList = new ArrayList<>();
        Class<?> forName = Ins_Asset_Inventory_Record.class;
        voList.add(forName);
        String url = exportService.exportFile(request, response, voList,"ins_asset_inventory_record",list);
        return  url;
    }

    @At
    @Ok("void")
    @RequiresPermissions("assets.inventory.record")
    public void exportInventoryRecordInfoByUrl(@Param("url")String url,HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取文件名
        String filename = Configer.getInstance().getProperty("ins_asset_inventory_record");
        //根据URL找到路径下载,返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file, filename, response, true);
    }

}
