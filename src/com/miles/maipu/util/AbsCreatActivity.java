package com.miles.maipu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;

public abstract class AbsCreatActivity extends AbsBaseActivity
{
	public Uri fileUri;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	public static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	public abstract void UploadData();
	
	public Bitmap localimg = null;
	public String localpath = null;
	public String netUrl = null;
	public void uplaodPic()
	{
			
		new SendDataTask()
		{
			@Override
			protected Object doInBackground(ParamData... parm)
			{
				// TODO Auto-generated method stub
				if(localimg==null)
				{
					//装载图片并压缩
					localimg = ImageUtil.compressImage((BitmapFactory.decodeFile(localpath)));
				}
				String imgbase = ImageUtil.Bitmap2StrByBase64(localimg);
//				FileUtils.getFile(imgbase.getBytes(), OverAllData.SDCardRoot, UnixTime.getStrCurrentUnixTime()+"img.txt");
				
				Map<String, Object> sendmap = new HashMap<String, Object>();
				sendmap.put("FileName", "img"+UnixTime.getStrCurrentUnixTime()+".jpg");		//图片名称
				sendmap.put("FileString", imgbase);			//图片base64字符换
				
				return super.doInBackground(new ParamData(ApiCode.SaveFile, JSONUtil.toJson(sendmap)));
			}

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				System.out.println(res.toString());
				if(res.get("IsSuccess")!=null&&res.get("IsSuccess").toString().equals("true"))
				{
					netUrl = res.get("Message").toString();
					UploadData();
				}
				else
				{
					hideProgressDlg();
					Toast.makeText(mContext, res.get("msg")!=null?res.get("msg")+"":"图片上传失败", 0).show();
					return;
				}
				
				super.onPostExecute(result);
			}
		}.execute();
	}
	
	
	public void goCamera()
	{
		goCamearNormal();
	}

	public String getCamera(ImageView img_Photo, Bitmap img, int requestCode, int resultCode, Intent data)
	{
		return cameraResultNormal(img_Photo, img, requestCode, resultCode, data);
	}

	public String cameraResultNormal(ImageView img_Photo, Bitmap img, int requestCode, int resultCode, Intent data)
	{
		if (requestCode == RESULT_OK)
		{
			return "";
		}
		switch (requestCode)
		{
		case 1001:
			try
			{
				img = (Bitmap) data.getExtras().get("data");
				img_Photo.setImageBitmap(img);
			} catch (Exception e)
			{
				// TODO: handle exception
				return null;
			}
		}
		// havePic = true;
		String SaveFilepath = OverAllData.SDCardRoot + "luzheng.png"; // 填充相片路径
		/** 相片保存 */
		Bitmap2Bytes(img, SaveFilepath);

		return SaveFilepath;

	}

	public void Bitmap2Bytes(Bitmap bm, String path)
	{
		File file = new File(path);
		FileOutputStream baos = null;
		try
		{
			baos = new FileOutputStream(file);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		try
		{
			baos.flush();
			baos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public String cameraForresult(ImageView img_Photo, Bitmap img, int requestCode, int resultCode, Intent data)
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
					// Toast.makeText(this, "Image saved to:\n" +
					// data.getData(), Toast.LENGTH_LONG).show();
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

					img = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

					img_Photo.setImageBitmap(img);
					return fileUri.getPath();
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
		return null;
	}

	public void goCameargetPhoto()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(1);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	}

	public void goCamearNormal()
	{
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, 1001);
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

	
}