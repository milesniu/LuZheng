package com.miles.maipu.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import com.miles.maipu.util.JSONUtil;

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
	/**
	 * post网络交互
	 * 
	 * @param serlizetype
	 *            接口类型
	 * @param requestString
	 *            需向网络发送的参数字符串
	 * @return 返回Json解析后的数据对象
	 * */
	public static Object httpUrlConnection(ApiCode code,String... requestString)
	{
		if(!NetApiUtil.isCanuse())
		{
			return null;
		}
		Object objBack = null;	
		try
		{
			// 建立连接
			String u = NetApiUtil.BaseUrl+NetApiUtil.getApiName(code);
			if(code==ApiCode.AddEventAllot||code==ApiCode.AddEvaluate||code == ApiCode.AddEventSubmit||code==ApiCode.AddEventFeedback)
			{
				u+= requestString[1];
			}
			
			URL url = new URL(u);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			httpConn.setDoOutput(true); // 使用 URL 连接进行输出
			httpConn.setDoInput(true); // 使用 URL 连接进行输入
			httpConn.setUseCaches(false); // 忽略缓存
			httpConn.setRequestMethod("POST"); // 设置URL请求方法

//			Log.i("UPLOADpost", requestString[0]);
			
			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString[0].getBytes();
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Connection", "Keep-Alive"); // 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setRequestProperty("Owner", "Z2Nuc3Q=");
			httpConn.setConnectTimeout(60*1000);
			httpConn.setReadTimeout(60*1000);

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
				String strRes = sb.toString();
				if(strRes.charAt(0)=='[')
				{
					return JSONUtil.getListFromJson(strRes);
				}
				else
				{
					return JSONUtil.getMapFromJson(strRes);
				}
//				return (HashMap<String, Object>) JSONUtil.getMapFromJson(sb.toString());

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return (HashMap<String, Object>) JSONUtil.getMapFromJson("{\"Status\":\"false\",\"msg\":\""+ex.toString()+"\"}");
		}
		return null;
	}	
}
