package com.miles.maipu.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.miles.maipu.luzheng.IndexActivity;
import com.miles.maipu.luzheng.R;
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
	private static final double EARTH_RADIUS = 6378137.0;
	// public BDLocation myLocation = null;
	private double lastLat = 0;
	private double lastLng = 0;
	private List<HashMap<String, String>> latlngList = new Vector<HashMap<String, String>>();
	Timer timer = null;
	WakeLock wakeLock;

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
				Log.i("UPLOADgo", "[ 定时器执行  ]");
				if (!OverAllData.getRecordId().equals("") && latlngList.size() > 0)
				{
					HashMap<String, Object> senddata = new HashMap<String, Object>();
					senddata.put("data", latlngList);
					String strData = JSONUtil.toJson(senddata);
					strData = strData.substring(8, strData.length() - 1);
					Log.i("UPLOADgo", "[" + strData + "]");
					write(UnixTime.getStrCurrentSimleTime() + "----sendGPS2Web:\r\n" + strData + "\r\n");
					SimpleDateFormat df = new SimpleDateFormat("HHmmss");// 设置日期格式
					if (Integer.parseInt(df.format(new Date())) < 235900)
					{
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
									write(UnixTime.getStrCurrentSimleTime() + "----ClearData:\r\n" + res.toString() + "\r\n");
								}
								Log.i("UPLOADresult", result.toString() + ":::listClear:::" + latlngList.size());
								// Intent intent = new
								// Intent("com.miles.uploadgps");
								// intent.putExtra("data", result.toString());
								// sendBroadcast(intent);// 传递过去
								super.onPostExecute(result);
							}
						}.execute(new ParamData(ApiCode.SaveTrajectory, strData));

					}

				}
			}
		}, 100, 60000);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, UploadLatLngService.class.getName());
		wakeLock.acquire();

	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		Log.v("TrafficService", "startCommand");

		Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.app_name), System.currentTimeMillis());

		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this, IndexActivity.class), 0);
		notification.setLatestEventInfo(this, "路政巡查", "请保持开启", pendingintent);
		notification.flags |= Notification.FLAG_NO_CLEAR;
		startForeground(0x111, notification);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
		// return START_REDELIVER_INTENT;
	}

	// 返回单位是米
	public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2)
	{
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
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
			lastLat = location.getLatitude();
			lastLng = location.getLongitude();
			String checkprotor = "";
			if (location == null || location.getLocType() == 162 || "4.9E-324".equals(location.getLongitude() + "") || "4.9E-324".equals(location.getLongitude() + "") || (location.getLatitude() == lastLat && location.getLongitude() == lastLng))
			{
				// Log.i("UPLOADgo", "[未获取最新GPS数据]");
				List<HashMap<String, String>> partor = OverAllData.getpatorLatLng();
				if (partor != null && partor.size() > 0)
				{
					int thou = -1;
					for (int i = 0; i < partor.size(); i++)
					{
						HashMap<String, String> obj = partor.get(i);
						double[] latlng = new double[]
						{ Double.parseDouble(obj.get("item").split(",")[0]), Double.parseDouble(obj.get("item").split(",")[1]) };
						double dis = getDistance(lastLng, lastLat, latlng[0], latlng[1]);
						if (dis > 500)
						{
							checkprotor += (latlng[0] + "," + latlng[1] + "|");
						}
						else
						{
							thou = i;
						}
					}
					// 从列表中删除改点
					if (thou != -1)
					{
						Log.i("UPLOADgo", "【删除点】" + thou);
						partor.remove(thou);
					}
					uploadCheck(checkprotor.substring(0, checkprotor.length() - 1));
				}
			}
			// else
			// {
			Log.i("UPLOADgo", "[add GPS data]" + "----" + location.getLatitude() + "**" + location.getLongitude());
			HashMap<String, String> gpsdata = new HashMap<String, String>();
			gpsdata.put("PatorlRecord", OverAllData.getRecordId());
			gpsdata.put("CreateTime", UnixTime.getStrCurrentSimleTime());
			gpsdata.put("LatitudeLongitude", location.getLongitude() + "," + location.getLatitude());
			latlngList.add(gpsdata);
			// }

		}
	}

	private void uploadCheck(String protorPos)
	{

		// ParamData pdata = null;
		// pdata = new ParamData(ApiCode.UnReachPoint,
		// OverAllData.getRecordId(), protorPos);

		Map<String, Object> senddata = new HashMap<String, Object>();
		senddata.put("ID", OverAllData.getRecordId());
		senddata.put("UnReach", protorPos);
		Log.i("UPLOADgo", "上传巡更数据----" + JSONUtil.toJson(senddata));
		new SendDataTask()
		{
			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				Log.i("UPLOADgo", "巡更点结果----" + result);
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.UnReachPoint, JSONUtil.toJson(senddata)));
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		int span = 5000;
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
		stopForeground(true);
		Intent sevice = new Intent(this, UploadLatLngService.class);
		this.startService(sevice);
		mLocationClient.stop();
		mLocationClient = null;
		System.out.println("UPLOADend Service");
		if (wakeLock != null)
		{
			wakeLock.release();
			wakeLock = null;
		}
	}
}
