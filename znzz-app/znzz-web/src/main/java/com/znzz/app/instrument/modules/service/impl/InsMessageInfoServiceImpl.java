package com.znzz.app.instrument.modules.service.impl;

import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.app.instrument.modules.service.InsMessageInfoService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.sys.modules.services.SysUserService;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.framework.base.service.BaseServiceImpl;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import java.text.SimpleDateFormat;
import java.util.*;

@IocBean(args = {"refer:dao"})
public class InsMessageInfoServiceImpl extends BaseServiceImpl<Ins_MessageInfo> implements InsMessageInfoService {

    @Inject
    private SysUserService userService;

    public InsMessageInfoServiceImpl(Dao dao) {
        super(dao);
    }

	/*
	 * 定时任务，将设备使用率过低的记录写入数据库 
	 */
	@Override
	public void quartzInsert(List<Ins_MessageInfo> list) {
		
		//获取所有接收人id
		String receiveUnitid = (String) Configer.getInstance().get("receiveUnitid");
		String receiveUserid = (String) Configer.getInstance().get("receiveUserid");
		
		List<Sys_user> query = dao().query(Sys_user.class, Cnd.where("unitid", "in", receiveUnitid).and("userStatus", "=", "2"));
		//不允许重复的set集合，存放接收人id数据
		Set<String> idSet = new HashSet<String>();
		for (Sys_user user : query) {
			idSet.add(user.getId());
		}
		if(!StringUtils.isBlank(receiveUserid)){
			String[] ids = receiveUserid.split(",");
			for (String s : ids) {
				idSet.add(s);
			}
		}
		
		//整理数据，插入
		//将insertList集合里的数据发送给每一个接收人，总插入条数=insertList大小*idSet大小
		List<Ins_MessageInfo> messageList = new ArrayList<Ins_MessageInfo>();
		
		String[] date = getPastDate();
		
		for(String id :idSet){
			for(int i=0;i<list.size();i++){
				Ins_MessageInfo info = new Ins_MessageInfo();
				String context = list.get(i).getUname()+" 于过去七天（"+date[0]+"至"+date[1]+") 设备使用率过低予以提醒！";
				
				info.setSendId("10000");
				info.setReceiveId(id);
				info.setMessageContext(context);
				info.setStatus(0);
				info.setSend_time(new Date());
				
				messageList.add(info);
			}
		}
		
		dao().fastInsert(messageList);
	}
	
	
	 //获取过去七天的时间范围
	 public static String[] getPastDate() {  
	       Calendar calendar = Calendar.getInstance();  
	       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);  
	       Date today = calendar.getTime();  
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	       String[] result = new String[]{format.format(today),format.format(new Date())};  
	       return result;  
	   }  
    

    //获取消息列表信息
    @Override
    public NutMap getMessageInfoData(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, Object o) {
        //查询SQL语句
        String sqlString = "SELECT a.id id, c.username sendName, b.username receiveName, a.messageContext messageContext, a.`status` `status`, a.send_time send_time\n" +
                "FROM ins_message_info a LEFT JOIN sys_user b ON a.receiveId = b.id\n" +
                "LEFT JOIN sys_user c ON a.sendId = c.id  $order";
        //开启查询
        Sql sql = Sqls.queryRecord(sqlString);
        //默认开启false
        //cnd.and("a.delFlag", "=", "false");
        cnd.and("a.delFlag","is",null);
        // 添加排序功能
        if (order != null && order.size() > 0) {
            for (DataTableOrder or : order) {
                cnd.orderBy(Sqls.escapeSqlFieldValue(columns.get(or.getColumn()).getData()).toString(), or.getDir());
            }
        }
        //获取当前登录用户
        Sys_user user = (Sys_user) SecurityUtils.getSubject().getPrincipal();
        if(!"管理员".equals(user.getUsername()) && !"超级管理员".equals(user.getUsername())){
            cnd.and("a.receiveId","=",user.getId());
        }
        //设置条件
        sql.setVar("order", cnd);
        //获取数据
        NutMap data = data(length, start, draw, sql, sql);
        //返回数据
        return data;
    }

    //删除消息信息
    @Override
    public void deleteMessageInfo(Integer id) {
        //SQL语句
        Sql sql = Sqls.create("UPDATE ins_message_info set delFlag = 1 WHERE id = @id");
        //设置id
        sql.setParam("id", id);
        //执行SQL
        dao().execute(sql);
    }

    //批量删除消息信息
    @Override
    public void deleteMessageInfo(String[] ids) {
        //SQL语句
        Sql sql = Sqls.create("UPDATE ins_message_info set delFlag = 1 $order");
        //设置ids
        Cnd cnd = Cnd.NEW();
        cnd.and("id","in",ids);
        sql.setVar("order", cnd);
        //执行SQL
        dao().execute(sql);
    }

    //单个已读
    @Override
    public void updateMessageInfoStatus(Integer id) {
        //SQL语句
        Sql sql = Sqls.create("UPDATE ins_message_info set `status` = 1 WHERE id = @id");
        //设置id
        sql.setParam("id", id);
        //执行SQL
        dao().execute(sql);
    }

    //批量已读
    @Override
    public void updateMessageInfoStatus(String[] ids) {
        //SQL语句
        Sql sql = Sqls.create("UPDATE ins_message_info set `status` = 1 $order");
        //设置ids
        Cnd cnd = Cnd.NEW();
        cnd.and("id","in",ids);
        sql.setVar("order", cnd);
        //执行SQL
        dao().execute(sql);
    }

    //获取未读消息数目
    @Override
    public Integer getMessageCount(String id) {
        //创建查询条件
        Cnd cnd = Cnd.NEW();
        //默认显示
        cnd.and("status", "=", 0).and("delFlag", "is", null);
        //根据条件刷选
        Sys_user user = dao().fetch(Sys_user.class, Cnd.where("id", "=", id));
        //超级管理员和管理员查看所有,非管理员只能查看自己的
        if(!"管理员".equals(user.getUsername()) && !"超级管理员".equals(user.getUsername())){
            cnd.and("receiveId","=",id);
        }
        //查询SQL
        Integer count = dao().count(Ins_MessageInfo.class, cnd);

        return count;
    }

    //全部标记为已读
    @Override
    public boolean allReadMessage() {
	    //dao().update(Ins_MessageInfo.class,Cnd.where("status",""))
        return false;
    }

}
