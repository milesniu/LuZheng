package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

public class NormalCheckActivity extends AbsBaseActivity
{
	
	private ListView list_Cotent;
	private List<HashMap<String,Object>> datalist = new Vector<HashMap<String,Object>>();
	private boolean isneedrefresh = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_check);
		
	}

	
	

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==Btn_Right)
		{
			isneedrefresh = true;
			goActivity(CreatNormalActivity.class, "");
		}
		super.onClick(v);
	}

	private void getAndInputData()
	{
		list_Cotent = (ListView)findViewById(R.id.list_content);
		showprogressdialog();
		getDataList();
	}

	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		text_title.setText("巡查列表");
		if(isneedrefresh)
		{
			getAndInputData();
		}
	}

	private void getDataList()
	{
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
				list_Cotent.setAdapter(new NormalAdapter(mContext, datalist,AdapterCode.norMalCheck));
				list_Cotent.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						isneedrefresh = false;
						startActivity(new Intent(mContext, NormalCheckinfoActivity.class).putExtra("id", datalist.get(arg2).get("ID")+""));
					}
				});
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.GetPatorlRecordDetailList, OverAllData.getRecordId(),currentpage+"",pagesize+""));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_check, menu);
		return true;
	}

}
