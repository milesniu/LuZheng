package com.miles.maipu.luzheng;

import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyCenterActivity extends AbsBaseActivity
{

	private TextView text_name;
	private TextView text_postion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_center);
		initView();
	}
	
	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		text_name = (TextView)findViewById(R.id.text_name);
		text_postion = (TextView)findViewById(R.id.text_postion);
		text_name.setText("姓名："+OverAllData.getLoginName());
		text_postion.setText("职务："+OverAllData.getPostionName());
		
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setVisibility(View.INVISIBLE);
		}
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		text_title.setText("个人中心");
		
	}
	
}
