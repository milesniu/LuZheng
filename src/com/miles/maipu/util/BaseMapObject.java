package com.miles.maipu.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseMapObject extends HashMap<String, Object> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716948129426981023L;

	/**
	 * 
	 */

	public static BaseMapObject HashtoMyself(HashMap<String, Object> map)
	{
		if(map==null)
			return null;
		BaseMapObject tmp = new BaseMapObject();
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		while (iter.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			tmp.put(entry.getKey().toString(), entry.getValue());
		}

		return tmp;
	}
	
}
