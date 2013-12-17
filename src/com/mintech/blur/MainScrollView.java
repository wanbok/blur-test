/**
 * 
 */
package com.mintech.blur;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
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
	private GestureDetectorCompat mGestureDetector;
	private float mScaleFactor = 1.f;
	private float mDeltaScaleFactor;
	private BluredScrollView delegateScrollView;
	private static final float MIN_ZOOM_AMOUNT = 1.0f;
	private static final float MAX_ZOOM_AMOUNT = 2.0f;
    private static final int FPS = 30;
    private static boolean isZoomIn = true;
    private static long lastTime;
    private Handler mHandler = new Handler();
    private final Runnable mUpdateRunnable = new Runnable() {
        public void run() {
        	final long startTime = SystemClock.uptimeMillis();
        	float deltaTime = (float)(lastTime - startTime) / 100.f;
            mDeltaScaleFactor = isZoomIn ? 1.f - deltaTime : 1.f + deltaTime;
            if (update()) {
                lastTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(mUpdateRunnable, 1000 / FPS - (lastTime - startTime));
            }
        }
    };
    
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
			containerRef.get().mDeltaScaleFactor = detector.getScaleFactor();
			containerRef.get().scale();
			return true;
		}
	}
	
	private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
		private WeakReference<MainScrollView> containerRef;

		public DoubleTapListener(MainScrollView container)
		{
			super();
			containerRef = new WeakReference<MainScrollView>(container);
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent ev) {
			isZoomIn = Math.abs(MAX_ZOOM_AMOUNT-mScaleFactor) > Math.abs(MIN_ZOOM_AMOUNT-mScaleFactor);
            lastTime = SystemClock.uptimeMillis();
			mHandler.post(mUpdateRunnable);
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			containerRef.get().scrollBy((int)distanceX, (int)distanceY);
			((android.widget.HorizontalScrollView)containerRef.get().getChildAt(0)).scrollBy((int)distanceX, (int)distanceY);
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			containerRef.get().fling(-(int)velocityY);
			((android.widget.HorizontalScrollView)containerRef.get().getChildAt(0)).fling(-(int)velocityX);
			return super.onFling(e1, e2, velocityX, velocityY);
		}
		
	}
	
	public boolean update() {
		scale();
		boolean result = true;
		if (mScaleFactor >= MAX_ZOOM_AMOUNT) {
			mScaleFactor = MAX_ZOOM_AMOUNT;
			result = false;
		} else if (mScaleFactor <= MIN_ZOOM_AMOUNT){
			mScaleFactor = MIN_ZOOM_AMOUNT;
			result = false;
		}
		
		return result;
	}
	public void scale() {
		mScaleFactor *= mDeltaScaleFactor;

		// Don't let the object get too small or too large.
		mScaleFactor = Math.max(MIN_ZOOM_AMOUNT, Math.min(mScaleFactor, MAX_ZOOM_AMOUNT));
    	
		//Get Child object
		LinearLayout mainLinearLayout = (LinearLayout)((android.widget.HorizontalScrollView)this.getChildAt(0)).getChildAt(0);
		LinearLayout delegateLinearLayout = (LinearLayout)((android.widget.HorizontalScrollView)this.delegateScrollView.getChildAt(0)).getChildAt(0);

		float scrollX = this.getScrollX();
		float scrollY = this.getScrollY();
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

		this.scrollTo(
				Math.round(scrollX * mScaleFactor * refContentWidth / contentWidth),
				Math.round(scrollY * mScaleFactor * refContentHeight / contentHeight));
		this.invalidate();
		this.requestLayout();
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
		DoubleTapListener doubleTapListener = new DoubleTapListener(this);
		mGestureDetector = new GestureDetectorCompat(context, doubleTapListener);
		mGestureDetector.setOnDoubleTapListener(doubleTapListener);
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
		mScaleDetector.onTouchEvent(ev);
		mGestureDetector.onTouchEvent(ev);
		return true;
	}
}
