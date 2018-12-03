package com.znzz.app.web.modules.controllers.platform.asset.safety.container;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.Ins_Equipment_Container_Annual_Inspection;
import com.znzz.app.asset.safety.InsEquipmentContainerAnnualInspectionService;
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
@At("/asset/safety/container")
public class InsEquipmentContainerAnnualInspectionController {
    @Inject
    private InsEquipmentContainerAnnualInspectionService containerAnnualInspectionService;
    @Inject
    private SysDictService sysDictService;
    @Inject
    private SysUserService sysUserService;
    @Inject
    private ImportService importService;
    @Inject
    private ExportService exportService;
    @Inject
    private static Ioc ioc_my = Mvcs.ctx().getDefaultIoc();
    Dao dao = ioc_my.get(Dao.class);


    //容器设备年检信息列表页面
    @At("")
    @Ok("beetl:/platform/asset/safety/container/index.html")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public void index() {

    }

    //获取容器设备年检信息
    @At
    @Ok("json:full")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object containerAnnualInspectionData(@Param("..") Ins_Equipment_Container_Annual_Inspection containerAnnualInspection, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        //获取容器设备年检信息
        return containerAnnualInspectionService.getContainerAnnualInspectionData(containerAnnualInspection,length,start,draw,order,columns,null,null);

    }

    //跳转容器设备添加页面
    @At
    @Ok("beetl:/platform/asset/safety/container/adda.html")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public void addAnnualInspection(HttpServletRequest request) {
        Cnd cnd = Cnd.NEW();
        List<Sys_dict> floors = sysDictService.query(cnd.where("code", "like", "%floorNo%").and("parentId", "<>", ""));
        List<Sys_dict> rooms = sysDictService.query(cnd.where("code", "like", "%roomNo%").and("parentId", "<>", ""));
        request.setAttribute("floors", floors);
        request.setAttribute("rooms", rooms);
    }

    //新建容器设备年检记录
    @At
    @Ok("json")
    @SLog(tag = "新建容器设备年检记录",msg = "容器设备统一编号:${args[0].containerCode}")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object addAnnualInspectionDo(@Param("..")Ins_Equipment_Container_Annual_Inspection containerAnnualInspection){
        try {
            containerAnnualInspectionService.insert(containerAnnualInspection);
            return Result.success("system.success");
        }catch (Exception e){
            return Result.error("system.error");
        }
    }

    //校验容器号
    @At("/checkContainerCode/?")
    @Ok("json:full")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object checkContainerCode(String containerCode){
        Cnd cnd = Cnd.NEW();
        //获取所有的容器编号
        boolean flag = containerAnnualInspectionService.checkContainerCode(containerCode,cnd);
        if (flag){
            return Result.success();
        }
        return Result.error("容器设备统一编号重复");
    }

    //跳转修改页面
    @At("/editAnnualInspection/?")
    @Ok("beetl:/platform/asset/safety/container/edita.html")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object editAnnualInspection(Integer id){
        Ins_Equipment_Container_Annual_Inspection annualInspectionEditData = containerAnnualInspectionService.getAnnualInspectionEditData(id);
        return annualInspectionEditData;
    }

    //修改提交
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object editAnnualInspectionDo(@Param("..")Ins_Equipment_Container_Annual_Inspection containerAnnualInspection, HttpServletRequest request){
        try {
            containerAnnualInspectionService.updateIgnoreNull(containerAnnualInspection);
            return Result.success("修改成功");
        }catch (Exception e){
            return Result.error("修改失败");
        }
    }

    //批量删除
    @At({"/deleteAnnualInspection","/deleteAnnualInspection/?"})
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object deleteAnnualInspection(@Param("ids") String[] ids, HttpServletRequest request){
        try {
            if (ids != null && ids.length > 0){
                containerAnnualInspectionService.delete(ids);
            }
            return Result.success("system.success");
        }catch (Exception e){
            return Result.error("system.error");
        }
    }

    //模板下载
    @At("/containerAnnualInspectionDownload")
    @Ok("raw")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object containerAnnualInspectionDownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //得到下载的文件
        File templateFile = TemplateDownUtils.getTemplateFile("containerAnnualInspection_template","template",response);

        return templateFile;
    }

    //导入功能
    @At
    @Ok("json:full")
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload/assets"})
    @RequiresPermissions("safety.production.equipment.container.certification")
    public Object annualUpload(@Param("containerAnnualInspectionFile") File file, HttpServletRequest request, HttpServletResponse response)throws Exception{
        Result result = Result.error("导入失败");
        List<ExcelImportResult<Ins_Equipment_Container_Annual_Inspection>> importFile = importService.importFile(request, file, "ins_equipment_container_annual_inspection");
        List<Ins_Equipment_Container_Annual_Inspection> failList = importFile.get(0).getFailList();
        if (failList.size() ==0){
            //获取数据集
            List<Ins_Equipment_Container_Annual_Inspection> importFileList = importFile.get(0).getList();
            //EXCEL非空校验
            if (importFileList.size() == 0){
                return Result.error("模板中数据为空,请填写数据后再进行导入!");
            }

            //校验excel中数据重复问题
            StringBuilder sb = new StringBuilder();
            StringBuilder sbCheckNo = new StringBuilder();
            StringBuilder sbContainerCode = new StringBuilder();
            if(importFileList.size() >0){
                for (int i = 0; i < importFileList.size(); i++) {

                    String checkNo = importFileList.get(i).getCheckNo();
                    String containerCode = importFileList.get(i).getContainerCode();
                    String floorNo = importFileList.get(i).getFloorNo();
                    String roomNo = importFileList.get(i).getRoomNo();
                    String chargePerson = importFileList.get(i).getChargePerson();

                    // 如果不存在返回 -1
                    if(sbCheckNo.indexOf(checkNo+",") > -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + checkNo + ":检测号重复!</br>");
                    }else {
                        sbCheckNo.append(checkNo).append(",");
                    }
                    if(sbContainerCode.indexOf(containerCode+",")> -1){
                        sb.append("EXCEL表中第" + (i + 1) + "行," + containerCode + ":统一编号重复!</br>");
                    }else {
                        sbContainerCode.append(containerCode).append(",");
                    }

                    //校验楼号、房间号、负责人是否存在
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
                String tempContainerCode = importFileList.get(0).getContainerCode();
                String tempCheckNo = importFileList.get(0).getCheckNo();
                int countTempContainerCode = dao.count(Ins_Equipment_Container_Annual_Inspection.class, Cnd.where("containerCode", "=", tempContainerCode));
                int countTempCheckNo = dao.count(Ins_Equipment_Container_Annual_Inspection.class, Cnd.where("checkNo", "=", tempCheckNo));
                if (countTempContainerCode > 0){
                    sb.append("第" + (i+1) + "行," + "统一编号" + tempContainerCode + "已经存在!</br>");
                }
                if (countTempCheckNo > 0){
                    sb.append("第" + (i+1) + "行," + "检测号" + tempCheckNo + "已经存在!</br>");
                }
                if (tempCheckNo == null && "".equals(tempCheckNo)){
                    sb.append("第" + (i+1) + "行," + "统一编号为空!</br>");
                }
                if (tempContainerCode == null && "".equals(tempContainerCode)){
                    sb.append("第" + (i+1) + "行," + "检测号为空!</br>");
                }
            }
            response.setContentType("text/html");
            if (sb.length() != 0){
                return Result.error(sb.toString());
            } else {
                if (importFileList.size() >0 && !importFileList.isEmpty()){
                    for (Ins_Equipment_Container_Annual_Inspection containerAnnualInspection : importFileList) {
                        if (containerAnnualInspection != null){
                            containerAnnualInspectionService.insert(containerAnnualInspection);
                        }
                    }
                }
            }
        }
        if (!failList.isEmpty() || failList.size() > 0){
            return result;
        }

        response.setContentType("text/html");
        result = Result.success("导入成功");
        return  result ;
    }

    //批量导出
    @At
    @Ok("json")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public String exportContainerAnnualInspection(@Param("..")Ins_Equipment_Container_Annual_Inspection containerAnnualInspection, HttpServletRequest request, HttpServletResponse response,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {
        NutMap re = containerAnnualInspectionService.getContainerAnnualInspectionData(containerAnnualInspection,100000,0,10000,order,columns,null,null);
        List<Ins_Equipment_Container_Annual_Inspection> containerAnnualInspectionList = (List<Ins_Equipment_Container_Annual_Inspection>) re.get("data");
        List<Class<?>> voList = new ArrayList<>();
        Class<?> forName = Ins_Equipment_Container_Annual_Inspection.class;
        voList.add(forName);
        String url = exportService.exportFile(request,response,voList,"Ins_Equipment_Container_Annual_Inspection",containerAnnualInspectionList);
        return url;
    }

    @At
    @Ok("void")
    @RequiresPermissions("safety.production.equipment.container.certification")
    public void exportContainerAnnualInspectionByUrl(@Param("url") String url, HttpServletRequest request,HttpServletResponse response) throws IOException{
        //获取文件名
        String fileName = Configer.getInstance().getProperty("Ins_Equipment_Container_Annual_Inspection");
        //根据URL找到路径下载返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file,fileName,response,true);
    }

}