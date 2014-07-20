package com.miles.maipu.util;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.miles.maipu.luzheng.MainActivity;
import com.miles.maipu.luzheng.R;

public class MapBaseActivity extends AbsBaseActivity
{
	public MapView mMapView = null;
	public BaiduMap mBaiduMap;
	
//	public BitmapDescriptor mark = BitmapDescriptorFactory.fromResource(R.drawable.localcar);
//	public BitmapDescriptor markonline = BitmapDescriptorFactory.fromResource(R.drawable.jgonline);
	

	public BitmapDescriptor mCurrentMarker;
	public LinearLayout linmap = null;
	public InfoWindow mInfoWindow;
	public boolean isFirstLoc = true;// 是否首次定位
	public LayoutParams layoutParams2;
	public Timer timer = null;
	public TimerTask tTask = null;
	public int countTime = 10;
	public Handler hander = null;
	public LatLng showlatlng = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BaiduMapOptions bo = new BaiduMapOptions().zoomControlsEnabled(true);
		mMapView = new MapView(mContext, bo);
		layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mBaiduMap = mMapView.getMap();
		
		linmap = (LinearLayout) findViewById(R.id.linear_map);
		linmap.removeAllViews();
		linmap.addView(mMapView, layoutParams2);

		
	}

	public void setCenterPoint(BDLocation lo)
	{
		mBaiduMap.setMyLocationEnabled(true);
		if (mMapView == null)
			return;
		MyLocationData locData = new MyLocationData.Builder().accuracy(lo.getRadius())
				.direction(100).latitude(lo.getLatitude()).longitude(lo.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		
//		mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.tuom);
//		mBaiduMap.setMyLocationConfigeration(new MyLocationConfigeration(LocationMode.NORMAL, true, mCurrentMarker));
	
//		if (isFirstLoc)
			isFirstLoc = false;
			LatLng ll = new LatLng(lo.getLatitude(), lo.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
	}
	
	
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		Btn_Right.setVisibility(View.INVISIBLE);
//		setCenterPoint(DemoApplication.myLocation);
		timer = new Timer();		//延迟500ms再加载位置，不然会卡死，新版sdk的bug
		timer.schedule(new TimerTask()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				setCenterPoint(DemoApplication.myLocation);
			}
		}, 500);
	}

	public void canleTimer()
	{
		if (timer != null)
			timer.cancel();
		if (tTask != null)
			tTask.cancel();
		timer = null;
		tTask = null;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		canleTimer();
		
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;


	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mMapView.onPause();
	}

}
