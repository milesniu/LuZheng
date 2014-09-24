package com.miles.maipu.luzheng;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.BaseMapObject;
import com.miles.maipu.util.UGallery;

public class NormalCheckinfoActivity extends AbsBaseActivity
{

	private TextView text_Category;
	private TextView text_Project;
//	private TextView text_isSunmit;
	private TextView text_desCription;
	private String id;
	private boolean isorg = false;
	private ImageView img_Front;
	private ImageView img_After;
	
	private UGallery gallery_Front;
	private UGallery gallery_After;
	
	private LinearLayout linear_Remark;
	
	private TextView text_remark;
	private LatLng latlng = null;
	public static boolean isneedrefresh = false;
	HashMap<String, Object> res = null;
	private HashMap<String, SoftReference<Bitmap>> FrontimagesCache = new HashMap<String, SoftReference<Bitmap>>(); // 图片缓存
	private HashMap<String, SoftReference<Bitmap>> AfterimagesCache = new HashMap<String, SoftReference<Bitmap>>(); // 图片缓存
	private LinearLayout gallery_Linear;
	private LinearLayout gallery_Linearafter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		setContentView(R.layout.activity_normal_checkinfo);
		super.onCreate(savedInstanceState);
		isneedrefresh = true;
		id = getIntent().getStringExtra("id");
		isorg  = getIntent().getBooleanExtra("isorg", false);
		initView();
	}

	
	
	
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		BitmapDrawable bitmapDrawable = (BitmapDrawable) img_Front.getDrawable();
		if(bitmapDrawable.getBitmap().isRecycled())
		{
			bitmapDrawable.getBitmap().recycle();

		}
		
		BitmapDrawable bitmapDrawable2 = (BitmapDrawable) img_After.getDrawable();
		if(bitmapDrawable2.getBitmap().isRecycled())

		{

			bitmapDrawable2.getBitmap().recycle();

		}
		super.onDestroy();
	}

	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		text_title.setText("路政巡查");
		Btn_Right.setBackgroundResource(R.drawable.dothis);
		text_Category = (TextView)findViewById(R.id.text_category);
		text_Project = (TextView)findViewById(R.id.text_project);
//		text_isSunmit = (TextView)findViewById(R.id.text_issubmit);
		text_desCription = (TextView)findViewById(R.id.text_descrption);
		img_Front = (ImageView)findViewById(R.id.img_front);
		linear_Remark = (LinearLayout)findViewById(R.id.linear_remark);
		text_remark = (TextView)findViewById(R.id.text_remark);
		img_After = (ImageView)findViewById(R.id.img_after);
		gallery_Front = (UGallery)findViewById(R.id.gallery_front);
		
		gallery_Linear = (LinearLayout) findViewById(R.id.grally_llinar);
		linear_Remark = (LinearLayout)findViewById(R.id.linear_remark);
		text_remark = (TextView)findViewById(R.id.text_remark);
		gallery_After = (UGallery)findViewById(R.id.gallery_after);
		gallery_Linearafter = (LinearLayout)findViewById(R.id.grally_llinarafter);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		
		
		
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
				in.putExtra("lat", latlng.latitude).putExtra("lng", latlng.longitude);
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
				if(res==null)
				{
					Toast.makeText(mContext, "网络连接失败...", 0).show();
					return;
				}
				text_Category.setText(res.get("PatorlItemCategoryName")+"");
				text_Project.setText(res.get("PatorlItemName")+"");
				String[] strlatlng = res.get("LatitudeLongitude").toString().split(",");
				latlng = new LatLng(Double.parseDouble(strlatlng[1]), Double.parseDouble(strlatlng[0]));
				
				((TextView)findViewById(R.id.text_num)).setText(res.get("Extent")+""+res.get("Unit")+"");
				((TextView)findViewById(R.id.text_time)).setText(res.get("RecordTime")+"");
				((TextView)findViewById(R.id.text_line)).setText(res.get("RoadLine")+"");
				((TextView)findViewById(R.id.text_zhuanghao)).setText(res.get("Mark")+"");
				((TextView)findViewById(R.id.text_lane)).setText(res.get("Lane")+"");
				
//				text_isSunmit.setText("是否上报："+((res.get("IsSubmit")+"").equals("true")?"已上报":"未上报"));
				text_desCription.setText(res.get("HandleDescription")+"");
				
				String[] path = res.get("FrontPicture").toString().split("\\|");
				
				
				ComposeImg(gallery_Front, gallery_Linear,path, FrontimagesCache);
				
				if(isorg)
				{
					Btn_Right.setVisibility(View.INVISIBLE);
				}
				else
				{
					Btn_Right.setVisibility(View.VISIBLE);
				}
//				ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl+res.get("FrontPicture")+"", img_Front);
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
					
					String[] pathf = res.get("AfterPicture").toString().split("\\|");
					
					
					ComposeImg(gallery_After,gallery_Linearafter, pathf, AfterimagesCache);
					
//					ImageUtil.getBitmapAsyn(NetApiUtil.ImgBaseUrl+res.get("AfterPicture")+"", img_After);
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


	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		if(isneedrefresh)
		{
			showprogressdialog();
			getDetailInfo();
		}
		isneedrefresh = false;
		super.onResume();
	}

	
	
	
}
