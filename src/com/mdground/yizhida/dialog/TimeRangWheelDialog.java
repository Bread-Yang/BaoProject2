package com.mdground.yizhida.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mdground.yizhida.R;
import com.mdground.yizhida.view.wheel.OnWheelChangedListener;
import com.mdground.yizhida.view.wheel.WheelView;
import com.mdground.yizhida.view.wheel.adapter.ArrayWheelAdapter;

public class TimeRangWheelDialog extends Dialog implements OnWheelChangedListener, View.OnClickListener {

	private String timeArray[] = null;
	java.text.DecimalFormat df = new java.text.DecimalFormat("00");
	private static final String timeSpit = ":";
	private WheelView beginTime;
	private WheelView endTime;
	private ImageView imgClose;

	private int nCurrentBeginItem = 0;
	private int nCurrentEndItem = 0;

	private static TimeRangWheelDialog instance = null;
	private TimeChangeListener mTimeChangeListener;

	public static interface TimeChangeListener {
		public void onChangeTime(String begin, String end);
	}

	protected TimeRangWheelDialog(Context context) {
		super(context);
		View view = LinearLayout.inflate(getContext(), R.layout.view_wheel_time, null);
		beginTime = (WheelView) view.findViewById(R.id.wheel_time_begin);
		endTime = (WheelView) view.findViewById(R.id.wheel_time_end);
//		imgClose = (ImageView) view.findViewById(R.id.img_closed);
//		imgClose.setOnClickListener(this);
		initWheelView(beginTime);
		initWheelView(endTime);

		setContentView(view);

		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(false);
	}

	public static TimeRangWheelDialog getInstance(Context context) {
		if (instance == null) {
			instance = new TimeRangWheelDialog(context);
		}
		instance.beginTime.setCurrentItem(0);
		instance.endTime.setCurrentItem(0);
		return instance;
	}

	private void initWheelView(WheelView wheelView) {
//		wheelView.setAdapter(new ArrayWheelAdapter<String>(initDate()));
		// wheelView.setCyclic(true);// 可循环滚动
		wheelView.setCurrentItem(0);// 初始化时显示的数据
		wheelView.addChangingListener(this);
	}

	public String getBeginTime() {
		return this.timeArray[nCurrentBeginItem];
	}

	public String getEndTime() {
		return this.timeArray[nCurrentEndItem];
	}

	private String[] initDate() {
		if (this.timeArray != null) {
			return this.timeArray;
		} else {
			this.timeArray = new String[48];
		}
		StringBuffer sb = new StringBuffer();
		int hour = 0;
		int min = 0;
		for (int i = 0; i < this.timeArray.length; i++) {
			min = (i % 2) * 30;
			if (i > 0 && min % 60 == 0) {
				min = 0;
				hour++;
			}
			this.timeArray[i] = sb.append(df.format(hour)).append(timeSpit).append(df.format(min)).toString();
		}

		return this.timeArray;
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel.getId() == R.id.wheel_time_begin) {
			this.nCurrentBeginItem = newValue;
		} else if (wheel.getId() == R.id.wheel_time_end) {
			if (newValue < nCurrentBeginItem) {
				this.beginTime.setCurrentItem(newValue, true);
				this.nCurrentBeginItem = newValue;
			}
			this.nCurrentEndItem = newValue;
		}
		
		if (mTimeChangeListener != null) {
			mTimeChangeListener.onChangeTime(timeArray[nCurrentBeginItem], timeArray[nCurrentEndItem]);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.img_closed:
//			dismiss();
//			break;

		default:
			break;
		}
	}

	public TimeChangeListener getTimeChangeListener() {
		return mTimeChangeListener;
	}

	public void setTimeChangeListener(TimeChangeListener mTimeChangeListener) {
		this.mTimeChangeListener = mTimeChangeListener;
	}

}
