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
		switch(v.getId())
		{
		case R.id.img_singin:
			goActivity(SinginActivity.class, Weathermap.get("weather1").toString(),Weathermap.get("temp1").toString());
			break;
		case R.id.img_normalcheck:
			goActivity(NormalCheckActivity.class, "");
			
			break;
		}
		super.onClick(v);
	}



	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		img_Singin = (ImageView)findViewById(R.id.img_singin);
		img_Singin.setOnClickListener(this);
		img_NormalCheck = (ImageView)findViewById(R.id.img_normalcheck);
		img_NormalCheck.setOnClickListener(this);
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

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			Weathermap = (Map)JSONUtil.getMapFromJson(result).get("weatherinfo");
			((TextView)findViewById(R.id.text_weather)).setText(Weathermap.get("weather1").toString());
			((TextView)findViewById(R.id.text_temp)).setText(Weathermap.get("temp1").toString());
			int imgid = R.drawable.a00;
			imgid+=Integer.parseInt(Weathermap.get("img1").toString());
			((ImageView)findViewById(R.id.image_weather)).setImageResource(imgid);
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
