package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.znzz.app.asset.moudules.models.*;
import com.znzz.app.asset.moudules.services.apply.ApplyAssetService;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.framework.util.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.InsExcel;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsLend;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.StringUtil;


/**
 * @author fengguoxin
 * @ClassName: InsAssetLendRecordController
 * @Description: (仪器室设备借调记录)
 * @date 2017年8月23日 下午3:19:40
 */
@IocBean
@At("/asset/recode")
public class InsAssetLendRecordController {
    //private static final Log log = Logs.get();

    @Inject
    InsAssetLendRecordService lendRecordService;
    @Inject
    InsAssetsService insAssetsService;
    @Inject
    private SysUnitService sysUnitService;
    @Inject
    private SysUserService userService;
    @Inject
    ExportService exportService;
    @Inject
    ShiroUtil shiroUtil;
    @Inject
    ApplyAssetService applyAssetService;
    // 首页
    @At("")
    @Ok("beetl:/platform/asset/recode/index.html")
    @RequiresPermissions("asset.auth.recode")
    public void index() {
        System.out.println("进入首页");
    }

    /**
     * 借出记录列表
     */

    @At
    @Ok("json:full")
    /* @RequiresPermissions("asset.auth.recode.data")*/
    public Object data(@Param("code") String code, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        cnd.and("assetCode", "=", code);
        NutMap lendData = lendRecordService.data(length, start, draw, order, columns, cnd, null);

        return lendData;
    }


    /**
     * 使用databale组件，返回空表
     */
    @At("/showRecode1")
    @Ok("json:full")
    public Object showRecode1(@Param("assetCodeArray") String assetCodeArray, @Param("length") int length,
                              @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        NutMap re = new NutMap();
        if (Strings.isNotBlank(assetCodeArray)) {
            re = lendRecordService.getReturnRecord(assetCodeArray, length, start, draw, order, columns);
        } else {
            setVoidList(length, draw, re);
        }

        return re;
    }

    @At("/showReturnIndex")
    @Ok("json:full")
    public Object showReturnIndex(@Param("assetCodeArray") String assetCodeArray, @Param("length") int length,
                                  @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        NutMap re = new NutMap();
        if (Strings.isNotBlank(assetCodeArray)) {
            re = lendRecordService.getLendRecord(assetCodeArray, length, start, draw, order, columns);
        } else {
            setVoidList(length, draw, re);
        }

        return re;
    }

    private void setVoidList(@Param("length") int length, @Param("draw") int draw, NutMap re) {
        List<AssetsLend> list = new ArrayList<>();
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
    }

    /**
     * 依据统一编码查询借调记录
     */
    @At("/showLendRecode/")
    @Ok("json:full")
    public Object showLendRecode(@Param("assetCode") String assetCode) {
        Cnd cnd = Cnd.NEW();
        AssetsLend assetsLend = new AssetsLend();
        if (!Strings.isBlank(assetCode)) {
            cnd.and("assetCode", "=", assetCode);
            Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
            if (null == ins_Assets) {
                return null;
            } else {
                String deviceVersion = ins_Assets.getDeviceVersion();
                Ins_Asset_Rule ins_Asset_Rule = lendRecordService.fetchIns_Asset_Rule(deviceVersion);
                assetsLend.setAssetCode(assetCode);
                assetsLend.setAssetName(ins_Asset_Rule.getAssetName());
                assetsLend.setDeviceVersion(deviceVersion);
                assetsLend.setGgName(ins_Assets.getGgName());
                // Ins_Asset_lend_record newReturnInfo =
                // lendRecordService.getNewReturnInfo(assetCode);

                assetsLend.setReturnDate(ins_Assets.getReturnDate());
                assetsLend.setReturnPerson(ins_Assets.getReturnPerson());
                assetsLend.setSerialNumber(ins_Assets.getSerialNumber());
                // 如果使用日期为空
                if (Strings.isBlank(ins_Assets.getBorrowDepart())) {
                    assetsLend.setActionType("-1");
                } else {
                    assetsLend.setActionType("" + ins_Assets.getIsLend());
                }
                // 待报废与报废的资产返回值类型定位(待报废：10 报废：11)
                if (ins_Assets.getScrapState() != null) {
                    if (ins_Assets.getScrapState() == 0) {
                        assetsLend.setActionType("10");
                    } else if (ins_Assets.getScrapState() == 1) {
                        assetsLend.setActionType("11");
                    }
                }
                // 封存的资产不允许借用(封存：12)
                if (ins_Assets.getManageStatus() != null) {
                    if (ins_Assets.getManageStatus() == 1) {
                        assetsLend.setActionType("12");
                    }
                }
                // 用于区分资产类型
                assetsLend.setAssetType(ins_Assets.getAssetType());
            }
        }
        return assetsLend;
    }

    @At("/showReturnRecode/")
    @Ok("json:full")
    public Object showReturnRecode(@Param("assetCode") String assetCode) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isBlank(assetCode) && "" != assetCode) {
            cnd.and("assetCode", "=", assetCode);
            Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
            if (null == ins_Assets) {
                return null;
            } else {
                String deviceVersion = ins_Assets.getDeviceVersion();
                Ins_Asset_Rule ins_Asset_Rule = lendRecordService.fetchIns_Asset_Rule(deviceVersion);
                AssetsLend assetsLend = new AssetsLend();
                assetsLend.setAssetCode(assetCode);
                assetsLend.setAssetName(ins_Asset_Rule.getAssetName());
                assetsLend.setDeviceVersion(deviceVersion);
                assetsLend.setGgName(ins_Assets.getGgName());
                Ins_Asset_lend_record newReturnInfo = lendRecordService.getNewReturnInfo(assetCode);
                if (null != newReturnInfo) {
                    Cnd cnd1 = Cnd.NEW();
                    cnd1.and("name", "=", newReturnInfo.getApplyDepart());
                    // String applyDepartId = sysUnitService.fetch(cnd1).getId();
                    String applyDepartId = sysUnitService.fetch(cnd1) == null ? "" : sysUnitService.fetch(cnd1).getId();
                    Cnd cnd2 = Cnd.NEW();
                    cnd2.and("username", "=", newReturnInfo.getApplyPerson());
                    String applyPersonId = userService.fetch(cnd2) == null ? "" : userService.fetch(cnd2).getId();
                    assetsLend.setApplyDepartment(applyDepartId);
                    assetsLend.setApplyPerson(applyPersonId);
                }
                assetsLend.setReturnDate(ins_Assets.getReturnDate());
                assetsLend.setReturnPerson(ins_Assets.getReturnPerson());
                assetsLend.setSerialNumber(ins_Assets.getSerialNumber());
                assetsLend.setActionType("" + ins_Assets.getIsLend());
                assetsLend.setRemark(ins_Assets.getRemark());
                return assetsLend;
            }
        }
        return null;
    }


    /**
     * 跳转归还
     */
    @At({"/returnRecode", "/returnRecode/?"})
    @Ok("beetl:/platform/asset/recodeLendAndReturnAndTransferAccounts/return.html")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object returnRecode(@Param("ids") String ids) {

        Subject s = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) s.getPrincipal();
        if (Strings.isNotBlank(ids)) {
            user.setAssetCodeArray(ids);
        } else {
            user.setAssetCodeArray(null);
        }
        return user;

    }


    /**
     * 跳转借出
     */
    @At({"/lendRecode", "/lendRecode/?"})
    @Ok("beetl:/platform/asset/recodeLendAndReturnAndTransferAccounts/lend.html")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object lendRecode(@Param("ids") String ids) {
        Subject s = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) s.getPrincipal();
        if (Strings.isNotBlank(ids)) {
            user.setAssetCodeArray(ids);
        } else {
            user.setAssetCodeArray(null);
        }
        return user;

    }

    /**
     * 跳转转账
     * @param id
     * @return
     */
    @At({"/transferAccounts/?"})
    @Ok("beetl:/platform/asset/recodeLendAndReturnAndTransferAccounts/transferAccounts.html")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object transferAccounts(@Param("id") String id){
        Subject s = SecurityUtils.getSubject();
        Sys_user user = (Sys_user) s.getPrincipal();

        Ins_Assets insAssets = insAssetsService.fetch(Cnd.where("assetCode","=",id));
        insAssets.setUnit(user.getUnit().getName());
        insAssets.setUserName(user.getUsername());
        insAssets.setUserId(user.getId());
        if (null != insAssets.getBorrowDepart() && !"".equals(insAssets.getBorrowDepart())) {
            insAssets.setBorrowDepart(sysUnitService.fetch(insAssets.getBorrowDepart()).getName());
        }
        if (null != insAssets.getChargePerson() && !"".equals(insAssets.getChargePerson())) {
            insAssets.setChargePerson(userService.fetch(insAssets.getChargePerson()).getUsername());
        }
        return insAssets;
    }

    /**
     *加载转账资产信息
     * @param assetCodeArray
     * @param length
     * @param start
     * @param draw
     * @param order
     * @param columns
     * @return
     */
    @At("/showTransferAccountsInfo")
    @Ok("json:full")
    public Object showTransferAccountsInfo(@Param("assetCodeArray") String assetCodeArray,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        NutMap re = new NutMap();
        if (Strings.isNotBlank(assetCodeArray)){
            re = lendRecordService.getReturnRecord(assetCodeArray, length, start, draw, order, columns);
        }else {
            setVoidList(length,draw,re);
        }
        return re;
    }



    /**
     * 增加仪器设备借出记录
     */
    @At("/addLendRecode/")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object addLendRecode(@Param("..") Ins_Asset_lend_record asset_lend_record, @Param("handlePerson") String handlePerson, @Param("code_return_list_lend") String code_lend_list, HttpServletRequest request) {
        try {

            String[] codes1 = code_lend_list.split(",");
            String applyDepartCode = asset_lend_record.getApplyDepart();
            String applyPersonCode = asset_lend_record.getApplyPerson();
            ArrayList<String> list = new ArrayList<>();
            for (String code : codes1) {
                if (!"".equals(code) && null != code) {
                    list.add(code);
                }
            }
            String applyDepart = sysUnitService.fetch(applyDepartCode).getName();
            String applyPerson = "";
            if (Strings.isNotBlank(applyPersonCode)){
                applyPerson = userService.fetch(applyPersonCode).getUsername();
            }
            for (String code : list) {

                Cnd cnd = Cnd.NEW();
                cnd.and("assetCode", "=", code);
                Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
                if (null == ins_Assets) {
                    return Result.error("请输入正确的资产编号");
                }else if(ins_Assets.getAssetType() == 2 && Strings.isBlank(applyPerson)){//0全部1设备2仪器3工量具
                    return Result.error("仪器类型必须得有使用人");
                } else {
                    //
                    Ins_Asset_lend_record asset_lend_record2 = new Ins_Asset_lend_record();
                    asset_lend_record2.setActionType(0);//借出
                    asset_lend_record2.setApplyDepart(applyDepart);
                    asset_lend_record2.setApplyPerson(applyPerson);
                    asset_lend_record2.setAssetCode(code);
                    asset_lend_record2.setHandlePerson(asset_lend_record.getHandlePerson());
                    asset_lend_record2.setOprateTime(asset_lend_record.getOprateTime());
                    asset_lend_record2.setOriginalDepart(asset_lend_record.getOriginalDepart());
                    asset_lend_record2.setRemark(asset_lend_record.getRemark());
                    asset_lend_record2.setStr1(asset_lend_record.getStr1());
                    //插入借出记录
                    lendRecordService.insert(asset_lend_record2);
                    //插入unit表借出记录
                    Ins_Asset_Unit ins_Asset_Unit = new Ins_Asset_Unit();
                    ins_Asset_Unit.setAssetCode(code);
                    ins_Asset_Unit.setChargeDepart(applyDepartCode);
                    ins_Asset_Unit.setChargeMan(applyPersonCode);
                    ins_Asset_Unit.setUserDepart(applyDepartCode);
                    ins_Asset_Unit.setUserMan(applyPersonCode);
                    ins_Asset_Unit.setPosition(applyDepart);


                    Integer isConnectCloud = ins_Assets.getIsConnectCloud();
                    if (isConnectCloud == null) {
                        ins_Asset_Unit.setRate("--");
                    } else {
                        ins_Asset_Unit.setRate(isConnectCloud == 0 ? "0%" : "--");
                    }
                    ins_Asset_Unit.setStatus(0);
                    ins_Asset_Unit.setOperateTime(asset_lend_record.getOprateTime());
                    lendRecordService.insertAssetRecodeToUnit(ins_Asset_Unit);


                    //设置借出日期，责任人，单位，
                    ins_Assets.setLendDate(asset_lend_record.getOprateTime());
                    insAssetsService.updateChargePersonAndBorrowDepart(code, applyDepartCode, applyPersonCode);
                    ins_Assets.setBorrowDepart(applyDepartCode);
                    ins_Assets.setChargePerson(applyPersonCode);
                    //设置归还日期归还人为null
                    ins_Assets.setReturnDate(null);
                    ins_Assets.setReturnPerson(null);
                    //设置借出办理人
                    ins_Assets.setHandlePerson(handlePerson);
                    //更该借出
                    ins_Assets.setIsLend(0);
                    // 借出->无归还时间
                    ins_Assets.setReturnDate(null);
                    insAssetsService.update(ins_Assets);

                    //预约表进行小环闭合(借出)
                    ApplyUtils applyUtils = new ApplyUtils();
                    Sys_user user = (Sys_user) shiroUtil.getPrincipal();
                    //查资产表里的字段信息进行判断(str1 = 1 被占用 str = 2 可以借)
                    applyAssetService.fetch(Cnd.where("assetCode","=",""));
                    Dao dao = Mvcs.ctx().getDefaultIoc().get(Dao.class);
                    applyUtils.cancelAccordForbidHandAsset(ins_Assets.getAssetCode(), applyUtils.pureDate(), dao, "借出");
                    //applyUtils.insertMessageInfo(ins_Assets.getAssetCode(),dao,"借出",user.getId());
                }


                /*//预约表进行小环闭合(借出)
                ApplyUtils applyUtils = new ApplyUtils();
                //查资产表里的字段信息进行判断(str1 = 1 被占用 str = 2 可以借)

                applyUtils.cancelAccordForbid(code,new Date(),dao,"借出");*/

            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    /**
     * 增加仪器设备归还记录
     */
    @At("/addReturnRecode/")
    @Ok("json")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object addReturnRecode(@Param("..") Ins_Asset_lend_record asset_lend_record, @Param("code_return_list") String code_return_list, HttpServletRequest request) {
        try {


            String applyDepartCode = asset_lend_record.getApplyDepart();
            String applyPersonCode = asset_lend_record.getApplyPerson();
            String[] codes = code_return_list.split(",");
            ArrayList<String> listCode = new ArrayList<>();


            for (String code : codes) {
                if (!"".equals(code) && null != code) {
                    listCode.add(code);
                }
            }
            Sys_unit sys_unit = sysUnitService.fetch(applyDepartCode);
            Sys_unit sys_unit2 = sysUnitService.fetch(Cnd.where("name", "=", "库房"));
            String kufangcode = sys_unit2.getUnitcode();
            //判断是否是设备
            String applyPerson = "";
            if (Strings.isNotBlank(applyPersonCode)){
                applyPerson = userService.fetch(applyPersonCode).getUsername();
            }

            String applyDepart = sys_unit.getName();
            for (String code : listCode) {
                Ins_Asset_lend_record asset_lend_record2 = new Ins_Asset_lend_record();
                asset_lend_record2.setActionType(1);
                asset_lend_record2.setApplyDepart(applyDepart);
                asset_lend_record2.setApplyPerson(applyPerson);
                asset_lend_record2.setAssetCode(code);
                asset_lend_record2.setHandlePerson(asset_lend_record.getHandlePerson());
                asset_lend_record2.setOprateTime(asset_lend_record.getOprateTime());
                asset_lend_record2.setOriginalDepart(asset_lend_record.getOriginalDepart());
                asset_lend_record2.setRemark(asset_lend_record.getRemark());
                asset_lend_record2.setStr1(asset_lend_record.getStr1());
                //批量查出归还记录
                lendRecordService.insert(asset_lend_record2);

                Ins_Asset_Unit ins_Asset_Unit = new Ins_Asset_Unit();
                ins_Asset_Unit.setAssetCode(code);
                lendRecordService.deleteAssetRecodeToUnit(code);
                Cnd cnd = Cnd.NEW();
                cnd.and("assetCode", "=", code);
                Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
                if (null == ins_Assets) {
                    return Result.error("请输入正确的仪器编号");
                } else {

                    ins_Assets.setIsLend(1);
                    //设置归还日期，办理人，归还人
                    //清空 ，单位     ，借出日期不清空
                    ins_Assets.setBorrowDepart(kufangcode);
                    //设置责任人为库房
                    ins_Assets.setChargePerson(null);
                    ins_Assets.setReturnDate(asset_lend_record.getOprateTime());
                    ins_Assets.setHandlePerson(asset_lend_record.getHandlePerson());
                    ins_Assets.setReturnPerson(applyPerson);
                    // 归还->无借出时间
                    ins_Assets.setLendDate(null);
                    insAssetsService.update(ins_Assets);

                }

            }
        } catch (Exception e) {
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    //增加仪器设备转账记录
    @At("/addTransferAccountsRecord/")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.recode.lendAndReturn")
    public Object excuteTransfer(@Param("..") Ins_Asset_lend_record lendRecord, @Param("assetCode") String assetCode){
        try {
            //使用单位、使用人、办理人字符串
            String applyPersonCode = lendRecord.getApplyPerson();
            String applyDepartCode = lendRecord.getApplyDepart();
            String handlePersonCode = lendRecord.getHandlePerson();
            //将使用单位、使用人、办理人转为名字
            String applyDepartName = sysUnitService.fetch(applyDepartCode).getName();

            //String applyPersonName = userService.fetch(applyPersonCode).getUsername();
            Sys_user fetch = userService.fetch(applyPersonCode);
            if (fetch !=null ){
                String applyPersonName = fetch.getUsername();
                lendRecord.setApplyPerson(applyPersonName);
            }
            String handlePersonName = userService.fetch(handlePersonCode).getUsername();
            lendRecord.setApplyDepart(applyDepartName);

            lendRecord.setHandlePerson(handlePersonName);

            // 记录入库
            lendRecordService.insert(lendRecord);
            // 修改Ins_device_info 单位和人
            insAssetsService.updateChargePersonAndBorrowDepart(assetCode, applyDepartCode, applyPersonCode);
            // 修改台账表
            insAssetsService.update(Chain.make("borrowDepart", applyDepartCode).add("chargePerson", applyPersonCode),
                    Cnd.where("assetCode", "=", assetCode));

        } catch (Exception e) {
            return Result.error("system.error");
        }
        return Result.success("system.success");
    }

    /**
     * 导出条形码
     */
    @SuppressWarnings("unused")
    @At
    @Ok("json")
    @RequiresPermissions("asset.auth.recode.export")
    public Object export(@Param("..") AssetsForm assetsForm, HttpServletResponse response, HttpServletRequest request) throws Exception {

        List<AssetsForm> list = insAssetsService.getExportList(assetsForm);
        List<Class<?>> voList= new ArrayList<Class<?>>();
        Class<?> forName = AssetsForm.class;
        voList.add(forName);
        String url = exportService.exportFile(request, response, voList, "ins_asset_info_barCode", list);
        return url;
    }

    /**
     * 生成条形码图片
     * 暂时不用
     * @param list
     */
    @Deprecated
    private void genrateBarCode(List<AssetsForm> list) {
        for (AssetsForm form : list) {
            if (Strings.isBlank(form.getBarCodeImage())){
                String barUrl = InsExcel.makeBarcode(form.getAssetCode(),null);
                insAssetsService.update(Chain.make("barCodeImage",barUrl),Cnd.where("assetCode","=",form.getAssetCode()));
            }
        }
    }

    //条形码下载
    @At
    @Ok("void")
    @RequiresPermissions("asset.auth.recode.export")
    public void downloadBarCode(@Param("url") String url, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        //获取文件名
        String filename = Configer.getInstance().getProperty("barCode_name");
        //根据URL找到路径下载,返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file, filename, response, true);

    }

    @At("/edit/?")
    @Ok("beetl:/platform/asset/recode/edit.html")
    @RequiresPermissions("asset.auth.recod")
    public Object edit(Integer id) {
        Ins_Asset_lend_record fetch = lendRecordService.fetch(id);
        return fetch;
    }

    @At
    @Ok("json")
    @RequiresPermissions("asset.auth.recod.edit")
    //@SLog(tag = "编辑记录", msg = "网关名称:${args[0].gatewayName}")
    public Object editDo(@Param("..") Ins_Asset_lend_record asset_lend_record, HttpServletRequest req) {
        try {
            asset_lend_record.setOpBy(StringUtil.getUid());
            asset_lend_record.setOpAt((int) (System.currentTimeMillis() / 1000));
            lendRecordService.updateIgnoreNull(asset_lend_record);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions("asset.auth.recode.delete")
    public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                lendRecordService.delete(ids);
                req.setAttribute("id", StringUtils.toString(ids));
            } else {
                lendRecordService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @SuppressWarnings("unused")
    @At()
    @Ok("json:full")
    @RequiresPermissions("asset.auth.recode")
    public Object search(@Param("assetCode") String assetCode, @Param("originalDepart") String originalDepart, @Param("applyPerson") String applyPerson, @Param("applyDepart") String applyDepart, @Param("handlePerson") String handlePerson, @Param("actionType") String actionType, @Param("oprateTime") String oprateTime, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        String[] list = oprateTime.split("-");
        for (String string : list) {
            System.out.println(string.trim());
        }
        Cnd cnd = Cnd.NEW();
        String linkName = null;
        NutMap recodeData = lendRecordService.data(length, start, draw, order, columns, cnd, linkName);

        Cnd subCnd = null;
        NutMap nutMap = lendRecordService.data(length, start, draw, order, columns, cnd, linkName, subCnd);

        return recodeData;
    }

}
