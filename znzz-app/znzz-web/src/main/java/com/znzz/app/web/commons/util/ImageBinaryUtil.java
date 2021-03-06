package com.znzz.app.web.commons.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 图片文件与base64编码之间的转换
 * @classname ImageBinaryUtil.java
 * @author chenzhongliang
 * @date 2017年9月5日
 */
public class ImageBinaryUtil {
	
	@SuppressWarnings("restriction")
	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();    
    @SuppressWarnings("restriction")
	static BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
        
  /*  public static void main(String[] args) {    
            System.out.println(getImageBinary());  //将图片转成base64编码     
            base64StringToImage(getImageBinary()); //将base64的编码转成图片   
    } */   
        
     public String getImageBinary(String url){  
    	if(url=="" || url==null){
    		return null;
    	}
        File f = new File(url);           
        BufferedImage bi;    
        try {    
            bi = ImageIO.read(f);    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();    
            ImageIO.write(bi, "jpg", baos);    
            byte[] bytes = baos.toByteArray();    
                
            return encoder.encodeBuffer(bytes).trim();    
        } catch (IOException e) {
        	System.out.println("该路径图片找不到 或 图片转换二级制数据流错误");
            //e.printStackTrace();    
        	return null;
        }    
    }    
        
     public void base64StringToImage(String base64String){    
        try {    
            byte[] bytes1 = decoder.decodeBuffer(base64String);                  
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
            BufferedImage bi1 =ImageIO.read(bais);    
            File w2 = new File("d://2.png");//可以是jpg,png,gif格式    
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    }    
    

}
