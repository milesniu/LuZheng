package com.miles.maipu.util;

import java.io.File;
import java.text.SimpleDateFormat;

import com.miles.maipu.luzheng.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbsBaseActivity extends Activity implements OnClickListener
{
	public Context mContext = this;
	public View LayoutTitle;
	public Button Btn_Left;
	public Button Btn_Right;
	public TextView text_title;
	public ListView List_Content;
	public Uri fileUri;

	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


	public static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	public void cameraForresult(ImageView img_Photo,int requestCode, int resultCode, Intent data)
	{
		// 如果是拍照
				if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode)
				{
					if (RESULT_OK == resultCode)
					{
						// Check if the result includes a thumbnail Bitmap
						if (data != null)
						{
							// 没有指定特定存储路径的时候
							// 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
							// Image captured and saved to fileUri specified in the
							// Intent
//							Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
							if (data.hasExtra("data"))
							{
								Bitmap thumbnail = data.getParcelableExtra("data");
								img_Photo.setImageBitmap(thumbnail);
							}
						} else
						{

							// If there is no thumbnail image data, the image
							// will have been stored in the target output URI.
							// Resize the full image to fit in out image view.
							int width = img_Photo.getWidth();
							int height = img_Photo.getHeight();
							BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
							factoryOptions.inJustDecodeBounds = true;
							BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
							int imageWidth = factoryOptions.outWidth;
							int imageHeight = factoryOptions.outHeight;
							// Determine how much to scale down the image
							int scaleFactor = Math.min(imageWidth / width, imageHeight / height);
							// Decode the image file into a Bitmap sized to fill the
							// View
							factoryOptions.inJustDecodeBounds = false;
							factoryOptions.inSampleSize = scaleFactor;
							factoryOptions.inPurgeable = true;

							Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

							img_Photo.setImageBitmap(bitmap);
						}
					} else if (resultCode == RESULT_CANCELED)
					{
						// User cancelled the image capture
					} else
					{
						// Image capture failed, advise user
					}
				}

				// 如果是录像
				if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
				{

					if (resultCode == RESULT_OK)
					{
					} else if (resultCode == RESULT_CANCELED)
					{
						// User cancelled the video capture
					} else
					{
						// Video capture failed, advise user
					}
				}
	}
	
	public void goCameargetPhoto()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(1);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type)
	{
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = null;
		try
		{
			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.
			mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

			// Log.d(LOG_TAG, "Successfully created mediaStorageDir: "
			// + mediaStorageDir);

		} catch (Exception e)
		{
			e.printStackTrace();
			// Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
			// + mediaStorageDir);
		}

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				// 在SD卡上创建文件夹需要权限：
				// <uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				// Log.d(LOG_TAG,
				// "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
		File mediaFile;
		if (type == 1)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == 2)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else
		{
			return null;
		}

		return mediaFile;
	}
	public void initView()
	{
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button)findViewById(R.id.bt_right);
		text_title = (TextView)findViewById(R.id.title_text);
		List_Content = (ListView)findViewById(R.id.list_content);
		if(Btn_Left!=null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if(Btn_Right!=null)
		{
			Btn_Right.setOnClickListener(this);
		}
	}

	
	public void goActivity(Class<?> cls,String... parm)
	{
		Intent intent = new Intent(mContext, cls);
		for(int i=0;i<parm.length;i++)
		{
			intent.putExtra("item"+i, parm[i]);
		}
		this.startActivity(new Intent(mContext, cls));
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		initView();
		super.onStart();
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v == Btn_Left)
		{
			this.finish();
		}
	}
	
	
	
}
