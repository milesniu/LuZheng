package com.miles.maipu.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.miles.uploadgps"); // 为BroadcastReceiver指定action，即要监听的消息名字。
		registerReceiver(new MyBroadcastReceive(), intentFilter); // 注册监听

	}

	private class MyBroadcastReceive extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (action.equals("com.miles.uploadgps")) // 判断是否接到电池变换消息
			{
				Toast.makeText(getApplicationContext(), intent.getStringExtra("data"), 0).show();
			}

		}

	}

}