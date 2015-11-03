package com.mdground.yizhida.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;

@SuppressLint("ResourceAsColor")
public class RadioView extends LinearLayout implements OnClickListener{
	public static final int WATTING= 0 ;//候诊
	public static final int PASSED = 1;//过号
	public static final int AREADY_DIAGNOSIS =2;//已就诊
	
	private android.view.View mainView;
	private Context context;
	private LinearLayout radio1;
	private TextView radiotext1;
	private View radioline1;
	private LinearLayout radio2;
	private TextView radiotext2;
	private View radioline2;
	private LinearLayout radio3;
	private TextView radiotext3;
	private View radioline3;
	SelectListener selectListener;
	private int currentSelect = 0;

	public SelectListener getSelectListener() {
		return selectListener;
	}

	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}

	public static interface SelectListener {

		void onSelect(int i);
	}

	public RadioView(Context context) {
		super(context);
		this.context = context;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(initView(), layoutParams);
	}

	@SuppressLint("NewApi")
	public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.context = context;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(initView(), layoutParams);
	}

	public RadioView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		addView(initView(), layoutParams);
	}

	public android.view.View initView() {
		LayoutInflater layoutInflater = (LayoutInflater.from(context));
		mainView = layoutInflater.inflate(R.layout.layout_radio_group, null);
		radio1 = (LinearLayout) mainView.findViewById(R.id.radio1);
		radiotext1 = (TextView) mainView.findViewById(R.id.passed_title);
		radioline1 = (View) mainView.findViewById(R.id.radioline1);
		radio2 = (LinearLayout) mainView.findViewById(R.id.radio2);
		radiotext2 = (TextView) mainView.findViewById(R.id.radiotext2);
		radioline2 = (View) mainView.findViewById(R.id.radioline2);
		radio3 = (LinearLayout) mainView.findViewById(R.id.radio3);
		radiotext3 = (TextView) mainView.findViewById(R.id.radiotext3);
		radioline3 = (View) mainView.findViewById(R.id.radioline3);
		radio1.setOnClickListener(this);
		radio2.setOnClickListener(this);
		radio3.setOnClickListener(this);
		updatecheckView(radio1);
		return mainView;
	}

	public void onClick(android.view.View v) {
		updatecheckView(v);
	}

	public void updatecheckView(View view) {
		radiotext1.setTextColor(getResources().getColor(R.color.mynor_text));
		radiotext2.setTextColor(getResources().getColor(R.color.mynor_text));
		radiotext3.setTextColor(getResources().getColor(R.color.mynor_text));
		radioline1.setVisibility(View.GONE);
		radioline2.setVisibility(View.GONE);
		radioline3.setVisibility(View.GONE);

		if (view.getId() == radio1.getId()) {
			currentSelect =WATTING;
			radiotext1.setTextColor(getResources().getColor(R.color.mysel2_text));
			radioline1.setVisibility(View.VISIBLE);
		} else if (view.getId() == radio2.getId()) {
			currentSelect = PASSED;
			radiotext2.setTextColor(getResources().getColor(R.color.mysel2_text));
			radioline2.setVisibility(View.VISIBLE);
		} else if (view.getId() == radio3.getId()) {
			currentSelect = AREADY_DIAGNOSIS;
			radiotext3.setTextColor(getResources().getColor(R.color.mysel2_text));
			radioline3.setVisibility(View.VISIBLE);
		}
		
		if (selectListener != null) {
			selectListener.onSelect(currentSelect);
		}

	}

	public int getCurrentSelect() {
		return currentSelect;
	}
	
	public String getSelectText(){
		String selectText = "";
		switch (getCurrentSelect()) {
		case WATTING:
			selectText = "候诊";
			break;
		case PASSED:
			selectText = "过号";
			break;
		case AREADY_DIAGNOSIS:
			selectText = "已诊";
			break;

		default:
			break;
		}
		
		return selectText;
	}


}
