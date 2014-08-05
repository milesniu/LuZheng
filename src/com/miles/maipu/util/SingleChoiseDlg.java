package com.miles.maipu.util;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.miles.maipu.luzheng.R;

public class SingleChoiseDlg
{
	private Context mContext;
	private List<HashMap<String, Object>> contactList;
	int selected;

	public SingleChoiseDlg(Context contex, List<HashMap<String, Object>> contacts)
	{
		this.mContext = contex;
		contactList = contacts;
	}

	int index =0;
	
	public String getDlg(final EditText edit)
	{
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("联系人选择");
		builder.setIcon(R.drawable.ic_launcher);
		
		
		
		DialogInterface.OnClickListener mutiListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				index = which;
			}

		};
		String[] arrayc = new String[contactList.size()];
		for (int i = 0; i < contactList.size(); i++)
		{
			arrayc[i] = contactList.get(i).get("Name").toString();
		}
		builder.setSingleChoiceItems(arrayc, selected, mutiListener);
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int which)
			{
				try
				{
					edit.setText(contactList.get(index).get("Name").toString());
					edit.setTag(contactList.get(index).get("ID").toString());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		builder.setPositiveButton("确定", btnListener);
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.show();
		return null;
	}
}
