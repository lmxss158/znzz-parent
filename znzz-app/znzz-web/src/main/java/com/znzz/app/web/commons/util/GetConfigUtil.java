package com.znzz.app.web.commons.util;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.nutz.log.Log;
import org.nutz.log.Logs;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetConfigUtil {

    private static Log logger = Logs.get() ;

    public static List<String> getPropertiesValue(String proName) {

        InputStreamReader isr= null;
        try {
            isr = new InputStreamReader(GetConfigUtil.class.getClassLoader().getResourceAsStream(proName), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(isr);

        String line= "";
        List<String> result = new ArrayList<>();
        try {
            while ((line=br.readLine())!=null) {
                if (line.trim().length() > 0){
                    result.add(line.trim());
                }
            }
            br.close();
            isr.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static void main(String[] args) {
        List<String> result = null;
        result = GetConfigUtil.getPropertiesValue("uploadToCloud.properties");

        System.out.println(result);
        System.out.println(result.size());
    }


}
