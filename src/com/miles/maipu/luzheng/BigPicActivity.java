package com.miles.maipu.luzheng;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.miles.maipu.net.NetApiUtil;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.WebImageBuilder;
import com.miles.maipu.view.Image;
import com.miles.maipu.view.MyPhotoView;
import com.miles.maipu.view.MyViewPager;

public class BigPicActivity extends AbsBaseActivity
{
	// public static Bitmap bitmap = null;
	public static List<Bitmap> bitlist = new Vector<Bitmap>();
	private MyViewPager pager;
	private List<Image> images;
	private int index = -1;
	private String pathStr[] = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		index = getIntent().getIntExtra("index", 0);
		String strpath = getIntent().getStringExtra("path");
		pathStr = strpath==null?null:(strpath.split("\\|"));
		pager = (MyViewPager) findViewById(R.id.my_pager);
		images = new ArrayList<Image>();
		if(index==-1)
		{
			bitlist.clear();
			showprogressdialog();
		}
		new getImg().execute(pathStr);
		
	}
	
	

	class getImg extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			if(params!=null)
			{
				for(String s: params)
				{
					bitlist.add(new WebImageBuilder().returnBitMap(NetApiUtil.ImgBaseUrl+s, WebImageBuilder.BIGSIZE));
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			hideProgressDlg();
			init();
			pager.setAdapter(new ImageAdapter(images));
			pager.setCurrentItem(index);
			super.onPostExecute(result);
		}
		
		
		
	}
	
	private void init()
	{
		for (int i = 0; i < bitlist.size(); i++)
		{
			// BitmapDrawable bd = (BitmapDrawable)
			// getResources().getDrawable(sDrawables[i]);
			Bitmap bm = bitlist.get(i);
			Image image = new Image();
			image.setBitmap(bm);
			images.add(image);
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class ImageAdapter extends PagerAdapter
	{
		private List<Image> list;

		public ImageAdapter(List<Image> list)
		{
			this.list = list;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position)
		{
			Image image = list.get(position);

			MyPhotoView photoView = null;
			if (null != image)
			{
				System.out.println("context:" + container.getContext());
				photoView = new MyPhotoView(container.getContext());
				photoView.setImage(image.getBitmap());
			}
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == object;
		}

		@Override
		public int getCount()
		{
			return images.size();
		}

	}

}
