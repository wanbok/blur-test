package com.mintech.blur;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class CommonUtil {
	
	public static void moveView(View v, float scale) {
		LayoutParams params = (LayoutParams)v.getLayoutParams();
		v.setLayoutParams(params);
	}
	
	public static void scaleTwiceHeight(View v) {
		LayoutParams params = (LayoutParams)v.getLayoutParams();
		params.height = params.height * 2;
		v.setLayoutParams(params);
	}
	
	public static void stretchImageViewByScale(ImageView iv, float w, float h) {
		FrameLayout.LayoutParams pastParams = (FrameLayout.LayoutParams)iv.getLayoutParams();
		iv.setLayoutParams(new FrameLayout.LayoutParams(
	            FrameLayout.LayoutParams.MATCH_PARENT,
	            FrameLayout.LayoutParams.WRAP_CONTENT));
		iv.getLayoutParams().width = (int)(pastParams.width * w);
	    iv.getLayoutParams().height = (int)(pastParams.height * h);
	}

}
