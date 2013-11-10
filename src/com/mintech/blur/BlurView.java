package com.mintech.blur;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BlurView extends ImageView {

	public BlurView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BlurView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BlurView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
}
