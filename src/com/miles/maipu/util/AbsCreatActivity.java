package com.miles.maipu.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.miles.maipu.adapter.LocalImageAdapter;
import com.miles.maipu.luzheng.BigPicActivity;
import com.miles.maipu.luzheng.R;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.HttpPostUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class AbsCreatActivity extends AbsBaseActivity
{
	public Uri fileUri;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public LinearLayout gallery_Linear;
	public LatLng tarlatlng = null;
	public static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	public BDLocation myLocation = null;

	public abstract void UploadData();

//	public Bitmap localimg = null;
//	public String localpath = null;
//	public String netUrl = null;
	public UGallery gallery;
	public LocalImageAdapter imageAdapter;
	public EditText edit_UnitNum;
	public TextView text_unit;
	private int uppicount = 0;
	public List<GalleryData> bitlist = new Vector<GalleryData>();
//	SoftReference<Bitmap> softbit = null;
	public void uplaodPic()
	{
		
		uppicount++;
		new AsyncTask<String, String, String>()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected String doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				for (int i = 0; i < bitlist.size() - 1; i++)
				{
					GalleryData d = bitlist.get(i);
					if(d!=null)
					{
						Log.v("base64", "startupdata");
						Log.v("base64", "startencode");
						String imgbase = ImageUtil.Bitmap2StrByBase64(d.getBitdata());
						Log.v("base64", "endcode");
						// FileUtils.getFile(imgbase.getBytes(),
						// OverAllData.SDCardRoot,
						// UnixTime.getStrCurrentUnixTime()+"img.txt");
	
						Map<String, Object> sendmap = new HashMap<String, Object>();
						sendmap.put("FileName", UnixTime.getImgNameTime() + ".jpg"); // 图片名称
						sendmap.put("FileString", imgbase); // 图片base64字符换
						HashMap<String, Object> res = ((HashMap<String, Object>) HttpPostUtil.httpUrlConnection(ApiCode.SaveFile, JSONUtil.toJson(sendmap)));
						if (res.get("IsSuccess") != null && res.get("IsSuccess").toString().equals("true"))
						{
							d.setUrlPath(res.get("Message").toString());
						} else
						{
							return null;
						}
					}
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				return "ok";
			}

			@Override
			protected void onPostExecute(String result)
			{
				// TODO Auto-generated method stub
				
				if(bitlist.size()<2)
				{
					hideProgressDlg();
					Toast.makeText(mContext, "请拍照后再上传", 0).show();
					return;
				}
				if (result != null && result.equals("ok"))
				{
					Log.v("base64", "endupdata");
					UploadData();
				} 
				else
				{
					if(uppicount>2)
					{
						uppicount=0;
						hideProgressDlg();
						Toast.makeText(mContext, "图片上传失败", 0).show();
					}
					else
					{
						uplaodPic();
					}
				}
				
				super.onPostExecute(result);
			}

		}.execute("");

		// new SendDataTask()
		// {
		// @Override
		// protected Object doInBackground(ParamData... parm)
		// {
		// // TODO Auto-generated method stub
		// if(localimg==null)
		// {
		// //装载图片并压缩
		// localimg =
		// ImageUtil.compressImage((BitmapFactory.decodeFile(localpath)));
		// }
		// String imgbase = ImageUtil.Bitmap2StrByBase64(localimg);
		// // FileUtils.getFile(imgbase.getBytes(), OverAllData.SDCardRoot,
		// UnixTime.getStrCurrentUnixTime()+"img.txt");
		//
		// Map<String, Object> sendmap = new HashMap<String, Object>();
		// sendmap.put("FileName",
		// "img"+UnixTime.getStrCurrentUnixTime()+".jpg"); //图片名称
		// sendmap.put("FileString", imgbase); //图片base64字符换
		//
		// return super.doInBackground(new ParamData(ApiCode.SaveFile,
		// JSONUtil.toJson(sendmap)));
		// }
		//
		// @Override
		// protected void onPostExecute(Object result)
		// {
		// // TODO Auto-generated method stub
		//
		// HashMap<String, Object> res = (HashMap<String, Object>) result;
		// System.out.println(res.toString());
		// if(res.get("IsSuccess")!=null&&res.get("IsSuccess").toString().equals("true"))
		// {
		// netUrl = res.get("Message").toString();
		// UploadData();
		// }
		// else
		// {
		// hideProgressDlg();
		// Toast.makeText(mContext,
		// res.get("msg")!=null?res.get("msg")+"":"图片上传失败", 0).show();
		// return;
		// }
		//
		// super.onPostExecute(result);
		// }
		// }.execute();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		InitLocation();
		super.onCreate(savedInstanceState);
	}

	private void InitLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		int span = 1000;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		mLocationClient.stop();
		for(GalleryData data :bitlist)
		{
			Bitmap bdata = data.getBitdata();
			if(bdata!=null&&!bdata.isRecycled())
			{
				bdata.recycle();
				bdata = null;
			}
		}
		System.gc();
		super.onDestroy();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// Receive Location
			myLocation = location;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation)
			{
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation)
			{
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			// Log.i("BaiduLocationApiDem", sb.toString());
		}
	}

	public void ComposGallery(UGallery gallery)
	{
		bitlist.add(new GalleryData(BitmapFactory.decodeResource(getResources(), R.drawable.emptyphoto), ""));
		imageAdapter = new LocalImageAdapter(mContext, bitlist);
		gallery_Linear = (LinearLayout) findViewById(R.id.grally_llinar);

		gallery.setAdapter(imageAdapter);
		gallery.setSpacing(50);

		gallery.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				// Toast.makeText(mContext, bitlist.get(arg2).getPath(),
				// 0).show();
				try
				{
					if(arg2==3)
					{
						Toast.makeText(mContext, "目前最多允许拍摄三张照片！", 0).show();
						return;
					}
					if(tarlatlng!=null && DistanceUtil.getDistance(tarlatlng, new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))>1000)
					{
						Toast.makeText(mContext, "当前位置距离处理点较远，无法拍照！", 0).show();
						return;
					}
					if (arg2 == bitlist.size() - 1)
					{
						goCamera();
					} else
					{
						// BigPicActivity.bitmap = bitlist.get(arg2).getBitdata();
						List<Bitmap> blist = new Vector<Bitmap>();
						for (GalleryData d : bitlist)
						{
							blist.add(d.getBitdata());
						}
						BigPicActivity.bitlist = blist;
						startActivity(new Intent(mContext, BigPicActivity.class).putExtra("index", arg2));
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					Toast.makeText(mContext, "位置信息暂未获取到，请稍后再试...", 0).show();
					return;
				}
			}
		});

	}

	public void goCamera()
	{
		// goCamearNormal();
		goCameargetBigPhoto();
	}

	public GalleryData getCamera(String name, int requestCode, int resultCode, Intent data)
	{
		// return cameraResultNormal(name, requestCode, resultCode, data);

		return cameraForresult(requestCode, resultCode, data);
	}

	public void compostPoint()
	{
		final ImageView[] imgBottem = new ImageView[bitlist.size()];
		gallery_Linear.removeAllViews();
		for (int i = 0; i < bitlist.size(); i++)
		{
			imgBottem[i] = new ImageView(mContext);
			imgBottem[i].setImageResource(R.drawable.selcetno);
			imgBottem[i].setId(110 + i); // 注意这点 设置id
			imgBottem[i].setScaleType(ScaleType.FIT_XY);
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			if (gallery_Linear != null)
				gallery_Linear.addView(imgBottem[i], lp1);
		}
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

	public GalleryData cameraResultNormal(String name, int requestCode, int resultCode, Intent data)
	{
		if (requestCode == RESULT_OK)
		{
			return null;
		}
		Bitmap img = null;
		switch (requestCode)
		{
		case 1001:
			try
			{
				img = (Bitmap) data.getExtras().get("data");
				img = ImageUtil.addtext2Image(img); // 加水印
			} catch (Exception e)
			{
				// TODO: handle exception
				return null;
			}
		}
		// havePic = true;
		String SaveFilepath = OverAllData.SDCardRoot + name + ".png"; // 填充相片路径
		/** 相片保存 */
		Bitmap2Bytes(img, SaveFilepath);

		return new GalleryData(img, SaveFilepath);

	}

	//保存照片
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

//	public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
//	{
//
//		Matrix m = new Matrix();
//		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//		float targetX, targetY;
//		if (orientationDegree == 90)
//		{
//			targetX = bm.getHeight();
//			targetY = 0;
//		} else
//		{
//			targetX = bm.getHeight();
//			targetY = bm.getWidth();
//		}
//
//		final float[] values = new float[9];
//		m.getValues(values);
//
//		float x1 = values[Matrix.MTRANS_X];
//		float y1 = values[Matrix.MTRANS_Y];
//
//		m.postTranslate(targetX - x1, targetY - y1);
//
//		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
//		Paint paint = new Paint();
//		Canvas canvas = new Canvas(bm1);
//		canvas.drawBitmap(bm, m, paint);
//
//		return bm1;
//	}

	//计算旋转角度
	public int exifinfo(String path)
	{
		int rotate = 0;
		try
		{
			ExifInterface exifInterface = new ExifInterface(path);
			int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

			switch (result)
			{
			
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case 1:
				rotate = 360;
			default:
				break;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return rotate;
	}

	public GalleryData cameraForresult(int requestCode, int resultCode, Intent data)
	{
		// 如果是拍照
		if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode)
		{
			GalleryData d = new GalleryData();
			if (RESULT_OK == resultCode)
			{
				// Check if the result includes a thumbnail Bitmap
				if (data != null)
				{
					// 没有指定特定存储路径的时候
					if (data.hasExtra("data"))
					{
						Bitmap thumbnail = data.getParcelableExtra("data");
						thumbnail = ImageUtil.addtext2Image(thumbnail); // 加水印
						d.setBitdata(thumbnail);
					}
				} else
				{

					BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
					factoryOptions.inJustDecodeBounds = false;
					factoryOptions.inSampleSize = 6;//压缩为原来的四分之一，防止OOM
					factoryOptions.inPurgeable = true;
					Bitmap bittmp = null;
					 try
					 {
						 bittmp = BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
					 }
					 catch(OutOfMemoryError err)
					 {
						 err.printStackTrace();
						 return null;
					 }
					int rotate = exifinfo(fileUri.getPath());
					if (rotate > 0)
					{
						Matrix matrix = new Matrix();
						matrix.setRotate(rotate);
						try
						{
							bittmp =Bitmap.createBitmap(bittmp, 0, 0, factoryOptions.outWidth, factoryOptions.outHeight, matrix, true);
						}
						catch(OutOfMemoryError e)
						{
							e.printStackTrace();
							bittmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.emptyphoto);
						}
						d.setBitdata(ImageUtil.addtext2Image(bittmp));
						d.setPath(fileUri.getPath());
						if(bittmp!=null&&!bittmp.isRecycled())
						{
							bittmp.recycle();
							bittmp=null;
							System.gc();
						}
					}

					
					return d;
					// img = ;
					//
					// img_Photo.setImageBitmap(img);
					// return fileUri.getPath();
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

	public void goCameargetBigPhoto()
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
