package com.znzz.app.web.commons.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ctc.wstx.util.DataUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Test;
import org.nutz.lang.Strings;

import com.znzz.framework.util.DateUtil;

public class CommonExcelUtil {

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";

	public static final String NOT_EXCEL_FILE = " : 没有此excel文件!";
	public static final String PROCESSING = "Processing...";

	/**
	 * 获取单元格里的值
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getValue(XSSFCell cell) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd");
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		}

		else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sFormat.format(date);
			} else {
				cellValue = decimalFormat.format((cell.getNumericCellValue()));
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula().toString();
		}
		return cellValue;
	}

	/**
	 * 获取HSSFCell单元格里的数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getValue(HSSFCell cell) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd");
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		}

		else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sFormat.format(date);
			} else {
				cellValue = decimalFormat.format((cell.getNumericCellValue()));
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula().toString();
		}
		return cellValue;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path) {
		if (path == null || "".equals(path.trim())) {
			return CommonExcelUtil.EMPTY;
		}
		if (path.contains(CommonExcelUtil.POINT)) {
			return path.substring(path.lastIndexOf(CommonExcelUtil.POINT) + 1, path.length());
		}
		return CommonExcelUtil.EMPTY;
	}

	/**
	 * 获取表格里的时间插入数据库（2007版本以上）
	 * 
	 * @param time
	 * @return
	 */
	public static Date getDate(XSSFCell time) {
		String val = null;
		String cellValue = getValue(time);
		if (Strings.isBlank(cellValue)) {
			return null;
		}
		//time.setCellType(Cell.CELL_TYPE_STRING);
		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(time)) {
			Date theDate = time.getDateCellValue();
			SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd");
			if (theDate != null) {
				val = dff.format(theDate);
			} else {
				// val = dff.format(new Date());
				return null;
			}
		} else {
			DecimalFormat df = new DecimalFormat("0");
			val = df.format(time.getNumericCellValue());
		}

		return DateUtil.getDateFromString(val);
	}

	/**
	 * 获取表格里的时间插入数据库（2003-2007版本以上）
	 * 
	 * @param time
	 * @return
	 */
	public static Date getDate(HSSFCell time) {
		String val = null;
		String cellValue = getValue(time);
		if (Strings.isBlank(cellValue)) {
			return null;
		}

		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(time)) {
			Date theDate = time.getDateCellValue();
			SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			if (theDate != null) {
				val = dff.format(theDate);
			} else {
				// val = dff.format(new Date());
				return null;
			}
		} else {
			DecimalFormat df = new DecimalFormat("0");
			val = df.format(time.getNumericCellValue());
		}

		return DateUtil.getDateFromString(val);
	}

	/**
	 * 获取合并单元格的值(2007以上)
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(XSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();

		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();

			if (row >= firstRow && row <= lastRow) {

				if (column >= firstColumn && column <= lastColumn) {
					XSSFRow fRow = sheet.getRow(firstRow);
					XSSFCell cell = fRow.getCell(firstColumn);
					return getValue(cell);
				}
			}
		}
		return null;
	}
	/**
	 * 获取合并单元格的值(2003-2007)
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			
			if (row >= firstRow && row <= lastRow) {
				
				if (column >= firstColumn && column <= lastColumn) {
					HSSFRow fRow = sheet.getRow(firstRow);
					HSSFCell cell = fRow.getCell(firstColumn);
					return getValue(cell);
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断sheet页中是否含有合并单元格
	 * 
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean hasMerged(Sheet sheet) {
		return sheet.getNumMergedRegions() > 0 ? true : false;
	}

	/**
	 * 将时间格式转成Date
	 * 
	 * @param value
	 * @return
	 */
	public static Date getDateFromString(String value) {
		return null;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getValue(Cell cell) {

		/*
		 * if(cell == null) return "";
		 * 
		 * if(cell.getCellType() == Cell.CELL_TYPE_STRING){
		 * 
		 * return cell.getStringCellValue();
		 * 
		 * }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
		 * 
		 * return String.valueOf(cell.getBooleanCellValue());
		 * 
		 * }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
		 * 
		 * return cell.getCellFormula() ;
		 * 
		 * }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
		 * 
		 * return String.valueOf(cell.getNumericCellValue());
		 * 
		 * } return "";
		 */

		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		}

		else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sFormat.format(date);
			} else {
				cellValue = decimalFormat.format((cell.getNumericCellValue()));
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cellValue = "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula().toString();
		}
		return cellValue;
	}

	public static Date getDateWithYearMonth(String date) {
		Date newDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			if(Strings.isNotBlank(date)){
				newDate = sdf.parse(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	}

	/**
	 * 判断是否是数字
	 * @param num
	 * @returnSample Text
	 */
	public static boolean isNumber(String num){
		//非负整数
		boolean first = Pattern.compile("^\\d+$").matcher(num).matches();
		//非负浮点数
		boolean sencond = Pattern.compile("^\\d+(\\.\\d+)?$").matcher(num).matches();
		if (first || sencond){
			return true;
		}
		return false;
	}

}
