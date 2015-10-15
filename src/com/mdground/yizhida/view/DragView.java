package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class DragView extends ImageButton {

	private Context mContext;

	private int screenWidth;
	private int screenHeight;

	public void setScreenSize(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}

	public DragView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		setListener();
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setListener();
	}

	public DragView(Context context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	private void setListener() {
//		this.setOnTouchListener(new OnTouchListener() {
//			int lastX, lastY;
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				int ea = event.getAction();
//				switch (ea) {
//				case MotionEvent.ACTION_DOWN:
//					lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
//					lastY = (int) event.getRawY();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					int dx = (int) event.getRawX() - lastX;
//					int dy = (int) event.getRawY() - lastY;
//					int l = v.getLeft() + dx;
//					int b = v.getBottom() + dy;
//					int r = v.getRight() + dx;
//					int t = v.getTop() + dy;
//					// 下面判断移动是否超出屏幕
//					if (l < 0) {
//						l = 0;
//						r = l + v.getWidth();
//					}
//					if (t < 0) {
//						t = 0;
//						b = t + v.getHeight();
//					}
//					if (r > screenWidth) {
//						r = screenWidth;
//						l = r - v.getWidth();
//					}
//					if (b > screenHeight) {
//						b = screenHeight;
//						t = b - v.getHeight();
//					}
//					v.layout(l, t, r, b);
//					lastX = (int) event.getRawX();
//					lastY = (int) event.getRawY();
//					v.postInvalidate();
//					break;
//				case MotionEvent.ACTION_UP:
//					break;
//				}
//				return false;
//			}
//		});
//	}
	}

}
