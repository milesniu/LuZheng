package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.ImageUtil;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UnixTime;

public class CreatNormalActivity extends AbsBaseActivity
{

	ImageView img_Photo = null;
	private String imgPath = null;
	private List<HashMap<String, Object>> roadlist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> categorylist = new Vector<HashMap<String, Object>>();
	Spinner sp_road;
	Spinner sp_line;
	Spinner sp_project;
	Spinner sp_category;
	private boolean isgetcate = false;
	private boolean islines = false;
	private Bitmap bit = null;

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
		sp_road = (Spinner) findViewById(R.id.sp_road);
		sp_line = (Spinner) findViewById(R.id.sp_line);
		sp_category = (Spinner) findViewById(R.id.sp_category);
		sp_project = (Spinner) findViewById(R.id.sp_project);
		super.initView();
		Btn_Right.setBackgroundResource(R.drawable.btsure);
		text_title.setText("新建巡查");
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
		case R.id.bt_right:
			uplaodPic();
			break;
		}
		super.onClick(v);
	}

	
	private void uplaodPic()
	{
		if(bit==null)
		{
			bit = ImageUtil.compressImage((BitmapFactory.decodeFile(imgPath)));
		}
		String imgbase = ImageUtil.Bitmap2StrByBase64(bit);
		FileUtils.getFile(imgbase.getBytes(), OverAllData.SDCardRoot, UnixTime.getStrCurrentUnixTime()+"img.txt");
		
		Map<String, Object> sendmap = new HashMap<String, Object>();
		sendmap.put("FileName", "img"+UnixTime.getStrCurrentUnixTime()+".jpg");
		sendmap.put("FileString", imgbase);
		new SendDataTask()
		{
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				
				
				Toast.makeText(mContext, result+"", 0).show();
				super.onPostExecute(result);
			}
		}.execute(new ParamData(ApiCode.SaveFile,JSONUtil.toJson(sendmap) ));
	}

	private void getspinnerData()
	{
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				isgetcate = true;
				if(islines)
				{
					hideProgressDlg();
				}
				categorylist = (List<HashMap<String, Object>>) result;

				String[] arraystr = new String[categorylist.size()];
				for (int i = 0; i < categorylist.size(); i++)
				{
					arraystr[i] = categorylist.get(i).get("Name") + "";
				}
				sp_category.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				sp_category.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						List<HashMap<String, Object>> prolist = (List<HashMap<String, Object>>) (categorylist.get(arg2).get("PatorlItems"));
						String[] arraystr = new String[prolist.size()];
						for (int i = 0; i < prolist.size(); i++)
						{
							arraystr[i] = prolist.get(i).get("Name") + "";
						}
						sp_project.setAdapter(new MySpinnerAdapter(mContext, arraystr));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetAllPatorlCateGoryAndItems, ""));

		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				islines = true;
				if(isgetcate)
				{
					hideProgressDlg();
				}
				roadlist = (List<HashMap<String, Object>>) result;

				String[] arraystr = new String[roadlist.size()];
				for (int i = 0; i < roadlist.size(); i++)
				{
					arraystr[i] = roadlist.get(i).get("Name") + "";
				}
				sp_road.setAdapter(new MySpinnerAdapter(mContext, arraystr));

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetRoadLines, OverAllData.loginInfo.get("ID") + ""));

		String[] arraystr = new String[]
		{ "上行", "下行" };
		sp_line.setAdapter(new MySpinnerAdapter(mContext, arraystr));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		imgPath = cameraForresult(img_Photo, bit, requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creat_normal, menu);
		return true;
	}

}
