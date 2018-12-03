package com.znzz.app.web.commons.base;

import com.znzz.app.sys.modules.models.Sys_config;
import com.znzz.app.sys.modules.models.Sys_route;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局属性.
 */
public class Globals {
    //项目路径
    public static String AppRoot = "";
    //项目目录
    public static String AppBase = "";
    //项目名称
    public static String AppName = "仪器设备管理系统";
    //项目短名称
    public static String AppShrotName = "IBMS";
    //项目域名
    public static String AppDomain = "127.0.0.1";
    //文件上传路径
    public static String AppUploadPath = "/upload";
    // 是否启用了队列
    public static boolean RabbitMQEnabled = false;
    //系统自定义参数
    public static Map<String, String> MyConfig = new HashMap<>();
    //自定义路由
    public static Map<String, Sys_route> RouteMap = new HashMap<>();
    //模糊匹配删除所有key
    public static final String KEYMODEL = "BASE*";
    //sacard数据 key
    public static final String SCARDDATA = "BASE:DEVICE:STR";
    //生产编号与设备统一编号key
    public static final String ORIGNDEVICECODE = "BASE:ORIGN:DEVICECODE:STR";
    //维修各个数量
    public static final String REPAIRCOUNT = "BASE:REPAIR:COUNT:STR";
    //科室借调要审批的数量
    public static final String ASSETUNITLENDCOUNT = "BASE:ASSETUNIT:COUNT:HASH";
    //未分配时长名称
    public static final String UNDURATION = "未分配时长";
    //状态数据读取
    public static final String GETALLSTATEMESSAGE = "GetAllStateMessage";
    //设置A卡
    public static final String SETACARD = "SetACard";
    //批量设置A卡
    public static final String SETACARDBATCH = "SetACardBatch";
    //设置B卡
    public static final String SETBCARD = "SetBCard";
    //批量设置B卡
    public static final String SETBCARDBATCH = "SetBCardBatch";
    //更新A卡
    public static final String UPDATEACARD = "UpdateACard";
    //更新B卡 
    public static final String UPDATEBCARD = "UpdateBCard";
    //删除A卡
    public static final String DELETEACARD = "DeleteACard";
    //批量删除A卡
    public static final String DELETEACARDBATCH = "DeleteACardBatch";
    //删除B卡
    public static final String DELETEBCARD = "DeleteBCard";
    //批量删除b卡
    public static final String DELETEBCARDBATCH = "DeleteBCardBatch";
    //A卡检测
    public static final String TESTACARD = "TestACard";
    //B卡检测
    public static final String TESTBCARD = "TestBCard";
    //检定周期
    public static final String SETTESTCYCLE = "SetTestCycle";
    //A卡设备状态
    public static final String GETACARDSTATE = "GetACardState";
    //A卡重启
    public static final String RESTARTACARD = "RestartACard";
    //数据字典
    public static final String DICTKEY = "DICT:";

    //实体
    public static final String DIAMOND = "diamond";
    //空心
    public static final String EMPTYDIAMOND = "emptyDiamond";
    //白色white
    public static final String WHITE = "white";
    //黑色black
    public static final String BLACK = "black";
    //theme
    public static final String THEME = "theme";

    //系统超级管理员权限标识
    public static final String SUPERADMIN = "sysadmin";
    //管理员权限标识
    public static final String ADMIN = "admin";

    public static List<String> ASSET_LIST = new ArrayList();

    public static List<String> DEVICE_LIST = new ArrayList();

    public static final String YUN_GATEWAY = "ins_yun_gateway";

    public static void initSysConfig(Dao dao) {
        Globals.MyConfig.clear();
        List<Sys_config> configList = dao.query(Sys_config.class, Cnd.NEW());
        for (Sys_config sysConfig : configList) {
        	if(sysConfig.getConfigKey() == null){
        		continue;
        	}
            switch (sysConfig.getConfigKey()) {
                case "AppName":
                    Globals.AppName = sysConfig.getConfigValue();
                    break;
                case "AppShrotName":
                    Globals.AppShrotName = sysConfig.getConfigValue();
                    break;
                case "AppDomain":
                    Globals.AppDomain = sysConfig.getConfigValue();
                    break;
                case "AppUploadPath":
                    Globals.AppUploadPath = sysConfig.getConfigValue();
                    break;
                default:
                    Globals.MyConfig.put(sysConfig.getConfigKey(), sysConfig.getConfigValue());
                    break;
            }
        }
    }

    public static void initRoute(Dao dao) {
        Globals.RouteMap.clear();
        List<Sys_route> routeList = dao.query(Sys_route.class, Cnd.where("disabled", "=", false));
        for (Sys_route route : routeList) {
            Globals.RouteMap.put(route.getUrl(), route);
        }
    }

    /**
     * All tables which should be updated..
     */
    static {
        ASSET_LIST.add("ins_apply_record");
        ASSET_LIST.add("ins_asset_apply");
        ASSET_LIST.add("ins_asset_cyclecheck");
        ASSET_LIST.add("ins_asset_inventory_record");
        ASSET_LIST.add("ins_asset_lend_record");
        ASSET_LIST.add("ins_asset_samplingcheck");
//        ASSET_LIST.add("ins_assets_info");
        ASSET_LIST.add("ins_assets_repair");
        ASSET_LIST.add("ins_assets_scrap_record");
        ASSET_LIST.add("ins_assets_seal_record");
        ASSET_LIST.add("ins_assets_unit");
        DEVICE_LIST.add("ins_device_facility");
        DEVICE_LIST.add("ins_device_facility_day");
//        DEVICE_LIST.add("ins_device_info");
        DEVICE_LIST.add("ins_device_state");
        DEVICE_LIST.add("ins_switching_flow");
        ASSET_LIST.add("ins_assets_unit_record");
        ASSET_LIST.add("ins_examine_record");
        ASSET_LIST.add("ins_fixed_assets_ledger");

        DEVICE_LIST.add("ins_collect");
    }

}
