package com.miles.maipu.view;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.graphics.Bitmap;

public class MyPhotoView extends PhotoView{

	public MyPhotoView(Context context) {
		super(context);
	}
	
	/**
     * 初始化ImageView图片
     * @param bitmap
     */
    public void setImage(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }
}
