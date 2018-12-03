package com.znzz.app.asset.moudules.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Cnd;
import org.nutz.lang.util.NutMap;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsScrapRecordVo;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsSealedRecordVo;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;
/**
 * 
 * @ClassName: InsAssetsService
 * @Description: 资产管理接口
 * @author pengmantai
 * @date 2017年8月17日 上午10:56:13
 */
public interface InsAssetsService_Czl extends BaseService<Ins_Assets>{

	/**
	 * 查询资产信息
	 * @param length
	 * @param start
	 * @param draw
	 * @param order
	 * @param columns
	 * @param
	 * @param object2
	 * @param originalValue2
	 * @return
	 */
	NutMap getAssetsDataWith(Ins_Assets vo, int length, int start, int draw, List<DataTableOrder> order,
                             List<DataTableColumn> columns, Cnd cnd, Object object2, String originalValue2);
	
	/**
	 * 判断数据库中是否有资产编号
	 * @param assetCode
	 * @return
	 */
	String getAssetCodeList(String assetCode,String id);


	/**
	 * 
	 * @param cnd
	 * @return
	 */
	Ins_Assets getAssetsInfo(Cnd cnd);


	/**
	 * 获取资产和型号信息
	 * @param id
	 * @return
	 */
	AssetsForm getAssetAndRuleInfo(Integer id);

	
	
	/**
	 * 根据统一编码获取资产相关信息
	 */
	Map getAssetAndRuleInfo(String assetCode);

	/**
	 * 根据id封存资产
	 * @param ids
	 */
	void sealed(Integer[] ids);

	/**
	 * 根据id解封资产
	 * @param
	 */
	void unsealed(Integer[] ids);


	List<AssetsForm> getExportList();


	/**
	 * 获取封存启封记录列表
	 * @param vo
	 * @return
	 */
	NutMap getSealRecordList(AssetsSealedRecordVo vo);

	/**
	 * 确认报废
	 * @param ids
	 */
	void confirmScrap(Integer[] ids);

	/**
	 * 报废
	 * @param ids
	 */
	void scrap(Integer[] ids);

	/**
	 * 获取报废记录
	 * @param vo
	 * @return
	 */
	NutMap getScrapRecordList(AssetsScrapRecordVo vo);

	/**
	 * 更新资产信息表, 链接云网
	 * @param deviceCode
	 * @return
	 */
	boolean updateConnectCloud(Integer state,String deviceCode);

	/**
	 * 多个采集同时解绑, 更新云网信息
	 * @param ids
	 */
	void deleteConnectCloud(String[] ids);

	/**
	 * 导出资产信息至excel表格当中
	 * @param ids
	 * @param request
	 * @param response
	 * @param exportmenu
	 * @param vo 
	 * @return
	 */
	List<AssetsForm> exportAssetsInfo(String[] ids, HttpServletRequest request, HttpServletResponse response,
			Map<String, String> exportmenu, Ins_Assets vo);

	
	AssetsForm getAssetAndRuleInfoDetail(Integer id);

	/**
	 * 根据统一编号修改使用单位责任人
	 * @param assetCode
	 * @param borrowDepart
	 * @param chargePerson
	 */
	void updateChargePersonAndBorrowDepart(String assetCode, String borrowDepart, String chargePerson);

	/**
	 * 资产信息批量插入数据库
	 * @param assetsList
	 */
	void insertList(List<Ins_Assets> assetsList);

	/**
	 * 校验添加是统一编号是否重复
	 * @param assetCode
	 * @return
	 */
	boolean getAssetCodeListAdd(String assetCode);

	/**
	 * 查看是否有绑定
	 * @param assetCode
	 * @return
	 */
	boolean checkBind(String assetCode);

	/**
	 * 导入或者更新资产信息
	 * @param assetsList
	 */
	void saveOrUpdate(List<Ins_Assets> assetsList);

	String getDeviceCode(Integer id);

	/**
	 * 查询资产表中的统一编号
	 * @return
	 */
	String getAssetCodeListByUnitId(String unitId) ;

	/**
	 * 根据统一编号获取资产详情
	 * @param assetCode
	 * @return
	 */
	Map<String,AssetsForm> getAssetAndRuleInfoByAssetCodes(String assetCode);

	/**
	 * 根据统一编号(逗号分隔),获取资产实体bean列表
	 * @param deviceCodes
	 * @return
	 */
	List<Ins_Assets> getInsAssetBeanList(String deviceCodes);

	/**
	 * 根据统一编号更新位置
	 * @param deviceCode
	 * @param locationInfo
	 */
	void updateInsAssetBeanLocationInfo(String deviceCode,String locationInfo) ;
}
