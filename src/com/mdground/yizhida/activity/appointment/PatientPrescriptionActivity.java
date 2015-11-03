package com.mdground.yizhida.activity.appointment;

import com.mdground.yizhida.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientPrescriptionActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_title, tv_top_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_prescription);

		findViewById();
		init();
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);
	}
	
	private void init() {
		tv_title.setText(R.string.signs);
		tv_top_right.setText(R.string.save);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:
			
			break;

		}
	}

}
