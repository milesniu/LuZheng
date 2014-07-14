package com.miles.maipu.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AbsBaseActivity extends Activity
{
	public Context mContext = this;
	
	public void goActivity(Class<?> cls,String... parm)
	{
		Intent intent = new Intent(mContext, cls);
		for(int i=0;i<parm.length;i++)
		{
			intent.putExtra("item"+i, parm[i]);
		}
		this.startActivity(new Intent(mContext, cls));
	}
	
}
