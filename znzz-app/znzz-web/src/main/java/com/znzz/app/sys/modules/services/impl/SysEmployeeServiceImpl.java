package com.znzz.app.sys.modules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;

import com.znzz.app.sys.modules.models.Sys_employee;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysEmployeeService;
import com.znzz.framework.base.service.BaseServiceImpl;

@IocBean(args = {"refer:dao"})
public class SysEmployeeServiceImpl  extends BaseServiceImpl<Sys_user> implements SysEmployeeService {
    public SysEmployeeServiceImpl(Dao dao) {
        super(dao);
    }
	@Override
	public String insertList(List<Sys_user> employeeList) {
			//校验身份证号是否存在
			Sql sql = Sqls.create("select idNumber from Sys_user");
			sql = sql.setCallback(new SqlCallback() {

				@Override
				public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<String> list = new ArrayList<>();
					while(rs.next()) {
						list.add(rs.getString("idNumber"));
					}
					return list;
				}
			});
			List<String> list = dao().execute(sql).getList(String.class);
			List<Sys_user> insertList = new ArrayList<Sys_user>();
			List<Sys_user> updateList = new ArrayList<Sys_user>();
			
			for(Sys_user e :employeeList){
				if(e.getIdNumber()!=null && !"".equals(e.getIdNumber()) && list.contains(e.getIdNumber())){
					return "导入失败，身份证号"+e.getIdNumber()+"已存在，请确保身份证号唯一";
				}else{
					
					//根据部门名称获取部门id(注意要求 部门名称和id之间一对一的关系)
					Sql sql2 = Sqls.create("select id from sys_unit where name=@name");
					sql2.setParam("name", e.getUnitName());
					sql2 = sql2.setCallback(new SqlCallback() {

						@Override
						public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
							List<String> list = new ArrayList<>();
							while(rs.next()) {
								list.add(rs.getString("id"));
							}
							return list;
						}
					});
					List<String> list2 = dao().execute(sql2).getList(String.class);
					if(!list2.isEmpty()){
						e.setUnitid(list2.get(0));
						e.setUserStatus(3);//导入的都是职工
					}else{
						return "导入失败，单位"+e.getUnitName()+"不存在！";
					}
					
					boolean flag = isExistByUnitAndName(e);
					if(!flag){
						insertList.add(e);
					}else{
						updateList.add(e);
					}
					
				}
			}
		    dao().insert(insertList);
		    dao().updateIgnoreNull(updateList);
		    return "success";
	}
	/**
	 * 判断同一单位下同名员工是否存在
	 * @param e
	 * @return
	 */
	private boolean isExistByUnitAndName(Sys_user e) {
		Cnd cnd = Cnd.NEW();
		cnd.where().and("unitid","=",e.getUnitid()).and("username","=",e.getUsername());
		List<Sys_user> list = dao().query(Sys_user.class, cnd);
		if(list.size()>0){
			String id = list.get(0).getId();
			e.setId(id);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean existDeviceinfo(String id) {
		Cnd cnd = Cnd.NEW();
		cnd.where().and("chargePerson","=",id);
		List<Record> list = dao().query("ins_device_info", cnd);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
}
