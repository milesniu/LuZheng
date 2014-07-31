package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.adapter.AdapterCode;
import com.miles.maipu.adapter.MySpinnerAdapter;
import com.miles.maipu.adapter.NormalAdapter;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.OverAllData;

public class TaskManagerActivity extends AbsBaseActivity
{
	private ListView list_Cotent;
	private List<HashMap<String,Object>> taskList = new Vector<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		initView();
		getData();
	}
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==Btn_Right)
		{
			startActivity(new Intent(mContext, CreatTaskActivity.class));
		}
		super.onClick(v);
	}
	
	private void getData()
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
				taskList = (List<HashMap<String, Object>>) result;
				 refreshList();
				super.onPostExecute(result);
			}
	
		}.execute(new ParamData(ApiCode.GetEventsByPersonID,OverAllData.getLoginId(),currentpage+"",pagesize+""));
	}
	
	private void refreshList()
	{
		list_Cotent.setAdapter(new NormalAdapter(mContext, taskList,AdapterCode.taskManger));
		list_Cotent.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				startActivity(new Intent(mContext, TaskInfoActivity.class).putExtra("id", taskList.get(arg2).get("ID")+""));
			}
		});
		list_Cotent.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		{

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			{
				// TODO Auto-generated method stub
				AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
				int ListItem = (int) info.position;
				
				menu.setHeaderTitle("任务列表");
				menu.add(0, 0, 0, "查看任务");
				if(OverAllData.getPostion()>0&&!taskList.get(ListItem).get("ReceiverID").toString().equals(OverAllData.getLoginId())&&!taskList.get(ListItem).get("State").toString().equals("已分配"))
				{
					menu.add(0, 1, 1, "分配任务");
				}
				menu.add(0, 2, 2, "取消");
			}
		});
	}
	
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int ListItem = (int) info.position;
		switch (item.getItemId())
		{
		case 0:
			startActivity(new Intent(mContext, TaskInfoActivity.class).putExtra("id", taskList.get(ListItem).get("ID")+""));	
			break;
		case 1:
			showFenPei(taskList.get(ListItem).get("ID")+"");
			break;
		case 2:
			break;
		}
		return super.onContextItemSelected(item);
	}

	
	private Spinner sp_Organization;
	private Spinner sp_Person;
	private List<HashMap<String, Object>> organizalist = new Vector<HashMap<String, Object>>();
	private List<HashMap<String, Object>> personlist = new Vector<HashMap<String, Object>>();
	private AlertDialog builder;
	
	
	private void showFenPei(final String tid)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dlg_fenpeitask, null);
				sp_Organization = (Spinner) layout.findViewById(R.id.sp_organi);
				sp_Person = (Spinner) layout.findViewById(R.id.sp_person);
				TextView title = new TextView(mContext);
				title.setText("任务分配");
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle("任务分配").setPositiveButton("确定", new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						FenPeiToAlloted(personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"", tid);
//						Toast.makeText(mContext, tid, 0).show();
					}
				}).setNegativeButton("取消", null).show();

				hideProgressDlg();
				organizalist = (List<HashMap<String, Object>>) result;
				organizalist.add(0, OverAllData.getMyOrganization());//添加同一级机构，同级机构间可以分配给下属
				String[] arraystr = new String[organizalist.size()];
				for (int i = 0; i < organizalist.size(); i++)
				{
					arraystr[i] = organizalist.get(i).get("Name") + "";
				}
				sp_Organization.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				sp_Organization.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						//获取机构下人员
						getPerson(organizalist.get(arg2).get("ID").toString());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
						// TODO Auto-generated method stub

					}
				});
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetOrganizationUpOrDown, OverAllData.getLoginId(),"0"));//0，获取下属机构
	}
	
	private void FenPeiToAlloted(String pid,String tid)
	{
			
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				getData();
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.GetEventReceiveToAlloted,pid,tid));
	}
	
	//根据机构获取机构下人员
		private void getPerson(String oid)
		{
			showprogressdialog();
			new SendDataTask()
			{

				@Override
				protected void onPostExecute(Object result)
				{
					// TODO Auto-generated method stub
					personlist.clear();
					
					personlist = (List<HashMap<String, Object>>) result;
					
					String[] arraystr = new String[personlist.size()];
					for (int i = 0; i < personlist.size(); i++)
					{
						arraystr[i] = personlist.get(i).get("Name") + "";
					}
					sp_Person.setAdapter(new MySpinnerAdapter(mContext, arraystr));
					
					hideProgressDlg();
					
					super.onPostExecute(result);
				}
				
			}.execute(new ParamData(ApiCode.GetPersonInformationByOrganization, oid));
		}
	
	public void initView()
	{
		// TODO Auto-generated method stub
		list_Cotent = (ListView)findViewById(R.id.list_content);
		
		Btn_Left = (Button)findViewById(R.id.bt_left);
		Btn_Right = (Button) findViewById(R.id.bt_right);
		text_title = (TextView) findViewById(R.id.title_text);
		if (Btn_Left != null)
		{
			Btn_Left.setOnClickListener(this);
		}
		if (Btn_Right != null)
		{
			Btn_Right.setOnClickListener(this);
		}
		if(OverAllData.getPostion()>0)
		{
			Btn_Right.setBackgroundResource(R.drawable.newnormal);
		}
		else
		{
			Btn_Right.setVisibility(View.INVISIBLE);
		}
		
		text_title.setText("任务列表");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_manager, menu);
		return true;
	}

}
