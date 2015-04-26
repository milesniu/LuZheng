package com.miles.maipu.net;

import android.annotation.SuppressLint;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import java.io.InputStream;
import java.net.URLEncoder;

public class NetApiUtil
{
    /**
     * 通用接口
     */
    public static String BaseUrl = "http://58.216.243.77:3768/";
    public static String ImgBaseUrl = "http://58.216.243.77/Picture/";
    public static String thumbImgBaseUrl = "http://58.216.243.77/Thumbnail/";//缩略图路径

    //天气预报地址(常州)
    public static String WeatherUrl = "http://www.weather.com.cn/data/cityinfo/101191101.html";

    //	public static String WeatherUrl = "http://m.weather.com.cn/atad/101191101.html";
    //	public static String WeatherUrl = "http://www.weather.com.cn/data/cityinfo/101191101.html";
    //程序可用性检测地址(阿里云)
    public static String checkUrl = "http://ossmiles.oss-cn-hangzhou.aliyuncs.com/AppCtrl/com.miles.maipu.luzheng.txt";

    private static String checkResult = "-1";


    public static String getApiName(ApiCode api)
    {
        switch (api)
        {
            case login:
                return "api/platform/user/login/";
            case GetAllPersonOfSameDepart:
                return "api/basicinformation/person/GetAllPersonOfSameDepart/";
            case Signin:
                return "api/patrol/patorlrecord/AddSign/";
            case GetRoadLinesByPersonID:
                return "api/basicinformation/roadline/GetRoadLinesByPersonID/";
            case GetAllPatorlCateGoryAndItems:
                return "api/patrol/patorlcategory/GetAllPatorlCateGoryAndItems/";
            case SaveFile:
                return "api/system/SaveFile";
            case AddPatorlRecordDetail:
                return "api/patrol/patorlrecord/AddPatorlRecordDetail/";
            case GetPatorlRecordDetailList:
                return "api/patrol/patorlrecord/GetPatorlRecordDetailList/";
            case GetPatorlRecordDetail:
                return "api/patrol/patorlrecord/GetPatorlRecordDetail/";
            case UpdatePatorlRecordDetail:
                return "api/patrol/patorlrecord/UpdatePatorlRecordDetail";
            case GetEventsByPersonID:
                return "api/patrol/event/GetEventsByPersonID/";
            case GetEventAllot:
                return "api/patrol/event/GetEventAllot/";
            case AddEventFeedback:
                return "api/patrol/event/AddEventFeedback/";
            case GetOrganizationUpOrDown:
                return "api/basicinformation/organization/GetOrganizationUpOrDown/";
            case GetPersonInformationByOrganization:
                return "api/basicinformation/person/GetPersonInformationByOrganization/";
            case AddEventAllot:
                return "api/patrol/event/AddEventAllot/";
            case GetEventReceiveToAlloted:
                return "api/patrol/event/GetEventReceiveToAlloted/";
            case GetEventAllotDetails:
                return "api/patrol/event/GetEventAllotDetails/";
            case AddEventSubmit:
                return "api/patrol/event/AddEventSubmit/";
            case GetEventSubmitsNoAlloted:
                return "api/test/event/GetEventSubmitsNoAlloted/";
            case geteventsubmit:
                return "api/patrol/event/geteventsubmit/";
            case GetEventSubmitToAlloted:
                return "api/patrol/event/GetEventSubmitToAlloted/";
            case GetSubmitEvent:
                return "api/patrol/eventsubmit/GetSubmitEvent/";
            case GetSubordinate:
                return "api/basicinformation/person/GetSubordinate/";
            case PostLicenseInfoByItemAndNum:
                return "api/patrol/license/PostLicenseInfoByItemAndNum/";
            case GetLicenseInfoForPN:
                return "api/patrol/LicenseNavigation/GetLicenseInfoForPN/";
            case GetAllUsedApplicationItem:
                return "api/patrol/license/GetAllUsedApplicationItem/";
            case GetNoticesByPersonID:
                return "api/basicinformation/notice/GetNoticesByPersonID/";
            case GetNoticeByID:
                return "api/basicinformation/notice/GetNoticeByID/";
            case getallLawForApp:
                return "api/LawManage/Law/getallLawForApp/";
            case GetLawByPatorlItemID:
                return "api/LawManage/Law/GetLawByPatorlItemID/";
            case AuditEventAllot:
                return "api/patrol/event/AuditEventAllot/";
            case GetHandleNumByPersonID:
                return "api/patrol/statistics/GetHandleNumByPersonID/";
            case GetPatorlRecordDetailListByOrgID:
                return "api/patrol/patorlrecord/GetPatorlRecordDetailListByOrgID/";
            case GetEventsByOrgID:
                return "api/patrol/event/GetEventsByOrgID/";
            case GetAllPatorlRecordDetailByPersonID:
                return "api/patrol/patorlrecord/GetAllPatorlRecordDetailByPersonID/";
            case GetAllPatorlCars:
                return "api/basicinformation/person/GetAllPatorlCars/";
            case GetSignRoadLineByPersonID:
                return "api/basicinformation/roadline/GetSignRoadLineByPersonID/";
            case SaveTrajectory:
                return "api/patrol/trajectory/SaveTrajectory/";
            case appinfo:
                return "api/system/appinfo/package/";
            case GetIsAuth:
                return "api/basicinformation/auth/GetIsAuth/";
            case SaveClientOnline:
                return "api/basicinformation/client/SaveClientOnline/";
            case GetAllEvaluateEvent:
                return "api/patrol/evaluate/GetAllEvaluateEvent/";
        }
        return null;
    }


    public static boolean isCanuse()
    {
        if (checkResult.equals("-1") || checkResult.equals("0"))
        {
            checkResult = GetCheckapp();
        }
        return checkResult.equals("0") ? false : true;

    }

    @SuppressLint("SimpleDateFormat")
    public static String GetWeather()
    {

        String result = "";
        InputStream is = null;
        HttpGet httpRequest = new HttpGet(WeatherUrl);
        try
        {
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            { // 正确

                is = httpResponse.getEntity().getContent();
                byte[] data = new byte[1024];
                int n = -1;
                ByteArrayBuffer buf = new ByteArrayBuffer(10 * 1024);
                while ((n = is.read(data)) != -1)
                    buf.append(data, 0, n);
                result = new String(buf.toByteArray(), HTTP.UTF_8);
                is.close();

                return result;
            } else
            {
                Log.v("tip==", "error response code");
                return "";
            }
        } catch (Exception e)
        {
            Log.e("error==", "" + e.getMessage());
            return result;
        }
    }

    public static String URLencode(String url)
    {
        try
        {
            return URLEncoder.encode(url, "utf8");
        } catch (Exception e)
        {
            return url;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String GetCheckapp()
    {

        String result = "";
        InputStream is = null;
        HttpGet httpRequest = new HttpGet(checkUrl);
        try
        {
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            { // 正确

                is = httpResponse.getEntity().getContent();
                byte[] data = new byte[1024];
                int n = -1;
                ByteArrayBuffer buf = new ByteArrayBuffer(10 * 1024);
                while ((n = is.read(data)) != -1)
                    buf.append(data, 0, n);
                result = new String(buf.toByteArray(), HTTP.UTF_8);
                is.close();

                return result;
            } else
            {
                Log.v("tip==", "error response code");
                return "";
            }
        } catch (Exception e)
        {
            Log.e("error==", "" + e.getMessage());
            return "";
        }
    }


}
