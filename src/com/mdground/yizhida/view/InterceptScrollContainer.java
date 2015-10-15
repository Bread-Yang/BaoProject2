package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/*
 * 
 * 一个视图容器控件
 * 阻止 拦截 ontouch事件传递给其子控件
 * */
public class InterceptScrollContainer extends LinearLayout {

	private VelocityTracker velocityTracker;

	public InterceptScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterceptScrollContainer(Context context) {
		super(context);
	}

	//
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// //return super.dispatchTouchEvent(ev);
	// Log.i("pdwy","ScrollContainer dispatchTouchEvent");
	// return true;
	// }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return true;
	}
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// Log.i("pdwy","ScrollContainer onTouchEvent");
	// return true;
	// }
}
