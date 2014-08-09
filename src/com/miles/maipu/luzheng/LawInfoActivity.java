package com.miles.maipu.luzheng;

import java.util.HashMap;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LawInfoActivity extends AbsBaseActivity
{

	String id="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law_info);
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
		
		text_title.setText("法规详情");
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
				
				((TextView)findViewById(R.id.text_gonglulaw)).setText("公路法：\r\n"+res.get("RoadLaw").toString());
				((TextView)findViewById(R.id.text_anquantl)).setText("公路安全保护条例：\r\n"+res.get("RoadSafeRegulations").toString());
				((TextView)findViewById(R.id.text_jiangsu)).setText("江苏省公路条例：\r\n"+res.get("JiangSuRoadRegulations").toString());
				((TextView)findViewById(R.id.text_content)).setText("处置要点：\r\n"+res.get("HandleRegulations").toString());
				
				super.onPostExecute(result);
			}
			
			
			
		}.execute(new ParamData(ApiCode.GetLawByPatorlItemID,id));
	}
	
	
}
