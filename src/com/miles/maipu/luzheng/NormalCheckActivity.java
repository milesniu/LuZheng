package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

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
			goActivity(CreatNormalActivity.class, "");
		}
		super.onClick(v);
	}



	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		Btn_Right.setBackgroundResource(R.drawable.newnormal);
		text_title.setText("巡查列表");
		list_Cotent = (ListView)findViewById(R.id.list_content);
		showprogressdialog();
		getDataList();
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
				list_Cotent.setAdapter(new NormalAdapter(mContext, datalist));
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
