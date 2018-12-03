package com.znzz.app.web.commons.rpc;

import org.nutz.integration.cxf.AbstractCxfModule;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

/**
 * 远程调用启动项
 * @author wangqiang
 *
 */
@IocBean(create = "_init", depose = "depose")
@At("/cxf")
public class RpcModule extends AbstractCxfModule{
	

	    public RpcModule() {
	       // 与类上的@At对应
	       pathROOT = "/cxf/" ;
	       // 设置webservice实现类的package
	       pkg = "com.znzz.app.instrument.modules.service.impl";
	    }

	    //定义入口方法
	    @At("/*")
	    @Ok("void")
	    @Fail("void")
	    public void service() throws Exception {
	        super.service();
	    }
}
