package com.znzz.app.web.commons.junit;

import java.util.Properties;


import javax.xml.namespace.QName;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import com.alibaba.druid.support.json.JSONUtils;
import com.rabbitmq.tools.json.JSONUtil;
import com.znzz.app.instrument.modules.service.impl.ScardDeviceServiceImpl;

public class TestScada {
	private static String wsdlURL ;
	private static String namespaceUrl ;
	public static void main(String[] args) {
		try {
			Properties prop = new Properties();
			prop.load(ScardDeviceServiceImpl.class.getClassLoader().getResourceAsStream("scard.properties"));
		    wsdlURL  = prop.getProperty("wsdlURL");
		    namespaceUrl = prop.getProperty("namespaceUrl") ;
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			org.apache.cxf.endpoint.Client client = dcf.createClient(wsdlURL);
			QName qName = new QName(namespaceUrl,"GetAllStateMessage") ;
			Object[] res = client.invoke(qName, "");
			String jsonString = (String) res[0] ;
			
			
			System.out.println(jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
