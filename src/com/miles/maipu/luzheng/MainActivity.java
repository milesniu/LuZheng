package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.OverAllData;

public class MainActivity extends AbsBaseActivity
{
	
	 private boolean flag;
	 private String IMEI = "";
	
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
		initLogFile();
	}
	
	
	private void initLogFile()
	{
		FileUtils filetools = new FileUtils();
		if (filetools.isFileExist(OverAllData.logcat))
		{
			// Toast.makeText(LoginActivity.this, "文件夹mywork已经存在.", 0).show();
		} else
		{
			filetools.creatSDDir(OverAllData.SDCardRoot);
			try
			{
				filetools.creatSDFile(OverAllData.logcat);
				
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);     
		IMEI = tm.getDeviceId();
		
		DisplayMetrics dm = new DisplayMetrics();getWindowManager().getDefaultDisplay().getMetrics(dm);
		OverAllData.width = dm.widthPixels;//宽度
		OverAllData.height = dm.heightPixels ;//高度
		
		PrePareFile();
//		String apiUrl = SmartWeatherUrlUtil.getInterfaceURL("101191101","forecast");
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				 VervifiAuth();
			}
		}, 1500);
		
	}
	
	
	private void VervifiAuth()
	{
		new SendDataTask()
		{
			@Override
			protected void onPostExecute(Object result)
			{
				if(result==null)
				{
					Toast.makeText(mContext, "网络连接失败，请检查！", 1).show();
					MainActivity.this.finish();
					return;
				}
				HashMap<String, Object> res =(HashMap<String, Object>)result;
				if(res.get("IsSuccess").toString().equals("false"))
				{
					Toast.makeText(mContext, "终端认证失败", 1).show();
					MainActivity.this.finish();
					
				}
				else{
					getVersion();
				}
			}
			
		}.execute(new ParamData(ApiCode.GetIsAuth, IMEI));
	}
	
	
	 private void getVersion()
	 {
		 new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				if(result==null)
				{
					Toast.makeText(mContext, "网络连接失败，请检查！", 1).show();
					MainActivity.this.finish();
					return;
				}
				final HashMap<String, Object> res =(HashMap<String, Object>)result;
				
				int vcode = Integer.parseInt(res.get("VersionNum").toString());
				if (getPackageInfo(MainActivity.this).versionCode < vcode)
				{
					//需要更新
					Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle("版本检测");
					builder.setMessage("发现新版本，是否更新？");
					builder.setPositiveButton("更新", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri.parse(res.get("DownloadUrl").toString());
							intent.setData(content_url);
							startActivity(intent);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialoginterface, int i)
						{
							if (res.get("IsCompulsoryUpdate").toString().equals("true"))
							{
								MainActivity.this.finish();
							}
						}
					});
					builder.show();
				}
				else
				{
					rhandler.sendEmptyMessageDelayed(1, 100);
				}
				super.onPostExecute(result);
			}
			 
		}.execute(new ParamData(ApiCode.appinfo, "com.miles.maipu.luzheng"));
	 }
	 
	 public static PackageInfo getPackageInfo(Context context)
		{
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = context.getPackageManager().getPackageInfo("com.miles.maipu.luzheng", 0);
			} catch (NameNotFoundException e)
			{
				Log.e("versioncode", e.getMessage());
			}
			return packageInfo;
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
