package com.miles.maipu.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

public class OverAllData
{
	public static String TitleName = "路政巡查";
	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.miles.maipu.luzheng" + File.separator;
	public static HashMap<String, Object> loginInfo = new HashMap<String, Object>();
	public static Map<String, Object> Weathermap = null;
}
