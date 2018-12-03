package com.znzz.app.web.modules.controllers.platform.instruments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.UploadAdaptor;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;
import com.znzz.app.instrument.modules.models.Ins_DeviceState;
import com.znzz.app.instrument.modules.service.InsCollectService;
import com.znzz.app.instrument.modules.service.InsDeviceFacilityService;
import com.znzz.app.instrument.modules.service.InsDeviceService;
import com.znzz.app.instrument.modules.service.InsSwitchingFlowService;
import com.znzz.app.instrument.modules.service.InsbcardService;
import com.znzz.app.instrument.modules.service.ScadaDeviceServcie;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.slog.annotation.SLog;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.app.web.commons.util.ReadExcelUtil;
import com.znzz.app.web.commons.util.TemplateDownUtils;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.CollectBindDeviceBean;
import com.znzz.app.web.modules.controllers.platform.instruments.formBean.DeviceForm;
import com.znzz.framework.base.Result;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

@IocBean
@At("/instrument/monitor/collect")
public class InsCollectController {
	private static final Logger log = Logger.getLogger("scadaLogger");
	
	@Inject
	private InsCollectService collectService;
	@Inject 
	private InsDeviceService deviceInfoService;
	@Inject
	private InsAssetsService assetsService;
	@Inject
	private InsDeviceFacilityService deviceFacilityService;
	@Inject
	private InsSwitchingFlowService switchFlowService;

	@Inject
	ScadaDeviceServcie   scadaDeviceServcie;

	@Inject
	private InsbcardService bCardServcice;
	@Inject
	private InsDeviceService deviceService;
   

	// 首页
	@At("")
	@Ok("beetl:/platform/monitor/collect/index.html")
	@RequiresPermissions("instrument.monitor.collect")
	public void index() {

	}
	
    @At
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    public Object data(@Param("..") DeviceForm deviceForm,@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
     	// 获取采集器和绑定设备的信息
    	return collectService.getCollectDataWith(deviceForm,length, start, draw, order, columns, null, null);
    }
    
    @At
    @Ok("beetl:/platform/monitor/collect/add.html")
    @RequiresPermissions("instrument.monitor.collect")
    public void add() {
    	
    }
    
    @At
    @Ok("json")
    @SLog(tag="采集器绑定设备",msg = "采集器编号:${args[0].collectCode}")
    @RequiresPermissions("instrument.monitor.collect.add")
    public Object addDo(@Param("..")Ins_Collect collect) {
    	// 设置创建时间
    	collect.setCreateTime(new Date());
    	try {
    		String deviceCode = collect.getDeviceCode();
    		// 判断使用单位和责任人是否为空
    		//boolean flag = deviceInfoService.checkDeviceInfoDepartAndPerson(deviceCode);
    		
    		String collectCode = collect.getCollectCode();
    		
    		// 调用SCADA接口
    		//String result = scadaDeviceServcie.getResult(collectCode, "SetBCard");
    		
    		//Map<String, String> map = bCardServcice.getOrignCodeAndDeviceCode();
    		String orignCode = bCardServcice.fetch(Cnd.where("bcardCode","=",collectCode)).getOrignCode();
        	//String productCode = map.get(deviceCode);
        	String[] data = {orignCode};
			String result = scadaDeviceServcie.getResult(data , Globals.SETBCARD);
			
    		if("1".equals(result)){
    			// 绑定设备
    			collectService.insert(collect);
    			// 删除key
        		bCardServcice.delOrignCodeAndDeviceCodeKey();
        		//设置链接云网状态为0(连接云网)
            	assetsService.updateConnectCloud(0,collect.getDeviceCode());
            	// 往设备状态表里插入一条数据(device_state表)
            	Ins_DeviceState deviceState = new Ins_DeviceState();
            	deviceState.setDeviceCode(collect.getDeviceCode());	//设置设备编号
            	deviceState.setState(1);							//状态是离线
            	// 往设备状态表里插入数据
            	deviceService.insert(deviceState);
            	log.info("scada:编号为"+deviceCode+"绑定成功");
        		return Result.success("编号为"+deviceCode+"绑定成功");
    		}else if("0".equals(result)){
    			log.info("scada:编号为"+deviceCode+"绑定失败");
    			return Result.error("编号为"+deviceCode+"绑定失败");
    		}else{
    			log.info("scada接口错误，绑定失败");
    			return Result.error("scada接口错误，绑定失败");
    		}

		} catch (Exception e) {
			return Result.error("调取SCADA接口错误");
		}
    }
	
    @At({"/delete", "/delete/?"})
    @Ok("json")
    @RequiresPermissions("instrument.monitor.collect.delete")
    public Object delete(Integer id, @Param("ids") String[] ids, HttpServletRequest req) {
    	
        try {
        	Result re = null;
            if (ids != null && ids.length > 0) {// 多个设备
            	String productCodes  = "";
              	
            	for(String iD :ids){
            		Ins_Collect collect = collectService.fetch(Integer.valueOf(iD));
                	String deviceCode = collect.getDeviceCode();
                	Map<String, String> map = bCardServcice.getOrignCodeAndDeviceCode();
                	String productCode = map.get(deviceCode);
                	productCodes += productCode + ",";
            	}
            	org.apache.commons.lang3.StringUtils.strip(productCodes, ",");
            	String[] param = { productCodes };
            	String result = scadaDeviceServcie.invoke(param, Globals.DELETEBCARDBATCH);
            	if(null==result){
            		re = Result.error("scada接口错误，解绑失败");
            	}else{
            		/** 解析返回json字符串 **/
        			JSONObject object = new JSONObject(result);
        			JSONArray jsonArray = object.getJSONArray("deviceidList");
        			String errorCode = "";
        			for (int i = 0; i < jsonArray.length(); i++) {
        				JSONObject obj = jsonArray.getJSONObject(i);
        				if (0 == obj.getInt("result")) {
        					errorCode += obj.getString("deviceid") + ",";
        				}
        				if (1 == obj.getInt("result")) {
        					String deviceid = obj.getString("deviceid");
        					
        					Cnd cnd = Cnd.NEW();
                    		cnd.and("orignCode", "=",deviceid );
                    		Ins_DeviceBcard ins_DeviceBcard = bCardServcice.fetch(cnd);
                    		String bcardCode = ins_DeviceBcard.getBcardCode();//采集器编号
                    		
                    		Cnd cnd1 = Cnd.NEW();
                    		cnd1.and("collectCode", "=",bcardCode );
                    		Ins_Collect collect = collectService.fetch(cnd1);
        					
        					Integer id_collect = collect.getId();//collect id
        					String deviceCode = collect.getDeviceCode();//统一编号
        	            	//String deviceCode = collect.getDeviceCode();
        					// 更新云网信息
                    		assetsService.updateConnectCloud(1,deviceCode);
                    		/*// 清除设备状况里的绑定信息
                    		deviceFacilityService.clear("ins_device_facility", Cnd.where("deviceCode", "=", deviceCode));*/
                    		// 清除设备状态表里的信息
                    		deviceService.clear("ins_device_state", Cnd.where("deviceCode", "=", deviceCode));
                    		//删除库
                        	collectService.delete(id_collect);
                        	// 删除key
                        	bCardServcice.delOrignCodeAndDeviceCodeKey();
                        	// 状态履历结束
                        	switchFlowService.setSwitchFlowEnd(deviceCode);
        					
        				}
        			}
        			errorCode = org.apache.commons.lang3.StringUtils.strip(errorCode, ",");
        			if (org.apache.commons.lang3.StringUtils.isNotBlank(errorCode)) {
        				log.info("编号为:" + errorCode + "解绑失败,");
        				re = Result.error("编号为:" + errorCode + "解绑失败");
        			}else{
        				log.info("调用SCADA成功，批量解绑成功");
        				re= Result.success("调用SCADA成功，批量解绑成功");
        			}
            	}
            	
            } else {	// 单个设备
            	// 根据id查询出所有deviceCode/assetCode
            	Ins_Collect collect = collectService.fetch(id);
            	String deviceCode = collect.getDeviceCode();
            	Map<String, String> map = bCardServcice.getOrignCodeAndDeviceCode();
            	String productCode = map.get(deviceCode);
            	String[] param = {productCode};
				String result = scadaDeviceServcie.getResult(param , Globals.DELETEBCARD); 
            			//scadaDeviceServcie.getResult(deviceCode, "DeleteBCard");
            
            	if("1".equals(result)){
            		// 更新云网信息
            		assetsService.updateConnectCloud(1,deviceCode);
            		/*// 清除设备状况里的绑定信息
            		deviceFacilityService.clear("ins_device_facility", Cnd.where("deviceCode", "=", deviceCode));*/
            		// 清除设备状态表里的信息
            		deviceService.clear("ins_device_state", Cnd.where("deviceCode", "=", deviceCode));
                	collectService.delete(id);
                	// 删除key
                	bCardServcice.delOrignCodeAndDeviceCodeKey();
                	// 状态履历结束
                	switchFlowService.setSwitchFlowEnd(deviceCode);
                	log.info("scada:编号为"+deviceCode + ",解绑成功");
            		return Result.success("编号为:"+deviceCode + ",解绑成功");
            	}else if("0".equals(result)){
            		log.info("scada:编号为"+deviceCode + ",解绑失败");
            		return Result.error("编号为:"+deviceCode + ",解绑失败");
            	}else {
            		log.info("scada接口错误，解绑失败");
            		return Result.error("scada接口错误，解绑失败");
            	}
         
            }
            return re;
          
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
    
    @At("/collectCodeList")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    public Object collectCodeList(@Param("collectCode")String collectCode) {
    	// 获取所有采集器编号
    	List<String> list = collectService.getCollectCodeList(collectCode);
    	if (list != null && list.size() > 0) {
			return Result.error("system.error");
		}
    	return Result.success("system.success");
    }
    
    @At("/deviceCodeListSelect")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    public Object deviceCodeListSelect(@Param("deviceCode")String deviceCode) {
    	NutMap re = collectService.getDeviceCodeList();
    	return re;
    }
    
    @At("/deviceCodeList/**")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    public Object deviceCodeList(@Param("deviceCode")String deviceCode) {
    	// 获取所有采集器编号
    	List<String> list = collectService.getDeviceCodeList(deviceCode);
    	if (list != null && list.size() > 0) {
    		return Result.error("该设备已经绑定过采集器了,如果需要绑定,请先解绑~");
    	}
    	List<CollectBindDeviceBean> deviceInfoList = collectService.getDeviceInfo(deviceCode);
    	if (deviceInfoList != null && deviceInfoList.size() == 1) {
    		String json = Json.toJson(deviceInfoList.get(0));
			return json;
		}
    	
    	return Result.error();
    }
    
    // 模板下载
    @At()
    @Ok("raw")
    @RequiresPermissions("instrument.monitor.collect")
    public Object downLoad(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	// 获取采集器导入模板
    	return TemplateDownUtils.getTemplateFile("collect_template", "template", response);
    	
    }
    
    // 导入功能
    @At
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    @AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/upload" })
    @SuppressWarnings("unchecked")
    public Object upload( @Param("collectFile") File file,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
    	Result result = null;
		try {
			// 获取文件的绝对路径
			String path = file.getAbsolutePath();
			ReadExcelUtil excelUtil = new ReadExcelUtil();
			// 将excel表格中的数据存放到list当中
			// List<CollectBindDeviceBean> collect_devicetList = excelUtil.readExcel(path);
			Object object = excelUtil.readExcel(path);
			if (object instanceof  HashMap) {
				Map<String, String> map = (HashMap<String, String>)object;
				String errorMsg = map.get("error");
				response.setContentType("text/html");
				result = Result.error(errorMsg);
			}else {
				List<CollectBindDeviceBean> collect_devicetList = (List<CollectBindDeviceBean>) object;
				String re="" ;
				// 插入数据库当中
				if(collect_devicetList.size()>0){
					re = collectService.insertList(collect_devicetList);
				}
				//设置响应头信息
				response.setContentType("text/html");
				if(StringUtils.isNotBlank(re)){
					result = Result.error(re);
				}else{
					result = Result.success("导入成功");	  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    // 导出采集器设备
    @At
    @Ok("void")
    @RequiresPermissions("instrument.monitor.collect")
    @SLog(tag="导出采集器数据",msg = "采集器与设备绑定信息")
    public void export(HttpServletResponse response, HttpServletRequest request) throws Exception{
    	// 获取导出模板的路径
    	String exportTemplatePath = (String) Configer.getInstance().get("export_template");
    	//linux下jdk1.8 方法获取时，不会拼接自己写的目录
		String path = request.getSession().getServletContext().getRealPath("/") + exportTemplatePath;
		// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get("collect_template");
		InputStream is = new FileInputStream(new File(path + filename));

		Workbook wb = new XSSFWorkbook(is);		//打开一个模板文件，工作簿 2007以上版本
		Sheet sheet = wb.getSheetAt(0);			//获取到第一个工作表
		
		Row nRow = null;
		Cell nCell = null;
		int rowNo = 0;							//行号
		int colNo = 1;							//列号
		
		//获取模板上的单元格样式
		nRow = sheet.getRow(2);
		
		//采集器编号的样式
		nCell = nRow.getCell(1);
		CellStyle collectCodeStyle = nCell.getCellStyle();		
		
		//采集器名称的样式
		/*nCell = nRow.getCell(2);
		CellStyle collectNameStyle = nCell.getCellStyle();*/		
		
		//设备名称的样式
		nCell = nRow.getCell(2);
		CellStyle deviceNameStyle = nCell.getCellStyle();		
		
		//设备型号的样式
		nCell = nRow.getCell(3);
		CellStyle deviceVersionStyle = nCell.getCellStyle();		
		
		//统一编号的样式
		nCell = nRow.getCell(4);
		CellStyle assetCodeStyle = nCell.getCellStyle();
		
		nCell = nRow.getCell(5);
		CellStyle borrowDepartStyle = nCell.getCellStyle();	
		
		nCell = nRow.getCell(6);
		CellStyle chargePersonStyle = nCell.getCellStyle();	
		
		nCell = nRow.getCell(7);
		CellStyle timeStyle = nCell.getCellStyle();		
		
		//处理大标题
		nRow = sheet.getRow(rowNo++);			//获取一个行对象
		nCell = nRow.getCell(colNo);			//获取一个单元格对象
//		nCell.setCellValue(inputDate.replaceFirst("-0", "-").replaceFirst("-", "年") + "月份出货表");		//yyyy-MM
		nCell.setCellValue("采集器数据导出表");		//yyyy-MM
		//sheet.setColumnWidth(6, 26);
		rowNo++;								//跳过静态表格头
		//处理内容(获取list,进行处理)
		List<CollectBindDeviceBean> dataList = collectService.getExportList();
		for(int j=0;j<dataList.size();j++){
			//sheet.autoSizeColumn(colNo);
			nRow.setHeightInPoints(34);
			
			//InsExcel insExcel = new InsExcel();
			colNo = 1;				//初始化
			CollectBindDeviceBean cbd = dataList.get(j);
			
			nRow = sheet.createRow(rowNo++);
			nRow.setHeightInPoints(24);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getCollectCode());
			nCell.setCellStyle(collectCodeStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getDeviceName());
			nCell.setCellStyle(deviceNameStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getDeviceVersion());
			nCell.setCellStyle(deviceVersionStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getDeviceCode());
			nCell.setCellStyle(assetCodeStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getBorrowDepart());
			nCell.setCellStyle(borrowDepartStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getChargePerson());
			nCell.setCellStyle(chargePersonStyle);
			
			nCell = nRow.createCell(colNo++);
			nCell.setCellValue(cbd.getCreateTime());
			nCell.setCellStyle(timeStyle);
			
			nCell = nRow.createCell(colNo++);
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		wb.write(os);
		
		DownloadUtil downloadUtil = new DownloadUtil();				//直接弹出下载框，用户可以打开，可以保存
		downloadUtil.download(os, response, "采集器导出数据表.xlsx");
		
		os.flush();
		os.close();
		
    }
  	
/*  	@At("/updateCloud/?")
    @Ok("json:full")
    @RequiresPermissions("instrument.monitor.collect")
    public Object updateCloud(@Param("deviceCode")String deviceCode) {
  		boolean flag = assetsService.updateConnectCloud(0, deviceCode);
  		log.info("资产编号为: "+deviceCode+" 连接云网");
    	return null;
    }*/
  	
  	@At("/bcardList")
  	@Ok("json:full")
  	@RequiresPermissions("instrument.monitor.collect")
  	public Object bcardList() {
  		return collectService.getBCardList();
  	}
  	
    //lur
    @At("/check")
    @Ok("json")
    @RequiresPermissions("instrument.monitor.collect")
    public Object check(String devicecode) {
    	
    	//Ins_Collect ins_Collect = collectService.fetch(Cnd.where("deviceCode", "=", devicecode));
      	 //=====sacada接口操作=========
    	Map<String, String> map = bCardServcice.getOrignCodeAndDeviceCode();
    	String productCode = map.get(devicecode);
    	String[] data = {productCode};
      	String result = scadaDeviceServcie.getResult(data, Globals.TESTBCARD);
      	if (result==null){ //接口返回为null，返回值为02，前台提示接口错误
      		log.info("scada接口错误，检测失败");
      		return "2";
      	}else{
      		if("1".equals(result)){
      			log.info("scada：编号为"+devicecode+"检测成功");
      		}else{
      			log.info("scada：编号为"+devicecode+"检测失败");
      		}
      		
      		return result;
      	}
    }
	
}
