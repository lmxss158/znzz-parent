package com.znzz.app.web.modules.controllers.platform.asset;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.*;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.instrument.modules.service.InsDeviceInfoService;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.app.sys.modules.models.Sys_config;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysConfigService;
import com.znzz.app.sys.modules.services.SysExportColumnService;
import com.znzz.app.sys.modules.services.SysExportTableService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.*;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsScrapRecordVo;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsSealedRecordVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.ShiroUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p> Title:InsAssets.java </p>
 * <p> Description:资产管理模块controller </p>
 * <p> Company: www.htfudao.com.cn </p>
 *
 * @author ChangZheng
 * @version V1.0
 * @Package com.znzz.app.web.modules.controllers.platform.asset
 * @date 2017年8月21日 下午1:46:53
 */
@IocBean
@At("/asset/info")
public class InsAssetsController {

    private static final Log log = Logs.get();
    private static final Logger log2 = Logger.getLogger("scadaLogger");    //scada日志
    private static final String CONFIGKEY = (String) Configer.getInstance().get("assetCheckNums");

    @Inject
    InsAssetsService assetsService;
    @Inject
    InsAssertRuleService ruleService;
    @Inject
    InsDeviceInfoService deviceInfoService;
    @Inject
    private InsAssetMouthReportService assetMouthReportService;
    @Inject
    private InsProjectService projectService;
    @Inject
    private InsAssetLendRecordService lendRecordService;
    @Inject
    InsAssetUnitService assetUnitService;
    @Inject
    private SysConfigService configService;
    @Inject
    ShiroUtil shiroUtil;
    @Inject
    ExportService exportService;
    @Inject
    SysExportColumnService exportColumnService;
    @Inject
    SysExportTableService exportTableService;
    @Inject
    ImportService importService;
    @Inject
    private Dao dao;

    //错误信息
    Map<String, Object> resultMap = new HashMap<String, Object>();
    private String errors = "0";

    // 首页
    @At("")
    @Ok("beetl:/platform/asset/info/index.html")
    @RequiresPermissions("asset.auth.info")
    public void index(HttpServletRequest request) {
        // 给页面定义该变量
        request.setAttribute("errors", "0");
    }

    // 数据项
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object data(@Param("..") AssetsForm assets, @Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return assetsService.getAssetsDataWith(assets, length, start, draw, order, columns, null, null, null);
    }

    // 跳转到资产增加页面
    @At
    @Ok("beetl:/platform/asset/info/add.html")
    @RequiresPermissions("asset.auth.info")
    public void add() {

    }

    // 录入资产信息
    @At("/addDo/**")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info.add")
    @SLog(tag = "录入资产信息", msg = "资产信息编号:${args[0].assetCode}")
    public Object addDo(@Param("..") Ins_Assets assets, @Param("..") Ins_Asset_Rule rule, @Param("..") Ins_DeviceInfo deviceInfo, HttpServletRequest request) {

        try {
            //ins_asset_info表
            // 是否借出(0是/1否)
            assets.setIsLend(1);
            // 是否过期(0是/1否)
            assets.setIsOverdue(1);
            // 是否链接云网(0是/1否)
            assets.setIsConnectCloud(1);
            // 默认是库房(使用单位)
            assets.setBorrowDepart("KF_001");
            //录入时间
            assets.setCreateTime(new Date());
            //生成条形码图片信息
            String barCodeInfo = InsExcel.makeBarcode(assets.getAssetCode(), null);
            assets.setBarCodeImage(barCodeInfo);

            // ins_device_info
            // 设置资产统一编号
            deviceInfo.setDeviceCode(assets.getAssetCode());
            // 设置设备名称
            deviceInfo.setDeviceName(rule.getAssetName());
            // 使用单位和责任人
            deviceInfo.setBorrowDepart(assets.getBorrowDepart());
            deviceInfo.setChargePerson(assets.getChargePerson());
            // 是否在外场(0否/1是)
            deviceInfo.setOutField(0);

            // ins_asset_version
            //处理型号
            handDeviceVersion(assets, rule);
            //插入assetInfo & deviceInfo
            assetsService.insert(assets);
            deviceInfoService.insert(deviceInfo);
            return Result.success("资产信息录入成功");
        } catch (Exception e) {
            log.error("资产信息录入失败");
            e.printStackTrace();
        }
        return Result.error("资产信息录入失败");
    }

    //处理型号
    private void handDeviceVersion(Ins_Assets assets, Ins_Asset_Rule rule) {
        //型号为空  或者  型号不存在
        //型号为空

        if (Strings.isBlank(rule.getDeviceVersion())) {
            String findDeviceVersion = assets.getAssetCode() + "(" + assets.getAssetName() + ")";
            boolean flag = assetsService.existDeviceVersion(findDeviceVersion);
            if (!flag) {
                rule.setDeviceVersionOrg(findDeviceVersion);
                assets.setDeviceVersion(findDeviceVersion);
                rule.setCreateTime(new Date());
                ruleService.insert(rule);
            }

        } else if (!assetsService.existDeviceVersion(rule.getDeviceVersion())) {//数据库不存在
            rule.setDeviceVersionOrg(assets.getDeviceVersion());
            rule.setCreateTime(new Date());
            ruleService.insert(rule);
        }
    }


    // 检查资产编号是否存在
    @At("/checkAssetCode/?")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object checkAssetCode(@Param("assetCode") String assetCode) {
        boolean flag = assetsService.getAssetCode(assetCode);
        // 数据库中没有assetCode
        if (flag) {
            return Result.success("可以插入");
        }
        return Result.error("资产编号:" + assetCode + "已经存在");
    }

    // 检查资产编号是否存在
    @At("/checkAssetCodeEdit")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object checkAssetCodeEdit(@Param("assetCode") String assetCode, @Param("id") String id) {
        String message = assetsService.getAssetCodeList(assetCode, id);
        // 数据库中没有assetCode
        if ("0".equals(message)) {
            return Result.success("可以修改");
        } else if ("1".equals(message)) {
            return Result.error("资产编号:" + assetCode + "已经存在,请更换!");
        }
        return Result.error("资产编号:" + assetCode + "可能重复,请检查相关数据.");
    }

    // 检查型号是否存在
    @At("/checkDeviceVersion/**")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object checkDeviceVersion(@Param("deviceVersion") String deviceVersion, HttpServletRequest request) {

        //型号不为空
        List<Ins_Asset_Rule> list = ruleService.getDeviceVersionList(deviceVersion);
        // 数据库中有此型号
        if (list.size() > 0) {
            Ins_Asset_Rule rule = list.get(0);
            String imageCode = new ImageBinaryUtil().getImageBinary(rule.getUrlImage());
            //如果有图片,进行设置
            if (Strings.isNotBlank(imageCode)) {
                rule.setUrlImage(imageCode);
            } else {
                rule.setUrlImage("");
            }
            //返回型号信息
            return rule;
        } else {
            return new Ins_Asset_Rule();
        }

    }

    @At("/edit/?")
    @Ok("beetl:/platform/asset/info/edit.html")
    @RequiresPermissions("asset.auth.info.edit")
    public Object edit(Integer id) {
        // 获得资产信息
        AssetsForm assetsForm = assetsService.getAssetAndRuleInfo(id);

        // 将图片进行转化处理
        String code = new ImageBinaryUtil().getImageBinary(assetsForm.getUrlImage());
        if (null != code) {
            assetsForm.setUrlImage(code);
        } else {
            assetsForm.setUrlImage("");
        }
        // 查看该资产是否有关联
        List<String> assetCodeList = assetMouthReportService.assetCodeList(null, null);
        // 是否有绑定
        boolean flag = assetsService.checkBind(assetsForm.getAssetCode());

        if (assetCodeList.contains(assetsForm.getAssetCode()) || flag) { // 有关联 没有绑定
            assetsForm.setEditState(0);    // 这个设备跟其他记录有关联
        } else {
            assetsForm.setEditState(1);
        }

        return assetsForm;
    }

    @At
    @Ok("json")
    @RequiresPermissions("asset.auth.info.edit")
    @SLog(tag = "编辑资产信息", msg = "资产编号名称:${args[0].assetCode}")
    public Object editDo(@Param("..") Ins_Assets assets, @Param("..") Ins_Asset_Rule rule, HttpServletRequest req) {
        try {
            //修改型号
            handDeviceVersion(assets, rule);
            String oldCode = assets.getOldCode();
            String assetCode = assets.getAssetCode();
            String chargePerson = assets.getChargePerson();
            String borrowDepart = assets.getBorrowDepart();
            String oldAssetCode = assetsService.getDeviceCode(assets.getId());
            // 更新资产表(必须先更新表,再更新使用单位和责任人的信息)
            assetsService.updateIgnoreNull(assets);
            //往位置变更信息表里插入一条数据
            assetUnitService.insert(null);
            // 更新device_info表
            String deviceCode = assets.getAssetCode();
            //根据原始id查找到原始deviceInfo
            Ins_DeviceInfo deviceInfo = deviceInfoService.getDeviceInfo(oldAssetCode);
            //给deviceInfo属性赋值
            deviceInfo.setDeviceCode(deviceCode);
            deviceInfo.setAssetType(assets.getAssetType());
            deviceInfo.setDeviceVersion(assets.getDeviceVersion());
            //在规格型号表里获取到deviceName
            String deviceName = ruleService.getDeviceName(assets.getDeviceVersion());
            deviceInfo.setDeviceName(deviceName);

            deviceInfoService.updateIgnoreNull(deviceInfo);
            // 修改device_info表里的使用单位和责任人
            assetsService.updateChargePersonAndBorrowDepart(assetCode, borrowDepart, chargePerson);

            //判断维修
            if (assets.getRepairState() != null && assets.getRepairState() == 0) {
                review(assetCode, "维修中");
            }

            // 校验统一编号重复
            int num = assetsService.count(Cnd.where("assetCode", "=", assetCode).and("id", "<>", assets.getId()));
            if (num == 0) {
                // 修改统一编号:Add at 20181008
                assetCodeUpdate(assetCode, oldCode);
            } else {
                return Result.error("修改后的统一编号与库中有重复,请重新输入!");
            }

            return Result.success("操作成功");
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }

    /**
     * 修改统一编号
     *
     * @param assetCode
     * @param oldCode
     */
    private void assetCodeUpdate(String assetCode, String oldCode) {
        try {
            // 先判断是否有改变
            if (!assetCode.equals(oldCode)) {
                int num = 0;
                for (String assetTable : Globals.ASSET_LIST) {
                    int num1 = dao.update(assetTable, Chain.make("assetCode", assetCode),
                            Cnd.where("assetCode", "=", oldCode));
                    num = num + num1;
                }
                for (String deviceTable : Globals.DEVICE_LIST) {
                    int num2 = dao.update(deviceTable, Chain.make("deviceCode", assetCode),
                            Cnd.where("deviceCode", "=", oldCode));
                    num = num + num2;
                }

                int num3 = dao.update(Globals.YUN_GATEWAY, Chain.make("device_code", assetCode), Cnd.where("device_code", "=", oldCode));
                System.out.println(num + num3);//改变了的行数
            }

        } catch (Exception e) {
            log.error("Error when update of assets info..", e);
        }
    }

    @At("/getinfo")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object getInfo(@Param("assetCode") String assetCode, HttpServletRequest re) {
        // 获得资产信息
        @SuppressWarnings("rawtypes")
        Map map = assetsService.getAssetAndRuleInfo(assetCode);
        return map;

    }

    @At("/detail/**")
    @Ok("beetl:/platform/asset/info/detail.html")
    @RequiresPermissions("asset.auth.info")
    public Object detail(@Param("id") String idOrassetCode, HttpServletRequest req) {

        try {
            Integer id = Integer.parseInt(idOrassetCode);
            AssetsForm assetsForm = assetsService.getAssetAndRuleInfoDetail(id, null);
            // 图片转成流的形式
            String code = new ImageBinaryUtil().getImageBinary(assetsForm.getUrlImage());
            assetsForm.setUrlImage(code);
            return assetsForm;
        } catch (Exception e) {
            AssetsForm assetsForm = assetsService.getAssetAndRuleInfoDetail(null, idOrassetCode);
            // 图片转成流的形式
            String code = new ImageBinaryUtil().getImageBinary(assetsForm.getUrlImage());
            assetsForm.setUrlImage(code);
            return assetsForm;
        }
    }


    //保存自定义表格参数
    @At("/saveCheckNums/?")
    @Ok("json")
    @RequiresPermissions("asset.auth.info")
    public Object saveCheckNums(@Param("hideArray") String hideArray) {
        //全部显示
        exportColumnService.update(Chain.make("is_column_hidden", "0"), Cnd.where("table_id", "=", "12"));
        if ("isNull".equals(hideArray)) {
            hideArray = null;
        } else {
            //1隐藏 0不隐藏
            String[] hideToExportTable = hideArray.split(",");
            exportColumnService.update(Chain.make("is_column_hidden", "1"), Cnd.where("table_id", "=", "12").and("order_num", "in", hideToExportTable));

        }

        Sys_config config = configService.fetch(Cnd.where("configkey", "=", CONFIGKEY).and("userId", "=", getUserId()));
        if (config == null) {
            Sys_config configNew = new Sys_config();
            configNew.setConfigKey(CONFIGKEY);
            configNew.setConfigValue(hideArray);
            configNew.setNote("资产自定义表格属性");
            configNew.setUserId(getUserId());
            configService.insert(configNew);
            return Result.success(configNew.getConfigValue());
        } else {
            config.setConfigValue(hideArray);
            configService.update(Chain.make("configValue", hideArray), Cnd.where("userId", "=", getUserId()).and("configkey", "=", CONFIGKEY));
            return Result.success(hideArray);
        }


    }

    @At("/initSelectCheck/?")
    @Ok("json")
    @RequiresPermissions("asset.auth.info")
    public Object initSelectCheck(Integer id) {
        Sys_config config = configService.fetch(Cnd.where("configkey", "=", CONFIGKEY).and("userId", "=", getUserId()));
        if (config != null) {
            return Result.success(config.getConfigValue());
        }
        return Result.success("success");
    }

    /**
     * @param assetsForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @At
    @Ok("json")
    @RequiresPermissions("asset.auth.recode.export")
    public String exportAssetsInfo(@Param("..") AssetsForm assetsForm, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<AssetsForm> list = assetsService.getExportList(assetsForm);
        List<Class<?>> voList = new ArrayList<Class<?>>();
        Class<?> forName = AssetsForm.class;
        voList.add(forName);
        String url = exportService.exportFile(request, response, voList, "ins_asset_info", list);

        return url;

    }


    @At
    @Ok("void")
    @RequiresPermissions("asset.auth.recode.export")
    public void exportAssetsInfoByUrl(@Param("url") String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取文件名
        String filename = Configer.getInstance().getProperty("export_asset_name");
        //根据URL找到路径下载,返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file, filename, response, true);
    }

    /**
     * 封存
     *
     * @param ids
     * @return
     */
    @At("/sealed")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.sealed")
    public Object sealed(@Param("ids") Integer[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                assetsService.sealed(ids);
                List<Ins_Assets> assets = assetsService.query(Cnd.where("id", "in", ids));
                String message = "封存";
                for (Ins_Assets asset : assets) {
                    //取消预约
                    review(asset.getAssetCode(), message);
                }
                return Result.success("封存成功 ");
            } else {
                return Result.error("封存失败");
            }
        } catch (Exception e) {
            return Result.error("封存失败");
        }
    }


    /**
     * 在用
     *
     * @param ids
     * @return
     */
    @At("/unsealed")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.unsealed")
    public Object unsealed(@Param("ids") Integer[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                assetsService.unsealed(ids);
                /*List<Ins_Assets> assets = assetsService.query(Cnd.where("id","in",ids));
                String message = "闲置";
                for (Ins_Assets asset : assets) {
                    //取消预约
                    review(asset.getAssetCode(),message);
                }*/
                return Result.success("在用成功");
            } else {
                return Result.error("在用失败");
            }
        } catch (Exception e) {
            return Result.error("在用失败");
        }
    }

    @At("/scrap")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.scrap")
    public Object scrap(@Param("ids") Integer[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                assetsService.scrap(ids);
                return Result.success("报废成功");
            } else {
                return Result.error("报废失败");
            }
        } catch (Exception e) {
            return Result.error("报废失败");
        }
    }

    @At("/confirmScrap")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.confirmScrap")
    public Object confirmScrap(@Param("ids") Integer[] ids) {
        try {
            if (ids != null && ids.length > 0) {
                assetsService.confirmScrap(ids);
                List<Ins_Assets> assets = assetsService.query(Cnd.where("id", "in", ids));
                String message = "确认报废";
                for (Ins_Assets asset : assets) {
                    //取消预约
                    review(asset.getAssetCode(), message);
                }
                return Result.success("确认报废成功");
            } else {
                return Result.error("确认报废失败");
            }
        } catch (Exception e) {
            return Result.error("确认报废失败");
        }
    }

    @At("/sealedRecord")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object sealedRecord(@Param("..") AssetsSealedRecordVo vo) {
        return assetsService.getSealRecordList(vo);
    }

    @At("/scrapRecord")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object scrapRecord(@Param("..") AssetsScrapRecordVo vo) {
        return assetsService.getScrapRecordList(vo);
    }

    // 下载模板
    @At("/templateDownload")
    @Ok("raw")
    @RequiresPermissions("asset.auth.info")
    public Object templateDownload(HttpServletRequest request, HttpServletResponse response, @Param("type") Integer type) throws UnsupportedEncodingException {
        // 得到下载的文件
        File templateFile = null;
        if (null != type && 2 == type) {//履历填充模板
            templateFile = TemplateDownUtils.getTemplateFile("resume_template", "template", response);
        } else {
            templateFile = TemplateDownUtils.getTemplateFile("asset_template", "template", response);
        }
        return templateFile;
    }

    //预约提醒
    @At({"/review", "/review/?"})
    @Ok("void")
    @RequiresPermissions("asset.auth.info")
    public void review(@Param("assetCode") String assetCode, @Param("message") String message) {
        String userId = getUserId();
        //isOrder == 1
        //预约表进行小环闭合(借出)
        ApplyUtils applyUtils = new ApplyUtils();
        //查资产表里的字段信息进行判断(str1 = 1 被占用 str = 2 可以借)
        Dao dao = Mvcs.ctx().getDefaultIoc().get(Dao.class);
        applyUtils.cancelAccordForbidHandAsset(assetCode, applyUtils.pureDate(), dao, message);
        /*applyUtils.insertMessageInfo(assetCode,dao,message,userId);*/
        //applyUtils.pureDate();
    }

    //闲置
    @At({"/unUsed", "/unUsed/?"})
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object unUsed(@Param("ids") Integer[] ids, @Param("message") String message) {
        try {
            if (ids != null && ids.length > 0) {
                List<Ins_Assets> assets = assetsService.query(Cnd.where("id", "in", ids));
                String message2 = "闲置";
                for (Ins_Assets asset : assets) {
                    //取消预约
                    review(asset.getAssetCode(), message2);
                }
                assetsService.unUsed(ids, message2);
                return Result.success("闲置成功 ");
            } else {
                return Result.error("闲置失败");
            }
        } catch (Exception e) {
            return Result.error("闲置失败");
        }
    }

    //禁用
    @At({"/forbidden", "/forbidden/?"})
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object forbidden(@Param("ids") Integer[] ids, @Param("message") String message) {

        try {
            if (ids != null && ids.length > 0) {
                List<Ins_Assets> assets = assetsService.query(Cnd.where("id", "in", ids));
                String message2 = "禁用";
                for (Ins_Assets asset : assets) {
                    //取消预约
                    review(asset.getAssetCode(), message2);
                }
                assetsService.forbidden(ids, message2);
                return Result.success("禁用成功 ");
            } else {
                return Result.error("禁用失败");
            }
        } catch (Exception e) {
            return Result.error("闲置失败");
        }
    }

    // 批量导入
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload/assets"})
    public Object upload(@Param("assetFile") File file, HttpServletRequest request, HttpServletResponse response, @Param("saveOrUpdate") String saveOrUpdate) throws Exception {

        if (file == null) {
            response.setContentType("text/html");
            return Result.error("导入错误，文件为空");
        }

        List<ExcelImportResult<Ins_Assets>> assetsImportFile = importService.importFile(request, file, "ins_asset_info_import");

        NutMap map = assetsService.getImportInfo(assetsImportFile, saveOrUpdate, getUserId());

        //进行校验
        Result result = Result.error();
        if (map.get("errorMsg") != null || assetsImportFile.get(0).getFailList().size() > 0) {
        	StringBuilder sbMsg = new StringBuilder();
            response.setContentType("text/html");
            if(map.get("errorMsg") != null){
            	sbMsg.append((String) map.get("errorMsg"));
            }
            for (Ins_Assets assets : assetsImportFile.get(0).getFailList()) {
            	int rowNum  = assets.getRowNum() + 1;
            	sbMsg.append("第" + rowNum + "行, " + assets.getErrorMsg() + "<br/>");
            }
            result.addMsg(sbMsg.toString());
            return result;


        } else {//没有错误进行导入
            List<Ins_Assets> assetsList = map.getList("assetInfo", Ins_Assets.class);
            List<Ins_DeviceInfo> deviceList = map.getList("deviceInfo", Ins_DeviceInfo.class);
            List<Ins_Asset_Rule> ruleList = map.getList("ruleInfo", Ins_Asset_Rule.class);
            List<Ins_Asset_lend_record> lendRecordList = map.getList("lendRecordInfo", Ins_Asset_lend_record.class);
            List<Ins_Asset_Unit> unitList = map.getList("unitInfo", Ins_Asset_Unit.class);
            List<Ins_ProjectInfo> projectList = map.getList("projectInfo", Ins_ProjectInfo.class);


            if (!"update".equals(saveOrUpdate)) {//插入
                Trans.exec(new Atom() {
                    @Override
                    public void run() {
                        assetsService.insertList(assetsList);
                        ruleService.insertList(ruleList);
                        deviceInfoService.insertList(deviceList);
                        //借入借出记录插入
                        lendRecordService.insertList(lendRecordList);
                        //科室间借调
                        assetUnitService.insertList(unitList);
                    }
                });

                /*//项目
                projectService.insertList(projectList);*/

            } else { //更新
                Trans.exec(new Atom() {
                    @Override
                    public void run() {
                        assetsService.updateList(assetsList);
                        ruleService.updateList(ruleList);
                        deviceInfoService.updateList(deviceList);
                    }
                });
            }


            response.setContentType("text/html");
            return Result.success("操作成功");
        }

    }


    /**
     * 批量修改
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload/assetsUpdate"})
    public Object uploadWithUpdate(@Param("updateAssetFile") File file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //批量修改不修改 资产单位借调记录 项借出记录
        return upload(file, request, response, "update");
    }


    /**
     * 履历导入,完成填充返回
     *
     * @param file
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    @At
    @Ok("raw")
    @RequiresPermissions("asset.auth.info")
    @AdaptBy(type = UploadAdaptor.class, args = {"${app.root}/WEB-INF/upload/assets"})
    public Object resumeUpload(@Param("resumeFile") File file, HttpServletRequest request, HttpServletResponse response, ViewModel model) throws UnsupportedEncodingException {
        try {
            // 获取文件的绝对路径
            String path = file.getAbsolutePath();
            ReadExcelUtil excelUtil = new ReadExcelUtil();
            // 将excel表格中的数据存放到map当中,错误信息也在map当中
            // excelUtil.readResumeExcelAndFillIt(path);
            // 错误信息,每次先清空,再赋值~
            resultMap.clear();
            resultMap = (Map<String, Object>) excelUtil.readResumeExcelAndFillIt(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 告诉浏览器要下载文件
        String str = "";
        try {
            str = new String("仪器仪表履历.xlsx".getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + str);
        // 告诉浏览器要下载文件而不是直接打开文件
        response.setContentType("application/-download");
        response.setCharacterEncoding("UTF-8");
        return file;
    }

    /**
     * 履历填充错误信息提示
     *
     * @returnahh
     */
    @At("/errorMessages")
    @Ok("json:full")
    public Object returnsErrorMsg() {
        // 先清理,再赋值
        errors = "";
        if (resultMap.containsKey("error")) {
            errors = resultMap.get("error").toString();
        }
        return errors;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    private String getUserId() {
        Sys_user user = (Sys_user) shiroUtil.getPrincipal();
        return user.getId();
    }

}
