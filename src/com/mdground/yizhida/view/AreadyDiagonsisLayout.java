package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;

/**
 * 病人基本信息
 * @author Administrator
 *
 */
public class AreadyDiagonsisLayout extends RelativeLayout{

	private TextView TvDiagonsisDate;
	private TextView TvWaitingDate;
	private TextView TvRegisterDate;
	private TextView TvRegisterSource;
	
	private View view;
	
	public AreadyDiagonsisLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		view = LayoutInflater.from(context).inflate(R.layout.aready_diagnosis_info_layout, null);
	    this.addView(view);
	    findView();
	}

	public AreadyDiagonsisLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.aready_diagnosis_info_layout, null);
		this.addView(view);
		findView();
	}

	public AreadyDiagonsisLayout(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(R.layout.aready_diagnosis_info_layout, null);
		this.addView(view);
		findView();
	}
	
	private void findView(){
		TvDiagonsisDate = (TextView) view.findViewById(R.id.diagnosis_value);
		TvWaitingDate = (TextView) view.findViewById(R.id.waiting_value);
		TvRegisterDate = (TextView) view.findViewById(R.id.register_value);
		TvRegisterSource = (TextView) view.findViewById(R.id.register_source_value);
	}
	
	/**
	 * 这个方法把数据对象传过来，并且初始化控件值。
	 * @param object
	 */
	public void initData(Object object){
		
	}

}
