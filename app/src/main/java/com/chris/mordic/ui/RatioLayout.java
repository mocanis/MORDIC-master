package com.chris.mordic.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.chris.mordic.R;

public class RatioLayout extends FrameLayout {

	private float				mPicRatio	= 0f;				// 图片的宽高比
	public static final int	RELATIVE_WIDTH	= 0;				// 控件宽度固定,已知图片的宽高比,求控件的高度
	public static final int	RELATIVE_HEIGHT	= 1;				// 控件高度固定,已知图片的宽高比,求控件的宽度

	private int					mRelative	= RELATIVE_WIDTH;

	public void setPicRatio(float picRatio) {
		mPicRatio = picRatio;
	}

	public void setRelative(int relative) {
		mRelative = relative;
	}

	public RatioLayout(Context context) {
		this(context, null);
	}
	//xml文件中使用RatioLayout时会调用此方法来转化成代码
	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//得到RatioLayout的属性值
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		//R.styleable.RatioLayout_picRatpio是固定格式
		mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_pic_ratio, 0);

		mRelative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);
		//TypedArray是从一个特定pool中得到的，使用完TypedArray后需要释放以供复用
		typedArray.recycle();
	}
	//自定义控件一般会用到onMeasure(控制控件大小) onlayout(继承容器，要修改布局会用到) ondraw(自定义view)
	//此方法会被measure调用
	//思路：通过指定RatioLayout的宽/高来算出里面的ImageView的高度
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 控件宽度固定,已知图片的宽高比,求控件的高度
		int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);

		// 控件高度固定,已知图片的宽高比,求控件的宽度
		int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		//如果宽度被指定
		if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_WIDTH) {// 控件宽度固定,已知图片的宽高比,求控件的高度
			// 得到父容器的宽度
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			// 得到孩子的宽度
			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
			// 控件的宽度/控件的高度 = mPicRatio;

			// 计算孩子的高度
			int childHeight = (int) (childWidth / mPicRatio + .5f);

			// 计算父容器的高度
			int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

			// 主动测绘孩子.固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

			// 设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);

		} else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_HEIGHT) {
			// 控件高度固定,已知图片的宽高比,求控件的宽度
			// 得到父亲的高度
			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

			// 得到孩子的高度
			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();

			// 控件的宽度/控件的高度 = mPicRatio;
			// 计算控件宽度
			int childWidth = (int) (childHeight * mPicRatio + .5f);

			// 得到父亲的宽度
			int parentWidth = childWidth + getPaddingRight() + getPaddingLeft();

			// 主动测绘孩子.固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

			// 设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);

		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		}

	}
}
