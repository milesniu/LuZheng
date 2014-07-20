package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

public class CreatNormalActivity extends AbsBaseActivity
{

	ImageView img_Photo = null;
	private String imgPath= null;
	private List<HashMap<String,Object>> roadlist = new Vector<HashMap<String,Object>>();
	Spinner sp_road;
	Spinner sp_line;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_normal);
		
		
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		img_Photo = (ImageView) findViewById(R.id.img_photo);
		img_Photo.setOnClickListener(this);
		sp_road = (Spinner)findViewById(R.id.sp_road);
		sp_line = (Spinner)findViewById(R.id.sp_line);
		super.initView();
		Btn_Right.setBackgroundResource(R.drawable.btsure);
		
		showprogressdialog();
		getspinnerData();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.img_photo:
			goCameargetPhoto();
			break;
		}
		super.onClick(v);
	}

	private void getspinnerData()
	{
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				roadlist = (List<HashMap<String, Object>>) result;
				
				String[] arraystr = new String[roadlist.size()];
				for(int i=0;i<roadlist.size();i++)
				{
					arraystr[i] = roadlist.get(i).get("Name")+"";
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, arraystr);
				sp_road.setAdapter(adapter);
				
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.GetRoadLines, OverAllData.loginInfo.get("ID")+""));
		
		String[] arraystr = new String[]{"上行","下行"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, arraystr);
		sp_line.setAdapter(adapter);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		imgPath = cameraForresult(img_Photo, requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creat_normal, menu);
		return true;
	}

}
