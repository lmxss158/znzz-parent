package com.znzz.app.web.commons.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import com.znzz.app.web.commons.base.Globals;

public class TemplateDownUtils {

	public static File getTemplateFile(String filenamePath,String templatePath,HttpServletResponse response){
		// 通过配置文件获取文件名称
    	String filename = (String) Configer.getInstance().get(filenamePath);
    	try {
			filename = new String(filename.getBytes("UTF-8"),"iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	// 告诉浏览器要下载文件
    	response.setHeader("Content-Disposition", "attachment;filename="+filename);
    	// 告诉浏览器要下载文件而不是直接打开文件
    	response.setContentType("application/-download");
    	response.setCharacterEncoding("UTF-8");
    	// 获取项目路径
    	String appRoot = Globals.AppRoot;
    	String gettemplatePath = (String) Configer.getInstance().get(templatePath);
    	appRoot = appRoot + gettemplatePath;
    	File file = new File(appRoot+filename);
    	// 另一种方法
    	/*String contextPath = request.getServletContext().getRealPath("/")+"WEB-INF/download/";
    		File file = new File(appRoot+filename);
    	*/
    	return file;
	}
}
