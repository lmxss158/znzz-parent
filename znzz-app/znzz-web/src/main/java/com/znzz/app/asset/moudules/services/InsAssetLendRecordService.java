package com.znzz.app.asset.moudules.services;

import java.util.List;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.framework.base.service.BaseService;


/**
 * 
 * @ClassName: InsAssetLendRecordService   
 * @Description: TODO(仪器室设备借调记录)
 * @author fengguoxin
 * @date 2017年8月23日 下午3:11:46   
 */
public interface InsAssetLendRecordService extends BaseService<Ins_Asset_lend_record> {

	Ins_Asset_Rule fetchIns_Asset_Rule(String deviceVersion);

	void insertAssetRecodeToUnit(Ins_Asset_Unit unit);

	void deleteAssetRecodeToUnit(String code);

	List<Ins_Asset_lend_record> getCurMonthReportList();

	/**
	 * 导入借调记录
	 * @param lendRecordList
	 */
	void saveLendRecord(List<Ins_Asset_lend_record> lendRecordList);
	
	Ins_Asset_lend_record getNewReturnInfo(String assetCode);

	//借出记录信息(回显)
    NutMap getLendRecord(String assetCode, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns);

    //归还记录信息(回显
	NutMap getReturnRecord(String assetCodeArray, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns);

    void insertList(List<Ins_Asset_lend_record> lendRecordInfo);

	void updateList(List<Ins_Asset_lend_record> lendRecordList);
}
