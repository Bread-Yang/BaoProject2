package com.mdground.yizhida.activity.patientinfo;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.bean.Patient;
import com.mdground.yizhida.util.CommonUtils;
import com.mdground.yizhida.util.RegexUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PatientCommonActivity extends BaseActivity {

	protected Button btn_left_arrow, btn_right_arrow;

	protected TextView TvAppionmentNo;
	protected TextView TvPatientName;
	protected TextView TvPatientAge;
	protected TextView TvPatientSex;
	protected TextView TvPhone;

	protected View VtabOne;
	protected View VtabTwo;
	protected View VtabThree;
	protected View VtabFour;

	protected TextView TvTabOne;
	protected TextView TvTabTwo;
	protected TextView TvTabThree;
	protected TextView TvTabFour;

	protected ViewPager viewpager;

	protected Resources resources;

	@Override
	public void findView() {
		btn_left_arrow = (Button) this.findViewById(R.id.btn_left_arrow);
		btn_right_arrow = (Button) this.findViewById(R.id.btn_right_arrow);

		TvAppionmentNo = (TextView) this.findViewById(R.id.patient_no);
		TvPatientName = (TextView) this.findViewById(R.id.patient_name);
		TvPatientAge = (TextView) this.findViewById(R.id.patient_age);
		TvPatientSex = (TextView) this.findViewById(R.id.patient_sex);
		TvPhone = (TextView) this.findViewById(R.id.phone_no);

		VtabOne = this.findViewById(R.id.view_tab_one);
		VtabTwo = this.findViewById(R.id.view_tab_two);
		VtabThree = this.findViewById(R.id.view_tab_three);
		VtabFour = this.findViewById(R.id.view_tab_four);

		TvTabOne = (TextView) this.findViewById(R.id.tv_tab_one);
		TvTabTwo = (TextView) this.findViewById(R.id.tv_tab_two);
		TvTabThree = (TextView) this.findViewById(R.id.tv_tab_three);
		TvTabFour = (TextView) this.findViewById(R.id.tv_tab_four);

		viewpager = (ViewPager) this.findViewById(R.id.viewpager);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initMemberData() {
		resources = this.getResources();

	}

	@Override
	public void setListener() {
		TvPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TvPhone.getText() != null && RegexUtil.isPhoneNumber(TvPhone.getText().toString())) {
					final String phone = TvPhone.getText().toString();

					Dialog dialog = new AlertDialog.Builder(PatientCommonActivity.this).setMessage(phone)
							.setNegativeButton("呼叫", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}

					}).setPositiveButton("取消", null).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				} else {
					Toast.makeText(PatientCommonActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
				}
			}
		});
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				handleTab(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				handleTab(arg0);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	protected void handleTab(int position) {
		switch (position) {
		case 0:
			CommonUtils.showPageTab(resources, TvTabOne, VtabOne, TvTabTwo, VtabTwo, TvTabThree, VtabThree, TvTabFour,
					VtabFour);
			break;
		case 1:
			CommonUtils.showPageTab(resources, TvTabTwo, VtabTwo, TvTabOne, VtabOne, TvTabThree, VtabThree, TvTabFour,
					VtabFour);
			break;
		case 2:
			CommonUtils.showPageTab(resources, TvTabThree, VtabThree, TvTabOne, VtabOne, TvTabTwo, VtabTwo, TvTabFour,
					VtabFour);
			break;
		case 3:
			CommonUtils.showPageTab(resources, TvTabFour, VtabFour, TvTabOne, VtabOne, TvTabTwo, VtabTwo, TvTabThree,
					VtabThree);
			break;
		}
	}

	/**
	 * 将数据更新到界面
	 */
	protected void updateView(Patient patient, long appiontmentNo) {
		if (patient == null) {
			return;
		}
		this.TvPatientAge.setText(patient.getAgeStr());
		this.TvPatientName.setText(patient.getPatientName());
		if (appiontmentNo != 0) {
			this.TvAppionmentNo.setText(String.valueOf(appiontmentNo));
		} else {
			this.TvAppionmentNo.setVisibility(View.GONE);
		}
		this.TvPatientSex.setText(patient.getGenderStr());
		if (patient.getPhone() == null || patient.getPhone().equals("")) {
			this.TvPhone.setVisibility(View.INVISIBLE);
		} else {
			this.TvPhone.setText(patient.getPhone());
		}
	}
}
