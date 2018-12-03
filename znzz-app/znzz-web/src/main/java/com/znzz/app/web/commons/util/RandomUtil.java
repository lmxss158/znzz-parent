package com.znzz.app.web.commons.util;

import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Random; 

  
/**
 * 
 * @ClassName: RandomUtil   
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fengguoxin
 * @date 2017年9月1日 下午5:23:09   
 */
public class RandomUtil {  
  
    /** 
     * 生成随机文件名：当前年月日时分秒+五位随机数 
     *  
     * @return 
     */  
    public static String getRandomFileName() {  
  
        SimpleDateFormat simpleDateFormat;  
  
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");  
  
        Date date = new Date();  
  
        String str = simpleDateFormat.format(date);  
  
        Random random = new Random();  
  
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
  
        return rannum + str;// 当前时间  
    }  
  
    public static void main(String[] args) {  
  
        String fileName = RandomUtil.getRandomFileName();  
  
        System.out.println(fileName);//8835920140307  
    }  
}  
