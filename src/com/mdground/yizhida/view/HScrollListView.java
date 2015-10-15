package com.mdground.yizhida.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

public class HScrollListView extends ListView {
	private float mCurrentX = 0;
	private List<HorizontalScrollView> mHScrollViewList = new ArrayList<HorizontalScrollView>();

	public HScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HScrollListView(Context context) {
		super(context);
	}

	public HScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.mCurrentX = ev.getRawX();
			break;
		case MotionEvent.ACTION_UP:
			float x2 = ev.getRawX();
			if (mCurrentX - x2 != 0) {
				onScrollChanged((int) (mCurrentX - x2));
				return true;
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void onScrollChanged(int f) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			if (view instanceof HorizontalScrollView) {
				((HorizontalScrollView) view).smoothScrollBy(f, 0);
			} else if (view instanceof ViewGroup) {
				int c = ((ViewGroup) view).getChildCount();
				for (int j = 0; j < c; j++) {
					View v = ((ViewGroup) view).getChildAt(j);
					if (v instanceof HorizontalScrollView) {
						((HorizontalScrollView) v).smoothScrollBy(f, 0);
					}
				}
			}
			mHScrollViewList.get(i).smoothScrollBy(f, 0);
		}
	}

	public void addHScrollView(HorizontalScrollView hsv) {
		if (!this.mHScrollViewList.contains(hsv)) {
			this.mHScrollViewList.add(hsv);
		}
	}
}
