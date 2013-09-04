package com.mintech.blur;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	private Context mContext;
	private MainWebView mWebView;
	private ControllerView mController;
	private ScrollView mScrollView;
	private BlurView mBlurView;
	private Bitmap mBitmap;
	
	public Bitmap getBitmapForVisibleRegion(WebView webview) {
	    Bitmap returnedBitmap = null;
	    webview.setDrawingCacheEnabled(true);	    
	    returnedBitmap = Bitmap.createBitmap(webview.getDrawingCache());
	    webview.setDrawingCacheEnabled(false);
	    return returnedBitmap;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this.getApplicationContext();
		
		mWebView = (MainWebView)findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://mintech.kr/");
		mWebView.setWebChromeClient(new WebChromeClient());

		mWebView.setWebViewClient(new WebViewClient() {
			boolean loadingFinished = true;
			boolean redirect = false;
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
				if (!loadingFinished) {
					redirect = true;
				}

				loadingFinished = false;
				view.loadUrl(urlNewString);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				loadingFinished = false;
				//SHOW LOADING IF IT ISNT ALREADY VISIBLE  
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if(!redirect){
					loadingFinished = true;
				}

				if(loadingFinished && !redirect){
					//HIDE LOADING IT HAS FINISHED
					mBlurView.setAlpha(1.f);
					mBitmap = BlurEngine.pictureDrawable2Bitmap(mWebView.capturePicture());
					mBlurView.setImageBitmap(mBitmap);
					mBlurView.post(new Runnable() {
						@Override
						public void run() {
							mScrollView.scrollTo(mWebView.getScrollX(), mWebView.getScrollY() + mScrollView.getTop());
						}
					});
				} else{
					redirect = false; 
				}
			}
		});
		
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mWebView.setDelegateScrollView(mScrollView);
		mBlurView = (BlurView) findViewById(R.id.blurView);
		
		mController = (ControllerView) findViewById(R.id.controller);
		mController.setOnTouchListener(new OnTouchListener() {
			private float oldY = 0.f;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getRawY();
				if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
					mController.resizeHeightByDrag(oldY, y);
					mScrollView.scrollTo(mWebView.getScrollX(), mWebView.getScrollY() + mScrollView.getTop());
					mScrollView.setLayoutParams((RelativeLayout.LayoutParams) mController.getLayoutParams());
//					mScrollView.setScrollX(mWebView.getScrollX());
//					mScrollView.setScrollY(mWebView.getScrollY() + mScrollView.getTop());
//				} else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				}
				oldY = y;
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
