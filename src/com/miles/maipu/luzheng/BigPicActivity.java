package com.miles.maipu.luzheng;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class BigPicActivity extends Activity
{
	public static Bitmap bitmap = null;
	private Matrix matrix = new Matrix();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		ImageView img = (ImageView) findViewById(R.id.image_pic);

		if (bitmap != null)
		{

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			if (w > h)
			{
				matrix.setRotate(90);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			}

			img.setImageBitmap(bitmap);
		} else
		{
			Toast.makeText(BigPicActivity.this, "null img", 0).show();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		bitmap = null;
		super.onDestroy();
	}

}
