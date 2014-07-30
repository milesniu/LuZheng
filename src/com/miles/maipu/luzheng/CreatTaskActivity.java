package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.AbsCreatActivity;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreatTaskActivity extends AbsCreatActivity
{

	ImageView img_Photo = null;
//	private String imgPath = null;
	private List<HashMap<String, Object>> roadlist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
	private Spinner sp_road;
	private Spinner sp_lane;
	private Spinner sp_Organization;
	private Spinner sp_Person;
	private boolean isgetorga = false;
	private boolean islines = false;
//	private Bitmap bit = null;
	private EditText edit_zhuanghao;
	private EditText edit_descrtion;
//	private String uploadurl="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creat_task);
		initView();
	}
	

	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
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
		text_title.setText("新建任务");
		
		img_Photo = (ImageView) findViewById(R.id.img_photo);
		img_Photo.setOnClickListener(this);
		sp_road = (Spinner) findViewById(R.id.sp_road);
		sp_lane = (Spinner) findViewById(R.id.sp_lane);
		sp_Organization = (Spinner) findViewById(R.id.sp_Organization);
		sp_Person = (Spinner) findViewById(R.id.sp_person);
		edit_zhuanghao = (EditText)findViewById(R.id.edit_zhuanghao);
		edit_descrtion = (EditText)findViewById(R.id.edid_descrption);

		showprogressdialog();
		getspinnerData();
	}

	
	//根据机构获取机构下人员
	private void getPerson(String oid)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				personlist.clear();
				
				personlist = (List<HashMap<String, Object>>) result;
				
				String[] arraystr = new String[personlist.size()];
				for (int i = 0; i < personlist.size(); i++)
				{
					arraystr[i] = personlist.get(i).get("Name") + "";
				}
				sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				
				hideProgressDlg();
				
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.GetPersonInformationByOrganization, oid));
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		localpath = getCamera(img_Photo, localimg, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.img_photo:
//			goCameargetPhoto();
			goCamera();
			break;
		case R.id.bt_right:
			String zhuanghao = edit_zhuanghao.getText().toString();
			String desc = edit_descrtion.getText().toString();
			if(zhuanghao.equals(""))
			{
				Toast.makeText(mContext, "请输入桩号", 0).show();
				return;
			}else if(desc.equals(""))
			{
				Toast.makeText(mContext, "请输入事件描述信息", 0).show();
				return;
			}
			else
			{
//				uploadEventData();
//				showprogressdialog();
				uplaodPic();
			}
			
			break;
		}
		super.onClick(v);
	}
	
	//获取机构与线路
	private void getspinnerData()
	{
		//获取巡查分类与巡查项
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				isgetorga = true;
				if(islines)
				{
					hideProgressDlg();
				}
				organizalist = (List<HashMap<String, Object>>) result;
				organizalist.add(0, OverAllData.getMyOrganization());//添加同一级机构，同级机构间可以分配给下属
				String[] arraystr = new String[organizalist.size()];
				for (int i = 0; i < organizalist.size(); i++)
				{
					arraystr[i] = organizalist.get(i).get("Name") + "";
				}
				sp_Organization.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				sp_Organization.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						//获取机构下人员
						getPerson(organizalist.get(arg2).get("ID").toString());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetOrganizationUpOrDown, OverAllData.getLoginId(),"0"));//0，获取下属机构

		
		//获取线路
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				islines = true;
				if(isgetorga)
				{
					hideProgressDlg();
				}
				roadlist = (List<HashMap<String, Object>>) result;
				if(roadlist==null || roadlist.size()==0)
					return;
				String[] arraystr = new String[roadlist.size()];
				for (int i = 0; i < roadlist.size(); i++)
				{
					arraystr[i] = roadlist.get(i).get("Name") + "";
				}
				sp_road.setAdapter(new MySpinnerAdapter(mContext, arraystr));

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetRoadLines, OverAllData.getLoginId()));

		//组装上行下行
		String[] arraystr = new String[]
		{ "上行", "下行" };
		sp_lane.setAdapter(new MySpinnerAdapter(mContext, arraystr));
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creat_task, menu);
		return true;
	}


	@Override
	public void UploadData()
	{
		// TODO Auto-generated method stub
		
		Map<String, Object> senddata = new HashMap<String, Object>();
		
		String loginid =OverAllData.getLoginId();
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("ID", loginid);
		senddata.put("SendPersonInformation", p1);//登陆id
		
		String roadid = roadlist.get(sp_road.getSelectedItemPosition()).get("ID")+"";
		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("ID", roadid);
		senddata.put("RoadLine", p2);
		
		senddata.put("LatitudeLongitude",  DemoApplication.myLocation.getLatitude()+","+DemoApplication.myLocation.getLongitude());
		senddata.put("Mark", edit_zhuanghao.getText().toString());
		senddata.put("Picture", netUrl);
		senddata.put("EventContent", edit_descrtion.getText().toString());
		
		
		String pid = personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"";
		
	
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				if(res.get("IsSuccess").toString().toUpperCase().equals("TRUE"))
				{
					Toast.makeText(mContext, "新增任务成功",0).show();
					CreatTaskActivity.this.finish();
				}
				else
				{
					Toast.makeText(mContext, res.get("Message").toString(), 0).show();
					return;
				}
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.AddEventAllot, JSONUtil.toJson(senddata),pid));
	}

}
