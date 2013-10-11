package com.mintech.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Bitmap.Config;
import android.util.DisplayMetrics;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class BlurEngine {
	
	private Context mContext;
	
	private RenderScript mRS;
	private ScriptC_Gaussian mGaussianScript;

	public BlurEngine(Context context) {
		mContext = context;
		mRS = RenderScript.create(mContext);
		mGaussianScript = new ScriptC_Gaussian(mRS);
	}

	private void gBlur(Bitmap bitmap) {
		
	    Allocation in = Allocation.createFromBitmap(mRS, bitmap);
	    Allocation out = Allocation.createTyped(mRS, in.getType());
	    
	    mGaussianScript.set_gIn(in);
	    mGaussianScript.set_gOut(out);
	    mGaussianScript.set_gScript(mGaussianScript);
	    mGaussianScript.invoke_filter();
	    out.copyTo(bitmap);
	}

	public Bitmap picture2Bitmap(Picture picture) {
		Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.setDensity(DisplayMetrics.DENSITY_DEFAULT/8);
		canvas.drawPicture(picture);
		bitmap = Bitmap.createScaledBitmap(bitmap, picture.getWidth()/8, picture.getHeight()/8, true);
		gBlur(bitmap);
		return bitmap;
	}
}
