package com.mdground.yizhida.activity.appointment;

import org.apache.http.Header;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveVitalSign;
import com.mdground.yizhida.bean.VitalSigns;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.Tools;
import com.mdground.yizhida.view.wheel.WheelView;
import com.mdground.yizhida.view.wheel.adapter.ArrayWheelAdapter;
import com.mdground.yizhida.view.wheel.adapter.NumericWheelAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PatientVitalSignsActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_title, tv_top_right;
	private TextView tv_temperature, tv_height, tv_weight, tv_bmi, tv_heartbeat, tv_breath, tv_blood_pressure,
			tv_blood_glucose;

	private Dialog dialog_wheelView;

	private ImageView iv_close;
	private WheelView wheelView1, wheelView2;

	private VitalSigns vitalSigns;

	private String temperatureArray[], heightArray[], weightArray[], hearbeatArray[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_vital_signs);

		findViewById();
		setListener();
		init();

		new Handler().postDelayed(new Runnable() {
			public void run() {
				((ScrollView) findViewById(R.id.scrollview)).scrollTo(0, 40);
			}
		}, 5000);
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);

		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_bmi = (TextView) findViewById(R.id.tv_bmi);
		tv_heartbeat = (TextView) findViewById(R.id.tv_heartbeat);
		tv_breath = (TextView) findViewById(R.id.tv_breath);
		tv_blood_pressure = (TextView) findViewById(R.id.tv_blood_pressure);
		tv_blood_glucose = (TextView) findViewById(R.id.tv_blood_glucose);

		// 初始化dialog及dialog里面的控件
		dialog_wheelView = new Dialog(this, R.style.patient_detail_dialog);
		dialog_wheelView.setContentView(R.layout.dialog_wheel_view);

		// 设置dialog弹入弹出的动画
		Window window = dialog_wheelView.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window.setAttributes(wlp);

		iv_close = (ImageView) dialog_wheelView.findViewById(R.id.iv_close);

		wheelView1 = (WheelView) dialog_wheelView.findViewById(R.id.wheelview1);
		wheelView2 = (WheelView) dialog_wheelView.findViewById(R.id.wheelview2);
	}

	private void setListener() {
		tv_top_right.setOnClickListener(this);
		tv_height.setOnClickListener(this);
		tv_weight.setOnClickListener(this);
		tv_bmi.setOnClickListener(this);
		tv_heartbeat.setOnClickListener(this);
		tv_breath.setOnClickListener(this);
		tv_blood_pressure.setOnClickListener(this);
		tv_blood_glucose.setOnClickListener(this);

		iv_close.setOnClickListener(this);
	}

	private void init() {
		tv_title.setText(R.string.signs);
		tv_top_right.setText(R.string.save);

		vitalSigns = getIntent().getParcelableExtra(MemberConstant.APPOINTMENT_VITAL_SIGNS);
		if (vitalSigns == null) {
			vitalSigns = new VitalSigns();

			vitalSigns.setBodyTemperature(37.0f);
			vitalSigns.setHeight(100);
			vitalSigns.setWeight(50);
			vitalSigns.setBMI(50.0f);

			vitalSigns.setHeartbeat(70);
			vitalSigns.setBreathing(15);
			vitalSigns.setSystolicPressure(140);
			vitalSigns.setDiastolicBloodPressure(70);
			vitalSigns.setBloodGlucose(110);
		}

		tv_temperature.setText(
				Tools.getFormat(getApplicationContext(), R.string.temperature_unit, vitalSigns.getBodyTemperature()));
		tv_height.setText(Tools.getFormat(getApplicationContext(), R.string.height_unit, vitalSigns.getHeight()));
		tv_weight.setText(Tools.getFormat(getApplicationContext(), R.string.weight_unit, vitalSigns.getWeight()));
		tv_bmi.setText(String.valueOf(vitalSigns.getBMI()));
		tv_heartbeat
				.setText(Tools.getFormat(getApplicationContext(), R.string.heartbeat_unit, vitalSigns.getHeartbeat()));
		tv_breath.setText(Tools.getFormat(getApplicationContext(), R.string.breath_unit, vitalSigns.getBreathing()));
		tv_blood_pressure.setText(Tools.getFormat(getApplicationContext(), R.string.blood_pressure_unit,
				vitalSigns.getSystolicPressure(), vitalSigns.getDiastolicBloodPressure()));
		tv_blood_glucose.setText(
				Tools.getFormat(getApplicationContext(), R.string.blood_glucose_unit, vitalSigns.getBloodGlucose()));

		tv_temperature.setOnClickListener(this);

		// 体温
		temperatureArray = new String[21];
		float temp = 35.0f;
		for (int i = 0; i < 21; i++) {
			temperatureArray[i] = String.valueOf(temp + 0.1 * i);
		}

		// 身高
		heightArray = new String[201];
		float height = 40f;
		for (int i = 0; i < 201; i++) {
			heightArray[i] = String.valueOf(height + 1 * i);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:

			new SaveVitalSign(getApplicationContext()).saveVitalSign(vitalSigns, new RequestCallBack() {

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

		case R.id.iv_close:
			dialog_wheelView.dismiss();
			break;

		case R.id.tv_temperature:
			wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(this, temperatureArray));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;
		case R.id.tv_height:
			wheelView1.setViewAdapter(new ArrayWheelAdapter<String>(this, heightArray));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;
		case R.id.tv_weight:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 2, 150));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;
		case R.id.tv_bmi:
			break;
		case R.id.tv_heartbeat:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 40, 220));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;
		case R.id.tv_breath:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 8, 30));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;
		case R.id.tv_blood_pressure:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 100, 250));
			wheelView2.setViewAdapter(new NumericWheelAdapter(this, 60, 100));
			wheelView2.setVisibility(View.VISIBLE);

			dialog_wheelView.show();
			break;
		case R.id.tv_blood_glucose:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 50, 250));
			wheelView2.setVisibility(View.GONE);

			dialog_wheelView.show();
			break;

		}

	}

}
