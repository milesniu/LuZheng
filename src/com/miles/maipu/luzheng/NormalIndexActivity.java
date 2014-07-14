package com.miles.maipu.luzheng;

import java.util.Map;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.net.HttpPostUtil;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.JSONUtil;

public class NormalIndexActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		new getweather().execute("");
	}
	
	class getweather extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			return HttpPostUtil.GetWeather();
//			return null;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			Map map = (Map)JSONUtil.getMapFromJson(result).get("weatherinfo");
			Toast.makeText(mContext, map.toString(), 0).show();
			((TextView)findViewById(R.id.text_weather)).setText(map.get("weather1").toString());
			((TextView)findViewById(R.id.text_temp)).setText(map.get("temp1").toString());
			int imgid = R.drawable.a00;
			imgid+=Integer.parseInt(map.get("img1").toString());
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
