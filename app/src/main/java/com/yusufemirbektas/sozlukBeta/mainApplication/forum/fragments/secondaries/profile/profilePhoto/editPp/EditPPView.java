package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.profilePhoto.editPp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.Nullable;

import com.yusufemirbektas.sozlukBeta.R;


public class EditPPView extends View {
    private Bitmap mImage;
    private Bitmap mImageGrill;
    private Rect myRect;
    private Paint mPaint;

    public EditPPView(Context context) {
        super(context);
        onInit(null);
    }

    public EditPPView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onInit(attrs);
    }

    public EditPPView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit(attrs);
    }

    public EditPPView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onInit(attrs);
    }

    public void setBitmapRsc(Bitmap mImage){
        this.mImage = mImage;
    }

    private void onInit(@Nullable AttributeSet attrs){

        mImageGrill = BitmapFactory.decodeResource(getResources(), R.drawable.edit_pp_grill);
        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.basic_rectangle);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mImageGrill = getResizedBitmap(mImageGrill, getWidth(), getHeight());
                mImage = getResizedBitmap(mImage, getWidth(),getHeight());
            }


        });
        Rect  myRect = new Rect();
        myRect.left = 50;
        myRect.top = 50;
        myRect.bottom = myRect.top + 50;
        myRect.right = myRect.left + 50;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);


    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(myRect, mPaint);
        //canvas.drawBitmap(mImage,0,0,null);
        //canvas.drawBitmap(mImageGrill, 0, 0, null);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int reqWidth, int reqHeight ){

        Matrix matrix = new Matrix();

        RectF src = new RectF(0,0, bitmap.getWidth(), bitmap.getHeight());
        RectF dst = new RectF(0, 0, reqWidth, reqHeight);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }
}
