package com.miles.maipu.net;

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
		case GetRoadLines:
		case GetAllPatorlCateGoryAndItems:
		case GetPatorlRecordDetailList:
		case GetPatorlRecordDetail:
		case GetEventsByPersonID:
		case GetEventAllot:
		case GetOrganizationUpOrDown:
		case GetPersonInformationByOrganization:
			return HttpGetUtil.httpUrlConnection(parm[0].getCode(), parm[0].getParms());
			
		case SaveFile:
		case AddPatorlRecordDetail:
		case UpdatePatorlRecordDetail:
		case AddEventFeedback:
			return HttpPostUtil.httpUrlConnection(parm[0].getCode(),  parm[0].getParms()[0]);
		}
		return null;
	}

}
