package com.znzz.app.web.modules.controllers.platform.asset;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.znzz.app.asset.moudules.models.*;
import com.znzz.app.asset.moudules.services.InsAssetLendRecordService;
import com.znzz.app.asset.moudules.services.InsAssetMouthReportService;
import com.znzz.app.asset.moudules.services.InsAssetsService;
import com.znzz.app.web.commons.base.Globals;
import com.znzz.app.web.commons.util.Configer;
import com.znzz.app.web.commons.util.MonthReportUtils;
import com.znzz.framework.base.Result;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import com.znzz.app.web.commons.util.DownloadUtil;
import com.znzz.framework.page.datatable.DataTableColumn;
import com.znzz.framework.page.datatable.DataTableOrder;

/**
 * @author fengguoxin
 * @ClassName: InsAssetMonthreportController
 * @Description: TODO(月度报告controller)
 * @date 2017年9月12日 上午10:51:59
 */
@IocBean
@At("/asset/report")
public class InsAssetMonthreportController {
    private static final Log log = Logs.get();
    @Inject
    private InsAssetMouthReportService insAssetMouthReportService;
    @Inject
    private InsAssetLendRecordService assetLendRecordService;
    @Inject
    private InsAssetsService insAssetsService;

    // 首页
    @At("")
    @Ok("beetl:/platform/asset/report/index.html")
    //@RequiresPermissions("asset.report")
    public void index() {

    }

    // 跳转到生成月度报告选择时间页面
    @At
    @Ok("beetl:/platform/asset/report/add.html")
    //@RequiresPermissions("aasset.report")
    public void add() {

    }

    /**
     * 月度报告list
     *
     * @param
     * @param start
     * @param length
     * @param draw
     * @return
     */
    @At
    @Ok("json:full")
    public Object data(@Param("fileName") String fileName, @Param("month") String yearMonth, @Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        // 对传入日期进行处理
        if (yearMonth != null && !"".equals(yearMonth)) {

            Date startDate = null;
            Date endDate = null;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int year = Integer.parseInt(yearMonth.split("-")[0]);
                int month = Integer.parseInt(yearMonth.split("-")[1]);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);    //当前月
                int maxday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                cal.set(Calendar.DAY_OF_MONTH, maxday);
                //cal.set(Calendar.DAY_OF_MONTH, 25);
                String last = format.format(cal.getTime());

                cal.set(Calendar.MONTH, month - 1);    //上个月（支持1月的上个月去年12月）
                cal.set(Calendar.DAY_OF_MONTH, 1);
                String first = format.format(cal.getTime());
                startDate = format.parse(first);
                endDate = format.parse(last);

            } catch (Exception e) {
                log.error("时间转换异常");
                e.printStackTrace();
            }
            cnd = Cnd.where("month", "between", new Object[]{startDate, endDate});
        }
        //去除首尾空格
        fileName = fileName.trim();
        if (!Strings.isBlank(fileName)) {
            cnd.and("fileName", "like", "%" + fileName + "%");
        }
        NutMap monthreport = insAssetMouthReportService.data(length, start, draw, order, columns, cnd, null);
        return monthreport;
    }

    // 月度报告下载
    @At("/downLoad/?")
    @Ok("void")
    //@RequiresPermissions("instrument.monitor.collect")
    public void downLoad(@Param("id") String id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        Cnd cnd = Cnd.NEW();
        cnd.and("id", "=", id);
        Ins_Month_report ins_Month_report = insAssetMouthReportService.fetch(cnd);

        String fileName2 = ins_Month_report.getFileName();
        String filePath = ins_Month_report.getFilePath();

        DownloadUtil download = new DownloadUtil();
        String filename = fileName2 + ".xlsx";
        File file = new File(filePath);
        download.prototypeDownload(file, filename, response, false);
    }

    //生成月度报告
   @At("/manuallyGeneratedDo/**")
   @Ok("json:full")
   public Object manuallyGeneratedDo(@Param("month") String  month, HttpServletRequest request, HttpServletResponse response) {
       int year = Integer.parseInt(month.split("-")[0]);
       int monthI = Integer.parseInt(month.split("-")[1]);
       try {

           //获取数据
           NutMap allRecodeListMap = insAssetMouthReportService.getAllRecodeList(month);
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
           lendRecord(year, monthI, list_lend, sheetLend, "资产借出记录", "资产借出记录(无记录)");

           //归还记录
           returnRecord(year, monthI, list_return, sheetReturn, "资产归还记录", "资产归还记录(无记录)");

           //转账记录
           transferRecord(year, monthI, list_transferred, sheetLend, sheetTransfer);

           //送修记录
           repairSongRecord(year, monthI, list_repairSong, sheetRepairing);

           //维修完成记录
           repairedRecord(year, monthI, list_repaired, sheetRepaired);

           String monthreport_path = (String) Configer.getInstance().get("monthreport_path");
           File file =new File(monthreport_path);
           //如果文件夹不存在则创建
           //file .exists()表示的目录是否存在
           //file .isDirectory()  当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true；否则返回 false
           if  (!file .exists()  && !file .isDirectory())	{
               file .mkdir();
           }
           return getReportSuccess(year, monthI, wb, monthreport_path);

       } catch (Exception e) {
            log.info("写数据到EXCEL失败");
           return Result.error("生成失败");
       }

   }

    private Object getReportSuccess(int year, int monthI, Workbook wb, String monthreport_path) throws IOException {
        String filepath = monthreport_path +"/" +year+"年"+monthI+"月资产报告.xlsx";
        Ins_Month_report ins_Month_report = new Ins_Month_report();
        ins_Month_report.setFileName(year+"年"+monthI+"月资产报告");
        ins_Month_report.setFilePath(filepath);
        Date data = new Date();
        ins_Month_report.setMonth(data);
        insAssetMouthReportService.insert(ins_Month_report);
        wb.write(new FileOutputStream(new File(filepath)));
        return Result.success("生成成功");
    }

    private void repairedRecord(int year, int monthI, List<Ins_Asset_Repair> list_repaired, Sheet sheetRepaired) {
        if(null!=list_repaired&&list_repaired.size()>0){
            //获取到第四个工作表
            Row nRow = null;
            Cell nCell = null;
            int rowNo = 0;							//行号
            int colNo = 0;							//列号
            //获取模板上的单元格样式
            nRow = sheetRepaired.getRow(2);
            //处理大标题
            nRow = sheetRepaired.getRow(rowNo++);			//获取一个行对象
            nCell = nRow.getCell(colNo);			//获取一个单元格对象
            nCell.setCellValue(year+"年"+monthI+"月"+"资产维修完成记录");
            rowNo++;								//跳过静态表格头
            for(int j=0;j<list_repaired.size();j++){
                //sheet.autoSizeColumn(colNo);
                GetForRepairedRecord getForRepairedRecord = new GetForRepairedRecord(list_repaired, sheetRepaired, nRow, rowNo, j).invoke();
                nRow = getForRepairedRecord.getnRow();
                rowNo = getForRepairedRecord.getRowNo();
            }
        }else{
            //获取到四个工作表
            MonthReportUtils.getFourRecord(year, monthI, sheetRepaired, "资产维修完成记录(无记录)");
        }
    }


    private void repairSongRecord(int year, int monthI, List<Ins_Asset_Repair> list_repairSong, Sheet sheetRepairing) {
        if(null!=list_repairSong&&list_repairSong.size()>0){
            //获取到第五个工作表
            Row nRow = null;
            Cell nCell = null;
            int rowNo = 0;							//行号
            int colNo = 0;							//列号
            //获取模板上的单元格样式
            nRow = sheetRepairing.getRow(2);
            //处理大标题
            nRow = sheetRepairing.getRow(rowNo++);			//获取一个行对象
            nCell = nRow.getCell(colNo);			//获取一个单元格对象
            nCell.setCellValue(year+"年"+monthI+"月"+"资产送修记录");
            rowNo++;								//跳过静态表格头
            for(int j=0;j<list_repairSong.size();j++){
                //sheet.autoSizeColumn(colNo);
                GetForSongRepaired getForSongRepaired = new GetForSongRepaired(list_repairSong, sheetRepairing, nRow, rowNo, j).invoke();
                nRow = getForSongRepaired.getnRow();
                rowNo = getForSongRepaired.getRowNo();

            }
        }else{
            //获取到五个工作表
            MonthReportUtils.getFiveSheet(year, monthI, sheetRepairing, "资产送修记录(无记录)");
        }
    }

    private void transferRecord(int year, int monthI, List<Ins_Asset_lend_record> list_transferred, Sheet sheetLend, Sheet sheetTransfer) {
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
            nCell.setCellValue(year +"年"+monthI+"月"+"资产转账记录");
            rowNo++;
            for (int j = 0; j < list_transferred.size(); j++) {
                GetForLendRecord getForLendRecord = new GetForLendRecord(list_transferred, sheetTransfer, nRow, rowNo, j).invoke();
                nRow = getForLendRecord.getnRow();
                rowNo = getForLendRecord.getRowNo();

            }
        }else{
            //获取到第三个工作表
            MonthReportUtils.getThirdSheet(year, monthI, sheetLend);
        }
    }

    private void returnRecord(int year, int monthI, List<Ins_Asset_lend_record> list_return, Sheet sheetReturn, String 资产归还记录, String s) {
        if (null != list_return && list_return.size() > 0) {
            //获取到第二个工作表
            Row nRow = null;
            Cell nCell = null;
            int rowNo = 0;                            //行号
            int colNo = 0;                            //列号
            //获取模板上的单元格样式
            nRow = sheetReturn.getRow(2);
            //处理大标题
            nRow = sheetReturn.getRow(rowNo++);            //获取一个行对象
            nCell = nRow.getCell(colNo);            //获取一个单元格对象
            nCell.setCellValue(year + "年" + monthI + "月" + 资产归还记录);
            rowNo++;                                //跳过静态表格头
            for (int j = 0; j < list_return.size(); j++) {
                //sheet.autoSizeColumn(colNo);
                GetForReturnRecord getForReturnRecord = new GetForReturnRecord(list_return, sheetReturn, nRow, rowNo, j).invoke();
                nRow = getForReturnRecord.getnRow();
                rowNo = getForReturnRecord.getRowNo();

            }
        } else {
            MonthReportUtils.getSecondSheet(year, monthI, sheetReturn, s);
        }
    }

    private void lendRecord(int year, int monthI, List<Ins_Asset_lend_record> list_lend, Sheet sheetLend, String 资产借出记录, String s) {
        if (null != list_lend && list_lend.size() > 0) {

            //获取到第一个工作表
            Row nRow = null;
            Cell nCell = null;
            int rowNo = 0;                            //行号
            int colNo = 0;                            //列号
            //获取模板上的单元格样式
            nRow = sheetLend.getRow(2);
            //处理大标题
            nRow = sheetLend.getRow(rowNo++);            //获取一个行对象
            nCell = nRow.getCell(colNo);            //获取一个单元格对象
            nCell.setCellValue(year + "年" + monthI + "月" + 资产借出记录);
            rowNo++;                                //跳过静态表格头
            for (int j = 0; j < list_lend.size(); j++) {
                //sheet.autoSizeColumn(colNo);
                GetForSheetLendRecord getForSheetLendRecord = new GetForSheetLendRecord(list_lend, sheetLend, nRow, rowNo, j).invoke();
                nRow = getForSheetLendRecord.getnRow();
                rowNo = getForSheetLendRecord.getRowNo();

            }
        } else {
            //获取到第一个工作表
            MonthReportUtils.getFirstSheet(year, monthI, sheetLend, s);
        }
    }

    private class GetForLendRecord {
        private List<Ins_Asset_lend_record> list_transferred;
        private Sheet sheetTransfer;
        private Row nRow;
        private int rowNo;
        private int j;

        public GetForLendRecord(List<Ins_Asset_lend_record> list_transferred, Sheet sheetTransfer, Row nRow, int rowNo, int j) {
            this.list_transferred = list_transferred;
            this.sheetTransfer = sheetTransfer;
            this.nRow = nRow;
            this.rowNo = rowNo;
            this.j = j;
        }

        public Row getnRow() {
            return nRow;
        }

        public int getRowNo() {
            return rowNo;
        }

        public GetForLendRecord invoke() {
            int colNo;Cell nCell;
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

            //现使用单位K71U8DBPNE-eyJsaWNlbnNlSWQiOiJLNzFVOERCUE5FIiwibGljZW5zZWVOYW1lIjoibGFuIHl1IiwiYXNzaWduZWVOYW1lIjoiIiwiYXNzaWduZWVFbWFpbCI6IiIsImxpY2Vuc2VSZXN0cmljdGlvbiI6IkZvciBlZHVjYXRpb25hbCB1c2Ugb25seSIsImNoZWNrQ29uY3VycmVudFVzZSI6ZmFsc2UsInByb2R1Y3RzIjpbeyJjb2RlIjoiSUkiLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJSUzAiLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJXUyIsInBhaWRVcFRvIjoiMjAxOS0wNS0wNCJ9LHsiY29kZSI6IlJEIiwicGFpZFVwVG8iOiIyMDE5LTA1LTA0In0seyJjb2RlIjoiUkMiLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJEQyIsInBhaWRVcFRvIjoiMjAxOS0wNS0wNCJ9LHsiY29kZSI6IkRCIiwicGFpZFVwVG8iOiIyMDE5LTA1LTA0In0seyJjb2RlIjoiUk0iLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJETSIsInBhaWRVcFRvIjoiMjAxOS0wNS0wNCJ9LHsiY29kZSI6IkFDIiwicGFpZFVwVG8iOiIyMDE5LTA1LTA0In0seyJjb2RlIjoiRFBOIiwicGFpZFVwVG8iOiIyMDE5LTA1LTA0In0seyJjb2RlIjoiR08iLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJQUyIsInBhaWRVcFRvIjoiMjAxOS0wNS0wNCJ9LHsiY29kZSI6IkNMIiwicGFpZFVwVG8iOiIyMDE5LTA1LTA0In0seyJjb2RlIjoiUEMiLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifSx7ImNvZGUiOiJSU1UiLCJwYWlkVXBUbyI6IjIwMTktMDUtMDQifV0sImhhc2giOiI4OTA4Mjg5LzAiLCJncmFjZVBlcmlvZERheXMiOjAsImF1dG9Qcm9sb25nYXRlZCI6ZmFsc2UsImlzQXV0b1Byb2xvbmdhdGVkIjpmYWxzZX0=-Owt3/+LdCpedvF0eQ8635yYt0+ZLtCfIHOKzSrx5hBtbKGYRPFDrdgQAK6lJjexl2emLBcUq729K1+ukY9Js0nx1NH09l9Rw4c7k9wUksLl6RWx7Hcdcma1AHolfSp79NynSMZzQQLFohNyjD+dXfXM5GYd2OTHya0zYjTNMmAJuuRsapJMP9F1z7UTpMpLMxS/JaCWdyX6qIs+funJdPF7bjzYAQBvtbz+6SANBgN36gG1B2xHhccTn6WE8vagwwSNuM70egpahcTktoHxI7uS1JGN9gKAr6nbp+8DbFz3a2wd+XoF3nSJb/d2f/6zJR8yJF8AOyb30kwg3zf5cWw==-MIIEPjCCAiagAwIBAgIBBTANBgkqhkiG9w0BAQsFADAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBMB4XDTE1MTEwMjA4MjE0OFoXDTE4MTEwMTA4MjE0OFowETEPMA0GA1UEAwwGcHJvZDN5MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxcQkq+zdxlR2mmRYBPzGbUNdMN6OaXiXzxIWtMEkrJMO/5oUfQJbLLuMSMK0QHFmaI37WShyxZcfRCidwXjot4zmNBKnlyHodDij/78TmVqFl8nOeD5+07B8VEaIu7c3E1N+e1doC6wht4I4+IEmtsPAdoaj5WCQVQbrI8KeT8M9VcBIWX7fD0fhexfg3ZRt0xqwMcXGNp3DdJHiO0rCdU+Itv7EmtnSVq9jBG1usMSFvMowR25mju2JcPFp1+I4ZI+FqgR8gyG8oiNDyNEoAbsR3lOpI7grUYSvkB/xVy/VoklPCK2h0f0GJxFjnye8NT1PAywoyl7RmiAVRE/EKwIDAQABo4GZMIGWMAkGA1UdEwQCMAAwHQYDVR0OBBYEFGEpG9oZGcfLMGNBkY7SgHiMGgTcMEgGA1UdIwRBMD+AFKOetkhnQhI2Qb1t4Lm0oFKLl/GzoRykGjAYMRYwFAYDVQQDDA1KZXRQcm9maWxlIENBggkA0myxg7KDeeEwEwYDVR0lBAwwCgYIKwYBBQUHAwEwCwYDVR0PBAQDAgWgMA0GCSqGSIb3DQEBCwUAA4ICAQC9WZuYgQedSuOc5TOUSrRigMw4/+wuC5EtZBfvdl4HT/8vzMW/oUlIP4YCvA0XKyBaCJ2iX+ZCDKoPfiYXiaSiH+HxAPV6J79vvouxKrWg2XV6ShFtPLP+0gPdGq3x9R3+kJbmAm8w+FOdlWqAfJrLvpzMGNeDU14YGXiZ9bVzmIQbwrBA+c/F4tlK/DV07dsNExihqFoibnqDiVNTGombaU2dDup2gwKdL81ua8EIcGNExHe82kjF4zwfadHk3bQVvbfdAwxcDy4xBjs3L4raPLU3yenSzr/OEur1+jfOxnQSmEcMXKXgrAQ9U55gwjcOFKrgOxEdek/Sk1VfOjvS+nuM4eyEruFMfaZHzoQiuw4IqgGc45ohFH0UUyjYcuFxxDSU9lMCv8qdHKm+wnPRb0l9l5vXsCBDuhAGYD6ss+Ga+aDY6f/qXZuUCEUOH3QUNbbCUlviSz6+GiRnt1kA9N2Qachl+2yBfaqUqr8h7Z2gsx5LcIf5kYNsqJ0GavXTVyWh7PYiKX4bs354ZQLUwwa/cG++2+wNWP+HtBhVxMRNTdVhSm38AknZlD+PTAsWGu9GyLmhti2EnVwGybSD2Dxmhxk3IPCkhKAK+pl0eWYGZWG3tJ9mZ7SowcXLWDFAk0lRJnKGFMTggrWjV8GYpw5bq23VmIqqDLgkNzuoog==
            //
            //作者：禾灮
            //链接：https://www.jianshu.com/p/38499ad947c4
            //來源：简书
            //简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
            //简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(cbd.getApplyDepart());
            //现使用人
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(cbd.getApplyPerson());
            return this;
        }
    }

    private class GetForRepairedRecord {
        private List<Ins_Asset_Repair> list_repaired;
        private Sheet sheetRepaired;
        private Row nRow;
        private int rowNo;
        private int j;

        public GetForRepairedRecord(List<Ins_Asset_Repair> list_repaired, Sheet sheetRepaired, Row nRow, int rowNo, int j) {
            this.list_repaired = list_repaired;
            this.sheetRepaired = sheetRepaired;
            this.nRow = nRow;
            this.rowNo = rowNo;
            this.j = j;
        }

        public Row getnRow() {
            return nRow;
        }

        public int getRowNo() {
            return rowNo;
        }

        public GetForRepairedRecord invoke() {
            int colNo;Cell nCell;
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
            //计量日期
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(cbd.getExt2());
            //计量结论
            nCell = nRow.createCell(colNo++);
            return this;
        }
    }

    private class GetForReturnRecord {
        private List<Ins_Asset_lend_record> list_return;
        private Sheet sheetReturn;
        private Row nRow;
        private int rowNo;
        private int j;

        public GetForReturnRecord(List<Ins_Asset_lend_record> list_return, Sheet sheetReturn, Row nRow, int rowNo, int j) {
            this.list_return = list_return;
            this.sheetReturn = sheetReturn;
            this.nRow = nRow;
            this.rowNo = rowNo;
            this.j = j;
        }

        public Row getnRow() {
            return nRow;
        }

        public int getRowNo() {
            return rowNo;
        }

        public GetForReturnRecord invoke() {
            int colNo;Cell nCell;
            nRow.setHeightInPoints(34);
            colNo = 0;                //初始化
            Ins_Asset_lend_record cbd = list_return.get(j);
            nRow = sheetReturn.createRow(rowNo++);
            nRow.setHeightInPoints(24);
            // 设置编号
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(cbd.getAssetCode());
            //设置名称
            Cnd cnd = Cnd.NEW();
            cnd.and("assetCode", "=", cbd.getAssetCode());
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
            return this;
        }
    }

    private class GetForSongRepaired {
        private List<Ins_Asset_Repair> list_repairSong;
        private Sheet sheetRepairing;
        private Row nRow;
        private int rowNo;
        private int j;

        public GetForSongRepaired(List<Ins_Asset_Repair> list_repairSong, Sheet sheetRepairing, Row nRow, int rowNo, int j) {
            this.list_repairSong = list_repairSong;
            this.sheetRepairing = sheetRepairing;
            this.nRow = nRow;
            this.rowNo = rowNo;
            this.j = j;
        }

        public Row getnRow() {
            return nRow;
        }

        public int getRowNo() {
            return rowNo;
        }

        public GetForSongRepaired invoke() {
            int colNo;Cell nCell;
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
            return this;
        }
    }

    private class GetForSheetLendRecord {
        private List<Ins_Asset_lend_record> list_lend;
        private Sheet sheetLend;
        private Row nRow;
        private int rowNo;
        private int j;

        public GetForSheetLendRecord(List<Ins_Asset_lend_record> list_lend, Sheet sheetLend, Row nRow, int rowNo, int j) {
            this.list_lend = list_lend;
            this.sheetLend = sheetLend;
            this.nRow = nRow;
            this.rowNo = rowNo;
            this.j = j;
        }

        public Row getnRow() {
            return nRow;
        }

        public int getRowNo() {
            return rowNo;
        }

        public GetForSheetLendRecord invoke() {
            int colNo;Cell nCell;
            nRow.setHeightInPoints(34);
            colNo = 0;                //初始化
            Ins_Asset_lend_record cbd = list_lend.get(j);
            nRow = sheetLend.createRow(rowNo++);
            nRow.setHeightInPoints(24);
            // 设置编号
            nCell = nRow.createCell(colNo++);
            nCell.setCellValue(cbd.getAssetCode());
            //设置名称
            Cnd cndLend = Cnd.NEW();
            cndLend.and("assetCode", "=", cbd.getAssetCode());
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
            return this;
        }
    }
}
