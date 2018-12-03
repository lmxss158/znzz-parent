package com.znzz.app.sys.modules.services.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.nutz.aop.interceptor.ioc.TransAop;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

import com.znzz.app.sys.modules.models.Sys_unit;
import com.znzz.app.sys.modules.services.SysUnitService;
import com.znzz.app.web.commons.util.DataSourceUtils;
import com.znzz.framework.base.service.BaseServiceImpl;

/**
 * 单位service实现类
 */
@IocBean(args = {"refer:dao"})
public class SysUnitServiceImpl extends BaseServiceImpl<Sys_unit> implements SysUnitService {
    public SysUnitServiceImpl(Dao dao) {
        super(dao);
    }

    /**
     * 新增单位
     *
     * @param unit
     * @param pid
     */
    @Aop(TransAop.READ_COMMITTED)
    public void save(Sys_unit unit, String parentUnitcode) {
        String path = "";
       
        if (!Strings.isEmpty(parentUnitcode)) {
            Sys_unit pp = this.fetch(parentUnitcode);
            path = pp.getPath();
            
        }
        unit.setId(unit.getUnitcode());
        unit.setPath(getSubPath("sys_unit", "path", path));
        //父级ID保存为父级部门编码
        unit.setParentId(parentUnitcode);
        //当前记录ID保存为当前部门编码
        unit.setId(unit.getUnitcode());
        dao().insert(unit);
        //跟新父字段
        if (!Strings.isEmpty(parentUnitcode)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", parentUnitcode));
        }
        
        
        
        
        
        
        
      /*  String path = "";
        if (!Strings.isEmpty(pid)) {
            Sys_unit pp = this.fetch(pid);
            path = pp.getPath();
        }
        unit.setPath(getSubPath("sys_unit", "path", path));
        unit.setParentId(pid);
        dao().insert(unit);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }*/
        
        
        
    }

    /**
     * 级联删除单位
     *
     * @param unit
     */
    @Aop(TransAop.READ_COMMITTED)
    public void deleteAndChild(Sys_unit unit) {
        dao().execute(Sqls.create("delete from sys_unit where path like @path").setParam("path", unit.getPath() + "%"));
        dao().execute(Sqls.create("delete from sys_user_unit where unitId=@id or unitId in(SELECT id FROM sys_unit WHERE path like @path)").setParam("id", unit.getId()).setParam("path", unit.getPath() + "%"));
        dao().execute(Sqls.create("delete from sys_role where unitid=@id or unitid in(SELECT id FROM sys_unit WHERE path like @path)").setParam("id", unit.getId()).setParam("path", unit.getPath() + "%"));
        if (!Strings.isEmpty(unit.getParentId())) {
            int count = count(Cnd.where("parentId", "=", unit.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update sys_unit set hasChildren=0 where id=@pid").setParam("pid", unit.getParentId()));
            }
        }
    }

    
    /**
     * 获取单位名称及id
     */
	@Override
	public List<Record> getUnitIdAndUnitNameList(String unitIds) {
	
		Sql sql = Sqls.create("SELECT id,`name` from sys_unit where id in ($unitIds)") ;
		sql.setVar("unitIds", unitIds) ;
		// 设置回调函数
	    sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    return sql.getList(Record.class);
	}
	
	/**
	 * 获取每个单位id&父级id
	 */
	@Override
	public Map<String,String> getUnitIdAndParentId() {
		HashMap<Object, Object> map = new HashMap<>() ;
		Sql sql = Sqls.create("SELECT id,parentId from sys_unit ") ;
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				HashMap<String, String> idMap = new HashMap<String,String>() ;
				while (rs.next()) {
					idMap.put(rs.getString("id"), rs.getString("parentId")) ;
				}
				list.add(idMap) ;
				return list;
			}
		});
		
		Map<String ,String> idMap = dao().execute(sql).getList(Map.class).get(0); 
		
		return idMap;
	}
	
	/**
	 * 查询单位id所在顶级单位id
	 */
	@Override
	public String getTopUnitId(Map<String, String> map, String untiId) {
		String topUnitId = null ;
		String parentId = map.get(untiId) ;
		if(StringUtils.isNotBlank(parentId)&&(!parentId.equals(untiId))){
			topUnitId = getTopUnitId(map,parentId) ;
		}else{
			topUnitId = untiId ;
		}
		return topUnitId;
	}
	
	/**
	 * 将单位单位转换为顶级与子级关系
	 */
	@Override
	public Map<String, List<String>> getTopUnitGroup(Map<String, String> map) {
		
		Map<String,List<String>> topMap = new HashMap<String,List<String>>() ;
		 Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
	     while (it.hasNext()) {
	        Map.Entry<String, String> entry = it.next();
	        String parentId = entry.getValue() ;
	        if(StringUtils.isBlank(parentId)){
	        	if(!topMap.containsKey(entry.getKey())){
	        		topMap.put(entry.getKey(), new ArrayList<String>()) ;
	        	}
	        }else{
	        	String topUnitId = getTopUnitId(map,parentId) ;
	        	List<String> list ;
	        	if(topMap.containsKey(topUnitId)){//包含顶级
	        		list = topMap.get(topUnitId) ;
	        	}else{
	        		list = new ArrayList<String>() ;
	        	}
	        	list.add(entry.getKey());
	        	topMap.put(topUnitId, list) ;
	        }
	        
	     }
		
		
		return topMap;
	}

	/**
	 * 转换为父子级列表关系
	 */
	@Override
	public Map<String, List<String>> getUnitTreeGroup(Map<String, String> map) {
		
		Map<String,List<String>> topMap = new HashMap<String,List<String>>() ;
		 Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next() ;
		    String unitId = entry.getKey() ;
		    String parentId = entry.getValue() ;
		    
		    if(!topMap.containsKey(unitId)){
		    	topMap.put(unitId, new ArrayList<String>()) ;
		    }
		    
		    if(StringUtils.isNotBlank(parentId)){
		    	List<String> list = null ;
		    	if(!topMap.containsKey(parentId)){
		    		list = new ArrayList<String>() ;
		    	}else{//包含
		    		list = topMap.get(parentId) ;
		    	}
		    	list.add(unitId) ;
	    		topMap.put(parentId, list) ;
		    }
		}
		
		
		return topMap;
	}
	
	/**
	 * 获取所有子级列表包含当前级
	 */
	@Override
	public void getChildUnitList(Map<String, List<String>> map, String unitId,List<String> list) {
		List<String> childList = map.get(unitId) ;
		if(childList!=null){
			for(int i=0;i<childList.size();i++){
				String id = childList.get(i) ;
				list.add(id) ;
				getChildUnitList(map, id, list) ;
			}
		}
		
	}
	
	
	/**
	 * 导入批量插入部门
	 */
	@Override
	public void insertList(List<Sys_unit> unitList) {
		
	/*	List<Sys_unit> units = new ArrayList<>();
		Sys_unit unit = null;*/
		for (Sys_unit bean : unitList) {
		/*	unit = new Sys_unit();
			unit.setName(bean.getName());
			unit.setId(bean.getUnitcode());
			unit.setAddress(bean.getAddress());
			unit.setParentId(bean.getParentId());
			
			unit.setUnitcode(bean.getUnitcode());
			
			if(Strings.isEmpty(bean.getParentId())){
				unit.setParentId("");
			}else{
				 unit.setParentId(bean.getParentId());
				 //更新父菜单
				 this.update(Chain.make("hasChildren", true), Cnd.where("unitcode", "=", bean.getParentId()));
			}
			if(Strings.isEmpty(bean.getEmail())){
				unit.setEmail("");
			}else{
				unit.setEmail(bean.getEmail());
			}
			if(Strings.isEmail(bean.getTelephone())){
				unit.setTelephone("");
			}else{
				unit.setTelephone(bean.getTelephone());
			}
		  */
		
            String path = "";
	        if (!Strings.isEmpty(bean.getParentId())) {
	            Sys_unit pp = this.fetch(bean.getParentId());
	            path = pp.getPath();
	            
	        }
	      
	        bean.setPath(getSubPath("sys_unit", "path", path));
	        //父级ID保存为部门编码
	       
	      
	        //跟新父字段
	        if (!Strings.isEmpty(bean.getParentId())) {
	            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", bean.getParentId()));
	        }
	        
		
	        dao().insert(bean);
			
			
		}
		
		// 进行批量插入
		//dao().insert(unitList);
		
		
	}
	/**
	 * 获取当前级下所有子级
	 */
	@Override
	public String getChildList(String parentId) {
		
		Map<String, String> unitAndParentId = getUnitIdAndParentId() ;
        
        Map<String, List<String>> unitTreeGroup = getUnitTreeGroup(unitAndParentId) ;//获取父子分级map
        ArrayList<String> list = new ArrayList<String>() ;
        list.add(parentId) ;
        getChildUnitList(unitTreeGroup, parentId, list);
        //顶级下所有单位ids
        String allUnitds = StringUtils.strip(Json.toJson(list),"[]") ;	
		
		return allUnitds;
	}
	public static void main(String[] args) {
		Dao d = DataSourceUtils.getDao() ;
		SysUnitServiceImpl impl = new SysUnitServiceImpl(d) ;
		String childList = impl.getChildList("921739970b3644b78506314c6f4a16e022") ;
		System.out.println(childList);
		
	}
	/*@Override
	public void updateUnit(Sys_unit unit) {
		Sql sql = Sqls.create("update Sys_unit set id =@id,parentId=@parentId,name=@name,unitcode=@unitcode,"
				+ " note=@note,address=@address,telephone=@telephone,email=@email,"
				+ " hasChildren=@hasChildren,opBy=@opBy,opAt=@opAt where id=@oid");
		sql.setParam("id", unit.getUnitcode());
		sql.setParam("parentId", unit.getParentId());
		sql.setParam("name", unit.getName());
		sql.setParam("unitcode", unit.getUnitcode());
		sql.setParam("note", unit.getNote());
		sql.setParam("address", unit.getAddress());
		sql.setParam("telephone", unit.getTelephone());
		sql.setParam("email", unit.getEmail());
		sql.setParam("hasChildren",unit.isHasChildren());
		sql.setParam("opBy", unit.getOpBy());
		sql.setParam("opAt", unit.getOpAt());
		sql.setParam("oid", unit.getId());
	   
		dao().execute(sql);
		
	}*/

	@Override
	public boolean hashEmployee(String id) {
		String childList = getChildList(id);
		//childList.replace("\"","");
		//String[] split = childList.split(",");
		Sql sql = Sqls.create("select id from sys_user where unitid in ($var)");
		sql.setVar("var", childList);
		// 设置回调函数
	    sql.setCallback(Sqls.callback.entities());
	    Entity<Record> entity = dao().getEntity(Record.class);
	    sql.setEntity(entity);
	    dao().execute(sql);
	    List<Record> list = sql.getList(Record.class);
	    if(list!=null && list.size()>0){
	    	return true;
	    }
	    return false;
	}

	@Override
	public List<String> getAllUnitid() {
		Sql sql = Sqls.create("select id from sys_unit");
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("id"));
				}
				return list;
			}
			});
				
		 List<String> list = dao().execute(sql).getList(String.class);
		 
		 return list;
		
	}

	/* 
	 * 更加id拿到单位名称
	 */
	@Override
	public String getUnitName(String key) {
		Sql sql = Sqls.create("select name from sys_unit where id=@id");
		sql.setParam("id", key);
		// 设置回调函数
		sql = sql.setCallback(new SqlCallback() {

		@Override
		public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("name"));
				}
				return list;
			}
			});
				
		 String name = dao().execute(sql).getList(String.class).get(0);
		 
		 return name;
	}

	@Override
	public Map<String, String> getUnitAndName(String unitIds) {

		Sql sql = Sqls.create("SELECT id,`name` from sys_unit where id in ($unitIds)") ;
		sql.setVar("unitIds", unitIds) ;
		sql = sql.setCallback(new SqlCallback() {

			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();
					Map<String,String> map = new HashMap<String,String>() ;
					while (rs.next()) {
						map.put(rs.getString("id"), rs.getString("name")) ;
					}
					list.add(map);
					return list;
				}
				});
					
			Map<String,String> map = dao().execute(sql).getList(Map.class).get(0);
			 
			 return map;
	}
	
	
}
