package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.znzz.app.sys.modules.models.Sys_role;
import com.znzz.app.web.commons.util.ApplyUtils;
import com.znzz.framework.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import com.znzz.app.asset.moudules.models.Ins_Asset_Repair;
import com.znzz.app.asset.moudules.services.InsAssetRepairService;
import com.znzz.app.asset.moudules.services.InsAssetUnitRecordService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.sys.modules.models.Sys_user;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DateUtils;
import com.znzz.app.web.commons.util.FillDataToWordUtils;
import com.znzz.app.web.modules.controllers.platform.asset.common.RepairEnum;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsRepairVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * 维修管理controller
 * 
 * @author wangqiang
 *
 */
@IocBean
@At("/repair")
public class InsRepairController {

	@Inject
	private InsAssetRepairService repairService;
	@Inject
	private InsAssetUnitRecordService recordService;
	@Inject
	private InsAssetsService assetService;
	@Inject
	private RedisService redisService;
	@Inject
	ShiroUtil shiroUtil;

	// 维修申请
	@At("/add")
	@Ok("beetl:/platform/asset/repair/index.html")
	@RequiresPermissions("repair.manager.add")
	public Object index() {
		Subject subject = SecurityUtils.getSubject();
		Sys_user user = (Sys_user) subject.getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		String unitcode = user.getUnit().getUnitcode();
		String unitid = "";

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

		if (!unitcode.contains(RepairEnum.YQCODE.toString()) && !flag) {// 非仪器室&非管理员
			unitid = user.getUnitid();
		}

		String assetCodeList = assetService.getAssetCodeListByUnitId(unitid);
		map.put("assetCodelist", assetCodeList);
		return map;
	}

	/**
	 * 维修申请添加
	 * 
	 * @param repair
	 * @return
	 */
	@At("/addInfo")
	@Ok("json:full")
	@RequiresPermissions("repair.manager.add")
	public Object addRepair(@Param("..") Ins_Asset_Repair repair) {
		boolean flag = true;
		try {
			// 校验该资产是否已经申报且未完结
			List<Ins_Asset_Repair> list = repairService.getListByAssetCode(repair.getAssetCode());
			for (int i = 0; i < list.size(); i++) {// 只有完结,驳回,报废状态的才可以重新申请
				Ins_Asset_Repair re = list.get(i);
				Integer status = re.getStatus();
				if (!(status.equals(RepairEnum.FINISH.toValue()) || status.equals(RepairEnum.REFUSE.toValue())
						|| status.equals(RepairEnum.SCRAPPED.toValue()))) {
					return Result.error("该资产已申请维修且在处理中,请勿重新申请");
				}
			}
			// 校验通过
			Subject subject = SecurityUtils.getSubject();
			Sys_user user = (Sys_user) subject.getPrincipal();
			repair.setApplyPreson(user.getId());
			repair.setApplyDepart(user.getUnitid());
			repair.setPid(RepairEnum.APPROVE.toValue());
			repair.setOperatorTime(new Date());
			repair.setStatus(RepairEnum.APPROVE.toValue());
			repairService.insert(repair);
			delRepairCountKey();


		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		return flag == true ? Result.success("添加成功") : Result.success("添加失败");

	}

	/**
	 * 维修待审批
	 */
	@At("/approve")
	@Ok("beetl:/platform/asset/repair/repairList.html")
	@RequiresPermissions(value = { "repair.manager" })
	public Object repairApprove(@Param("type") Integer type, @Param("opType") Integer opType,
			@Param("view") Integer view) {
		Map obj = new HashMap<>();

		obj.put("type", type);
		if (opType != null) {
			RepairEnum en = RepairEnum.valueOf(opType);
			obj.put("op", en.toValue());
			obj.put("opName", en.toString());
		} else {
			obj.put("op", null);
			obj.put("opName", "维修记录");
			obj.put("repairProcess", 1);
		}
		if (type == null) {
			obj.put("assetTypeName", "");
		} else if (type == 1) {// 设备
			obj.put("assetTypeName", RepairEnum.DEVICE.toString());
		} else if (type == 2) {// 仪器
			obj.put("assetTypeName", RepairEnum.INSTRUMENT.toString());
		}

		if (view != null) {
			obj.put("view", view);
		}

		return obj;
	}

	@At("/repairList")
	@Ok("json:full")
	@RequiresPermissions(value = { "repair.manager" })
	public NutMap getRepairList(@Param("..") AssetsRepairVo vo, @Param("::order") List<DataTableOrder> order,
			@Param("::columns") List<DataTableColumn> columns, @Param("repairProcess") String repairProcess) {

		if (vo.getView() != null) {// 流程查看菜单,结束菜单,需要区分角色
			// 仪器室看全部
			Subject subject = SecurityUtils.getSubject();
			Sys_user user = (Sys_user) subject.getPrincipal();
			String unitcode = user.getUnit().getUnitcode();

			if (StringUtils.isNotBlank(unitcode)) {

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

				if (!unitcode.contains(RepairEnum.YQCODE.toString()) && !flag) {// 非仪器室&非管理员
					vo.setApplyDepart(user.getUnitid());
				}
			}
		}

		if (StringUtils.isNotBlank(repairProcess)) {
			if (!repairProcess.equals("-1")) {
				vo.setOpType(repairProcess);
			}
		}

		return repairService.getRepairList(vo.getAssetCode(), vo.getApplyDepart(), vo.getApplyPerson(), vo.getType(),
				vo.getOpType(), vo.getTime(), vo.getLength(), vo.getStart(), vo.getDraw(), order, columns);
	}

	/**
	 * 维修待审批
	 */
	@At("/approveHtml")
	@Ok("beetl:/platform/asset/repair/approve.html")
	@RequiresPermissions("repair.manager")
	public Object repairApproveHtml(@Param("id") Integer id, @Param("isshow") Integer isshow) {
		Map map = repairService.getDetail(id);
		map.put("isshow", isshow);
		if (isshow != null && isshow == 1) {
			map.put("list", repairService.viewList(id));
		}

		return map;
	}

	/**
	 * 流程查看
	 */
	@At("/processHtml")
	@Ok("beetl:/platform/asset/repair/process.html")
	@RequiresPermissions("repair.manager")
	public Object repairProcessHtml(@Param("id") Integer id) {
		Map map = repairService.getDetail(id);
		map.put("list", repairService.viewList(id));
		return map;
	}

	/**
	 * 接收操作,要记录送修人和送修单位
	 * 
	 * @return
	 */
	@At("/doApprove")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doApprove(@Param("id") Integer id, @Param("sendDepart") String sendDepart,
			@Param("sendPerson") String sendPerson) {
		Ins_Asset_Repair asset_Repair = new Ins_Asset_Repair();
		asset_Repair.setRemarkDepart(sendDepart);
		asset_Repair.setRemarkMan(sendPerson);
		boolean re = changeRepairBean(id, RepairEnum.REPAIRING.toValue(), asset_Repair);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("接收成功") : Result.error("接收失败");
	}

	/**
	 * 驳回操作
	 * 
	 * @return
	 */
	@At("/doRefuse")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doRefuse(@Param("id") Integer id) {
		boolean re = changeRepairBean(id, RepairEnum.REFUSE.toValue(), null);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("驳回成功") : Result.error("驳回失败");
	}

	/**
	 * 无法维修操作
	 * 
	 * @return
	 */
	@At("/doScrapped")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doScrapped(@Param("id") Integer id, @Param("remark") String remark) {
		boolean re = changeRepairBean(id, RepairEnum.SCRAPPED.toValue(), null);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("流程结束成功") : Result.error("流程结束失败");
	}

	/**
	 * 维修操作
	 * 
	 * @return
	 */
	@At("/doRepair")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doRepair(@Param("id") Integer id, @Param("remark") String remark) {
		boolean re = changeRepairBean(id, RepairEnum.REPAIRING.toValue(), null);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("确认维修成功") : Result.error("确认维修失败");
	}

	/**
	 * 维修完成
	 * 
	 * @return
	 */
	@At("/doRepairing")
	@Ok("json:full")
	@RequiresPermissions("repair.manager") // repairDepart,repairMan,repairContent,repairCost,remark
	public Object doRepairing(@Param("id") Integer id, @Param("type") Integer type,
			@Param("repairDepart") String repairDepart, @Param("repairMan") String repairMan,
			@Param("repairContent") String repairContent, @Param("repairCost") Double repairCost,
			@Param("remark") String remark, @Param("measure") Boolean measure) {
		Integer t;
		Ins_Asset_Repair asset_Repair = new Ins_Asset_Repair();
		if (type != null && type == 2) {// 仪器
			if (null != measure && true == measure) {// 选中计量
				t = RepairEnum.METERING.toValue();
			} else {
				t = RepairEnum.RECEIVE.toValue();
				asset_Repair.setExt1(1);// 没有经过计量的
			}
		} else {// 设备
			t = RepairEnum.ACCEPTING.toValue();
		}

		asset_Repair.setRemark(remark);
		asset_Repair.setRemarkDepart(repairDepart);
		asset_Repair.setRemarkMan(repairMan);
		asset_Repair.setRepairContent(repairContent);
		asset_Repair.setRepairCost(repairCost);

		boolean re = changeRepairBean(id, t, asset_Repair);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("确认维修完成成功") : Result.error("确认维修完成失败");
	}

	/**
	 * 计量
	 * 
	 * @return
	 */
	@At("/doMeter")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doMeter(@Param("id") Integer id, @Param("remark") String remark,
			@Param("repairMan") String repairMan) {
		Subject subject = SecurityUtils.getSubject();
		Sys_user user = (Sys_user) subject.getPrincipal();
		Ins_Asset_Repair asset_Repair = new Ins_Asset_Repair();
		asset_Repair.setRemarkMan(user.getUsername());
		asset_Repair.setRemark(remark);
		boolean re = changeRepairBean(id, RepairEnum.RECEIVE.toValue(), asset_Repair);
		if (re == true) {
			delRepairCountKey();
		}
		return re == true ? Result.success("计量成功") : Result.error("计量失败");
	}

	/**
	 * 验收
	 * 
	 * @return
	 */
	@At("/doFinish")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doFinish(@Param("id") Integer id, @Param("acceptDate") String acceptDate,
			@Param("remark") String remark, @Param("acceptMan") String acceptMan, @Param("type") Integer type) {
		String str1 = "操作成功";
		String str2 = "操作失败";
		boolean re = false;
		if (type != null) {
			Ins_Asset_Repair repair = new Ins_Asset_Repair();
			repair.setRemark(remark); // 备注
			repair.setExt2(acceptDate); // 日期

			if (type == 2) {// 领取
				repair.setRemarkMan(acceptMan);// 领取人
			}

			re = changeRepairBean(id, RepairEnum.FINISH.toValue(), repair);
			if (re == true) {
				delRepairCountKey();
			}

		}
		return re == true ? Result.success(str1) : Result.error(str2);
	}

	/**
	 * 计量返回维修
	 * 
	 * @return
	 */
	@At("/doBackRepair")
	@Ok("json:full")
	@RequiresPermissions("repair.manager")
	public Object doBackRepair(@Param("id") Integer id) {
		String str1 = "返回维修成功";
		String str2 = "返回维修失败";
		boolean re = true;
		Ins_Asset_Repair repairingBean = repairService.getRepairingBean(id);
		if (repairingBean != null) {
			repairingBean.setId(null);
			Ins_Asset_Repair asset_Repair = repairingBean;
			asset_Repair.setStatus(RepairEnum.BACKREPARING.toValue());
			re = changeRepairBean(id, RepairEnum.REPAIRING.toValue(), asset_Repair);
			if (re == true) {
				delRepairCountKey();
			}
		} else {
			re = false;
		}

		return re == true ? Result.success(str1) : Result.error(str2);
	}

	/**
	 * 查询数量
	 * 
	 * @param
	 * @return
	 */

	@At("/getCount")
	@Ok("json:full")
	@RequiresAuthentication
	public Map getRepairListCount() {
		String unitId = "";
		Subject subject = SecurityUtils.getSubject();
		Sys_user user = (Sys_user) subject.getPrincipal();
		String unitcode = user.getUnit().getUnitcode();
		if (StringUtils.isNotBlank(unitcode)) {
			if (!unitcode.contains(RepairEnum.YQCODE.toString())) {// 非仪器室
				unitId = user.getUnitid();
			}
		}
		// 获取该单位下 仪器借调 要审批的个数
		int unitRecordApproveCount = 0;
		if (redisService.hexists(Globals.ASSETUNITLENDCOUNT, user.getUnitid())) {
			String lendCount = redisService.hget(Globals.ASSETUNITLENDCOUNT, user.getUnitid());
			unitRecordApproveCount = Integer.parseInt(lendCount);
		} else {
			unitRecordApproveCount = recordService.getApproveCountByUnitId(user.getUnitid());
			redisService.hset(Globals.ASSETUNITLENDCOUNT, user.getUnitid(), unitRecordApproveCount + "");
		}

		Map map = null;
		if (redisService.exists(Globals.REPAIRCOUNT)) {
			String repaircount = redisService.get(Globals.REPAIRCOUNT);
			map = Json.fromJsonAsMap(Object.class, repaircount);
		} else {
			map = repairService.getCount(unitId);
			redisService.set(Globals.REPAIRCOUNT, Json.toJson(map));
		}
		map.put("unitRecordApproveCount", unitRecordApproveCount);

		return map;
	}

	public boolean changeRepairBean(Integer id, Integer opType, Ins_Asset_Repair asset_repair) {
		boolean flag = true;
		try {
			Ins_Asset_Repair ins_Asset_Repair = repairService.getDetailBean(id);

			if (ins_Asset_Repair.getStatus() >= opType
					&& asset_repair.getStatus() != RepairEnum.BACKREPARING.toValue()) {// 维修进度不能时光倒流,除了无法计量
				return false;
			}
			ins_Asset_Repair.setStatus(opType);

			if (asset_repair == null) {
				asset_repair = new Ins_Asset_Repair();
			}

			final Ins_Asset_Repair ins_Asset_Repair2 = asset_repair;

			makeChildRepairBean(ins_Asset_Repair, ins_Asset_Repair2);

			Trans.exec(new Atom() {
				public void run() {
					// 资产维修状态更改
					if (RepairEnum.REPAIRING.toValue().equals(opType) || RepairEnum.FINISH.toValue().equals(opType)) {// 维修中更改资产信息表中的维修状态为0
						Chain chain = RepairEnum.REPAIRING.toValue().equals(opType)
								? Chain.make("repairState", RepairEnum.APPROVE.toValue())
								: Chain.make("repairState", RepairEnum.REPAIR.toValue());
						//维修中 --> 取消预约
						if(RepairEnum.REPAIRING.toValue().equals(opType)){
							ApplyUtils applyUtils = new ApplyUtils();
							Sys_user user = (Sys_user) shiroUtil.getPrincipal();
							//查资产表里的字段信息进行判断(str1 = 1 被占用 str = 2 可以借)
							Dao dao = Mvcs.ctx().getDefaultIoc().get(Dao.class);
							applyUtils.cancelAccordForbidHandAsset(ins_Asset_Repair.getAssetCode(), applyUtils.pureDate(), dao, "维修");
						}

						Cnd cnd = Cnd.where("assetCode", "=", ins_Asset_Repair.getAssetCode());

						assetService.update(chain, cnd);
					}

					repairService.update(ins_Asset_Repair);
					repairService.insert(ins_Asset_Repair2);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		return flag;

	}

	public void makeChildRepairBean(Ins_Asset_Repair parent, Ins_Asset_Repair child) {
		// 校验通过
		Subject subject = SecurityUtils.getSubject();
		Sys_user user = (Sys_user) subject.getPrincipal();
		child.setAssetCode(parent.getAssetCode());
		child.setOperatorDepart(user.getUnitid());
		child.setOperatorPerson(user.getId());
		child.setOperatorTime(new Date());
		child.setPid(parent.getId());
		child.setStatus(parent.getStatus());
		child.setApplyDepart(parent.getApplyDepart());
		child.setApplyPreson(parent.getApplyPreson());
	}

	/**
	 * 维修或计量结论填写
	 */
	@At("/resultHtml")
	@Ok("beetl:/platform/asset/repair/result.html")
	@RequiresPermissions("repair.manager")
	public Object resultHtml(@Param("id") Integer id, @Param("reusltType") Integer resultType,
			@Param("type") Integer type) {
		Map map = repairService.getDetail(id);
		map.put("reusltType", resultType);
		String str = resultType == 3 ? "维修单" : "计量单";
		map.put("resultPage", str);
		return map;
	}

	/**
	 * 资产详情中查询设备的维修记录
	 */
	@At("/getRepairRecordForAssetInfo")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object getRepairRecordForAssetInfo(@Param("..") AssetsRepairVo vo) {
		NutMap nutMap = repairService.getRepairRecordForAssetInfo(vo.getAssetCode(), vo.getLength(), vo.getStart(),
				vo.getDraw());
		return nutMap;
	}

	/**
	 * 校验 维修是否在流程中
	 */
	@At("/isRepairing")
	@Ok("json:full")
	@RequiresPermissions("repair.manager.add")
	public Object validateIsRepairing(@Param("assetCode") String assetCode) {
		// 校验该资产是否已经申报且未完结
		List<Ins_Asset_Repair> list = repairService.getListByAssetCode(assetCode);
		for (int i = 0; i < list.size(); i++) {// 只有完结,驳回,报废状态的才可以重新申请
			Ins_Asset_Repair re = list.get(i);
			Integer status = re.getStatus();
			if (!(status.equals(RepairEnum.FINISH.toValue()) || status.equals(RepairEnum.REFUSE.toValue())
					|| status.equals(RepairEnum.SCRAPPED.toValue()))) {
				return Result.error("该资产已申请维修且在处理中,请勿重新申请");
			}
		}
		return Result.success("资产没有在流程中");
	}

	/**
	 * 删除维修数量的key
	 */
	public void delRepairCountKey() {
		redisService.del(Globals.REPAIRCOUNT);
	}

	/**
	 * 验收或领取
	 */
	@At("/accpetHtml")
	@Ok("beetl:/platform/asset/repair/accept.html")
	@RequiresPermissions("repair.manager")
	public Object accpetHtml(@Param("id") Integer id, @Param("type") Integer type,
			@Param("operateTime") String operateTime) {
		Map map = new HashMap<String, Object>();
		map.put("type", type);
		String str = type == 1 ? "验收单" : "领取单";
		map.put("id", id);
		map.put("str", str);
		map.put("operateTime", operateTime);
		return map;
	}

	/**
	 * 生成报告
	 */
	@At("/getReport")
	@Ok("raw")
	@RequiresPermissions(value = { "repair.manager" })
	public Object getReport(HttpServletRequest request, HttpServletResponse response, @Param("id") Integer id) {

		Map detail = repairService.getDetail(id);
		Integer assetType = (Integer) detail.get("assetType");
		File file = null;
		if (assetType != null) {
			if (assetType == 2) {// 仪器仪表
				file = generateInstrumentReport(detail, response);
			} else {// 设备
				file = generateDeviceReport(detail, response);
			}
		}

		return file;
	}

	/**
	 * 生成仪器仪表单子
	 * 
	 * @param detail
	 * @param response
	 * @return
	 */
	public File generateInstrumentReport(Map detail, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("T", DateUtils.converteToDay(detail.get("applyTime"), 3) + detail.get("id"));// 维修单号
		map.put("A", isBlank(detail.get("name")));// 送修单位
		map.put("B", isBlank(detail.get("linkMan")));// 联系人
		map.put("C", isBlank(detail.get("linkPhone")));// 联系电话
		map.put("D", isBlank(detail.get("assetName")));// 仪器名称
		map.put("E", isBlank(detail.get("deviceVersion")));// 型号规格
		Object assetCode = isBlank(detail.get("assetCode"));
		map.put("F", assetCode);// 统一编号
		map.put("G", isBlank(detail.get("ext2")));// 附件
		map.put("H", isBlank(detail.get("remark")));// 故障原因
		map.put("I", isBlank(detail.get("username")));// 送修人
		map.put("J", DateUtils.converteToDay(detail.get("sendDate"), 1));// 送修时间
		map.put("K", isBlank(detail.get("repairRemark")));// 维修内容
		map.put("L", isBlank(detail.get("remarkMan")));// 审核人
		map.put("M", DateUtils.converteToDay(detail.get("repairDate"), 1));// 日期
		map.put("N", isBlank(detail.get("measureResult")));// 计量结果
		map.put("O", isBlank(detail.get("measureMan")));// 计量人
		map.put("P", DateUtils.converteToDay(detail.get("measureDate"), 1));// 计量日期
		map.put("Q", isBlank(detail.get("acceptMan")));// 领取人
		map.put("R", isBlank(detail.get("acceptDate")));// 领取日期

		String exportPath = Configer.getInstance().getProperty("repairExportPath");
		String templatePath = Configer.getInstance().getProperty("repairFtlTemplatePath");
		String instumentTemplateName = Configer.getInstance().getProperty("instumentTemplateName");
		String prefixName = Configer.getInstance().getProperty("instrumentPrefixName");
		String fileName = FillDataToWordUtils.createWord(templatePath, instumentTemplateName, map,
				assetCode + prefixName, exportPath);

		return getFile(fileName, exportPath, response);

	}

	/**
	 * 生成设备维修及验收单子
	 * 
	 * @param detail
	 * @param response
	 * @return
	 */
	public File generateDeviceReport(Map detail, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object assetCode = isBlank(detail.get("assetCode"));
		map.put("A", assetCode);// 统一编号
		map.put("B", isBlank(detail.get("applyDepartName")));// 申办单位
		map.put("C", DateUtils.converteToDay(detail.get("applyTime"), 2));// 申办日期
		map.put("D", isBlank(detail.get("assetName")));// 设备名称
		map.put("E", isBlank(detail.get("deviceVersion")));// 型号规格
		map.put("F", isBlank(detail.get("remark")));// 故障原因
		map.put("H",
				isBlank(detail.get("applyPresonName")) + "    " + DateUtils.converteToDay(detail.get("applyTime"), 2));// 申请人+申请日期
		map.put("I", isBlank(detail.get("repairRemark")));// 维修内容
		map.put("J", isBlank(detail.get("remarkDepart")));// 维修单位
		map.put("O", isBlank(detail.get("remarkMan")));// 维修人
		map.put("L", isBlank(detail.get("acceptRemark")));// 设备运转情况
		map.put("M", isBlank(detail.get("applyPresonName")));// 验收人员
		map.put("N", DateUtils.converteToDay(detail.get("acceptDate"), 2));// 验收日期
		String exportPath = Configer.getInstance().getProperty("repairExportPath");
		String templatePath = Configer.getInstance().getProperty("repairFtlTemplatePath");
		String deviceTemplateName = Configer.getInstance().getProperty("deviceTemplateName");
		String prefixName = Configer.getInstance().getProperty("devicePrefixName");
		String fileName = FillDataToWordUtils.createWord(templatePath, deviceTemplateName, map, assetCode + prefixName,
				exportPath);

		return getFile(fileName, exportPath, response);

	}

	public Object isBlank(Object value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	public File getFile(String filename, String filepath, HttpServletResponse response) {
		// 告诉浏览器要下载文件
		String str = "";
		try {
			str = new String(filename.getBytes("UTF-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment;filename=" + str);
		// 告诉浏览器要下载文件而不是直接打开文件
		response.setContentType("application/-download");
		response.setCharacterEncoding("UTF-8");
		// 获取项目路径
		String path = filepath + filename;
		File file = new File(path);
		return file;
	}

	/**
	 * 根据统一编号查找资产详情
	 * 
	 * @param assetCode
	 * @param re
	 * @return
	 */
	@At("/getinfo")
	@Ok("json:full")
	public Object getInfo(@Param("assetCode") String assetCode, HttpServletRequest re) {
		// 获得资产信息
		Map map = assetService.getAssetAndRuleInfo(assetCode);
		return map;
	}

}
