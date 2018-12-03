package com.znzz.app.asset.moudules.services;

import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Record;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.asset.moudules.models.Ins_Asset_Unit_Record;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsUnitVo;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 资产管理(科室)设备借调记录表
 * @author wangqiang
 *
 */
public interface InsAssetUnitRecordService {
	
	/**
	 * 插入一条记录
	 * @param record
	 */
	void insertRecord(Ins_Asset_Unit_Record record) ;
	
	/**
	 * 更新一条记录
	 * @param record
	 */
	void updateRecord(Ins_Asset_Unit_Record record) ;
	
	
	/**
	 * 查看完结状态
	 */
	NutMap getRecordList(String assetCode,String operateType,int length ,int start ,int draw, List<DataTableOrder> order,  List<DataTableColumn> columns) ;
	
	
	/**
	 * 查看本单位待审批的记录
	 */
	NutMap getAllApproveList(AssetsUnitVo vo,List<DataTableOrder> order, List<DataTableColumn> columns) ;
	
	 /**
     * 获取单位资产记录中的bean
     */
    Map getAssetUnitRecordInfoBean(Integer id);
	
	/**
	 * 根据id获取bean
	 */
    Ins_Asset_Unit_Record fetchBean(Integer id) ;
    
    /**
     * 获取申请 审批全过程记录
     * @param id
     * @return
     */
    List<Record> getAssetUnitRecordInfoList(Integer id) ;
    
    /**
     * 查询该单位下的要审批个数
     * @param unitid
     * @return
     */
    int getApproveCountByUnitId(String unitid) ;
    
    
    /**
     * 查询该单位是否已经发了申请
     * @param unitid
     * @return
     */
    int getApprovingCountByUnitId(String assetCode,String unitid,Integer OperateType) ;
    
    
}
