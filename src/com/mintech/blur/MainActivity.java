package com.mintech.blur;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	private Context mContext;
	private Handler mHandler;
	private MainWebView mWebView;
	private TextView mTextView;
	private ControllerView mController;
	private ScrollView mScrollView;
	private BlurView mBlurView;
	private Bitmap mBitmap;
	private BlurEngine mBlurEngine;
	
	private String htmlCountdownPreloader = "<!DOCTYPE html>" +
			"<html>" +
			"<head>" +
				"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=5.0\">" +
				"<script src='http://code.jquery.com/jquery-1.10.1.min.js'></script>" +
				"<script src='file:///android_asset/preload.js'></script>" +
			"</head>" +
			"<body>" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47372/mintshop_001.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47372/mintshop_002.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47372/mintshop_003.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47372/mintshop_004.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47373/mintshop_001.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47373/mintshop_002.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47373/mintshop_003.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47373/mintshop_004.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47373/mintshop_005.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
				"<img src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/production/CroppedImage/WebImage/47374/mintshop_001.JPEG' width=\"100%\" style=\"min-height: 1000px;\">" +
			"</body>" +
			"</html>";
	
	class WebAppInterface {
		
	    @JavascriptInterface
	    public void startToLoad() {
	    	mHandler.post(new Runnable() {

				@Override
				public void run() {
			        if (!mTextView.isShown()) mTextView.setVisibility(View.VISIBLE);
					mTextView.setText("0%");
				}
	    		
	    	});
	    }
	    
	    @JavascriptInterface
	    public void completeToLoad() {
	    	mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
			        captureWebView();
			        Animation fadeOut = new AlphaAnimation(1, 0);
			        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
			        fadeOut.setStartOffset(1000);
			        fadeOut.setDuration(1000);
			        mTextView.setAnimation(fadeOut);
			        mTextView.setVisibility(View.INVISIBLE);
			        mTextView.animate();
				}
			}, 100);
	    }

	    @JavascriptInterface
	    public void drawPercentage(final String percentage) {

	    	mHandler.post(new Runnable() {
				@Override
				public void run() {
					mTextView.setText(percentage);
				}
	    	});
	    }

	    @JavascriptInterface
	    public void failToLoad() {

	    	mHandler.post(new Runnable() {
				@Override
				public void run() {
					// TODO: 로딩 실패시 처리
				}
	    	});
	    }
	}
	
	public void captureWebView() {
		mBlurView.setAlpha(1.f);
		mBitmap = mBlurEngine.picture2Bitmap(mWebView.capturePicture());
		mBlurView.setImageBitmap(mBitmap);
		mBlurView.setBottom(mWebView.getHeight()); 
		mBlurView.post(new Runnable() {
			@Override
			public void run() {
				mScrollView.scrollTo(mWebView.getScrollX(), mWebView.getScrollY() + mScrollView.getTop());
			}
		});
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		mHandler = new Handler();
		
		mBlurEngine = new BlurEngine(mContext);
		
		mTextView = (TextView)findViewById(R.id.textView);
		
		mWebView = (MainWebView)findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new WebAppInterface(), "Android");
		mWebView.loadDataWithBaseURL("file:///android_asset/", htmlCountdownPreloader, "text/html", "UTF-8", "");
		mWebView.setWebChromeClient(new WebChromeClient());
		
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mWebView.setDelegateScrollView(mScrollView);
		mBlurView = (BlurView) findViewById(R.id.blurView);
		
		mController = (ControllerView) findViewById(R.id.controller);
		mController.setReferenceView(mWebView);
		mController.setOnTouchListener(new OnTouchListener() {
			private float oldY = 0.f;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getRawY();
				if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
					int top = mController.resizeHeightByDrag(oldY, y);
					mScrollView.setLayoutParams(mController.getLayoutParams());
					mScrollView.scrollTo(mWebView.getScrollX(), mWebView.getScrollY() + top);
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
