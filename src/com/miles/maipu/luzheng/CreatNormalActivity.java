package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.wheel.widget.SelectMarkDlg;
import com.lee.wheel.widget.SelectNumDlg;
import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsCreatActivity;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.GalleryData;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UGallery;

public class CreatNormalActivity extends AbsCreatActivity
{

	ImageView img_Photo = null;
	// private String imgPath = null;
	private List<HashMap<String, Object>> roadlist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> categorylist = new Vector<HashMap<String, Object>>();
	private Spinner sp_road;
	private Spinner sp_lane;
	private Spinner sp_project;
	private Spinner sp_category;
	private boolean isgetcate = false;
	private boolean islines = false;

	// private Bitmap bit = null;
	private EditText edit_zhuanghao;
//	private EditText edit_zhuanghaom;
	private EditText edit_descrtion;
	private AlertDialog builder;
	private EditText edit_k;
	private EditText edit_m;

	// private String uploadurl="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_creat_normal);
		super.onCreate(savedInstanceState);
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
		text_title.setText("新建巡查");

		img_Photo = (ImageView) findViewById(R.id.img_photo);
		img_Photo.setOnClickListener(this);
		sp_road = (Spinner) findViewById(R.id.sp_road);
		sp_lane = (Spinner) findViewById(R.id.sp_lane);
		sp_category = (Spinner) findViewById(R.id.sp_category);
		sp_project = (Spinner) findViewById(R.id.sp_project);
		edit_zhuanghao = (EditText) findViewById(R.id.edit_zhuanghao);
//		edit_zhuanghaom = (EditText) findViewById(R.id.edit_zhuanghaom);
		edit_descrtion = (EditText) findViewById(R.id.edit_descrption);
		
		edit_zhuanghao.setInputType(InputType.TYPE_NULL);
		edit_zhuanghao.setOnClickListener(this);
		gallery = (UGallery)findViewById(R.id.gallery_photo);
		edit_UnitNum = (EditText)findViewById(R.id.edit_num);
//		edit_UnitNum.setInputType(InputType.TYPE_NULL);
//		edit_UnitNum.setOnClickListener(this);
		
		text_unit = (TextView)findViewById(R.id.text_unit);
		findViewById(R.id.bt_law).setOnClickListener(this);
		ComposGallery(gallery);
		showprogressdialog();
		getspinnerData();
		edit_UnitNum.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				// TODO Auto-generated method stub
				if(!hasFocus)
				{
					edit_descrtion.setText(((List<HashMap<String, Object>>) categorylist.get(sp_category.getSelectedItemPosition()).get("PatorlItems")).get(sp_project.getSelectedItemPosition()).get("Name") +edit_UnitNum.getText().toString()+text_unit.getText().toString());
				}
			}
		});
		
	}
	
	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.img_photo:
			// goCameargetPhoto();
			goCamera();
			break;
		case R.id.bt_right:
			String zhuanghao = edit_zhuanghao.getText().toString();
			String desc = edit_descrtion.getText().toString();
			if (zhuanghao.equals(""))
			{
				Toast.makeText(mContext, "请输入桩号", 0).show();
				return;
			} 
//				else if (desc.equals(""))
//			{
//				Toast.makeText(mContext, "请输入事件描述信息", 0).show();
//				return;
//			} 
			else
			{
				// uploadEventData();
				showprogressdialog();
				uplaodPic();
			}

			break;
		case R.id.edit_zhuanghao:
//			new SelectMarkDlg(mContext).ShowDlg(edit_zhuanghao);
			selectMark();
			break;
		case R.id.edit_num:
			new SelectNumDlg(mContext).ShowDlg(edit_UnitNum);
			break;
		case R.id.bt_law:
			startActivity(new Intent(mContext, LawInfoActivity.class).putExtra("id",  ((List<HashMap<String, Object>>) categorylist.get(sp_category.getSelectedItemPosition()).get("PatorlItems")).get(sp_project.getSelectedItemPosition()).get("ID") + ""));
			break;
		}
		super.onClick(v);
	}

	
	
	
	private void selectMark()
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dlg_selectmark, null);
		edit_k = (EditText) layout.findViewById(R.id.edit_zhuanghaok);
		edit_m = (EditText) layout.findViewById(R.id.edit_zhuanghaom);
		
		builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle("桩号").setPositiveButton("确定", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				String k = edit_k.getText().toString();
				String  m = edit_m.getText().toString();
				edit_zhuanghao.setText("K"+k+"+"+m+"M");
				// Toast.makeText(mContexkt, tid, 0).show();
			}
		}).setNegativeButton("取消", null).show();

	}
	
	private void getspinnerData()
	{
		// 获取巡查分类与巡查项
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				isgetcate = true;
				if (islines)
				{
					hideProgressDlg();
				}
				categorylist = (List<HashMap<String, Object>>) result;
				if(categorylist==null||categorylist.size()<1)
				{
					Toast.makeText(mContext, "网络连接失败...", 0).show();
					return;
				}
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
						final List<HashMap<String, Object>> prolist = (List<HashMap<String, Object>>) (categorylist.get(arg2).get("PatorlItems"));
						String[] arraystr = new String[prolist.size()];
						for (int i = 0; i < prolist.size(); i++)
						{
							arraystr[i] = prolist.get(i).get("Name") + "";
						}
						sp_project.setAdapter(new MySpinnerAdapter(mContext, arraystr));
						sp_project.setOnItemSelectedListener(new OnItemSelectedListener()
						{

							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								// TODO Auto-generated method stub
								text_unit.setText(prolist.get(position).get("Unit")+"");
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{
								// TODO Auto-generated method stub
								
							}
						});

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
					CreatNormalActivity.this.finish();
				}
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetRoadLinesByPersonID, OverAllData.getLoginId()));

		// 组装上行下行
		String[] arraystr = new String[]
		{ "上行", "下行" };
		sp_lane.setAdapter(new MySpinnerAdapter(mContext, arraystr));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		GalleryData imgdata = getCamera(bitlist.size()+"", requestCode, resultCode, data);
		if(imgdata!=null)
		{
			bitlist.add(bitlist.size()-1,imgdata);
			imageAdapter.notifyDataSetChanged();
	
			compostPoint();
		}
		else
		{
			Toast.makeText(mContext, "请重新拍照", 0);
			return;
		}
		// imgPath = cameraForresult(img_Photo, bit, requestCode, resultCode,
		// data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creat_normal, menu);
		return true;
	}

	@Override
	public void UploadData()
	{
		// TODO Auto-generated method stub
		String PatorlRecord = OverAllData.getRecordId();
		String PatorlItem = ((List<HashMap<String, Object>>) categorylist.get(sp_category.getSelectedItemPosition()).get("PatorlItems")).get(sp_project.getSelectedItemPosition()).get("ID") + "";
		String RoadLine = roadlist.get(sp_road.getSelectedItemPosition()).get("ID") + "";
		String Lane = sp_lane.getSelectedItemPosition() + "";
		String LatitudeLongitude = myLocation.getLongitude()+","+myLocation.getLatitude();		//徐猛确定数值顺序与字段顺序反着填写
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
		String pictrues = "";
		for(int i=0;i<bitlist.size()-1;i++)
		{
			pictrues=pictrues+bitlist.get(i).getUrlPath()+"|";
		}
		pictrues = pictrues.substring(0, pictrues.length()-1);
		
		senddata.put("FrontPicture", pictrues);
		senddata.put("Lane", Lane);
		senddata.put("LatitudeLongitude", LatitudeLongitude);
		senddata.put("Extent", edit_UnitNum.getText().toString());
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
				if (res.get("IsSuccess").toString().toUpperCase().equals("TRUE"))
				{
					Toast.makeText(mContext, "巡查记录新增成功", 0).show();
					CreatNormalActivity.this.finish();
				} else
				{
					Toast.makeText(mContext, res.get("Message").toString(), 0).show();
					return;
				}

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.AddPatorlRecordDetail, JSONUtil.toJson(senddata)));

	}

}
