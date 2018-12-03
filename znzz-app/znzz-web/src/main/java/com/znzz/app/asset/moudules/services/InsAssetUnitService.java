package com.znzz.app.asset.moudules.services;

import java.util.List;
import java.util.Map;

import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsUnitVo;
import com.znzz.framework.base.service.BaseService;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 单位资产接口
 * @author wangqiang
 *
 */
public interface InsAssetUnitService extends BaseService<Ins_Asset_Unit>{
	  /**
	   * 查询单位资产列表
	   * @param vo
	   * @return
	   */
      NutMap getAssetUnitList(AssetsUnitVo vo,List<DataTableOrder> order, List<DataTableColumn> columns) ;
      
      /**
       * 根据id查询 单位资产详情
       * @param id
       * @return
       */
      Map getAssetUnitInfo(Integer id) ;
      
      /**
       * 更新单位资产信息
       * @param unit
       */
      void updateAssetUnitInfo(Ins_Asset_Unit unit) ;
      
      /**
       * 获取单位资产bean
       */
      Ins_Asset_Unit getAssetUnitInfoBean(Integer id);
      
      /**
       * 根据assetCode获取bean
       */
      Ins_Asset_Unit getAssetUnitInfoBean(String assetCode) ;
      
      /**
       * 更新设备的使用率
       * @param map
       */
      void UpdateAssetUnitRate(Map<String ,Integer> map);

      /**
       * 导入资产时，插入单位资产信息
       * @param assetUnitList
       */
	void saveOrUpdate(List<Ins_Asset_Unit> assetUnitList);

    void insertList(List<Ins_Asset_Unit> unitList);

    void updateList(List<Ins_Asset_Unit> unitList);
}
