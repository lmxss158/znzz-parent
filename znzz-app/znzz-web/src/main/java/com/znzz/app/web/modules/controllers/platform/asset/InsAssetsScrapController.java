package com.znzz.app.web.modules.controllers.platform.asset;

import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 报废资产信息
 */
@IocBean
@At("/asset/scrap")
public class InsAssetsScrapController {

    @Inject
    private InsAssetsService assetsService;
    // 首页
    @At("")
    @Ok("beetl:/platform/asset/info/index.html")
    @RequiresPermissions("asset.manage.scrap")
    public void index(HttpServletRequest request) {
        // 给页面定义该变量
        request.setAttribute("errors", "0");
    }

    // 数据项
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.manage.scrap")
    public Object data(@Param("..") AssetsForm assets, @Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        assets.setScrapState(1);
        cnd.and("a.scrapState","=","1");
        return assetsService.getAssetsDataWith(assets, length, start, draw, order, columns, cnd, null, null);
    }

}
