package com.znzz.app.web.commons.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.lang.Strings;
import com.znzz.app.asset.moudules.models.Ins_Asset_SamplingCheck;

public class SamplingUtil {

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";

	public static final String NOT_EXCEL_FILE = " : 没有此excel文件!";
	public static final String PROCESSING = "Processing...";

	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = SamplingUtil.getPostfix(path);
			if (!SamplingUtil.EMPTY.equals(postfix)) {
				// xls格式的
				if (SamplingUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (SamplingUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + SamplingUtil.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

		// 获取文件后缀
		public static String getPostfix(String path) {
			if (path == null || "".equals(path.trim())) {
				return SamplingUtil.EMPTY;
			}
			if (path.contains(SamplingUtil.POINT)) {
				return path.substring(path.lastIndexOf(SamplingUtil.POINT) + 1, path.length());
			}
			return SamplingUtil.EMPTY;
		}

		// 获取单元格里的值
		@SuppressWarnings("unused")
		private String getValue(XSSFCell xssfRow) {
			if(xssfRow==null){
				return "";
			}
			if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
				return String.valueOf(xssfRow.getBooleanCellValue());
			} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(xssfRow)) {
					//单元格内是时间
                    Date theDate = xssfRow.getDateCellValue();  
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");  
                    return dff.format(theDate);  
                }else{
                	return NumberToTextConverter.toText(xssfRow.getNumericCellValue());
                }
				
			} else {
				return String.valueOf(xssfRow.getStringCellValue());
			}
		}

		private String getValue(HSSFCell hssfCell) {
			if(hssfCell==null){
				return "";
			}
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(hssfCell)) {
					//单元格内是时间
                    Date theDate = hssfCell.getDateCellValue();  
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");  
                    return dff.format(theDate);  
                }else{
                	return NumberToTextConverter.toText(hssfCell.getNumericCellValue());
                }
			} else {
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}	
		
		
		// 2003-2007版本
		public Object readXls(String path) throws Exception {
			List<Ins_Asset_SamplingCheck> list = new ArrayList<Ins_Asset_SamplingCheck>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Ins_Asset_SamplingCheck check = null;
				// Read the Sheet
				for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
					HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
					if (hssfSheet == null) {
						continue;
					}
					// Read the Row
					for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
						HSSFRow hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow != null) {
							check = new Ins_Asset_SamplingCheck();
							// 用来存放错误信息的map
							Map<String, String> map = new HashMap<>();
							HSSFCell assetCode = hssfRow.getCell(0);	//统一编号
							HSSFCell assetName = hssfRow.getCell(1);	//名称
							HSSFCell assetType = hssfRow.getCell(2);	//型号
							HSSFCell serialNumber= hssfRow.getCell(3);  //出厂编号
							HSSFCell checkDepartment = hssfRow.getCell(4);	//检定部门
							HSSFCell useDepartment = hssfRow.getCell(5);  //使用部门
							HSSFCell userMan = hssfRow.getCell(6);  //使用人
							HSSFCell samplingDate = hssfRow.getCell(7);  //抽检日期
							HSSFCell respMan = hssfRow.getCell(8);  //责任人
							HSSFCell sentCheckDate = hssfRow.getCell(9);	//送检日期
							HSSFCell checkDate = hssfRow.getCell(10);	//检定日期
							HSSFCell testResult = hssfRow.getCell(11);	//检定结果
							HSSFCell getDate = hssfRow.getCell(12);	//领取日期
							HSSFCell filDate = hssfRow.getCell(13);	//归档日期
							HSSFCell remark = hssfRow.getCell(14);	//备注
							
							if(assetCode!=null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号不能为空");
								return map;
							}
							if(samplingDate==null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中抽检日期不能为空");
								return map;
							}
							
							check.setAssetCode(getValue(assetCode));
							check.setAssetName(getValue(assetName));;
							check.setAssetModel(getValue(assetType));
							check.setSerialNumber(getValue(serialNumber));
							check.setUseDepartment(getValue(useDepartment));
							check.setTestDepartment(getValue(checkDepartment));
							check.setUserMan(getValue(userMan));
							SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd");
							check.setSamplingDate(sdf.parse(getValue(samplingDate)));
							check.setRespMan(getValue(respMan));
							check.setSentCheckDate(sdf.parse(getValue(sentCheckDate)));
							check.setCheckDate(sdf.parse(getValue(checkDate)));
							check.setTestResult(getValue(testResult));
							check.setGetDate(sdf.parse(getValue(getDate)));
							check.setRemark(getValue(remark));
							check.setFilDate(sdf.parse(getValue(filDate)));
							
							list.add(check);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is.close();
			}
			return list;
		}		
			

		// 2007以上版本
		public Object readXlsx(String path) throws Exception  {
			List<Ins_Asset_SamplingCheck> list = new ArrayList<Ins_Asset_SamplingCheck>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
				Ins_Asset_SamplingCheck check = null;
				// Read the Sheet
				for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
					XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
					if (xssfSheet == null) {
						continue;
					}
					// Read the Row
					for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
						XSSFRow xssfRow = xssfSheet.getRow(rowNum);
						if (xssfRow != null && xssfRow.getCell(0) != null) {
							check = new Ins_Asset_SamplingCheck();
							Map<String, String> map = new HashMap<>();
							XSSFCell assetCode = xssfRow.getCell(0);	//统一编号
							XSSFCell assetName = xssfRow.getCell(1);	//名称
							XSSFCell assetType = xssfRow.getCell(2);	//型号
							XSSFCell serialNumber= xssfRow.getCell(3);  //出厂编号
							XSSFCell checkDepartment = xssfRow.getCell(4);	//检定部门
							XSSFCell useDepartment = xssfRow.getCell(5);  //使用部门
							XSSFCell userMan = xssfRow.getCell(6);  //使用人
							XSSFCell samplingDate = xssfRow.getCell(7);  //抽检日期
							XSSFCell respMan = xssfRow.getCell(8);  //责任人
							XSSFCell sentCheckDate = xssfRow.getCell(9);	//送检日期
							XSSFCell checkDate = xssfRow.getCell(10);	//检定日期
							XSSFCell testResult = xssfRow.getCell(11);	//检定结果
							XSSFCell getDate = xssfRow.getCell(12);	//领取日期
							XSSFCell remark = xssfRow.getCell(13);	//备注
							XSSFCell filDate = xssfRow.getCell(14);	//归档日期
							
							if(assetCode==null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号不能为空");
								return map;
							}
							if(samplingDate==null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中抽检日期不能为空");
								return map;
							}
							
							check.setAssetCode(getValue(assetCode));
							check.setAssetName(getValue(assetName));;
							check.setAssetModel(getValue(assetType));
							check.setSerialNumber(getValue(serialNumber));
							check.setUseDepartment(getValue(useDepartment));
							check.setTestDepartment(getValue(checkDepartment));
							check.setUserMan(getValue(userMan));
							SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd");
							check.setSamplingDate(sdf.parse(getValue(samplingDate)));
							check.setRespMan(getValue(respMan));
							String v1 = getValue(sentCheckDate);
							if(!Strings.isBlank(v1)){
								check.setSentCheckDate(sdf.parse(v1));
							}
							String v2 = getValue(checkDate);
							if(!Strings.isBlank(v2)){
								check.setCheckDate(sdf.parse(v2));
							}
							check.setTestResult(getValue(testResult));
							String v3 = getValue(getDate);
							if(!Strings.isBlank(v3)){
								check.setGetDate(sdf.parse(v3));
							}
							check.setRemark(getValue(remark));
							String v4 = getValue(filDate);
							if(!Strings.isBlank(v4)){
								check.setFilDate(sdf.parse(v4));
							}
							
							list.add(check);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is.close();
			}
			return list;
		}
		
}
