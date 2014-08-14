package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.WebImageBuilder;

public class EventListActivity extends AbsBaseActivity implements OnScrollListener
{
	
//	private ListView list_Cotent;
	private List<HashMap<String,Object>> datalist = new Vector<HashMap<String,Object>>();
	private NormalAdapter adapter;
	private boolean isNeedrefresh = false;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		initView();
		isNeedrefresh = true;
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
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		if(OverAllData.isNeedUploadEvent())
		{
			Btn_Right.setVisibility(View.VISIBLE);
		}
		else
		{
			Btn_Right.setVisibility(View.INVISIBLE);
		}
		text_title.setText("事件列表");
	}
	
	
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isNeedrefresh)
		{
			currentpage = 1;
			datalist.clear();
			getDataList();
		}
		isNeedrefresh = false;
		super.onResume();
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v==Btn_Right)
		{
			isNeedrefresh = true;
			startActivity(new Intent(mContext, UplaodEventActivity.class));
		}
	}

	private void getDataList()
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
		
				moredata_list = (List<HashMap<String, Object>>) result;
				if (moredata_list == null||moredata_list.size()==0)
				{
					Toast.makeText(mContext,"未取得任何数据...", 0).show();
					List_Content.removeFooterView(moreView);
					List_Content.setOnScrollListener(null);
					return;
				}
				
				datalist.addAll(moredata_list);
				count = datalist.size();
				if (moredata_list.size() >= pagesize)
				{
					List_Content.addFooterView(moreView); // 添加底部view，一定要在setAdapter之前添加，否则会报错。
					List_Content.setOnScrollListener(EventListActivity.this);
				} else
				{
					List_Content.removeFooterView(moreView);
					List_Content.setOnScrollListener(null);
				}
				
				new Thread()
				{
					public void run()
					{
						for (int i = 0; i < datalist.size(); i++)
						{
							Map<String, Object> buss = datalist.get(i);
							if (buss.get("bitmap") == null)
							{
								String path = buss.get("Picture").toString().split("\\|")[0];
								buss.put("bitmap", new WebImageBuilder().returnBitMap(NetApiUtil.thumbImgBaseUrl + path, WebImageBuilder.MINSIZE));
							}
						}
						handler.sendEmptyMessage(1);
					}
				}.start();
				
				if(currentpage>1&&adapter!=null)
				{
					adapter.notifyDataSetChanged();
					return;
				}
				
				adapter = new NormalAdapter(mContext, datalist,AdapterCode.eventList);
				List_Content.setAdapter(adapter);
				List_Content.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						isNeedrefresh = false;
						startActivity(new Intent(mContext, EventInfoActivity.class).putExtra("id", datalist.get(arg2).get("ID")+"").putExtra("time", datalist.get(arg2).get("SubmitDateTime")+""));
					}
				});
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.GetEventSubmitsNoAlloted, OverAllData.getLoginId(),(currentpage++)+"",pagesize+""));
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// TODO Auto-generated method stub
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE)
		{
			// Log.i(TAG, "拉到最底部");
			moreView.setVisibility(View.VISIBLE);
			getDataList();
//			new New_Youhuijuan_Task().execute(LOADMOREDATA + "");
			// Toast.makeText(CircleActivity.this, "加载了更多", 0).show();
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	

}
