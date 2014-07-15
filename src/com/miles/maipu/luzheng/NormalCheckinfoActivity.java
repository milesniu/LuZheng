package com.miles.maipu.luzheng;

import com.miles.maipu.util.AbsBaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NormalCheckinfoActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_checkinfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_checkinfo, menu);
		return true;
	}

}
