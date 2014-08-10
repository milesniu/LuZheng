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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
		bit.compress(CompressFormat.JPEG, 50, bos);// 参数100表示不压缩
		byte[] bytes = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	
	public static Bitmap addtext2Image(Bitmap photo)
	{
		 	String str = OverAllData.getLoginName();
		 	String strtime = UnixTime.getStrCurrentSimleTime();
		 	Bitmap icon = null;
	       int width = photo.getWidth(), hight = photo.getHeight();
	       System.out.println("宽"+width+"高"+hight);
	       icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); //建立一个空的BItMap  
	       Canvas canvas = new Canvas(icon);//初始化画布 绘制的图像到icon上  
	        
	       Paint photoPaint = new Paint(); //建立画笔  
	       photoPaint.setDither(true); //获取跟清晰的图像采样  
	       photoPaint.setFilterBitmap(true);//过滤一些  
	        
	       Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标  
	       Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标  
	       canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint  
	        
	       Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔  
	       textPaint.setTextSize(30.0f);//字体大小  
	       textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度  
	       textPaint.setColor(Color.BLUE);//采用的颜色  
	       //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置  
	       canvas.drawText(str, 10, hight-65, textPaint);//绘制上去 字，开始未知x,y采用那只笔绘制 
	       canvas.drawText(strtime, 10, hight-30, textPaint);//绘制上去 字，开始未知x,y采用那只笔绘制 
	       canvas.save(Canvas.ALL_SAVE_FLAG); 
	       canvas.restore(); 
	       return icon;
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
		while (baos.toByteArray().length / 1024 > 150)// 循环判断如果压缩后图片是否大于150kb,大于继续压缩
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
