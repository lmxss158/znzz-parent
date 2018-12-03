package com.znzz.app.asset.moudules.services;



import java.util.List;
import org.nutz.lang.util.NutMap;
import com.znzz.app.asset.moudules.models.Ins_Month_report;
import com.znzz.framework.base.service.BaseService;

/**
 * 
 * @ClassName: InsAssetMouthReportService   
 * @Description: TODO(月度报告接口)
 * @author fengguoxin
 * @date 2017年9月12日 上午10:50:10   
 */
public interface InsAssetMouthReportService extends BaseService<Ins_Month_report> {

	/**
	 * 统计维修、报废、借还、转账、封存启封五张表assetCode并去重后返回资产信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	//List<AssetsForm> checkAssetCode(String month);
	
	public NutMap getAllRecodeList(String nianyue);
	
	public List<String> assetCodeList(String startDate,String endDate);
	
}
