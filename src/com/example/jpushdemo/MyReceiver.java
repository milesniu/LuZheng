package com.example.jpushdemo;

import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import cn.jpush.android.api.JPushInterface;

import com.miles.maipu.luzheng.R;
import com.miles.maipu.luzheng.TaskInfoActivity;
import com.miles.maipu.util.JSONUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver
{
	private static final String TAG = "JPush";

	// 点击查看
	private Intent messageIntent = null;
	private PendingIntent messagePendingIntent = null;

	// 通知栏消息
	private int messageNotificationID = 1000;
	private Notification messageNotification = null;
	private NotificationManager messageNotificatioManager = null;

	@Override
	public void onReceive(Context context, Intent intent)
	{

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));

			// 打开自定义的Activity
			Intent i = new Intent(context, TestActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
		{
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else
		{
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle)
	{
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet())
		{
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 创建通知
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void setMsgNotification(Context mContext, String msg,Map<String, Object> reqMap)
	{
		
		if(reqMap==null)
			return;
		
		Intent intent = new Intent();
		try
		{
//			if (reqMap.get("Type").toString().equals("0"))
//			{

			if(reqMap.get("ID").toString().equals("-99"))
			{
				MediaPlayer player  = MediaPlayer.create(mContext,R.raw.fastspeedvoice);
	            player.start();
	            return;
//				intent.setClass(mContext, TestActivity.class);
//				intent.putExtra("content", reqMatp.get("Content").toString());
				
//				Toast.makeText(mContext, reqMap.get("Conten t")+"", 0).show();
//				return;
			}
			else
			{
				intent.setClass(mContext, TaskInfoActivity.class);
			}
				intent.putExtra("id", reqMap.get("ID").toString());

//			} 
//			else if (reqMap.get("Type").toString().equals("1"))
//			{
//				intent.setClass(mContext, EventInfoActivity.class);
//				intent.putExtra("id", reqMap.get("ID").toString());
//				intent.putExtra("time", reqMap.get("CreateTime") + "");
//			}
		} catch (Exception e)
		{
			return;
		}
		
		
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = msg;
		long when = System.currentTimeMillis();
		messageNotification = new Notification(icon, tickerText, when);
		// 放置在"正在运行"栏目中

		messageNotification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
		messageNotification.flags = messageNotification.FLAG_AUTO_CANCEL;

		RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.notify_view);
		contentView.setTextViewText(R.id.notify_name, "路政巡查系统");
		contentView.setTextViewText(R.id.notify_msg,reqMap.get("Content").toString());
		contentView.setTextViewText(R.id.notify_time, reqMap.get("CreateTime").toString().substring(5, 16));

		messageNotificatioManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

		// 指定个性化视图
		messageNotification.contentView = contentView;
		
		// 指定内容意图
		messageNotification.contentIntent = contentIntent;
		messageNotificatioManager.notify(messageNotificationID, messageNotification);
		messageNotificationID++;
	}


	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle)
	{
		if (MainActivity.isForeground)
		{
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			setMsgNotification(context,message, JSONUtil.getMapFromJson(extras));
			
		}
	}
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras))
//			{
//				try
//				{
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0)
//					{
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e)
//				{
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
