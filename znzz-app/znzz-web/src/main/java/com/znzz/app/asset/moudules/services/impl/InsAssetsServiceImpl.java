package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.znzz.app.asset.moudules.models.*;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.instrument.modules.service.InsDeviceInfoService;
import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.framework.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.DaoUp;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsScrapRecordVo;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsSealedRecordVo;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.DateUtil;
import com.znzz.framework.util.StringUtil;
import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title:InsAssetsServiceImpl.java
 * </p>
 * <p>
 * Description:资产信息的实现类
 * </p>
 * <p>
 * Company: www.htfudao.com.cn
 * </p>
 *
 * @author changzheng
 * @version V1.0
 * @Package com.znzz.app.asset.moudules.services.impl
 * @date 2017年8月28日 上午10:27:52
 */
@IocBean(args = {"refer:dao"})
public class InsAssetsServiceImpl extends BaseServiceImpl<Ins_Assets> implements InsAssetsService {

    public InsAssetsServiceImpl(Dao dao) {
        super(dao);
    }

    @Inject
    private SysUnitService unitService;
    @Inject
    private InsAssetMouthReportService assetMouthReportService;
    @Inject
    ShiroUtil shiroUtil;


    private static final String DATA_SQL = "SELECT a.id id, a.assetCode assetCode, a.assetType assetType, b.deviceVersion deviceVersion, a.factoryTime factoryTime,  " +
            " a.enableTime enableTime, a.serialNumber serialNumber, a.isLend isLend, b.assetName assetName, c.`name` borrowDepart,  " +
            " d.username chargePerson, a.lendDate lendDate, a.returnDate returnDate, a.depreciationRate depreciationRate, a.checker checker,  " +
            " a.imagePath imagePath, a.manageStatus manageStatus, a.manageLevel manageLevel, a.completeStatus completeStatus, a.assetCategory assetCategory, a.instrumentCategory instrumentCategory,  " +
            " a.fundSources fundSources, ( SELECT e.`name` FROM ins_project_info e  WHERE e.CODE = a.projectName ) projectName,  " +
            " a.contractCode contractCode, a.batchCode batchCode, a.power power, a.warrantyPeriod warrantyPeriod, a.scrapState scrapState,  " +
            " a.repairState repairState, a.locationInfo locationInfo, a.macHour macHour, a.topicNo topicNo, a.isOverdue isOverdue,  " +
            " a.examineDate examineDate, a.dueDate dueDate, a.supplier supplier, a.unpackingDate unpackingDate, a.depreciationYear depreciationYear,  " +
            " a.isConnectCloud isConnectCloud, a.technicalIndex technicalIndex, a.isMilitary isMilitary, a.useType useType,a.isOrder isOrder,  " +
            " a.ggName ggName, a.accuracyClass accuracyClass, a.originalValue originalValue, a.manufactor manufactor,a.weight weight,a.installedCapacity installedCapacity,  " +
            " a.country country, a.opAt, b.urlImage urlImage, a.remark remark, a.isMeasure isMeasure,a.barCodeImage barCodeImage  " +
            " FROM ins_assets_info a LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg  " +
            " LEFT JOIN sys_unit c ON a.borrowDepart = c.id  " +
            " LEFT JOIN sys_user d ON a.chargePerson = d.id $order";


    private static final String EDIT_SQL = "SELECT a.id id, a.assetCode assetCode,  a.assetType assetType, b.deviceVersion deviceVersion, a.factoryTime factoryTime, " +
            " a.enableTime enableTime, a.serialNumber serialNumber, a.isLend isLend, a.`borrowDepart` borrowDepart,a.chargePerson chargePerson, " +
            " a.lendDate lendDate, a.returnDate returnDate, a.depreciationRate depreciationRate, a.checker checker, " +
            " a.imagePath imagePath,a.manageStatus manageStatus,a.manageLevel manageLevel,a.completeStatus completeStatus,a.assetCategory assetCategory, " +
            " a.instrumentCategory instrumentCategory,a.fundSources fundSources,a.`projectName` projectName,a.contractCode contractCode, " +
            " a.batchCode batchCode,a.power power,a.warrantyPeriod warrantyPeriod,a.scrapState scrapState,a.remark, " +
            " a.repairState repairState,a.locationInfo locationInfo,a.macHour macHour,a.topicNo topicNo, a.isOverdue isOverdue, a.examineDate examineDate,a.dueDate dueDate, " +
            " a.supplier supplier,a.unpackingDate unpackingDate,a.depreciationYear depreciationYear,a.isConnectCloud isConnectCloud,a.technicalIndex technicalIndex, " +
            " a.isMilitary isMilitary,a.useType useType,b.assetName assetName,a.ggName ggName,a.accuracyClass accuracyClass,a.weight weight,a.installedCapacity installedCapacity, " +
            " a.originalValue originalValue,a.country country,a.manufactor manufactor,b.createTime createTime,b.urlImage  " +
            " FROM ins_assets_info a LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg  " +
            " $order";

    @Override
    public NutMap getAssetsDataWith(AssetsForm asset, int length, int start, int draw, List<DataTableOrder> order,
                                    List<DataTableColumn> columns, Cnd cnd, Object object2, String originalValue2) {

        NutMap re = new NutMap();
        if (cnd == null) {
            cnd = Cnd.NEW();
        }
        //设置资产查询条件
        setWhereCondition(cnd, asset);

        // 查询sql
        Sql sql = Sqls.queryRecord(DATA_SQL);
        // 添加排序功能
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String name = col.getData();
                //String data = col.getName();
                if ("borrowdepart".equals(name)) {
                    name = " CONVERT( c.`name` USING gbk)";
                } else if ("chargeperson".equals(name)) {
                    name = "CONVERT( d.username USING GBK)";
                } else if ("assetname".equals(name)) {
                    name = "CONVERT( assetName USING GBK)";
                } else if ("opat".equals(name)) {
                    name = "opat";
                } else if ("originalvalue".equals(name)) {
                    name = "originalvalue";
                } else if ("deviceversion".equals(name)) {
                    name = "a.deviceversion";
                } else {
                    name = "CONVERT(" + name + "  USING GBK)";

                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
            }
        }
        sql.setVar("order", cnd);
        re = data(length, start, draw, sql, sql);
        return re;
    }


    @Override
    public String getAssetCodeList(String assetCode, String id) {
        String state = "";
		/*// 如果这个资产有其他任何关联则不能修改,只能是录入时输错了 让修改
		List<String> assetCodeList = assetMouthReportService.assetCodeList(null, null);
		if (assetCodeList.contains(assetCode)) {
			return "-1";	//表示此资产有关联记录(维修,封存,借还登记录)
		}*/
        List<Ins_Assets> assetList = dao().query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode).and("id", "<>", id));

        //数据库里有这个数据,并且没有绑定此台设备 没有关联信息并且这个统一编号是他自己
        if (assetList.size() == 0) {
            state = "0";
        } else {
            state = "1";    // 资产编号已经存在了.
        }
        return state;
    }

    @Override
    public boolean getAssetCode(String assetCode) {
        int count = dao().count(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
        if (count == 0) {
            return true;    // 插入
        }
        return false;
    }

    @Override
    public AssetsForm getAssetAndRuleInfo(Integer id) {
        Cnd cnd = Cnd.NEW();
        //sql
        Sql sql = Sqls.create(EDIT_SQL);
        //condition
        sql.setVar("order", cnd.and("a.id", "=", id));
        Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(AssetsForm.class).get(0);
    }

    /**
     * 查询需要详情字段,维修管理用
     */
    @Override
    public Map getAssetAndRuleInfo(String assetCode) {

        Sql sql = Sqls.create(
                "SELECT a.assetType,b.assetName,a.serialNumber,b.deviceVersion from ins_assets_info a ,ins_assets_version b WHERE a.deviceVersion = b.deviceVersionOrg AND a.assetCode=@assetCode");
        sql.setParam("assetCode", assetCode);

        // 设置回调函数
        sql = sql.setCallback(new SqlCallback() {

            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                HashMap<String, Object> map = new HashMap<String, Object>();
                ArrayList<Map> list = new ArrayList<Map>();
                while (rs.next()) {
                    map.put("assetType", rs.getInt("assetType"));
                    map.put("assetName", rs.getString("assetName"));
                    map.put("serialNumber", rs.getString("serialNumber"));
                    map.put("deviceVersion", rs.getString("deviceVersion"));
                    list.add(map);
                }
                return list;
            }
        });
        Map map = new HashMap<>();
        dao().execute(sql);
        List<Map> list = sql.getList(Map.class);
        if (list.size() > 0) {
            map = list.get(0);
        }
        return map;
    }

    @Override
    public Ins_Assets getAssetsInfo(Cnd cnd) {
        List<Ins_Assets> list = dao().query(Ins_Assets.class, cnd);
        return list.get(0);
    }

    @Override
    public void sealed(Integer[] ids) {
        Sql sql = Sqls.create("update ins_assets_info set manageStatus=1 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            // 写入封存记录
            writeRecord(id);
            dao().execute(sql);

        }

    }

    @Override
    public void unsealed(Integer[] ids) {
        Sql sql = Sqls.create("update ins_assets_info set manageStatus=0 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            // 写入封存记录
            writeRecord(id);
            dao().execute(sql);

        }

    }

    @Override
    public void confirmScrap(Integer[] ids) {
        Sql sql = Sqls.create("update ins_assets_info set scrapState=1 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            writeScrapRe(id);
            dao().execute(sql);

        }

    }

    @Override
    public void scrap(Integer[] ids) {
        Sql sql = Sqls.create("update ins_assets_info set scrapState=0 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            writeScrapRe(id);
            dao().execute(sql);

        }
    }

    private void writeRecord(Integer id) {
        List<Ins_Assets> list = dao().query(Ins_Assets.class, Cnd.where("id", "=", id));
        Ins_Asset_Sealrecord seal = new Ins_Asset_Sealrecord();
        seal.setOpBy(StringUtil.getUid());
        seal.setOpAt((int) (System.currentTimeMillis() / 1000));
        seal.setAssetCode(list.get(0).getAssetCode());
        seal.setSealStatus(list.get(0).getManageStatus());
        seal.setOperator(StringUtil.getUsername());
        seal.setOperateTime(new Date());
        dao().insert(seal);
    }

    private void writeScrapRe(Integer id) {
        List<Ins_Assets> list = dao().query(Ins_Assets.class, Cnd.where("id", "=", id));
        Ins_Asset_Scrap_Record seal = new Ins_Asset_Scrap_Record();
        seal.setOpBy(StringUtil.getUid());
        seal.setOpAt((int) (System.currentTimeMillis() / 1000));
        seal.setAssetCode(list.get(0).getAssetCode());
        seal.setScrapState(list.get(0).getScrapState());
        seal.setOperator(StringUtil.getUsername());
        seal.setOperateTime(new Date());
        dao().insert(seal);
    }

     /**
     * 获取导出信息
     * @param assetsForm
     * @return
     */
    @Override
    public List<AssetsForm> getExportList(AssetsForm assetsForm) {
        Sql sql = Sqls.create(DATA_SQL);
        Cnd cnd = Cnd.NEW();
        setWhereCondition(cnd, assetsForm);
        if (assetsForm.getExportScrapState() != null && !"".equals(assetsForm.getExportScrapState())){
            cnd.and("a.scrapState","=","1");
        }
        //condition
        sql.setVar("order", cnd.orderBy("a.opat", "desc"));
        Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        List<AssetsForm> list = sql.getList(AssetsForm.class);
        return list;
    }

    @Override
    public NutMap getSealRecordList(AssetsSealedRecordVo vo) {
        Sql sql = Sqls
                .create("select r.assetCode,i.assetType,u.name as borrowdepart,r.operator,r.sealStatus,r.operateTime "
                        + "from ins_assets_seal_record r LEFT JOIN ins_assets_info i on r.assetCode=i.assetCode "
                        + "left join sys_unit u on i.borrowDepart=u.id "
                        + "where r.assetCode = @code ORDER BY r.operateTime DESC");
        sql.setParam("code", vo.getAssetCode());

        NutMap map = data(vo.getLength(), vo.getStart(), vo.getDraw(), sql, sql);
        return map;
    }

    @Override
    public NutMap getScrapRecordList(AssetsScrapRecordVo vo) {
        Sql sql = Sqls
                .create("select r.assetCode,i.assetType,u.name as borrowdepart,r.operator,r.scrapState,r.operateTime "
                        + "from ins_assets_scrap_record r LEFT JOIN ins_assets_info i on r.assetCode=i.assetCode "
                        + "left join sys_unit u on i.borrowDepart=u.id "
                        + "where r.assetCode = @code ORDER BY r.operateTime DESC");
        sql.setParam("code", vo.getAssetCode());
        NutMap map = data(vo.getLength(), vo.getStart(), vo.getDraw(), sql, sql);
        return map;
    }

    @Override
    public boolean updateConnectCloud(Integer state, String deviceCode) {
        // 根据统一编号进行链接云网信息的更改
        // dao().update(assets, Cnd.where("assetCode", "=", deviceCode));
        Sql sql = Sqls
                .create("update ins_assets_info set isConnectCloud = " + state + " WHERE assetCode = @deviceCode");
        sql.setParam("deviceCode", deviceCode);
        dao().execute(sql);
        return false;
    }

    @Override
    public void deleteConnectCloud(String[] ids) {
        // 根据ids查询出所有的deviceCode
        List<Ins_Collect> list = dao().query(Ins_Collect.class, Cnd.where("id", "in", ids));
        List<String> deviceCodeList = new ArrayList<>();
        for (Ins_Collect collect : list) {
            deviceCodeList.add(collect.getDeviceCode());
        }
        // 根据deviceCode查询出所有的assets
        List<Ins_Assets> assetsList = dao().query(Ins_Assets.class, Cnd.where("assetCode", "in", deviceCodeList));
        // 进行更新
        for (Ins_Assets asset : assetsList) {
            asset.setIsConnectCloud(1);
            dao().update(asset, "^isConnectCloud$");
        }
        // 仅更新isConnectCloud
        // Sql sql = Sqls.create("update ins_assets_info set isConnectCloud = 1
        // WHERE assetCode in " +Arrays.asList(codeList));
        // System.out.println(sql);
    }

    @Override
    public XSSFWorkbook exportAssetsInfo(String[] ids, HttpServletRequest request, HttpServletResponse response, Map<String, String> exportmenu, Ins_Assets vo) {

        return null;
    }


    //资产详情
    @Override
    public AssetsForm getAssetAndRuleInfoDetail(Integer id, String assetCode) {
        //sql
        Sql sql = Sqls.queryEntity(DATA_SQL);
        //condition
        if (id != null) {
            sql.setVar("order", Cnd.where("a.id", "=", id));
        } else {
            sql.setVar("order", Cnd.where("a.assetCode", "=", assetCode));
        }
        //获取回调
        sql.setCallback(Sqls.callback.entities());
        Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
        sql.setEntity(entity);
        dao().execute(sql);
        return sql.getList(AssetsForm.class).get(0);


    }

    // 更新设备的使用单位,责任人(对ins_device_info表进行操作)
    @Override
    public void updateChargePersonAndBorrowDepart(String assetCode, String borrowDepart, String chargePerson) {
        // 根据统一编号查出设备信息
        Ins_DeviceInfo deviceInfo = dao().fetch(Ins_DeviceInfo.class, Cnd.where("deviceCode", "=", assetCode));
        Ins_Assets assets = dao().fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));

        deviceInfo.setDeviceCode(assets.getAssetCode());
        deviceInfo.setAssetType(assets.getAssetType());
        deviceInfo.setChargePerson(chargePerson);
        deviceInfo.setBorrowDepart(borrowDepart);

        //更新device_info
        dao().updateIgnoreNull(deviceInfo);
    }

    @Override
    public void insertList(List<Ins_Assets> assetsList) {
        dao().insert(assetsList);
    }

    @Override
    public boolean checkBind(String assetCode) {
        List<Ins_Collect> collectList = dao().query(Ins_Collect.class, Cnd.where("deviceCode", "=", assetCode));
        if (collectList.size() > 0) {    // 有绑定
            return true;
        }
        return false;
    }

    @Override
    public void saveOrUpdate(List<Ins_Assets> assetsList) {
        //循环遍历操作
        for (Ins_Assets assets : assetsList) {
            //id不为null,进行更新
            if (assets.getId() != null) {
                dao().updateIgnoreNull(assets);
            } else {//进行插入
                dao().insert(assets);
            }
        }
    }

    @Override
    public String getDeviceCode(Integer id) {
        Ins_Assets assets = dao().fetch(Ins_Assets.class, Cnd.where("id", "=", id));
        if (Strings.isNotBlank(assets.getAssetCode())) {
            return assets.getAssetCode();
        }
        return null;
    }

    /**
     * 获取资产表中的统一编号
     */
    @Override
    public String getAssetCodeListByUnitId(String unitId) {
        Sql sql = Sqls.create("SELECT DISTINCT(assetCode) FROM ins_assets_info WHERE assetType IN (1,2) AND (scrapState is null or scrapState !=1) $var ");
        if (StringUtils.isNotBlank(unitId)) {
            StringBuilder bulid = new StringBuilder();
            bulid.append("and borrowDepart = '").append(unitId).append("'");
            sql.setVar("var", bulid);
        }


        sql = sql.setCallback(new SqlCallback() {

            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<String> list = new ArrayList<>();
                JSONArray jarr = new JSONArray();
                while (rs.next()) {
                    JSONObject jobj = new JSONObject();
                    jobj.put("assetCode", rs.getString("assetCode"));
                    jarr.put(jobj);
                }
                list.add(jarr.toString());
                return list;
            }
        });
        String str = dao().execute(sql).getList(String.class).get(0);
        return str;
    }


    @Override
    public Map<String, Ins_Assets> getAssetAndRuleInfoByAssetCodes(String assetCode) {
		/*Sql sql = Sqls.create(SQL +" $var");
		Criteria cri = Cnd.cri() ;
		cri.where().and("a.assetCode", "in", assetCode) ;
		sql.setVar("var", cri);
		Entity<Ins_Assets> entity = dao().getEntity(Ins_Assets.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		List<Ins_Assets> list = sql.getList(Ins_Assets.class);
		Map<String, Ins_Assets> map = new HashMap<String, Ins_Assets>() ;
		for(int i=0;i<list.size();i++){
			Ins_Assets form = list.get(i) ;
			//map.put(form.getAssetCode(), form) ;
		}*/


        return null;
    }

    /**
     * 根据统一编号(逗号分隔),获取资产实体bean列表
     *
     * @param deviceCodes
     * @return
     */
    @Override
    public List<Ins_Assets> getInsAssetBeanList(String deviceCodes) {
        Sql sql = Sqls.create("SELECT * FROM INS_ASSETS_INFO WHERE ASSETCODE IN (" + deviceCodes + ")");
        Entity<Ins_Assets> entity = dao().getEntity(Ins_Assets.class);
        sql.setEntity(entity);
        sql.setCallback(Sqls.callback.entities());
        dao().execute(sql);
        return sql.getList(Ins_Assets.class);
    }

    @Override
    public void updateInsAssetBeanLocationInfo(String deviceCode, String locationInfo) {
        dao().update(Ins_Assets.class, Chain.make("locationInfo", locationInfo), Cnd.where("assetCode", "=", deviceCode));
    }

    public Ins_Assets getInsAssetBean(String assetCode) {
        Ins_Assets ins_assets = dao().fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
        if (ins_assets != null) {
            return ins_assets;
        }
        return null;
    }

    //闲置
    @Override
    public void unUsed(Integer[] ids, String message) {
        Sql sql = Sqls.create("update ins_assets_info set manageStatus=2 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            dao().execute(sql);

        }
    }

    //禁用
    @Override
    public void forbidden(Integer[] ids, String message) {
        Sql sql = Sqls.create("update ins_assets_info set manageStatus=3 where id=@id");
        for (Integer id : ids) {
            sql.setParam("id", id);
            dao().execute(sql);

        }
    }

    /**
     * 获取导入数据
     *
     * @param assetsImportFile
     * @param saveOrUpdate
     * @return
     */
    @Override
    public NutMap getImportInfo(List<ExcelImportResult<Ins_Assets>> assetsImportFile, String saveOrUpdate,String userId) {
        NutMap map = new NutMap();
        //校验数据
        StringBuilder sb = new StringBuilder();

        List<Ins_Assets> failList = assetsImportFile.get(0).getFailList();          //失败的数据集合

        List<Ins_Assets> list = assetsImportFile.get(0).getList();                  //资产信息
        List<Ins_Asset_Rule> rulesList = new ArrayList<>();                         //型号集合信息
        List<Ins_DeviceInfo> deviceInfoList = new ArrayList<>();                    //ins_device_info集合信息
        List<Ins_Asset_lend_record> lendRecordList = new ArrayList<>();             //借出记录集合信息
        List<Ins_Asset_Unit> assetUnitList = new ArrayList<>();                     //资产单位集合信息
        List<Ins_ProjectInfo> projectList = new ArrayList<>();                      //项目

        //判断更新还是保存
        if (!"update".equalsIgnoreCase(saveOrUpdate)){

            for (Ins_Assets assets : list) {
                Ins_Asset_Rule assetRule = null;
                Ins_DeviceInfo deviceInfo = new Ins_DeviceInfo();
                Ins_Asset_lend_record lendRecord = new Ins_Asset_lend_record();
                Ins_Asset_Unit assetUnit = new Ins_Asset_Unit();
                //数据库校验是否重复
                Ins_Assets assetsFromDB = dao().fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assets.getAssetCode()));

                if (assetsFromDB != null){//数据当中有
                    if (assetsFromDB.getScrapState() != null &&assetsFromDB.getScrapState() == 1){//报废资产
                        sb.append("第" + (assets.getRowNum() + 1) +"行, 统一编号在报废资产行列，请检查填写正确后导入<br/>");
                        continue;
                    } else {
                        sb.append("第" + (assets.getRowNum() + 1) +"行, 统一编号已经存在，请检查填写正确后导入<br/>");
                        continue;
                    }

                }



                /*********************************  型号  start*****************************************************/
                //设置型号
                assetRule = setAssetRule(rulesList, assets, assetRule);
                if (assetRule != null) {
                    rulesList.add(assetRule);
                }
                /*********************************  型号  end*****************************************************/

                /*********************************  ins_device_info  start*****************************************************/
                setDeviceInfo(deviceInfoList, assets, deviceInfo);
                /*********************************  ins_device_info  end*****************************************************/


                /*********************************  lendRecord 借出记录  start*****************************************************/
                if (assets.getLendDate() != null){
                    setLendRecord(dao(), assets, lendRecord,assets.getAssetCode(), assets.getBorrowDepart(), assets.getChargePerson(), assets.getLendDate());
                    lendRecordList.add(lendRecord);
                }
                /*********************************  lendRecord 借出记录  end*****************************************************/

                /*********************************  assetUnit 科室借调记录  start*****************************************************/
                Ins_Asset_Unit fetchAssetUnit = dao().fetch(Ins_Asset_Unit.class, Cnd.where("assetCode","=",assets.getAssetCode()));

                String locationInfo = assets.getLocationInfo();
                if (Strings.isBlank(locationInfo)){
                    locationInfo = "--";
                }
                insertUntiLend(assetUnit,assets.getAssetCode(),locationInfo,assets.getBorrowDepart(),assets.getChargePerson(),fetchAssetUnit);
                assetUnitList.add(assetUnit);
                /*********************************  assetUnit 科室借调记录  end*****************************************************/



            }
        } else {//更新
            Dao dao = Mvcs.ctx().getDefaultIoc().get(Dao.class);
            ApplyUtils applyUtils = new ApplyUtils();
            Date pureDate = applyUtils.pureDate();
            for (Ins_Assets assets : list) {
                Ins_Asset_Rule assetRule = null;
                Ins_DeviceInfo deviceInfo = new Ins_DeviceInfo();

                Ins_Assets assetFromDB = dao().fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assets.getAssetCode()));
                /*********************************  资产信息表  start*******************************************/

                if (assetFromDB != null && assetFromDB.getScrapState() != null && assetFromDB.getScrapState() == 1){
                    sb.append("第" + (assets.getRowNum() + 1) +"行, 统一编号在报废资产行列，请检查填写正确后导入<br/>");
                    continue;
                }

                if (assetFromDB == null){
                    sb.append("第" + (assets.getRowNum() + 1) +"行, 统一编号并不存在<br/>");
                    continue;
                } else {
                    //id 赋值
                    assets.setId(assetFromDB.getId());
                    assets.setBorrowDepart(null);
                    assets.setChargePerson(null);
                    assets.setLendDate(null);
                    assets.setReturnDate(null);
                }
                /*********************************  资产信息表  end*******************************************/


                /*********************************  型号  start*******************************************/
                //设置型号
                assetRule = setAssetRuleUpdate(rulesList, assets, assetRule);
                if (assetRule != null) {
                    rulesList.add(assetRule);
                }
                /*********************************  型号  end*******************************************/


                /*********************************  device_info  start*******************************************/
                setDeviceInfo(deviceInfoList, assets, deviceInfo);
                /*********************************  device_info  end*******************************************/


                /*************************************各种状态的变更 start*************************************************/
                String assetCodeHander = assets.getAssetCode();
                String message = null;
                //库房里的进行操作
                if ("库房".equalsIgnoreCase(assets.getBorrowDepart())){
                    Integer manageState = assetFromDB.getManageStatus();
                    switch (manageState){
                        case 1 : message = "封存";
                        break;
                        case 2 : message = "闲置";
                        break;
                        case 3 : message = "禁用";
                        break;
                        default: message = "未知处理";
                        break;
                    }
                    if (manageState != 0){// No在用
                        //闲置，封存，禁用，确认报废
                        //isOrder == 1
                        //预约表进行小环闭合(借出)
                        //查资产表里的字段信息进行判断(str1 = 1 被占用 str = 2 可以借)
                        applyUtils.cancelAccordForbidHandAsset(assetCodeHander, pureDate , dao, message);
                        applyUtils.insertMessageInfo(assetCodeHander, dao, message, userId);
                    }

                } else {//不是库房
                    assets.setManageStatus(null);
                    assets.setScrapState(null);
                    assets.setRepairState(null);

                }
                /*************************************各种状态的变更  end  *************************************************/

            }
        }


        //错误信息
        String errorMessage = sb.toString();
        if (Strings.isNotBlank(errorMessage)){
            map.put("errorMsg",errorMessage);
        } else {
            map.put("assetInfo", list);
            map.put("deviceInfo", deviceInfoList);
            if (rulesList.size() > 0)
                map.put("ruleInfo",rulesList);
            if (lendRecordList.size() > 0)
                map.put("lendRecordInfo", lendRecordList);
            if (assetUnitList.size() > 0)
                map.put("unitInfo", assetUnitList);
        }

        return map;
    }

    private void setDeviceInfo(List<Ins_DeviceInfo> deviceInfoList, Ins_Assets assets, Ins_DeviceInfo deviceInfo) {
        if (assets.getId() == null){//插入
            deviceInfo.setDeviceCode(assets.getAssetCode());
            deviceInfo.setDeviceName(assets.getAssetName());
            deviceInfo.setDeviceVersion(assets.getDeviceVersion());
            //1是0否
            deviceInfo.setOutField(0);
            deviceInfo.setBorrowDepart(assets.getBorrowDepart());
            if (Strings.isNotBlank(assets.getChargePerson())) {
                deviceInfo.setChargePerson(assets.getChargePerson());
            }
            deviceInfo.setAssetType(assets.getAssetType());
            deviceInfoList.add(deviceInfo);
        } else {//更新
            Ins_DeviceInfo deviceInfoFromDB = dao().fetch(Ins_DeviceInfo.class,Cnd.where("deviceCode","=",assets.getAssetCode()));
            deviceInfo.setId(deviceInfoFromDB.getId());
            deviceInfo.setDeviceName(assets.getAssetName());
            deviceInfo.setDeviceVersion(assets.getDeviceVersion());
        }
    }

    private Ins_Asset_Rule setAssetRule(List<Ins_Asset_Rule> rulesList, Ins_Assets assets, Ins_Asset_Rule assetRule) {
        //型号为空
        if (Strings.isBlank(assets.getDeviceVersion())) {
            //查询数据库当中有没有
            //deviceVersionOrg
            Ins_Asset_Rule ruleFroomDB = dao().fetch(Ins_Asset_Rule.class,Cnd.where("deviceVersionOrg","=",assets.getAssetCode() + "(" + assets.getAssetName() + ")"));
            if (ruleFroomDB == null){//没有创建
                assetRule = generateRule(assets);
            } else {//有的话赋值id
                assetRule = new Ins_Asset_Rule();
              assetRule.setId(ruleFroomDB.getId());
                assetRule.setDeviceVersionOrg(assets.getAssetCode() + "(" + assets.getAssetName() + ")");
                assetRule.setAssetName(assets.getAssetName());
                assets.setDeviceVersion(assets.getAssetCode() + "(" + assets.getAssetName() + ")");
            }
        } else {
            //先检查数据库里是否有同样的型号
            Ins_Asset_Rule ruleFromDB = dao().fetch(Ins_Asset_Rule.class, Cnd.where("deviceVersionOrg", "=", assets.getDeviceVersion()));
            if (ruleFromDB == null) {
                //如果数据库里没有，再检查本次导入是否有同样的型号
                boolean assetRuleExist = false;

                assetRuleExist = existRuleFromExcel(rulesList, assets, assetRuleExist);
                //如果不存在同样的型号，则创建新的型号
                if (!assetRuleExist) {
                    assetRule = new Ins_Asset_Rule();
                    assetRule.setDeviceVersionOrg(assets.getDeviceVersion());
                    assetRule.setDeviceVersion(assets.getDeviceVersion());
                    assetRule.setAssetName(assets.getAssetName());
                    assetRule.setCreateTime( new Date() );
                }
            } /*else {//数据库有这个型号，nothing
                assetRule = new Ins_Asset_Rule();
                assetRule.setId(ruleFromDB.getId());
                assetRule.setDeviceVersionOrg(assets.getDeviceVersion());
                assetRule.setDeviceVersion(assets.getDeviceVersion());
                assetRule.setAssetName(assets.getAssetName());
            }*/
        }
        return assetRule;
    }
    private Ins_Asset_Rule setAssetRuleUpdate(List<Ins_Asset_Rule> rulesList, Ins_Assets assets, Ins_Asset_Rule assetRule) {
        //型号为空
        if (Strings.isBlank(assets.getDeviceVersion())) {
            //查询数据库当中有没有
            //deviceVersionOrg
            Ins_Asset_Rule ruleFroomDB = dao().fetch(Ins_Asset_Rule.class,Cnd.where("deviceVersionOrg","=",assets.getAssetCode() + "(" + assets.getAssetName() + ")"));
            if (ruleFroomDB == null){//没有创建
                assetRule = generateRule(assets);
            } else {//有的话赋值id
                assets.setDeviceVersion(ruleFroomDB.getDeviceVersionOrg());
            }
        } else {
            //先检查数据库里是否有同样的型号
            Ins_Asset_Rule ruleFromDB = dao().fetch(Ins_Asset_Rule.class, Cnd.where("deviceVersionOrg", "=", assets.getDeviceVersion()));
            if (ruleFromDB == null) {
                //如果数据库里没有，再检查本次导入是否有同样的型号
                boolean assetRuleExist = false;

                assetRuleExist = existRuleFromExcel(rulesList, assets, assetRuleExist);
                //如果不存在同样的型号，则创建新的型号
                if (!assetRuleExist) {
                    assetRule = new Ins_Asset_Rule();
                    assetRule.setDeviceVersionOrg(assets.getDeviceVersion());
                    assetRule.setDeviceVersion(assets.getDeviceVersion());
                    assetRule.setAssetName(assets.getAssetName());
                    assetRule.setCreateTime(new Date());
                }
            } else {//数据库有这个型号，nothing
                assetRule = new Ins_Asset_Rule();
                assetRule.setId(ruleFromDB.getId());
                assetRule.setDeviceVersionOrg(assets.getDeviceVersion());
                assetRule.setDeviceVersion(assets.getDeviceVersion());
                assetRule.setAssetName(assets.getAssetName());
            }
        }
        return assetRule;
    }

    private Ins_Asset_Rule generateRule(Ins_Assets assets) {
        Ins_Asset_Rule assetRule;
        assetRule = new Ins_Asset_Rule();
        assetRule.setDeviceVersionOrg(assets.getAssetCode() + "(" + assets.getAssetName() + ")");
        assetRule.setAssetName(assets.getAssetName());
        assets.setDeviceVersion(assets.getAssetCode() + "(" + assets.getAssetName() + ")");
        assetRule.setCreateTime(new Date());
        return assetRule;
    }

    private boolean existRuleFromExcel(List<Ins_Asset_Rule> rulesList, Ins_Assets assets, boolean assetRuleExist) {
        for (Ins_Asset_Rule rule : rulesList) {
            if (assets.getDeviceVersion().equals(rule.getDeviceVersion())) {
                assetRuleExist = true;
                break;
            }
        }
        return assetRuleExist;
    }

    @Override
    public void updateList(List<Ins_Assets> assetsList) {
        for (Ins_Assets assets : assetsList) {
            dao().updateIgnoreNull(assets);
        }
    }


    /**
     * 设置资产查询条件
     * @param cnd
     * @param vo
     */
    private void setWhereCondition(Cnd cnd, AssetsForm vo) {
        //统一编号
        if (Strings.isNotBlank(vo.getAssetCode())) {
            cnd.and("a.assetCode", "like", "%" + vo.getAssetCode().trim() + "%");
        }
        //类别
        if (vo.getAssetType() != null && vo.getAssetType() != 0) {
            cnd.and("a.assetType", "=", vo.getAssetType());
        }
        //出厂日期
        if (Strings.isNotBlank(vo.getFactoryTimeRange())) {
            List<Date> dateList = DateUtil.getBetweenAndTime(vo.getFactoryTimeRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.factoryTime", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        //型号
        if (Strings.isNotBlank(vo.getDeviceVersion())) {
            cnd.and("b.deviceVersion", "like", "%" + vo.getDeviceVersion().trim() + "%");
        }
        //名称
        if (Strings.isNotBlank(vo.getAssetName())) {
            cnd.and("b.assetName", "like", "%" + vo.getAssetName().trim() + "%");
        }
        //规格
        if (Strings.isNotBlank(vo.getGgName())) {
            cnd.and("a.ggName", "like", "%" + vo.getGgName().trim() + "%");
        }
        //出厂编号
        if (Strings.isNotBlank(vo.getSerialNumber())) {
            cnd.and("a.serialNumber", "like", "%" + vo.getSerialNumber().trim() + "%");
        }
        // 原值索引
        if (vo.getOriginalValue1() != null && vo.getOriginalValue2() == null) {

            cnd.and("a.originalValue", ">=", vo.getOriginalValue1());

        } else if (vo.getOriginalValue1() == null && vo.getOriginalValue2() != null) {
            cnd.and("a.originalValue", "<=", vo.getOriginalValue2());
        } else if (vo.getOriginalValue1() != null && vo.getOriginalValue2() != null) {
            cnd.and("a.originalValue", "between", new Object[]{vo.getOriginalValue1(), vo.getOriginalValue2()});
        }
        //位置信息
        if (Strings.isNotBlank(vo.getLocationInfo())) {
            cnd.and("a.locationInfo", "like", "%" + vo.getLocationInfo().trim() + "%");
        }
        //使用单位
        if (Strings.isNotBlank(vo.getBorrowDepart())) {
            String childList = unitService.getChildList(vo.getBorrowDepart().trim());
            cnd.and("a.borrowDepart", "in", childList);
        }
        //责任人
        if (Strings.isNotBlank(vo.getChargePerson())) {
            cnd.and("a.chargePerson", "=", vo.getChargePerson().trim());
        }
        //借用日期
        if (Strings.isNotBlank(vo.getLendDateRange())) {
            List<Date> dateList = DateUtil.getBetweenAndTime(vo.getLendDateRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.lendDate", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        // 判断归还时间
        if (Strings.isNotBlank(vo.getReturnDateRange())) {
            List<Date> dateList = DateUtil.getBetweenAndTime(vo.getReturnDateRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.returnDate", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        //合同号
        if (Strings.isNotBlank(vo.getContractCode())) {
            cnd.and("a.contractCode", "like", "%" + vo.getContractCode().trim() + "%");
        }
        //批件序号
        if (Strings.isNotBlank(vo.getBatchCode())) {
            cnd.and("a.batchCode", "like", "%" + vo.getBatchCode().trim() + "%");
        }
        //供货单位
        if (Strings.isNotBlank(vo.getSupplier())) {
            cnd.and("a.supplier", "like", "%" + vo.getSupplier().trim() + "%");
        }
        //项目名称
        if (Strings.isNotBlank(vo.getProjectName())) {
            cnd.and("a.projectName", "like", "%" + vo.getProjectName().trim() + "%");
        }
        //资金来源
        if (vo.getFundSources() != null) {
            cnd.and("a.fundSources", "=", vo.getFundSources());
        }
        //功率
        if (Strings.isNotBlank(vo.getPower())) {
            cnd.and("a.power", "=", vo.getPower().trim());
        }
        //台时信息
        if (Strings.isNotBlank(vo.getMacHour())) {
            cnd.and("a.macHour", "like", "%" + vo.getMacHour().trim() + "%");
        }
        //管理状态
        if (vo.getManageStatus() != null) {
            cnd.and("a.manageStatus", "=", vo.getManageStatus());
        }
        //管理级别
        if (vo.getManageLevel() != null) {
            cnd.and("a.manageLevel", "=", vo.getManageLevel());
        }
        //资产类别
        if (vo.getAssetCategory() != null) {
            cnd.and("a.assetCategory", "=", vo.getAssetCategory());
        }
        //维修状态
        if (vo.getRepairState() != null) {
            cnd.and("a.repairState", "=", vo.getRepairState());
        }
        // 判断启用时间
        if (Strings.isNotBlank(vo.getEnableTimeRange())) {
            List<Date> dateList = DateUtil.getBetweenAndTime(vo.getEnableTimeRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.enableTime", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        //用途
        if (vo.getUseType() != null) {
            cnd.and("a.useType", "=", vo.getUseType());
        }
        //国别
        if (Strings.isNotBlank(vo.getCountry())) {
            cnd.and("a.country", "like", "%" + vo.getCountry().trim() + "%");
        }
        //厂家
        if (Strings.isNotBlank(vo.getManufactor())) {
            cnd.and("a.manufactor", "like", "%" + vo.getManufactor().trim() + "%");
        }
        //课题号
        if (Strings.isNotBlank(vo.getTopicNo())) {
            cnd.and("a.topicNo", "like", "%" + vo.getTopicNo().trim() + "%");
        }
        //保修期
        if (Strings.isNotBlank(vo.getWarrantyPeriod())) {
            cnd.and("a.warrantyPeriod", "like", "%" + vo.getWarrantyPeriod().trim() + "%");
        }
        //折旧年限
        if (Strings.isNotBlank(vo.getDepreciationYear())) {
            cnd.and("a.depreciationYear", "like", "%" + vo.getDepreciationYear().trim() + "%");
        }
        //折旧率
        if (Strings.isNotBlank(vo.getDepreciationRate())) {
            cnd.and("a.depreciationRate", "like", "%" + vo.getDepreciationRate().trim() + "%");
        }
        //仪器设备类别
        if (vo.getInstrumentCategory() != null) {
            cnd.and("a.instrumentCategory", "=", vo.getInstrumentCategory());
        }
        //开箱日期
        if (Strings.isNotBlank(vo.getUnpackingDateRange())) {
             List<Date> dateList = DateUtil.getBetweenAndTime(vo.getUnpackingDateRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.unpackingDate", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        // 判断检定日期
        if (Strings.isNotBlank(vo.getExamineDateRange())) {
            List<Date> dateList = DateUtil.getBetweenAndTime(vo.getExamineDateRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.examineDate", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        // 判断到期检定日期
        if (Strings.isNotBlank(vo.getDueDateRange())) {
             List<Date> dateList = DateUtil.getBetweenAndTime(vo.getDueDateRange());
            if (dateList != null && dateList.size() > 0) {
                cnd.and("a.dueDate", "between", new Object[]{dateList.get(0), dateList.get(1)});
            }
        }
        //军工
        if (vo.getIsMilitary() != null) {
            cnd.and("a.isMilitary", "=", vo.getIsMilitary());
        }
        //验收人
        if (Strings.isNotBlank(vo.getChecker())) {
            cnd.and("a.checker", "like", "%" + vo.getChecker().trim() + "%");
        }
        //是否过期
        if (vo.getIsOverdue() != null) {
            cnd.and("a.isOverDue", "=", vo.getIsOverdue());
        }
        //是否计量
        if(vo.getIsMeasure() != null){
            if (vo.getIsMeasure() == 0) { //是
                cnd.and("a.isMeasure", "=", vo.getIsMeasure());
            } else { //否
                cnd.and(Cnd.exps("a.isMeasure","is",null).or("a.isMeasure","=","1"));
            }
        }
        //完好状态
        if (vo.getCompleteStatus() != null) {
            cnd.and("a.completeStatus", "=", vo.getCompleteStatus());
        }
        //是否借出
        if (vo.getIsLend() != null) {
            cnd.and("a.isLend", "=", vo.getIsLend());
        }


        //报废状态
        String exportScrapState = vo.getExportScrapState();
        if (Strings.isBlank(exportScrapState)){//资产信息页面
            if (vo.getScrapState() == null ){
                cnd.and(Cnd.exps("a.scrapState","is",null).or("a.scrapState","=","0"));

            } else if (vo.getScrapState() != null){
                cnd.and("a.scrapState", "=", vo.getScrapState());
            }
        }
        //是否连接云网
        if(vo.getIsConnectCloud() != null){
            if (vo.getIsConnectCloud() == 0) { //是
                cnd.and("a.isConnectCloud", "=", vo.getIsConnectCloud());
            } else { //否
                cnd.and(Cnd.exps("a.isConnectCloud","is",null).or("a.isConnectCloud","=","1"));
            }
        }

        // 重量索引
        if (vo.getWeight1() != null && vo.getWeight2() == null) {

            cnd.and("a.weight", ">=", vo.getWeight1());

        } else if (vo.getWeight1() == null && vo.getWeight2() != null) {
            cnd.and("a.weight", "<=", vo.getWeight2());
        } else if (vo.getWeight1() != null && vo.getWeight2() != null) {
            cnd.and("a.weight", "between", new Object[]{vo.getWeight1(), vo.getWeight2()});
        }

        //装机容量
        if (Strings.isNotBlank(vo.getInstalledCapacity())) {
            cnd.and("a.installedCapacity", "like", "%" + vo.getInstalledCapacity().trim() + "%");
        }

        //备注
        if (Strings.isNotBlank(vo.getRemark())) {
            cnd.and("a.remark", "like", "%" + vo.getRemark().trim() + "%");
        }

    }

    @Override
    public boolean existDeviceVersion(String deviceVersion) {
        int count = dao().count(Ins_Asset_Rule.class, Cnd.where("deviceVersionOrg", "=", deviceVersion));
        if (count == 0) {
            return false;
        }
        return true;
    }


    /**
     * 插入借还记录
     *
     * @param dao
     * @param assets
     * @param lendRecord
     * @param assetCode
     * @param newBorrowDepart
     * @param newChargePerson
     * @param lendDateForm
     */
    private void setLendRecord(Dao dao, Ins_Assets assets, Ins_Asset_lend_record lendRecord, String assetCode,
                               String newBorrowDepart, String newChargePerson, Date lendDateForm) {
        // 获取当前登录用户
        Sys_user currentUser = (Sys_user) SecurityUtils.getSubject().getPrincipal();
        String username = currentUser.getUsername();
        String unitid = currentUser.getUnitid();
        // 根据单位id查出单位名称
        Sys_unit fetch = dao.fetch(Sys_unit.class, Cnd.where("id", "=", unitid));
        lendRecord.setOriginalDepart(fetch.getName());
        // 为资产借出记录赋值
        lendRecord.setAssetCode(assetCode);
        Sys_unit unit = dao.fetch(Sys_unit.class, Cnd.where("id", "=", newBorrowDepart));
        if (unit != null) {
            String departName = unit.getName();
            lendRecord.setApplyDepart(departName);
        }
        Sys_user user = dao.fetch(Sys_user.class, Cnd.where("id", "=", newChargePerson));
        if (user != null) {
            String personName = user.getUsername();
            lendRecord.setApplyPerson(personName);
        }

        lendRecord.setHandlePerson(username);
        lendRecord.setActionType(0); // 事件类型，0借出1归还(仪器室角度)
        lendRecord.setOprateTime(lendDateForm);

        assets.setLendDate(lendDateForm);
        assets.setIsLend(0); // 借出状态

    }

    /**
     * 科室借调
     *
     * @param assetUnit
     * @param assetCode
     * @param locationInfo
     * @param newBorrowDepart
     * @param newChargePerson
     * @param fetchAssetUnit
     */
    private void insertUntiLend(Ins_Asset_Unit assetUnit, String assetCode, String locationInfo, String newBorrowDepart,
                                String newChargePerson, Ins_Asset_Unit fetchAssetUnit) {
        if (fetchAssetUnit == null) { // 数据库当中没有,初始化导入
            assetUnit.setAssetCode(assetCode);
            assetUnit.setChargeDepart(newBorrowDepart);
            assetUnit.setChargeMan(newChargePerson);
            assetUnit.setUserDepart(newBorrowDepart);
            assetUnit.setUserMan(newChargePerson);
            assetUnit.setStatus(0);
            assetUnit.setRate("--");
            assetUnit.setOperateTime(new Date());
            assetUnit.setPosition(locationInfo);
        } else {
            assetUnit.setId(fetchAssetUnit.getId());
            assetUnit.setAssetCode(assetCode);
            // 可以变更使用人和责任单位
            assetUnit.setChargeMan(newChargePerson);
            assetUnit.setChargeDepart(newBorrowDepart);
        }
    }


}
