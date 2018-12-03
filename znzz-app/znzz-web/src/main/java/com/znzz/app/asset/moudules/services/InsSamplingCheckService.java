package com.znzz.app.asset.moudules.services;

import java.util.List;

import org.nutz.lang.util.NutMap;

import com.znzz.app.asset.moudules.models.Ins_Asset_CycleCheck;
import com.znzz.app.asset.moudules.models.Ins_Asset_SamplingCheck;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * @classname InsAssetCycleCheckService.java
 * @author chenzhongliang
 * @date 2017年11月30日
 */
public interface InsSamplingCheckService extends BaseService<Ins_Asset_SamplingCheck>{

	NutMap getList(Ins_Asset_SamplingCheck assetCheck, String module, int length, int start, int draw, List<DataTableOrder> order, List<DataTableColumn> columns);

	void insertList(List<Ins_Asset_SamplingCheck> assetList);

}
