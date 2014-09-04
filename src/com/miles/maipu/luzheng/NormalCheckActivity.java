package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
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

public class NormalCheckActivity extends AbsBaseActivity implements OnScrollListener
{
	
	private ListView list_Cotent;
	private List<HashMap<String,Object>> datalist = new Vector<HashMap<String,Object>>();
	private boolean isneedrefresh = false;
	private boolean isorg = false;
	private NormalAdapter adapter;
	private String status;
	private String oid;
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
		setContentView(R.layout.activity_normal_check);
		super.onCreate(savedInstanceState);
		isorg = getIntent().getBooleanExtra("isorg", false);
		status = getIntent().getStringExtra("status");
		oid = getIntent().getStringExtra("id");
		 initView();
		 isneedrefresh = true;
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==Btn_Right)
		{
			if(OverAllData.getRecordId().equals(""))
			{
				Toast.makeText(mContext, "还未签到，无法新建记录...", 0).show();
				return;
			}
			
			isneedrefresh = true;
			startActivity(new Intent(mContext, CreatNormalActivity.class));
		}
		super.onClick(v);
	}

	private void getAndInputData()
	{
		list_Cotent = (ListView)findViewById(R.id.list_content);
		showprogressdialog();
		getDataList();
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
		if(isorg)
		{
			Btn_Right.setVisibility(View.INVISIBLE);
		}
		else
		{
			Btn_Right.setVisibility(View.VISIBLE);
		}
		text_title.setText("路政巡查");
		
	}

	private void getDataList()
	{
		ParamData pdata = null;
		if(isorg)
		{
			pdata = new ParamData(ApiCode.GetPatorlRecordDetailListByOrgID, oid,(currentpage++)+"",pagesize+"",status);
			
		}
		else
		{
			pdata = new ParamData(ApiCode.GetPatorlRecordDetailList, OverAllData.getRecordId(),(currentpage++)+"",pagesize+"");
			
		}
		
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
					list_Cotent.removeFooterView(moreView);
					List_Content.addFooterView(moreView); // 添加底部view，一定要在setAdapter之前添加，否则会报错。
					List_Content.setOnScrollListener(NormalCheckActivity.this);
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
//								buss.put("bitmap", new SoftReference<Bitmap>(new WebImageBuilder().returnBitMap(NetApiUtil.thumbImgBaseUrl + path, WebImageBuilder.MINSIZE)));
								
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
				
				
				adapter = new NormalAdapter(mContext, datalist,AdapterCode.norMalCheck);
				list_Cotent.setAdapter(adapter);
				list_Cotent.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						isneedrefresh = false;
						startActivity(new Intent(mContext, NormalCheckinfoActivity.class).putExtra("isorg", isorg).putExtra("id", datalist.get(arg2).get("ID")+""));
					}
				});
				super.onPostExecute(result);
			}
			
			
		}.execute(pdata);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		for (HashMap<String, Object> item : datalist)
		{
			if (item.get("bitmap") != null)
			{
				Bitmap bitmap = ((Bitmap) item.get("bitmap"));
				if (bitmap != null && !bitmap.isRecycled())
				{
					// 回收并且置为null
					bitmap.recycle();
					bitmap = null;
				}

			}
		}
		System.gc();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_check, menu);
		return true;
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isneedrefresh)
		{
			currentpage = 1;
			datalist.clear();
			getAndInputData();
		}
		isneedrefresh = false;
		super.onResume();
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
