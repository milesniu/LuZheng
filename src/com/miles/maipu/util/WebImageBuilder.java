package com.miles.maipu.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebImageBuilder
{
	public static final int MINSIZE = 2;
	public static final int MEDIASIZE = 3;
	public static final int BIGSIZE = 1;

	/**
	 * 通过图片url返回图片Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap returnBitMap(String path, int size)
	{
		URL url = null;
		Bitmap bitmap = null;
		try
		{
			url = new URL(path);
//			Log.v("WebImageBuilder:returnBitMap:url", url.toString());
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		try
		{
			// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			Log.v("WebImageBuilder:returnBitMap:conn", conn.toString());
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = size; // width，hight设为原来的size分一
			bitmap = BitmapFactory.decodeStream(is, null, options);

			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (NullPointerException e)
		{
			e.printStackTrace();
		} catch (OutOfMemoryError e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
}
