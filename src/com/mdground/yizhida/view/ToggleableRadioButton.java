package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ToggleableRadioButton extends RadioButton {

	public ToggleableRadioButton(Context context) {
		super(context);
	}

	public ToggleableRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ToggleableRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void toggle() {
		if (isChecked()) {
			if (getParent() instanceof RadioGroup) {
				((RadioGroup) getParent()).clearCheck();
			}
		} else {
			setChecked(true);
		}
	}

}
