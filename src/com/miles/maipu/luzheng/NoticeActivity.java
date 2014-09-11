package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.navisdk.ui.routeguide.subview.E;
import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

public class NoticeActivity extends AbsBaseActivity
{

	List<HashMap<String,Object>> noticelist = new Vector<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		initView();
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
		Btn_Right.setVisibility(View.INVISIBLE);;
		text_title.setText("通知公告");
		getNotice();
		
	}
	
	private void getNotice()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				noticelist = (List<HashMap<String, Object>>) result;
				List_Content.setAdapter(new NormalAdapter(mContext, noticelist,AdapterCode.notice));
				List_Content.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id)
					{
						// TODO Auto-generated method stub
						startActivity(new Intent(mContext, NoticeInfoActivity.class).putExtra("id", noticelist.get(position).get("ID").toString()));
					}
				});
				
				
				super.onPostExecute(result);
			}
			
			
			
		}.execute(new ParamData(ApiCode.GetNoticesByPersonID, OverAllData.getLoginId(),currentpage+"","2000"));
	}
	
	
}
