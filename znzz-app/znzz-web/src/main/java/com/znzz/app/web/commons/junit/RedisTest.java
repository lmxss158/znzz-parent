package com.znzz.app.web.commons.junit;
import static org.nutz.integration.jedis.RedisInterceptor.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.aop.Aop;
import org.nutz.trans.Trans;

import com.znzz.app.web.commons.base.Globals;


public class RedisTest  extends TestBase{
		

	public RedisService redisService;

	@Before
	public void _init() {
		if (redisService == null)
			redisService = ioc.get(RedisService.class);
		try {
			Trans.begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {
		try {
			Trans.rollback();
			Trans.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  @Test
      public void test_chinese_var_name() {
		 // redisService.get(Globals.ORIGNDEVICECODE) ;
		//  redisService.del(Globals.ORIGNDEVICECODE);
		  System.out.println(  redisService.get(Globals.ORIGNDEVICECODE) );
      }
	
}
