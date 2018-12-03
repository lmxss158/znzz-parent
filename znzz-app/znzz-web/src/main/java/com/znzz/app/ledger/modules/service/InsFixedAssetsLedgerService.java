package com.znzz.app.ledger.modules.service;

import java.util.List;

import org.nutz.lang.util.NutMap;

import com.znzz.app.ledger.modules.models.InsFixedAssetsLedger;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

public interface InsFixedAssetsLedgerService extends BaseService<InsFixedAssetsLedger>{

	/**
	 * 获取固定资产台账信息
	 * @param fixedAssetsLedger
	 * @param length
	 * @param start
	 * @param draw
	 * @param order
	 * @param columns
	 * @return
	 */
	NutMap getFixedAssetsData(InsFixedAssetsLedger fixedAssetsLedger, int length, int start, int draw,
			List<DataTableOrder> order, List<DataTableColumn> columns);

	/**
	 * 批量插入台账信息
	 * @param fixedAssetsList
	 */
	void saveOrUpate(List<InsFixedAssetsLedger> fixedAssetsList);

}
