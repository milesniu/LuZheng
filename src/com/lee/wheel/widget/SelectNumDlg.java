package com.lee.wheel.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.miles.maipu.luzheng.R;

public class SelectNumDlg
{
	private Context mContext;
	String textContent = null;
	int[] mData =
	{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, };

	WheelView mWheel1 = null;
	WheelView mWheel2 = null;
	WheelView mWheel3 = null;
	WheelView mWheel4 = null;
	WheelView mWheel01 = null;
	WheelView mWheel02 = null;
	WheelView mWheel03 = null;
	View mDecorView = null;
	boolean mStart = false;

	
	public SelectNumDlg(Context contex)
	{
		mContext = contex;
	}
	
	
	private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id)
		{
			formatData();
		}

		@Override
		public void onNothingSelected(TosAdapterView<?> parent)
		{
		}
	};

	public void ShowDlg(final EditText edit)
	{
		
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View linearlayout = inflater.inflate(R.layout.wheel_password, null);

		// mTextView = (TextView) findViewById(R.id.sel_password);

		mWheel1 = (WheelView) linearlayout.findViewById(R.id.wheel1);
		mWheel2 = (WheelView) linearlayout.findViewById(R.id.wheel2);
		mWheel3 = (WheelView) linearlayout.findViewById(R.id.wheel3);
		mWheel4 = (WheelView) linearlayout.findViewById(R.id.wheel4);

		mWheel01 = (WheelView) linearlayout.findViewById(R.id.WheelView01);
		mWheel02 = (WheelView) linearlayout.findViewById(R.id.WheelView02);
		mWheel03 = (WheelView) linearlayout.findViewById(R.id.WheelView03);

		mWheel1.setScrollCycle(true);
		mWheel2.setScrollCycle(true);
		mWheel3.setScrollCycle(true);
		mWheel4.setScrollCycle(true);

		mWheel01.setScrollCycle(true);
		mWheel02.setScrollCycle(true);
		mWheel03.setScrollCycle(true);

		mWheel1.setAdapter(new NumberAdapter());
		mWheel2.setAdapter(new NumberAdapter());
		mWheel3.setAdapter(new NumberAdapter());
		mWheel4.setAdapter(new NumberAdapter());
		mWheel01.setAdapter(new NumberAdapter());
		mWheel02.setAdapter(new NumberAdapter());
		mWheel03.setAdapter(new NumberAdapter());

		mWheel1.setOnItemSelectedListener(mListener);
		mWheel2.setOnItemSelectedListener(mListener);
		mWheel3.setOnItemSelectedListener(mListener);
		mWheel4.setOnItemSelectedListener(mListener);
		mWheel01.setOnItemSelectedListener(mListener);
		mWheel02.setOnItemSelectedListener(mListener);
		mWheel03.setOnItemSelectedListener(mListener);

		formatData();

		mDecorView = ((Activity) mContext).getWindow().getDecorView();
		
		
		Dialog dialog = null;

		dialog = new AlertDialog.Builder(mContext).setTitle("选择桩号").setView(linearlayout).setPositiveButton("确定", new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				edit.setText(textContent);
			}
		}).setNegativeButton("取消", null).show();
	}

	private void formatData()
	{
		int pos1 = mWheel1.getSelectedItemPosition();
		int pos2 = mWheel2.getSelectedItemPosition();
		int pos3 = mWheel3.getSelectedItemPosition();
		int pos4 = mWheel4.getSelectedItemPosition();
		int pos01 = mWheel01.getSelectedItemPosition();
		int pos02 = mWheel02.getSelectedItemPosition();
		int pos03 = mWheel03.getSelectedItemPosition();

		textContent = String.format("K%d%d%d%d+%d%d%d", pos1, pos2, pos3, pos4, pos01, pos02, pos03);
	}

	private class NumberAdapter extends BaseAdapter
	{
		int mHeight = 50;

		public NumberAdapter()
		{
			mHeight = (int) Utils.pixelToDp(mContext, mHeight);
		}

		@Override
		public int getCount()
		{
			return (null != mData) ? mData.length : 0;
		}

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView txtView = null;

			if (null == convertView)
			{
				convertView = new TextView(mContext);
				convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));

				txtView = (TextView) convertView;
				txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
				txtView.setTextColor(Color.BLACK);
				txtView.setGravity(Gravity.CENTER);
			}

			String text = String.valueOf(mData[position]);
			if (null == txtView)
			{
				txtView = (TextView) convertView;
			}

			txtView.setText(text);

			return convertView;
		}
	}
}
