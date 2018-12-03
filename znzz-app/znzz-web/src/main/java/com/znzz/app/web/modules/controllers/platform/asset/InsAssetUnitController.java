package com.znzz.app.web.modules.controllers.platform.asset;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.znzz.app.sys.modules.models.Sys_role;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.znzz.app.asset.moudules.models.Ins_Asset_Unit;
import com.znzz.app.asset.moudules.models.Ins_Asset_Unit_Record;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssetUnitRecordService;
import com.znzz.app.asset.moudules.services.InsAssetUnitService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.modules.controllers.platform.asset.common.AssetUnitEnum;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsUnitVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 单位资产controller
 * 
 * @author wangqiang
 *
 */
@IocBean
@At("/assetUnit")
public class InsAssetUnitController {

	@Inject
	private InsAssetUnitService assetUnitService;
	@Inject
	private InsAssetUnitRecordService recordService;
	@Inject
	private RedisService redisService;
	@Inject
	private InsAssetsService assetsService;

	// 跳转首页
	@At("/index")
	@Ok("beetl:/platform/asset/assetUnit/index.html")
	@RequiresPermissions("asset.unit")
	public void index() {
	}

	/**
	 * 获取列表
	 * 
	 * @param vo
	 * @return
	 */
	@At("/list")
	@Ok("json:full")
	@RequiresPermissions("asset.unit.info")
	public Object getAssetUnitlist(@Param("..") AssetsUnitVo vo, @Param("::order") List<DataTableOrder> order,
			@Param("::columns") List<DataTableColumn> columns) {
		return assetUnitService.getAssetUnitList(vo, order, columns);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@At("/operate")
	@Ok("beetl:/platform/asset/assetUnit/approve.html")
	public Object doOperate(@Param("id") Integer id, @Param("type") Integer type) {
		Map map = assetUnitService.getAssetUnitInfo(id);
		map.put("infoTitle", AssetUnitEnum.valueOf(type).toString());
		map.put("infoType", type);
		return map;
	}

	/**
	 * 位置变更 申请借入 申请归还
	 * 
	 * @param id
	 * @param type
	 * @param targetDepart
	 * @param targetPosition
	 * @param remark
	 * @return
	 */
	@At("/doApprove")
	@Ok("json:full")
	@RequiresPermissions("asset.unit.info")
	public Object doApprove(@Param("id") Integer id, @Param("type") Integer type,
			@Param("targetDepart") String targetDepart, @Param("targetPosition") String targetPosition,
			@Param("remark") String remark) {
		boolean flag = true;
		String chargeUnitId = "";
		if (StringUtils.isBlank(targetPosition)) {
			return Result.error("请输入位置信息.");
		}
		try {
			Ins_Asset_Unit unitbean = assetUnitService.getAssetUnitInfoBean(id);
			Subject subject = SecurityUtils.getSubject();
			Sys_user user = (Sys_user) subject.getPrincipal();
			chargeUnitId = unitbean.getChargeDepart();
			if (!AssetUnitEnum.POSITION.toValue().equals(type)) {// 借入/归还
				int count = recordService.getApprovingCountByUnitId(unitbean.getAssetCode(), user.getUnitid(),
						AssetUnitEnum.valueOf(type).toValue());
				if (count > 0) {// 已存在
					String str1 = "您所在单位已经提交该设备的借入申请,请勿重复提交.";
					String str2 = "您所在单位已经提交该设备的归还申请,请勿重复提交.";
					String str = AssetUnitEnum.APPROVEIN.toValue().equals(type) ? str1 : str2;
					return Result.error(str);
				}

				if (AssetUnitEnum.APPROVEIN.toValue().equals(type)) {// 借入
																		// ,判断该设备是否已经借入到该单位
					if (user.getUnitid().equals(unitbean.getUserDepart())) {
						return Result.error("该设备已经借入到您所在单位,请刷新列表.");
					}
				} else {
					if (unitbean.getUserDepart().equals(unitbean.getChargeDepart())) {
						return Result.error("该设备已经归还到责任单位,请刷新列表.");
					}
				}

			}

			Ins_Asset_Unit_Record record = new Ins_Asset_Unit_Record();
			record.setAssetCode(unitbean.getAssetCode());
			record.setChargeDepart(unitbean.getChargeDepart());
			record.setChargeMan(unitbean.getChargeMan());
			record.setOperateTime(new Date());
			record.setOperateType(AssetUnitEnum.valueOf(type).toValue());
			record.setRemark(remark);
			record.setPid(0); // 申请记录父级id 为0
			record.setPosition(targetPosition);
			record.setUserMan(user.getId());
			if (type.equals(AssetUnitEnum.POSITION.toValue())) {// 位置变换

				// unitbean.setPosition(targetPosition);
				unitbean.setOperateTime(new Date());// 位置

				String chargeDepart = unitbean.getChargeDepart();// 责任单位
				String userDepart = unitbean.getUserDepart();// 使用单位

				if (chargeDepart.equals(userDepart)) {// 设备没有借出去,设置使用人
					unitbean.setUserMan(user.getId());// 使用人
				}

				record.setUserDepart(unitbean.getUserDepart());
				record.setOperateType(AssetUnitEnum.POSITION.toValue());
				record.setStatus(null);// 同意状态
				// 更新资产表中的位置
				updateAssetInfoPosition(unitbean.getAssetCode(), targetPosition);
				assetUnitService.updateAssetUnitInfo(unitbean);// 更新 单位资产表
			} else {
				if (AssetUnitEnum.APPROVEIN.toValue().equals(type)) {// 归还
					record.setUserDepart(targetDepart);
				} else {
					record.setUserDepart(unitbean.getUserDepart());
				}
				record.setStatus(AssetUnitEnum.POSITION.toValue());// 申请中
			}
			recordService.insertRecord(record);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		if (flag == true && StringUtils.isNotBlank(chargeUnitId)) {
			redisService.hdel(Globals.ASSETUNITLENDCOUNT, chargeUnitId);
		}

		return flag == true ? Result.success("操作成功.") : Result.error("操作失败.");
	}

	/**
	 * 查看完结状态
	 * 
	 * @param assetCode
	 * @param length
	 * @param start
	 * @param draw
	 * @return
	 */
	@At("/getRecordList")
	@Ok("json:full")
	@RequiresPermissions("asset.unit.info")
	public Object getUnitRecordList(@Param("assetCode") String assetCode, @Param("operateType") String operateType,
			@Param("length") int length, @Param("start") int start, @Param("draw") int draw,
			@Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		return recordService.getRecordList(assetCode, operateType, length, start, draw, order, columns);
	}

	/**
	 * 资产信息-查看页面
	 * 
	 * @param id
	 * @return
	 */
	@At("/doView")
	@Ok("beetl:/platform/asset/assetUnit/unitRecord.html")
	@RequiresPermissions("asset.unit.info")
	public Object doView(@Param("id") Integer id) {

		Map map = assetUnitService.getAssetUnitInfo(id);
		map.put("operateTypeTemp", "1,2");
		return map;
	}

	/**
	 * 位置变更-查看页面
	 * 
	 * @param id
	 * @return
	 */
	@At("/doPositionView")
	@Ok("beetl:/platform/asset/assetUnit/unitRecord.html")
	public Object doPositionView(@Param("id") Integer id) {

		Map map = assetUnitService.getAssetUnitInfo(id);
		map.put("operateTypeTemp", "0");
		return map;
	}

	/**
	 * 跳转申请列表页
	 */
	@At("/approveHtml")
	@Ok("beetl:/platform/asset/assetUnit/approveList.html")
	@RequiresPermissions("asset.unit")
	public Object doApproveListHtml() {
		Map map = new HashMap<>();
		map.put("depart", 1);// 责任单位
		map.put("status", "0");
		map.put("headName", "审批");
		return map;
	}

	/**
	 * 跳转申请记录
	 */
	@At("/applyRecordHtml")
	@Ok("beetl:/platform/asset/assetUnit/approveList.html")
	@RequiresPermissions("asset.unit")
	public Object doApplyHtml() {
		Map map = new HashMap<>();
		map.put("depart", 2);// 使用单位
		map.put("headName", "申请记录");
		map.put("isshow", 1);// 是否展示类型 状态
		return map;
	}

	/**
	 * 跳转审批记录
	 */
	@At("/approveRecordHtml")
	@Ok("beetl:/platform/asset/assetUnit/approveList.html")
	@RequiresPermissions("asset.unit")
	public Object doApproveHtml() {
		Map map = new HashMap<>();
		map.put("depart", 1);
		map.put("status", "1,2");
		map.put("headName", "审批记录");
		map.put("isshow", 1);// 是否展示类型 状态
		map.put("mark", 1);// 审批列表标示,查询列表展示 审批时间
		return map;
	}

	/**
	 * 获取所有申请列表
	 * 
	 * @return
	 */
	@At("/allListByCondition")
	@Ok("json:full")
	@RequiresPermissions("asset.unit")
	public Object getAllList(@Param("..") AssetsUnitVo vo, @Param("::order") List<DataTableOrder> order,
			@Param("::columns") List<DataTableColumn> columns) {
		return recordService.getAllApproveList(vo, order, columns);
	}

	/**
	 * 查看页面
	 * 
	 * @param id
	 * @return
	 */
	@At("/doApproveDetail")
	@Ok("beetl:/platform/asset/assetUnit/approveDetail.html")
	@RequiresPermissions("asset.unit")
	public Object doApproveDetail(@Param("id") Integer id, @Param("type") Integer type) {
		Map map = recordService.getAssetUnitRecordInfoBean(id);
		map.put("type", type);
		return map;
	}

	/**
	 * 审批或驳回
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@At("/doAgreeOrRefuse")
	@Ok("json:full")
	@RequiresPermissions("asset.unit")
	public Object doApproveStatus(@Param("id") Integer id, @Param("status") Integer status) {
		boolean flag = true;
		String chargeUnitId = "";
		try {
			if (id != null) {
				Ins_Asset_Unit_Record bean = recordService.fetchBean(id);
				Integer tempstatus = bean.getOperateType();// 操作
				Integer tempstatus2 = bean.getStatus();
				bean.setStatus(status);// 将该条申请变更为 驳回 或 同意
				Ins_Asset_Unit_Record newbean = new Ins_Asset_Unit_Record();
				converteBean(bean, newbean);

				if (1 == status) {// 同意
					Ins_Asset_Unit asset_Unit = assetUnitService.getAssetUnitInfoBean(bean.getAssetCode());

					if (bean.getOperateType().equals(AssetUnitEnum.REVERT.toValue())) {// 归还
						if (asset_Unit.getChargeDepart().equals(asset_Unit.getUserDepart())) {// 已同意归还了
							return Result.error("该资产已归还,请确认.");
						}

						asset_Unit.setUserDepart(bean.getChargeDepart());
						asset_Unit.setUserMan(bean.getChargeMan());
					} else {
						if (asset_Unit.getStatus() == 1) {
							return Result.error("该资产已被借入到其他部门,请确认.");
						}
						asset_Unit.setUserDepart(bean.getUserDepart());
						asset_Unit.setUserMan(bean.getUserMan());
					}

					asset_Unit.setOperateTime(new Date());
					// asset_Unit.setPosition(bean.getPosition());
					asset_Unit.setStatus(tempstatus == 1 ? 1 : 0);// 申请借入 同意 改为1
																	// 归还同意 改为0
					updateAssetInfoPosition(bean.getAssetCode(), bean.getPosition());
					assetUnitService.update(asset_Unit);

				}

				recordService.updateRecord(bean);
				recordService.insertRecord(newbean);
				chargeUnitId = bean.getChargeDepart();
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		if (flag == true && StringUtils.isNotBlank(chargeUnitId)) {
			redisService.hdel(Globals.ASSETUNITLENDCOUNT, chargeUnitId);
		}
		return flag == true ? Result.success("操作成功") : Result.error("操作失败");
	}

	/**
	 * 根据id获取所有流程详情
	 * 
	 * @param id
	 * @return
	 */
	@At("/unitFlowRecord")
	@Ok("beetl:/platform/asset/assetUnit/unitFlowRecord.html")
	@RequiresPermissions("asset.unit")
	public Object getAllFlow(@Param("id") Integer id) {
		Map map = new HashMap<String, Object>();
		List<Record> recordInfoList = recordService.getAssetUnitRecordInfoList(id);
		map.put("list", recordInfoList);
		return map;
	}

	public void converteBean(Ins_Asset_Unit_Record orecord, Ins_Asset_Unit_Record trecord) {
		trecord.setAssetCode(orecord.getAssetCode());
		trecord.setChargeDepart(orecord.getChargeDepart());
		trecord.setChargeMan(orecord.getChargeMan());
		trecord.setUserDepart(orecord.getUserDepart());
		trecord.setUserMan(orecord.getUserMan());
		trecord.setOperateTime(new Date());
		trecord.setOperateType(orecord.getOperateType());
		trecord.setStatus(orecord.getStatus());
		trecord.setPid(orecord.getId());

	}

	/**
	 * 位置变更列表
	 * 
	 * @param vo
	 * @return
	 */
	@At("/positionList")
	@Ok("json:full")
	@RequiresPermissions("asset.unit.positionList")
	public Object getAssetUnitPositionList(@Param("..") AssetsUnitVo vo, @Param("::order") List<DataTableOrder> order,
			@Param("::columns") List<DataTableColumn> columns) {
		Subject subject = SecurityUtils.getSubject();
		Sys_user user = (Sys_user) subject.getPrincipal();
		List<Sys_role> roles = user.getRoles();
		boolean flag = false; // 区分是否为管理员查看
		if (roles != null) {
			for (int i = 0; i < roles.size(); i++) {
				Sys_role role = roles.get(i);
				String code = role.getCode();
				if (Globals.SUPERADMIN.equalsIgnoreCase(code) || Globals.ADMIN.equalsIgnoreCase(code)) {
					flag = true;
					break;
				}
			}
		}
		if (flag) {// 管理员与超级管理员
			vo.setChargeDepart(null);
		}
		return assetUnitService.getAssetUnitList(vo, order, columns);
	}
	//审批
	public void updateAssetInfoPosition(String assetCode, String position) {
		Ins_Assets assets = assetsService.getAssetsInfo(Cnd.where("assetCode", "=", assetCode));
		assets.setLocationInfo(position);
		assetsService.update(assets);
	}

	// 跳转位置变更列表页
	@At("/positionIndex")
	@Ok("beetl:/platform/asset/assetUnit/positionList.html")
	@RequiresPermissions("asset.unit.positionList")
	public void positionIndex() {
	}
}
