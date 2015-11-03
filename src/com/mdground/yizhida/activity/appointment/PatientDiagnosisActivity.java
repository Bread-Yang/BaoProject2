package com.mdground.yizhida.activity.appointment;

import java.util.ArrayList;

import org.apache.http.Header;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveDiagnosis;
import com.mdground.yizhida.api.utils.PxUtil;
import com.mdground.yizhida.bean.Diagnosis;
import com.mdground.yizhida.constant.MemberConstant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatientDiagnosisActivity extends Activity implements OnClickListener {

	private LinearLayout llt_diagnosis;
	private RelativeLayout rlt_add_diagnosis;
	private ImageView iv_back;
	private TextView tv_title, tv_top_right;

	private ArrayList<Diagnosis> mDiagnosisList;
	private Diagnosis mDiagnosisTemplate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_diagnosis);

		findViewById();
		init();
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);

		llt_diagnosis = (LinearLayout) findViewById(R.id.llt_diagnosis);
		rlt_add_diagnosis = (RelativeLayout) findViewById(R.id.rlt_add_diagnosis);
	}

	private void init() {
		tv_title.setText(R.string.diagnosis);
		tv_top_right.setText(R.string.save);
		tv_top_right.setOnClickListener(this);

		mDiagnosisList = getIntent().getParcelableArrayListExtra(MemberConstant.APPOINTMENT_DIAGNOSIS_LIST);
		mDiagnosisTemplate = getIntent().getParcelableExtra(MemberConstant.APPOINTMENT_DIAGNOSIS_TEMPLATE);

		if (mDiagnosisList != null) {
			for (Diagnosis item : mDiagnosisList) {
				addDiagnosis(item.getDiagnosisName());
			}
		}
	}

	private void addDiagnosis(String diagnosis) {
		final RelativeLayout rlt_diagnosis = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_diagnosis,
				null);

		llt_diagnosis.addView(rlt_diagnosis);

		LinearLayout.LayoutParams params = (LayoutParams) rlt_diagnosis.getLayoutParams();
		params.height = PxUtil.dip2px(getApplicationContext(), 55);
		rlt_diagnosis.setLayoutParams(params);

		EditText et_diagnosis = (EditText) rlt_diagnosis.findViewById(R.id.et_diagnosis);
		et_diagnosis.setText(diagnosis);

		ImageView iv_delete = (ImageView) rlt_diagnosis.findViewById(R.id.iv_delete);

		iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				llt_diagnosis.removeView(rlt_diagnosis);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:
			if (mDiagnosisList != null) {
				mDiagnosisList.clear();
			} else {
				mDiagnosisList = new ArrayList<Diagnosis>();
			}
			
			findAllEditText(llt_diagnosis);
			
			new SaveDiagnosis(getApplicationContext()).saveDiagnosis(mDiagnosisList, new RequestCallBack() {
				
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
			break;

		case R.id.rlt_add_diagnosis:
			addDiagnosis("");
			break;

		}
	}

	private void findAllEditText(View v) {
		try {
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					// you can recursively call this method
					findAllEditText(child);
				}
			} else if (v instanceof EditText) {
				String diagnosisName = ((EditText)v).getText().toString();
				
				if (!TextUtils.isEmpty(diagnosisName)) {
					Diagnosis newDiagnosis = new Diagnosis();
					newDiagnosis.setClinicID(mDiagnosisTemplate.getClinicID());
					newDiagnosis.setDoctorID(mDiagnosisTemplate.getDoctorID());
					newDiagnosis.setPatientID(mDiagnosisTemplate.getPatientID());
					newDiagnosis.setVisitID(mDiagnosisTemplate.getVisitID());
					newDiagnosis.setDiagnosisName(diagnosisName);
					
					mDiagnosisList.add(newDiagnosis);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
