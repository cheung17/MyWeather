package com.ztx.myWhether.httpUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.ztx.myWhether.MainActivity;

import android.widget.Toast;

public class HttpUtil {
	static String path = "";
	static String result = "";
	static HttpURLConnection conn=null;
	static URL url=null;
	public static String getJson(String city){
		  
		    	
		    	path = "http://wthrcdn.etouch.cn/weather_mini?citykey="
				+city;
		    	System.out.println("---path1="+path+"------");
		    	System.out.println("---city="+city+"------");
			
			
			try {
				url = new URL(path);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				is.close();
				bos.close();
				result = new String(bos.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(result);
			conn.disconnect();
		
		return result;
	}

}
