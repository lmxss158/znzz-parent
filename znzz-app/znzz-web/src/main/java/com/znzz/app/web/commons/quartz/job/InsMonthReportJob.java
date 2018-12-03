package com.znzz.app.web.commons.quartz.job;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.znzz.app.asset.moudules.models.Ins_Asset_Repair;
import com.znzz.app.asset.moudules.models.Ins_Asset_Rule;
import com.znzz.app.asset.moudules.models.Ins_Asset_lend_record;
import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.asset.moudules.models.Ins_Month_report;
import com.znzz.app.asset.moudules.services.InsAssertRuleService;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.Configer;

@IocBean
public class InsMonthReportJob implements Job{
	private static final Log log = Logs.get();
	@Inject
	InsAssetLendRecordService assetLendRecordService;
	@Inject
	InsAssetMouthReportService insAssetMouthReportService;
	@Inject
	InsAssertRuleService assertRuleService;
	@Inject
	InsAssetsService insAssetsService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		log.info("月度报告任务开始~~~~~~~~~~");
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		
		String nianyue = dateFormat.format( date ); 
		
	     Calendar now = Calendar.getInstance();  
	      int year = now.get(Calendar.YEAR);
	      int month = now.get(Calendar.MONTH) +1;
		try {
		
			//获取数据
			NutMap allRecodeListMap = insAssetMouthReportService.getAllRecodeList(nianyue);
			List<Ins_Asset_lend_record>  list_lend = (List<Ins_Asset_lend_record>) allRecodeListMap.get("lend");
			List<Ins_Asset_lend_record>  list_return = (List<Ins_Asset_lend_record>) allRecodeListMap.get("return");
			List<Ins_Asset_lend_record>  list_transferred = (List<Ins_Asset_lend_record>) allRecodeListMap.get("transferred");
			List<Ins_Asset_Repair>  list_repairSong = (List<Ins_Asset_Repair>) allRecodeListMap.get("repairing");
			List<Ins_Asset_Repair>  list_repaired = (List<Ins_Asset_Repair>) allRecodeListMap.get("repaired");
			//获取excel模板
			String export_template = (String) Configer.getInstance().get("export_template");
			String monthreport_template = (String) Configer.getInstance().get("monthReport_template");
			String appRoot = Globals.AppRoot;
			String path = appRoot + export_template+monthreport_template;
			InputStream is = new FileInputStream(new File(path));
			Workbook wb = new XSSFWorkbook(is);		//打开一个模板文件，工作簿 2007以上版本
			Sheet sheetLend = wb.getSheetAt(0);
			Sheet sheetReturn = wb.getSheetAt(1);
			Sheet sheetTransfer = wb.getSheetAt(2);
			Sheet sheetRepairing = wb.getSheetAt(3);
			Sheet sheetRepaired = wb.getSheetAt(4);

		    //借出记录
			if(null!=list_lend&&list_lend.size()>0){

				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetLend.getRow(2);
				//处理大标题
				nRow = sheetLend.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产借出记录");	
				rowNo++;								//跳过静态表格头
				for(int j=0;j<list_lend.size();j++){
					//sheet.autoSizeColumn(colNo);
					nRow.setHeightInPoints(34);
					colNo = 0;				//初始化
					Ins_Asset_lend_record cbd = list_lend.get(j);
					nRow = sheetLend.createRow(rowNo++);
					nRow.setHeightInPoints(24);
					// 设置编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getAssetCode());
				    //设置名称
					Cnd cndLend = Cnd.NEW();
					cndLend.and("assetCode","=",cbd.getAssetCode());
					Ins_Assets ins_Assets = insAssetsService.fetch(cndLend);
					String deviceVersion2 = ins_Assets.getDeviceVersion();
	         	    Ins_Asset_Rule ins_Asset_Rule = assetLendRecordService.fetchIns_Asset_Rule(deviceVersion2);
	         	    String assetName = ins_Asset_Rule.getAssetName();
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(assetName);
					//型号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(deviceVersion2);
					//出厂编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(ins_Assets.getSerialNumber());
					//日期
					nCell = nRow.createCell(colNo++);
				    String formatDate = DateFormatUtils.format(cbd.getOprateTime(), "yyyy-MM-dd HH:mm:ss");
					nCell.setCellValue(formatDate);
					//部门
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyDepart());
					//姓名
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyPerson());
				
				}
			}else{
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetLend.getRow(2);
				//处理大标题
				nRow = sheetLend.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产借出记录(无记录)");	
			}

			//归还记录
			if(null!=list_return&&list_return.size()>0){
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetReturn.getRow(2);
				//处理大标题
				nRow = sheetReturn.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产归还记录");	
				rowNo++;								//跳过静态表格头
				for(int j=0;j<list_return.size();j++){
					//sheet.autoSizeColumn(colNo);
					nRow.setHeightInPoints(34);
					colNo = 0;				//初始化
					Ins_Asset_lend_record cbd = list_return.get(j);
					nRow = sheetReturn.createRow(rowNo++);
					nRow.setHeightInPoints(24);
					// 设置编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getAssetCode());
				    //设置名称
					Cnd cnd = Cnd.NEW();
					cnd.and("assetCode","=",cbd.getAssetCode());
					Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
					String deviceVersion2 = ins_Assets.getDeviceVersion();
	         	    Ins_Asset_Rule ins_Asset_Rule = assetLendRecordService.fetchIns_Asset_Rule(deviceVersion2);
	         	    String assetName = ins_Asset_Rule.getAssetName();
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(assetName);
					//型号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(deviceVersion2);
					//出厂编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(ins_Assets.getSerialNumber());
					//日期
					nCell = nRow.createCell(colNo++);
				    String formatDate = DateFormatUtils.format(cbd.getOprateTime(), "yyyy-MM-dd HH:mm:ss");
					nCell.setCellValue(formatDate);
					//部门
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyDepart());
					//姓名
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyPerson());
				
				}
			}else{
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetReturn.getRow(2);
				//处理大标题
				nRow = sheetReturn.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产归还记录(无记录)");	
			}

			//转账记录
			if(null != list_transferred && list_transferred.size() >0){

				//获取到第三个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0; 										//行号
				int colNo = 0; 										//列号

				//获取模板上的单元格样式
				nRow = sheetTransfer.getRow(2);

				//处理大标题
				nRow = sheetTransfer.getRow(rowNo++);				//获取第一个行对象
				nCell = nRow.getCell(colNo);						//获取一个单元格对象
				nCell.setCellValue(year +"年"+month+"月"+"资产转账记录");
				rowNo++;
				for (int j = 0; j < list_transferred.size(); j++) {
					nRow.setHeightInPoints(34);
					colNo = 0; 										//初始化
					Ins_Asset_lend_record cbd = list_transferred.get(j);
					nRow = sheetTransfer.createRow(rowNo++);
					nRow.setHeightInPoints(24);

					//设置统一编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getAssetCode());

					//设置名称
					Cnd cndTransfer = Cnd.NEW();
					cndTransfer.and("assetCode","=",cbd.getAssetCode());
					Ins_Assets ins_assets = insAssetsService.fetch(cndTransfer);
					String deviceVersion2 = ins_assets.getDeviceVersion();
					Ins_Asset_Rule ins_asset_rule = assetLendRecordService.fetchIns_Asset_Rule(deviceVersion2);
					String assetName = ins_asset_rule.getAssetName();
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(assetName);

					//型号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(deviceVersion2);

					//出厂编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(ins_assets.getSerialNumber());

					//日期
					nCell = nRow.createCell(colNo++);
					String formatDateO = DateFormatUtils.format(cbd.getOprateTime(), "yyyy-MM-dd HH:mm:ss");
					nCell.setCellValue(formatDateO);

					//原使用单位
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getBorrowDepart());

					//原使用人
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getChargePerson());

					//现使用单位
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyDepart());
					//现使用人
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getApplyPerson());

				}
			}else{
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;									//行号
				int colNo = 0;									//列号
				//获取模板上的单元格样式
				nRow = sheetLend.getRow(2);
				//处理大标题
				nRow = sheetLend.getRow(rowNo++);				//获取一个行对象
				nCell = nRow.getCell(colNo);					//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产转账记录(无记录)");
			}

			//送修记录
			if(null!=list_repairSong&&list_repairSong.size()>0){
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetRepairing.getRow(2);
				//处理大标题
				nRow = sheetRepairing.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产送修记录");	
				rowNo++;								//跳过静态表格头
				for(int j=0;j<list_repairSong.size();j++){
					//sheet.autoSizeColumn(colNo);
					nRow.setHeightInPoints(34);
					colNo = 0;				//初始化
					Ins_Asset_Repair cbd = list_repairSong.get(j);
					nRow = sheetRepairing.createRow(rowNo++);
					nRow.setHeightInPoints(24);
					// 设置编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getAssetCode());
				    //设置名称
					Cnd cnd = Cnd.NEW();
					cnd.and("assetCode","=",cbd.getAssetCode());
					Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
					String deviceVersion2 = ins_Assets.getDeviceVersion();
	         	    Ins_Asset_Rule ins_Asset_Rule = assetLendRecordService.fetchIns_Asset_Rule(deviceVersion2);
	         	    String assetName = ins_Asset_Rule.getAssetName();
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(assetName);
					//型号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(deviceVersion2);
					//出厂编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(ins_Assets.getSerialNumber());
					//日期
					nCell = nRow.createCell(colNo++);
				    String formatDate = DateFormatUtils.format(cbd.getOperatorTime(), "yyyy-MM-dd HH:mm:ss");
					nCell.setCellValue(formatDate);

				}
			}else{
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetRepairing.getRow(2);
				//处理大标题
				nRow = sheetRepairing.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产送修记录(无记录)");	
			}
			//维修完成记录
			if(null!=list_repaired&&list_repaired.size()>0){
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetRepaired.getRow(2);
				//处理大标题
				nRow = sheetRepaired.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产维修完成记录");	
				rowNo++;								//跳过静态表格头
				for(int j=0;j<list_repaired.size();j++){
					//sheet.autoSizeColumn(colNo);
					nRow.setHeightInPoints(34);
					colNo = 0;				//初始化
					Ins_Asset_Repair cbd = list_repaired.get(j);
					nRow = sheetRepaired.createRow(rowNo++);
					nRow.setHeightInPoints(24);
					// 设置编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getAssetCode());
				    //设置名称
					Cnd cnd = Cnd.NEW();
					cnd.and("assetCode","=",cbd.getAssetCode());
					Ins_Assets ins_Assets = insAssetsService.fetch(cnd);
					String deviceVersion2 = ins_Assets.getDeviceVersion();
	         	    Ins_Asset_Rule ins_Asset_Rule = assetLendRecordService.fetchIns_Asset_Rule(deviceVersion2);
	         	    String assetName = ins_Asset_Rule.getAssetName();
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(assetName);
					//型号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(deviceVersion2);
					//出厂编号
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(ins_Assets.getSerialNumber());
					//日期
					nCell = nRow.createCell(colNo++);
					if(null!=cbd.getOperatorTime()){
						String formatDate = DateFormatUtils.format(cbd.getOperatorTime(), "yyyy-MM-dd HH:mm:ss");
						nCell.setCellValue(formatDate);
					}else{
						nCell.setCellValue("");
					}
	 /********************************************************czl**************************************/
					//计量日期
					nCell = nRow.createCell(colNo++);
					nCell.setCellValue(cbd.getExt2());
					//计量结论
					nCell = nRow.createCell(colNo++);

	 /********************************************************czl**************************************/
				}
			}else{
				//获取到第一个工作表
				Row nRow = null;
				Cell nCell = null;
				int rowNo = 0;							//行号
				int colNo = 0;							//列号
				//获取模板上的单元格样式
				nRow = sheetRepaired.getRow(2);
				//处理大标题
				nRow = sheetRepaired.getRow(rowNo++);			//获取一个行对象
				nCell = nRow.getCell(colNo);			//获取一个单元格对象
				nCell.setCellValue(year+"年"+month+"月"+"资产维修完成记录(无记录)");	
			}			
			String monthreport_path = (String) Configer.getInstance().get("monthreport_path");
			File file =new File(monthreport_path);    
			//如果文件夹不存在则创建    
			//file .exists()表示的目录是否存在
			//file .isDirectory()  当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true；否则返回 false 
			if  (!file .exists()  && !file .isDirectory())	{      
			    file .mkdir();    
			}
		    String filepath = monthreport_path +"/" +year+"年"+month+"月资产报告.xlsx";
		    Ins_Month_report  ins_Month_report = new Ins_Month_report();
		    ins_Month_report.setFileName(year+"年"+month+"月资产报告");
		    ins_Month_report.setFilePath(filepath);
		    Date data = new Date();
		    ins_Month_report.setMonth(data);
		    insAssetMouthReportService.insert(ins_Month_report);
			wb.write(new FileOutputStream(new File(filepath)));
			
		} catch (Exception e) {
			
		}
	
		log.info("月度报告任务结束~~~~~~~~~~");
	}

	
}
