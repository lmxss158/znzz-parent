package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.service.InsCollectService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.BCardForm;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.DeviceForm;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.DateUtil;

@IocBean(args = { "refer:dao" })
public class InsCollectServiceImpl extends BaseServiceImpl<Ins_Collect> implements InsCollectService {

	@Inject
	ScadaDeviceServcie scadaDeviceServcie;

	@Inject
	InsbcardService bcardService;
	
	@Inject
	private InsAssetsService assetsService;
	// private static String SQL = "SELECT a.id id, a.collectName collectName,
	// a.collectCode collectCode, b.deviceName deviceName, b.deviceVersion
	// deviceVersion, b.deviceCode deviceCode, b.borrowDepart borrowDepart,
	// b.chargePerson chargePerson, a.createTime createTime FROM ins_collect a
	// LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode";
	private static String $SQL = "SELECT a.id id, a.collectCode collectCode, b.deviceName deviceName, b.deviceVersion deviceVersion,"
			+ "b.deviceCode deviceCode, c.`name` borrowDepart, d.username chargePerson,"
			+ "a.createTime createTime FROM ins_collect a LEFT JOIN ins_device_info b ON a.deviceCode = b.deviceCode "
			+ "LEFT JOIN sys_unit c ON c.id = b.borrowDepart LEFT JOIN sys_user d ON d.id = b.chargePerson $condition";

	public InsCollectServiceImpl(Dao dao) {
		super(dao);
	}

	@Inject
	private SysUnitService unitService;

	@Override
	public NutMap getCollectDataWith(DeviceForm deviceForm, int length, int start, int draw, List<DataTableOrder> order,
			List<DataTableColumn> columns, Cnd cnd2, String linkName) {
		Cnd cnd = Cnd.NEW();
		
		// 采集器编号进行处理
		if (Strings.isNotBlank(deviceForm.getCollectCode())) {
			cnd.and("a.collectCode", "like", "%" + deviceForm.getCollectCode().trim() + "%");
		}
		// 统一编号进行处理
		if (Strings.isNotBlank(deviceForm.getDeviceCode())) {
			SqlExpressionGroup expressionGroup = Cnd.exps("a.deviceCode","like","%" + deviceForm.getDeviceCode().trim() + "%")
			.or("b.deviceCode", "like", "%" + deviceForm.getDeviceCode().trim() + "%")
		    .or("b.deviceName", "like", "%" + deviceForm.getDeviceCode().trim() + "%")
			.or("b.deviceVersion", "like", "%" + deviceForm.getDeviceCode().trim() + "%");
			cnd.where().and(expressionGroup);
		}
		// 使用单位
		if (Strings.isNotBlank(deviceForm.getBorrowDepart())) {
			String childList = unitService.getChildList(deviceForm.getBorrowDepart());
			cnd.and("borrowDepart", "in", childList);
		}
		// 责任人
		if (Strings.isNotBlank(deviceForm.getChargePerson())) {
			cnd.and("chargePerson", "=", deviceForm.getChargePerson().trim());
		}
		// 对传入日期进行处理
		ArrayList<Date> list = DateUtil.getBetweenAndTimeWithHHmmss(deviceForm.getCreateTime());
		if (list.size() > 0 && list != null) {
			// 对传入的时间进行处理
			cnd.and("a.createTime", "between", new Object[] { list.get(0), list.get(1) });
		}
		// 查询sql
		Sql sql = Sqls.create($SQL);
		// 添加排序功能
		if (order != null && order.size() > 0) {
			for (DataTableOrder or : order) {
				DataTableColumn col = columns.get(or.getColumn());
				cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), or.getDir());
			}
		}
		sql.setVar("order", cnd); 
		sql.setCondition(cnd);
		return data(length, start, draw, sql, sql);
	}

	@Override
	public List<String> getCollectCodeList(String collectCode) {

		// 自定义sql
		Sql sql = Sqls.create("SELECT collectCode FROM ins_collect WHERE collectCode = '" + collectCode + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("collectCode"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	@Override
	public List<String> getDeviceCodeList(String deviceCode) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT deviceCode FROM ins_collect WHERE deviceCode = '" + deviceCode + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("deviceCode"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	@Override
	public List<CollectBindDeviceBean> getDeviceInfo(String deviceCode) {
		// 自定义sql
		String sqlNew = "SELECT b.deviceName deviceName, b.deviceVersion deviceVersion, b.deviceCode deviceCode, "
				+ "c.`name` borrowDepart, d.username chargePerson FROM ins_device_info b "
				+ "LEFT JOIN sys_unit c ON c.id = b.borrowDepart LEFT JOIN sys_user d ON d.id = b.chargePerson $condition";
		Sql sql = Sqls.create(sqlNew);
		sql.setCondition(Cnd.where("b.deviceCode", "=", deviceCode));
		Entity<CollectBindDeviceBean> entity = dao().getEntity(CollectBindDeviceBean.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		/*
		 * // 设置回调函数 sql = sql.setCallback(new SqlCallback() {
		 * 
		 * @Override public Object invoke(Connection conn, ResultSet rs, Sql
		 * sql) throws SQLException { List<Ins_DeviceInfo> list = new
		 * ArrayList<>(); Ins_DeviceInfo deviceInfo = null; while (rs.next()) {
		 * deviceInfo = new Ins_DeviceInfo();
		 * deviceInfo.setDeviceName(rs.getString("deviceName"));
		 * deviceInfo.setDeviceVersion(rs.getString("deviceVersion"));
		 * deviceInfo.setBorrowDepart(rs.getString("borrowDepart"));
		 * deviceInfo.setChargePerson(rs.getString("chargePerson"));
		 * list.add(deviceInfo); } return list; } });
		 */

		dao().execute(sql);
		return sql.getList(CollectBindDeviceBean.class);
	}

	@Override
	public String insertList(List<CollectBindDeviceBean> collect_devicetList) {
		// 分别创建存放采集器和设备的list
		List<Ins_Collect> collects = new ArrayList<>();
		List<Ins_DeviceState> states = new ArrayList<>();
		// 存放临时bean
		Map<String, CollectBindDeviceBean> tempMap = new HashMap<String, CollectBindDeviceBean>();

		// 创建采集器和设备对象
		Ins_Collect collect = null;
		Ins_DeviceState state = null;

		// 获取生产编号和统一编号
		Map<String, String> map = bcardService.convertClollectCodeAndDeviceCode(collect_devicetList);

		String proCodeStr = "";
		String unExistStr = "";
		String errorStr = "";
		String unitUnExistStr = "";
		// 批量插入完成之后进行scada校验
		for (CollectBindDeviceBean checkCollect : collect_devicetList) {
			String deviceCode = checkCollect.getDeviceCode();

			Ins_DeviceInfo info = dao().fetch(Ins_DeviceInfo.class, Cnd.where("deviceCode", "=", deviceCode));
			if (info == null) {
				unitUnExistStr += deviceCode + ",";
				break;// 没有单位的
			}

			String pcode = map.get(deviceCode);
			if (StringUtils.isBlank(pcode)) {
				unExistStr += deviceCode + ",";// 对应关系没找到
				break;// 对应关系找不见
			} else {
				proCodeStr += pcode + ",";
			}

			tempMap.put(deviceCode, checkCollect);
		}
		String re = "";
		if (StringUtils.isBlank(unExistStr) && StringUtils.isBlank(unitUnExistStr)) {
			String[] data = { StringUtils.strip(proCodeStr, ",") };
			String scardRe = scadaDeviceServcie.invoke(data, Globals.SETBCARDBATCH);

			JSONObject obj = new JSONObject(scardRe);
			JSONArray arr = obj.getJSONArray("deviceidList");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jobj = arr.getJSONObject(i);
				String procode = jobj.getString("deviceid");
				String deviceCode = map.get(procode);
				int num = jobj.getInt("result");
				if (num == 0) {// 失败
					errorStr += deviceCode + ",";
				} else {// 成功
					collect = new Ins_Collect();
					state = new Ins_DeviceState();
					CollectBindDeviceBean bean = tempMap.get(deviceCode);
					collect.setCollectCode(bean.getCollectCode());
					collect.setCollectName(bean.getCollectName());
					collect.setDeviceCode(bean.getDeviceCode());
					collect.setCreateTime(bean.getCreateTime());

					state.setDeviceCode(bean.getDeviceCode());
					state.setState(1); // 离线
					// int count =
					// dao().count(Ins_Collect.class,Cnd.where("deviceCode",
					// "=", deviceCode)) ;
					collects.add(collect);
					states.add(state);

				}

			}
			if (collects.size() > 0) {
				// 进行批量插入
				dao().insert(collects);
				dao().insert(states);
				for (Ins_Collect collectDevice : collects) {
					//设置链接云网状态为0(连接云网)
	            	assetsService.updateConnectCloud(0,collectDevice.getDeviceCode());
				}
			}

			if (StringUtils.isNotBlank(errorStr)) {
				errorStr = StringUtils.strip(errorStr, ",");
				re = "统一编号:" + errorStr + " 导入失败,请校验数据是否正确";
			}
			bcardService.delOrignCodeAndDeviceCodeKey();

		} else {
			if (StringUtils.isNotBlank(unitUnExistStr)) {
				unitUnExistStr = StringUtils.strip(unitUnExistStr, ",");
				re = "请校验统一编号:" + unitUnExistStr + " 的资产是否存在";
			} else {
				unExistStr = StringUtils.strip(unExistStr, ",");
				re = "请校验统一编号:" + unExistStr + " 对应的采集器编号是否存在";
			}
		}

		return re;
	}

	@Override
	public List<CollectBindDeviceBean> getExportList() {
		Sql sql = Sqls.create($SQL + " ORDER BY a.createTime DESC");
		Entity<CollectBindDeviceBean> entity = dao().getEntity(CollectBindDeviceBean.class);
		sql.setEntity(entity);
		sql.setCallback(Sqls.callback.entities());
		dao().execute(sql);

		return sql.getList(CollectBindDeviceBean.class);
	}

	@Override
	public NutMap getBCardList() {
		// sql语句
		String sql = "SELECT DISTINCT a.bcardCode, a.orignCode from ins_device_bcard a WHERE a.bcardCode NOT IN (SELECT collectCode FROM ins_collect)";
		// 创建sql
		Sql sqls = Sqls.create(sql);
		// 封装实体类
		sqls = sqls.setEntity(dao().getEntity(BCardForm.class));
		// 设置回调函数
		sqls.setCallback(Sqls.callback.entities());
		// 执行sql
		dao().execute(sqls);
		// 获取查询结果
		List<BCardForm> list = sqls.getList(BCardForm.class);
		// 封装到nutMap里
		NutMap re = new NutMap();
		re.addv("data", list);
		return re;
	}

	@Override
	public NutMap getDeviceCodeList() {
		// sql语句
		NutMap re = new NutMap();
		String sql = "SELECT a.* from ins_device_info a WHERE a.deviceCode NOT IN ( SELECT deviceCode from ins_collect ) ";
		// 创建sql
		Sql sqls = Sqls.queryRecord(sql);
		// 封装实体类
		sqls = sqls.setEntity(dao().getEntity(Ins_DeviceInfo.class));
		// 设置回调函数
		sqls.setCallback(Sqls.callback.entities());
		// 执行sql
		dao().execute(sqls);
		// 获取查询结果
		List<Ins_DeviceInfo> list = sqls.getList(Ins_DeviceInfo.class);
		// 封装到nutMap里
		re.addv("data", list);
		return re;

	}

	/**
	 * 测是否绑定
	 */
	@Override
	public boolean isBinding(String deviceCode) {
		boolean flag = false;
		int count = dao().count(Ins_Collect.class, Cnd.where("deviceCode", "=", deviceCode));
		if (count > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取所有受监控的设备统一编码
	 * 
	 * @return
	 */
	@Override
	public List<String> getMonitorDeviceCodeList() {

		Sql sql = Sqls.create("SELECT DISTINCT(deviceCode) from ins_collect");
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("deviceCode"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

}
