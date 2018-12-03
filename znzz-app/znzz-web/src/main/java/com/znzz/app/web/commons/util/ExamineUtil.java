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
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.Mvcs;

import com.znzz.app.asset.moudules.models.Ins_Assets;
import com.znzz.app.instrument.modules.models.Ins_Collect;
import com.znzz.app.sys.modules.models.Sys_employee;

public class ExamineUtil {
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
			String postfix = ExamineUtil.getPostfix(path);
			if (!ExamineUtil.EMPTY.equals(postfix)) {
				// xls格式的
				if (ExamineUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (ExamineUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + ExamineUtil.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

		// 获取文件后缀
		public static String getPostfix(String path) {
			if (path == null || "".equals(path.trim())) {
				return ExamineUtil.EMPTY;
			}
			if (path.contains(ExamineUtil.POINT)) {
				return path.substring(path.lastIndexOf(ExamineUtil.POINT) + 1, path.length());
			}
			return ExamineUtil.EMPTY;
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
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");  
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
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");  
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
			List<Ins_Assets> list = new ArrayList<Ins_Assets>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Ins_Assets employee = null;
				String val = null;
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
							employee = new Ins_Assets();
							// 用来存放错误信息的map
							Map<String, String> map = new HashMap<>();
							HSSFCell assetCode = hssfRow.getCell(0);	//统一编号
							//HSSFCell assetName = hssfRow.getCell(1);	//名称(资产名称通过统一编号获取的，这里就不导入了)
							HSSFCell deviceVersion = hssfRow.getCell(2);	//型号
							HSSFCell serialNumber= hssfRow.getCell(3);  //出厂编号
							HSSFCell examineDate = hssfRow.getCell(4);	//检定日期
							HSSFCell dueDate = hssfRow.getCell(5);  //到期检定日期
							
							if(assetCode!=null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号不能为空");
								return map;
							}
							
							employee.setAssetCode(getValue(assetCode));
							employee.setDeviceVersion(getValue(deviceVersion));
							employee.setSerialNumber(getValue(serialNumber));
							SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy/MM/dd");
							employee.setExamineDate(sdf.parse(getValue(examineDate)));
							employee.setDueDate(sdf.parse(getValue(dueDate)));
				
							
							// excel中统一编号不允许重复
							if (exitsEmployeeCode(list,employee.getAssetCode())) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号重复，重复的统一编号是：" + employee.getAssetCode());
								return map;
							}
							
							if (!duplicateAssetCode(employee.getAssetCode())){
								// 资产表中 统一编号不存在
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,资产信息表中不存在该统一编号，不存在的编号是：" + employee.getAssetCode());
								return map;
							}else{
								//资产表统一编号存在，判断资产种类是否为仪器类型
								if(!judgeAssetCode(employee.getAssetCode())){
									is.close();
									map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,该资产种类不是仪器类型，该资产对应编号是：" + employee.getAssetCode());
									return map;
								}
							}
							
							
							
							
							list.add(employee);
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
			List<Ins_Assets> list = new ArrayList<Ins_Assets>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
				Ins_Assets employee = null;
				// Read the Sheet
				for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
					XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
					if (xssfSheet == null) {
						continue;
					}
					// Read the Row
					for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
						XSSFRow xssfRow = xssfSheet.getRow(rowNum);
						String val = "";
						if (xssfRow != null && xssfRow.getCell(0) != null) {
							employee = new Ins_Assets();
							// 用来存放错误信息的map
							Map<String, String> map = new HashMap<>();
							XSSFCell assetCode = xssfRow.getCell(0);	//统一编号
							//HSSFCell assetName = hssfRow.getCell(1);	//名称(资产名称通过统一编号获取的，这里就不导入了)
							XSSFCell deviceVersion = xssfRow.getCell(2);	//型号
							XSSFCell serialNumber= xssfRow.getCell(3);  //出厂编号
							XSSFCell examineDate = xssfRow.getCell(4);	//检定日期
							XSSFCell dueDate = xssfRow.getCell(5);  //到期检定日期
							
							if(assetCode==null){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号不能为空");
								return map;
							}
							
							employee.setAssetCode(getValue(assetCode));
							employee.setDeviceVersion(getValue(deviceVersion));
							employee.setSerialNumber(getValue(serialNumber));
							SimpleDateFormat sdf  =   new  SimpleDateFormat("yyyy-MM-dd");
							employee.setExamineDate(sdf.parse(getValue(examineDate)));
							employee.setDueDate(sdf.parse(getValue(dueDate)));
						
							
							// excel中统一编号不允许重复
							if (exitsEmployeeCode(list,employee.getAssetCode())) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中统一编号重复，重复的统一编号是：" + employee.getAssetCode());
								return map;
							}

							if (!duplicateAssetCode(employee.getAssetCode())){
								// 资产表中 统一编号不存在
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,资产信息表中不存在该统一编号，不存在的编号是：" + employee.getAssetCode());
								return map;
							}else{
								//资产表统一编号存在，判断资产种类是否为仪器类型
								if(!judgeAssetCode(employee.getAssetCode())){
									is.close();
									map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,该资产种类不是仪器类型，该资产对应编号是：" + employee.getAssetCode());
									return map;
								}
							}
							
							list.add(employee);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return list;
		}
		
		
		private boolean exitsEmployeeCode(List<Ins_Assets> list, String assetcode) {
			for (Ins_Assets domain : list) {
				if( domain.getAssetCode().equals(assetcode) ){
					return true;
				}
			}
			return false;
			
		}	
		
		private boolean duplicateAssetCode(String assetCode) {
			Ioc ioc = Mvcs.ctx().getDefaultIoc();
			Dao dao = ioc.get(Dao.class);
			List<Ins_Assets> query = dao.query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
			if (query.size() == 0) {
				return false;
			}
			return true;
		}
		
		private boolean judgeAssetCode(String assetCode) {
			Ioc ioc = Mvcs.ctx().getDefaultIoc();
			Dao dao = ioc.get(Dao.class);
			List<Ins_Assets> query = dao.query(Ins_Assets.class, Cnd.where("assetCode", "=", assetCode));
			if (query.get(0).getAssetType()!=2) {
				return false;
			}
			return true;
		}
}
