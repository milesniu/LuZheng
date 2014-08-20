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
import android.widget.LinearLayout;
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
	public static boolean isNeedrefresh = false;
	private Button Btn_More;
	private LinearLayout linear_more;
	private TextView text_All;
	private TextView text_Yijiaoban;
	private TextView text_Weijiaoban;
	private int type = 0;
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
		Btn_More = (Button)findViewById(R.id.bt_more);
		Btn_More.setVisibility(View.VISIBLE);
		Btn_More.setBackgroundResource(R.drawable.btmore);
		linear_more = (LinearLayout)findViewById(R.id.linear_more);
		text_All = (TextView)findViewById(R.id.text_all);
		text_Yijiaoban = (TextView)findViewById(R.id.text_yifenpei);
		text_Weijiaoban = (TextView)findViewById(R.id.text_weifenpei);
		text_All.setOnClickListener(this);
		text_Yijiaoban.setOnClickListener(this);
		text_Weijiaoban.setOnClickListener(this);
		Btn_More.setOnClickListener(this);
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
	

	private void changeMoreText(View v)
	{
		text_All.setTextColor(getResources().getColor(R.color.graytext));
		text_Yijiaoban.setTextColor(getResources().getColor(R.color.graytext));
		text_Weijiaoban.setTextColor(getResources().getColor(R.color.graytext));
		((TextView)v).setTextColor(getResources().getColor(R.color.black));
		currentpage = 1;
		moreView.setVisibility(View.GONE);
		datalist.clear();
		switch(v.getId())
		{
		case R.id.text_all:
			type = 0;
			break;
		case R.id.text_yifenpei:
			type = 2;
			break;
		case R.id.text_weifenpei:
			type = 1;
			break;
		}
		getDataList(true);	
	}
	
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isNeedrefresh)
		{
			currentpage = 1;
			datalist.clear();
			getDataList(true);
		}
		isNeedrefresh = false;
		super.onResume();
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		
		switch(v.getId())
		{
		case R.id.bt_right:
			isNeedrefresh = true;
			startActivity(new Intent(mContext, UplaodEventActivity.class));
			break;
		case R.id.bt_more:
			if(linear_more.getVisibility()==View.GONE)
			{
				Btn_More.setBackgroundResource(R.drawable.btmoreup);
				linear_more.setVisibility(View.VISIBLE);
			}
			else
			{
				Btn_More.setBackgroundResource(R.drawable.btmore);
				linear_more.setVisibility(View.GONE);
			}
			break;
		case R.id.text_all:
		case R.id.text_yifenpei:
		case R.id.text_weifenpei:
		case R.id.text_yichuli:
			changeMoreText(v);
			linear_more.setVisibility(View.GONE);
			Btn_More.setBackgroundResource(R.drawable.btmore);
			break;
		}
		
	}

	private void getDataList(boolean isshowpro)
	{
		if(isshowpro)
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
					List_Content.removeFooterView(moreView);
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
			
			
		}.execute(new ParamData(ApiCode.GetEventSubmitsNoAlloted, OverAllData.getLoginId(),(currentpage++)+"",pagesize+"",type+""));
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// TODO Auto-generated method stub
		if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE)
		{
			// Log.i(TAG, "拉到最底部");
			moreView.setVisibility(View.VISIBLE);
			getDataList(false);
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	

}
