package com.miles.maipu.luzheng;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

import com.miles.maipu.service.UploadLatLngService;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.OverAllData;

public class MainActivity extends AbsBaseActivity
{
	
	 private boolean flag;
	
	Handler rhandler = new Handler()
	{
		public void handleMessage(Message message)
		{
			super.handleMessage(message);
			
			MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
			MainActivity.this.finish();
		};
	};
	
	
	
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		View view = findViewById(R.id.rela_root);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
		view.setBackgroundResource(0);
		bitmapDrawable.setCallback(null);
		Bitmap bitmap = bitmapDrawable.getBitmap();
		if (bitmap != null && !bitmap.isRecycled())
		{
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
		super.onDestroy();
	}

	/** 文件目录的准备 */
	private void PrePareFile()
	{
		FileUtils fileutil = new FileUtils();
		//  主目录
		if (!fileutil.isFileExist(OverAllData.SDCardRoot))
		{
			fileutil.creatSDDir(OverAllData.SDCardRoot);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		PrePareFile();
//		String apiUrl = SmartWeatherUrlUtil.getInterfaceURL("101191101","forecast");
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				rhandler.sendEmptyMessageDelayed(1, 100);
			}
		}, 2000);
		
	}
	
	 
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
