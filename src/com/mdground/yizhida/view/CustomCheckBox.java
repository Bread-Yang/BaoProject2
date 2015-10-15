package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;

public class CustomCheckBox extends FrameLayout implements View.OnClickListener {

	private static final int STATUS_SELECTED = 1;
	private static final int STATUS_UNSELECTED = 2;
	private static final int STATUS_ENABLE = 3;

	ImageView icon;
	TextView textView;
	boolean isEnable;
	boolean isSelected;
	OnSelectedChangeCallBack mCallBack;

	public static interface OnSelectedChangeCallBack {
		public void onSelectedChange(View view, boolean isSelected);
	}

	public CustomCheckBox(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomCheckBox(Context context) {
		this(context, null, 0);
	}

	public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		View rangView = LayoutInflater.from(context).inflate(R.layout.view_checkbox, null);
		if (!isInEditMode()) {
			icon = (ImageView) rangView.findViewById(R.id.img_icon);
			textView = (TextView) rangView.findViewById(R.id.tv_text);
			addView(rangView);
		}
		setOnClickListener(this);
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
		if (isEnable) {
			setIcon(STATUS_ENABLE);
		} else {
			if (isSelected) {
				setIcon(STATUS_SELECTED);
			} else {
				setIcon(STATUS_UNSELECTED);
			}
		}
	}

	public boolean isEnable() {
		return isEnable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if (isSelected) {
			setIcon(STATUS_SELECTED);
		} else {
			setIcon(STATUS_UNSELECTED);
		}
	}

	public OnSelectedChangeCallBack getOnSelectedChangeCallBack() {
		return mCallBack;
	}

	public void setOnSelectedChangeCallBack(OnSelectedChangeCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	@Override
	public void onClick(View v) {
		if (mCallBack == null) {
			return;
		}

		if (isEnable) {
			return;
		}

		isSelected = !isSelected;
		mCallBack.onSelectedChange(this, isSelected);

		if (isSelected) {
			setIcon(STATUS_SELECTED);
		} else {
			setIcon(STATUS_UNSELECTED);
		}
	}

	private void setIcon(int status) {
		if (icon == null) {
			return;
		}

		if (isEnable) {
			icon.setImageResource(R.drawable.sel_off);
			return;
		}

		switch (status) {
		case STATUS_SELECTED:
			icon.setImageResource(R.drawable.sel_icon2);
			break;
		case STATUS_UNSELECTED:
			icon.setImageResource(R.drawable.sel_nor);
			break;
		case STATUS_ENABLE:
			icon.setImageResource(R.drawable.sel_off);
			break;

		default:
			break;
		}
	}

	public void setText(String text) {
		this.textView.setText(text);
	}
}
