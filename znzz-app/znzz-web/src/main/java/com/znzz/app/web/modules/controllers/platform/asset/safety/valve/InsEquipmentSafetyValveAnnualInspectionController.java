package com.znzz.app.web.modules.controllers.platform.asset.safety.valve;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.Ins_Equipment_Lift_Annual_Inspection;
import com.znzz.app.asset.moudules.models.Ins_Equipment_Safety_Valve_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentSafetyValveAnnualInspectionService;
import com.znzz.app.sys.modules.models.Sys_dict;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysDictService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
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
@At("/asset/safety/valve")
public class InsEquipmentSafetyValveAnnualInspectionController {
    @Inject
    private InsEquipmentSafetyValveAnnualInspectionService valveAnnualInspectionService;
    @Inject
    SysDictService sysDictService;
    @Inject
    SysUserService sysUserService;
    @Inject
    ImportService importService;
    @Inject
    ExportService exportService;
    @Inject
    private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
    Dao dao = ioc_my.get(Dao.class);

    //安全阀设备年检信息列表页面
    @At("")
    @Ok("beetl:/platform/asset/safety/valve/index.html")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public void index() {

    }

    //获取安全阀设备年检信息
    @At
    @Ok("json:full")
    public Object data(@Param("..") Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        //获取安全阀设备年检信息
        return valveAnnualInspectionService.getEquipmentValveData(valveAnnualInspection, length, start, draw, order, columns,null, null);
    }

    //跳转到安全阀添加页面
    @At
    @Ok("beetl:/platform/asset/safety/valve/add.html")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public void add(HttpServletRequest request) {
        Cnd cnd = Cnd.NEW();
        //查询楼号、房间号
        List<Sys_dict> floors = sysDictService.query(cnd.where("code", "like", "%floorNo%").and("parentId", "<>", ""));
        List<Sys_dict> rooms = sysDictService.query(cnd.where("code", "like", "%roomNo%").and("parentId", "<>", ""));
        request.setAttribute("floors", floors);
        request.setAttribute("rooms", rooms);
    }

    //新建安全阀设备年检记录
    @At
    @Ok("json")
    // @SLog(tag = "新建安全阀设备年检记录",msg = "安全阀设备统一编号:${arg[0].safetyValveCode}")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object addDo(@Param("..") Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection) {
        try {

            StringBuilder sb = new StringBuilder();
            valveAnnualInspection.setSafetyValveSite(sb.append(sysDictService.fetch(valveAnnualInspection.getFloorNo()).getName()).append("-").append(sysDictService.fetch(valveAnnualInspection.getRoomNo()).getName()).toString());
            valveAnnualInspectionService.insert(valveAnnualInspection);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    //校验安全阀统一编号
    @At("/checkValveCode/?")
    @Ok("json:full")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object checkValveCode(@Param("safetyValveCode") String safetyValveCode) {
        Cnd cnd = Cnd.NEW();
        boolean flag = valveAnnualInspectionService.checkValveCode(safetyValveCode, cnd);
        if (flag) {
            return Result.success("system.success");
        }
        return Result.error("统一编号重复");
    }

    //跳转修改页面
    @At("/edit/?")
    @Ok("beetl:/platform/asset/safety/valve/edit.html")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object edit(Integer id) {
        Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection = valveAnnualInspectionService.getEquipmentValveEditData(id);
        return valveAnnualInspection;
    }

    //修改提交
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object editDo(@Param("..") Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection, HttpServletRequest req) {
        try {
            valveAnnualInspectionService.updateIgnoreNull(valveAnnualInspection);
            return Result.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败");
        }
    }

    //批量删除
    @At({"/delete","/delete/?"})
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object delete(@Param("ids") String[] ids, HttpServletRequest request){
        try {
            if ( ids != null && ids.length > 0){
                valveAnnualInspectionService.delete(ids);
            }
            return Result.success("system.success");
        }catch (Exception e){
            return Result.error("system.error");
        }
    }

    //模板下载
    @At("/templateDownload")
    @Ok("raw")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object templateDownLoad(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //得到下载的文件
        File templateFile = TemplateDownUtils.getTemplateFile("safetyValveAnnualnspection_template", "template", response);
        return templateFile;
    }

    //导入功能
    @At
    @Ok("json:full")
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload/assets"})
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public Object upload(@Param("inspectionCycleFile") File file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result = Result.error("导入失败");
        List<ExcelImportResult<Ins_Equipment_Safety_Valve_Annual_Inspection>> importFile = importService.importFile(request, file, "ins_equipment_safety_valve_annual_inspection");
        List<Ins_Equipment_Safety_Valve_Annual_Inspection> failList = importFile.get(0).getFailList();
        if (failList.size() == 0) {
            //获取数据集
            List<Ins_Equipment_Safety_Valve_Annual_Inspection> importFileList = importFile.get(0).getList();

            //excel非空校验
            if (importFileList.size() == 0) {
                return Result.error("模板中数据为空,请填写数据后再进行导入!");
            }

            //校验excel中数据重复问题
            StringBuilder sb = new StringBuilder();
            StringBuilder sbCheckNo = new StringBuilder();
            StringBuilder sbSafetyValveCode = new StringBuilder();
            if(importFileList.size() >0){
                for (int i = 0; i < importFileList.size(); i++) {

                    String checkNo = importFileList.get(i).getCheckNo();
                    String safetyValveCode = importFileList.get(i).getSafetyValveCode();
                    String floorNo = importFileList.get(i).getFloorNo();
                    String roomNo = importFileList.get(i).getRoomNo();
                    String chargePerson = importFileList.get(i).getChargePerson();

                    // 如果不存在返回 -1
                    if(sbCheckNo.indexOf(checkNo+",") > -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + checkNo + ":检测号重复!</br>");
                    }else {
                        sbCheckNo.append(checkNo).append(",");
                    }
                    if(sbSafetyValveCode.indexOf(safetyValveCode+",")> -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + safetyValveCode + ":统一编号重复!</br>");
                    }else {
                        sbSafetyValveCode.append(safetyValveCode).append(",");
                    }

                    //校验楼号、安全阀统一编号、负责人是否存在
                    Sys_dict dict = sysDictService.fetch(Cnd.where("name", "=", floorNo));
                    Sys_dict dict1 = sysDictService.fetch(Cnd.where("name", "=", roomNo));
                    Sys_user user = sysUserService.fetch(Cnd.where("username", "=", chargePerson));

                    if (dict != null && !"".equals(dict)){
                        importFileList.get(i).setFloorNo(dict.getId());
                    }else {
                        sb.append("第" + (i+1) + "行," + "楼号:" + floorNo + "不存在,请检查!</br>");
                    }
                    if (dict1 != null && !"".equals(dict1)){
                        importFileList.get(i).setRoomNo(dict1.getId());
                    }else {
                        sb.append("第" + (i+1) + "行," + "房间号:" + roomNo + "不存在,请检查!</br>");
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
                String tempSafetyValveCode = importFileList.get(i).getSafetyValveCode();
                String tempCheckNo = importFileList.get(i).getCheckNo();

                int countTempcheckNo = dao.count(Ins_Equipment_Safety_Valve_Annual_Inspection.class, Cnd.where("checkNo", "=", tempCheckNo.trim()));
                int countTempSafetyValveCode = dao.count(Ins_Equipment_Safety_Valve_Annual_Inspection.class, Cnd.where("safetyValveCode", "=", tempSafetyValveCode.trim()));
                if (tempCheckNo == null || "".equals(tempCheckNo)) {
                    sb.append("第" + (i + 1) + "行," + "检测号为空!</br>");
                }
                if (tempSafetyValveCode == null || "".equals(tempSafetyValveCode)) {
                    sb.append("第" + (i + 1) + "行," + "统一编号为空!</br>");
                }
                if (countTempcheckNo > 0) {
                    sb.append("第" + (i + 1) + "行," + "检测号" + tempCheckNo + "在数据库中已经存在!</br>");
                }
                if (countTempSafetyValveCode > 0) {
                    sb.append("第" + (i + 1) + "行," + "统一编号" + tempSafetyValveCode + "在数据库中已经存在!</br>");
                }
            }
            response.setContentType("text/html");
            if (sb.length() != 0) {
                return Result.error(sb.toString());
            } else {
                if (importFileList.size() > 0 && !importFileList.isEmpty()) {
                    for (Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection : importFileList) {
                        if (valveAnnualInspection != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            valveAnnualInspection.setSafetyValveSite(stringBuilder.append(sysDictService.fetch(valveAnnualInspection.getFloorNo()).getName()).append("-").append(sysDictService.fetch(valveAnnualInspection.getRoomNo()).getName()).toString());
                            valveAnnualInspectionService.insert(valveAnnualInspection);
                        }
                    }
                }
            }
        }

        if (!failList.isEmpty() || failList.size() > 0) {
            return result;
        }
        response.setContentType("text/html");
        result = Result.success("导入成功");
        return result;
    }

    //批量导出
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.valve.certification")
    public String exportInspectionCycleInfo(@Param("..") Ins_Equipment_Safety_Valve_Annual_Inspection valveAnnualInspection, HttpServletRequest request, HttpServletResponse response, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        NutMap re = valveAnnualInspectionService.getEquipmentValveData(valveAnnualInspection, 100000, 0, 10000, order, columns,null, null);
        List<Ins_Equipment_Safety_Valve_Annual_Inspection> valveAnnualInspectionList = (List<Ins_Equipment_Safety_Valve_Annual_Inspection>) re.get("data");
        List<Class<?>> voList = new ArrayList<>();
        Class<?> forName = Ins_Equipment_Safety_Valve_Annual_Inspection.class;
        voList.add(forName);
        String url = exportService.exportFile(request, response, voList, "Ins_Equipment_Safety_Valve_Annual_Inspection",valveAnnualInspectionList);
        return url;
    }

    @At
    @Ok("void")
    @RequiresPermissions("safety.production.equipment.lift.certification")
    public void exportInspectionCycleInfoByUrl(@Param("url") String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取文件名
        String fileName = Configer.getInstance().getProperty("Ins_Equipment_Safety_Valve_Annual_Inspection");
        //根据URL找到路径下载返回给前台
        DownloadUtil downloadUtil = new DownloadUtil();
        File file = new File(url);
        downloadUtil.prototypeDownload(file, fileName, response, true);
    }
}
