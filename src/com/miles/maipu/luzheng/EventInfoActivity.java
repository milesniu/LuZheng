package com.miles.maipu.luzheng;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navi.sdkdemo.BNavigatorActivity;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.ImageUtil;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UGallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EventInfoActivity extends AbsBaseActivity implements OnGetGeoCoderResultListener
{

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private String id = "";
	private String time = "";
	private ImageView img_Photo;
	private LatLng latlng = null;
	private HashMap<String, Object> res;
	private LinearLayout linear_Dothis;
	private Button Btn_Fenpei;
	private Button Btn_Uplaod;
	private UGallery gallery_photo;
	private LinearLayout Linear_Step;
	private LinearLayout gallery_Linear;
	private HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>(); // 图片缓存

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		img_Photo = (ImageView) findViewById(R.id.img_photo);
		id = getIntent().getStringExtra("id");
		time = getIntent().getStringExtra("time");
		initView();
		getallotData();
	}

	private void getallotData()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				res = (HashMap<String, Object>) result;
				if(res==null)
				{
					Toast.makeText(mContext, "网络数据异常...", 0).show();
					return;
				}
				String[] strlatlng = res.get("LatitudeLongitude").toString().split(",");
				((TextView) findViewById(R.id.text_category)).setText(res.get("PatorCateGory").toString());
				((TextView) findViewById(R.id.text_time)).setText(time);
				((TextView) findViewById(R.id.text_uploadname)).setText(res.get("PersonInformation").toString());
				((TextView) findViewById(R.id.text_uploadunit)).setText(res.get("Organization").toString());
				
//				((TextView) findViewById(R.id.text_revcername)).setText("接收人：" + res.get("ReceiverName").toString());
				
				((TextView) findViewById(R.id.text_project)).setText(res.get("PatorlItem").toString());
				((TextView) findViewById(R.id.text_num)).setText(res.get("Extent")+""+res.get("Unit")+"");
				
				((TextView) findViewById(R.id.text_line)).setText(res.get("RoadLine").toString() );
				((TextView) findViewById(R.id.text_zhuanghao)).setText( res.get("Mark").toString());
				
				((TextView) findViewById(R.id.text_lane)).setText( res.get("Lane").toString());
				
				
				
				// ((TextView) findViewById(R.id.text_address)).setText("状态：" +
				// res.get("HandleStatus").toString());
				((TextView) findViewById(R.id.text_conntext)).setText(res.get("SubmiContent").toString());
				if (res.get("IsAlloted").toString().equals("true")||OverAllData.getPostion()==0||!res.get("ReceiverID").toString().equals(OverAllData.getLoginId()))
				{	//已分配||职位为巡查员||ReceiverID与登录ID不等
					linear_Dothis.setVisibility(View.GONE);
				} else
				{
					linear_Dothis.setVisibility(View.VISIBLE);
					if(OverAllData.isNeedUploadEvent())
					{
						Btn_Uplaod.setVisibility(View.VISIBLE);
					}
					else
					{
						Btn_Uplaod.setVisibility(View.GONE);
					}
				}
				
				List<HashMap<String, Object>> stepList = (List<HashMap<String, Object>>) res.get("EventSubmitReceives");
				
				
				InputStep(stepList);

				// 初始化搜索模块，注册事件监听
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(EventInfoActivity.this);
				latlng = new LatLng(Double.parseDouble(strlatlng[1]), Double.parseDouble(strlatlng[0]));
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
				

				String[] path = res.get("Picture").toString().split("\\|");
				
				
				ComposeImg(gallery_photo,gallery_Linear, path, imagesCache);
				
//				ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl + res.get("Picture") + "", img_Photo);
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.geteventsubmit, id,OverAllData.getLoginId()));
	}
	
	
	
	private void InputStep(List<HashMap<String, Object>> stepList)
	{
		for(HashMap<String, Object> map : stepList)
		{
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2);  
			layoutParams.setMargins(5, 5, 5, 5);
			
			ImageView img = new ImageView(mContext);
			img.setBackgroundResource(R.drawable.diver);
			Linear_Step.addView(img, layoutParams);
			
			
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			View view = mInflater.inflate(R.layout.listitem_steptask, null);
			((TextView)view.findViewById(R.id.text_unit)).setText(map.get("Organization").toString());
			((TextView)view.findViewById(R.id.text_name)).setText(map.get("Receiver").toString());
			((TextView)view.findViewById(R.id.text_time)).setText(map.get("SubmitDateTime").toString());

			
//			TextView text = new TextView(mContext);
//			text.setText("接收人："+map.get("Receiver").toString()+"\r\n接收机构："+map.get("Organization").toString()+"\r\n上报时间："+map.get("SubmitDateTime").toString());
//			text.setTextColor(getResources().getColor(R.color.black));
			Linear_Step.addView(view);
			
		}
	}
	
	

	private Spinner sp_Organization;
	private Spinner sp_Person;
	private EditText edit_jiaoban;
	private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
	private AlertDialog builder;

	private void showSelcet(final String tid, final String title, String org)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dlg_fenpeitask, null);
				sp_Organization = (Spinner) layout.findViewById(R.id.sp_organi);
				sp_Person = (Spinner) layout.findViewById(R.id.sp_person);
				LinearLayout linear = (LinearLayout)layout.findViewById(R.id.linear_jiaoban);
				edit_jiaoban = (EditText)layout.findViewById(R.id.edit_jiaoban);
				if(title.equals("事件分配"))
				{
					linear.setVisibility(View.VISIBLE);
				}
				
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle(title).setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						if(title.equals("事件分配"))
						{
							DothisToAlloted(0,OverAllData.getLoginId(),tid,personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"",URLEncoder.encode(edit_jiaoban.getText().toString()));
						}
						else if(title.equals("事件上报"))
						{
							DothisToAlloted(1,tid, OverAllData.getLoginId(),personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"");
						}
					}
				}).setNegativeButton("取消", null).show();

				hideProgressDlg();
				organizalist = (List<HashMap<String, Object>>) result;
				
				String[] arraystr = null;
				if (OverAllData.getPostion() > 0)		//领导才能上报给上级机构
				{
					arraystr = new String[organizalist.size()];
					for (int i = 0; i < organizalist.size(); i++)
					{
						arraystr[i] = organizalist.get(i).get("Name") + "";
					}
				} 
				else	//巡查员只能上报给同机构的领导
				{
					organizalist.add(0, OverAllData.getMyOrganization());//添加同一级机构,上报给同级机构的领导
					arraystr = new String[1];
					arraystr[0] = organizalist.get(0).get("Name") + "";
				}
				
				sp_Organization.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				sp_Organization.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						// 获取机构下人员
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

		}.execute(new ParamData(ApiCode.GetOrganizationUpOrDown, OverAllData.getLoginId(), org));
	}

	
	private void DothisToAlloted(int ford,String... id)
	{
			
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				HashMap<String, Object> res = (HashMap<String, Object>)result;
				if(res.get("IsSuccess").toString().equals("true"))
				{
					linear_Dothis.setVisibility(View.GONE);
				}
				getallotData();
				Toast.makeText(mContext, res.get("Message")+"", 0).show();
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ford==0?ApiCode.GetEventSubmitToAlloted:ApiCode.GetSubmitEvent,id));
	}
	
	// 根据机构获取机构下人员
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

	public void initView()
	{
		// TODO Auto-generated method stub

		Btn_Left = (Button) findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		Btn_Fenpei = (Button) findViewById(R.id.bt_fenpei);
		Btn_Uplaod = (Button) findViewById(R.id.bt_upload);
		Linear_Step = (LinearLayout)findViewById(R.id.linear_step);
		gallery_Linear = (LinearLayout) findViewById(R.id.grally_llinar);
		Btn_Fenpei.setOnClickListener(this);
		Btn_Uplaod.setOnClickListener(this);

		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		text_title.setText("事件详情");
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		Btn_Right.setVisibility(View.INVISIBLE);
		linear_Dothis = (LinearLayout) findViewById(R.id.linear_Dothis);
		gallery_photo = (UGallery)findViewById(R.id.gallery_photo);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.bt_right:
			break;
		case R.id.bt_fenpei:
			showSelcet(id, "事件分配", "0");
			// Toast.makeText(mContext, "事件分配", 0).show();
			break;
		case R.id.bt_upload:
			showSelcet(id, "事件上报", "1");
			// Toast.makeText(mContext, "事件上报", 0).show();
			break;
		}
	}

	@Override
	public void onDestroy()
	{
		try
		{
			BitmapDrawable bitmapDrawable = (BitmapDrawable) img_Photo.getDrawable();
			if (bitmapDrawable != null && bitmapDrawable.getBitmap().isRecycled())
			{
				bitmapDrawable.getBitmap().recycle();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_info, menu);
		return true;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result)
	{
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
		{
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
		}

		String strInfo = String.format("纬度：%f 经度：%f", result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result)
	{
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR)
		{
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
		}
		// Toast.makeText(mContext, result.getAddress(),
		// Toast.LENGTH_LONG).show();
		((TextView) findViewById(R.id.text_address)).setText(result.getAddress());

	}

}
