package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.MapBaseActivity;
import com.miles.maipu.util.OverAllData;

public class MapViewActivity extends MapBaseActivity
{

	private List<HashMap<String, Object>> taskList = new Vector<HashMap<String, Object>>();
	private List<LatLng> taskLatlng = new Vector<LatLng>();
	public BitmapDescriptor mark = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

	private void getTaskLatLngData()
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
				taskList = (List<HashMap<String, Object>>) result;
				for (HashMap<String, Object> item : taskList)
				{
					String[] t = (item.get("LatitudeLongitude").toString()).split(",");
					taskLatlng.add(new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[0])));
				}
				initOverlay();
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetEventsByPersonID, OverAllData.getLoginId(), currentpage + "", pagesize + ""));
	}

	private void initOverlay()
	{

		if (mBaiduMap == null)
		{
			return;
		}

		mBaiduMap.clear();

		for (int i=0;i<taskLatlng.size();i++)
		{
			showOnePoint(i);
		}

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
		{

			@Override
			public boolean onMarkerClick(Marker marker)
			{
				// TODO Auto-generated method stub
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				final LatLng ll = marker.getPosition();
				final int pos = marker.getZIndex();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;

				button.setText(taskList.get(pos).get("EventContent").toString());
				button.setTextColor(Color.rgb(0, 0, 0));
				listener = new OnInfoWindowClickListener()
				{
					public void onInfoWindowClick()
					{
						Intent newint = new Intent(mContext, TaskInfoActivity.class);
						newint.putExtra("id", taskList.get(pos).get("ID").toString());
						mContext.startActivity(newint);
						mBaiduMap.hideInfoWindow();
					}
				};

				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

	}

	private void showOnePoint(int pos)
	{
		mBaiduMap.addOverlay(new MarkerOptions().position(taskLatlng.get(pos)).icon(mark).zIndex(pos));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		initView();
		getTaskLatLngData();
	
	}

	public void initView()
	{
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		Btn_Right.setVisibility(View.INVISIBLE);
		text_title = (TextView) findViewById(R.id.title_text);
		text_title.setText("实时地图");
		Btn_Right.setVisibility(View.INVISIBLE);
		Btn_Left.setOnClickListener(this);
		linmap = (LinearLayout) findViewById(R.id.linear_map);
		linmap.removeAllViews();
		linmap.addView(mMapView, layoutParams2);
		timer = new Timer(); // 延迟500ms再加载位置，不然会卡死，新版sdk的bug
		timer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				try
				{
				setCenterPoint(DemoApplication.myLocation);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 500);

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}

}
