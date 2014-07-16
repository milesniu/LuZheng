package com.miles.maipu.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 网络通信底层方法类，通过本类实现网络数据交互和Json的数据解析
 * 
 * @author 牛程
 * @version 1.0
 * @since 2013/04/12
 * */
public class HttpPostUtil
{	
	/** 通用接口 */

	public static String Url = "http://micro.nestcms.com/router.api";
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
		if(!isCanuse())
		{
			return "false";
		}
		
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

	
	/**
	 * post网络交互
	 * 
	 * @param serlizetype
	 *            接口类型
	 * @param requestString
	 *            需向网络发送的参数字符串
	 * @return 返回Json解析后的数据对象
	 * */
	public static Object httpUrlConnection(String requestString)
	{
		if(!isCanuse())
		{
			return null;
		}
		Object objBack = null;	
		try
		{
			// 建立连接
			URL url = new URL(Url);

			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			httpConn.setDoOutput(true); // 使用 URL 连接进行输出
			httpConn.setDoInput(true); // 使用 URL 连接进行输入
			httpConn.setUseCaches(false); // 忽略缓存
			httpConn.setRequestMethod("POST"); // 设置URL请求方法

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes();
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Connection", "Keep-Alive"); // 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setConnectTimeout(10000);
			httpConn.setReadTimeout(10000);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();

			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode)
			{
				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null)
				{
					sb.append(readLine).append("\n");
				}
				responseReader.close();

				return sb.toString().equals("")?"{}":sb.toString();

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return "{\"Status\":\"false\",\"msg\":\"网络连接失败...\"}";
		}
		return objBack;
	}

	
}
