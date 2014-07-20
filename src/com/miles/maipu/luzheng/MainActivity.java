package com.miles.maipu.luzheng;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.OverAllData;

public class MainActivity extends AbsBaseActivity
{
	
	Handler rhandler = new Handler()
	{
		public void handleMessage(Message message)
		{
			super.handleMessage(message);
			
			
			MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
			MainActivity.this.finish();
		};
	};
	
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PrePareFile();
		
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
