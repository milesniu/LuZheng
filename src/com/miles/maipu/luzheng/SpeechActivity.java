package com.miles.maipu.luzheng;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shoushuo.android.tts.ITts;
import com.shoushuo.android.tts.ITtsCallback;

public class SpeechActivity extends Activity {

	private EditText edtSpeectText;
	private Button btnSpeechGo;
	private Context context;
	private ITts ttsService;
	private boolean ttsBound;

	/**
	 * 定义Handler.
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(context, " 我的话说完了 ", Toast.LENGTH_SHORT).show();
			btnSpeechGo.setEnabled(true);
		}
	};

	/**
	 * 回调参数.
	 */
	private final ITtsCallback ttsCallback = new ITtsCallback.Stub() {
		//朗读完毕.
		@Override
		public void speakCompleted() throws RemoteException {
			handler.sendEmptyMessage(0);
		}
	};

	/**
	 * tts服务连接.
	 */
	private final ServiceConnection ttsConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			try {
				//注册回调参数
				ttsService.unregisterCallback(ttsCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			ttsService = null;
			ttsBound = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ttsService = ITts.Stub.asInterface(service);
			ttsBound = true;
			try {
				//tts服务初始化
				ttsService.initialize();
				//撤销回调参数.
				ttsService.registerCallback(ttsCallback);
			} catch (RemoteException e) {
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speech);
		context = this;
		edtSpeectText = (EditText) findViewById(R.id.edtSpeectText);
		btnSpeechGo = (Button) findViewById(R.id.btnSpeechGo);
	}

	/**
	 * 按钮：朗读.
	 * 
	 * @param v
	 */
	public void speechText(View v) {
		v.setEnabled(false);
		try {
			ttsService.speak(edtSpeectText.getText().toString(),
					TextToSpeech.QUEUE_FLUSH);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if (ttsBound) {
			ttsBound = false;
			//撤销tts服务
			this.unbindService(ttsConnection);
		}
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!ttsBound) {
			String actionName = "com.shoushuo.android.tts.intent.action.InvokeTts";
			Intent intent = new Intent(actionName);
			//绑定tts服务
			this.bindService(intent, ttsConnection, Context.BIND_AUTO_CREATE);
		}
	}

}