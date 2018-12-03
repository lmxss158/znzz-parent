package com.znzz.app.web.commons.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class MonthReportUtils {
    public static void getFourRecord(int year, int monthI, Sheet sheetRepaired, String s) {
        Row nRow = null;
        Cell nCell = null;
        int rowNo = 0;                            //行号
        int colNo = 0;                            //列号
        //获取模板上的单元格样式
        nRow = sheetRepaired.getRow(2);
        //处理大标题
        nRow = sheetRepaired.getRow(rowNo++);            //获取一个行对象
        nCell = nRow.getCell(colNo);            //获取一个单元格对象
        nCell.setCellValue(year + "年" + monthI + "月" + s);
    }

    public static void getFirstSheet(int year, int monthI, Sheet sheetLend, String s) {
        Row nRow = null;
        Cell nCell = null;
        int rowNo = 0;                            //行号
        int colNo = 0;                            //列号
        //获取模板上的单元格样式
        nRow = sheetLend.getRow(2);
        //处理大标题
        nRow = sheetLend.getRow(rowNo++);            //获取一个行对象
        nCell = nRow.getCell(colNo);            //获取一个单元格对象
        nCell.setCellValue(year + "年" + monthI + "月" + s);
    }

    public static void getSecondSheet(int year, int monthI, Sheet sheetReturn, String s) {
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
        nCell.setCellValue(year + "年" + monthI + "月" + s);
    }

    public static void getThirdSheet(int year, int monthI, Sheet sheetLend) {
        Row nRow = null;
        Cell nCell = null;
        int rowNo = 0;									//行号
        int colNo = 0;									//列号
        //获取模板上的单元格样式
        nRow = sheetLend.getRow(2);
        //处理大标题
        nRow = sheetLend.getRow(rowNo++);				//获取一个行对象
        nCell = nRow.getCell(colNo);					//获取一个单元格对象
        nCell.setCellValue(year+"年"+monthI+"月"+"资产转账记录(无记录)");
    }

    public static void getFiveSheet(int year, int monthI, Sheet sheetRepairing, String s) {
        Row nRow = null;
        Cell nCell = null;
        int rowNo = 0;                            //行号
        int colNo = 0;                            //列号
        //获取模板上的单元格样式
        nRow = sheetRepairing.getRow(2);
        //处理大标题
        nRow = sheetRepairing.getRow(rowNo++);            //获取一个行对象
        nCell = nRow.getCell(colNo);            //获取一个单元格对象
        nCell.setCellValue(year + "年" + monthI + "月" + s);
    }

}
