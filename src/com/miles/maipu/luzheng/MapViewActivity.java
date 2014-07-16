package com.miles.maipu.luzheng;

import com.miles.maipu.util.MapBaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MapViewActivity extends MapBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_map_view);
		super.onCreate(savedInstanceState);
	}

	
	
	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		text_title.setText("实时地图");
		
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}

}
