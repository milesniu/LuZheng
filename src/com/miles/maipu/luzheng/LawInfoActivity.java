package com.miles.maipu.luzheng;

import java.util.HashMap;

import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;
import com.miles.maipu.util.AbsBaseActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class LawInfoActivity extends AbsBaseActivity
{

	String id="";
	private TextView GongluLaw;
	private TextView GongluSafe;
	private TextView JiangsuGonglu;
	private HashMap<String, Object> res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_law_info);
		initView();
		getInfo(getIntent().getStringExtra("id"));
	}
	
	public void initView()
	{
		// TODO Auto-generated method stub
		
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
		Btn_Right.setVisibility(View.INVISIBLE);
		
		text_title.setText("政策法规");
		
		GongluLaw = (TextView)findViewById(R.id.text_gonglulaw);
		GongluSafe = (TextView)findViewById(R.id.text_anquantl);
		JiangsuGonglu = (TextView)findViewById(R.id.text_jiangsu);
		
		GongluLaw.setOnClickListener(this);
		GongluSafe.setOnClickListener(this);
		JiangsuGonglu.setOnClickListener(this);
		
	}

	
	
	
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId())
		{
		case R.id.text_gonglulaw:
			if(!GongluLaw.getText().toString().equals(""))
			{
				showLaw("公路法",res.get("RoadLawContent").toString());
			}
			break;
		case R.id.text_anquantl:
			if(!GongluSafe.getText().toString().equals(""))
			{
				showLaw("公路安全保护条例",res.get("RoadSafeContent").toString());
			}
			break;
		case R.id.text_jiangsu:
			if(!JiangsuGonglu.getText().toString().equals(""))
			{
				showLaw("江苏省公路条例",res.get("JSRoadContent").toString());
			}
			break;
		}
	}

	private void getInfo(String id)
	{
		showprogressdialog();
		new SendDataTask()
		{

			@Override
			protected void onPostExecute(Object result)
			{
				// TODO Auto-generated method stub
				hideProgressDlg();
				
				res = (HashMap<String, Object>)result;
				
				GongluLaw.setText(res.get("RoadLaw").toString());
				GongluLaw.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
				GongluSafe.setText(res.get("RoadSafeRegulations").toString());
				GongluSafe.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
				
				JiangsuGonglu.setText(res.get("JiangSuRoadRegulations").toString());
				JiangsuGonglu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
				
				
				((TextView)findViewById(R.id.text_content)).setText(res.get("HandleRegulations").toString());
				
				
				super.onPostExecute(result);
			}
			
			
			
		}.execute(new ParamData(ApiCode.GetLawByPatorlItemID,id));
	}
	
	private AlertDialog builder;
	private void showLaw(String dlgtitle,String detail)
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dlg_lawdetail, null);
		TextView textdetail = (TextView) layout.findViewById(R.id.text_detail);
		textdetail.setText(detail);
		
		builder = new AlertDialog.Builder(mContext).setView(layout).setCustomTitle(null).setInverseBackgroundForced(true).setTitle(dlgtitle).setPositiveButton("确定", null).show();

	}
	
}
