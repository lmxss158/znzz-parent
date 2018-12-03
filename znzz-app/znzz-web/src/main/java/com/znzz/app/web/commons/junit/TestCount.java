package com.znzz.app.web.commons.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCount {
		static List<Object> list  =new ArrayList<Object>();
		
		static Map<Integer ,Object> map =new HashMap<Integer ,Object>();
		
		
		static java.util.Stack<String> strStack=new java.util.Stack<String>();//栈类，导入响应的util包即可使用
		
		
		static String [] arry = new  String[24]; 
		  
		  
		public static void createList(){
			for (int i=0;i<24;i++){
				list.add("对应时间:"+i+":00----模拟数量--"+"100"+i);
			}
		}
		
		public  static void createMap (){
			
			for (int i=0;i<24;i++){
				map.put(i, list.get(i));
			}
			
		}
		
		public static void  testOut (int para){
			

			for (int i=para;i>=0;i--){
			
				strStack.push(map.get(i).toString()); // 8 7 ....0
				
			}
			
			for (int j=23  ;j>para;j--) {
				strStack.push(map.get(j).toString()); // 23 ....7
			}
			

			for (int i=0;i<=23;i++){
				
					arry[i]=strStack.pop();
			}	
				
			for (int i=0;i<=23;i++){
				
				System.out.print(arry[i]+",");
			}	
		
		}
		
		public static void main(String[] args) {
				createList();
				createMap ();
				testOut(9);
		}
		
}
