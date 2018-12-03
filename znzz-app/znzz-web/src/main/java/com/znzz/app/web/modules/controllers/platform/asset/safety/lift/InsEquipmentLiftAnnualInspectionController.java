package com.znzz.app.web.modules.controllers.platform.asset.safety.lift;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentLiftAnnualInspectionService;
import com.znzz.app.sys.modules.models.Sys_dict;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysDictService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.TemplateDownUtils;
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
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@IocBean
@At("/asset/safety/lift")
public class InsEquipmentLiftAnnualInspectionController {
    @Inject
    private InsEquipmentLiftAnnualInspectionService insEquipmentLiftAnnualInsectionService;
    @Inject
    ImportService importService;
    @Inject
    ExportService exportService;
    @Inject
    SysDictService sysDictService;
    @Inject
    SysUserService sysUserService;
    @Inject
    private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
    Dao dao = ioc_my.get(Dao.class);

    //电梯设备年检信息列表页面
    @At("")
    @Ok("beetl:/platform/asset/safety/lift/index.html")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public void index(){

    }

    //获取电梯设备年检信息
    @At
    @Ok("json:full")
    public  Object data(@Param("..")Ins_Equipment_Lift_Annual_Inspection ins_equipment_lift_annual_inspection, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        //获取电梯设备年检信息
        return insEquipmentLiftAnnualInsectionService.getEquipmentLiftData(ins_equipment_lift_annual_inspection, length, start, draw, order, columns, null, null);
    }

    //跳转电梯添加页面
    @At
    @Ok("beetl:/platform/asset/safety/lift/add.html")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public void add(HttpServletRequest request) {
        Cnd cnd = Cnd.NEW();
        List<Sys_dict> floors = sysDictService.query(cnd.where("code","like","%floorNo%").and("parentId","<>",""));
        List<Sys_dict> lifts = sysDictService.query(cnd.where("code","like","%liftNo%").and("parentId","<>",""));
        request.setAttribute("floors",floors);
        request.setAttribute("lifts",lifts);
    }

    //新建电梯设备年检记录
    @At
    @Ok("json")
    @SLog(tag = "新建电梯设备年检记录",msg = "电梯统一编号:${args[0].liftCode}")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object addDo(@Param("..") Ins_Equipment_Lift_Annual_Inspection lift_annual_inspection){
        try {
            insEquipmentLiftAnnualInsectionService.insert(lift_annual_inspection);
            return Result.success("system.success");
        }catch (Exception e){
            return Result.error("system.error");
        }
    }

    //校验电梯号
    @At("/checkLiftCode/?")
    @Ok("json:full")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object checkLiftCode(String liftCode){
        Cnd cnd = Cnd.NEW();
        //获取所有电梯编号
        boolean flag = insEquipmentLiftAnnualInsectionService.checkLiftCode(liftCode,cnd);
        if (flag){
            return Result.success();
        }
        return  Result.error("电梯设备统一编号重复");
    }

    //批量删除
    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object delete(@Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                insEquipmentLiftAnnualInsectionService.delete(ids);
            }
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    //跳转修改页面
    @At("/edit/?")
    @Ok("beetl:/platform/asset/safety/lift/edit.html")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object edit(Integer id) {
        Ins_Equipment_Lift_Annual_Inspection liftAnnualInspection = insEquipmentLiftAnnualInsectionService.getEquipmentLiftEditData(id);
        return liftAnnualInspection;
    }

    //修改提交
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object editDo(@Param("..") Ins_Equipment_Lift_Annual_Inspection liftAnnualInspection,HttpServletRequest req){
        try {
            insEquipmentLiftAnnualInsectionService.updateIgnoreNull(liftAnnualInspection);
            return Result.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return  Result.error("修改失败");
        }
    }

    //模板下载
    @At("/templateDownload")
    @Ok("raw")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object templateDownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //得到下载的文件
        File templateFile = TemplateDownUtils.getTemplateFile("liftAnnualInspection_template", "template", response);
        return templateFile;
    }

    //导入功能
    @At
    @Ok("json:full")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload/assets"})
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public Object upload(@Param("inspectionCycleFile") File file,HttpServletRequest request,HttpServletResponse response) throws Exception  {
        Result result = Result.error("导入失败");
        List<ExcelImportResult<Ins_Equipment_Lift_Annual_Inspection>> importFile = importService.importFile(request, file, "ins_equipment_lift_annual_inspection");
        List<Ins_Equipment_Lift_Annual_Inspection> failList = importFile.get(0).getFailList();
        if (failList.size() == 0){
            //获取数据集
            List<Ins_Equipment_Lift_Annual_Inspection> importFileList = importFile.get(0).getList();
            //excel非空校验
            if (importFileList.size() == 0){
                return Result.error("模板中数据为空,请填写数据后再进行导入!");
            }

            //校验excel中数据重复问题
            StringBuilder sb = new StringBuilder();
            StringBuilder sbCheckNo = new StringBuilder();
            StringBuilder sbLiftNo = new StringBuilder();
            if(importFileList.size() >0){
                for (int i = 0; i < importFileList.size(); i++) {

                    String checkNo = importFileList.get(i).getCheckNo();
                    String liftCode = importFileList.get(i).getLiftCode();
                    String floorNo = importFileList.get(i).getFloorNo();
                    String liftNo = importFileList.get(i).getLiftNo();
                    String chargePerson = importFileList.get(i).getChargePerson();

                    // 如果不存在返回 -1
                    if(sbCheckNo.indexOf(checkNo+",") > -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + checkNo + ":检测号重复!</br>");
                    }else {
                        sbCheckNo.append(checkNo).append(",");
                    }
                    if(sbLiftNo.indexOf(liftCode+",") > -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + liftCode + ":统一编号重复!</br>");
                    }else {
                        sbLiftNo.append(liftCode).append(",");
                    }

                    //校验楼号、电梯号、负责人是否存在
                    Sys_dict dict = sysDictService.fetch(Cnd.where("name", "=", floorNo));
                    Sys_dict dict1 = sysDictService.fetch(Cnd.where("name", "=", liftNo));
                    Sys_user user = sysUserService.fetch(Cnd.where("username", "=", chargePerson));

                        if (dict != null && !"".equals(dict)){
                            importFileList.get(i).setFloorNo(dict.getId());
                        }else {
                            sb.append("第" + (i+1) + "行," + "楼号:" + floorNo + "不存在,请检查!</br>");
                        }
                        if (dict1 != null && !"".equals(dict1)){
                            importFileList.get(i).setLiftNo(dict1.getId());
                        }else {
                            sb.append("第" + (i+1) + "行," + "电梯号:" + liftNo + "不存在,请检查!</br>");
                        }
                        if (user != null && !"".equals(user)){
                            importFileList.get(i).setChargePerson(user.getId());
                        }else {
                            sb.append("第" + (i+1) + "行," + "负责人" + "不存在,请检查!</br>");
                        }
                }
            }

            //校验数据重复问题(数据库重复)
            for (int i = 0; i < importFileList.size(); i++) {
                String tempLiftCode = importFileList.get(i).getLiftCode();
                String tempCheckNo = importFileList.get(i).getCheckNo();

                int countTempCheckNo = dao.count(Ins_Equipment_Lift_Annual_Inspection.class, Cnd.where("checkNo","=",tempCheckNo.trim()));
                int countTempLiftCode = dao.count(Ins_Equipment_Lift_Annual_Inspection.class, Cnd.where("liftCode", "=", tempLiftCode.trim()));
                if (countTempLiftCode > 0){
                    sb.append("第" + (i+1) + "行," + "统一编号" + tempLiftCode + "已经存在!</br>");
                }
                if (tempLiftCode == null || "".equals(tempLiftCode)){
                    sb.append("第" + (i+1) +"行," + "统一编号为空!</br>");
                }
                if (countTempCheckNo > 0){
                    sb.append("第" + (i+1) + "行," + "检测号" + tempCheckNo + "已经存在!</br>");
                }
                if (tempCheckNo == null || "".equals(tempCheckNo)){
                    sb.append("第" + (i+1) +"行," + "检测号为空!</br>");
                }
            }
            response.setContentType("text/html");
            if (sb.length() != 0){
                return Result.error(sb.toString());
            } else {
                if (importFileList.size() > 0 && !importFileList.isEmpty()) {
                    for (Ins_Equipment_Lift_Annual_Inspection liftAnnualInspection : importFileList) {

                        if (liftAnnualInspection != null) {
                            insEquipmentLiftAnnualInsectionService.insert(liftAnnualInspection);
                        }
                    }
                }
            }
        }

        if(!failList.isEmpty() || failList.size() >0 ){
            return result;
        }

        response.setContentType("text/html");
        result = Result.success("导入成功");
        return result;
    }


    //批量导出
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public  String exportInspectionCycleInfo(@Param("..")Ins_Equipment_Lift_Annual_Inspection liftAnnualInspection, HttpServletRequest request, HttpServletResponse response,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        NutMap re = insEquipmentLiftAnnualInsectionService.getEquipmentLiftData(liftAnnualInspection, 100000, 0, 10000, order, columns, null, null );
        List<Ins_Equipment_Lift_Annual_Inspection> liftAnnualInspectionList = (List<Ins_Equipment_Lift_Annual_Inspection>) re.get("data");
        List<Class<?>> voList = new ArrayList<>();
        Class<?> forName = Ins_Equipment_Lift_Annual_Inspection.class;
        voList.add(forName);
        String url = exportService.exportFile(request,response,voList,"Ins_Equipment_Lift_Annual_Inspection",liftAnnualInspectionList);
        return url;
    }

    @At
    @Ok("void")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public void exportInspectionCycleInfoByUrl(@Param("url") String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取文件名
        String fileName = Configer.getInstance().getProperty("Ins_Equipment_Lift_Annual_Inspection");
        //根据URL找到路径下载返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file, fileName, response, true);

    }

}
