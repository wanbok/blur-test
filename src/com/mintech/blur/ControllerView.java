package com.mintech.blur;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ControllerView extends View {
	
	private View referenceView;

	public ControllerView(Context context) {
		super(context);
	}
	
	public ControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ControllerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * @param oldY
	 * @param newY
	 * @return provide top distance from parent view. Because getTop() is lazy.
	 */
	public int resizeHeightByDrag(float oldY, float newY) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
		params.height += oldY - newY;
		int referenceViewHeight = this.referenceView.getHeight();
		if (params.height > referenceViewHeight) {
			params.height = referenceViewHeight;
		}
		this.setLayoutParams(params);
		return referenceViewHeight - params.height;
	}

	public void setReferenceView(View referenceView) {
		this.referenceView = referenceView;
	}
	
}
