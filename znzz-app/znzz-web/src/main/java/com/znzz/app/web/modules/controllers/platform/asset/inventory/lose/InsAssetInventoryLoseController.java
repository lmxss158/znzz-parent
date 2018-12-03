package com.znzz.app.web.modules.controllers.platform.asset.inventory.lose;

import com.znzz.app.asset.moudules.services.InsAssetInventoryLoseService;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@IocBean
@At("/asset/inventory/lose")
public class InsAssetInventoryLoseController {
    @Inject
    private InsAssetInventoryLoseService insAssetInventoryLoseService;
    @Inject
    private  ExportService exportService;

    @At("")
    @Ok("beetl:/platform/asset/inventory/lose/index.html")
    @RequiresPermissions("assets.inventory.lose")
    public void index(){

    }

    //获取列表数据
    @At
    @Ok("json:full")
    @RequiresPermissions("assets.inventory.lose")
    public Object data(@Param("..") AssetsForm assets,@Param("createTimeLose") String createTimeLose, @Param("length") int length, @Param("start") int start, @Param("draw") int draw,
                       @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {

        return insAssetInventoryLoseService.getAssetsLoseData(assets, createTimeLose, length, start, draw, order, columns, null, null, null);
    }

    //导出
    @At
    @Ok("json")
    @RequiresPermissions("assets.inventory.lose")
    public String exportInventoryLoseInfo( @Param("..") AssetsForm assets, @Param("createTimeLose")String createTimeLose, HttpServletRequest request, HttpServletResponse response,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) throws ParseException {

        NutMap re = insAssetInventoryLoseService.getAssetsLoseData(assets,createTimeLose, 100000, 0, 10000, order, columns, null, null, null);
        List<AssetsForm> list = (List<AssetsForm>) re.get("data");
        List<Class<?>> voList = new ArrayList<>();
        Class<?> forName = AssetsForm.class;
        voList.add(forName);
        String url = exportService.exportFile(request, response, voList,"ins_asset_inventory_lose",list);
        return  url;
    }

    @At
    @Ok("void")
    @RequiresPermissions("assets.inventory.lose")
    public void exportInventoryLoseInfoByUrl(@Param("url")String url,HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取文件名
        String filename = Configer.getInstance().getProperty("ins_asset_inventory_lose");
        //根据URL找到路径下载,返给前台
        DownloadUtil download = new DownloadUtil();
        File file = new File(url);
        download.prototypeDownload(file, filename, response, true);
    }

}
