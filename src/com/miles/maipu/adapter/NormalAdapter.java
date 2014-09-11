package com.miles.maipu.adapter;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.luzheng.BigPicActivity;
import com.miles.maipu.luzheng.NormalCheckActivity;
import com.miles.maipu.luzheng.R;
import com.miles.maipu.luzheng.TaskManagerActivity;
import com.miles.maipu.util.OverAllData;

@SuppressLint({ "InflateParams", "ViewHolder" })
public class NormalAdapter extends BaseAdapter
{

	private List<HashMap<String,Object>> data = null;
	private Context mContex = null;
	private AdapterCode code;
	
	
	public NormalAdapter(Context mContex,List<HashMap<String, Object>> data,AdapterCode c)
	{
		super();
		this.data = data;
		this.mContex = mContex;
		this.code =c;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		LayoutInflater mInflater = LayoutInflater.from(mContex);
		View view = mInflater.inflate(R.layout.listitem_normalcheck, null);
		final HashMap<String, Object> item = data.get(position);
		ImageView img = null;
		switch(code)
		{
		case norMalCheck:
			 view = mInflater.inflate(R.layout.listitem_wxinfo, null);
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine").toString()+" "+item.get("Mark"));
			
			((TextView)view.findViewById(R.id.text_info)).setText(item.get("PatorlItemName")+"");
			((TextView)view.findViewById(R.id.text_status)).setText(item.get("IsHandle").toString().equals("true")?"已处理":"未处理");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("HandleDescription")+"");
			String ntime = (item.get("RecordTime")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(ntime);//.substring(5));//, ntime.length()-3));
			img = ((ImageView)view.findViewById(R.id.image_thumb));
			img.setVisibility(View.VISIBLE);
			
			if (item.get("bitmap") != null)
			{
				img.setImageBitmap((Bitmap) item.get("bitmap"));
//				img.setImageBitmap(((SoftReference<Bitmap>) item.get("bitmap")).get());
			}
			img.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mContex.startActivity(new Intent(mContex, BigPicActivity.class).putExtra("index", -1).putExtra("path", item.get("Picture").toString()));
				}
			});
			
			
			break;
		case taskManger:
			 view = mInflater.inflate(R.layout.listitem_wxinfo, null);
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine")+" "+item.get("Mark"));
			((TextView)view.findViewById(R.id.text_info)).setText(item.get("PatorlItem")+"");
			
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("EventContent")+"");
			((TextView)view.findViewById(R.id.text_status)).setText(item.get("State")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(item.get("AllotedDate")+"");//item.get("State")+"");
			ImageView dcl = (ImageView)view.findViewById(R.id.imageView_dcl);
			dcl.setVisibility(View.VISIBLE);
			img = ((ImageView)view.findViewById(R.id.image_thumb));
			img.setVisibility(View.VISIBLE);
			
			if (item.get("bitmap") != null)
			{
				img.setImageBitmap((Bitmap) item.get("bitmap"));
//				img.setImageBitmap(((SoftReference<Bitmap>) item.get("bitmap")).get());
			}
			img.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mContex.startActivity(new Intent(mContex, BigPicActivity.class).putExtra("index", -1).putExtra("path", item.get("Picture").toString()));
				}
			});
			if(Integer.parseInt(item.get("IsMine").toString())>-1)
			{
				dcl.setVisibility(View.VISIBLE);
			}
			else
			{
				dcl.setVisibility(View.INVISIBLE);
			}
			break;
		case eventList:
			 view = mInflater.inflate(R.layout.listitem_wxinfo, null);
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine")+" "+item.get("Mark"));
			((TextView)view.findViewById(R.id.text_info)).setText(item.get("PatorlItem")+"");
			((TextView)view.findViewById(R.id.text_status)).setText((item.get("IsAlloted")+"").equals("true")?"已交办":"未交办");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("SubmiContent")+"");
			String etime = (item.get("SubmitDateTime")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(etime);//.substring(5));//(5, etime.length()-3));
			img = ((ImageView)view.findViewById(R.id.image_thumb));
			img.setVisibility(View.VISIBLE);
			dcl = (ImageView)view.findViewById(R.id.imageView_dcl);
			dcl.setVisibility(View.VISIBLE);
			if (item.get("bitmap") != null)
			{
				img.setImageBitmap((Bitmap) item.get("bitmap"));
			}
			img.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mContex.startActivity(new Intent(mContex, BigPicActivity.class).putExtra("index", -1).putExtra("path", item.get("Picture").toString()));
				}
			});
			if(item.get("IsMine").toString().equals("true"))
			{
				dcl.setVisibility(View.VISIBLE);
			}
			else
			{
				dcl.setVisibility(View.INVISIBLE);
			}
			break;
		case premiss:
			String unit = item.get("ApplicationUnit")+"";
			((TextView)view.findViewById(R.id.text_project)).setText(unit.length()>18?(unit.subSequence(0, 18)+"..."):unit);
			((TextView)view.findViewById(R.id.text_info)).setText("");
			
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("ApplicationItem")+"");
			((TextView)view.findViewById(R.id.text_time)).setVisibility(View.GONE);//.setText("");
			break;
		case notice:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("Title")+"");
			((TextView)view.findViewById(R.id.text_info)).setText("");
			String notime = (item.get("ReleaseDateTime")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText("");
			
			((TextView)view.findViewById(R.id.text_time)).setText(item.get("ReleaseOrganization")+"    "+notime.subSequence(5, notime.length()-3));
			break;
		case law:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("PatorlCateGoryName")+"");
			((TextView)view.findViewById(R.id.text_info)).setText(item.get("PatorlItemName")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("HandleRegulations")+"");
			((TextView)view.findViewById(R.id.text_time)).setVisibility(View.GONE);//.setText("");
			break;
		case center:
			 view = mInflater.inflate(R.layout.listitem_center, null);
			 final LinearLayout info = (LinearLayout)view.findViewById(R.id.linear_content);
			 LinearLayout root = (LinearLayout)view.findViewById(R.id.linear_root);
			 final ImageView imgarraw  = (ImageView)view.findViewById(R.id.img_arraw);
			 if(OverAllData.getPostion()<2)
			 {
				 ((TextView)view.findViewById(R.id.text_name)).setText(OverAllData.getOrgName());//OverAllData.getLoginName()+"的案件统计");
			 }
			 else
			 {
				 ((TextView)view.findViewById(R.id.text_name)).setText(item.get("OrgName")+"");
			 }
			 ((TextView)view.findViewById(R.id.text_count)).setText(item.get("Total")+"");
			 ((TextView)view.findViewById(R.id.text_checknochuli)).setText("未处理("+item.get("P_NoHandleNum")+")");
			 ((TextView)view.findViewById(R.id.text_checkyichuli)).setText("已处理("+item.get("P_IsHandleNum")+")");
			 ((TextView)view.findViewById(R.id.text_tasknojiaoban)).setText("未交办("+item.get("E_NoAllotNum")+")");
			 ((TextView)view.findViewById(R.id.text_tasknochuli)).setText("未处理("+item.get("E_NoHandleNum")+")");
			 ((TextView)view.findViewById(R.id.text_taskyichuli)).setText("已处理("+item.get("E_IsHandleNum")+")");
			 			 
			 root.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					if(info.getVisibility()==View.GONE)
					{
						info.setVisibility(View.VISIBLE);
						imgarraw.setImageResource(R.drawable.wangshang);
					}
					else
					{
						info.setVisibility(View.GONE);
						imgarraw.setImageResource(R.drawable.wangxia);
					}
				}
			});
			 ((TextView)view.findViewById(R.id.text_checknochuli)).setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mContex.startActivity(new Intent(mContex, NormalCheckActivity.class).putExtra("isorg", OverAllData.getPostion()<2?false:true).putExtra("status", "2").putExtra("id", item.get("OrgID").toString()));
//					Toast.makeText(mContex, "未处理", 0).show();
				}
			});
			 ((TextView)view.findViewById(R.id.text_checkyichuli)).setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						mContex.startActivity(new Intent(mContex, NormalCheckActivity.class).putExtra("isorg", OverAllData.getPostion()<2?false:true).putExtra("status", "1").putExtra("id", item.get("OrgID").toString()));
//						Toast.makeText(mContex, "已处理", 0).show();
					}
				});
			 ((TextView)view.findViewById(R.id.text_tasknojiaoban)).setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						mContex.startActivity(new Intent(mContex, TaskManagerActivity.class).putExtra("isorg", OverAllData.getPostion()<2?false:true).putExtra("status", "1").putExtra("id", item.get("OrgID").toString()));
//						
					}
				});
			 ((TextView)view.findViewById(R.id.text_tasknochuli)).setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						mContex.startActivity(new Intent(mContex, TaskManagerActivity.class).putExtra("isorg", OverAllData.getPostion()<2?false:true).putExtra("status", "2").putExtra("id", item.get("OrgID").toString()));
//						
					}
				});
			 ((TextView)view.findViewById(R.id.text_taskyichuli)).setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						mContex.startActivity(new Intent(mContex, TaskManagerActivity.class).putExtra("isorg", OverAllData.getPostion()<2?false:true).putExtra("status", "3").putExtra("id", item.get("OrgID").toString()));
//						
					}
				});
			
			break;
		}
		
		return view;
	}

}
