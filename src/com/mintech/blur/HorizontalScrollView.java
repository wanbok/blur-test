package com.mintech.blur;

import android.content.Context;
import android.util.AttributeSet;

public class HorizontalScrollView extends android.widget.HorizontalScrollView {

	private android.widget.HorizontalScrollView delegateScrollView;
	
	public HorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDelegateScrollView(android.widget.HorizontalScrollView horizontalScrollView) {
		delegateScrollView = horizontalScrollView;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (delegateScrollView != null) {
			delegateScrollView.getChildAt(0).scrollTo(l, t);
		}
	}

}
