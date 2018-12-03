package com.znzz.app.web.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileConvertor {

	public static byte[] getByte(File file) throws Exception {
		byte[] bytes = null;
		try {
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = is.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			is.close();
			bos.close();
			bytes = bos.toByteArray();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} 
		catch (IOException  e) {
			e.printStackTrace();
		}
		return bytes;
	}
}