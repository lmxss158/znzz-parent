package com.znzz.app.asset.moudules.services;


import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.util.NutMap;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.instrument.modules.models.Ins_CycleExamine;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * @author chenzhongliang
 *
 */
public interface InsCycleExamineService extends BaseService<Ins_Assets>{

	/**
	 * @param insCycleExamine
	 */
	void insertExamineRecord(Ins_CycleExamine insCycleExamine);
	
	/**
	 * 批量插入 (excel导入)
	 * @param examineList
	 */
	void insertExamineRecordList(List<Ins_CycleExamine> examineList);

	/**
	 * 查找检定记录列表
	 * @param assetCode
	 * @param draw 
	 * @param start 
	 * @param length 
	 * @return
	 */
	NutMap findList(String assetCode, int length, int start, int draw);

	/**
	 * 通过id获取检定记录
	 * @param sql
	 * @return
	 */
	List<Object> findExaminebyId(Sql sql);
	
	List<Object> findExaminebyId2(Sql sql2);
	/**
	 * 根据id修改资产信息表examineDate，dueDate两个字段
	 * @param id
	 * @param examineDate
	 * @param dueDate
	 */
	void updateAssetInfo(Integer id, Date examineDate, Date dueDate);

	/**
	 * 插入过期操作数据
	 * @param assetCode
	 * @param examineDate
	 */
	void updateExamine(String assetCode, String overdueReaspn);

	/**
	 * 获取表中id，dueDate字段集合
	 * @return
	 */
	List<Ins_Assets> getDuedateList();

	/**
	 * 根据id修改isOverDate字段
	 * @param id
	 * @param i 
	 */
	void updateIsover(Integer id, int i);

	/**
	 * 统计资产表中过期数量
	 * @return
	 */
	int countOver();

	/**
	 * 根据设置好的时间栈，判断资产表中即将过期的资产数量
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int countOverByTime(Date startTime, Date endTime);

	void insertList(List<Ins_Assets> assetList);

	

}
