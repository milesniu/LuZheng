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
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine")+" "+item.get("PatorlItem"));
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("AllotedDate")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(item.get("State")+"");
			break;
		case eventList:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("RoadLine")+" "+item.get("PatorlItem"));
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("SubmiContent")+"");
			String etime = (item.get("SubmitDateTime")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(etime.subSequence(5, etime.length()-3));
			break;
		case premiss:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("ApplicationUnit")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("ApplicationItem")+"");
			((TextView)view.findViewById(R.id.text_time)).setText("");
			break;
		case notice:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("Title")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("ReleaseOrganization")+"");
			String notime = (item.get("ReleaseDateTime")+"");
			((TextView)view.findViewById(R.id.text_time)).setText(notime.subSequence(5, notime.length()-3));
			((TextView)view.findViewById(R.id.text_time)).setText("");
			break;
		case law:
			((TextView)view.findViewById(R.id.text_project)).setText(item.get("PatorlCateGoryName")+"  "+item.get("PatorlItemName")+"");
			((TextView)view.findViewById(R.id.text_descrption)).setText(item.get("HandleRegulations")+"");
			((TextView)view.findViewById(R.id.text_time)).setText("");
			break;
		}
		
		return view;
	}

}
