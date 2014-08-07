package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UGallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PremissInfoActivity extends AbsBaseActivity implements OnGetGeoCoderResultListener
{

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_premiss_info);
		id = getIntent().getStringExtra("id");
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
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				String[] strlatlng = res.get("LatitudeLongitude").toString().split(",");
				((TextView) findViewById(R.id.text_application)).setText("申请事项：\r\n" + res.get("ApplicationItem").toString());
				((TextView) findViewById(R.id.text_num)).setText("许可编号：" + res.get("DecisionNum").toString());
				((TextView) findViewById(R.id.text_unit)).setText("申请单位：" + res.get("ApplicationUnit").toString());
				((TextView) findViewById(R.id.text_mark)).setText("桩号：" + res.get("Mark").toString());
				((TextView) findViewById(R.id.text_content)).setText("申请内容：\r\n" + res.get("ApplicationDigest").toString());
				// 初始化搜索模块，注册事件监听
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(PremissInfoActivity.this);
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.parseDouble(strlatlng[1]), Double.parseDouble(strlatlng[0]))));
				
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetLicenseInfoForPN, id));
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
		text_title.setText("许可详情");
		Btn_Right.setVisibility(View.INVISIBLE);
		
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
