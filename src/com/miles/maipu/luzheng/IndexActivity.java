package com.miles.maipu.luzheng;

import java.util.Map;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.net.HttpPostUtil;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.JSONUtil;

public class IndexActivity extends AbsBaseActivity
{

	ImageView img_Singin = null;
	ImageView img_NormalCheck = null;
	ImageView img_TaskManager = null;

	ImageView img_MapView = null;
	ImageView img_Upload = null;
	ImageView img_Premiss = null;

	ImageView img_Notice = null;
	ImageView img_Law = null;
	ImageView img_Setting = null;
	Map<String, Object> Weathermap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		new getweather().execute("");
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.img_singin:
			goActivity(SinginActivity.class, Weathermap.get("weather1").toString(), Weathermap.get("temp1").toString());
			break;
		case R.id.img_normalcheck:
			goActivity(NormalCheckActivity.class, "");
			break;
		case R.id.img_taskmanager:
			goActivity(TaskManagerActivity.class, "");
			break;
		case R.id.img_mapview:
			goActivity(MapViewActivity.class, "");
			break;
		case R.id.img_upload:
			goActivity(TaskManagerActivity.class, "");
			break;
		case R.id.img_premiss:
			goActivity(TaskManagerActivity.class, "");
			break;
		case R.id.img_notice:
			goActivity(TaskManagerActivity.class, "");
			break;
		case R.id.img_law:
			goActivity(TaskManagerActivity.class, "");
			break;
		case R.id.img_setting:
			goActivity(TaskManagerActivity.class, "");
			break;
		}
		super.onClick(v);
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		img_Singin = (ImageView) findViewById(R.id.img_singin);
		img_Singin.setOnClickListener(this);
		img_NormalCheck = (ImageView) findViewById(R.id.img_normalcheck);
		img_NormalCheck.setOnClickListener(this);
		img_TaskManager = (ImageView) findViewById(R.id.img_taskmanager);
		img_TaskManager.setOnClickListener(this);
		img_MapView = (ImageView) findViewById(R.id.img_mapview);
		img_Upload = (ImageView) findViewById(R.id.img_upload);
		img_Premiss = (ImageView) findViewById(R.id.img_premiss);
		img_Notice = (ImageView) findViewById(R.id.img_notice);
		img_Law = (ImageView) findViewById(R.id.img_law);
		img_Setting = (ImageView) findViewById(R.id.img_setting);

		img_MapView.setOnClickListener(this);
		img_Upload.setOnClickListener(this);
		img_Premiss.setOnClickListener(this);
		img_Notice.setOnClickListener(this);
		img_Law.setOnClickListener(this);
		img_Setting.setOnClickListener(this);

		super.initView();
	}

	class getweather extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			return HttpPostUtil.GetWeather();
		}

		@SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			if(result.equals("false"))
			{
				Toast.makeText(mContext, "访问失败...", 0).show();
				IndexActivity.this.finish();
				return;
			}
			Weathermap = (Map) JSONUtil.getMapFromJson(result).get("weatherinfo");
			((TextView) findViewById(R.id.text_weather)).setText(Weathermap.get("weather1").toString());
			((TextView) findViewById(R.id.text_temp)).setText(Weathermap.get("temp1").toString());
			int imgid = R.drawable.a00;
			imgid += Integer.parseInt(Weathermap.get("img1").toString());
			((ImageView) findViewById(R.id.image_weather)).setImageResource(imgid);
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

}
