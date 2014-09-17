package com.miles.maipu.adapter;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.miles.maipu.luzheng.R;
import com.miles.maipu.util.GalleryData;

public class LocalImageAdapter extends BaseAdapter
{

	private Context mContext; // 上下文对象
//	private List<String> imageUrls; // 图片地址list
//	private List<String> imageids; // 图片地址list
	private List<GalleryData> images;
//	private HashMap<String, Bitmap> imageCache;

	// 构造方法
	public LocalImageAdapter(Context context, List<GalleryData> imgarr)
	{
		this.mContext = context;
		this.images = imgarr;
	}
	
	// 构造方法
		public LocalImageAdapter(Context context, List<SoftReference<GalleryData>> imgarr,boolean issoft)
		{
			this.mContext = context;
			images = new Vector<GalleryData>();
			for(SoftReference<GalleryData> so :imgarr)
			{
				images.add(so.get());
			}
		}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return images.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Bitmap image = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null); // 实例化convertView
			Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.FILL_PARENT);
			convertView.setLayoutParams(params);
			image = images.get(position).getBitdata();
			convertView.setTag(image);

		} else
		{
			image = (Bitmap) convertView.getTag();
		}
		ImageView imageView = new ImageView(mContext);
		if (image != null)
		{
			int hei = image.getHeight();
			int wid = image.getWidth();

			imageView.setImageBitmap(image);
			imageView.setBackgroundResource(R.drawable.biankuang);
			imageView.setPadding(2, 2, 2, 2);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if (hei > wid)
			{
				imageView.setLayoutParams(new Gallery.LayoutParams(600, 900));
			} else
			{
				imageView.setLayoutParams(new Gallery.LayoutParams(900, 600));
			}
		} else
		{
			imageView.setLayoutParams(new Gallery.LayoutParams(600, 900));
			imageView.setImageResource(R.drawable.emptyphoto);
		}
		
		// 设置Gallery组件的背景风格
		// imageView.setBackgroundResource(mGalleryItemBackground);
		return imageView;

	}
}
