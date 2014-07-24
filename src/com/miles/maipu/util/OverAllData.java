package com.miles.maipu.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

public class OverAllData
{
	public static String TitleName = "路政巡查";
	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng" + File.separator;
	private static HashMap<String, Object> loginInfo = null;
	public static Map<String, Object> Weathermap = null;
	
	
	
	public static void SetLogininfo(HashMap<String, Object> info)
	{
		loginInfo = info;
	}
	
	/**获取签到id
	 * */
	public static String getRecordId()
	{
		if(loginInfo==null)
		{
			return "";
		}
		else
		{
			return ((Map)loginInfo.get("PatorlRecord")).get("ID").toString();
		}
	}
	
	/**获取签到id
	 * */
	public static int getPatorlType()
	{
		if(loginInfo==null)
		{
			return 0;
		}
		else
		{
			try
			{
				return Integer.parseInt(((Map)loginInfo.get("PatorlRecord")).get("PatorlType").toString());
			}catch(Exception e)
			{
				return 0;
			}
		}
	}
	
	
	/**获取签到id
	 * */
	public static boolean setRecordId(String id)
	{
		if(loginInfo==null)
		{
			return false;
		}
		else
		{
			 ((Map)loginInfo.get("PatorlRecord")).put("ID", id);
			 return true;
		}
	}
	

	/**获取登陆id
	 * */
	public static String getLoginId()
	{

		if(loginInfo==null)
		{
			return "";
		}
		else
		{
			return loginInfo.get("ID").toString();
		}
	}
	

	/**获取用户权限或者职位
	 * */
	public static String getPostion()
	{

		if(loginInfo==null)
		{
			return "";
		}
		else
		{
			return loginInfo.get("Postion").toString();
		}
	}
	

	/**获取用户所属组织id
	 * */
	public static String getOrganizationID()
	{

		if(loginInfo==null)
		{
			return "";
		}
		else
		{
			return ((Map)loginInfo.get("Organization")).get("ID").toString();
		}
	}
	
	
}
