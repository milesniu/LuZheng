package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
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
import com.miles.maipu.net.HttpGetUtil;
import com.miles.maipu.net.HttpPostUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.MapBaseActivity;
import com.miles.maipu.util.OverAllData;

public class MapViewActivity extends MapBaseActivity
{

	private List<HashMap<String, Object>> dataList = new Vector<HashMap<String, Object>>();
	private List<PostionData> dataLatlng = new Vector<PostionData>();
	public BitmapDescriptor mark_task = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding_blue);
	public BitmapDescriptor mark_event = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding_red);
	public BitmapDescriptor mark_premiss = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding_green);
	public static final int MARK_TASK = 0;
	public static final int MARK_EVENT = 1;
	public static final int MARK_PREMISS = 2;
	
	
	private void getTaskLatLngData()
	{
		showprogressdialog();
		
		new AsyncTask<String, String, String>()
		{

			@Override
			protected String doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				//任务部分
				List<HashMap<String, Object>> task = (List<HashMap<String, Object>>) HttpGetUtil.httpUrlConnection(ApiCode.GetEventsByPersonID,OverAllData.getLoginId(), currentpage + "", "200","0","0");
				for (HashMap<String, Object> item : task)
				{
					item.put("type", MARK_TASK);
					String[] t = (item.get("LatitudeLongitude").toString()).split(",");
					dataLatlng.add(new PostionData(new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[0])), MARK_TASK));
				}
				dataList.addAll(task);
//				if(OverAllData.getPostion()>0)	//非巡查员才在地图上显示事件上报的点
//				{
//					//事件部分
//					List<HashMap<String, Object>> event = (List<HashMap<String, Object>>) HttpGetUtil.httpUrlConnection(ApiCode.GetEventSubmitsNoAlloted,OverAllData.getLoginId(),currentpage + "", "200","0");
//					
//					if(event)
//					for (HashMap<String, Object> item : event)
//					{
//						if((item.get("IsAlloted")+"").equals("false"))
//						{
//							item.put("type", MARK_EVENT);
//							String[] t = (item.get("LatitudeLongitude").toString()).split(",");
//							dataLatlng.add(new PostionData(new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[0])), MARK_EVENT));
//							dataList.add(item);
//						}
//					}
//					
//				}
				//许可部分
				Map<String, Object> send = new HashMap<String, Object>();
				send.put("page", currentpage+"");
				send.put("rows", pagesize+"");
				
				List<HashMap<String, Object>> premiss = (List<HashMap<String, Object>>) HttpPostUtil.httpUrlConnection(ApiCode.PostLicenseInfoByItemAndNum,JSONUtil.toJson(send));
				for (HashMap<String, Object> item : premiss)
				{
					try
					{
						item.put("type", MARK_PREMISS);
						String[] t = (item.get("LatitudeLongitude").toString()).split(",");
						dataLatlng.add(new PostionData(new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[0])), MARK_PREMISS));
					}catch(Exception e)
					{
						e.printStackTrace();
						continue;
					}
				}
				dataList.addAll(premiss);
				
				return null;
			}

			@Override
			protected void onPostExecute(String result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				initOverlay();
				super.onPostExecute(result);
			}
		}.execute("");
		
//		new SendDataTask()
//		{
//			@SuppressWarnings("unchecked")
//			@Override
//			protected void onPostExecute(Object result)
//			{
//				// TODO Auto-generated method stub
//				hideProgressDlg();
//				taskList = (List<HashMap<String, Object>>) result;
//				for (HashMap<String, Object> item : taskList)
//				{
//					String[] t = (item.get("LatitudeLongitude").toString()).split(",");
//					taskLatlng.add(new PostionData(new LatLng(Double.parseDouble(t[1]), Double.parseDouble(t[0])), MARK_TASK));
//				}
//				initOverlay();
//				super.onPostExecute(result);
//			}
//
//		}.execute(new ParamData(ApiCode.GetEventsByPersonID, OverAllData.getLoginId(), currentpage + "", pagesize + ""));
	}
	
	private void initOverlay()
	{

		if (mBaiduMap == null)
		{
			return;
		}

		try
		{
			mBaiduMap.clear();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		for (int i=0;i<dataLatlng.size();i++)
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
				PostionData data = dataLatlng.get(pos);
				if(data.getCatrgoty() == MARK_TASK)
				{
					button.setText(dataList.get(pos).get("EventContent").toString());
					button.setTextColor(Color.rgb(0, 0, 0));
					listener = new OnInfoWindowClickListener()
					{
						public void onInfoWindowClick()
						{
							Intent newint = new Intent(mContext, TaskInfoActivity.class);
							newint.putExtra("id", dataList.get(pos).get("ID").toString());
							mContext.startActivity(newint);
							mBaiduMap.hideInfoWindow();
						}
					};
				}
				else if(data.getCatrgoty() == MARK_EVENT)
				{
					button.setText(dataList.get(pos).get("SubmiContent").toString());
					button.setTextColor(Color.rgb(0, 0, 0));
					listener = new OnInfoWindowClickListener()
					{
						public void onInfoWindowClick()
						{
							Intent newint = new Intent(mContext, EventInfoActivity.class);
							newint.putExtra("id", dataList.get(pos).get("ID").toString());
							newint.putExtra("time", dataList.get(pos).get("SubmitDateTime")+"");
							mContext.startActivity(newint);
							mBaiduMap.hideInfoWindow();
						}
					};
				}
				else if(data.getCatrgoty() == MARK_PREMISS)
				{
					button.setText(dataList.get(pos).get("ApplicationItem").toString());
					button.setTextColor(Color.rgb(0, 0, 0));
					listener = new OnInfoWindowClickListener()
					{
						public void onInfoWindowClick()
						{
							mContext.startActivity(new Intent(mContext, PremissInfoActivity.class).putExtra("id", dataList.get(pos).get("ID").toString()));
							
//							Intent newint = new Intent(mContext, EventInfoActivity.class);
//							newint.putExtra("id", dataList.get(pos).get("ID").toString());
//							newint.putExtra("time", dataList.get(pos).get("SubmitDateTime")+"");
//							mContext.startActivity(newint);
							mBaiduMap.hideInfoWindow();
						}
					};
				}
				

				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

	}

	private void showOnePoint(int pos)
	{
		BitmapDescriptor mark = null;
		if(dataLatlng.get(pos).getCatrgoty()==MARK_TASK)
		{
			mark = mark_task;	
		}
		else if (dataLatlng.get(pos).getCatrgoty()==MARK_EVENT)
		{
			mark = mark_event;
		}
		else if (dataLatlng.get(pos).getCatrgoty()==MARK_PREMISS)
		{
			mark =  mark_premiss;
		}
		
		mBaiduMap.addOverlay(new MarkerOptions().position(dataLatlng.get(pos).getLatlng()).icon(mark).zIndex(pos));
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
					while(myLocation==null)
					{
						Thread.sleep(1000);
					}
					setCenterPoint(myLocation);
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

	private class PostionData
	{
		LatLng latlng;
		int catrgoty;
		
		
		public PostionData(LatLng latlng, int catrgoty)
		{
			super();
			this.latlng = latlng;
			this.catrgoty = catrgoty;
		}
		public LatLng getLatlng()
		{
			return latlng;
		}
		public void setLatlng(LatLng latlng)
		{
			this.latlng = latlng;
		}
		public int getCatrgoty()
		{
			return catrgoty;
		}
		public void setCatrgoty(int catrgoty)
		{
			this.catrgoty = catrgoty;
		}
		
		
	}
	
}
