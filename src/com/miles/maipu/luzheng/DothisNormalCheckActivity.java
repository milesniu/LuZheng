package com.miles.maipu.luzheng;

import com.miles.maipu.util.AbsBaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DothisNormalCheckActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dothis_normal_check);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dothis_normal_check, menu);
		return true;
	}

}
