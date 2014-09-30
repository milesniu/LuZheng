package com.miles.maipu.util;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class MutiChoiseDlg
{
	private Context mContext;
	private List<HashMap<String, Object>> contactList;
	boolean[] selected;
	String dlgTitle;

	// String rowName;
	// String rowId;

	public MutiChoiseDlg(Context contex, List<HashMap<String, Object>> contacts, String title)
	{
		this.mContext = contex;
		contactList = contacts;
		selected = new boolean[contactList.size()];
		this.dlgTitle = title;
	}

	public String getDlg(final EditText edit)
	{
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(dlgTitle);
		// builder.setIcon(R.drawable.ic_launcher);
		DialogInterface.OnMultiChoiceClickListener mutiListener = new DialogInterface.OnMultiChoiceClickListener()
		{

			@Override
			public void onClick(DialogInterface dialogInterface, int which, boolean isChecked)
			{
				selected[which] = isChecked;
			}
		};
		
		
		
		String[] arrayc = new String[contactList.size()];
		for (int i = 0; i < contactList.size(); i++)
		{
			arrayc[i] = contactList.get(i).get("Name").toString();
		}
		if(dlgTitle.equals("车辆选择"))
		{
			selected[0] = true;
			builder.setSingleChoiceItems(arrayc, 0, new OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					for(int i=0;i<selected.length;i++)
					{
						selected[i] = false;
					}
					selected[which] = true;
				}
			});
		}
		else
		{
			builder.setMultiChoiceItems(arrayc, selected, mutiListener);
		}
		DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int which)
			{
				String selectedStr = "";
				String selectedTag = "";
				for (int i = 0; i < selected.length; i++)
				{
					if (selected[i] == true)
					{
						selectedStr = selectedStr + contactList.get(i).get("Name").toString() + ",";
						selectedTag = selectedTag + contactList.get(i).get("ID").toString() + "|";
					}
				}
				edit.setText(selectedStr.equals("") ? "" : selectedStr.subSequence(0, selectedStr.length() - 1));
				edit.setTag(selectedStr.equals("") ? "" : selectedTag);
			}
		};
		builder.setPositiveButton("确定", btnListener);
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.show();
		return null;
	}
}
