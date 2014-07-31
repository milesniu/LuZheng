package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
		setContentView(R.layout.activity_normal_check);
		super.onCreate(savedInstanceState);
		 initView();
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
		text_title.setText("巡查列表");
		
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

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isneedrefresh)
		{
			getAndInputData();
		}
		super.onResume();
	}

	

}
