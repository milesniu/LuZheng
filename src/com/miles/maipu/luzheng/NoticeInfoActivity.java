package com.miles.maipu.luzheng;

import java.util.HashMap;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
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

public class NoticeInfoActivity extends AbsBaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_info);
		initView();
		getInfo(getIntent().getStringExtra("id"));
		
	}
	
	
	public void initView()
	{
		// TODO Auto-generated method stub
		
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		Btn_Right.setVisibility(View.INVISIBLE);
		
		text_title.setText("通知详情");
	}

	
	private void getInfo(String id)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				
				HashMap<String, Object> res = (HashMap<String, Object>)result;
				
				((TextView)findViewById(R.id.text_title)).setText("通知标题："+res.get("Title").toString());
				((TextView)findViewById(R.id.text_time)).setText("发布时间："+res.get("ReleaseDateTime").toString());
				((TextView)findViewById(R.id.text_unit)).setText("发布机构："+res.get("ReleaseOrganization").toString());
				((TextView)findViewById(R.id.text_person)).setText("发布人："+res.get("ReleasePerson").toString());
				((TextView)findViewById(R.id.text_content)).setText("通知内容：\r\n"+res.get("Content").toString());
				
				super.onPostExecute(result);
			}
			
			
			
		}.execute(new ParamData(ApiCode.GetNoticeByID,id));
	}
	
}
