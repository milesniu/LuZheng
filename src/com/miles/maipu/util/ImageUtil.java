package com.miles.maipu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.widget.ImageView;

public class ImageUtil
{
	
	/**图片压缩base64*/
	public static String Bitmap2StrByBase64(Bitmap bit)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bit.compress(CompressFormat.JPEG, 100, bos);// 参数100表示不压缩
		byte[] bytes = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	
	public static void getBitmapAsyn(String url,final ImageView img)
	{
	
		new AsyncTask<String, String, Bitmap>()
		{

			@Override
			protected Bitmap doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				return returnBitMap(params[0]);
			}

			@Override
			protected void onPostExecute(Bitmap result)
			{
				// TODO Auto-generated method stub
				img.setImageBitmap(result);
				super.onPostExecute(result);
			}			
		}.execute(url);
	}
	
	/**获取网路图片*/
	public static Bitmap returnBitMap(String url)
	{
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try
		{
			myFileUrl = new URL(url);
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		try
		{
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}

	
	/**图片压缩*/
	public static Bitmap compressImage(Bitmap image)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 30;// 压缩质量
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		while (baos.toByteArray().length / 1024 > 150)// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		{
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	@SuppressLint("NewApi")
	public static long getBitmapsize(Bitmap bitmap)
	{

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
		{
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}
}
