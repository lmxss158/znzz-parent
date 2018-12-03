package com.znzz.app.asset.moudules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Asset_Scrap_Record;
import com.znzz.app.asset.moudules.models.Ins_Asset_Sealrecord;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.app.asset.moudules.services.InsAssetsService_Czl;
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

/**
 * 
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
 * @Package com.znzz.app.asset.moudules.services.impl
 * @author changzheng
 * @date 2017年8月28日 上午10:27:52
 * @version V1.0
 */
@IocBean(args = { "refer:dao" })
public class InsAssetsServiceImpl_Czl extends BaseServiceImpl<Ins_Assets> implements InsAssetsService_Czl {

	public InsAssetsServiceImpl_Czl(Dao dao) {
		super(dao);
	}

	@Inject
	private SysUnitService unitService;
	@Inject
	private InsAssetMouthReportService assetMouthReportService;
                                                                                                                                      
	private static final String SQL = "SELECT                                                         "
	+"a.id id,                                                                                         "
	+"a.assetCode assetCode,                                                                           "
	+"a.assetType assetType,                                                                           "
	+"b.deviceVersion deviceVersion,                                                                   "
	+"a.factoryTime factoryTime,                                                                       "
	+"a.enableTime enableTime,                                                                         "
	+"a.serialNumber serialNumber,                                                                     "
	+"a.isLend isLend,                                                                                 "
	+"b.assetName assetName,                                                                           "
	+"c.`name` borrowDepart,                    "
	+"d.username chargePerson,                  "
	+"a.lendDate lendDate,                                                                             "
	+"a.returnDate returnDate,                                                                         "
	+"a.depreciationRate depreciationRate,                                                             "
	+"a.checker checker,                                                                               "
	+"a.imagePath imagePath,                                                                           "
	+"a.manageStatus manageStatus,                                                                     "
	+"a.manageLevel manageLevel,                                                                       "
	+"a.completeStatus completeStatus,                                                                 "
	+"a.assetCategory assetCategory,                                                                   "
	+"a.instrumentCategory instrumentCategory,                                                         "
	+"a.fundSources fundSources,                                                                       "
	+"( SELECT e.`name` FROM ins_project_info e WHERE e.code = a.projectName ) projectName,            "
	+"a.contractCode contractCode,                                                                     "
	+"a.batch_code batchCode,                                                                          "
	+"a.power power,                                                                                   "
	+"a.warrantyPeriod warrantyPeriod,                                                                 "
	+"a.scrapState scrapState,                                                                         "
	+"a.repairState repairState,                                                                       "
	+"a.locationInfo locationInfo,                                                                     "
	+"a.macHour macHour,                                                                               "
	+"a.topicNo topicNo,                                                                               "
	+"a.isOverdue isOverdue,                                                                           "
	+"a.examineDate examineDate,                                                                       "
	+"a.dueDate dueDate,                                                                               "
	+"a.supplier supplier,                                                                             "
	+"a.unpackingDate unpackingDate,                                                                   "
	+"a.depreciationYear depreciationYear,                                                             "
	+"a.isConnectCloud isConnectCloud,                                                                 "
	+"a.technicalIndex technicalIndex,                                                                 "
	+"a.is_military isMilitary,                                                                        "
	+"a.use_type use_type,                                                                              "
	+"a.ggName ggName,                                                                                 "
	+"a.accuracyClass accuracyClass,                                                                   "
	+"a.originalValue originalValue,                                                                   "
	+"a.manufactor manufactor,                                                                         "
	+"a.country country,                                                                               "
	+"a.opAt,                                                                                          "
	+"b.urlImage urlImage,                                                                             "
	+"a.remark FROM ins_assets_info a LEFT JOIN ins_assets_version b ON  a.deviceVersion = b.deviceVersionOrg "
	+ " LEFT JOIN sys_unit c ON a.borrowDepart = c.id LEFT JOIN sys_user d ON a.chargePerson = d.id $order";
	
	private static final String EDIT_SQL = "SELECT a.id id, a.assetCode assetCode,  a.assetType assetType, b.deviceVersion deviceVersion, a.factoryTime factoryTime,"
			+ " a.enableTime enableTime, a.serialNumber serialNumber, a.isLend isLend, a.`borrowDepart` borrowDepart,a.chargePerson chargePerson,"
			+ " a.lendDate lendDate, a.returnDate returnDate, a.depreciationRate depreciationRate, a.checker checker,"
			+ " a.imagePath imagePath,a.manageStatus manageStatus,a.manageLevel manageLevel,a.completeStatus completeStatus,a.assetCategory assetCategory,"
			+ " a.instrumentCategory instrumentCategory,a.fundSources fundSources,a.`projectName` projectName,a.contractCode contractCode,"
			+ " a.batch_code batchCode,a.power power,a.warrantyPeriod warrantyPeriod,a.scrapState scrapState,a.remark,"
			+ " a.repairState repairState,a.locationInfo locationInfo,a.macHour macHour,a.topicNo topicNo, a.isOverdue isOverdue, a.examineDate examineDate,a.dueDate dueDate,"
			+ " a.supplier supplier,a.unpackingDate unpackingDate,a.depreciationYear depreciationYear,a.isConnectCloud isConnectCloud,a.technicalIndex technicalIndex,"
			+ " a.is_military isMilitary,a.use_type use_type,b.assetName assetName,a.ggName ggName,a.accuracyClass accuracyClass,"
			+ " a.originalValue originalValue,a.country country,a.manufactor manufactor,b.createTime createTime,b.urlImage  "
			+ " FROM ins_assets_info a LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg "
			+ "  $order";

	@Override
	public NutMap getAssetsDataWith(Ins_Assets vo, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, Object object2, String originalValue2) {
		NutMap re = new NutMap();
		System.out.println(SQL);
		Sql sql = Sqls.queryRecord(SQL);
		// 添加排序功能
		if (order != null && order.size() > 0) {
			for (DataTableOrder or : order) {
				DataTableColumn col = columns.get(or.getColumn());
				String name = col.getData();
				//String data = col.getName();
				if("borrowdepart".equals(name)){
					name = " CONVERT( c.`name` USING gbk)";
				}else if("chargeperson".equals(name)) {
					name = "CONVERT( d.username USING GBK)";
				}else if("assetname".equals(name)){
					name = "CONVERT( assetName USING GBK)";
				}else if("opat".equals(name)){
					name = "opat";
				}else if("originalvalue".equals(name)){
					name = "originalvalue";
				}else if("deviceversion".equals(name)){
					name = "a.deviceversion";
				}else {
					name = "CONVERT("+name+" USING GBK)";

				}
				cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
			}
		}
		sql.setVar("order", cnd);
		re = data(length, start, draw, sql, sql);
		return re;
	}

	/*@Override
        public NutMap getAssetsDataWith(AssetsVoDel vo, int length, int start, int draw, List<DataTableOrder> order,
                                        List<DataTableColumn> columns, Cnd cnd, Object object2, String originalValue2) {
            NutMap re = new NutMap();
            cnd = Cnd.NEW();

            if (Strings.isNotBlank(vo.getAssetCode())) {
                cnd.and("a.assetCode", "like", "%" + vo.getAssetCode().trim() + "%");
            }
            if (vo.getAssetType() != null && vo.getAssetType() != 0) {
                cnd.and("a.assetType", "=", vo.getAssetType());
            }
            if (Strings.isNotBlank(vo.getDeviceVersion())) {
                cnd.and("b.deviceVersion", "like", "%" + vo.getDeviceVersion().trim() + "%");
            }
            if (Strings.isNotBlank(vo.getAssetName())) {
                cnd.and("b.assetName", "like", "%" + vo.getAssetName().trim() + "%");
            }
            if (Strings.isNotBlank(vo.getGgName())) {
                cnd.and("a.ggName", "like", "%" + vo.getGgName().trim() + "%");
            }
            if (Strings.isNotBlank(vo.getCountry())) {
                cnd.and("a.country", "like", "%" + vo.getCountry().trim() + "%");
            }
            if (vo.getAccuracyClass() != null) {
                cnd.and("a.accuracyClass", "=", vo.getAccuracyClass());
            }
            // 原值索引
            if (Strings.isNotBlank(vo.getOriginalValue1()) && Strings.isNotBlank(vo.getOriginalValue2())) {
                int value1 = Integer.parseInt(vo.getOriginalValue1().trim());
                int value2 = Integer.parseInt(vo.getOriginalValue2().trim());
                cnd.and("a.originalValue", "between", new Object[]{value1,value2});
            }
            if (Strings.isNotBlank(vo.getOriginalValue1())) {
                int value1 = Integer.parseInt(vo.getOriginalValue1().trim());
                cnd.and("a.originalValue", ">=", value1);
            }
            if (Strings.isNotBlank(vo.getOriginalValue2())) {
                int value2 = Integer.parseInt(vo.getOriginalValue2().trim());
                cnd.and("a.originalValue", "<=", value2);
            }

            if (Strings.isNotBlank(vo.getManufactor())) {
                cnd.and("a.manufactor", "like", "%" + vo.getManufactor().trim() + "%");
            }
            // 判断启用时间
            if (Strings.isNotBlank(vo.getEnableTime())) {
                ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getEnableTime());
                if (dateList != null && dateList.size() > 0) {
                    cnd.and("a.enableTime", "between", new Object[] { dateList.get(0), dateList.get(1) });
                }
            }
            // 判断检定日期
            if (Strings.isNotBlank(vo.getExamineDate())) {
                ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getEnableTime());
                if (dateList != null && dateList.size() > 0) {
                    cnd.and("a.examineDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
                }
            }
            // 判断到期检定日期
            if (Strings.isNotBlank(vo.getDueDate())) {
                ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getDueDate());
                if (dateList != null && dateList.size() > 0) {
                    cnd.and("a.dueDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
                }
            }
            if (Strings.isNotBlank(vo.getSerialNumber())) {
                cnd.and("a.serialNumber", "like", "%" + vo.getSerialNumber().trim() + "%");
            }
            if (Strings.isNotBlank(vo.getBorrowDepart())) {
                String childList = unitService.getChildList(vo.getBorrowDepart().trim());
                cnd.and("a.borrowDepart", "in", childList);

                *//*cnd.and("a.borrowDepart", "like", "%" + vo.getBorrowDepart().trim() + "%");*//*
		}
		if (Strings.isNotBlank(vo.getChargePerson())) {
			cnd.and("a.chargePerson", "=", vo.getChargePerson().trim());
		}
		// 判断生产时间
		if (Strings.isNotBlank(vo.getFactoryTime())) {
			ArrayList<Date> dateList = (ArrayList<Date>) DateUtil.getBetweenAndTime(vo.getFactoryTime());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.factoryTime", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断借出时间
		if (Strings.isNotBlank(vo.getLendDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getLendDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.lendDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断归还时间
		if (Strings.isNotBlank(vo.getReturnDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getReturnDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.returnDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		if (Strings.isNotBlank(vo.getChecker())) {
			cnd.and("a.checker", "like", "%" + vo.getChecker().trim() + "%");
		}
		if (vo.getManageStatus() != null) {
			cnd.and("a.manageStatus", "=", vo.getManageStatus());
		}
		if (vo.getManageLevel() != null) {
			cnd.and("a.manageLevel", "=", vo.getManageLevel());
		}
		if (vo.getAssetCategory() != null) {

			cnd.and("a.assetCategory", "=", vo.getAssetCategory());
		}
		if (vo.getInstrumentCategory() != null) {
			cnd.and("a.instrumentCategory", "=", vo.getInstrumentCategory());
		}
		if (vo.getFundSources() != null) {
			cnd.and("a.fundSources", "=", vo.getFundSources());
		}
		if (vo.getIsLend() != null) {
			cnd.and("a.isLend", "=", vo.getIsLend());
		}
		if (vo.getIsOverdue() != null) {
			cnd.and("a.isOverDue", "=", vo.getIsOverdue());
		}

		if (Strings.isNotBlank(vo.getContractCode())) {
			cnd.and("a.contractCode", "like", "%" + vo.getContractCode().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getBatch_code())) {
			cnd.and("a.batch_code", "like", "%" + vo.getBatch_code().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getPower())) {
			cnd.and("a.power", "=", vo.getPower().trim());
		}
		if (Strings.isNotBlank(vo.getWarrantyPeriod())) {
			cnd.and("a.warrantyPeriod", "like", "%" + vo.getWarrantyPeriod().trim() + "%");
		}

		if (Strings.isNotBlank(vo.getRemark())) {
			cnd.and("a.remark", "like", "%" + vo.getRemark().trim() + "%");
		}
		if (vo.getScrapState() != null) {
			cnd.and("a.scrapState", "=", vo.getScrapState());
		}
		if (vo.getRepairState() != null) {
			cnd.and("a.repairState", "=", vo.getRepairState());
		}
		if (vo.getUse_type() != null) {
			cnd.and("a.use_type", "=", vo.getUse_type());
		}

		if (Strings.isNotBlank(vo.getLocationInfo())) {
			cnd.and("a.locationInfo", "like", "%" + vo.getLocationInfo().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getProjectName())) {
			cnd.and("a.projectName", "like", "%" + vo.getProjectName().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getMacHour())) {
			cnd.and("a.macHour", "like", "%" + vo.getMacHour().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getSupplier())) {
			cnd.and("a.supplier", "like", "%" + vo.getSupplier().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getTopicNo())) {
			cnd.and("a.topicNo", "like", "%" + vo.getTopicNo().trim() + "%");
		}
		// 判断归还时间
		if (Strings.isNotBlank(vo.getUnpackingDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getUnpackingDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.unpackingDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		if (Strings.isNotBlank(vo.getDepreciationRate())) {
			cnd.and("a.depreciationRate", "like", "%" + vo.getDepreciationRate().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getDepreciationYear())) {
			cnd.and("a.depreciationYear", "like", "%" + vo.getDepreciationYear().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getIs_military())) {
			cnd.and("a.is_military", "like", "%" + vo.getIs_military().trim() + "%");
		}
		if (vo.getCompleteStatus() != null ) {
			cnd.and("a.completeStatus", "=", vo.getCompleteStatus());
		}

		// 查询sql
		Sql sql = Sqls.queryRecord(SQL);
		// 添加排序功能
		if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                DataTableColumn col = columns.get(or.getColumn());
                String name = col.getData();
                //String data = col.getName();
                if("borrowdepart".equals(name)){
                	name = " CONVERT( c.`name` USING gbk)";
                }else if("chargeperson".equals(name)) {
                	name = "CONVERT( d.username USING GBK)";
                }else if("assetname".equals(name)){
                	name = "CONVERT( assetName USING GBK)";
                }else if("opat".equals(name)){
                	name = "opat";
                }else if("originalvalue".equals(name)){
                	name = "originalvalue";
                }else if("deviceversion".equals(name)){
                	name = "a.deviceversion";
                }else {
                	name = "CONVERT("+name+" USING GBK)";
                	
                }
                cnd.orderBy(Sqls.escapeSqlFieldValue(name).toString(), or.getDir());
            }
        }
		sql.setVar("order", cnd);
 		re = data(length, start, draw, sql, sql);
		return re;
	}
*/
	@Override
	public String getAssetCodeList(String assetCode,String id) {
		String state = "";
		/*// 如果这个资产有其他任何关联则不能修改,只能是录入时输错了 让修改
		List<String> assetCodeList = assetMouthReportService.assetCodeList(null, null);
		if (assetCodeList.contains(assetCode)) {
			return "-1";	//表示此资产有关联记录(维修,封存,借还登记录)
		}*/
		List<Ins_Assets> assetList = dao().query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode).and("id","<>",id));
		
		//数据库里有这个数据,并且没有绑定此台设备 没有关联信息并且这个统一编号是他自己
		if(assetList.size() == 0 ){
			state = "0";
		}else {
			state =  "1";	// 资产编号已经存在了.
		} 
		return state;
	}
	
	@Override
	public boolean getAssetCodeListAdd(String assetCode) {
		List<Ins_Assets> assetList = dao().query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
		if (assetList.size() == 0) {
			return true;	// 插入
		}
		return false;
	}

	@Override
	public AssetsForm getAssetAndRuleInfo(Integer id) {
		Sql sql = Sqls.create(EDIT_SQL + " where a.id =  @id");
		sql.setParam("id", id);
		Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);

		AssetsForm assetsInfo = sql.getList(AssetsForm.class).get(0);
		return assetsInfo;
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

	public List<AssetsForm> getExportList() {
		String $sql = SQL + " ORDER BY a.opAt DESC";
		Sql sql = Sqls.create($sql);
		Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);

		return sql.getList(AssetsForm.class);
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
	public List<AssetsForm> exportAssetsInfo(String[] ids, HttpServletRequest request, HttpServletResponse response, Map<String, String> exportmenu, Ins_Assets vo) {
		return null;
	}

	/*@Override
	public List<AssetsForm> exportAssetsInfo(String[] ids, HttpServletRequest request, HttpServletResponse response,
			Map<String, String> exportmenu,AssetsVoDel vo) {
*//*		// 创建一个工作簿
		XSSFWorkbook wb = new XSSFWorkbook();
		// 创建一个sheet
		XSSFSheet sheet = wb.createSheet();
		// 初始化行号 列号
		Row nRow = null;
		Cell nCell = null;

		// 创建样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		XSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(HSSFColor.LIME.index);// 背景色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 创建表头
		XSSFRow rowt = sheet.createRow(0);
		rowt.setHeightInPoints(25);

		int rowNo = 0; // 行号
		int colNo = 1; // 列号

		StringBuilder sb = new StringBuilder("SELECT ");
		// 构造一个map用来判断是否选择了这个属性
		Map<String, String> map = new HashMap<>();
		XSSFCell cellt_X = rowt.createCell(0);
		cellt_X.setCellValue("序号");
		cellt_X.setCellStyle(cellStyle);
		// 获取采集器的信息
		for (int i = 0; i < ids.length; i++) {
			switch (ids[i]) {

			case "1":
				sb.append("a.assetCode assetCode,");
				// 创建单元格(表头)
				XSSFCell cellt0 = rowt.createCell(i+1);
				cellt0.setCellValue("统一编号");
				cellt0.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "2":
				sb.append("b.assetName assetName,");
				XSSFCell cellt1 = rowt.createCell(i+1);
				cellt1.setCellValue("仪器名称");
				cellt1.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "3":
				sb.append("a.deviceVersion deviceVersion,");
				XSSFCell cellt2 = rowt.createCell(i+1);
				cellt2.setCellValue("型号");
				cellt2.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "4":
				sb.append("a.ggName ggName,");
				XSSFCell cellt3 = rowt.createCell(i+1);
				cellt3.setCellValue("规格");
				cellt3.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "5":
				sb.append("a.assetType assetType,");
				XSSFCell cellt4 = rowt.createCell(i+1);
				cellt4.setCellValue("类别");
				cellt4.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "6":
				sb.append("c.`name` borrowDepart,");
				XSSFCell cellt5 = rowt.createCell(i+1);
				cellt5.setCellValue("使用单位");
				cellt5.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "7":
				sb.append("d.username chargePerson,");
				XSSFCell cellt6 = rowt.createCell(i+1);
				cellt6.setCellValue("责任人");
				cellt6.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;

			case "8":
				sb.append("a.originalValue originalValue,");
				XSSFCell cellt7 = rowt.createCell(i+1);
				cellt7.setCellValue("原值");
				cellt7.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "9":
				sb.append("a.serialNumber serialNumber,");
				XSSFCell cellt8 = rowt.createCell(i+1);
				cellt8.setCellValue("出厂编号");
				cellt8.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "10":
				sb.append("a.lendDate lendDate,");
				XSSFCell cellt9 = rowt.createCell(i+1);
				cellt9.setCellValue("借用日期");
				cellt9.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "11":
				sb.append("a.returnDate returnDate,");
				XSSFCell cellt10 = rowt.createCell(i+1);
				cellt10.setCellValue("归还日期");
				cellt10.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "12":
				sb.append("a.locationInfo locationInfo,");
				XSSFCell cellt11 = rowt.createCell(i+1);
				cellt11.setCellValue("位置信息");
				cellt11.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;

			case "13":
				sb.append("a.contractCode contractCode,");
				XSSFCell cellt12 = rowt.createCell(i+1);
				cellt12.setCellValue("合同号");
				cellt12.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "14":
				sb.append("a.batch_code batchCode,");
				XSSFCell cellt13 = rowt.createCell(i+1);
				cellt13.setCellValue("批件序号");
				cellt13.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "15":
				sb.append("a.supplier supplier,");
				XSSFCell cellt14 = rowt.createCell(i+1);
				cellt14.setCellValue("供货单位");
				cellt14.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "16":
				sb.append("a.projectName projectName,");
				XSSFCell cellt15 = rowt.createCell(i+1);
				cellt15.setCellValue("项目名称");
				cellt15.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "17":
				sb.append("a.fundSources fundSources,");
				XSSFCell cellt16 = rowt.createCell(i+1);
				cellt16.setCellValue("基金来源");
				cellt16.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;

			case "18":
				sb.append("a.power power,");
				XSSFCell cellt17 = rowt.createCell(i+1);
				cellt17.setCellValue("功率");
				cellt17.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "19":
				sb.append("a.macHour macHour,");
				XSSFCell cellt18 = rowt.createCell(i+1);
				cellt18.setCellValue("台时信息");
				cellt18.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "20":
				sb.append("a.manageStatus manageStatus,");
				XSSFCell cellt19 = rowt.createCell(i+1);
				cellt19.setCellValue("管理状态");
				cellt19.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "21":
				sb.append("a.manageLevel manageLevel,");
				XSSFCell cellt20 = rowt.createCell(i+1);
				cellt20.setCellValue("管理级别");
				cellt20.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "22":
				sb.append("a.assetCategory assetCategory,");
				XSSFCell cellt21 = rowt.createCell(i+1);
				cellt21.setCellValue("资产类别");
				cellt21.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "23":
				sb.append("a.repairState repairState,");
				XSSFCell cellt22 = rowt.createCell(i+1);
				cellt22.setCellValue("维修状态");
				cellt22.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "24":
				sb.append("a.enableTime enableTime,");
				XSSFCell cellt23 = rowt.createCell(i+1);
				cellt23.setCellValue("启用日期");
				cellt23.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "25":
				sb.append("a.factoryTime factoryTime,");
				XSSFCell cellt24 = rowt.createCell(i+1);
				cellt24.setCellValue("出厂年月");
				cellt24.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;

			case "26":
				sb.append("a.country country,");
				XSSFCell cellt25 = rowt.createCell(i+1);
				cellt25.setCellValue("国别");
				cellt25.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "27":
				sb.append("a.manufactor manufactor,");
				XSSFCell cellt26 = rowt.createCell(i+1);
				cellt26.setCellValue("厂家");
				cellt26.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "28":
				sb.append("a.accuracyClass accuracyClass,");
				XSSFCell cellt27 = rowt.createCell(i+1);
				cellt27.setCellValue("精度等级");
				cellt27.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "29":
				sb.append("a.topicNo topicNo,");
				XSSFCell cellt28 = rowt.createCell(i+1);
				cellt28.setCellValue("课题号");
				cellt28.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "30":
				sb.append("a.warrantyPeriod warrantyPeriod,");
				XSSFCell cellt29 = rowt.createCell(i+1);
				cellt29.setCellValue("保修期");
				cellt29.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "31":
				sb.append("a.depreciationYear depreciationYear,");
				XSSFCell cellt30 = rowt.createCell(i+1);
				cellt30.setCellValue("折扣年限");
				cellt30.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "32":
				sb.append("a.depreciationRate depreciationRate,");
				XSSFCell cellt31 = rowt.createCell(i+1);
				cellt31.setCellValue("年折旧率");
				cellt31.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "33":
				sb.append("a.unpackingDate unpackingDate,");
				XSSFCell cellt32 = rowt.createCell(i+1);
				cellt32.setCellValue("开箱日期");
				cellt32.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "34":
				sb.append("a.examineDate examineDate,");
				XSSFCell cellt33 = rowt.createCell(i+1);
				cellt33.setCellValue("检定日期");
				cellt33.setCellStyle(cellStyle);;
				map.put(ids[i], ids[i]);
				break;
			case "35":
				sb.append("a.dueDate dueDate,");
				XSSFCell cellt34 = rowt.createCell(i+1);
				cellt34.setCellValue("到期检定日期");
				cellt34.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "36":
				sb.append("a.use_type useType,");
				XSSFCell cellt35 = rowt.createCell(i+1);
				cellt35.setCellValue("用途");
				cellt35.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "37":
				sb.append("a.checker checker,");
				XSSFCell cellt36 = rowt.createCell(i+1);
				cellt36.setCellValue("验收人");
				cellt36.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "38":
				sb.append("a.isOverdue isOverdue,");
				XSSFCell cellt37 = rowt.createCell(i+1);
				cellt37.setCellValue("是否过期");
				cellt37.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "39":
				sb.append("a.is_military isMilitary,");
				XSSFCell cellt38 = rowt.createCell(i+1);
				cellt38.setCellValue("军工关键设备");
				cellt38.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "40":
				sb.append("a.isConnectCloud isConnectCloud,");
				XSSFCell cellt39 = rowt.createCell(i+1);
				cellt39.setCellValue("是否连接云网");
				cellt39.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "41":
				sb.append("a.scrapState scrapState,");
				XSSFCell cellt40 = rowt.createCell(i+1);
				cellt40.setCellValue("报废状态");
				cellt40.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "42":
				sb.append("a.technicalIndex technicalIndex,");
				XSSFCell cellt41 = rowt.createCell(i+1);
				cellt41.setCellValue("技术指标");
				cellt41.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;

			case "43":
				sb.append("a.isLend isLend,");
				XSSFCell cellt42 = rowt.createCell(i+1);
				cellt42.setCellValue("是否借用");
				cellt42.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "44":
				sb.append("a.instrumentCategory instrumentCategory,");
				XSSFCell cellt43 = rowt.createCell(i+1);
				cellt43.setCellValue("仪器类别");
				cellt43.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "45":
				sb.append("a.completeStatus completeStatus,");
				XSSFCell cellt44 = rowt.createCell(i+1);
				cellt44.setCellValue("完好状态");
				cellt44.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			case "47":
				sb.append("a.remark remark,");
				XSSFCell cellt47 = rowt.createCell(i+1);
				cellt47.setCellValue("备注");
				cellt47.setCellStyle(cellStyle);
				map.put(ids[i], ids[i]);
				break;
			default:
				sb.append("a.remark remark,");
				XSSFCell cellt44 = rowt.createCell(i+1);
				cellt44.setCellValue("备注");
				cellt44.setCellStyle(cellStyle);
				break;
			}
		}*//*
		Cnd cnd = Cnd.NEW();
		if (Strings.isNotBlank(vo.getAssetCode())) {
			cnd.and("a.assetCode", "like", "%" + vo.getAssetCode().trim() + "%");
		}
		if (vo.getAssetType() != null && vo.getAssetType() != 0) {
			cnd.and("a.assetType", "=", vo.getAssetType());
		}
		if (Strings.isNotBlank(vo.getDeviceVersion())) {
			cnd.and("b.deviceVersion", "like", "%" + vo.getDeviceVersion().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getAssetName())) {
			cnd.and("b.assetName", "like", "%" + vo.getAssetName().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getGgName())) {
			cnd.and("a.ggName", "like", "%" + vo.getGgName().trim() + "%");
		}
		if (vo.getAccuracyClass() != null) {
			cnd.and("a.accuracyClass", "=", vo.getAccuracyClass());
		}
		// 原值索引
		if (Strings.isNotBlank(vo.getOriginalValue1())) {
			int value1 = Integer.parseInt(vo.getOriginalValue1().trim());
			int value2 = Integer.parseInt(vo.getOriginalValue2().trim());
			cnd.and("a.originalValue", "between", new Object[]{value1,value2});
		}

		if (Strings.isNotBlank(vo.getManufactor())) {
			cnd.and("a.manufactor", "like", "%" + vo.getManufactor().trim() + "%");
		}
		// 判断启用时间
		if (Strings.isNotBlank(vo.getEnableTime())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getEnableTime());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.enableTime", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断检定日期
		if (Strings.isNotBlank(vo.getExamineDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getEnableTime());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.examineDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断到期检定日期
		if (Strings.isNotBlank(vo.getDueDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getDueDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.dueDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		if (Strings.isNotBlank(vo.getSerialNumber())) {
			cnd.and("a.serialNumber", "like", "%" + vo.getSerialNumber().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getBorrowDepart())) {
			String childList = unitService.getChildList(vo.getBorrowDepart().trim());
        	cnd.and("borrowDepart", "in", childList);

			*//*cnd.and("a.borrowDepart", "like", "%" + vo.getBorrowDepart().trim() + "%");*//*
		}
		if (Strings.isNotBlank(vo.getChargePerson())) {
			cnd.and("chargePerson", "=", vo.getChargePerson().trim());
		}
		// 判断生产时间
		if (Strings.isNotBlank(vo.getFactoryTime())) {
			ArrayList<Date> dateList = (ArrayList<Date>) DateUtil.getBetweenAndTime(vo.getFactoryTime());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.factoryTime", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断借出时间
		if (Strings.isNotBlank(vo.getLendDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getLendDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.lendDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		// 判断归还时间
		if (Strings.isNotBlank(vo.getReturnDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getReturnDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.returnDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		if (Strings.isNotBlank(vo.getChecker())) {
			cnd.and("a.checker", "like", "%" + vo.getChecker().trim() + "%");
		}
		if (vo.getManageStatus() != null) {
			cnd.and("a.manageStatus", "=", vo.getManageStatus());
		}
		if (vo.getManageLevel() != null) {
			cnd.and("a.manageLevel", "=", vo.getManageLevel());
		}
		if (vo.getAssetCategory() != null) {

			cnd.and("a.assetCategory", "=", vo.getAssetCategory());
		}
		if (vo.getInstrumentCategory() != null) {
			cnd.and("a.instrumentCategory", "=", vo.getInstrumentCategory());
		}
		if (vo.getFundSources() != null) {
			cnd.and("a.fundSources", "=", vo.getFundSources());
		}
		if (vo.getIsLend() != null) {
			cnd.and("a.isLend", "=", vo.getIsLend());
		}
		if (vo.getIsOverdue() != null) {
			cnd.and("a.isOverDue", "=", vo.getIsOverdue());
		}

		if (Strings.isNotBlank(vo.getContractCode())) {
			cnd.and("a.contractCode", "like", "%" + vo.getContractCode().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getBatch_code())) {
			cnd.and("a.batch_code", "like", "%" + vo.getBatch_code().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getPower())) {
			cnd.and("a.power", "=", vo.getPower().trim());
		}
		if (Strings.isNotBlank(vo.getWarrantyPeriod())) {
			cnd.and("a.warrantyPeriod", "like", "%" + vo.getWarrantyPeriod().trim() + "%");
		}

		if (Strings.isNotBlank(vo.getRemark())) {
			cnd.and("a.remark", "like", "%" + vo.getRemark().trim() + "%");
		}
		if (vo.getScrapState() != null) {
			cnd.and("a.scrapState", "=", vo.getScrapState());
		}
		if (vo.getRepairState() != null) {
			cnd.and("a.repairState", "=", vo.getRepairState());
		}
		if (vo.getUse_type() != null) {
			cnd.and("a.use_type", "=", vo.getUse_type());
		}

		if (Strings.isNotBlank(vo.getLocationInfo())) {
			cnd.and("a.locationInfo", "like", "%" + vo.getLocationInfo().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getProjectName())) {
			cnd.and("a.projectName", "like", "%" + vo.getProjectName().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getMacHour())) {
			cnd.and("a.macHour", "like", "%" + vo.getMacHour().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getSupplier())) {
			cnd.and("a.supplier", "like", "%" + vo.getSupplier().trim() + "%");
		}
		// 判断归还时间
		if (Strings.isNotBlank(vo.getUnpackingDate())) {
			ArrayList<Date> dateList = DateUtil.getBetweenAndTimeWithHHmmss(vo.getUnpackingDate());
			if (dateList != null && dateList.size() > 0) {
				cnd.and("a.unpackingDate", "between", new Object[] { dateList.get(0), dateList.get(1) });
			}
		}
		if (Strings.isNotBlank(vo.getDepreciationRate())) {
			cnd.and("a.depreciationRate", "like", "%" + vo.getDepreciationRate().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getDepreciationYear())) {
			cnd.and("a.depreciationYear", "like", "%" + vo.getDepreciationYear().trim() + "%");
		}
		if (Strings.isNotBlank(vo.getIs_military())) {
			cnd.and("a.is_military", "like", "%" + vo.getIs_military().trim() + "%");
		}
		// 拼接sql
		//String sql = sb.substring(0, sb.lastIndexOf(","));

		String sql="SELECT * FROM ins_assets_info a LEFT JOIN ins_assets_version b ON a.deviceVersion = b.deviceVersionOrg  LEFT JOIN sys_unit c ON a.borrowDepart = c.id LEFT JOIN sys_user d ON a.chargePerson = d.id $order";

		// 执行sql
		Sql sql2 = Sqls.queryRecord(sql);
		//按照操作时间降序排序
		cnd.orderBy("a.opat","desc");
		sql2.setVar("order", cnd);
		Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
		sql2.setEntity(entity);
		//设置回调
		sql2.setCallback(Sqls.callback.entities());
		// 执行sql
		dao().execute(sql2);

		// 获取数据
		List<AssetsForm> assetsInfoList = sql2.getList(AssetsForm.class);
		*//*try {
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*//*
		return assetsInfoList ;
		//跳过静态表格头
	*//*	rowNo++;
		// 进行判断,往excel里写入数据
		for (int j = 0; j < assetsInfoList.size(); j++) {
			colNo = 0;				//初始化
			AssetsForm assetsInfo = assetsInfoList.get(j);
			// 获取一个行对象
			nRow = sheet.createRow(rowNo++);
			nRow.setHeightInPoints(24);
			//序号
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(j+1);
			if(Strings.isNotBlank(assetsInfo.getAssetCode()) || map.containsKey("1")){	//统一编号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getAssetCode());
			}
			if(Strings.isNotBlank(assetsInfo.getAssetName()) || map.containsKey("2")){	//名称
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getAssetName());
			}
			if(Strings.isNotBlank(assetsInfo.getDeviceVersion()) || map.containsKey("3")){	// 型号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getDeviceVersion());
			}
			if(Strings.isNotBlank(assetsInfo.getGgName()) || map.containsKey("4")){	//规格
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getGgName());
			}
			if(assetsInfo.getAssetType() != null || map.containsKey("5")){	// 类别
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getAssetType();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("全部");
				}else if(1 == num){
					nCell.setCellValue("设备");
				}else if(2 == num){
					nCell.setCellValue("仪器");
				}else {
					nCell.setCellValue("工量具");
				}
			}
			if(Strings.isNotBlank(assetsInfo.getBorrowDepart())  || map.containsKey("6")){	// 使用单位
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getBorrowDepart());
			}
			if(Strings.isNotBlank(assetsInfo.getChargePerson())  || map.containsKey("7")){	// 责任人
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getChargePerson());
			}
			if(Strings.isNotBlank(assetsInfo.getOriginalValue()) || map.containsKey("8")){	// 原值
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getOriginalValue());
			}
			if(Strings.isNotBlank(assetsInfo.getSerialNumber()) || map.containsKey("9")){	// 出厂编号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getSerialNumber());
			}
			// 借用日期
			if(assetsInfo.getLendDate() != null  || map.containsKey("10")){
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date getLendDate = assetsInfo.getLendDate();
				String time = DateUtil.formatDateTime(getLendDate);
				nCell.setCellValue(time);
			}
			// 归还日期
			if(assetsInfo.getReturnDate() != null  || map.containsKey("11")){
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date getReturnDate = assetsInfo.getReturnDate();
				String time = DateUtil.formatDateTime(getReturnDate);
				nCell.setCellValue(time);
			}
			if(Strings.isNotBlank(assetsInfo.getLocationInfo()) || map.containsKey("12")){	// 位置信息
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getLocationInfo());
			}


			if(Strings.isNotBlank(assetsInfo.getContractCode()) || map.containsKey("13")){	// 合同号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getContractCode());
			}
			if(Strings.isNotBlank(assetsInfo.getBatch_code()) || map.containsKey("14")){	// 批件序号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getBatch_code());
			}
			if(Strings.isNotBlank(assetsInfo.getSupplier()) || map.containsKey("15")){	// 供货单位
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getSupplier());
			}
			if(Strings.isNotBlank(assetsInfo.getProjectName()) || map.containsKey("16")){	// 项目名称
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getProjectName());
			}
			if(assetsInfo.getFundSources() != null || map.containsKey("17")){				// 资金来源
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getFundSources();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("拨款");
				} else {
					nCell.setCellValue("自购");
				}
			}

			if(Strings.isNotBlank(assetsInfo.getPower()) || map.containsKey("18")){	// 功率
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getPower());
			}
			if(Strings.isNotBlank(assetsInfo.getMacHour()) || map.containsKey("19")){	// 台时信息
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getMacHour());
			}
			if(assetsInfo.getManageStatus() != null || map.containsKey("20")){	// 管理状态
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getManageStatus();
				if(num == null){
					nCell.setCellValue("");
				}else if( 0 == num){
					nCell.setCellValue("再用");
				}else if(1 == num){
					nCell.setCellValue("封存");
				}else if(2 == num){
					nCell.setCellValue("闲置");
				}else {
					nCell.setCellValue("禁用");
				}
			}
			if(assetsInfo.getManageLevel() != null || map.containsKey("21")){	// 管理级别
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getManageLevel();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("厂(所)管");
				}else if(1 == num){
					nCell.setCellValue("院管");
				}else {
					nCell.setCellValue("总公司部管");
				}
			}
			if(assetsInfo.getAssetCategory() != null || map.containsKey("22")){	// 资产类别
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getAssetCategory();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("固定资产");
				}else if(1 == num){
					nCell.setCellValue("低值");
				}else if(2 == num){
					nCell.setCellValue("在建");
				}else if(3 == num){
					nCell.setCellValue("附件");
				}else {
					nCell.setCellValue("零值");
				}
			}
			if(assetsInfo.getRepairState() != null || map.containsKey("23")){	// 维修状态
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getRepairState();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("维修中");
				}else if(1 == num) {
					nCell.setCellValue("已完成");
				}else {
					nCell.setCellValue("");
				}
			}
			if(assetsInfo.getEnableTime() != null || map.containsKey("24")){	// 启用日期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date enabelTime = assetsInfo.getEnableTime();
				String time = DateUtil.formatDateTime(enabelTime);
				nCell.setCellValue(time);
			}
			if(assetsInfo.getFactoryTime() != null || map.containsKey("25")){	// 出厂日期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date factoryTime = assetsInfo.getFactoryTime();
				String time = DateUtil.formatDateTime(factoryTime);
				nCell.setCellValue(time);
			}

			if(Strings.isNotBlank(assetsInfo.getCountry()) || map.containsKey("26")){	// 国别
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getCountry());
			}
			if(Strings.isNotBlank(assetsInfo.getManufactor()) || map.containsKey("27")){	// 厂家
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getManufactor());
			}
			if(assetsInfo.getAccuracyClass() != null || map.containsKey("28")){	// 精度等级
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getAccuracyClass();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num ){
					nCell.setCellValue("特级");
				}else if(1 == num){
					nCell.setCellValue("一级");
				}else{
					nCell.setCellValue("二级");
				}
			}
			if(Strings.isNotBlank(assetsInfo.getTopicNo()) || map.containsKey("29")){	// 课题号
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getTopicNo());
			}
			if(Strings.isNotBlank(assetsInfo.getWarrantyPeriod()) || map.containsKey("30")){	// 保修期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getWarrantyPeriod());
			}
			if(Strings.isNotBlank(assetsInfo.getDepreciationYear()) || map.containsKey("31")){	// 折旧年限
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getDepreciationYear());
			}
			if(Strings.isNotBlank(assetsInfo.getDepreciationRate()) || map.containsKey("32")){	// 年折旧率
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getDepreciationRate());
			}
			// 开箱日期
			if(assetsInfo.getUnpackingDate() != null  || map.containsKey("33")){
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date getUnpackingDate = assetsInfo.getUnpackingDate();
				String time = DateUtil.formatDateTime(getUnpackingDate);
				nCell.setCellValue(time);
			}
			if(assetsInfo.getExamineDate() != null  || map.containsKey("34")){	// 检定日期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date getExamineDate = assetsInfo.getExamineDate();
				String time = DateUtil.formatDateTime(getExamineDate);
				nCell.setCellValue(time);
			}
			if(assetsInfo.getDueDate() != null  || map.containsKey("35")){	// 到期检定日期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				Date getDueDate = assetsInfo.getDueDate();
				String time = DateUtil.formatDateTime(getDueDate);
				nCell.setCellValue(time);
			}
			if(assetsInfo.getUse_type() != null || map.containsKey("36")){	// 用途
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getUse_type();
				if(num == null){
					nCell.setCellValue("");
				}else if( 0== num){
					nCell.setCellValue("科研类");
				}else if(1 == num){
					nCell.setCellValue("生产类");
				}else {
					nCell.setCellValue("测量类");
				}
			}
			if(Strings.isNotBlank(assetsInfo.getChecker()) || map.containsKey("37")){	// 验收人
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getChecker());
			}
			if(assetsInfo.getIsOverdue() != null && map.containsKey("38")){	// 是否过期
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getIsOverdue();
				if(num == null){
					nCell.setCellValue("");
				}else if( 0 == num){
					nCell.setCellValue("过期");
				}else {
					nCell.setCellValue("未过期");
				}
			}
			if(assetsInfo.getIsMilitary() != null || map.containsKey("39")){	// 军工关键设备
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getIsMilitary();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("是");
				}else {
					nCell.setCellValue("否");
				}
			}
			if(assetsInfo.getIsConnectCloud() != null || map.containsKey("40")){	// 是否链接云网
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getIsConnectCloud();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("是");
				}else {
					nCell.setCellValue("否");
				}
			}
			if(assetsInfo.getScrapState() != null || map.containsKey("41")){	// 报废状态
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getScrapState();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("待报废");
				} else if(1 == num) {
					nCell.setCellValue("报废");
				}else {
					nCell.setCellValue("");
				}
			}
			if(Strings.isNotBlank(assetsInfo.getTechnicalIndex()) || map.containsKey("42")){	// 技术指标
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getTechnicalIndex());
			}
			if(assetsInfo.getIsLend() != null && map.containsKey("43")){	// 是否借用
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getIsLend();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("是");
				}else {
					nCell.setCellValue("否");
				}
			}
			if(assetsInfo.getInstrumentCategory() != null || map.containsKey("44")){	// 仪器类别
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getInstrumentCategory();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("普通");
				}else if(1 == num){
					nCell.setCellValue("精大贵希");
				}else if(2 == num) {
					nCell.setCellValue("进口普通");
				}else {
					nCell.setCellValue("进口精大贵希");
				}
			}
			if(assetsInfo.getCompleteStatus() != null || map.containsKey("45")){	// 完好状态
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				// 进行判断
				Integer num = assetsInfo.getCompleteStatus();
				if(num == null){
					nCell.setCellValue("");
				}else if(0 == num){
					nCell.setCellValue("完好");
				}else {
					nCell.setCellValue("不完好");
				}
			}
			if(Strings.isNotBlank(assetsInfo.getRemark()) || map.containsKey("47")){	// 备注
				// 获取列对象
				nCell = nRow.createCell(colNo++);
				nCell.setCellValue(assetsInfo.getRemark());
			}
		}*//*
		//return wb;
	}
*/
	@Override
	public AssetsForm getAssetAndRuleInfoDetail(Integer id) {
		Sql sql = Sqls.create(SQL + " where a.id = @id");
		sql.setParam("id", id);
		Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);

		AssetsForm assetsInfo = sql.getList(AssetsForm.class).get(0);
		return assetsInfo;
	}

	// 更新设备的使用单位,责任人(对ins_device_info表进行操作)
	@Override
	public void updateChargePersonAndBorrowDepart(String assetCode, String borrowDepart, String chargePerson) {
		// 根据统一编号查出设备信息
		Ins_DeviceInfo deviceInfo = dao().fetch(Ins_DeviceInfo.class, Cnd.where("deviceCode", "=", assetCode));
		Ins_Assets assets = dao().fetch(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
		
		deviceInfo.setDeviceCode(assets.getAssetCode());
		deviceInfo.setAssetType(assets.getAssetType());
		deviceInfo.setBorrowDepart(borrowDepart);
		deviceInfo.setChargePerson(chargePerson);
		
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
		if(collectList.size() > 0){	// 有绑定
			return true;
		}
		return false;
	}

	@Override
	public void saveOrUpdate(List<Ins_Assets> assetsList) {
		//循环遍历操作
		for (Ins_Assets assets : assetsList) {
			//id不为null,进行更新
			if(assets.getId() != null){
				dao().updateIgnoreNull(assets);
			}else {//进行插入
				dao().insert(assets);
			}
		}
	}

	@Override
	public String getDeviceCode(Integer id) {
		Ins_Assets assets = dao().fetch(Ins_Assets.class, Cnd.where("id", "=", id));
		if(Strings.isNotBlank(assets.getAssetCode())){
			return assets.getAssetCode();
		}
		return null;
	}
	
	/**
	 * 获取资产表中的统一编号
	 */
	@Override
	public String getAssetCodeListByUnitId(String unitId) {
		 Sql sql = Sqls.create("SELECT DISTINCT(assetCode) FROM ins_assets_info WHERE assetType IN (1,2) AND (scrapState is null or scrapState !=1) $var ") ;
		 if(StringUtils.isNotBlank(unitId)){
			 StringBuilder bulid = new StringBuilder();
			 bulid.append("and borrowDepart = '").append(unitId).append("'") ;
			 sql.setVar("var",bulid) ;
		 }


		 sql = sql.setCallback(new SqlCallback() {

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<String> list = new ArrayList<>();
					JSONArray jarr = new JSONArray() ;
					while (rs.next()) {
						JSONObject jobj = new JSONObject() ;
						jobj.put("assetCode", rs.getString("assetCode")) ;
						jarr.put(jobj) ;
					}
					list.add(jarr.toString()) ;
					return list;
				}
			});
		  String str = dao().execute(sql).getList(String.class).get(0);
		  return str;
	}


	@Override
	public Map<String, AssetsForm> getAssetAndRuleInfoByAssetCodes(String assetCode) {
		Sql sql = Sqls.create(SQL +" $var");
		Criteria cri = Cnd.cri() ;
		cri.where().and("a.assetCode", "in", assetCode) ;
		sql.setVar("var", cri);
		Entity<AssetsForm> entity = dao().getEntity(AssetsForm.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		List<AssetsForm> list = sql.getList(AssetsForm.class);
		Map<String, AssetsForm> map = new HashMap<String, AssetsForm>() ;
		for(int i=0;i<list.size();i++){
			AssetsForm form = list.get(i) ;
			//map.put(form.getAssetCode(), form) ;
		}
		
		
		return map;
	}
	/**
	 * 根据统一编号(逗号分隔),获取资产实体bean列表
	 * @param deviceCodes
	 * @return
	 */
	@Override
	public List<Ins_Assets> getInsAssetBeanList(String deviceCodes) {
		Sql sql = Sqls.create("SELECT * FROM INS_ASSETS_INFO WHERE ASSETCODE IN ("+deviceCodes+")") ;
		Entity<Ins_Assets> entity = dao().getEntity(Ins_Assets.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);
		return sql.getList(Ins_Assets.class);
	}

	@Override
	public void updateInsAssetBeanLocationInfo(String deviceCode, String locationInfo) {
		dao().update(Ins_Assets.class, Chain.make("locationInfo",locationInfo),Cnd.where("assetCode","=",deviceCode));
	}


}
