package com.chris.mordic.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.chris.mordic.R;

public class ChangeColorIconWithTextView extends View
{

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;
	/**
	 * 选中时颜色
	 */
	private int mColor = 0xef16a5e7;
	//private int mColor = 0x33333333;
	/**
	 * 透明度 0.0-1.0
	 */
	private float mAlpha = 0f;
	/**
	 * 图标
	 */
	private Bitmap mIconBitmap;
	/**
	 * 限制绘制icon的范围
	 */
	private Rect mIconRect;
	/**
	 * icon底部文本
	 */
	private String mText = "微信";
	private int mTextSize = (int) TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
	private Paint mTextPaint;
	private Rect mTextBound = new Rect();

	public ChangeColorIconWithTextView(Context context)
	{
		super(context);
	}

	/**
	 * 初始化自定义属性值
	 * 
	 * @param context
	 * @param attrs
	 */
	public ChangeColorIconWithTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// 获取设置的图标
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ChangeColorIconView);

		int n = a.getIndexCount();
		for (int i = 0; i < n; i++)
		{

			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.ChangeColorIconView_icon:
				BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
				mIconBitmap = drawable.getBitmap();
				break;
			case R.styleable.ChangeColorIconView_color:
				mColor = a.getColor(attr, 0x45C01A);
				break;
			case R.styleable.ChangeColorIconView_text:
				mText = a.getString(attr);
				break;
			case R.styleable.ChangeColorIconView_text_size:
				mTextSize = (int) a.getDimension(attr, TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
								getResources().getDisplayMetrics()));
				break;

			}
		}

		a.recycle();

		mTextPaint = new Paint();
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(0xff555555);
		// 得到text绘制范围
		mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 得到绘制icon的宽
		int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
				- getPaddingRight(), getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom() - mTextBound.height());

		int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
		int top = (getMeasuredHeight() - mTextBound.height()) / 2 - bitmapWidth
				/ 2;
		// 设置icon的绘制范围
		mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);

		/**   改动  **/
		int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		if(specWidthMode == MeasureSpec.EXACTLY){
			//得到父容器给指定的宽度
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

		}

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		/**
		 1、计算alpha（默认为0）
		 2、绘制原图

		 3、在绘图区域，绘制一个纯色块（设置了alpha），此步绘制在内存的bitmap上
		 4、设置mode，针对内存中的bitmap上的paint
		 5、绘制我们的图标，此步绘制在内存的bitmap上

		 6、绘制原文本
		 7、绘制设置alpha和颜色后的文本
		 8、将内存中的bitmap绘制出来
		 */

		int alpha = (int) Math.ceil((255 * mAlpha));
		canvas.drawBitmap(mIconBitmap, null, mIconRect, null);//画未选中时的图,即第二步绘制原图
		setupTargetBitmap(alpha);
		drawSourceText(canvas, alpha);//画SOURCE层的字,即未选中时显示的字
		drawTargetText(canvas, alpha);//画DST层的字,即选中时显示的字
		canvas.drawBitmap(mBitmap, 0, 0, null);//第8步，将内存中的bitmap绘制出来

	}
	
	private void setupTargetBitmap(int alpha)
	{
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);//防抖动，让颜色显示更平滑
		mPaint.setAlpha(alpha);//设置透明度
/**	因为没有下面两句，所以这里的	drawRect是画一个矩形板而不是矩形框
 * //paint.setStrokeWidth((float) 3.0);              //线宽
 * //paint.setStyle(Style.STROKE);                   //空心效果  */
		mCanvas.drawRect(mIconRect, mPaint);//画底色，即DST层.在绘图区域，绘制一个纯色块（设置了alpha），此步绘制在内存的bitmap上
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//显示底色中和图片交集的部分
		mPaint.setAlpha(255);
		mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);//画图片，即SRC层.绘制我们的图标，此步绘制在内存的bitmap上
	}

	private void drawSourceText(Canvas canvas, int alpha)
	{
		mTextPaint.setTextSize(mTextSize);
		Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fronts/unicodeui.ttf");
		mTextPaint.setTypeface(typeface);
		mTextPaint.setColor(0xff333333);
		mTextPaint.setAlpha(255 - 125);//设置未选中时字体的透明度
		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
				- mTextBound.width() / 2,
				mIconRect.bottom + mTextBound.height(), mTextPaint);
	}
	
	private void drawTargetText(Canvas canvas, int alpha)
	{
		mTextPaint.setColor(mColor);
		mTextPaint.setAlpha(alpha);
		canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
				- mTextBound.width() / 2,
				mIconRect.bottom + mTextBound.height(), mTextPaint);
		
	}

	

	

	public void setIconAlpha(float alpha)
	{
		this.mAlpha = alpha;
		invalidateView();
	}

	private void invalidateView()
	{
		if (Looper.getMainLooper() == Looper.myLooper())
		{
			invalidate();
		} else
		{
			postInvalidate();
		}
	}

	public void setIconColor(int color)
	{
		mColor = color;
	}

	public void setIcon(int resId)
	{
		this.mIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
		if (mIconRect != null)
			invalidateView();
	}

	public void setIcon(Bitmap iconBitmap)
	{
		this.mIconBitmap = iconBitmap;
		if (mIconRect != null)
			invalidateView();
	}

	private static final String INSTANCE_STATE = "instance_state";
	private static final String STATE_ALPHA = "state_alpha";

	@Override
	protected Parcelable onSaveInstanceState()
	{
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
		bundle.putFloat(STATE_ALPHA, mAlpha);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof Bundle)
		{
			Bundle bundle = (Bundle) state;
			mAlpha = bundle.getFloat(STATE_ALPHA);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
		} else
		{
			super.onRestoreInstanceState(state);
		}

	}

}
