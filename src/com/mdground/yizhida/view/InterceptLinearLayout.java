package com.mdground.yizhida.view;

import com.mdground.yizhida.api.utils.L;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptLinearLayout extends LinearLayout {

	boolean touchOn;
	boolean mDownTouch = false;

	public InterceptLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InterceptLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			touchOn = !touchOn;
			invalidate();

			mDownTouch = true;
			return true;

		case MotionEvent.ACTION_UP:
			if (mDownTouch) {
				mDownTouch = false;
				performClick(); // Call this method to handle the response, and
								// thereby enable accessibility services to
								// perform this action for a user who cannot
								// click the touchscreen.
				return true;
			}
		}
		return false; // Return false for other touch events
	}
}
