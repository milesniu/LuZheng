package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class LawActivity extends AbsBaseActivity
{

	List<HashMap<String, Object>> lawlist = new Vector<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law);
		initView();
	}

	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button) findViewById(R.id.bt_left);
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
		Btn_Right.setBackgroundResource(R.drawable.search);
		Btn_Right.setVisibility(View.VISIBLE);
		;
		text_title.setText("政策法规");
		getNotice();
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (v == Btn_Right)
		{
			showFindLaw();
		}
		super.onClick(v);
	}

	// // 获取巡查分类与巡查项
	// new SendDataTask()
	// {
	//
	// @Override
	// protected void onPostExecute(Object result)
	//
	//
	// }.execute(new ParamData(ApiCode.GetAllPatorlCateGoryAndItems, ""));
	//
	// }
	private Spinner sp_category;
	private Spinner sp_project;

	private AlertDialog builder;
	List<HashMap<String, Object>> categorylist;

	private void showFindLaw()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dlg_findlaw, null);
				sp_category = (Spinner) layout.findViewById(R.id.sp_category);
				sp_project = (Spinner) layout.findViewById(R.id.sp_projectn);
				TextView title = new TextView(mContext);
				title.setText("法规查询");
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle("法规查询").setPositiveButton("确定", new OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						startActivity(new Intent(mContext, LawInfoActivity.class).putExtra("id",  ((List<HashMap<String, Object>>) categorylist.get(sp_category.getSelectedItemPosition()).get("PatorlItems")).get(sp_project.getSelectedItemPosition()).get("ID") + ""));
						
					}
				}).setNegativeButton("取消", null).show();

				{
					categorylist = (List<HashMap<String, Object>>) result;

					String[] arraystr = new String[categorylist.size()];
					for (int i = 0; i < categorylist.size(); i++)
					{
						arraystr[i] = categorylist.get(i).get("Name") + "";
					}
					sp_category.setAdapter(new MySpinnerAdapter(mContext, arraystr));
					sp_category.setOnItemSelectedListener(new OnItemSelectedListener()
					{

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
						{
							// TODO Auto-generated method stub
							final List<HashMap<String, Object>> prolist = (List<HashMap<String, Object>>) (categorylist.get(arg2).get("PatorlItems"));
							String[] arraystr = new String[prolist.size()];
							for (int i = 0; i < prolist.size(); i++)
							{
								arraystr[i] = prolist.get(i).get("Name") + "";
							}
							sp_project.setAdapter(new MySpinnerAdapter(mContext, arraystr));

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0)
						{
							// TODO Auto-generated method stub

						}
					});
					super.onPostExecute(result);
				}
			}

		}.execute(new ParamData(ApiCode.GetAllPatorlCateGoryAndItems, ""));
	}

	private void getNotice()
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				lawlist = (List<HashMap<String, Object>>) ((Map) result).get("rows");
				List_Content.setAdapter(new NormalAdapter(mContext, lawlist, AdapterCode.law));
				List_Content.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id)
					{
						// TODO Auto-generated method stub
						startActivity(new Intent(mContext, LawInfoActivity.class).putExtra("id", lawlist.get(position).get("PatorlItem_ID").toString()));
					}
				});

				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.getallLawForApp, currentpage + "", "2000"));
	}

}
