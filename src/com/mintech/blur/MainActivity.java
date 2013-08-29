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
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	private Context mContext;
	private WebView mWebView;
	private TextureView mTextureView;
	private ControllerView mController;
	private BlurView mBlurView;
	
	public Bitmap getBitmapForVisibleRegion(WebView webview) {
	    Bitmap returnedBitmap = null;
	    webview.setClipBounds(new Rect(0, 100, 0, 300));
	    webview.setDrawingCacheEnabled(true);	    
	    returnedBitmap = Bitmap.createBitmap(webview.getDrawingCache());
	    webview.setDrawingCacheEnabled(false);
	    webview.setClipBounds(new Rect(0, 0, 0, 0));
	    return returnedBitmap;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this.getApplicationContext();
		
		mWebView = (WebView)findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.loadUrl("http://wanbok.com/blur_for_one_image.html");
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
					mBlurView.setPicture(mWebView.capturePicture());
				} else{
					redirect = false; 
				}

			}
		});

		mBlurView = new BlurView(getBaseContext());
		mBlurView.setBackgroundColor(Color.argb(120, 50, 200, 50));
		RelativeLayout parentLayout = (RelativeLayout)findViewById(R.id.layout);
		parentLayout.addView(mBlurView);
		
//		mTextureView = (TextureView)findViewById(R.id.textureview);
		
		mController = (ControllerView) findViewById(R.id.controller);
		mController.setOnTouchListener(new OnTouchListener() {
			private float oldY = 0.f;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getRawY();
//				Float height = Float.valueOf(v.getLayoutParams().height);
//				mWebView.loadUrl("javascript:doBlur("+height.toString()+");");
				if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
					mController.resizeHeightByDrag(oldY, y);
				} else if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					mBlurView.setPicture(mWebView.capturePicture());
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
