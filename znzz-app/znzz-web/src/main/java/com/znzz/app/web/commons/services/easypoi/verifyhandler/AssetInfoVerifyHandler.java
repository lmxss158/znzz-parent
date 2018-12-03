package com.znzz.app.web.commons.services.easypoi.verifyhandler;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.sys.modules.services.SysUserService;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import org.nutz.lang.Strings;

import java.util.Date;
import java.util.UUID;

@IocBean
public class AssetInfoVerifyHandler implements IExcelVerifyHandler<Ins_Assets> {

    @Inject
    SysUnitService unitService;
    @Inject
    SysUserService userService;
    @Inject
    InsProjectService projectService;
    @Inject
    InsAssetsService assetsService;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(Ins_Assets assets) {

        //进行数据校验
        ExcelVerifyHandlerResult verifyResult = new ExcelVerifyHandlerResult();
        verifyResult.setSuccess(true);
        if((assets != null) && Strings.isBlank(assets.getAssetCode())){
        	return verifyResult;
        }
        //非空校验
        checkNull(assets, verifyResult);

        //使用单位和责任人都为空
        if (Strings.isBlank(assets.getBorrowDepart()) && Strings.isBlank(assets.getChargePerson())) {
            assets.setBorrowDepart("KF_001");
        } else {
            //变更导入的使用单位 && 责任人
            Sys_unit unit = unitService.fetch(Cnd.where("name", "=", assets.getBorrowDepart()));
            String orgDepartName = assets.getBorrowDepart();
            if (unit != null) { // 单位存在设置id
                assets.setBorrowDepart(unit.getId());
                //责任人替换
                Sys_user user = userService.fetch(Cnd.where("username", "=", assets.getChargePerson()).and("unitid", "=", unit.getId()));
                if (user != null) {
                    assets.setChargePerson(user.getId());
                } else {
                    Sys_user userByAllUsers = userService.fetch(Cnd.where("username", "=", assets.getChargePerson()));
                    verifyResult.setSuccess(false);
                    if (userByAllUsers == null) {
                    	//如果是设备的话可以没有责任人
                    	if( Strings.isBlank(assets.getChargePerson()) && (assets.getAssetType() == 1)){
                    		verifyResult.setSuccess(true);
                    	}else{
                    		if(Strings.isBlank(assets.getChargePerson())){
                    			verifyResult.setMsg("责任人为空");
                    		}else{
                    			verifyResult.setMsg(assets.getChargePerson() + " 责任人不存在");
                    		}
                    	}
                    } else {
                    	Sys_unit sysUnit = unitService.fetch(Cnd.where("id", "=", userByAllUsers.getUnitid()));
                    	String departName = "";
                    	if(sysUnit != null){
                    		departName = sysUnit.getName();
                    	}
                        verifyResult.setMsg("责任人 : " + assets.getChargePerson() + "存在,但在 " + departName + " 不在 " + orgDepartName + " 单位下");
                    }
                }
            } else {
                verifyResult.setMsg(orgDepartName + " 使用单位不存在");
                verifyResult.setSuccess(false);
            }
        }

        //项目名称 替换
        if (Strings.isNotBlank(assets.getProjectName())) {
            Ins_ProjectInfo projectInfo = projectService.fetch(Cnd.where("name","=",assets.getProjectName()).and("type","=","0"));
            if (projectInfo != null){//数据库当中有此信息
                assets.setProjectName(projectInfo.getCode());
            } else {//项目名称不存在(插入数据库)
                projectInfo = new Ins_ProjectInfo();
                projectInfo.setName(assets.getProjectName());
                String code = UUID.randomUUID().toString();
                projectInfo.setCode(code);
                projectInfo.setCreateTime(new Date());
                assets.setProjectName(code);
                //JG - 0(技改项目)
                projectInfo.setType("0");
                projectService.insert(projectInfo);
            }
        }


        return verifyResult;


    }



    /**
     * 非空校验
     *
     * @param assets
     * @param verifyResult
     */
    private void checkNull(Ins_Assets assets, ExcelVerifyHandlerResult verifyResult) {
        if (assets.getAssetCode() == null || "".equals(assets.getAssetCode())) {
            verifyResult.setMsg("统一编号为空");
            verifyResult.setSuccess(false);
        }
        if (assets.getAssetType() == null) {
            verifyResult.setMsg("类别为空");
            verifyResult.setSuccess(false);
        }
        if (assets.getAssetName() == null || "".equals(assets.getAssetName())) {
            verifyResult.setMsg("名称为空");
            verifyResult.setSuccess(false);
        }
    }

}
