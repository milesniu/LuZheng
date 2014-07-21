package com.miles.maipu.adapter;

import com.miles.maipu.luzheng.R;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MySpinnerAdapter extends ArrayAdapter<String>
{

	public MySpinnerAdapter(Context context, String[] data)
	{
		super(context,  R.layout.simple_spinner_item, data);
		// TODO Auto-generated constructor stub
		this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

}
