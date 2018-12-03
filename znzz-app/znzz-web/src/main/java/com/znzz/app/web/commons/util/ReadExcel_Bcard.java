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
import org.nutz.mvc.Mvcs;
import com.znzz.app.instrument.modules.models.Ins_DeviceBcard;

public class ReadExcel_Bcard {
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
			String postfix = ReadExcel_Bcard.getPostfix(path);
			if (!ReadExcel_Bcard.EMPTY.equals(postfix)) {
				// xls格式的
				if (ReadExcel_Bcard.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				}else if (ReadExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					// xlsx格式的
					return readXlsx(path);
				}
			} else {
				// 前端已经作了校验
				System.out.println(path + ReadExcel_Bcard.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	// 获取文件后缀
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return ReadExcel_Bcard.EMPTY;
		}
		if (path.contains(ReadExcel_Bcard.POINT)) {
			return path.substring(path.lastIndexOf(ReadExcel_Bcard.POINT) + 1, path.length());
		}
		return ReadExcel_Bcard.EMPTY;
	}
	// 2003-2007版本
		public Object readXls(String path) throws Exception {
			System.out.println(ReadExcelUtil.PROCESSING + path);
			List<Ins_DeviceBcard> list = new ArrayList<Ins_DeviceBcard>();
			InputStream is = null;
			try {
				is = new FileInputStream(path);
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
				Ins_DeviceBcard collect = null;
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
							collect = new Ins_DeviceBcard();
							HSSFCell HcollectCode = hssfRow.getCell(0);
							HSSFCell HcollectName = hssfRow.getCell(1);
							if (null==HcollectCode) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "excel验出错:第" + (rowNum + 1) + "行,Excel中采集器编号为空");
								return map;
							}
							if (null==HcollectName) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "excel验出错:第" + (rowNum + 1) + "行,Excel中生产编号为空");
								return map;
							}
						
							/*
							 * student.setNo(getValue(no));
							 * student.setName(getValue(name));
							 * student.setAge(getValue(age));
							 * student.setScore(Float.valueOf(getValue(score)));
							 */
							String bcardCode = getValue(HcollectCode);
							String orignCode = getValue(HcollectName);
							
							collect.setBcardCode(bcardCode);
							collect.setOrignCode(orignCode);
						
							// 对单元格里的数据进行校验
							// 1.Excel表中采集器编号不允许重复
							if (exitsCollectCode(list,bcardCode)) {
								is.close();
								//throw new Exception("数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + collectCode);
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + bcardCode);
								return map;
							}
							// 2.校验数据库中采集器编号是否存在
							if(duplicateCheckCollectCode(bcardCode)){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库采集器表中已经存在相同采集器编号，重复的采集器编号是：" + bcardCode);
								return map;
							}
							// 1.Excel表中生产编号不允许重复
							if (exitsOrignCode(list,orignCode)) {
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中采生产编号重复，重复的生产编号是：" + orignCode);
								return map;
							}
							// 2.校验数据库中生产编号是否存在
							if(duplicateCheckorignCode(orignCode)){
								is.close();
								map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,数据库采集器表中已经存在相同生产编号，重复的生产编号是：" + orignCode);
								return map;
							}
							
							list.add(collect);
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
		System.out.println(ReadExcel_Bcard.PROCESSING + path);
		List<Ins_DeviceBcard> list = new ArrayList<Ins_DeviceBcard>();
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			Ins_DeviceBcard collect = null;
			// Read the Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// Read the Row
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					Map<String, String> map = new HashMap<>();
					if (xssfRow != null && xssfRow.getCell(0) != null) {
						
						collect = new Ins_DeviceBcard();
						XSSFCell XcollectCode = xssfRow.getCell(0);
						XSSFCell XcollectName = xssfRow.getCell(1);
						if (null==XcollectCode||"".equals(XcollectCode)||XcollectCode.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,采集器编号为空" + "<br/>");
							return map;
						}
						if (null==XcollectName||"".equals(XcollectName)||XcollectName.getCellType()==XSSFCell.CELL_TYPE_BLANK) {
							is.close();
							map.put("error", "Excel数据校验出错:第" + (rowNum + 1) + "行,生产编号为空" + "<br/>");
							return map;
						}
						
						String bcardCode = getValue(XcollectCode);
//						DecimalFormat df = new DecimalFormat("0");  
//						String whatYourWant = df.format(XcollectName.getNumericCellValue());  
						String orignCode = getValue(XcollectName);
						
						collect.setBcardCode(bcardCode);
						collect.setOrignCode(orignCode);
						
					/*	if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(createTime)) {     
			                Date theDate = createTime.getDateCellValue();  
			                SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");  
			                val = dff.format(theDate);  
			            }else{  
			                DecimalFormat df = new DecimalFormat("0");    
			                val = df.format(createTime.getNumericCellValue());  
			            }  */
						
						//collect.setOperateTime(DateUtil.getDateFromString(val));
						
						// 用来存放错误信息的map
						
						// 对单元格里的数据进行校验
						// 1.Excel表中采集器编号不允许重复
						if (exitsCollectCode(list,bcardCode)) {
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中采集器编号重复，重复的采集器编号是：" + bcardCode + "<br/>");
							return map;
						}
						// 3.校验数据库中采集器编号是否存在
						if(duplicateCheckCollectCode(bcardCode)){
							is.close();
							map.put("error", "数据校验出错：第"+ (rowNum + 1) + "行,系统中已有此采集器编号,采集编号是:" + bcardCode + "<br/>");
							return map;
						}
						// 1.Excel表中生产编号不允许重复
						if (exitsOrignCode(list,orignCode)) {
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,Excel中生产编号重复，重复的生产编号是：" + orignCode + "<br/>");
							return map;
						}
						// 2.校验数据库中生产编号是否存在
						if(duplicateCheckorignCode(orignCode)){
							is.close();
							map.put("error", "数据校验出错:第" + (rowNum + 1) + "行,系统中已有此生产编号，生产编号是：" + orignCode + "<br/>");
							return map;
						}
						
						list.add(collect);
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
	 * Excel表格中校验采集器编号是否重复
	 * @param list
	 * @param collectCode
	 * @return
	 */
	private boolean exitsCollectCode(List<Ins_DeviceBcard> list, String collectCode) {
		for (Ins_DeviceBcard domain : list) {
			if( domain.getBcardCode().equals(collectCode) ){
				return true;
			}
		}
		return false;
	}
	/**
	 * Excel表格中校验生产编号是否重复
	 * @param list
	 * @param collectCode
	 * @return
	 */
	private boolean exitsOrignCode(List<Ins_DeviceBcard> list, String orignCode) {
		for (Ins_DeviceBcard domain : list) {
			if( domain.getOrignCode().equals(orignCode) ){
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
	private boolean exitsDeviceCode(List<Ins_DeviceBcard> list, String deviceCode) {
		for (Ins_DeviceBcard domain : list) {
			if( domain.getBcardCode().equals(deviceCode)){
				return true;
			}
		}
		return false;
	}*/
	
	/**
	 * 校验采集器表中是否有重复
	 * @param collectCode
	 * @return
	 */
    
    private boolean duplicateCheckCollectCode(String bcardCode) {
    	Ioc ioc = Mvcs.ctx().getDefaultIoc();
    	Dao dao = ioc.get(Dao.class);
    	List<Ins_DeviceBcard> query = dao.query(Ins_DeviceBcard.class, Cnd.where("bcardCode","=",bcardCode));
    	if (query.size() == 0) {
			return false;
		}
    	return true ;
    }
	
    /**
     * 校验采集器表中生产编号是否有重复
     * @param collectCode
     * @return
     */
    private boolean duplicateCheckorignCode(String orignCode) {
    	Ioc ioc = Mvcs.ctx().getDefaultIoc();
    	Dao dao = ioc.get(Dao.class);
    	List<Ins_DeviceBcard> query = dao.query(Ins_DeviceBcard.class, Cnd.where("orignCode", "=", orignCode));
    	if (query.size() == 0) {
    		return false;
    	}
    	return true;
    }
}
