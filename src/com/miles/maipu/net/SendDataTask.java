package com.miles.maipu.net;

import java.util.HashMap;

import android.os.AsyncTask;

public abstract class SendDataTask extends AsyncTask<ParamData, String,Object>
{


	@SuppressWarnings("incomplete-switch")
	@Override
	protected Object doInBackground(ParamData... parm)
	{
		// TODO Auto-generated method stub
		switch(parm[0].getCode())
		{
		case login:
		case GetAllPersonOfSameDepart:
		case Signin:
			return HttpGetUtil.httpUrlConnection(parm[0].getCode(), parm[0].getParms());
		}
		return null;
	}

}
