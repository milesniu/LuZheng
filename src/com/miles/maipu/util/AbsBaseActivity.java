package com.miles.maipu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.miles.maipu.adapter.NetImageAdapter;
import com.miles.maipu.luzheng.BigPicActivity;
import com.miles.maipu.luzheng.R;
import com.miles.maipu.net.NetApiUtil;

public abstract class AbsBaseActivity extends Activity implements OnClickListener
{
	public Context mContext = this;
	public View LayoutTitle;
	public Button Btn_Left;
	public Button Btn_Right;
	public TextView text_title;
	public ListView List_Content;
	public ProgressDialog pdialog;
	public static String title = "常州公路";
	public static String message = "正在努力加载···";
	
	
	
	/***************************************** 数据分页 ******************************************/
	public View moreView; // 加载更多页面
	public int lastItem;
	public int count;
	public List<HashMap<String, Object>> moredata_list = null;
	
	public int pagesize = 7;
	public int currentpage = 1;
//	public LinearLayout gallery_Linear;
//	public LinearLayout gallery_Linearafter;
	

	public void showprogressdialog()
	{
		if (pdialog == null || !pdialog.isShowing())
		{
			pdialog = ProgressDialog.show(this, title, message);
			pdialog.setIcon(R.drawable.ic_launcher);
			pdialog.setCancelable(true);
		}
	}

	public void hideProgressDlg()
	{
		if (pdialog != null && pdialog.isShowing())
		{
			pdialog.dismiss();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		moreView = getLayoutInflater().inflate(R.layout.load, null);
		super.onCreate(savedInstanceState);
	}
	
	public void ComposeImg(UGallery gallery,LinearLayout gallerylin, String[] path, final HashMap<String, Bitmap> imagesCache)
	{
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.emptyphoto);
		imagesCache.put("background_non_load", image); // 设置缓存中默认的图片
//		gallery_Linear = (LinearLayout) findViewById(R.id.grally_llinar);
//		gallery_Linearafter = (LinearLayout)findViewById(R.id.grally_llinarafter);
		final ImageView[] imgBottem = new ImageView[path.length];
//		for (int i = 0; i < path.length; i++)
//		{
//			urls.add(OverallData.IMGWebSite + ((Map<String, Object>) ads_list.get(i)).get("imgurl"));
//			
//		}

		final ArrayList<String> urls = new ArrayList<String>(); // 图片地址List
		for (int i = 0; i < path.length; i++)
		{
			// "http://www.yemixilu.com/uploads/newupdate20140116/ads/index2.jpg"
			urls.add(NetApiUtil.ImgBaseUrl+path[i]);
			imgBottem[i] = new ImageView(mContext);
			imgBottem[i].setImageResource(R.drawable.selcetno);
			imgBottem[i].setId(110 + i); // 注意这点 设置id
			imgBottem[i].setScaleType(ScaleType.FIT_XY);
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			if(gallerylin!=null)
				gallerylin.addView(imgBottem[i], lp1);
		}

		NetImageAdapter imageAdapter = new NetImageAdapter(mContext, urls, imagesCache);
		gallery.setAdapter(imageAdapter);
		gallery.setSpacing(50);
		gallery.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
//				BigPicActivity.bitmap = imagesCache.get(urls.get(position % urls.size()));
				List<Bitmap> blist = new Vector<Bitmap>();
				for(int i=0;i<urls.size();i++)
				{
					blist.add(imagesCache.get(urls.get(i % urls.size())));
				}
				BigPicActivity.bitlist = blist;
				startActivity(new Intent(mContext, BigPicActivity.class).putExtra("index", position));
				
			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO Auto-generated method stub
				for (ImageView img : imgBottem)
				{
					img.setImageResource(R.drawable.selcetno);
				}
				imgBottem[position].setImageResource(R.drawable.selectyes);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (v == Btn_Left)
		{
			this.finish();
			return;
		}
	}

}
