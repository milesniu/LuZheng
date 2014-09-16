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
		/**GET方式提交
		 * */
		case login:
		case GetAllPersonOfSameDepart:
		
		case GetRoadLines:
		case GetAllPatorlCateGoryAndItems:
		case GetPatorlRecordDetailList:
		case GetPatorlRecordDetail:
		case GetEventsByPersonID:
		case GetEventAllot:
		case GetOrganizationUpOrDown:
		case GetPersonInformationByOrganization:
		case GetEventReceiveToAlloted:
		case GetEventAllotDetails:
		case GetEventSubmitsNoAlloted:
		case geteventsubmit:
		case GetSubmitEvent:
		case GetEventSubmitToAlloted:
		case GetSubordinate:
		case GetLicenseInfoForPN:
		case GetLicenseInfoByItemAndNum:
		case GetAllUsedApplicationItem:
		case GetNoticesByPersonID:
		case GetNoticeByID:
		case getallLawForApp:
		case GetLawByPatorlItemID:
		case AuditEventAllot:
		case GetHandleNumByPersonID:
		case GetPatorlRecordDetailListByOrgID:
		case GetEventsByOrgID:
		case GetAllPatorlRecordDetailByPersonID:
			return HttpGetUtil.httpUrlConnection(parm[0].getCode(), parm[0].getParms());
			
		/**POST方式提交
		 * */
		case Signin:
		case SaveFile:
		case AddPatorlRecordDetail:
		case UpdatePatorlRecordDetail:
		case PostLicenseInfoByItemAndNum:
			return HttpPostUtil.httpUrlConnection(parm[0].getCode(),  parm[0].getParms()[0]);
			
			
		/**Get+POST的方式，URL和POST里面各放一部分参数
		 * */	
		case AddEventAllot:
		case AddEventSubmit:
		case AddEventFeedback:
			return HttpPostUtil.httpUrlConnection(parm[0].getCode(),  parm[0].getParms()[0],parm[0].getParms()[1]);
		}
		return null;
	}

}
