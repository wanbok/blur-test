package com.mintech.blur;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class ControllerView extends View {

	public ControllerView(Context context) {
		super(context);
	}
	
	public ControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ControllerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void resizeHeightByDrag(float oldY, float newY) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
		params.height += oldY - newY;
		this.setLayoutParams(params);
	}
}
