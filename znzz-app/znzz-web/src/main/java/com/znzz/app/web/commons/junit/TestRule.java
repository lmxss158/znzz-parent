package com.znzz.app.web.commons.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.trans.Trans;

import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.app.asset.moudules.services.impl.InsAssertRuleServiceImpl;

public class TestRule extends TestBase {

	public InsAssertRuleService rule;

	@Before
	public void _init() {
		if (rule == null)
			rule = ioc.get(InsAssertRuleServiceImpl.class);
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
         System.out.println(rule.getDeviceVersionList("MM_009").size());
      }
	
}