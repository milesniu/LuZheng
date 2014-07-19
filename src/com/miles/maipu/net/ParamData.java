package com.miles.maipu.net;

public class ParamData
{
	private ApiCode code;
	private String[] parms;
	
	
	
	public ParamData(ApiCode code, String... parms)
	{
		super();
		this.code = code;
		this.parms = parms;
	}
	
	

	public String[] getParms()
	{
		return parms;
	}
	public void setParms(String... parms)
	{
		this.parms = parms;
	}



	public ApiCode getCode()
	{
		return code;
	}



	public void setCode(ApiCode code)
	{
		this.code = code;
	}
	
	
	
}
