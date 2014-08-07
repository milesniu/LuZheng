package com.miles.maipu.luzheng;

import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.UGallery;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}
	
	
	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button) findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		Btn_Right.setBackgroundResource(R.drawable.btsure);
		Btn_Right.setVisibility(View.INVISIBLE);
		text_title.setText("系统设置");

	}

	
}
