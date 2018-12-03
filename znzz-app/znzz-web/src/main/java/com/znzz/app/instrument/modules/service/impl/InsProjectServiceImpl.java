package com.znzz.app.instrument.modules.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import com.znzz.app.instrument.modules.models.Ins_ProjectInfo;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 项目管理实现类
 * 
 * @author chenzhongliang
 *
 */
@IocBean(args = { "refer:dao" })
public class InsProjectServiceImpl extends BaseServiceImpl<Ins_ProjectInfo> implements InsProjectService {

	public InsProjectServiceImpl(Dao dao) {
		super(dao);
	}

	@Override
	public List<String> getCodeList(String code, String type) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT code FROM ins_project_info WHERE code = '" + code + "' and type='" + type + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("code"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	/**
	 * 在资产表里查询是否存在当前项目下是否存在资产
	 * 
	 * @param
	 * @return
	 */
	@Override
	public boolean ExistInAsset(Integer id) {
		Sql sql = Sqls.create("select code from ins_project_info where id='" + id + "'");
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String projectcode = null;
				while (rs.next()) {
					projectcode = rs.getString("code");
				}
				return projectcode;
			}
		});
		
		String code = dao().execute(sql).getString(); // id对应项目编号

		// 根据编号 查询ins_assets_info表
		Sql sql2 = Sqls.create("select id from ins_assets_info where projectName='" + code + "'");
		// 设置回调函数
		sql2 = sql2.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("id"));
				}
				return list;
			}
		});
		List<String> list = dao().execute(sql2).getList(String.class);
		if (list != null && list.size() > 0) {
			// 当前id对应项目下存在资产
			return true;
		}
		return false;
	}

	@Override
	public boolean ExistInDevice(Integer id) {
		Sql sql = Sqls.create("select code from ins_project_info where id='" + id + "'");
		sql = sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				String projectcode = null;
				while (rs.next()) {
					projectcode = rs.getString("code");
				}
				return projectcode;
			}
		});
		
		String code = dao().execute(sql).getString(); // id对应项目编号
		List<String> list = getIdbyCode(code);
		if (list != null && list.size() > 0) {
			// 当前id对应项目下存在资产
			return true;
		}
		return false;
	}

	private List<String> getIdbyCode(String code) {
		// 根据编号 查询ins_device_facility表
		Sql sql2 = Sqls.create("select id from ins_device_facility where projectCode='" + code + "'");
		// 设置回调函数
		sql2 = sql2.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("id"));
				}
				return list;
			}
		});
		return dao().execute(sql2).getList(String.class);
	}

	@Override
	public List<String> checkID(String id, String name) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT code FROM ins_project_info WHERE name = '" + name + "' and id !='" + id + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("code"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	@Override
	public List<String> getNameList(String name, String type) {
		// 自定义sql
		Sql sql = Sqls.create("SELECT code FROM ins_project_info WHERE name = '" + name.trim() + "' and type='" + type + "'");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<>();
				while (rs.next()) {
					list.add(rs.getString("code"));
				}
				return list;
			}
		});

		return dao().execute(sql).getList(String.class);
	}

	@Override
	public void insertList(List<Ins_ProjectInfo> projectList) {
		dao().insert(projectList);
	}

	/**
	 * 检查该项目是否存在表中
	 */
	@Override
	public int isExist(String code) {
		int count = dao().count(Ins_ProjectInfo.class, Cnd.where("code", "=", code));
		return count;
	}

	@Override
	public void updateProjectInfo(List<Ins_ProjectInfo> projectList) {
		Ins_ProjectInfo queryProject = null;
		if (projectList != null && projectList.size() > 0) {
			for (Ins_ProjectInfo projectInfo : projectList) {
				queryProject = dao().fetch(Ins_ProjectInfo.class, Cnd.where("code", "=", projectInfo.getCode()));
				if (queryProject == null) {
					dao().insert(projectInfo);
				} else {
					projectInfo.setId(queryProject.getId());
					dao().update(projectInfo);
				}
			}
		}
	}

	@Override
	public void saveOrUpdate(List<Ins_ProjectInfo> projectList) {
		// 遍历进行更新或者插入项目信息
		for (Ins_ProjectInfo projectInfo : projectList) {
			// id不为null,进行更新操作
			if (projectInfo.getId() != null) {
				dao().update(projectInfo);
			} else {// 进行插入操作
				dao().insert(projectInfo);
			}
		}
	}

    @Override
    public List<Ins_ProjectInfo> getCodeAndName() {

		List<Ins_ProjectInfo> list =  dao().query(Ins_ProjectInfo.class,Cnd.where("type","=","0"));
        return list;
    }

	@Override
	public void updateList(List<Ins_ProjectInfo> projectList) {
		for (Ins_ProjectInfo projectInfo : projectList) {
			dao().updateIgnoreNull(projectInfo);
		}
	}

}
