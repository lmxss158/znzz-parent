package com.znzz.app.web.commons.services.easypoi;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerColorImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

/**
 * 带有样式的导出服务
 * @author wz
 *  2018年4月11日 下午15:06:15
 */
public class ExcelExportStylerColorBorderImpl extends ExcelExportStylerColorImpl implements IExcelExportStyler {

    public ExcelExportStylerColorBorderImpl(Workbook workbook) {
        super(workbook);
    }
    
	@Override
	public CellStyle getHeaderStyle(short headerColor) {
		CellStyle titleStyle = super.getHeaderStyle(headerColor);
		titleStyle.setBorderLeft((short) 1); // 左边框
		titleStyle.setBorderRight((short) 1); // 右边框
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        titleStyle.setFont(font);
		return titleStyle;
	}

	@Override
	public CellStyle getTitleStyle(short color) {
		CellStyle titleStyle = super.getTitleStyle(color);
		titleStyle.setBorderLeft((short) 1); // 左边框
		titleStyle.setBorderRight((short) 1); // 右边框
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setBorderTop((short) 1);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 24);
        titleStyle.setFont(font);
		return titleStyle;
	}

	@Override
	public CellStyle getSecondTitleStyle(short color) {
		CellStyle titleStyle = super.getSecondTitleStyle(color);
		titleStyle.setBorderLeft((short) 1); // 左边框
		titleStyle.setBorderRight((short) 1); // 右边框
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setBorderTop((short) 1);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        titleStyle.setFont(font);
		return titleStyle;
	}

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        return isWarp ? stringNoneWrapStyle : stringNoneStyle;
    }
    
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null; //stringNoneWrapStyle;
    }
    
}
