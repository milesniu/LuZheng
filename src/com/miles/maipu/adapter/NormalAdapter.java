package com.miles.maipu.adapter;

import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.miles.maipu.luzheng.R;

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		LayoutInflater mInflater = LayoutInflater.from(mContex);
		View view = mInflater.inflate(R.layout.listitem_normalcheck, null);
		HashMap<String, Object> item = data.get(position);
		switch(code)
		{
		case norMalCheck:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine")+" "+item.get("PatorlItemName"));
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("HandleDescription")+"");
			String ntime = (item.get("RecordTime")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(ntime.subSequence(5, ntime.length()-3));
			break;
		case taskManger:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("SubmitCode")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("EventContent")+"");
			String ttime = (item.get("AllotedDate")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(ttime.subSequence(5, ttime.length()-3));
			break;
		}
		
			
		
		return view;
	}

}
