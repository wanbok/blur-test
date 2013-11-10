/**
 * 
 */
package com.mintech.blur;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * @author wanbok
 *
 */
public class MainScrollView extends ScrollView {

	private ScrollView delegateScrollView;
	/**
	 * @param context
	 */
	public MainScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MainScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MainScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


	public void setDelegateScrollView(ScrollView mBluredScrollView) {
		this.delegateScrollView = mBluredScrollView;
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
