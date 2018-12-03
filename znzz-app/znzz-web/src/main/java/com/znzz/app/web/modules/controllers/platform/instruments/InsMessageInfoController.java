package com.znzz.app.web.modules.controllers.platform.instruments;

import com.znzz.app.instrument.modules.service.InsMessageInfoService;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.MessageInfo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import com.znzz.framework.util.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IocBean
@At("/sys/message/info")
public class InsMessageInfoController {

    //日志信息
    private static final Log LOG = Logs.get();

    //消息管理的service类
    @Inject
    InsMessageInfoService messageInfoService;

    // 首页
    @At("")
    @Ok("beetl:/platform/monitor/message/index.html")
    @RequiresPermissions("sys.manager.message.info")
    public void index() {

    }

    /**
     * 展示消息信息
     */
    @At
    @Ok("json:full")
    @RequiresPermissions("sys.manager.message.info")
    public Object data(@Param("..")MessageInfo messageInfo, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns){
        //创建条件查询语句
        Cnd cnd = Cnd.NEW();
        //内容不为空
        if (Strings.isNotBlank(messageInfo.getMessageContext().trim())){
            cnd.and("a.messageContext","like","%" + messageInfo.getMessageContext().trim() + "%");
        }
        //状态不为空
        if (messageInfo.getStatus() != null){
            cnd.and("a.`status`","=",messageInfo.getStatus());
        }
        //时间不为空,进行判断
        if (Strings.isNotBlank(messageInfo.getSend_time().trim())){
            ArrayList<Date> date = DateUtil.getBetweenAndTimeWithHHmmss(messageInfo.getSend_time().trim());
            if (date != null && date.size() > 0){
                cnd.and("a.send_time","between",new Object[]{date.get(0),date.get(1)});
            }
        }
        //获取消息管理信息
        return  messageInfoService.getMessageInfoData(length, start, draw, order, columns, cnd, null);

    }

    /**
     * 删除和批量删除
     */
    @At({"/delete","/delete/?"})
    @Ok("json:full")
    @RequiresPermissions("sys.manager.message.info")
    public Object delete(@Param("id") Integer id, @Param("ids") String[] ids, HttpServletRequest req){
        //单个删除
        if(id != null){
            messageInfoService.deleteMessageInfo(id);
            return Result.success("删除成功");
        }
        //批量删除
        if ( ids != null && ids.length > 0 ){
            messageInfoService.deleteMessageInfo(ids);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 阅读和批量已读
     */
    @At({"/read","/read/?"})
    @Ok("json:full")
    @RequiresPermissions("sys.manager.message.info")
    public Object read(@Param("id") Integer id, @Param("ids") String[] ids, HttpServletRequest req){
        //单个阅读
        if(id != null){
            messageInfoService.updateMessageInfoStatus(id);
            return Result.success("操作成功");
        }
        //批量已读
        if ( ids != null && ids.length > 0 ){
            messageInfoService.updateMessageInfoStatus(ids);
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }


    @At("/getMessageCount/?")
    @Ok("json:full")
    @RequiresPermissions("sys.manager.message.info")
    public Object getMessageCount(String id){
        return  messageInfoService.getMessageCount(id);
    }

    @At("/allReaded")
    @Ok("json:full")
    @RequiresPermissions("sys.manager.message.info")
    public Object allReaded(Integer id){
       // boolean flag = messageInfoService.allReadMessage();
        return  null;
    }

}
