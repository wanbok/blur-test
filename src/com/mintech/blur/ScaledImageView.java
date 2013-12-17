package com.mintech.blur;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScaledImageView extends ImageView {
	
	private float refChildWidth;
	private float refChildHeight;
	private boolean shownForTheFirstTime = true;
	
	public ScaledImageView(Context context) {
		super(context);
		this.setClickable(false);
	}
    
    public void scale(float scaleFactor) {
    	
    	if(shownForTheFirstTime) {
    		shownForTheFirstTime = false;

    		refChildWidth  = this.getWidth();
    		refChildHeight = this.getHeight();
    	}

    	int width = Math.round(scaleFactor*refChildWidth);
    	int height = Math.round(scaleFactor*refChildHeight);
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
    	this.setLayoutParams(params);

    	this.invalidate();
    	this.requestLayout();
    }
}
