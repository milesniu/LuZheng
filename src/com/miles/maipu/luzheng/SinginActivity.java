package com.miles.maipu.luzheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;
import com.miles.maipu.util.JSONUtil;
import com.miles.maipu.util.MutiChoiseDlg;
import com.miles.maipu.util.SingleChoiseDlg;
import com.miles.maipu.util.OverAllData;
import com.miles.maipu.util.UnixTime;

public class SinginActivity extends AbsBaseActivity
{

	private Button Btn_Select = null;
	private Button Btn_Singin = null;
	private List<HashMap<String,Object>> personlist = new Vector<HashMap<String,Object>>();
	private EditText edit_select;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_singin);
		super.onCreate(savedInstanceState);
		initView();
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				personlist = (List<HashMap<String, Object>>) result;
				super.onPostExecute(result);
			}
			
		}.execute(new ParamData(ApiCode.GetAllPersonOfSameDepart, OverAllData.getLoginId()));
	}

	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bt_select:
			try
			{
				new MutiChoiseDlg(mContext, personlist).getDlg(edit_select);
			} catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
//			new SingleChoiseDlg(mContext, personlist).getDlg(edit_select);
			break;
		case R.id.bt_singin:
			if(edit_select.getText().toString().equals(""))
			{
				edit_select.setTag("00000000-0000-0000-0000-000000000000");
			}
			
			Map<String, Object> PatorlRecord = new HashMap<String, Object>();

			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("ID", OverAllData.getLoginId());
			PatorlRecord.put("PersonInformation", p1);
			String xuid = edit_select.getTag()+"";
			PatorlRecord.put("Auxiliaries", xuid.subSequence(0, xuid.length()-1));
			PatorlRecord.put("Weather", OverAllData.Weathermap.get("weather").toString());
			showprogressdialog();
			new SendDataTask()
			{

				@Override
				protected void onPostExecute(Object result)
				{
					// TODO Auto-generated method stub
					hideProgressDlg();
					HashMap<String,Object> obj = (HashMap<String, Object>) result;
					if(obj==null || obj.get("IsSuccess")==null)
					{
						return;
					}
					if(obj.get("IsSuccess").toString().equals("true"))
					{
						OverAllData.setRecordId(((Map)obj.get("Result")).get("ID")+"");
						SinginActivity.this.finish();
					}
					else
					{
						Toast.makeText(mContext, obj.get("Message")+"", 0).show();
						return;
					}
					super.onPostExecute(result);
				}
				
			}.execute(new ParamData(ApiCode.Signin,JSONUtil.toJson(PatorlRecord)));
			
			
//			this.finish();
			break;
		}
		super.onClick(v);
	}



	public void initView()
	{
		// TODO Auto-generated method stub
		Btn_Left = (Button)findViewById(R.id.bt_left);
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
		text_title.setText("签到");
		Btn_Right.setVisibility(View.INVISIBLE);
		Btn_Select = (Button)findViewById(R.id.bt_select);
		Btn_Select.setOnClickListener(this);
		Btn_Singin = (Button)findViewById(R.id.bt_singin);
		Btn_Singin.setOnClickListener(this);
		edit_select = (EditText)findViewById(R.id.edit_concotact);
		((TextView)findViewById(R.id.text_time)).setText("日期："+UnixTime.getStrCurrentSimleTime());
		((TextView)findViewById(R.id.text_weather)).setText("天气："+OverAllData.Weathermap.get("weather").toString()+" "+OverAllData.Weathermap.get("temp1").toString()+"~"+OverAllData.Weathermap.get("temp2").toString());
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.singin, menu);
		return true;
	}
}
