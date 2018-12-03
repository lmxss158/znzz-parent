package com.znzz.app.web.commons.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
/**
 * 控制log4j日志打印
 * @author wangqiang
 *
 */
public class Log4jConfig extends HttpServlet {  
    
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
  
    public void destroy() {  
        super.destroy();  
    }  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws  
  
            ServletException, IOException {  
    }  
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws  
  
            ServletException, IOException {  
    }  
  
    public void init() throws ServletException {  
        String prefix = getServletContext().getRealPath("/");  
        String file = getInitParameter("log4j");  
        if (file != null) {  
            PropertyConfigurator.configure(prefix + file);  
        }  
    }  
}  