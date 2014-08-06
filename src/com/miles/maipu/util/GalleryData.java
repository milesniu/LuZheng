package com.miles.maipu.util;

import android.graphics.Bitmap;

public class GalleryData
{
	private Bitmap bitdata;
	private String path;
	private String urlPath;
	public Bitmap getBitdata()
	{
		return bitdata;
	}
	public void setBitdata(Bitmap bitdata)
	{
		this.bitdata = bitdata;
	}
	public String getPath()
	{
		return path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public GalleryData(Bitmap bitdata, String path)
	{
		super();
		this.bitdata = bitdata;
		this.path = path;
	}
	public String getUrlPath()
	{
		return urlPath;
	}
	public void setUrlPath(String urlPath)
	{
		this.urlPath = urlPath;
	}
	
	
}
