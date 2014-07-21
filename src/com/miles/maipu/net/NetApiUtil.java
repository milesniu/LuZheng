package com.miles.maipu.net;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.util.Log;

public class NetApiUtil
{
	
	
	public static String getApiName(ApiCode api)
	{
		switch(api)
		{
		case login:
			return "api/platform/user/login/";
		case GetAllPersonOfSameDepart:
			return "api/basicinformation/person/GetAllPersonOfSameDepart/";
		case Signin:
			return "api/patrol/patorlrecord/Sign/";
		case GetRoadLines:
			return "api/basicinformation/roadline/GetRoadLinesByPersonID/";
		case GetAllPatorlCateGoryAndItems:
			return "api/patrol/patorlcategory/GetAllPatorlCateGoryAndItems";
		case SaveFile:
			return "api/system/SaveFile";
		}
		return null;
	}
	
	/** 通用接口 */
	public static String BaseUrl = "http://58.216.243.77:3768/";
	//天气预报地址(常州)
	public static String WeatherUrl = "http://m.weather.com.cn/atad/101191101.html";
	//程序可用性检测地址(阿里云)
	public static String checkUrl = "http://ossmiles.oss-cn-hangzhou.aliyuncs.com/AppCtrl/com.miles.maipu.luzheng.txt";
	
	private static String checkResult = "-1";
	
	
	public static  boolean isCanuse()
	{
		if(checkResult.equals("-1"))
		{
			checkResult = GetCheckapp();
		}
		
		if(checkResult.equals("0"))
		{
			return false;
		}
		return true;
		
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String GetWeather()
	{		
		
		String result = "";
		InputStream is = null;
		HttpGet httpRequest = new HttpGet(WeatherUrl);
		try
		{
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{ // 正确

				is = httpResponse.getEntity().getContent();
				byte[] data = new byte[1024];
				int n = -1;
				ByteArrayBuffer buf = new ByteArrayBuffer(10 * 1024);
				while ((n = is.read(data)) != -1)
					buf.append(data, 0, n);
				result = new String(buf.toByteArray(), HTTP.UTF_8);
				is.close();
					
				return result;
			}
			else
			{
				Log.v("tip==", "error response code");
				return "";
			}
		}
		catch (Exception e)
		{
			Log.e("error==", "" + e.getMessage());
			return "";
		}
	}

	public static String URLencode(String url)
	{
		return URLEncoder.encode(url);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String GetCheckapp()
	{

		String result = "";
		InputStream is = null;
		HttpGet httpRequest = new HttpGet(checkUrl);
		try
		{
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{ // 正确

				is = httpResponse.getEntity().getContent();
				byte[] data = new byte[1024];
				int n = -1;
				ByteArrayBuffer buf = new ByteArrayBuffer(10 * 1024);
				while ((n = is.read(data)) != -1)
					buf.append(data, 0, n);
				result = new String(buf.toByteArray(), HTTP.UTF_8);
				is.close();
					
				return result;
			}
			else
			{
				Log.v("tip==", "error response code");
				return "";
			}
		}
		catch (Exception e)
		{
			Log.e("error==", "" + e.getMessage());
			return "";
		}
	}

	
}
