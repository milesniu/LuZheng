package com.miles.maipu.luzheng;

import java.util.HashMap;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.BaseMapObject;
import com.miles.maipu.util.ImageUtil;
import com.miles.maipu.util.OverAllData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NormalCheckinfoActivity extends AbsBaseActivity
{

	private TextView text_Category;
	private TextView text_Project;
	private TextView text_isSunmit;
	private TextView text_desCription;
	private String id;
	private ImageView img_Front;
	private LinearLayout linear_Remark;
	private ImageView img_After;
	private TextView text_remark;
	
	HashMap<String, Object> res = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_checkinfo);
		id = getIntent().getStringExtra("id");
		
	}

	private void initLocalView()
	{
		text_Category = (TextView)findViewById(R.id.text_category);
		text_Project = (TextView)findViewById(R.id.text_project);
		text_isSunmit = (TextView)findViewById(R.id.text_issubmit);
		text_desCription = (TextView)findViewById(R.id.text_descrption);
		img_Front = (ImageView)findViewById(R.id.img_front);
		linear_Remark = (LinearLayout)findViewById(R.id.linear_remark);
		text_remark = (TextView)findViewById(R.id.text_remark);
		img_After = (ImageView)findViewById(R.id.img_after);
		
		
		showprogressdialog();
		getDetailInfo();
		
	}
	
	
	
	
	
	@Override
	public void initView()
	{
		// TODO Auto-generated method stub
		super.initView();
		text_title.setText("巡查处理");
		Btn_Right.setBackgroundResource(R.drawable.dothis);
		initLocalView();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v==Btn_Right)
		{
			if(res!=null)
			{
				Intent in = new Intent(mContext, DothisNormalCheckActivity.class);
				BaseMapObject map = new BaseMapObject();
				map.put("ID", res.get("ID"));
				map.put("PatorlItemCategoryName",  res.get("PatorlItemCategoryName"));
				map.put("PatorlItemName",  res.get("PatorlItemName"));
				startActivity(in.putExtra("item", map));
			}
		}
	}

	private void getDetailInfo()
	{
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				res = (HashMap<String, Object>) result;
				text_Category.setText("巡查分类："+res.get("PatorlItemCategoryName")+"");
				text_Project.setText("巡查项："+res.get("PatorlItemName")+"");
				text_isSunmit.setText("是否上报："+((res.get("IsSubmit")+"").equals("true")?"已上报":"未上报"));
				text_desCription.setText(res.get("HandleDescription")+"");
				ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl+res.get("FrontPicture")+"", img_Front);
				if((res.get("AfterPicture")+"").equals("null"))
				{
					Btn_Right.setVisibility(View.VISIBLE);
					linear_Remark.setVisibility(View.GONE);
				}
				else
				{
					Btn_Right.setVisibility(View.INVISIBLE);
					linear_Remark.setVisibility(View.VISIBLE);
					text_remark.setText(res.get("Remark")+"");
					ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl+res.get("AfterPicture")+"", img_After);
				}
				
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.GetPatorlRecordDetail, id));
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_checkinfo, menu);
		return true;
	}

	
	
	
}
