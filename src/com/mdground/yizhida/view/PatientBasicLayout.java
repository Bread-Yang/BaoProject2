package com.mdground.yizhida.view;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Patient;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 病人基本信息
 * 
 * @author Administrator
 *
 */
public class PatientBasicLayout extends RelativeLayout {

	private TextView TvBirthday;// 生日
	private TextView tv_phone_num_value; // 手机
	private TextView TvIdentity;// 身份证

	private TextView TvEmergencyContact; // 紧急联系人
	private TextView TvEmergencyContactPhone;
	private TextView TvEmergencyContactRelation;
	private TextView TvMail;

	private TextView tv_internal_number_value; // 内部编号
	private TextView TvSecurity;// 社保
	private TextView TvMedicalSocialNumber; // 医保
	private TextView tv_marital_status_value; // 婚姻状况
	private TextView tv_nation_value; // 民族
	private TextView TvAddress; // 住址

	private TextView tv_company_value; // 就职公司
	private TextView tv_position_value; // 职位

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
	private View view;

	public PatientBasicLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		view = LayoutInflater.from(context).inflate(R.layout.layout_patient_basic_info, null);
		
		this.addView(view);
		findView();
	}

	public PatientBasicLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.layout_patient_basic_info, null);
		this.addView(view);
		findView();
	}

	public PatientBasicLayout(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(R.layout.layout_patient_basic_info, null);
		
		ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);

		adapterToBigScreen();
		
		this.addView(view);
		findView();
	}
	
	private void adapterToBigScreen() {
		ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollview);

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		
		android.view.ViewGroup.LayoutParams scrollViewParams = scrollView.getLayoutParams();

		scrollViewParams.height = (int) (height * 0.6);
		scrollView.setLayoutParams(scrollViewParams);
		
//		android.view.ViewGroup.LayoutParams rootParams = view.getLayoutParams();
//		rootParams.height = height;
//		view.setLayoutParams(rootParams);
	}

	private void findView() {

		TvBirthday = (TextView) view.findViewById(R.id.birthday_value);
		tv_phone_num_value = (TextView) view.findViewById(R.id.tv_phone_num_value);
		TvIdentity = (TextView) view.findViewById(R.id.identity_card_value);

		TvEmergencyContact = (TextView) view.findViewById(R.id.contact_value);
		TvEmergencyContactPhone = (TextView) view.findViewById(R.id.contact_phone_value);
		TvEmergencyContactRelation = (TextView) view.findViewById(R.id.contact_relation_value);
		TvMail = (TextView) view.findViewById(R.id.mail_value);

		tv_internal_number_value = (TextView) view.findViewById(R.id.tv_internal_number_value);
		TvSecurity = (TextView) view.findViewById(R.id.security_insurance_value);
		TvMedicalSocialNumber = (TextView) view.findViewById(R.id.medical_insurance_value);
		tv_marital_status_value = (TextView) view.findViewById(R.id.tv_marital_status_value);
		tv_nation_value = (TextView) view.findViewById(R.id.tv_nation_value);
		TvAddress = (TextView) view.findViewById(R.id.address_value);

		tv_company_value = (TextView) view.findViewById(R.id.tv_company_value);
		tv_position_value = (TextView) view.findViewById(R.id.tv_position_value);

	}

	/**
	 * 这个方法把数据对象传过来，并且初始化控件值。
	 * 
	 * @param object
	 */
	public void initData(Patient patient) {
		if (patient == null) {
			return;
		}
		TvBirthday.setText(sdf.format(patient.getDOB()));
		tv_phone_num_value.setText(patient.getPhone());
		TvIdentity.setText(patient.getIDCard());

		TvEmergencyContact.setText(patient.getEmergencyName());
		TvEmergencyContactPhone.setText(patient.getEmergencyPhone());
		TvEmergencyContactRelation.setText(patient.getEmergencyRelationship());
		TvMail.setText(patient.getEmail());

		tv_internal_number_value.setText(patient.getPatientCode());
		TvSecurity.setText(patient.getSSN());
		TvMedicalSocialNumber.setText(patient.getMSN());
		tv_marital_status_value.setText(patient.getMarriedStr());
		tv_nation_value.setText(patient.getNation());
		TvAddress.setText(patient.getAddress());

		tv_company_value.setText(patient.getCompanyName());
		tv_position_value.setText(patient.getTitle());
	}

}
