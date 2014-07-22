package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.DemoApplication;
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
	private Spinner sp_road;
	private Spinner sp_lane;
	private Spinner sp_project;
	private Spinner sp_category;
	private boolean isgetcate = false;
	private boolean islines = false;
	private Bitmap bit = null;
	private EditText edit_zhuanghao;
	private EditText edit_descrtion;
	private String uploadurl="";

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
		sp_lane = (Spinner) findViewById(R.id.sp_lane);
		sp_category = (Spinner) findViewById(R.id.sp_category);
		sp_project = (Spinner) findViewById(R.id.sp_project);
		edit_zhuanghao = (EditText)findViewById(R.id.edit_zhuanghao);
		edit_descrtion = (EditText)findViewById(R.id.edit_descrption);
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
//			goCamearNormal();
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
				showprogressdialog();
				uplaodPic();
			}
			
			break;
		}
		super.onClick(v);
	}

	
	
	
	
	private void uplaodPic()
	{
			
		new SendDataTask()
		{
			@Override
			protected Object doInBackground(ParamData... parm)
			{
				// TODO Auto-generated method stub
				if(bit==null)
				{
					//装载图片并压缩
					bit = ImageUtil.compressImage((BitmapFactory.decodeFile(imgPath)));
				}
				String imgbase = ImageUtil.Bitmap2StrByBase64(bit);
				FileUtils.getFile(imgbase.getBytes(), OverAllData.SDCardRoot, UnixTime.getStrCurrentUnixTime()+"img.txt");
				
				Map<String, Object> sendmap = new HashMap<String, Object>();
				sendmap.put("FileName", "img"+UnixTime.getStrCurrentUnixTime()+".jpg");		//图片名称
				sendmap.put("FileString", imgbase);			//图片base64字符换
				
				return super.doInBackground(new ParamData(ApiCode.SaveFile, JSONUtil.toJson(sendmap)));
			}

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				System.out.println(res.toString());
				if(res.get("IsSuccess")!=null&&res.get("IsSuccess").toString().equals("true"))
				{
					uploadurl = res.get("Message").toString();
					uploadEventData();
				}
				else
				{
					hideProgressDlg();
					Toast.makeText(mContext, res.get("msg")!=null?res.get("msg")+"":"图片上传失败", 0).show();
					return;
				}
				
				super.onPostExecute(result);
			}
		}.execute();
	}

	private void getspinnerData()
	{
		//获取巡查分类与巡查项
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

		
		//获取线路
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

		}.execute(new ParamData(ApiCode.GetRoadLines, OverAllData.loginInfo.get("ID") + ""));

		//组装上行下行
		String[] arraystr = new String[]
		{ "上行", "下行" };
		sp_lane.setAdapter(new MySpinnerAdapter(mContext, arraystr));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
//		imgPath = cameraResultNormal(img_Photo, bit, requestCode, resultCode, data);
		imgPath = cameraForresult(img_Photo, bit, requestCode, resultCode, data);
	}

	private void uploadEventData()
	{
		
		String PatorlRecord = ((Map)OverAllData.loginInfo.get("PatorlRecord")).get("ID")+"";
		String PatorlItem = ((List<HashMap<String, Object>>)categorylist.get(sp_category.getSelectedItemPosition()).get("PatorlItems")).get(sp_project.getSelectedItemPosition()).get("ID")+"";
		String RoadLine = roadlist.get(sp_road.getSelectedItemPosition()).get("ID")+"";
		String Lane = sp_lane.getSelectedItemPosition()+"";
		String LatitudeLongitude = DemoApplication.myLocation.getLatitude()+","+DemoApplication.myLocation.getLongitude();
		String Mark = edit_zhuanghao.getText().toString();
		String HandleDescription = edit_descrtion.getText().toString();
		Map<String, Object> senddata = new HashMap<String, Object>();
		
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("ID", PatorlRecord);
		senddata.put("PatorlRecord", p1);
		
		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("ID", PatorlItem);
		senddata.put("PatorlItem", p2);
		
		Map<String, Object> p3 = new HashMap<String, Object>();
		p3.put("ID", RoadLine);
		senddata.put("RoadLine", p3);
		senddata.put("Mark", Mark);
		senddata.put("HandleDescription", HandleDescription);
		senddata.put("AfterPicture", uploadurl);
		senddata.put("Lane", Lane);
		senddata.put("LatitudeLongitude", LatitudeLongitude);
		
		new SendDataTask()
		{	
			@SuppressWarnings("unchecked")
			@SuppressLint("DefaultLocale")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				if(res.get("IsSuccess").toString().toUpperCase().equals("TRUE"))
				{
					Toast.makeText(mContext, "巡查记录新增成功",0).show();
					CreatNormalActivity.this.finish();
				}
				else
				{
					Toast.makeText(mContext, res.get("Message").toString(), 0).show();
					return;
				}
				
				
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.AddPatorlRecordDetail, JSONUtil.toJson(senddata)));
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creat_normal, menu);
		return true;
	}

}
