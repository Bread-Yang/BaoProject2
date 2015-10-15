package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mdground.yizhida.R;
import com.mdground.yizhida.view.wheel.OnWheelChangedListener;
import com.mdground.yizhida.view.wheel.WheelView;
import com.mdground.yizhida.view.wheel.adapter.ArrayWheelAdapter;

public class TimeRangWheelView extends FrameLayout implements OnWheelChangedListener, View.OnClickListener {

	private String beginTimeArray[] = null;
	private String endTimeArray[] = null;
	java.text.DecimalFormat df = new java.text.DecimalFormat("00");
	private static final String timeSpit = ":";
	private WheelView beginTime;
	private WheelView endTime;
	private ImageView imgClose;

	private int nCurrentBeginItem = 0;
	private int nCurrentEndItem = 0;
	private View mView;
	private TimeChangeListener mTimeChangeListener;

	public static interface TimeChangeListener {
		public void onChangeTime(String begin, String end);

		public void onFinishInput();
	}

	public TimeRangWheelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeRangWheelView(Context context) {
		this(context, null, 0);
	}

	public TimeRangWheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mView = LinearLayout.inflate(context, R.layout.view_wheel_time, null);
		beginTime = (WheelView) mView.findViewById(R.id.wheel_time_begin);
		endTime = (WheelView) mView.findViewById(R.id.wheel_time_end);
		addView(mView);
		imgClose = (ImageView) mView.findViewById(R.id.img_close);
		imgClose.setOnClickListener(this);
		//
		beginTimeArray = initDate(0, 48);
		endTimeArray = initDate(1, 49);

		initWheelView(context, beginTime, beginTimeArray);
		initWheelView(context, endTime, endTimeArray);
		mView.setVisibility(View.GONE);
	}

	private void initWheelView(Context context, WheelView wheelView, String[] timeArray) {
		wheelView.setViewAdapter(new ArrayWheelAdapter<String>(context, timeArray));
		// wheelView.setCyclic(true);// 可循环滚动
		wheelView.setCurrentItem(0);// 初始化时显示的数据
		wheelView.addChangingListener(this);
	}

	public String getBeginTime() {
		return this.beginTimeArray[nCurrentBeginItem];
	}

	public void setBeginTime(String beginTime) {
		String[] times = beginTime.split(":");
		if (times == null || times.length != 2) {
			this.beginTime.setCurrentItem(0);
			return;
		}
		for (int i = 0; i < beginTimeArray.length; i++) {
			if (beginTimeArray[i].equals(beginTime)) {
				this.beginTime.setCurrentItem(i);
				break;
			}
		}
	}

	public String getEndTime() {
		return this.beginTimeArray[nCurrentEndItem];
	}

	public void setEndTime(String endTime) {
		String[] times = endTime.split(":");
		if (times == null || times.length != 2) {
			this.endTime.setCurrentItem(0);
			return;
		}

		for (int i = 0; i < endTimeArray.length; i++) {
			if (endTimeArray[i].equals(endTime)) {
				this.endTime.setCurrentItem(i);
				break;
			}
		}
	}

	/**
	 * 
	 * @param begin
	 *            0 ~ 47
	 * @param end
	 *            1 ~ 48
	 * @return
	 */
	private String[] initDate(int begin, int end) {
		String[] timeArray = new String[48];
		StringBuffer sb = new StringBuffer();
		int hour = 0;
		int min = 0;
		int index = 0;
		for (int i = begin; i < end; i++) {
			min = (i % 2) * 30;
			if (i > 0 && min % 60 == 0) {
				min = 0;
				hour++;
			}
			timeArray[index++] = sb.append(df.format(hour)).append(timeSpit).append(df.format(min)).toString();
			sb.delete(0, sb.length());
		}
		return timeArray;
	}

	@Override
	public synchronized void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel.getId() == R.id.wheel_time_begin) {
			this.nCurrentBeginItem = newValue;
			if (this.nCurrentBeginItem > this.nCurrentEndItem) {
				this.endTime.setCurrentItem(nCurrentBeginItem);
				this.nCurrentEndItem = nCurrentBeginItem;
			}
		} else if (wheel.getId() == R.id.wheel_time_end) {
			if (newValue < nCurrentBeginItem) {
				this.beginTime.setCurrentItem(newValue, true);
				this.nCurrentBeginItem = newValue;
			}
			this.nCurrentEndItem = newValue;
		}

		if (mTimeChangeListener != null) {
			mTimeChangeListener.onChangeTime(beginTimeArray[nCurrentBeginItem], endTimeArray[nCurrentEndItem]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_close:
			mView.setVisibility(View.GONE);
			if (mTimeChangeListener != null) {
				mTimeChangeListener.onFinishInput();
			}
			break;

		default:
			break;
		}
	}

	public TimeChangeListener getTimeChangeListener() {
		return mTimeChangeListener;
	}

	public void setTimeChangeListener(TimeChangeListener mTimeChangeListener) {
		if (this.mTimeChangeListener != null && mTimeChangeListener != this.mTimeChangeListener) {
			this.mTimeChangeListener.onFinishInput();
		}
		this.mTimeChangeListener = mTimeChangeListener;
	}

	public void show() {
		if (mView.getVisibility() == View.VISIBLE) {
			return;
		}
		mView.setVisibility(View.VISIBLE);
	}

	public void close() {
		mView.setVisibility(View.GONE);
		if (this.mTimeChangeListener != null) {
			this.mTimeChangeListener.onFinishInput();
		}
		this.mTimeChangeListener = null;
	}



}
