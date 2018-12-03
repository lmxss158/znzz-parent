package com.znzz.app.web.commons.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

import com.znzz.app.sys.modules.models.Sys_user;

public class SysEmployeeUtil {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";

	/*
	 * public static final String LIB_PATH = "lib"; public static final String
	 * STUDENT_INFO_XLS_PATH = LIB_PATH + "/student_info" + POINT +
	 * OFFICE_EXCEL_2003_POSTFIX; public static final String
	 * STUDENT_INFO_XLSX_PATH = LIB_PATH + "/student_info" + POINT +
	 * OFFICE_EXCEL_2010_POSTFIX;
	 */
	public static final String NOT_EXCEL_FILE = " : 没有此excel文件!";
	public static final String PROCESSING = "Processing...";

	/**
	 *  ExcelToList(将excel表格中的数据转换成List<T>)
	 * @throws Exception
	 */
	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = SysEmployeeUtil.getPostfix(path);
			if (!SysEmployeeUtil.EMPTY.equals(postfix)) {
				// xls格式的
				if (SysEmployeeUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (SysEmployeeUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + SysEmployeeUtil.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 获取文件后缀
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return SysEmployeeUtil.EMPTY;
		}
		if (path.contains(SysEmployeeUtil.POINT)) {
			return path.substring(path.lastIndexOf(SysEmployeeUtil.POINT) + 1, path.length());
		}
		return SysEmployeeUtil.EMPTY;
	}
	// 2003-2007版本
		public Object readXls(String path) throws Exception {
			List<Sys_user> list = new ArrayList<Sys_user>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Sys_user employee = null;
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
							employee = new Sys_user();
							// 用来存放错误信息的map
							Map<String, String> map = new HashMap<>();
							HSSFCell unitName = hssfRow.getCell(0);	//部门名称
							HSSFCell employeeName = hssfRow.getCell(1);	//员工姓名
							HSSFCell idNumber = hssfRow.getCell(2);	//身份证号
							HSSFCell entryNumber = hssfRow.getCell(3);//出入证号
							HSSFCell tel = hssfRow.getCell(4);	//电话
							HSSFCell isService = hssfRow.getCell(5);  //职工详情
							
							if(unitName==null){
								is.close();
								map.put("error", "数据导入出错:第" + (rowNum + 1) + "行,部门名称不能为空") ;
								return map;
							}
							if(employeeName==null){
								is.close();
								map.put("error", "数据导入出错:第" + (rowNum + 1) + "行,员工姓名不能为空") ;
								return map;
							}
							
							employee.setUnitName(getValue(unitName));
							employee.setUnitName(getValue(employeeName));
							employee.setIdNumber(getValue(idNumber));
							employee.setEntryNumber(getValue(entryNumber));
							employee.setTelephone(getValue(tel));
							String statusName = getValue(isService);//职工状态名称
							employee.setIs_service(getempStatus(statusName));
							/*employee.setUnitid(getValue(unitCode));
							employee.setJobNumber(getValue(employeeCode));
							employee.setName(getValue(employeeName));
							employee.setTelephone(getValue(tel));
							if("是".equals(getValue(isService))){
								employee.setIs_service(0);
							}else{
								employee.setIs_service(1);
							}*/
				
							// 员工编号不允许重复
							if (exitsEmployeeCode(list,employee.getIdNumber())) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中身份证号重复，重复的员工身份证号是：" + employee.getIdNumber());
								return map;
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
		List<Sys_user> list = new ArrayList<Sys_user>();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			Sys_user employee = null;
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
						
						employee = new Sys_user();
						// 用来存放错误信息的map
						Map<String, String> map = new HashMap<>();
						XSSFCell unitName = xssfRow.getCell(0);	//部门名称
						XSSFCell employeeName = xssfRow.getCell(1);	//员工姓名
						XSSFCell idNumber = xssfRow.getCell(2);	//身份证号
						XSSFCell entryNumber = xssfRow.getCell(3);//出入证号
						XSSFCell tel = xssfRow.getCell(4);	//电话
						XSSFCell isService = xssfRow.getCell(5);  //是否在职
						
						if(unitName==null){
							is.close();
							map.put("error", "数据导入出错:第" + (rowNum + 1) + "行,部门名称不能为空") ;
							return map;
						}
						if(employeeName==null){
							is.close();
							map.put("error", "数据导入出错:第" + (rowNum + 1) + "行,员工姓名不能为空") ;
							return map;
						}
						
						employee.setUnitName(getValue(unitName));
						employee.setUsername(getValue(employeeName));
						employee.setIdNumber(getValue(idNumber));
						employee.setEntryNumber(getValue(entryNumber));
						employee.setTelephone(getValue(tel));
						String statusName = getValue(isService);//职工状态名称
						employee.setIs_service(getempStatus(statusName));
						/*employee = new Sys_employee();
						XSSFCell unitCode = xssfRow.getCell(0);	//所属部门编码
						XSSFCell employeeCode = xssfRow.getCell(1);	//员工编号
						XSSFCell employeeName = xssfRow.getCell(2);	//员工姓名
						XSSFCell tel = xssfRow.getCell(3);	//电话
						XSSFCell isService = xssfRow.getCell(4);  //是否在职
						
						employee.setUnitid(getValue(unitCode));
						employee.setJobNumber(getValue(employeeCode));
						employee.setName(getValue(employeeName));
						employee.setTelephone(getValue(tel));
						if("是".equals(getValue(isService))){
							employee.setIs_service(0);
						}else{
							employee.setIs_service(1);
						}
						*/
						
						// 员工编号不允许重复
						if (exitsEmployeeCode(list,employee.getIdNumber())) {
							is.close();
							//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中员工身份证号重复，重复的身份证号是：" + employee.getIdNumber());
							return map;
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

	


	private Integer getempStatus(String name) {
		String statusName = name.replaceAll("\\s*", "");	//替换职工详情中的制表符、空白、换行符
		if("出站".equals(statusName)){
			return 1;
		}else if("辞职".equals(statusName)){
			return 2;
		}else if("返聘停聘".equals(statusName)){
			return 3;
		}else if("挂职".equals(statusName)){
			return 4;
		}else if("借调".equals(statusName)){
			return 5;
		}else if("进站工作".equals(statusName)){
			return 6;
		}else if("开始工作".equals(statusName)){
			return 7;
		}else if("开始工作借调".equals(statusName)){
			return 8;
		}else if("开始实习".equals(statusName)){
			return 9;
		}else if("死亡".equals(statusName)){
			return 10;
		}else if("调出".equals(statusName)){
			return 11;
		}else if("调入".equals(statusName)){
			return 12;
		}else if("停聘".equals(statusName)){
			return 13;
		}else if("停止挂职".equals(statusName)){
			return 14;
		}else if("退休".equals(statusName)){
			return 15;
		}else if("终止实习".equals(statusName)){
			return 16;
		}else if("转入".equals(statusName)){
			return 17;
		}else{
			return null;
		}
	}

	// 获取单元格里的值
	@SuppressWarnings("unused")
	private String getValue(XSSFCell xssfRow) {
		 
		 String cellValue = "";   
		 if(null==xssfRow){
			 return cellValue;
		 }
	     DecimalFormat df = new DecimalFormat("#");   
	     switch (xssfRow.getCellType()) {   
	     case HSSFCell.CELL_TYPE_STRING:   
	           cellValue =xssfRow.getRichStringCellValue().getString().trim();   
	           break;   
	     case HSSFCell.CELL_TYPE_NUMERIC:   
	            cellValue =df.format(xssfRow.getNumericCellValue()).toString();   
	            break;   
	     case HSSFCell.CELL_TYPE_BOOLEAN:   
	            cellValue =String.valueOf(xssfRow.getBooleanCellValue()).trim();   
	            break;   
	     case HSSFCell.CELL_TYPE_FORMULA:   
	            cellValue =xssfRow.getCellFormula();   
	            break;   
	     default:   
	            cellValue = "";   
	        }   
	     return cellValue; 
	}

	private String getValue(HSSFCell hssfCell) {
		 String cellValue = "";   
		 if(null==hssfCell){
			 return cellValue;
		 }
	     DecimalFormat df = new DecimalFormat("#");   
	     switch (hssfCell.getCellType()) {   
	     case HSSFCell.CELL_TYPE_STRING:   
	           cellValue =hssfCell.getRichStringCellValue().getString().trim();   
	           break;   
	     case HSSFCell.CELL_TYPE_NUMERIC:   
	            cellValue =df.format(hssfCell.getNumericCellValue()).toString();   
	            break;   
	     case HSSFCell.CELL_TYPE_BOOLEAN:   
	            cellValue =String.valueOf(hssfCell.getBooleanCellValue()).trim();   
	            break;   
	     case HSSFCell.CELL_TYPE_FORMULA:   
	            cellValue =hssfCell.getCellFormula();   
	            break;   
	     default:   
	            cellValue = "";   
	        }   
	     return cellValue; 
	}
	
	/**
	 * @param list
	 * @param idNumber
	 * @return
	 */
	private boolean exitsEmployeeCode(List<Sys_user> list, String idNumber) {
		for (Sys_user domain : list) {
			if(idNumber!=null && idNumber!="" && domain.getIdNumber().equals(idNumber) ){
				return true;
			}
		}
		return false;
		
	}
	
}
