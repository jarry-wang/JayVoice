package com.jarry.jayvoice.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpUtil {
	    
	public static Bitmap returnBitMap(String url) {   
	   URL myFileUrl = null;   
	   Bitmap bitmap = null;   
	   try {   
		   myFileUrl = new URL(url);   
	   } catch (MalformedURLException e) {   
		   e.printStackTrace();   
	   }   
	   try {   
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
		    conn.setDoInput(true);   
		    conn.connect();   
		    InputStream is = conn.getInputStream();   
		    bitmap = BitmapFactory.decodeStream(is);   
		    is.close();   
	   } catch (IOException e) {   
		    e.printStackTrace();   
	   }   
	   return bitmap;   
	} 
	
	public static String readParse(String urlPath)  {
		StringBuffer stringBuffer=new StringBuffer();
		HttpURLConnection conn = null;
        try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			int len = 0;

			URL url = new URL(urlPath);

			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000); 
			conn.setReadTimeout(10 * 1000); 
			conn.connect();
			InputStream inStream = conn.getInputStream();

			BufferedReader br=new BufferedReader(new InputStreamReader(inStream,"GB2312"));
      						
			String data="";
			
			while((data=br.readLine())!=null){
				stringBuffer.append(data);
			}
			
			br.close();
			
		    inStream.close();
		    
		    outStream.close();
		   	
		    conn.disconnect();
			return stringBuffer.toString();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.d("readParse IOException");
			conn.disconnect();
			conn = null;
			return "";
		}
		return "";		
	}
}
