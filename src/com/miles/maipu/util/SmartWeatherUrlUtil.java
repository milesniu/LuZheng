package com.miles.maipu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;
/**
*JAVA版。
*调用方法：String apiUrl=SmartWeatherUrlUtil.getInterfaceURL(城市编号,数据类型);
*/
public class SmartWeatherUrlUtil {
	private final static String TAG = "WeatherUrlUtil";
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	private static final String appid = "9b478bf5282bcffc";
	private static final String private_key = "2822f4_SmartWeatherAPI_72cfb18";
	private static final String url_header="http://open.weather.com.cn/data/?";

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param url
	 *            被签名的字符串
	 * @param privatekey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	private static byte[] HmacSHA1Encrypt(String url, String privatekey)
			throws Exception {
		byte[] data = privatekey.getBytes(ENCODING);
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);
		byte[] text = url.getBytes(ENCODING);
		// 完成 Mac 操作
		return mac.doFinal(text);
	}
	/**
	 * 获取URL通过privatekey加密后的码
	 * @param url
	 * @param privatekey
	 * @return
	 * @throws Exception
	 */
	private static String getKey(String url, String privatekey) throws Exception {
		byte[] key_bytes = HmacSHA1Encrypt(url, privatekey);
		String base64encoderStr = Base64.encodeToString(key_bytes, Base64.NO_WRAP);
		return URLEncoder.encode(base64encoderStr, ENCODING);
	}
	/**
	 * 获得接口的URL地址
	 * @param areaid
	 * @param type
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private static String getInterfaceURL(String areaid,String type,String date) throws Exception{
		String keyurl=url_header+"areaid="+areaid+"&type="+type+"&date="+date+"&appid=";
		String key=computeSignature(keyurl+appid,private_key);
		String appid6 = appid.substring(0, 6);
		
		return keyurl+appid6+"&key=" + key;
	}
	 private static final String HMAC_SHA1 = "HmacSHA1";     
	      /**  
	       * 生成签名数据  
	       *   
	       * @param data 待加密的数据  
	       * @param key  加密使用的key  
	       * @throws InvalidKeyException  
	       * @throws NoSuchAlgorithmException  
	       */  
	      public static String getSignature(String data,String key) throws Exception{
	       byte[] keyBytes=key.getBytes();
	          SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);   
	          Mac mac = Mac.getInstance(HMAC_SHA1);   
	          mac.init(signingKey);   
	          byte[] rawHmac = mac.doFinal(data.getBytes());
	          StringBuilder sb=new StringBuilder();
	          for(byte b:rawHmac){
	           sb.append(byteToHexString(b));
	          }
	          return sb.toString();   
	      }
	      
	      private static String byteToHexString(byte ib){
	       char[] Digit={
	         '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
	       };
	       char[] ob=new char[2];
	       ob[0]=Digit[(ib>>>4)& 0X0f];
	       ob[1]=Digit[ib & 0X0F];
	       String s=new String(ob);
	       return s;         
	      }     
	  


	
	/**
	 * 计算签名
	 * @param baseString 明文
	 * @param keyString 私钥
	 * @return 秘钥
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String computeSignature(String baseString, String keyString)
			throws GeneralSecurityException, UnsupportedEncodingException {

		SecretKey secretKey = null;

		byte[] keyBytes = keyString.getBytes();
		secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

		Mac mac = Mac.getInstance("HmacSHA1");

		mac.init(secretKey);

		byte[] text = baseString.getBytes();

		return new String(Base64.encodeToString(mac.doFinal(text), Base64.DEFAULT)).trim();
	}
	
	private static String hmac_sha1(String key, String datas)  
    {  
        String reString = "";  
  
        try  
        {  
            byte[] data = key.getBytes("UTF-8");    
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称     
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");     
            //生成一个指定 Mac 算法 的 Mac 对象     
            Mac mac = Mac.getInstance("HmacSHA1");     
            //用给定密钥初始化 Mac 对象     
            mac.init(secretKey);      
                        
            byte[] text = datas.getBytes("UTF-8");      
            //完成 Mac 操作      
            byte[] text1 = mac.doFinal(text);     
                     
            reString = Base64.encodeToString(text1, Base64.DEFAULT);  
  
        } catch (Exception e)  
        {  
            // TODO: handle exception   
        }  
          
        return reString;  
    }  
	
	public static String getInterfaceURL(String areaid,String type){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        String date = dateFormat.format(new Date());
		//String type="forecast3d";//"index";//"forecast3d";"observe"
		try {
			return getInterfaceURL(areaid,type,date);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(),e.fillInStackTrace());
		}
		return null;
	}

}
