package com.znzz.app.web.commons.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.znzz.app.instrument.modules.models.Ins_Gateway;
public class ReadExcel_Acard {
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
	 * 	网关绑定设备的list
	 *  ExcelToList(将excel表格中的数据转换成List<T>)
	 * @throws Exception
	 */
	public Object readExcel(String path) throws Exception {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = ReadExcel_Acard.getPostfix(path);
			if (!ReadExcel_Acard.EMPTY.equals(postfix)) {
				// xls格式的
				if (ReadExcel_Acard.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (ReadExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + ReadExcel_Acard.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 获取文件后缀
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return ReadExcel_Acard.EMPTY;
		}
		if (path.contains(ReadExcel_Acard.POINT)) {
			return path.substring(path.lastIndexOf(ReadExcel_Acard.POINT) + 1, path.length());
		}
		return ReadExcel_Acard.EMPTY;
	}
	// 2003-2007版本
		public Object readXls(String path) throws Exception {
			System.out.println(ReadExcelUtil.PROCESSING + path);
			List<Ins_Gateway> list = new ArrayList<Ins_Gateway>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Ins_Gateway gateway = null;
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

						// 用来存放错误信息的map
						Map<String, String> map = new HashMap<>();
						if (hssfRow != null) {
							gateway = new Ins_Gateway();
							HSSFCell hAcardCode = hssfRow.getCell(0);
							HSSFCell hAcardName = hssfRow.getCell(1);
							HSSFCell hLoaction = hssfRow.getCell(2);
							HSSFCell hIp = hssfRow.getCell(3);
							if (null==hAcardCode) {
								is.close();
								map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号为空" + "</br>");
								return map;
							}
							if (null==hAcardName) {
								is.close();
								map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关名称为空" + "</br>");
								return map;
							}
							if (null==hLoaction) {
								is.close();
								map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关位置为空" + "</br>");
								return map;
							}
							if (null==hIp) {
								is.close();
								map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中IP为空" + "</br>");
								return map;
							}
							
							/*
							 * student.setNo(getValue(no));
							 * student.setName(getValue(name));
							 * student.setAge(getValue(age));
							 * student.setScore(Float.valueOf(getValue(score)));
							 */
							String acardCode = getValue(hAcardCode);
							String acardName = getValue(hAcardName);
							String Loaction = getValue(hLoaction);
							String ip = getValue(hIp);
						
							/*//判断ip格式校验
							if(Strings.isNotBlank(ip) && !ip.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")){
								map.put("error", "excel数据校验出错:第" + (rowNum + 1) + "行,ip格式错误 请检查！" + "<br/>");
							}*/
							gateway.setGatewayCode(acardCode);
							gateway.setGatewayLocation(Loaction);
							gateway.setIp(ip);
							gateway.setGatewayName(acardName);
				
							// 对单元格里的数据进行校验
							// 1.Excel表网关编号不允许重复
							if (exitsgatewayCode(list,acardCode)) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号重复，重复的网关编号是：" + gatewayCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号重复，重复的网关编号是：" + acardCode);
								return map;
							}
							// 2.校验数据库中网关编号是否存在
						/*	if(duplicateCheckgatewayCode(acardCode)){
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,数据库网关表中已经存在相同编号，重复的网关编号是：" + deviceCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库收集器表中已经存在相同IP，重复的网关编号是：" + acardCode);
								return map;
							}*/
							// 1.Excel表中IP不允许重复
							if (exitsIp(list,ip)) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号重复，重复的网关编号是：" + gatewayCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中采IP重复，重复的IP是：" + ip);
								return map;
							}
							// 2.校验数据库中IP是否存在
						/*	if(duplicateCheckIp(ip)){
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,数据库网关表中已经存在相同编号，重复的网关编号是：" + deviceCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库收集器表中已经存在相同IP，重复的IP是：" + ip);
								return map;
							}*/
							//
							
							list.add(gateway);
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
		System.out.println(ReadExcel_Acard.PROCESSING + path);
		List<Ins_Gateway> list = new ArrayList<Ins_Gateway>();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			Ins_Gateway gateway = null;
			// Read the Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					
					if (xssfRow != null) {
						Map<String, String> map = new HashMap<>();
						gateway = new Ins_Gateway();
						XSSFCell xAcardCode = xssfRow.getCell(0);  //网关编号
						XSSFCell xAcardName = xssfRow.getCell(1);  //网关名称
						XSSFCell xLoaction = xssfRow.getCell(2);   //位置
						XSSFCell xIp = xssfRow.getCell(3);		   //ip
					
						if (null==xAcardCode||"".equals(xAcardCode)||xAcardCode.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号为空"+ "<br/>");
							return map;
						}
						if (null==xAcardName||"".equals(xAcardName)||xAcardName.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关名称为空"+ "<br/>");
							return map;
						}
						if (null==xLoaction||"".equals(xLoaction)||xLoaction.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中网关位置为空"+ "<br/>");
							return map;
						}
						if (null==xIp||"".equals(xIp)||xIp.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,Excel中IP为空"+ "<br/>");
							return map;
						}
						
						String acardCode = getValue(xAcardCode);
						String acardName = getValue(xAcardName);
						String loaction = getValue(xLoaction);
						String ip = getValue(xIp);
						
						//判断ip格式校验
						if(Strings.isNotBlank(ip) && !ip.matches("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")){
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,ip格式错误 请检查！" + "<br/>");
							return map;
						}
						
						gateway.setGatewayCode(acardCode);
						gateway.setGatewayLocation(loaction);
						gateway.setIp(ip);
						gateway.setGatewayName(acardName);
					
						// 用来存放错误信息的map
						
						// 对单元格里的数据进行校验
						// 1.Excel表中网关编号不允许重复
						if (exitsgatewayCode(list,acardCode)) {
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中网关编号重复，重复的网关编号是：" + acardCode+ "<br/>");
							return map;
						}
						// 3.校验数据库中网关编号是否存在
						if(duplicateCheckgatewayCode(acardCode)){
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,系统中已存在此网关编号，网关编号是：" + acardCode+ "<br/>");
							return map;
						}
						// 1.Excel表中IP不允许重复
						if (exitsIp(list,ip)) {
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中IP重复，重复的IP是：" + ip + "<br/>");
							return map;
						}
						// 2.校验数据库中IP是否存在
						if(duplicateCheckIp(ip)){
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,系统中已经存在此IP，IP是：" + ip + "<br/>");
							return map;
						}
						list.add(gateway);
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
		} else {
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
	 * Excel表格中校验网关器编号是否重复
	 * @param list
	 * @param gatewayCode
	 * @return
	 */
	private boolean exitsgatewayCode(List<Ins_Gateway> list, String gatewayCode) {
		for (Ins_Gateway domain : list) {
			if( domain.getGatewayCode().equals(gatewayCode) ){
				return true;
			}
		}
		return false;
	}
	/**
	 * Excel表格中校验ip是否重复
	 * @param list
	 * @param gatewayCode
	 * @return
	 */
	private boolean exitsIp(List<Ins_Gateway> list, String ip) {
		for (Ins_Gateway domain : list) {
			if( domain.getIp().equals(ip) ){
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 校验网关表中是否有重复
	 * @param gatewayCode
	 * @return
	 */
    private boolean duplicateCheckgatewayCode(String acardCode) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Dao dao = ioc.get(Dao.class);
		List<Ins_Gateway> query = dao.query(Ins_Gateway.class, Cnd.where("gatewayCode", "=", acardCode));
		if (query.size() == 0) {
			return false;
		}
		return true;
	}
	
    
    /**
     * 校验网关表中ip是否有重复
     * @param gatewayCode
     * @return
     */
    private boolean duplicateCheckIp(String ip) {
    	Ioc ioc = Mvcs.ctx().getDefaultIoc();
    	Dao dao = ioc.get(Dao.class);
    	List<Ins_Gateway> query = dao.query(Ins_Gateway.class, Cnd.where("ip", "=", ip));
    	if (query.size() == 0) {
    		return false;
    	}
    	return true;
    }
    

}
