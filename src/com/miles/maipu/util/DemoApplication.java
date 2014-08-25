package com.miles.maipu.util;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.miles.maipu.util.AbsCreatActivity.MyLocationListener;


public class DemoApplication extends Application {
	

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
		
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		
	
	}
	
	
	
}