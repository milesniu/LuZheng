package com.miles.maipu.adapter;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.miles.maipu.luzheng.R;
import com.miles.maipu.util.FileUtils;
import com.miles.maipu.util.GalleryData;
import com.miles.maipu.util.OverAllData;

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
		imageView.setImageBitmap(image);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(400,600));
		// 设置Gallery组件的背景风格
		// imageView.setBackgroundResource(mGalleryItemBackground);
		return imageView;

	}
}