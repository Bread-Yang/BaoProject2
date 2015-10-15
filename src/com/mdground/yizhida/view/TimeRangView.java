package com.mdground.yizhida.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;

public class TimeRangView extends FrameLayout implements OnClickListener, TimeRangWheelView.TimeChangeListener {

	TextView beginTime;
	TextView endTime;
	ImageView closeImageView;
	TimeRangChangeListener mTimeRangChangeListener;

	public static interface TimeRangChangeListener {
		public void onResetTime(TimeRangView view);

		public void onTimeRangChange(TimeRangView view, String beginTime, String endTime);

		public void onFinishInput(TimeRangView view);
	}

	public TimeRangView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeRangView(Context context) {
		this(context, null, 0);
	}

	public TimeRangView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View rangView = LayoutInflater.from(context).inflate(R.layout.view_rang_time, null);
		if (!isInEditMode()) {
			beginTime = (TextView) rangView.findViewById(R.id.tv_time_begin);
			endTime = (TextView) rangView.findViewById(R.id.tv_time_end);
			closeImageView = (ImageView) rangView.findViewById(R.id.img_close);
			// rangView.setOnClickListener(this);
			closeImageView.setOnClickListener(this);
			addView(rangView);
		}
		// setOnClickListener(this);
	}

	// 获取开始时间
	public String getBeginTime() {
		return this.beginTime.getText().toString();
	}

	public void setBeginTime(String beginTime) {
		if (beginTime == null) {
			return;
		}
		this.beginTime.setText(beginTime);
	}

	public void setEndTime(String endTime) {
		if (endTime == null) {
			return;
		}
		this.endTime.setText(endTime);
	}

	// 获取结束时间
	public String getEndTime() {
		return this.endTime.getText().toString();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.img_close) {
			// 清除时间
			cleanTime();
			return;
		}
	}

	private void cleanTime() {
		this.beginTime.setText("开始时间");
		this.endTime.setText("结束时间");
		this.closeImageView.setVisibility(View.GONE);
		if (this.mTimeRangChangeListener != null) {
			this.mTimeRangChangeListener.onResetTime(this);
		}
	}

	public void reset() {
		this.beginTime.setText("开始时间");
		this.endTime.setText("结束时间");
		this.closeImageView.setVisibility(View.GONE);
	}

	public TimeRangChangeListener getTimeRangChangeListener() {
		return mTimeRangChangeListener;
	}

	public void setTimeRangChangeListener(TimeRangChangeListener mTimeRangChangeListener) {
		this.mTimeRangChangeListener = mTimeRangChangeListener;
	}

	@Override
	public void onChangeTime(String begin, String end) {
		this.closeImageView.setVisibility(View.VISIBLE);
		this.beginTime.setText(begin);
		this.endTime.setText(end);
		if (this.mTimeRangChangeListener != null) {
			this.mTimeRangChangeListener.onTimeRangChange(this, begin, end);
		}
	}

	public void setEditing(boolean b) {
		if (b) {
			this.beginTime.setTextColor(Color.parseColor("#206ef0"));
			this.endTime.setTextColor(Color.parseColor("#206ef0"));
			if (beginTime.getText().toString().equals("开始时间") || endTime.getText().toString().equals("开始时间")) {
				this.closeImageView.setVisibility(View.GONE);
			} else {
				this.closeImageView.setVisibility(View.VISIBLE);
			}
		} else {
			this.closeImageView.setVisibility(View.GONE);
			this.beginTime.setTextColor(Color.BLACK);
			this.endTime.setTextColor(Color.BLACK);
		}
	}

	@Override
	public void onFinishInput() {
		this.closeImageView.setVisibility(View.GONE);
		setEditing(false);
		if (this.mTimeRangChangeListener != null) {
			this.mTimeRangChangeListener.onFinishInput(this);
		}
	}

	int position;

	public void setPostion(int position) {
		this.position = position;
	}

	public int getPostion() {
		return position;
	}

	int itemIndex;

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getItemIndex() {
		return this.itemIndex;
	}
}
