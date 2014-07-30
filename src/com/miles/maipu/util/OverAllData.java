package com.miles.maipu.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

public class OverAllData
{
	public static String TitleName = "路政巡查";
	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng" + File.separator;
	private static BaseMapObject loginInfo = null;
	public static Map<String, Object> Weathermap = null;
	public static String loginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng/login.dat" + File.separator;
	
	
	
	public static void SetLogininfo(BaseMapObject info)
	{
		loginInfo = info;
	}
	
	/**获取签到id
	 * */
	public static String getRecordId()
	{
		if(loginInfo==null)
		{
			FileUtils.getMapData4SD();
			if(loginInfo==null)
			{
				return "";
			}
		}
		
		return ((Map)loginInfo.get("PatorlRecord")).get("ID").toString();
	}
	
	/**获取签到类型
	 * */
//	public static int getPatorlType()
//	{
//		if (loginInfo == null)
//		{
//			FileUtils.getMapData4SD();
//			if (loginInfo == null)
//			{
//				return 0;
//			}
//		}
//		try
//		{
//			return Integer.parseInt(((Map) loginInfo.get("PatorlRecord")).get("PatorlType").toString());
//		} catch (Exception e)
//		{
//			return 0;
//		}
//	}
	
	
	/**设置签到id
	 * */
	@SuppressWarnings("unchecked")
	public static boolean setRecordId(String id)
	{
		if(loginInfo==null)
		{
			FileUtils.getMapData4SD();
			if (loginInfo == null)
			{
				return false;
			}
		}
		((Map)loginInfo.get("PatorlRecord")).put("ID", id);
		FileUtils.setMapData2SD(loginInfo);
		return true;
	}
	

	/**获取登陆id
	 * */
	public static String getLoginId()
	{
		if(loginInfo==null)
		{
			FileUtils.getMapData4SD();
			if (loginInfo == null)
			{	
				return "";
			}
		}
		return loginInfo.get("ID").toString();
	}
	

	/**获取用户权限或者职位
	 * */
	public static int getPostion()
	{

		if(loginInfo==null)
		{

			FileUtils.getMapData4SD();
			if (loginInfo == null)
			{
				return 0;
			}
		}
		return Integer.parseInt(loginInfo.get("Postion").toString());
	}
	
	/**获取用户所属组织id
	 * */
	public static HashMap<String, Object> getMyOrganization()
	{

		if(loginInfo==null)
		{
			FileUtils.getMapData4SD();
			if (loginInfo == null)
			{
				return null;
			}
		}
		return (HashMap<String, Object>) (loginInfo.get("Organization"));
	}

	/**获取用户所属组织id
	 * */
	public static String getOrganizationID()
	{

		if(loginInfo==null)
		{
			FileUtils.getMapData4SD();
			if (loginInfo == null)
			{
				return "";
			}
		}
		return ((Map)loginInfo.get("Organization")).get("ID").toString();
	}
	
	
}
