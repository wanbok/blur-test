package com.mintech.blur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";
	private static final String URL = "http://mintshop.com/api/products/115310";
	private static final String WEB_IMAGES_KEY = "web_images";
	private static final String ID_KEY = "id";
	private static final String RAW_CROPPED_IMAGES_KEY = "raw_cropped_images";
	private static final String URL_KEY = "url";
	private static final String WIDTH_KEY = "width";
	private static final String HEIGHT_KEY = "height";
	private ArrayList<JSONObject> mOrderedImageUrlList;
	private ArrayList<Bitmap> mBitmapList;
	private ArrayList<Bitmap> mBluredBitmapList;
	private ArrayList<Integer> mAccumulatedHeight;
	private ControllerView mController;
	private MainScrollView mMainScrollView;
	private BluredScrollView mBluredScrollView;
	
	private RenderScript rs;

	private Handler mHandler;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);

		mHandler = new Handler();
		
		rs = RenderScript.create(getApplicationContext());

		mMainScrollView = (MainScrollView) findViewById(R.id.mainScrollView);
		mBluredScrollView = (BluredScrollView) findViewById(R.id.bluredScrollView);

		mMainScrollView.setDelegateScrollView(mBluredScrollView);

		mController = (ControllerView) findViewById(R.id.controller);
		mController.setReferenceView(mMainScrollView);
		mController.setOnTouchListener(new OnTouchListener() {
			private float oldY = 0.f;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getRawY();
				if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
					int top = mController.resizeHeightByDrag(oldY, y);
					syncBlurViewScroll(mMainScrollView.getScrollX(), mMainScrollView.getScrollY(), top);
				}
				oldY = y;
				return true;
			}
		});

		new JSONParse().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void syncBlurViewScroll(int x, int y, int top) {
		mBluredScrollView.setLayoutParams(mController.getLayoutParams());
		mBluredScrollView.scrollTo(x, y + top);
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();

			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(MainActivity.URL);
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				// Getting JSON Array
				JSONArray webImages = json.getJSONArray(WEB_IMAGES_KEY);

				// Change JSONArray to ArrayList. Because JSONArray can't be sorted. 
				ArrayList<JSONObject> list = CommonUtil.sortedListFromJSONArray(webImages, ID_KEY);

				mOrderedImageUrlList = new ArrayList<JSONObject>();
				mAccumulatedHeight = new ArrayList<Integer>();
				for (int i = 0; i < list.size(); i++) {
					JSONObject jsonObject = list.get(i);
					JSONArray jsonArray = jsonObject.getJSONArray(RAW_CROPPED_IMAGES_KEY);
					ArrayList<JSONObject> listForUrl = CommonUtil.sortedListFromJSONArray(jsonArray, URL_KEY);
					Integer accumulatedHeight = 0;
					for (int j = 0; j < listForUrl.size(); j++) {
						JSONObject jObj = listForUrl.get(j);
						mOrderedImageUrlList.add(jObj);
						accumulatedHeight += Integer.parseInt(jObj.getString(HEIGHT_KEY));
						mAccumulatedHeight.add(accumulatedHeight);
					}
				}
				System.out.println("Done parsing api.");

				mHandler.post(new Runnable() {

					@Override
					public void run() {
						LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.mainScrollLinearLayout);
						LinearLayout bluredLinearLayout = (LinearLayout) findViewById(R.id.bluredScrollLinearLayout);
						try {

							for (int i = 0; i < mOrderedImageUrlList.size(); i++) {
								JSONObject jObj = mOrderedImageUrlList.get(i);
								int width = Integer.parseInt(jObj.getString(WIDTH_KEY));
								int height = Integer.parseInt(jObj.getString(HEIGHT_KEY));
								LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(width, height);
								ImageView imageView = new ImageView(MainActivity.this);
								imageView.setScaleType(ScaleType.FIT_CENTER);
								imageView.setLayoutParams(Params);

								mainLinearLayout.addView(imageView);

								ImageView bluredImageView = new ImageView(MainActivity.this);
								bluredImageView.setScaleType(ScaleType.FIT_CENTER);
								bluredImageView.setLayoutParams(Params);
								bluredLinearLayout.addView(bluredImageView);
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						mBitmapList = new ArrayList<Bitmap>(mOrderedImageUrlList.size());
						mBluredBitmapList = new ArrayList<Bitmap>(mOrderedImageUrlList.size());
						try {
							for (int i = 0; i < mOrderedImageUrlList.size(); i++) {
								JSONObject imageInfo = mOrderedImageUrlList.get(i);
								Bitmap bitmap = BitmapFactory.decodeStream(CommonUtil.fetch(imageInfo.getString(URL_KEY)));
								mBitmapList.add(i, bitmap);
								
								final int index = i;
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {

										LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.mainScrollLinearLayout);
										Bitmap bitmap = mBitmapList.get(index);
										ImageView imageView = (ImageView)mainLinearLayout.getChildAt(index);

										// set the Drawable on the ImageView
										imageView.setImageBitmap(bitmap);
									}
								});
								
								Thread blurredThread = new Thread(new Runnable() {
									
									@Override
									public void run() {
										Bitmap bitmap = mBitmapList.get(index);
										int density = bitmap.getDensity();
										int width = (int)(bitmap.getWidth() / 2);
										int height = (int)(bitmap.getHeight() / 2);
										Bitmap bluredBitmap = Bitmap.createScaledBitmap(bitmap, (width > 0 ? width : 1), (height > 0 ? height : 1), true);
										blurByRenderScript(bluredBitmap, 5.0f);
										bluredBitmap.setDensity(density/2);
										mBluredBitmapList.add(index, bluredBitmap);
										
										mHandler.post(new Runnable() {
											
											@Override
											public void run() {
												LinearLayout bluredLinearLayout = (LinearLayout) findViewById(R.id.bluredScrollLinearLayout);
												Bitmap bluredBitmap = mBluredBitmapList.get(index);
												ImageView bluredImageView = (ImageView)bluredLinearLayout.getChildAt(index);

												// set the Drawable on the ImageView
												bluredImageView.setImageBitmap(bluredBitmap);
											}
										});
									}
								});
								
								blurredThread.start();
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				thread.start();
				
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						syncBlurViewScroll(mMainScrollView.getScrollX(), mMainScrollView.getScrollY(), mController.getTop());
					}
				}, 100);

//				mHandler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.mainScrollLinearLayout);
//						LinearLayout bluredLinearLayout = (LinearLayout) findViewById(R.id.bluredScrollLinearLayout);
//						for (int i = 0; i < mBitmapList.size(); i++) {
//							Bitmap bitmap = mBitmapList.get(i);
//							ImageView imageView = (ImageView)mainLinearLayout.getChildAt(i);
//
//							// set the Drawable on the ImageView
//							imageView.setImageBitmap(bitmap);
//
//							Bitmap bluredBitmap = mBluredBitmapList.get(i);
//							ImageView bluredImageView = (ImageView)bluredLinearLayout.getChildAt(i);
//
//							// set the Drawable on the ImageView
//							bluredImageView.setImageBitmap(bluredBitmap);
//						}
//						
//						mHandler.postDelayed(new Runnable() {
//							
//							@Override
//							public void run() {
//								syncBlurViewScroll(mMainScrollView.getScrollX(), mMainScrollView.getScrollY(), mController.getTop());
//							}
//						}, 100);
//					}
//				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private void blurByRenderScript(Bitmap source, float radius) {
		final Allocation input = Allocation.createFromBitmap(rs, source); //, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped( rs, input.getType() );
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
		script.setRadius( 3.f );
		script.setInput(input);
		script.forEach(output);
		output.copyTo(source);
	}

}
