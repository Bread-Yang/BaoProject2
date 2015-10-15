package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;

/**
 * 病历信息布局
 * @author Administrator
 *
 */
public class PatientRecordLayout extends RelativeLayout{

	private TextView TvRecordOne;
	private TextView TvRecordTwo;
	private TextView TvRecordThree;
	
	private View view;
	
	public PatientRecordLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		view = LayoutInflater.from(context).inflate(R.layout.patient_record_info_layout, null);
	    this.addView(view);
	    findView();
	}

	public PatientRecordLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.patient_basic_info__layout, null);
		this.addView(view);
		findView();
	}

	public PatientRecordLayout(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(R.layout.patient_basic_info__layout, null);
		this.addView(view);
		findView();
	}
	
	private void findView(){
		TvRecordOne = (TextView) view.findViewById(R.id.record_one_value);
		TvRecordTwo = (TextView) view.findViewById(R.id.record_two_value);
		TvRecordThree = (TextView) view.findViewById(R.id.record_three_value);
	}
	
	/**
	 * 这个方法把数据对象传过来，并且初始化控件值。
	 * @param object
	 */
	public void initData(Object object){
		
	}

}
