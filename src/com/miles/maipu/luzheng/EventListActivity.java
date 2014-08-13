package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.WebImageBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EventListActivity extends AbsBaseActivity
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
				if(datalist==null)
					return;
				datalist = (List<HashMap<String, Object>>) result;
				
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
			
			
		}.execute(new ParamData(ApiCode.GetEventSubmitsNoAlloted, OverAllData.getLoginId(),currentpage+"",pagesize+""));
	}

	

}
