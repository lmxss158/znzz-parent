package com.znzz.app.instrument.modules.service;

import com.znzz.app.instrument.modules.models.Ins_MessageInfo;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * 消息展示的接口
 * @author changzheng
 * @version 1.0
 * @since 2018-01-04 15:15:03
 */
public interface InsMessageInfoService extends BaseService<Ins_MessageInfo> {
	
	//定时任务向表中插入数据
	void quartzInsert(List<Ins_MessageInfo> list);

	/**
	 * 获取消息列表数据
	 * @param length
	 * @param start
	 * @param draw
	 * @param order
	 * @param columns
	 * @param cnd
	 * @param o
	 * @return
	 */
    NutMap getMessageInfoData(int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns, Cnd cnd, Object o);

	/**
	 * 删除消息信息
	 * @param id
	 */
	void deleteMessageInfo(Integer id);

	/**
	 * 批量删除消息信息
	 * @param ids
	 */
	void deleteMessageInfo(String[] ids);

	/**
	 * 单个改变消息状态
	 * @param id
	 */
	void updateMessageInfoStatus(Integer id);

	/**
	 * 批量已读
	 * @param ids
	 */
	void updateMessageInfoStatus(String[] ids);

	//获取未读的消息数目
	Integer getMessageCount(String id);

	/**
	 * 全部标记为已读
	 * @return
	 */
	boolean allReadMessage();
}
