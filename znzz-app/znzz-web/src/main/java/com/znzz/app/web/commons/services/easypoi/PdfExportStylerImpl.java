package com.znzz.app.web.commons.services.easypoi;

import java.io.IOException;

import org.nutz.ioc.loader.annotation.IocBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.pdf.styler.IPdfExportStyler;
import cn.afterturn.easypoi.pdf.styler.PdfExportStylerDefaultImpl;
@IocBean
public class PdfExportStylerImpl extends PdfExportStylerDefaultImpl  implements IPdfExportStyler {
	
	private static Logger logger = LoggerFactory.getLogger(PdfExportStylerImpl.class);
	
    @Override
    public Font getFont(ExcelExportEntity entity, String text) {
        try {
            //用以支持中文
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setSize(8);
            return font;
        } catch (DocumentException e) {
        	logger.error(e.toString());
        } catch (IOException e) {
        }
        Font font = new Font(FontFamily.UNDEFINED);
        return font;
    }
    
    @Override
    public Font getHeaderFont(ExcelExportEntity entity, String text) {
        try {
            //用以支持中文
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setSize(10);
            return font;
        } catch (DocumentException e) {
        	logger.error(e.toString());
        } catch (IOException e) {
        }
        Font font = new Font(FontFamily.UNDEFINED);
        return font;
    }
    
    @Override
    public Font getTitleFont(ExcelExportEntity entity, String text) {
            try {
                //用以支持中文
                BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);
                Font font = new Font(bfChinese);
                font.setSize(16);
                return font;
            } catch (DocumentException e) {
            	logger.error(e.toString());
            } catch (IOException e) {
            }
            Font font = new Font(FontFamily.UNDEFINED);
            return font;
    }
    
    @Override
    public Font getSecondTitleFont(ExcelExportEntity entity, String text) {
        try {
            //用以支持中文
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setSize(12);
            return font;
        } catch (DocumentException e) {
        	logger.error(e.toString());
        } catch (IOException e) {
        }
        Font font = new Font(FontFamily.UNDEFINED);
        return font;
    }
}
