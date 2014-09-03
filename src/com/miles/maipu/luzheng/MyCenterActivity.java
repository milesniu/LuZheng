package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
		List_Content = (ListView)findViewById(R.id.list_content);
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
		refreshList();
	}
	
	
	private void refreshList()
	{
		List<HashMap<String, Object>> listcontent = new Vector<HashMap<String,Object>>();
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("isopen", "0");
		listcontent.add(item);
		listcontent.add(item);
		listcontent.add(item);
		listcontent.add(item);
		
		List_Content.setAdapter(new NormalAdapter(mContext, listcontent,AdapterCode.center));
//		list_Cotent.setOnItemClickListener(new OnItemClickListener()
//		{
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//			{
//				// TODO Auto-generated method stub
//				startActivity(new Intent(mContext, PremissInfoActivity.class).putExtra("id", promissList.get(arg2).get("ID")+""));
//			}
//		});
	}
	
}
