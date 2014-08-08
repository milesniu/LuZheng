package com.miles.maipu.luzheng;

import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.UGallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AbsBaseActivity
{

	RelativeLayout real_updata;
	RelativeLayout real_feedback;
	RelativeLayout real_about;
	
	
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
		
		real_updata = (RelativeLayout)findViewById(R.id.rela_updata);
		real_feedback = (RelativeLayout)findViewById(R.id.rela_feedback);
		real_about = (RelativeLayout)findViewById(R.id.rela_about);
		
		real_updata.setOnClickListener(this);
		real_feedback.setOnClickListener(this);
		real_about.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.rela_updata:
			Toast.makeText(mContext, "当前已经是最新版本", 0).show();
			break;
		case R.id.rela_feedback:
			startActivity(new Intent(mContext, FeedBackActivity.class));
			break;
		case R.id.rela_about:
			startActivity(new Intent(mContext, AboutActivity.class));
			break;
		}
		super.onClick(v);
	}

	
	
}
