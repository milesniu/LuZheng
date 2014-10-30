package com.miles.maipu.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.ui.routeguide.subview.E;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UnixTime;

public class UploadLatLngService extends Service
{

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	// public BDLocation myLocation = null;
	private double lastLat = 0;
	private double lastLng = 0;
	private List<HashMap<String, String>> latlngList = new Vector<HashMap<String, String>>();
	Timer timer = null;
	
	
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		System.out.println("UPLOADstart Service");
		InitLocation();
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				if (!OverAllData.getRecordId().equals("")&&latlngList.size()>1)
				{
					HashMap<String, Object> senddata = new HashMap<String, Object>();
					senddata.put("data", latlngList);
					String strData = JSONUtil.toJson(senddata);
					strData = strData.substring(8, strData.length() - 1);
					Log.i("UPLOADgo", "[" + strData + "]");
					write( UnixTime.getStrCurrentSimleTime()+"----sendGPS2Web:\r\n"+strData+"\r\n");
					SimpleDateFormat df = new SimpleDateFormat("HHmmss");// 设置日期格式
					if (Integer.parseInt(df.format(new Date())) < 173000)
					{
						// mLocationClient.stop();
						// mLocationClient = null;
						// UploadLatLngService.this.stopSelf();
						// Log.i("UPLOADgo", "[Stop Self]");

						new SendDataTask()
						{

							@Override
							protected void onPostExecute(Object result)
							{
								// TODO Auto-generated method stub
								HashMap<String, Object> res = (HashMap<String, Object>) result;
								if (res.get("IsSuccess").toString().equals("true"))
								{
									latlngList.clear();
									write(UnixTime.getStrCurrentSimleTime()+"----ClearData:\r\n"+res.toString()+"\r\n");
								}
								Log.i("UPLOADresult", result.toString() + ":::listClear:::" + latlngList.size());
								super.onPostExecute(result);
							}
						}.execute(new ParamData(ApiCode.SaveTrajectory, strData));

					}

				}
			}
		}, 100, 60000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		Log.v("TrafficService", "startCommand");

		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
		// return START_REDELIVER_INTENT;
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener
	{

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// Receive Location
			// myLocation = location;
			// if(location.getLatitude()!=lastLat||location.getLongitude()!=lastLng)
			// {
			// latlngList.add(new LatLng(location.getLatitude(),
			// location.getLongitude()));
			// }

			if (location == null || location.getLocType() == 162 || "4.9E-324".equals(location.getLongitude() + "") || "4.9E-324".equals(location.getLongitude() + "") || (location.getLatitude() == lastLat && location.getLongitude() == lastLng))
			{
				Log.i("UPLOADgo", "[未获取最新GPS数据]");
			} else
			{

				lastLat = location.getLatitude();
				lastLng = location.getLongitude();

//				if (latlngList.size() < 20)
//				{
					Log.i("UPLOADgo", "[add GPS data]");
					HashMap<String, String> gpsdata = new HashMap<String, String>();
					gpsdata.put("PatorlRecord", OverAllData.getRecordId());
					gpsdata.put("CreateTime", UnixTime.getStrCurrentSimleTime());
					gpsdata.put("LatitudeLongitude", location.getLongitude() + "," + location.getLatitude());
					latlngList.add(gpsdata);
					
//					write( UnixTime.getStrCurrentSimleTime()+"----addGPS2List:\r\n"+gpsdata.toString()+"\r\n");
					
					
//				} else
//				{

//					if (!OverAllData.getRecordId().equals(""))
//					{
//						HashMap<String, Object> senddata = new HashMap<String, Object>();
//						senddata.put("data", latlngList);
//						String strData = JSONUtil.toJson(senddata);
//						strData = strData.substring(8, strData.length() - 1);
//						Log.i("UPLOADgo", "[" + strData + "]");
//
//						SimpleDateFormat df = new SimpleDateFormat("HHmmss");// 设置日期格式
//						if (Integer.parseInt(df.format(new Date())) < 173000)
//						{
//							// mLocationClient.stop();
//							// mLocationClient = null;
//							// UploadLatLngService.this.stopSelf();
//							// Log.i("UPLOADgo", "[Stop Self]");
//
//							new SendDataTask()
//							{
//
//								@Override
//								protected void onPostExecute(Object result)
//								{
//									// TODO Auto-generated method stub
//									HashMap<String, Object> res = (HashMap<String, Object>) result;
//									if (res.get("IsSuccess").toString().equals("true"))
//									{
//										latlngList.clear();
//
//									}
//									Log.i("UPLOADresult", result.toString() + ":::listClear:::" + latlngList.size());
//									super.onPostExecute(result);
//								}
//							}.execute(new ParamData(ApiCode.SaveTrajectory, strData));
//
//						}
//
//					}
				}
//			}

		}
	}

	private void write(String content)
	{
		try
		{
			// 如果手机插入了SD卡,而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				// 获取SD卡根目录
				File targetFile = new File(OverAllData.logcat);
				// 以指定文件创建RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件记录指针移动到最后
				raf.seek(raf.length());
				// 输出文件内容
				raf.write(content.getBytes());
				raf.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	public void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		int span = 3000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mLocationClient.stop();
		mLocationClient = null;
		System.out.println("UPLOADend Service");
	}

}
