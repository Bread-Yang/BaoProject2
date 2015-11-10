package com.mdground.yizhida.activity.appointment;

import org.apache.http.Header;

import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.SaveVitalSign;
import com.mdground.yizhida.bean.VitalSigns;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.Tools;
import com.vanward.ehheater.view.wheelview.OnWheelScrollListener;
import com.vanward.ehheater.view.wheelview.WheelView;
import com.vanward.ehheater.view.wheelview.adapters.AbstractWheelTextAdapter;
import com.vanward.ehheater.view.wheelview.adapters.ArrayWheelAdapter;
import com.vanward.ehheater.view.wheelview.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class PatientVitalSignsActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_title, tv_top_right;
	private TextView tv_temperature, tv_height, tv_weight, tv_heartbeat, tv_breath, tv_blood_pressure,
			tv_blood_glucose;
	
	private EditText et_bmi;

	private Dialog dialog_wheelView;

	private ImageView iv_close;
	private WheelView wheelView1, wheelView2;

	private VitalSigns vitalSigns;

	private String temperatureArray[];

	private float setTemperature, setBMI;

	private int wheelview1_set_flag;

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
		tv_heartbeat = (TextView) findViewById(R.id.tv_heartbeat);
		tv_breath = (TextView) findViewById(R.id.tv_breath);
		tv_blood_pressure = (TextView) findViewById(R.id.tv_blood_pressure);
		tv_blood_glucose = (TextView) findViewById(R.id.tv_blood_glucose);
		
		et_bmi = (EditText) findViewById(R.id.et_bmi);

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
		et_bmi.setOnClickListener(this);
		tv_heartbeat.setOnClickListener(this);
		tv_breath.setOnClickListener(this);
		tv_blood_pressure.setOnClickListener(this);
		tv_blood_glucose.setOnClickListener(this);

		iv_close.setOnClickListener(this);

		wheelView1.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String text = ((AbstractWheelTextAdapter) wheel.getViewAdapter())
						.getItemText(wheel.getCurrentItem()).toString();

				switch (wheelview1_set_flag) {
				case R.id.tv_temperature:
					vitalSigns.setBodyTemperature(Float.valueOf(text));
					tv_temperature.setText(Tools.getFormat(getApplicationContext(), R.string.temperature_unit, text));
					break;
				case R.id.tv_height:
					vitalSigns.setHeight(Integer.valueOf(text));
					tv_height.setText(Tools.getFormat(getApplicationContext(), R.string.height_unit, text));
					break;
				case R.id.tv_weight:
					vitalSigns.setWeight(Integer.valueOf(text));
					tv_weight.setText(Tools.getFormat(getApplicationContext(), R.string.weight_unit, text));
					break;
				case R.id.tv_heartbeat:
					vitalSigns.setHeartbeat(Integer.valueOf(text));
					tv_heartbeat.setText(Tools.getFormat(getApplicationContext(), R.string.heartbeat_unit, text));
					break;
				case R.id.tv_breath:
					vitalSigns.setBreathing(Integer.valueOf(text));
					tv_breath.setText(Tools.getFormat(getApplicationContext(), R.string.breath_unit, text));
					break;
				case R.id.tv_blood_pressure:
					vitalSigns.setSystolicPressure(Integer.valueOf(text));
					tv_blood_pressure.setText(Tools.getFormat(getApplicationContext(), R.string.blood_pressure_unit,
							text, String.valueOf(vitalSigns.getDiastolicBloodPressure())));
					break;
				case R.id.tv_blood_glucose:
					vitalSigns.setBloodGlucose(Integer.valueOf(text));
					tv_blood_glucose
							.setText(Tools.getFormat(getApplicationContext(), R.string.blood_glucose_unit, text));
					break;

				}
			}
		});

		wheelView2.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String text = ((AbstractWheelTextAdapter) wheel.getViewAdapter())
						.getItemText(wheel.getCurrentItem()).toString();

				vitalSigns.setDiastolicBloodPressure(Integer.valueOf(text));
				tv_blood_pressure.setText(Tools.getFormat(getApplicationContext(), R.string.blood_pressure_unit,
						String.valueOf(vitalSigns.getSystolicPressure()), text));
			}
		});
	}

	private void init() {
		tv_title.setText(R.string.signs);
		tv_top_right.setText(R.string.save);

		vitalSigns = getIntent().getParcelableExtra(MemberConstant.APPOINTMENT_VITAL_SIGNS);

		tv_temperature.setText(
				Tools.getFormat(getApplicationContext(), R.string.temperature_unit, vitalSigns.getBodyTemperature()));
		tv_height.setText(Tools.getFormat(getApplicationContext(), R.string.height_unit, vitalSigns.getHeight()));
		tv_weight.setText(Tools.getFormat(getApplicationContext(), R.string.weight_unit, vitalSigns.getWeight()));
		tv_heartbeat
				.setText(Tools.getFormat(getApplicationContext(), R.string.heartbeat_unit, vitalSigns.getHeartbeat()));
		tv_breath.setText(Tools.getFormat(getApplicationContext(), R.string.breath_unit, vitalSigns.getBreathing()));
		tv_blood_pressure.setText(Tools.getFormat(getApplicationContext(), R.string.blood_pressure_unit,
				vitalSigns.getSystolicPressure(), vitalSigns.getDiastolicBloodPressure()));
		tv_blood_glucose.setText(
				Tools.getFormat(getApplicationContext(), R.string.blood_glucose_unit, vitalSigns.getBloodGlucose()));

		tv_temperature.setOnClickListener(this);
		
		et_bmi.setText(String.valueOf(vitalSigns.getBMI()));
		int position = et_bmi.length();
		Editable etext = et_bmi.getText();
		Selection.setSelection(etext, position);

		// 体温
		temperatureArray = new String[21];
		float temp = 35.0f;
		for (int i = 0; i < 21; i++) {
			temperatureArray[i] = String.valueOf(temp + 0.1 * i);
		}
		
		wheelView2.setViewAdapter(new NumericWheelAdapter(this, 60, 100, wheelView2));
		wheelView2.setLabel("mmhg");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:
			
			vitalSigns.setBMI(Float.valueOf(et_bmi.getText().toString()));

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
			wheelView1.setViewAdapter(new ArrayWheelAdapter(this, temperatureArray, wheelView1));
			wheelView1.setLabel("℃");
			wheelView1.setCurrentItem((int)((vitalSigns.getBodyTemperature() - 35.0f) * 10));
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_temperature;

			dialog_wheelView.show();
			break;
		case R.id.tv_height:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 40, 200, wheelView1));
			wheelView1.setLabel("cm");
			wheelView1.setCurrentItem(vitalSigns.getHeight() - 40);
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_height;

			dialog_wheelView.show();
			break;
		case R.id.tv_weight:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 2, 150, wheelView1));
			wheelView1.setLabel("kg");
			wheelView1.setCurrentItem(vitalSigns.getWeight() - 2);
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_weight;

			dialog_wheelView.show();
			break;
		case R.id.tv_bmi:
			break;
		case R.id.tv_heartbeat:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 40, 220, wheelView1));
			wheelView1.setLabel("bpm");
			wheelView1.setCurrentItem(vitalSigns.getHeartbeat() - 40);
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_heartbeat;

			dialog_wheelView.show();
			break;
		case R.id.tv_breath:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 8, 30, wheelView1));
			wheelView1.setLabel("/min");
			wheelView1.setCurrentItem(vitalSigns.getBreathing() - 8);
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_breath;

			dialog_wheelView.show();
			break;
		case R.id.tv_blood_pressure:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 100, 250, wheelView1));
			wheelView1.setLabel("/");
			wheelView1.setCurrentItem(vitalSigns.getSystolicPressure() - 100);
			wheelView2.setCurrentItem(vitalSigns.getDiastolicBloodPressure() - 60);
			wheelView2.setVisibility(View.VISIBLE);

			wheelview1_set_flag = R.id.tv_blood_pressure;

			dialog_wheelView.show();
			break;
		case R.id.tv_blood_glucose:
			wheelView1.setViewAdapter(new NumericWheelAdapter(this, 50, 250, wheelView1));
			wheelView1.setLabel("mg/dl");
			wheelView1.setCurrentItem(vitalSigns.getBloodGlucose() - 50);
			wheelView2.setVisibility(View.GONE);
			wheelview1_set_flag = R.id.tv_blood_glucose;

			dialog_wheelView.show();
			break;

		}
	}
}
