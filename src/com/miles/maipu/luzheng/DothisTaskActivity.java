package com.miles.maipu.luzheng;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DothisTaskActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dothis_task);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dothis_task, menu);
		return true;
	}

}
