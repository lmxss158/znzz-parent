package com.znzz.app.web.commons.junit;

import org.junit.Test;

public class TestString {

	@Test
	public void demo(){
		String str = "123.0";
		int i = str.lastIndexOf(".");
		String substring = str.substring(0, i);
		System.out.println(substring);
				
	}
}
