package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ViewModel;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.app.asset.moudules.services.InsAssetUnitService;
import com.znzz.app.asset.moudules.services.InsAssetsService_Czl;
import com.znzz.app.instrument.modules.models.Ins_DeviceInfo;
import com.znzz.app.instrument.modules.service.InsDeviceInfoService;
import com.znzz.app.instrument.modules.service.InsProjectService;
import com.znzz.app.web.commons.services.easypoi.ExportService;
import com.znzz.app.web.commons.services.easypoi.ImportService;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.ImageBinaryUtil;
import com.znzz.app.web.commons.util.ReadExcelUtil;
import com.znzz.app.web.commons.util.TemplateDownUtils;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsForm;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsScrapRecordVo;
import com.znzz.app.web.modules.controllers.platform.asset.vo.AssetsSealedRecordVo;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

/**
 *
 * <p> Title:InsAssets.java </p>
 * <p> Description:资产管理模块controller </p>
 * <p> Company: www.htfudao.com.cn </p>
 * @Package com.znzz.app.web.modules.controllers.platform.asset
 * @author ChangZheng
 * @date 2017年8月21日 下午1:46:53
 * @version V1.0
 */
@IocBean
@At("/asset/info1")
public class InsAssetsController_Czl {

	private static final Log log = Logs.get();
	private static final Logger log2 = Logger.getLogger("scadaLogger");	//scada日志

	private static final Map<String, String> exportMenu = new HashMap<>();
	static {
		exportMenu.put("1", "统一编号"); exportMenu.put("7", "责任人"); exportMenu.put("13", "合同号"); exportMenu.put("19", "台时信息"); exportMenu.put("25", "出厂年月"); exportMenu.put("31", "折扣年限");exportMenu.put("37", "验收人");exportMenu.put("43", "是否借用");
		exportMenu.put("2", "名称"); exportMenu.put("8", "原值"); exportMenu.put("14", "批件序号"); exportMenu.put("20", "管理状态"); exportMenu.put("26", "国别"); exportMenu.put("32", "年折旧率");exportMenu.put("38", "是否过期");exportMenu.put("44", "资产种类");
		exportMenu.put("3", "型号"); exportMenu.put("9", "出厂编号"); exportMenu.put("15", "供货单位"); exportMenu.put("21", "管理级别"); exportMenu.put("27", "厂家"); exportMenu.put("33", "开箱日期");exportMenu.put("39", "军工设备");exportMenu.put("45", "完好状态");
		exportMenu.put("4", "规格"); exportMenu.put("10", "借用日期"); exportMenu.put("16", "项目名称"); exportMenu.put("22", "资产类别"); exportMenu.put("28", "精度等级"); exportMenu.put("34", "检定日期");exportMenu.put("40", "连接云网");exportMenu.put("47", "备注");
		exportMenu.put("5", "类别"); exportMenu.put("11", "归还日期"); exportMenu.put("17", "资金来源"); exportMenu.put("23", "维修状态"); exportMenu.put("29", "课题号"); exportMenu.put("35", "到期检定日期");exportMenu.put("41", "报废状态");
		exportMenu.put("6", "使用单位"); exportMenu.put("12", "存放位置"); exportMenu.put("18", "功率"); exportMenu.put("24", "启用日期"); exportMenu.put("30", "保修期"); exportMenu.put("36", "用途");exportMenu.put("42", "技术指标");
	}

	@Inject
	InsAssetsService_Czl assetsService;
	@Inject
	InsAssertRuleService ruleService;
	@Inject
	InsDeviceInfoService deviceInfoService;
	@Inject
	private InsAssetMouthReportService assetMouthReportService;
	@Inject
	private InsProjectService  projectService;
	@Inject
	private InsAssetLendRecordService lendRecordService;
	@Inject
	InsAssetUnitService assetUnitService;
	@Inject
	ImportService importService;
	@Inject
	ExportService exportService;

	//错误信息
	Map<String, Object> resultMap = new HashMap<String, Object>();
	private String errors = "0";

	// 首页
	@At("")
	@Ok("beetl:/platform/asset/info/index.html")
	@RequiresPermissions("asset.auth.info")
	public void index(HttpServletRequest request) {
		// 给页面定义该变量
		request.setAttribute("errors", "0");
	}

	// 数据项
	@At
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object data(@Param("..") Ins_Assets vo, @Param("length") int length, @Param("start") int start, @Param("draw") int draw,
					   @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		System.out.println("Test hot deploy.....");
		return  assetsService.getAssetsDataWith(vo, length, start, draw, order, columns, null, null,null);
	}

	// 跳转到资产增加页面
	@At
	@Ok("beetl:/platform/asset/info/add.html")
	@RequiresPermissions("asset.auth.info")
	public void add() {

	}

	// 录入资产信息
	@At("/addDo/**")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info.add")
	@SLog(tag="录入资产信息",msg="资产信息编号:${args[0].assetCode}")
	public Object addDo(@Param("..")Ins_Assets assets, @Param("..")Ins_Asset_Rule rule,HttpServletRequest request){

		//处理型号
		if ("".equals(rule.getDeviceVersion())){//型号为空
			rule.setDeviceVersionOrg(assets.getAssetCode());
			assets.setDeviceVersion(assets.getAssetCode());
			rule.setCreateTime(new Date());
			ruleService.insert(rule);
		}

		Ins_DeviceInfo deviceInfo = new Ins_DeviceInfo();
		// 设置录入时间
		rule.setCreateTime(new Date());
		// 是否借出(0是/1否)
		assets.setIsLend(1);
		// 是否过期(0是/1否)
		assets.setIsOverdue(1);
		// 是否链接云网(0是/1否)
		assets.setIsConnectCloud(1);
		// 是否在外场(0否/1是)
		deviceInfo.setOutField(0);
		try {
			// 设置资产统一编号
			deviceInfo.setDeviceCode(assets.getAssetCode());
			// 设置设备名称
			deviceInfo.setDeviceName(rule.getAssetName());
			// 设置型号
			deviceInfo.setDeviceVersion(assets.getDeviceVersion());
			// 使用单位和责任人
			deviceInfo.setBorrowDepart(assets.getBorrowDepart());
			deviceInfo.setChargePerson(assets.getChargePerson());
			// 资产类型
			deviceInfo.setAssetType(assets.getAssetType());
			// 默认是库房
			assets.setBorrowDepart("KF_001");
			deviceInfo.setBorrowDepart("KF_001");
			assetsService.insert(assets);
			deviceInfoService.insert(deviceInfo);

			/*ruleService.insert(rule);*/
			System.out.println(deviceInfo);
			return Result.success("资产信息录入成功");
		} catch (Exception e) {
			log.error("资产信息录入失败");
			e.printStackTrace();
		}
		return Result.error("资产信息录入失败");
	}

	// 检查资产编号是否存在
	@At("/checkAssetCode/?")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object checkAssetCode(@Param("assetCode") String assetCode){
		boolean flag = assetsService.getAssetCodeListAdd(assetCode);
		// 数据库中没有assetCode
		if (flag) {
			return Result.success("可以插入");
		}
		return Result.error("资产编号:"+assetCode+"已经存在");
	}
	// 检查资产编号是否存在
	@At("/checkAssetCodeEdit")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object checkAssetCodeEdit(@Param("assetCode") String assetCode,@Param("id") String id){
		String message = assetsService.getAssetCodeList(assetCode,id);
		// 数据库中没有assetCode
		if ("0".equals(message)){
			return Result.success("可以修改");
		}else if("1".equals(message)){
			return Result.error("资产编号:"+assetCode+"已经存在,请更换!");
		}
		return Result.error("资产编号:"+assetCode+"可能重复,请检查相关数据.");
	}

	// 检查型号是否存在
	@At("/checkDeviceVersion/**")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object checkDeviceVersion(@Param("deviceVersion") String deviceVersion, HttpServletRequest request){
		//型号为空,可以修改
		if ("flag".equals(deviceVersion)){
			return Result.success("可以修改");
		}
		List<Ins_Asset_Rule> list = ruleService.getDeviceVersionList(deviceVersion);
		// 数据库中没有assetCode
		if (list.size() == 1) {
			Ins_Asset_Rule rule = list.get(0);
			String code = new ImageBinaryUtil().getImageBinary(rule.getUrlImage());
			if(null!=code){
				rule.setUrlImage(code);
			}else{
				rule.setUrlImage("");
			}
			return rule;
		}
		return Result.error("型号:"+deviceVersion+"并不存在,或者录入型号有误");
	}

	@At("/checkEdit/?")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info.edit")
	public Object checkEdit(Integer id) {

		return Result.success();
	}

	@At("/edit/?")
    @Ok("beetl:/platform/asset/info/edit.html")
    @RequiresPermissions("asset.auth.info.edit")
    public Object edit(Integer id) {
		/*// 获得资产信息
		AssetsForm assetsForm = assetsService.getAssetAndRuleInfo(id);
		// 将图片进行转化处理
		String code = new ImageBinaryUtil().getImageBinary(assetsForm.getAssets().getUrlImage());
		if(null!=code){
			assetsForm.getAssets().setUrlImage(code);
		}else{
			assetsForm.getAssets().setUrlImage("");
		}
		// 查看该资产是否有关联
		List<String> assetCodeList = assetMouthReportService.assetCodeList(null, null);
		// 是否有绑定
		boolean flag = assetsService.checkBind(assetsForm.getAssets().getAssetCode());
		if (assetCodeList.contains(assetsForm.getAssets().getAssetCode()) || flag) { // 有关联 没有绑定
			assetsForm.setEditState(0);	// 这个设备跟其他记录有关联
		}else {
			assetsForm.setEditState(1);
		}*/

		return null;
    }

	@At
    @Ok("json")
    @RequiresPermissions("asset.auth.info.edit")
    @SLog(tag = "编辑资产信息", msg = "资产编号名称:${args[0].assetCode}")
    public Object editDo(@Param("..") Ins_Assets assets, HttpServletRequest req) {
        try {
        	String assetCode = assets.getAssetCode();
        	String chargePerson = assets.getChargePerson();
        	String borrowDepart = assets.getBorrowDepart();
        	String oldAssetCode = assetsService.getDeviceCode(assets.getId());
        	// 更新资产表(必须先更新表,再更新使用单位和责任人的信息)
        	assetsService.updateIgnoreNull(assets);
        	//往位置变更信息表里插入一条数据
			assetUnitService.insert(null);
        	// 更新device_info表
        	String deviceCode = assets.getAssetCode();
        	//根据原始id查找到原始deviceInfo
        	Ins_DeviceInfo deviceInfo = deviceInfoService.getDeviceInfo(oldAssetCode);
        	//给deviceInfo属性赋值
        	deviceInfo.setDeviceCode(deviceCode);
        	deviceInfo.setAssetType(assets.getAssetType());
        	deviceInfo.setDeviceVersion(assets.getDeviceVersion());
        	//在规格型号表里获取到deviceName
        	String deviceName = ruleService.getDeviceName(assets.getDeviceVersion());
        	deviceInfo.setDeviceName(deviceName);

        	deviceInfoService.update(deviceInfo);
        	// 修改device_info表里的使用单位和责任人
        	assetsService.updateChargePersonAndBorrowDepart(assetCode,borrowDepart,chargePerson);
            return Result.success("操作成功");
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }

	@At("/getinfo")
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    public Object getInfo(@Param("assetCode") String assetCode,HttpServletRequest re) {
		// 获得资产信息
		@SuppressWarnings("rawtypes")
		Map map = assetsService.getAssetAndRuleInfo(assetCode);
		return map;

    }

	@At("/detail/**")
	@Ok("beetl:/platform/asset/info/detail.html")
	@RequiresPermissions("asset.auth.info")
	public Object detail(@Param("id") Integer id, HttpServletRequest req) {
		try {
			AssetsForm assetsForm = assetsService.getAssetAndRuleInfoDetail(id);
			// 图片转成流的形式
			String code = new ImageBinaryUtil().getImageBinary(assetsForm.getUrlImage());
			assetsForm.setUrlImage(code);
			return assetsForm;
		} catch (Exception e) {
			return null;
		}
	}

	@At({"/delete", "/delete/?"})
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object delete(@Param("ids") Integer[] ids, HttpServletRequest req) {
		try {
			if (ids != null && ids.length > 0) {
            	assetsService.delete(ids);
            	return Result.success("删除成功");
            }else {
            	return Result.error("删除失败");
            }
		} catch (Exception e) {
			return Result.error("删除失败");
		}
	}

	@At
	@Ok("beetl:/platform/asset/info/customFind.html")
	@RequiresPermissions("asset.auth.info")
	public Object custom(){

		return null;
	}

	/**
	 *
	 * @param ids   要导出列的id
	 * @param vo	查询条件的Vo(根据查询结果导出数据)
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@At
	@Ok("json")
	@RequiresPermissions("asset.auth.recode.export")
	public String exportAssetsInfo(@Param("ids")String[] ids, @Param("..") Ins_Assets vo, HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException{
		
		List<AssetsForm> list = assetsService.exportAssetsInfo(ids,request,response,exportMenu,vo);
		List<Class<?>> voList= new ArrayList<Class<?>>();
		Class<?> forName = AssetsForm.class;
		voList.add(forName);
		//String url = exportService.exportFile(request, response, "asset_info", list);
		/*Map<String,String> map = new HashMap<String,String>();
		map.put("资产信息", "assetCode,assetName");
		String url = exportService.exportFile(request, response, "asset_info", voList, map, list);*/
		String url = exportService.exportFile(request, response, voList, "asset_info", list);
		
	/*	Map<String,Object> m = new HashMap<String,Object>();
		m.put("assetCode", "1");
		m.put("assetName", "aaaa");
		Map<String,Object> m2 = new HashMap<String,Object>();
		m2.put("assetCode", "2");
		m2.put("assetName", "bbbbb");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list.add(m);
		list.add(m2);
		String url = exportService.exportFile(request, response, "asset_info", list);*/
		return url;
		/*// 返回一个工作簿
		XSSFWorkbook wb = assetsService.exportAssetsInfo(ids,request,response,exportMenu,vo);
		// 将excel表写入本地文件
		String exportPath = Configer.getInstance().getProperty("assetExportPath");
		String filename = Configer.getInstance().getProperty("export_asset_name");
		String url = exportPath + "/" + filename;
		log2.info("exportAssetsInfo exportPath=" + exportPath + " filename=" + filename);
		try{
		//文件夹存在则创建
		File file =new File(exportPath);
		if(!file.exists()  && !file.isDirectory()){
			file.mkdir();
		}
		wb.write(new FileOutputStream(new File(url)));
		}catch(Exception e){
			log2.info("exportAssetsInfo exception=" + e.getMessage());
		}
		log2.info("exportAssetsInfo url=" + url);
		//返给前台一个URL
		// 返回路径给前台
		return url;
*/	}

	@At
	@Ok("void")
	@RequiresPermissions("asset.auth.recode.export")
	public void exportAssetsInfoByUrl(@Param("url")String url,HttpServletRequest request,HttpServletResponse response) throws IOException{
		//获取文件名
        String filename = Configer.getInstance().getProperty("export_asset_name");
		//根据URL找到路径下载,返给前台
		DownloadUtil download = new DownloadUtil();
    	File file = new File(url);
    	download.prototypeDownload(file, filename, response, true);
    	System.out.println("");
	}

	@At("/sealed")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.sealed")
	public Object sealed(@Param("ids") Integer[] ids ){
		try {
			if (ids != null && ids.length > 0) {
            	assetsService.sealed(ids);
            	return Result.success("封存成功");
            }else {
            	return Result.error("封存失败");
            }
		} catch (Exception e) {
			return Result.error("封存失败");
		}
	}

	@At("/unsealed")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.unsealed")
	public Object unsealed(@Param("ids") Integer[] ids ){
		try {
			if (ids != null && ids.length > 0) {
            	assetsService.unsealed(ids);
            	return Result.success("启封成功");
            }else {
            	return Result.error("启封失败");
            }
		} catch (Exception e) {
			return Result.error("启封失败");
		}
	}
	@At("/scrap")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.scrap")
	public Object scrap(@Param("ids") Integer[] ids ){
		try {
			if (ids != null && ids.length > 0) {
            	assetsService.scrap(ids);
            	return Result.success("报废成功");
            }else {
            	return Result.error("报废失败");
            }
		} catch (Exception e) {
			return Result.error("报废失败");
		}
	}

	@At("/confirmScrap")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.confirmScrap")
	public Object confirmScrap(@Param("ids") Integer[] ids ){
		try {
			if (ids != null && ids.length > 0) {
            	assetsService.confirmScrap(ids);
            	return Result.success("确认报废成功");
            }else {
            	return Result.error("确认报废失败");
            }
		} catch (Exception e) {
			return Result.error("确认报废失败");
		}
	}

	@At("/sealedRecord")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object sealedRecord(@Param("..")AssetsSealedRecordVo vo){
		return assetsService.getSealRecordList(vo);
	}
	@At("/scrapRecord")
	@Ok("json:full")
	@RequiresPermissions("asset.auth.info")
	public Object scrapRecord(@Param("..")AssetsScrapRecordVo vo){
		return assetsService.getScrapRecordList(vo);
	}

	// 下载模板
	@At("/templateDownload")
	@Ok("raw")
	@RequiresPermissions("asset.auth.info")
	public Object templateDownload(HttpServletRequest request,HttpServletResponse response,@Param("type") Integer type) throws UnsupportedEncodingException{
		// 得到下载的文件
		File templateFile = null ;
		if(null!=type&&2==type){//履历填充模板
			templateFile = TemplateDownUtils.getTemplateFile("resume_template", "template", response);
		}else{
			templateFile = TemplateDownUtils.getTemplateFile("asset_template", "template", response);
		}
    	return templateFile;
	}

	// 导入功能
    @At
    @Ok("json:full")
    @RequiresPermissions("asset.auth.info")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload/assets" })
    @SuppressWarnings("unchecked")
    public Object upload( @Param("assetFile") File file,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	Result result = Result.error("导入失败");
    	List<ExcelImportResult<AssetsForm>> importFile = importService.importFile(request, file, "ins_asset_info_import");
    	List<AssetsForm> failList = importFile.get(0).getFailList(); //失败的数据集合
    	List<AssetsForm> list = importFile.get(0).getList();
    	if(failList !=null || failList.size()>0 ){
    		response.setContentType("text/html");
    		return result;
    	}
    	
    	/*try {
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			ReadExcelUtil excelUtil = new ReadExcelUtil();
			// 将excel表格中的数据存放到map当中,错误信息也在map当中
			HashMap<String,Object> resultMap = (HashMap<String, Object>) excelUtil.readAssetExcel(path);

			if(resultMap.containsKey("error")){
				String errorMsg = "";
				//获取错误信息,如果有错误信息,直接反馈给前台
				errorMsg = (String) resultMap.get("error");
				//设置响应信息
				response.setContentType("text/html");
				result = Result.error(errorMsg);
			}else {
				List<Ins_Assets> assetsList = (List<Ins_Assets>) resultMap.get("assetInfo");								//ins_asset_info集合信息
				List<Ins_Asset_Rule> rulessList = (List<Ins_Asset_Rule>) resultMap.get("ruleInfo");							//型号集合信息
				List<Ins_DeviceInfo> deviceInfoList = (List<Ins_DeviceInfo>) resultMap.get("deviceInfo");					//ins_device_info集合信息
				List<Ins_ProjectInfo> projectList = (List<Ins_ProjectInfo>) resultMap.get("projectInfo");					//项目集合信息
				List<Ins_Asset_lend_record> lendRecordList = (List<Ins_Asset_lend_record>) resultMap.get("lendRecordInfo");	//借出记录集合信息
				List<Ins_Asset_Unit> assetUnitList = (List<Ins_Asset_Unit>) resultMap.get("assetUnitInfo");					//资产单位集合信息
				//事务的处理(保证原子性)
				Trans.exec(new Atom() {
					@Override
					public void run() {
						//插入数据库
						assetsService.saveOrUpdate(assetsList);				//ins_asset_info表
						ruleService.saveOrUpdate(rulessList);				//型号表
						deviceInfoService.saveOrUpdate(deviceInfoList); 	//ins_device_info表
						projectService.saveOrUpdate(projectList);			//项目表
						//暂不处理
						lendRecordService.saveLendRecord(lendRecordList);	//借出记录表
						assetUnitService.saveOrUpdate(assetUnitList);		//资产单位表信息
					}
				});
				// 设置响应头信息
				response.setContentType("text/html");
				result = Result.success("导入成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = Result.error("导入失败");
		}*/
    	response.setContentType("text/html");
    	result = Result.success("导入成功");
    	return result;
    }

    /**
     * 履历导入,完成填充返回
     * @param file
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
	@At
    @Ok("raw")
    @RequiresPermissions("asset.auth.info")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload/assets" })
    public Object resumeUpload( @Param("resumeFile") File file,HttpServletRequest request,HttpServletResponse response,ViewModel model) throws UnsupportedEncodingException{
    	//String errors = "";
    	try {
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			ReadExcelUtil excelUtil = new ReadExcelUtil();
			// 将excel表格中的数据存放到map当中,错误信息也在map当中
			// excelUtil.readResumeExcelAndFillIt(path);
			// 错误信息,每次先清空,再赋值~
			resultMap.clear();
			resultMap = (Map<String, Object>) excelUtil.readResumeExcelAndFillIt(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 告诉浏览器要下载文件
		String str = "";
		try {
			str = new String("仪器仪表履历.xlsx".getBytes("UTF-8"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	response.setHeader("Content-Disposition", "attachment;filename="+str);
    	// 告诉浏览器要下载文件而不是直接打开文件
    	response.setContentType("application/-download");
    	response.setCharacterEncoding("UTF-8");
    	return file;
    }

    /**
     * 履历填充错误信息提示
     * @return
     */
    @At("/errorMessages")
	@Ok("json:full")
    public Object returnsErrorMsg(){
    	// 先清理,再赋值
    	errors = "";
    	if (resultMap.containsKey("error")) {
			errors = resultMap.get("error").toString();
		}
    	return errors;
    }
}
