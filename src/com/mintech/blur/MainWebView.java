package com.mintech.blur;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.RelativeLayout;

public class MainWebView extends WebView {
	
	private ScrollView delegateScrollView;

	public MainWebView(Context context) {
		super(context);
	}

	public MainWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDelegateScrollView(ScrollView delegateScrollView) {
		this.delegateScrollView = delegateScrollView;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (delegateScrollView != null) {
			delegateScrollView.scrollTo(l, t + delegateScrollView.getTop());
		}
	}
	
	public static Point gapFromParentView(View v) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
		return new Point(params.leftMargin, params.topMargin);
	}
}
