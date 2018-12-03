package com.znzz.app.web.commons.util;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;


public class DataSourceUtils {
	public static Dao getDao(){
		SimpleDataSource dataSource = new SimpleDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://192.168.177.50:3306/znzz_platform");
		dataSource.setUsername("root");
		dataSource.setPassword("");

		// 创建一个NutDao实例,在真实项目中, NutDao通常由ioc托管, 使用注入的方式获得.
		Dao dao = new NutDao(dataSource);
		return dao ;
	}
}
