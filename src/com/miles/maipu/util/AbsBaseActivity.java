package com.miles.maipu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miles.maipu.luzheng.R;
import com.miles.maipu.net.ApiCode;
import com.miles.maipu.net.ParamData;
import com.miles.maipu.net.SendDataTask;

public abstract class AbsBaseActivity extends Activity implements OnClickListener
{
	protected Context mContext = this;
	public View LayoutTitle;
	public Button Btn_Left;
	public Button Btn_Right;
	public TextView text_title;
	public ListView List_Content;
	public ProgressDialog pdialog;
	public static String title = "常州公路";
	public static String message = "正在努力加载···";
	public int pagesize = 200;
	public int currentpage = 1;

	

	

	public void showprogressdialog()
	{
		if (pdialog == null || !pdialog.isShowing())
		{
			pdialog = ProgressDialog.show(this, title, message);
			pdialog.setIcon(R.drawable.ic_launcher);
			pdialog.setCancelable(true);
		}
	}
	
	

	public void hideProgressDlg()
	{
		if (pdialog != null && pdialog.isShowing())
		{
			pdialog.dismiss();
		}
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		if (v == Btn_Left)
		{
			this.finish();
			return;
		}
	}

}
