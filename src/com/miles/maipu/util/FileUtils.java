package com.miles.maipu.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;
import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

public class FileUtils
{
	// private String SDPATH;

	// public String getSDPATH()
	// {
	// return SDPATH;
	// }

	public FileUtils()
	{
		// SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	
	//读文件
	public static String readSDFile(File file) throws IOException {  


	        FileInputStream fis = new FileInputStream(file);  

	        int length = fis.available(); 

	         byte [] buffer = new byte[length]; 
	         fis.read(buffer);     

	         String res = EncodingUtils.getString(buffer, "UTF-8"); 

	         fis.close();     
	         return res;  
	}  

	//写文件
	public void writeSDFile(String fileName, String write_str) throws IOException{  

	        File file = new File(fileName);  

	        FileOutputStream fos = new FileOutputStream(file);  

	        byte [] bytes = write_str.getBytes(); 

	        fos.write(bytes); 

	        fos.close(); 
	} 
	
	
	public static void setMapData2SD(BaseMapObject obj)
	{
		ObjectOutputStream objOutput = null;
		try
		{
			if (obj != null)
			{
				objOutput = new ObjectOutputStream(new FileOutputStream(OverAllData.loginPath));
				objOutput.writeObject(obj);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (objOutput != null)
				{
					objOutput.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static void getMapData4SD()
	{
		ObjectInputStream objInput = null;
		// List<PushMessage> outmsglist = null;
		try
		{
			objInput = new ObjectInputStream(new FileInputStream(OverAllData.loginPath));
			OverAllData.SetLogininfo((BaseMapObject) objInput.readObject());

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				if (objInput != null)
					objInput.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}


	public static List<File> getFile(File file)
	{
		List<File> mFileList = new Vector<File>();

		File[] fileArray = file.listFiles();
		for (File f : fileArray)
		{
			if (f.isFile())
			{
				mFileList.add(f);
			} else
			{
				getFile(f);
			}
		}
		return mFileList;
	}

	public static String getFileName(String path)
	{
		String[] filearr = path.split("/");
		return filearr[filearr.length - 1];
	}
	
	
	public static String getPath(Context context, Uri uri)
	{

		if ("content".equalsIgnoreCase(uri.getScheme()))
		{
			String[] projection =
			{ "_data" };
			Cursor cursor = null;

			try
			{
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst())
				{
					return cursor.getString(column_index);
				}
			} catch (Exception e)
			{
				// Eat it
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme()))
		{
			return uri.getPath();
		}

		return null;
	}

	public void RecursionDeleteFile(File file)
	{

		if (file.isFile())
		{
			file.delete();
			return;
		}
		if (file.isDirectory())
		{
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0)
			{
				file.delete();
				return;
			}
			for (File f : childFile)
			{
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	// @SuppressWarnings(
	// { "unchecked", "resource" })
	// public static void getlistDat4SD()
	// {
	// ObjectInputStream objInput = null;
	// // List<PushMessage> outmsglist = null;
	// try
	// {
	// objInput = new ObjectInputStream(new
	// FileInputStream(OverAllData.largePath));
	// OverAllData.LargeCompany_list = (List<HashMap<String, Object>>)
	// objInput.readObject();
	//
	// objInput = new ObjectInputStream(new
	// FileInputStream(OverAllData.goodPath));
	// OverAllData.GoodCompany_list = (List<HashMap<String, Object>>)
	// objInput.readObject();
	//
	// objInput = new ObjectInputStream(new
	// FileInputStream(OverAllData.noticePath));
	// OverAllData.NoticeCompany_list = (List<HashMap<String, Object>>)
	// objInput.readObject();
	//
	// objInput = new ObjectInputStream(new
	// FileInputStream(OverAllData.bannerPath));
	// OverAllData.bannerList = (List<HashMap<String, Object>>)
	// objInput.readObject();
	//
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// } finally
	// {
	//
	// try
	// {
	// if (objInput != null)
	// objInput.close();
	// } catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	// }

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException
	{
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName)
	{
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	public static void saveMyBitmap(String bitName, Bitmap mBitmap)
	{
		File f = new File(OverAllData.SDCardRoot + bitName);
		try
		{
			f.createNewFile();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			// DebugMessage.put("在保存图片时出错：" + e.toString());
		}
		FileOutputStream fOut = null;
		try
		{
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			fOut.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName)
	{
		File file = new File(fileName);
		return file.exists();
	}

	public static String getFile(byte[] bfile, String filePath, String fileName)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory())
			{// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
			return file.getAbsolutePath();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			if (fos != null)
			{
				try
				{
					fos.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File writeToSDCard(String path, String fileName, InputStream input)
	{
		File file = null;
		OutputStream output = null;
		try
		{
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int len = 0;
			while ((len = input.read(buffer)) != -1)
			{
				output.write(buffer, 0, len);
			}
			output.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				output.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

}
