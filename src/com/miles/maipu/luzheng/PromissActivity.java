package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.OverAllData;

public class PromissActivity extends AbsBaseActivity
{

	private ListView list_Cotent;
	private List<HashMap<String,Object>> promissList = new Vector<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promiss);
		initView();
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
		
		text_title.setText("许可列表");
		getDataList();
	}

	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if(v==Btn_Right)
		{
			showFindPremiss();
		}
		super.onClick(v);
	}



	private Spinner sp_item;
	private List<HashMap<String, Object>> itemlist = new Vector<HashMap<String, Object>>();
	private AlertDialog builder;
	private EditText edit_num;
	private void showFindPremiss()
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
				View layout = inflater.inflate(R.layout.dlg_findpremiss, null);
				sp_item = (Spinner) layout.findViewById(R.id.sp_item);
				edit_num = (EditText)layout.findViewById(R.id.edit_num);
				TextView title = new TextView(mContext);
				title.setText("许可查询");
				builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle("任务分配").setPositiveButton("确定", new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						gofind(itemlist.get(sp_item.getSelectedItemPosition()).get("ID")+"",edit_num.getText().toString());
//						FenPeiToAlloted(personlist.get(sp_Person.getSelectedItemPosition()).get("ID")+"", tid);
//						Toast.makeText(mContext, tid, 0).show();
					}
				}).setNegativeButton("取消", null).show();

				hideProgressDlg();
				itemlist = (List<HashMap<String, Object>>) result;
				String[] arraystr = null;
				arraystr = new String[itemlist.size()];
				for (int i = 0; i < itemlist.size(); i++)
				{
					arraystr[i] = itemlist.get(i).get("Name") + "";
				}
				
				
				sp_item.setAdapter(new MySpinnerAdapter(mContext, arraystr));
				
				super.onPostExecute(result);
			}

		}.execute(new ParamData(ApiCode.GetAllUsedApplicationItem));
	}
	
	private void gofind(String itemid,String num)
	{
		showprogressdialog();
		
		//许可部分
		Map<String, Object> send = new HashMap<String, Object>();
		send.put("page", "1");
		send.put("rows", "1");
		send.put("ID", itemid);
		send.put("Name", num);
		
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				
				if(result==null)
					return;
				List<HashMap<String, Object>> plist = (List<HashMap<String, Object>>) result;
				if(plist!=null&&plist.size()==1)
				{
					startActivity(new Intent(mContext, PremissInfoActivity.class).putExtra("id", plist.get(0).get("ID")+""));
				}
				else
				{
					Toast.makeText(mContext, "未查询到响应许可...", 0).show();
				}
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.PostLicenseInfoByItemAndNum, JSONUtil.toJson(send)));
	}

	
	private void getDataList()
	{
		showprogressdialog();
		
		//许可部分
		Map<String, Object> send = new HashMap<String, Object>();
		send.put("page", currentpage+"");
		send.put("rows", pagesize+"");
		
		new SendDataTask()
		{

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				
				if(result==null)
					return;
				promissList = (List<HashMap<String, Object>>) result;
				list_Cotent.setAdapter(new NormalAdapter(mContext, promissList,AdapterCode.premiss));
				list_Cotent.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
					{
						// TODO Auto-generated method stub
						startActivity(new Intent(mContext, PremissInfoActivity.class).putExtra("id", promissList.get(arg2).get("ID")+""));
					}
				});
				super.onPostExecute(result);
			}
			
			
		}.execute(new ParamData(ApiCode.PostLicenseInfoByItemAndNum, JSONUtil.toJson(send)));
	}

	
	
	
}
