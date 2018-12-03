package com.znzz.app.web.commons.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.Ioc;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import com.ctc.wstx.util.StringUtil;
import com.znzz.app.sys.modules.models.Sys_unit;


public class ReadExcel_Unit {
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
	 * 	采集器绑定设备的list
	 *  ExcelToList(将excel表格中的数据转换成List<T>)
	 * @throws Exception
	 */
	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = ReadExcel_Unit.getPostfix(path);
			if (!ReadExcel_Unit.EMPTY.equals(postfix)) {
				// xls格式的
				if (ReadExcel_Unit.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (ReadExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + ReadExcel_Unit.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 获取文件后缀
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return ReadExcel_Unit.EMPTY;
		}
		if (path.contains(ReadExcel_Unit.POINT)) {
			return path.substring(path.lastIndexOf(ReadExcel_Unit.POINT) + 1, path.length());
		}
		return ReadExcel_Unit.EMPTY;
	}
	// 2003-2007版本
		public Object readXls(String path) throws Exception {
			System.out.println(ReadExcelUtil.PROCESSING + path);
			List<Sys_unit> list = new ArrayList<Sys_unit>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Sys_unit unit = null;
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
							unit = new Sys_unit();
							Map<String, String> map = new HashMap<>();
							HSSFCell xdepartmentName = hssfRow.getCell(0);
							HSSFCell xdepartmentCode = hssfRow.getCell(1);
							HSSFCell xparentDepartmentCode = hssfRow.getCell(2);
							HSSFCell xtelephone = hssfRow.getCell(3);
							HSSFCell xaddress = hssfRow.getCell(4);
							HSSFCell xemail = hssfRow.getCell(5);
						

							if(null==xparentDepartmentCode){
								unit.setParentId(null);
							
							}else{
								String parentDepartmentCode = getValue(xparentDepartmentCode);
								unit.setParentId(parentDepartmentCode);
							}
			                if(null==xaddress){
			                	
			                	unit.setAddress(null);
			                }else{
			                	String address = getValue(xaddress);
			                	unit.setAddress(address);
			                }
			                if(null==xtelephone){
			                	unit.setTelephone(null);
			                }else{
			                	String telephone = getValue(xtelephone);
			                	unit.setTelephone(telephone);
			                }
			                if(null==xemail){
			                	unit.setEmail(null);
			                }else{
			                	String email = getValue(xemail);
			                	unit.setEmail(email);
			                }
							
							
							String departmentName = getValue(xdepartmentName);
							String departmentCode = getValue(xdepartmentCode);
							//String parentDepartmentCode = getValue(xparentDepartmentCode);
							
							
							
							
						
							
							unit.setId(departmentCode);
							unit.setUnitcode(departmentCode);
							unit.setName(departmentName);
							
							
							
						
				
							// 用来存放错误信息的map
							
							// 对单元格里的数据进行校验
							// 1.Excel表中采集器编号不允许重复
							if (exitsunitCode(list,departmentCode)) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + unitCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中部门编号重复，重复的部门编号是：" + departmentCode);
								return map;
							}
							// 3.校验数据库中采集器编号是否存在
							if(duplicateCheckunitCode(departmentCode)){
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,数据库采集器表中已经存在相同编号，重复的采集器编号是：" + deviceCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库部门表中已经存在相同编号，重复的部门编号是：" + departmentCode);
								return map;
							}
							//
					
							
							
							list.add(unit);
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
		System.out.println(ReadExcel_Unit.PROCESSING + path);
		List<Sys_unit> list = new ArrayList<Sys_unit>();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			Sys_unit unit = null;
			// Read the Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					String val = "";
					if (xssfRow != null) {
						unit = new Sys_unit();
						// 用来存放错误信息的map
						Map<String, String> map = new HashMap<>();
						XSSFCell xdepartmentName = xssfRow.getCell(0);
						XSSFCell xdepartmentCode = xssfRow.getCell(1);
						XSSFCell xparentDepartmentCode = xssfRow.getCell(2);
						XSSFCell xtelephone = xssfRow.getCell(3);
						XSSFCell xaddress = xssfRow.getCell(4);
						XSSFCell xemail = xssfRow.getCell(5);
					
						if (null==xdepartmentName) {
							is.close();
							//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + unitCode);
							map.put("error", "excel校验出错:第" + (rowNum + 1) + "行,Excel中部门名称为空");
							return map;
						}
						if (null==xdepartmentCode) {
							is.close();
							//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + unitCode);
							map.put("error", "excel校验出错:第" + (rowNum + 1) + "行,Excel中部门编号为空");
							return map;
						}
						
						
						
						if(null==xparentDepartmentCode){
							unit.setParentId(null);
						
						}else{
							String parentDepartmentCode = getValue(xparentDepartmentCode);
							unit.setParentId(parentDepartmentCode);
						}
		                if(null==xaddress){
		                	
		                	unit.setAddress(null);
		                }else{
		                	String address = getValue(xaddress);
		                	unit.setAddress(address);
		                }
		                if(null==xtelephone){
		                	unit.setTelephone(null);
		                }else{
		                	String telephone = getValue(xtelephone);
		                	unit.setTelephone(telephone);
		                }
		                if(null==xemail){
		                	unit.setEmail(null);
		                }else{
		                	String email = getValue(xemail);
		                	unit.setEmail(email);
		                }
						
						
						String departmentName = getValue(xdepartmentName);
						String departmentCode = getValue(xdepartmentCode);
						//String parentDepartmentCode = getValue(xparentDepartmentCode);
						
						
						
						
					
						
						unit.setId(departmentCode);
						unit.setUnitcode(departmentCode);
						unit.setName(departmentName);
		
						
					
						
					
						// 对单元格里的数据进行校验
						// 1.Excel表中采集器编号不允许重复
						if (exitsunitCode(list,departmentCode)) {
							is.close();
							//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + unitCode);
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中部门编号重复，重复的部门器编号是：" + departmentCode);
							return map;
						}
						// 3.校验数据库中采集器编号是否存在
						if(duplicateCheckunitCode(departmentCode)){
							is.close();
							//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,数据库采集器表中已经存在相同编号，重复的采集器编号是：" + deviceCode);
							//map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库采集器表中已经存在相同编号，重复的采集器编号是：" + unitCode);
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库部门表中已经存在相同编号，重复的部门编号是：" + departmentCode);
							return map;
						}
						//
						
						
					
						list.add(unit);
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

	


	// 获取单元格里的值
	@SuppressWarnings("unused")
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else if(null==xssfRow){
			
			return "";
			
		}else{
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	/**
	 * Excel表格中校验采集器编号是否重复
	 * @param list
	 * @param unitCode
	 * @return
	 */
	private boolean exitsunitCode(List<Sys_unit> list, String unitCode) {
		for (Sys_unit domain : list) {
			if( domain.getUnitcode().equals(unitCode) ){
				return true;
			}
		}
		return false;
	}
/*	
	*//**
	 * Excel表格中校验统一编号是否重复
	 * @param list
	 * @param deviceCode
	 * @return
	 *//*
	private boolean exitsDeviceCode(List<Sys_unit> list, String deviceCode) {
		for (Sys_unit domain : list) {
			if( domain.getunitCode().equals(deviceCode)){
				return true;
			}
		}
		return false;
	}*/
	
	/**
	 * 校验采集器表中是否有重复
	 * @param unitCode
	 * @return
	 */
    private boolean duplicateCheckunitCode(String unitCode) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Sys_unit> query = dao.query(Sys_unit.class, Cnd.where("unitcode", "=", unitCode));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}
	

}
