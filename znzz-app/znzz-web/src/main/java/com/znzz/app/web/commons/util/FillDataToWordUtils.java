package com.znzz.app.web.commons.util;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FillDataToWordUtils {
	
	 private static final Log log = Logs.get();
	
    /**
     * 得到图片
     * @return
     */
   /* private String getImageStr() {
        String imgFile = "D:/hanmanyi/pic/111.jpg";//需要在D盘下指定的目录下放一张图片
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // BASE64Encoder encoder = new BASE64Encoder();//这里会报错
        return encoder.encode(data);
    }*/

    
    /**
     * 
     * @param templatePath 模板路径
     * @param templateName 模板名称
     * @param dataMap 要填充的数据,要与模板中占位符保持一致
     * @param outFileName 输出文件名称
     * @param outFilePath 输出文件地址
     */
    public static String createWord(String templatePath , String templateName,Map<String,Object> dataMap,String outFileName,String outFilePath){
    	
    	log.info("准备调用模板::"+templatePath+templateName+"----输出文件::"+outFilePath+outFileName);
    	File outFile = null ;
    	Writer out = null;
    	String fileNmae="" ;
    	try {
    		
    		outFileName = outFileName+ new Date().getTime()+".doc" ;
    		
        	Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(FillDataToWordUtils.class, templatePath);  
            
            Template t = null;
            t = configuration.getTemplate(templateName);
            File file = new File(outFilePath) ;
            if(!file.exists()){//不存在,新建目录
            	file.mkdirs() ;
            }
            outFile = new File(outFilePath+outFileName);
           
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));

            t.process(dataMap, out);
            out.close();
            fileNmae = outFileName ;
		} catch (Exception e) {
			e.printStackTrace();
			if(null!=outFile){
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				outFile.delete() ;
			}
			log.info("fail:: word数据填充失败");
		}
    	
    	log.info("success:: word数据填充结束");

    	return fileNmae ;
    }
    
    public static void main(String[] args){
        Map map = new HashMap<String, Object>();
        map.put("T", "");
        map.put("A", "小洋人录音有限公式");
        map.put("B", "狗剩打天下");
        map.put("C", "18744343434");
        map.put("D", "光皮照相机");
        map.put("E", "V.IOJFWJI1");
        map.put("F", "FD.2354544343001");
        map.put("G", "光盘一份");
        map.put("H", "设备坏了,螺丝松动,不可拆卸fsadfsfdsfsfaffas");
        map.put("I", "狗霸天");
        map.put("J", "2017.12.23");
        map.put("K", "伪命题");
        map.put("L", "2017.12.24 23:54");
        map.put("M", "记得带钱,至少五元");
        map.put("N", "换了螺丝等等东西");
        map.put("O", "煌上煌");
        map.put("P", "2017.12.25");
        map.put("Q", "结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题结果精确么问题.");
        map.put("R", "曼妮芬");
        map.put("S", "2017/12/30");
        createWord("/ftl","送修单.ftl",map,"送修单","E:/template/");
    }
}
