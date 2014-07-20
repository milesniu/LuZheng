package com.miles.maipu.net;

import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

import com.miles.maipu.util.JSONUtil;

/**
 * 网络通信底层方法类，通过本类实现网络数据交互和Json的数据解析
 * 
 * @author 牛程
 * @version 1.0
 * @since 2013/04/12
 * */
public class HttpGetUtil
{	
	
	
	/**
	 * post网络交互
	 * 
	 * @param serlizetype
	 *            接口类型
	 * @param parm
	 *            需向网络发送的参数字符串列表序列
	 * @return 返回Json解析后的数据对象
	 * */
	public static Object httpUrlConnection(ApiCode api,String... parms)
	{
		if(!NetApiUtil.isCanuse())
		{
			return null;
		}
		String result = "";
		InputStream is = null;
		
		String userURL = NetApiUtil.BaseUrl+NetApiUtil.getApiName(api);
		for(String s:parms)
		{
			userURL+=NetApiUtil.URLencode(s)+"/";
		}
		
		HttpGet httpRequest = new HttpGet(userURL);
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
				{
					buf.append(data, 0, n);
				}
				result = new String(buf.toByteArray(), HTTP.UTF_8);
				is.close();
				if(result.charAt(0)=='[')
				{
					return JSONUtil.getListFromJson(result);
				}
				else
				{
					return JSONUtil.getMapFromJson(result);
				}
			}
			else
			{
				Log.v("tip==", "error response code");
				return null;
			}
		}
		catch (Exception e)
		{
			Log.e("error==", "" + e.getMessage());
			return null;
		}
	}
	

	
}
