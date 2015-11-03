package com.mdground.yizhida.activity.appointment;

import org.apache.http.Header;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveChiefComplaint;
import com.mdground.yizhida.bean.ChiefComplaint;
import com.mdground.yizhida.constant.MemberConstant;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PatientChiefComplaintActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_title, tv_top_right;
	private EditText et_description;
	
	private ChiefComplaint mChiefComplaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_chief_complaint);

		findViewById();
		init();
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		et_description = (EditText) findViewById(R.id.et_description);
	}

	private void init() {
		tv_title.setText(R.string.chief_complaint);
		tv_top_right.setText(R.string.save);
		tv_top_right.setOnClickListener(this);
		
		mChiefComplaint = getIntent().getParcelableExtra(MemberConstant.APPOINTMENT_CHIEF_COMPLAINT);
		
		if (mChiefComplaint != null) {
			String description  = mChiefComplaint.getDescription();
			
			if (!TextUtils.isEmpty(description)) {
				et_description.setText(description);
				
				int position = et_description.length();
				Editable etext = et_description.getText();
				Selection.setSelection(etext, position);
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:
			String desciption = et_description.getText().toString();

			if (TextUtils.isEmpty(desciption)) {
				Toast.makeText(getApplicationContext(), R.string.input_chief_complaint, Toast.LENGTH_LONG).show();
			} else {
				mChiefComplaint.setDescription(desciption);
				
				new SaveChiefComplaint(getApplicationContext()).saveChiefComplaint(mChiefComplaint, new RequestCallBack() {
					
					@Override
					public void onSuccess(ResponseData response) {
						finish();
					}
					
					@Override
					public void onStart() {
						
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
						
					}
				});
			}
			break;

		}
	}

}
