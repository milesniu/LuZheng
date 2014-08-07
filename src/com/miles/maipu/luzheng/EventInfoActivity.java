package com.miles.maipu.luzheng;

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
import android.widget.AdapterView;
import android.widget.Button;
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

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				res = (HashMap<String, Object>) result;

				String[] strlatlng = res.get("LatitudeLongitude").toString().split(",");
				((TextView) findViewById(R.id.text_category)).setText("巡查分类：" + res.get("PatorCateGory").toString());
				((TextView) findViewById(R.id.text_time)).setText("上报时间：" + time);
				((TextView) findViewById(R.id.text_uploadname)).setText("上报人：" + ((Map) res.get("PersonInformation")).get("Name").toString());
				Map line = (Map) res.get("RoadLine");
				((TextView) findViewById(R.id.text_line)).setText("线路：" + line.get("Name").toString() + " " + line.get("Code").toString() + " " + line.get("StartMark").toString() + " ");
				// ((TextView) findViewById(R.id.text_address)).setText("状态：" +
				// res.get("HandleStatus").toString());
				((TextView) findViewById(R.id.text_conntext)).setText(res.get("SubmiContent").toString());
				if (res.get("IsAlloted").toString().equals("true")||!res.get("ReceiverID").toString().equals(OverAllData.getLoginId()))
				{
					linear_Dothis.setVisibility(View.GONE);
				} else
				{
					linear_Dothis.setVisibility(View.VISIBLE);
				}

				// 初始化搜索模块，注册事件监听
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(EventInfoActivity.this);
				latlng = new LatLng(Double.parseDouble(strlatlng[1]), Double.parseDouble(strlatlng[0]));
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
				

				String[] path = res.get("Picture").toString().split("\\|");
				
				
				ComposeImg(gallery_photo, path, imagesCache);
				
//				ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl + res.get("Picture") + "", img_Photo);
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.geteventsubmit, id));
	}

	private Spinner sp_Organization;
	private Spinner sp_Person;
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
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle(title).setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						if(title.equals("事件分配"))
						{
							DothisToAlloted(0,OverAllData.getLoginId(),tid,personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"");
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
		((TextView) findViewById(R.id.text_address)).setText("地址：" + result.getAddress());

	}

}
