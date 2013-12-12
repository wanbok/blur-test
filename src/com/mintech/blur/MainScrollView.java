/**
 * 
 */
package com.mintech.blur;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * @author wanbok
 *
 */
public class MainScrollView extends ScrollView {

	private ScaleGestureDetector mScaleDetector;// = new ScaleGestureDetector(context, new ScaleListener());
	private float mScaleFactor = 1.f;
	private float mDeltaScaleFactor;
	private BluredScrollView delegateScrollView;
	private static final float MIN_ZOOM_AMOUNT = 1.0f;
	private static final float MAX_ZOOM_AMOUNT = 2.0f;
	private float refContentWidth;
	private float refContentHeight;
	private boolean shownForTheFirstTime = true;

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		private WeakReference<MainScrollView> containerRef;

		public ScaleListener(MainScrollView container)
		{
			super();

			containerRef = new WeakReference<MainScrollView>(container);
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mDeltaScaleFactor = detector.getScaleFactor(); 
			mScaleFactor *= mDeltaScaleFactor;

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(MIN_ZOOM_AMOUNT, Math.min(mScaleFactor, MAX_ZOOM_AMOUNT));
	    	
			//Get Child object
			LinearLayout mainLinearLayout = (LinearLayout)((android.widget.HorizontalScrollView)containerRef.get().getChildAt(0)).getChildAt(0);
			LinearLayout delegateLinearLayout = (LinearLayout)((android.widget.HorizontalScrollView)((MainScrollView)containerRef.get()).delegateScrollView.getChildAt(0)).getChildAt(0);

			float scrollX = containerRef.get().getScrollX();
			float scrollY = containerRef.get().getScrollY();
			float contentWidth = mainLinearLayout.getChildAt(0).getWidth();
			float contentHeight = 0.f;
			
			for (int i = 0; i < mainLinearLayout.getChildCount(); i++) {
				ScaledImageView imageView = (ScaledImageView)mainLinearLayout.getChildAt(i);
				ScaledImageView imageViewForDelegate = (ScaledImageView)delegateLinearLayout.getChildAt(i);
				contentHeight += imageView.getHeight(); // Getting heights before scaling. So scaled heights is mScaleFactor * heights
				imageView.scale(mScaleFactor);
				imageViewForDelegate.scale(mScaleFactor);
			}
			
	    	if(shownForTheFirstTime) {
	    		shownForTheFirstTime = false;

	    		refContentWidth  = contentWidth;
	    		refContentHeight = contentHeight;
	    	}

			containerRef.get().scrollTo(
					Math.round(scrollX * mScaleFactor * refContentWidth / contentWidth),
					Math.round(scrollY * mScaleFactor * refContentHeight / contentHeight));
			containerRef.get().invalidate();
			containerRef.get().requestLayout();

			return true;
		}
	}

	public MainScrollView(Context context) {
		super(context);
		init(context);
	}


	public MainScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MainScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener(this));
	}


	public void setDelegateScrollView(BluredScrollView mBluredScrollView) {
		this.delegateScrollView = mBluredScrollView;
		((HorizontalScrollView)this.getChildAt(0)).setDelegateScrollView((android.widget.HorizontalScrollView)mBluredScrollView.getChildAt(0));
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		HorizontalScrollView mainHorizontalScrollView = (HorizontalScrollView)this.getChildAt(0);
		mainHorizontalScrollView.onTouchEvent(ev);
		mScaleDetector.onTouchEvent(ev);
		return true;
	}
}
