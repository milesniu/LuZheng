package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
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

import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsCreatActivity;
import com.miles.maipu.util.OverAllData;

public class UplaodEventActivity extends AbsCreatActivity
{
	ImageView img_Photo = null;
//	private String imgPath = null;
	private List<HashMap<String, Object>> roadlist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> categorylist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
	private Spinner sp_Category;
	private Spinner sp_Project;
	private Spinner sp_road;
	private Spinner sp_lane;
	private Spinner sp_Organization;
	private Spinner sp_Person;
	private boolean isgetorga = false;
	private boolean islines = false;
	private boolean isgetcate = false;
//	private Bitmap bit = null;
	private EditText edit_zhuanghao;
	private EditText edit_descrtion;
//	private String uploadurl="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uplaod_event);
		initView();
	}

	private void initView()
	{
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
		text_title.setText("新建巡查");

		img_Photo = (ImageView) findViewById(R.id.img_photo);
		img_Photo.setOnClickListener(this);
		sp_road = (Spinner) findViewById(R.id.sp_road);
		sp_lane = (Spinner) findViewById(R.id.sp_lane);
		sp_Category = (Spinner) findViewById(R.id.sp_category);
		sp_Project = (Spinner) findViewById(R.id.sp_project);
		sp_Organization = (Spinner) findViewById(R.id.sp_organization);
		sp_Person = (Spinner) findViewById(R.id.sp_person);
		edit_zhuanghao = (EditText) findViewById(R.id.edit_zhuanghao);
		edit_descrtion = (EditText) findViewById(R.id.edit_descrption);
		 getspinnerData();
	}
	
	
	private void getspinnerData()
	{
		showprogressdialog();
		// 获取巡查分类与巡查项
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				isgetcate = true;
				if (islines&&isgetorga)
				{
					hideProgressDlg();
				}
				categorylist = (List<HashMap<String, Object>>) result;

				String[] arraystr = new String[categorylist.size()];
				for (int i = 0; i < categorylist.size(); i++)
				{
					arraystr[i] = categorylist.get(i).get("Name") + "";
				}
				sp_Category.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				sp_Category.setOnItemSelectedListener(new OnItemSelectedListener()
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
						sp_Project.setAdapter(new MySpinnerAdapter(mContext, arraystr));

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

		//获取机构
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
		
		// 获取线路
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				islines = true;
				if (isgetcate)
				{
					hideProgressDlg();
				}
				try
				{
					roadlist = (List<HashMap<String, Object>>) result;
					if (roadlist == null || roadlist.size() == 0)
						return;
					String[] arraystr = new String[roadlist.size()];
					for (int i = 0; i < roadlist.size(); i++)
					{
						arraystr[i] = roadlist.get(i).get("Name") + "";
					}
					sp_road.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				} catch (Exception e)
				{
					Toast.makeText(mContext, result.toString(), 0).show();
					UplaodEventActivity.this.finish();
				}
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetRoadLines, OverAllData.getLoginId()));

		// 组装上行下行
		String[] arraystr = new String[]
		{ "上行", "下行" };
		sp_lane.setAdapter(new MySpinnerAdapter(mContext, arraystr));
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
	public void UploadData()
	{
		// TODO Auto-generated method stub
		
	}
}
