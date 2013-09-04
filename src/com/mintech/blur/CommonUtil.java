package com.mintech.blur;

import android.view.View;
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

}
