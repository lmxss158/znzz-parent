package com.znzz.app.asset.moudules.services;

import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Record;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.asset.moudules.models.Ins_Asset_Repair;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
/**
 * 设备管理接口
 * @author wangqiang
 *
 */
public interface InsAssetRepairService  extends BaseService<Ins_Asset_Repair>{
   
	/**
	 * 维修申请
	 * @param repair
	 */
	void insertRepair(Ins_Asset_Repair repair) ;
	/**
	 * 根据assetCode获取list
	 * @param assetCode
	 * @return
	 */
	List<Ins_Asset_Repair> getListByAssetCode(String assetCode) ;
	
	/**
	 * 维修列表
	 * @param assetCode
	 * @param applyDepart
	 * @param applyPerson
	 * @param time
	 * @param length
	 * @param start
	 * @param draw
	 * @return
	 */
	NutMap getRepairList(String assetCode,String applyDepart,String applyPerson,Integer type,String opType,String time,int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns);
	
	/**
	 * 根据id获取维修详情
	 * @param id
	 * @return
	 */
	Map getDetail(Integer id) ;
	
	
	/**
	 * 获取详情bean
	 * @param id
	 * @return
	 */
	Ins_Asset_Repair getDetailBean(Integer id) ;
	
	/**
	 * 更新实体bean
	 */
	void updateDetailBean(Ins_Asset_Repair repair) ;
	
	
	/**
	 * 流程查看
	 */
	
	List<Record> viewList(Integer id) ;

	/**
	 * 获取各项状态数量
	 * @return
	 */
	Map getCount(String depart) ;
	
	/**
	 * 资产信息详情下查询维修记录
	 */
	
	NutMap getRepairRecordForAssetInfo(String assetCode,int length, int start, int draw) ;
	/**
	 * 获取该条记录中维修中的子记录
	 * @param id
	 * @return
	 */
	Ins_Asset_Repair getRepairingBean(Integer id);
}
