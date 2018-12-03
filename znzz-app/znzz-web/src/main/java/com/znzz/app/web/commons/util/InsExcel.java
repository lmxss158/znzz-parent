package com.znzz.app.web.commons.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import com.ctc.wstx.util.StringUtil;
import com.znzz.app.web.commons.base.Globals;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class InsExcel {


    // 生成条码文件至临时目录，并返回生成图片的路径信息
    public static String makeBarcode(String barcodeValue1, HttpServletRequest request) {
        //处理数据库统一编码 不规则问题
        String barcodeValue = null;
        if (barcodeValue1.contains("－")) {

            String barcodeValue2 = barcodeValue1.replace("－", "-");
            barcodeValue = toPinyin(barcodeValue2);
        } else if (barcodeValue1.contains("_")) {
            String barcodeValue2 = barcodeValue1.replace("_", "-");
            barcodeValue = toPinyin(barcodeValue2);

        } else {
            barcodeValue = toPinyin(barcodeValue1);
        }

        String property = Configer.getInstance().getProperty("barcodeUrl");
        //String realPath = Globals.AppRoot;
        String barcodePicPath = property;
        File file = new File(barcodePicPath);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        try {
            // Create the barcode bean
            //Code39Bean bean = new Code39Bean();
            Code128Bean bean = new Code128Bean();

            final int dpi = 120;

            // Configure the barcode generator
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); // makes the narrow
            bean.setBarHeight(6.0);                                            // bar

//			bean.setWideFactor(1.5);

            bean.doQuietZone(false);

            // Open output file
            String path = barcodePicPath + "/" + barcodeValue + ".png";
            File outputFile = new File(path);
            OutputStream out = new FileOutputStream(outputFile);
            try {
                // Set up the canvas provider for monochrome JPEG output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/png", dpi,
                        BufferedImage.TYPE_BYTE_BINARY, false, 0);

                // Generate the barcode
                bean.generateBarcode(canvas, barcodeValue);
                System.out.println();

                // Signal end of generation
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            System.out.println(barcodeValue + "--------------------------");

            e.printStackTrace();
        }

        return barcodePicPath + "/" + barcodeValue + ".png";
    }


    public static String toPinyin(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }


    @Test
    public void testBarPictrue(){
        String urlImageInfo = makeBarcode("Test",null);
        System.out.println(urlImageInfo);
    }
}