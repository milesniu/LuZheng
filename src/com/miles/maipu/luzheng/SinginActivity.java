package com.miles.maipu.luzheng;

import java.util.List;
import java.util.Vector;

import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.BaseMapObject;
import com.miles.maipu.util.MutiChoiseDlg;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SinginActivity extends AbsBaseActivity
{

	private Button Btn_Select = null;
	private Button Btn_Singin = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_singin);
		
	}

	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bt_select:
			new MutiChoiseDlg(mContext, new Vector<BaseMapObject>());
			break;
		case R.id.bt_singin:
			this.finish();
			break;
		}
		super.onClick(v);
	}



	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		text_title.setText("签到");
		Btn_Right.setVisibility(View.INVISIBLE);
		Btn_Select = (Button)findViewById(R.id.bt_select);
		Btn_Select.setOnClickListener(this);
		Btn_Singin = (Button)findViewById(R.id.bt_singin);
		Btn_Singin.setOnClickListener(this);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.singin, menu);
		return true;
	}

}
