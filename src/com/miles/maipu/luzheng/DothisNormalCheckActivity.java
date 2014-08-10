package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.AbsCreatActivity;
import com.miles.maipu.util.DemoApplication;
import com.miles.maipu.util.ImageUtil;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.UGallery;
import com.miles.maipu.util.UnixTime;

public class DothisNormalCheckActivity extends AbsCreatActivity
{
	private HashMap<String, Object> res = null;
	private TextView text_Time;
	private TextView text_Category;
	private TextView text_Project;
	private EditText edit_Descript;
	private ImageView img_photo;
//	private Bitmap bitmap;
//	private String imgPath = "";
//	private String uploadurl="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
			super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dothis_normal_check);
		res = (HashMap<String, Object>) getIntent().getSerializableExtra("item");
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dothis_normal_check, menu);
		return true;
	}

	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		List_Content = (ListView) findViewById(R.id.list_content);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		text_Time = (TextView)findViewById(R.id.text_time);
		text_Category = (TextView)findViewById(R.id.text_category);
		text_Project = (TextView)findViewById(R.id.text_project);
		edit_Descript = (EditText)findViewById(R.id.edit_dothisdescript);
		text_Time.setText(UnixTime.getStrCurrentSimleTime());//res.get("RecordTime")+"");
		text_Category.setText(res.get("PatorlItemCategoryName")+"");
		text_Project.setText(res.get("PatorlItemName")+"");
		img_photo = (ImageView)findViewById(R.id.img_photo);
		text_title.setText("巡查处理");
		Btn_Right.setBackgroundResource(R.drawable.btsure);
		img_photo.setOnClickListener(this);
		gallery = (UGallery)findViewById(R.id.gallery_photo);
		ComposGallery(gallery);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId())
		{
		case R.id.bt_right:
			String desc = edit_Descript.getText().toString();
			if(desc.equals(""))
			{
				Toast.makeText(mContext, "请输入处理信息", 0).show();
				return;
			}
			else
			{
				showprogressdialog();
				uplaodPic();
			}
			break;
		case R.id.img_photo:
			goCamera();
			break;
		}
	}

	

//	private void uplaodPic()
//	{
//		showprogressdialog();
//		new SendDataTask()
//		{
//			@Override
//			protected Object doInBackground(ParamData... parm)
//			{
//				// TODO Auto-generated method stub
//				if(bitmap==null)
//				{
//					//装载图片并压缩
//					bitmap = ImageUtil.compressImage((BitmapFactory.decodeFile(imgPath)));
//				}
//				String imgbase = ImageUtil.Bitmap2StrByBase64(bitmap);
////				FileUtils.getFile(imgbase.getBytes(), OverAllData.SDCardRoot, UnixTime.getStrCurrentUnixTime()+"img.txt");
//				
//				Map<String, Object> sendmap = new HashMap<String, Object>();
//				sendmap.put("FileName", "img"+UnixTime.getStrCurrentUnixTime()+".jpg");		//图片名称
//				sendmap.put("FileString", imgbase);			//图片base64字符换
//				
//				return super.doInBackground(new ParamData(ApiCode.SaveFile, JSONUtil.toJson(sendmap)));
//			}
//
//			@Override
//			protected void onPostExecute(Object result)
//			{
//				// TODO Auto-generated method stub
//				
//				HashMap<String, Object> res = (HashMap<String, Object>) result;
//				System.out.println(res.toString());
//				if(res.get("IsSuccess")!=null&&res.get("IsSuccess").toString().equals("true"))
//				{
//					uploadurl = res.get("Message").toString();
//					uploadDothisData();
//				}
//				else
//				{
//					hideProgressDlg();
//					Toast.makeText(mContext, res.get("msg")!=null?res.get("msg")+"":"图片上传失败", 0).show();
//					return;
//				}
//				
//				super.onPostExecute(result);
//			}
//		}.execute();
//	}
//	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		localpath = getCamera(img_photo, localimg, requestCode, resultCode, data);
		bitlist.add(bitlist.size()-1,getCamera(bitlist.size()+"", requestCode, resultCode, data));
		imageAdapter.notifyDataSetChanged();
	}

	@Override
	public void UploadData()
	{
		// TODO Auto-generated method stub
		String ID =res.get("ID")+"";
		String Remark = edit_Descript.getText().toString();
		String LatitudeLongitude = DemoApplication.myLocation.getLongitude()+","+DemoApplication.myLocation.getLatitude();
		
		Map<String, Object> senddata = new HashMap<String, Object>();
		senddata.put("ID", ID);
		
		String pictrues = "";
		for(int i=0;i<bitlist.size()-1;i++)
		{
			pictrues=pictrues+(bitlist.get(i).getUrlPath()+"|");
		}
		pictrues = pictrues.substring(0, pictrues.length()-1);
		
		senddata.put("AfterPicture", pictrues);
		senddata.put("Remark", Remark);
		senddata.put("LatitudeLongitude", LatitudeLongitude);
	
		
		new SendDataTask()
		{	
			@SuppressWarnings("unchecked")
			@SuppressLint("DefaultLocale")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				HashMap<String, Object> res = (HashMap<String, Object>) result;
				if(res.get("IsSuccess").toString().toUpperCase().equals("TRUE"))
				{
					Toast.makeText(mContext, "巡查处理成功",0).show();
					DothisNormalCheckActivity.this.finish();
				}
				else
				{
					Toast.makeText(mContext, res.get("Message").toString(), 0).show();
					return;
				}
				
				
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.UpdatePatorlRecordDetail, JSONUtil.toJson(senddata)));
	
	}	

}
