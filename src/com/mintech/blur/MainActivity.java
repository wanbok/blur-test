package com.mintech.blur;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	public static final String TAG = "MainActivity";
	private static final String URL = "http://mintshop.com/api/products/115310";
	private static final String HTML = "<!DOCTYPE html><html> <head> <meta name='viewport' content='width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=5.0'> <script src='http://mintshop.com/lazy_loader.js'></script> <style>img { display: block; } </style> </head>" +
			"<body> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518712/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518712/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518712/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518712/mintshop_0004.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518713/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518713/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518713/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518714/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518714/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518715/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518715/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518716/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518716/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518717/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518717/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518718/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518718/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518719/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518719/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518720/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518720/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518720/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518721/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518721/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518722/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518722/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518723/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518723/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518724/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518724/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518725/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518725/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518726/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518726/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518727/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518727/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518727/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518728/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518728/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518729/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518729/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518730/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518730/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518730/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518731/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518731/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518732/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518732/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518733/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518733/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518734/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518734/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518735/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518735/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518735/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518736/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518736/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518736/mintshop_0003.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518737/mintshop_0001.JPEG' width='100%' style='min-height: 1000px;'> <img lazy_src='https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_f46e842e-c688-460e-a70b-e6a4d30e9885/mintshopimage/CroppedImage/WebImage/518737/mintshop_0002.JPEG' width='100%' style='min-height: 1000px;'> </body></html>";
	private static final String URL_FOR_BLUR = "http://10.0.1.18:3000/capture";
//	private static final String PARAMS_FOR_BLUR = "product=115310&html="+HTML;
	private static final String PARAMS_FOR_BLUR = "product=115310&width=591";
	private MainWebView mWebView;
	private ControllerView mController;
	private WebView mBlurView;
	private Boolean isWebViewLoaded = false;
	private Boolean isBlurViewLoaded = false;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
		
//		mTextView = (TextView)findViewById(R.id.textView);
		
		mWebView = (MainWebView)findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadData(HTML, "text/html", null);
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
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if(!redirect){
					loadingFinished = true;
				}

				if(loadingFinished && !redirect){
					mBlurView.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							syncBlurViewScroll(mWebView.getScrollX(), mWebView.getScrollY(), mController.getTop());							
						}
					}, 500);
				} else{
					redirect = false; 
				}
			}
		});

		mBlurView = (WebView) findViewById(R.id.blurView);
		mBlurView.getSettings().setJavaScriptEnabled(true);
		mBlurView.postUrl(URL_FOR_BLUR, EncodingUtils.getBytes(PARAMS_FOR_BLUR, "BASE64"));
		mBlurView.setWebChromeClient(new WebChromeClient());
		mBlurView.setWebViewClient(new WebViewClient() {
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
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if(!redirect){
					loadingFinished = true;
				}

				if(loadingFinished && !redirect){
					mBlurView.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							syncBlurViewScroll(mWebView.getScrollX(), mWebView.getScrollY(), mController.getTop());							
						}
					}, 500);
				} else{
					redirect = false; 
				}
			}
		});
		
		mWebView.setDelegateScrollView(mBlurView);
		
		mController = (ControllerView) findViewById(R.id.controller);
		mController.setReferenceView(mWebView);
		mController.setOnTouchListener(new OnTouchListener() {
			private float oldY = 0.f;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getRawY();
				if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
					int top = mController.resizeHeightByDrag(oldY, y);
					syncBlurViewScroll(mWebView.getScrollX(), mWebView.getScrollY(), top);
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
	
	private void syncBlurViewScroll(int x, int y, int top) {
		mBlurView.setLayoutParams(mController.getLayoutParams());
		mBlurView.scrollTo(x, y + top);
	}
}
