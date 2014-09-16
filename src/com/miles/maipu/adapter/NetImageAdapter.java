package com.miles.maipu.adapter;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.miles.maipu.luzheng.BigPicActivity;
import com.miles.maipu.luzheng.R;
import com.miles.maipu.util.FileUtils;

public class NetImageAdapter extends BaseAdapter
{

	private Context mContext; // 上下文对象
	private List<String> imageUrls; // 图片地址list
	private HashMap<String, SoftReference<Bitmap>> imageCache;

	// 构造方法
	public NetImageAdapter(Context context, List<String> imgarr, HashMap<String, SoftReference<Bitmap>> cache)
	{
		this.mContext = context;
		this.imageUrls = imgarr;
		this.imageCache = cache;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		Bitmap image = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null); // 实例化convertView
			Gallery.LayoutParams params = new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.FILL_PARENT);
			convertView.setLayoutParams(params);
			SoftReference<Bitmap> softimg = imageCache.get(imageUrls.get(position % imageUrls.size()));
			image = softimg == null ? null : softimg.get(); // 从缓存中读取图片

			if (image == null)
			{
				// 当缓存中没有要使用的图片时，先显示默认的图片
				image = imageCache.get("background_non_load").get();
				// 异步加载图片
				LoadImageTask task = new LoadImageTask(convertView);
				task.execute(imageUrls.get(position));
			}

			// else if ("New_Youhuijuan_Activity".equals(who))
			// {
			// image = ((NC_TuansListActivity)
			// mContext).imagesCache.get(imageUrls.get(position %
			// imageUrls.size())); // 从缓存中读取图片
			// if (image == null)
			// {
			// // 当缓存中没有要使用的图片时，先显示默认的图片
			// image = ((NC_TuansListActivity)
			// mContext).imagesCache.get("background_non_load");
			// // 异步加载图片
			// LoadImageTask task = new LoadImageTask(convertView);
			// task.execute(imageUrls.get(position % imageUrls.size()));
			// }
			// }
			convertView.setTag(image);

		} else
		{
			image = (Bitmap) convertView.getTag();
		}
		ImageView imageView = new ImageView(mContext);
		if(image!=null)
		{
			int hei = image.getHeight();
			int wid = image.getWidth();
			imageView.setImageBitmap(image);
			
			if (hei > wid)
			{
				imageView.setLayoutParams(new Gallery.LayoutParams(600, 900));
			} else
			{
				imageView.setLayoutParams(new Gallery.LayoutParams(900, 600));
			}
		}
		else
		{
			imageView.setLayoutParams(new Gallery.LayoutParams(600, 900));
			imageView.setImageResource(R.drawable.emptyphoto);
		}
		
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setBackgroundResource(R.drawable.biankuang);
		imageView.setPadding(5, 5, 5, 5);
		// 设置Gallery组件的背景风格
		// imageView.setBackgroundResource(mGalleryItemBackground);

		// imageView.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// BigPicActivity.bitmap = imageCache.get(imageUrls.get(position %
		// imageUrls.size()));
		// mContext.startActivity(new Intent(mContext, BigPicActivity.class));
		// }
		// });
		//
		return imageView;

	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			try
			{
				switch (msg.what)
				{
				case 0:
				{
					NetImageAdapter.this.notifyDataSetChanged();
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e)
			{
			}
		}
	};

	// 加载图片的异步任务
	class LoadImageTask extends AsyncTask<String, Void, Bitmap>
	{
		private View resultView;

		LoadImageTask(View resultView)
		{
			this.resultView = resultView;
		}

		// doInBackground完成后才会被调用
		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			// 调用setTag保存图片以便于自动更新图片
			resultView.setTag(bitmap);
		}

		// 从网上下载图片
		@Override
		protected Bitmap doInBackground(String... params)
		{
			Bitmap  image = null;
			FileUtils ftools = new FileUtils();
			Bitmap img = null;

			try
			{
				// new URL对象 把网址传入
				URL url = new URL(params[0]);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(url.openStream(), null, options);

				int width = options.outWidth, height = options.outHeight;

				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = 0;
				try
				{
					img = BitmapFactory.decodeStream(url.openStream(), null, opt);
				} catch (OutOfMemoryError e)
				{
					image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.emptyphoto);
				}

//				imageReference = new SoftReference<Bitmap>(img); // 有OOM现象
				// // FileUtils.saveMyBitmap(params[1], image);
				// img = null;
				imageCache.put(params[0], new SoftReference<Bitmap>(img));

				// // 把下载好的图片保存到缓存中
				Message m = new Message();
				m.what = 0;
				mHandler.sendMessage(m);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return img;
		}
	}
}
