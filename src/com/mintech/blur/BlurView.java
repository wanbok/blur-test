package com.mintech.blur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BlurView extends View {
	
	private Picture mPicture;
    private Drawable mDrawable;
	
	public BlurView(Context context) {
		super(context);
		setPicture(new Picture());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 400);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.setMargins(0, 0, 0, 700);
		this.setLayoutParams(params);
	}

	public BlurView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BlurView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    static void drawSomething(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        p.setColor(0x88FF0000);
        canvas.drawCircle(50, 50, 40, p);
        
        p.setColor(Color.GREEN);
        p.setTextSize(30);
        canvas.drawText("Pictures", 60, 60, p);
    }
    
	@Override
	protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        canvas.drawPicture(mPicture);
        
        canvas.drawPicture(mPicture, new RectF(0, 100, getWidth(), 200));
        
        mDrawable.setBounds(0, 200, getWidth(), 300);
        mDrawable.draw(canvas);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mPicture.writeToStream(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        canvas.translate(0, 300);
        canvas.drawPicture(Picture.createFromStream(is));
	}

	public Picture getPicture() {
		return mPicture;
	}

	public void setPicture(Picture picture) {
		this.mPicture = picture;
        drawSomething(mPicture.beginRecording(picture.getWidth(), picture.getHeight()));
        mPicture.endRecording();
        mDrawable = new PictureDrawable(mPicture);
	}
	
}
