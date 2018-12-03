import com.znzz.app.web.commons.util.CommonExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.lang.Strings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelTest {

    public static void main(String[] args) {

        //文件
        File file = new File("D:\\Personal\\Desktop\\云网检索\\Find2018-10-29.xlsx");
        InputStream is = null;
        try {
            // 将文件放入流中
            is = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            //创建要封装的对象/集合
            BObject bObject = null;
            CloudObject cObject = null;
            DObject dObject = null;
            List<BObject> bList = new ArrayList<>();
            List<CloudObject> cList = new ArrayList<>();
            List<DObject> dList = new ArrayList<>();

            Sheet b = xssfWorkbook.getSheet("B卡");
            Sheet c = xssfWorkbook.getSheet("C卡");
            Sheet d = xssfWorkbook.getSheet("D");
            for (int rowNum = 1; rowNum <= b.getLastRowNum(); rowNum++) {
                Row bRow = b.getRow(rowNum);
                bObject = new BObject();
                bObject.setbId(CommonExcelUtil.getValue(bRow.getCell(0)).trim());
                bObject.setbAssetCode(CommonExcelUtil.getValue(bRow.getCell(1)).trim());
                bObject.setbAssetName(CommonExcelUtil.getValue(bRow.getCell(2)).trim());
                bObject.setbDeviceVersion(CommonExcelUtil.getValue(bRow.getCell(3)).trim());
                bObject.setbBorrowDepart(CommonExcelUtil.getValue(bRow.getCell(4)).trim());
                bObject.setbCountry(CommonExcelUtil.getValue(bRow.getCell(5)).trim());
                bList.add(bObject);
            }

            for (int rowNum = 1; rowNum <= c.getLastRowNum(); rowNum++) {
                Row cRow = c.getRow(rowNum);
                cObject = new CloudObject();
                cObject.setcSequenceId(CommonExcelUtil.getValue(cRow.getCell(0)).trim());
                cObject.setcAssetCode(CommonExcelUtil.getValue(cRow.getCell(1)).trim());
                cObject.setcId(CommonExcelUtil.getValue(cRow.getCell(2)).trim());
                cObject.setcAssetName(CommonExcelUtil.getValue(cRow.getCell(3)).trim());
                cObject.setcDeviceVersion(CommonExcelUtil.getValue(cRow.getCell(4)).trim());
                cObject.setcAssetGroup(CommonExcelUtil.getValue(cRow.getCell(5)).trim());
                cList.add(cObject);
            }
            for (int rowNum = 1; rowNum <= d.getLastRowNum(); rowNum++) {
                dObject = new DObject();
                Row dRow = d.getRow(rowNum);
                dObject.setdId(CommonExcelUtil.getValue(dRow.getCell(0)).trim());
                String dAssetCode = CommonExcelUtil.getValue(dRow.getCell(1)).trim();
                dObject.setdAssetCode(dAssetCode);
                for (BObject bVo : bList) {
                    //遍历B表 如果DSheet表中统一编号和BSheet表中统一编号相同则将D表的型号\使用单位设置为B表的型号\使用单位
                    if (dAssetCode.equalsIgnoreCase(bVo.getbAssetCode())) {
                        dObject.setdDeviceVersion(bVo.getbDeviceVersion());
                        dObject.setdBorrowDepart(bVo.getbBorrowDepart());
                        dObject.setdCountry(bVo.getbCountry());
                    }
                }
                dList.add(dObject);

                //遍历D 看型号是否存在
                for (DObject dObj : dList) {
                    //遍历C表 如果D表中型号和C表型号相同则将C表的设备名称设置为D表的设备名称
                    for (CloudObject cObj : cList) {
                        if (cObj.getcDeviceVersion().equalsIgnoreCase(dObj.getdDeviceVersion())) {
                            dObj.setdAssetName(cObj.getcAssetName());
                        }
                    }
                }
            }

            for (DObject dd : dList) {
                for (BObject bb : bList) {
                    if (Strings.isBlank(dd.getdAssetName())) {
                        if (dd.getdDeviceVersion().equalsIgnoreCase(bb.getbDeviceVersion())) {
                            dd.setdAssetName("++" + bb.getbAssetName() + "（" + bb.getbDeviceVersion() + "）");
                        }
                    }
                }
            }
            System.out.println(dList);
            String path = "E:/";
            String fileName = "差异2018-10-29";
            String fileType = "xlsx";
            String title[] = {"设备型号", "设备编号", "设备别名", "设备描述", "国别厂家","*省份", "*市", "*区县", "地址", "设备组"};
            writer(path, fileName, fileType, dList, title);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void writer(String path, String fileName, String fileType, List<DObject> list, String titleRow[]) throws IOException {
        Workbook wb = null;
        String excelPath = path + File.separator + fileName + "." + fileType;
        File file = new File(excelPath);
        Sheet sheet = null;
        //创建工作文档对象
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            }
            //创建sheet对象
            sheet = (Sheet) wb.createSheet("difference");
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } else {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();

            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook();
            }
        }
        //创建sheet对象
        if (sheet == null) {
            sheet = (Sheet) wb.createSheet("sheet1");
        }

        //添加表头
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        row.setHeight((short) 540);
        cell.setCellValue("差异");    //创建第一行

        CellStyle style = wb.createCellStyle(); // 样式对象
        // 设置单元格的背景颜色为淡蓝色
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

        cell.setCellStyle(style); // 样式，居中

        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 280);
        style.setFont(font);
        // 单元格合并
        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.autoSizeColumn(5200);

        row = sheet.createRow(1);    //创建第二行
        for (int i = 0; i < titleRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleRow[i]);
            cell.setCellStyle(style); // 样式，居中
            sheet.setColumnWidth(i, 20 * 256);
        }
        row.setHeight((short) 540);

        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            row = (Row) sheet.createRow(i + 2);
            row.setHeight((short) 500);
            row.createCell(0).setCellValue((list.get(i)).getdAssetName());
            row.createCell(1).setCellValue((list.get(i)).getdAssetCode());
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue((list.get(i)).getdCountry());
            row.createCell(5).setCellValue("北京市");
            row.createCell(6).setCellValue("北京市辖区");
            row.createCell(7).setCellValue("海淀区");
            row.createCell(8).setCellValue("");
            row.createCell(9).setCellValue((list.get(i)).getdBorrowDepart());
        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();


    }
}