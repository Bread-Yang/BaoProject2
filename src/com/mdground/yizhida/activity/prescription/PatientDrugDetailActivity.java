package com.mdground.yizhida.activity.prescription;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.util.DecimalDigitsInputFilter;
import com.mdground.yizhida.util.StringUtil;
import com.vanward.ehheater.view.wheelview.OnWheelScrollListener;
import com.vanward.ehheater.view.wheelview.WheelView;
import com.vanward.ehheater.view.wheelview.adapters.AbstractWheelTextAdapter;
import com.vanward.ehheater.view.wheelview.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientDrugDetailActivity extends Activity implements OnClickListener {

	private ImageView iv_back;

	private TextView tv_title, tv_top_right, tv_amount_unit, tv_direction, tv_frequency, tv_group;

	private EditText et_amount, et_single_dosage, et_days, et_comment;

	private Dialog dialog_wheelView;

	private ImageView iv_close;
	private WheelView wheelView1;

	private DrugUse mDrugUse;

	private Drug mDrug;

	private String[] mAmountUnitArray;

	private String[] mDirectionArray = new String[] { "滴(Drip)", "口服(PO)", "皮试(AST)", "皮内注射(ID)", "皮下注射(H)", "肌肉注射(IM)",
			"静脉注射(IV)", "静脉滴注(VD)" };

	private String[] mFrequencyArray = new String[] { "一天一次(QD)", "一天二次(BID)", "一天三次(TID)", "一天四次(QID)", "每小时一次(QH)",
			"每二小时一次(Q2H)", "每四小时一次(Q4H)", "每六小时一次(Q6H)", "每八小时一次(Q8H)", "隔天一次(QOD)", "隔三日一次(Q3D)", "每晚一次(QN)" };

	private String[] mGroupArray = new String[] { "未分组", "分组1", "分组2", "分组3", "分组4", "分组5", "分组6", "分组7", "分组8",
			"分组9" };

	private int mWheelview1_set_flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_drug_detail);

		findViewById();
		setListener();
		init();
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		tv_amount_unit = (TextView) findViewById(R.id.tv_amount_unit);
		tv_direction = (TextView) findViewById(R.id.tv_direction);
		tv_frequency = (TextView) findViewById(R.id.tv_frequency);
		tv_group = (TextView) findViewById(R.id.tv_group);

		et_amount = (EditText) findViewById(R.id.et_amount);
		et_single_dosage = (EditText) findViewById(R.id.et_single_dosage);
		et_days = (EditText) findViewById(R.id.et_days);
		et_comment = (EditText) findViewById(R.id.et_comment);

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
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_close.setOnClickListener(this);
		tv_top_right.setOnClickListener(this);
		tv_amount_unit.setOnClickListener(this);
		tv_direction.setOnClickListener(this);
		tv_frequency.setOnClickListener(this);
		tv_group.setOnClickListener(this);

		wheelView1.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String text = ((AbstractWheelTextAdapter) wheel.getViewAdapter()).getItemText(wheel.getCurrentItem())
						.toString();

				switch (mWheelview1_set_flag) {
				case R.id.tv_amount_unit:
					mDrugUse.setSaleUnit(text);
					tv_amount_unit.setText(text);
					break;
				case R.id.tv_direction:
					text = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
					mDrugUse.setTakeType(text);
					tv_direction.setText(text);
					break;
				case R.id.tv_group:
					if (wheel.getCurrentItem() == 0) {
						text = getResources().getString(R.string.not_yet_group);
					} else {
						mDrugUse.setGroupNo(Integer.valueOf(trimUnit(text)));
					}
					tv_group.setText(text);
					break;
				case R.id.tv_frequency:
					text = text.substring(text.indexOf("(") + 1, text.indexOf(")"));
					mDrugUse.setFrequency(text);
					tv_frequency.setText(text);
					break;
				}
			}
		});

		et_single_dosage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String text = et_single_dosage.getText().toString();
				if (hasFocus) {
					et_single_dosage.setText(trimUnit(text));
				} else {
					et_single_dosage.setText(text + mDrugUse.getTakeUnit());
				}
			}
		});
	}

	private void init() {
		mDrugUse = getIntent().getParcelableExtra(MemberConstant.PRESCRIPTION_DRUG_USE);

		tv_title.setText(mDrugUse.getDrugName());
		tv_top_right.setText(R.string.save);

		et_amount.setText(String.valueOf(mDrugUse.getSaleQuantity())); // 总量
		tv_amount_unit.setText(mDrugUse.getSaleUnit()); // 总量单位

		// 只能输入三位小数
		et_single_dosage.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(3) });
		et_single_dosage.setText(mDrugUse.getTake() + mDrugUse.getTakeUnit()); // 单次用量
		tv_direction.setText(mDrugUse.getTakeType()); // 用法
		tv_frequency.setText(mDrugUse.getFrequency()); // 频度
		et_days.setText(String.valueOf(mDrugUse.getDays()));
		et_comment.setText(mDrugUse.getRemark());

		if (mDrugUse.getGroupNo() == 0) {
			tv_group.setText(R.string.not_yet_group);
		} else {
			tv_group.setText(getResources().getString(R.string.group) + mDrugUse.getGroupNo());
		}

		mDrug = DrugDao.getInstance(getApplicationContext()).getDrugByDrugID(mDrugUse.getDrugID());

		if (mDrug != null) {
			mAmountUnitArray = new String[] { mDrug.getUnitGeneric(), mDrug.getUnitSmall() };
		}
	}

	private String trimUnit(String beTrimedString) {
		String regEx = "[^0-9.]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(beTrimedString);
		return m.replaceAll("").trim();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_top_right:
			String amountString = et_amount.getText().toString();
			int quantity = 0;
			if (!StringUtil.isEmpty(amountString)) {
				quantity = Integer.valueOf(amountString);
			}
			mDrugUse.setSaleQuantity(quantity); // 总量

			String singleDosageString = trimUnit(et_single_dosage.getText().toString());
			float singleDosage = 0f;
			if (!StringUtil.isEmpty(singleDosageString)) {
				singleDosage = Float.valueOf(singleDosageString);
			}
			mDrugUse.setTake(singleDosage); // 单次用量

			String daysString = et_days.getText().toString();
			int days = 0;
			if (!StringUtil.isEmpty(daysString)) {
				days = Integer.valueOf(daysString);
			}
			mDrugUse.setDays(days); // 天数

			mDrugUse.setRemark(et_comment.getText().toString());

			Intent intent = new Intent();
			intent.putExtra(MemberConstant.APPOINTMENT_DRUG_USE_EDIT, mDrugUse);
			setResult(RESULT_OK, intent);

			finish();
			break;
		case R.id.iv_close:
			dialog_wheelView.dismiss();
			break;
		case R.id.tv_amount_unit:
			if (mDrug != null) {
				if (mDrug.getUnitGeneric().equals(mDrug.getUnitSmall())) {
					return;
				} else {
					wheelView1.setViewAdapter(new ArrayWheelAdapter(this, mAmountUnitArray, wheelView1));

					if (mDrugUse.getSaleUnit().equals(mDrug.getUnitGeneric())) {
						wheelView1.setCurrentItem(0);
					} else {
						wheelView1.setCurrentItem(1);
					}
				}

				dialog_wheelView.show();

				mWheelview1_set_flag = R.id.tv_amount_unit;
			}
			break;
		case R.id.tv_direction:
			wheelView1.setViewAdapter(new ArrayWheelAdapter(this, mDirectionArray, wheelView1));

			String takeType = mDrugUse.getTakeType();

			if ("Drip".equals(takeType)) {
				wheelView1.setCurrentItem(0);
			} else if ("PO".equals(takeType)) {
				wheelView1.setCurrentItem(1);
			} else if ("AST".equals(takeType)) {
				wheelView1.setCurrentItem(2);
			} else if ("ID".equals(takeType)) {
				wheelView1.setCurrentItem(3);
			} else if ("H".equals(takeType)) {
				wheelView1.setCurrentItem(4);
			} else if ("IM".equals(takeType)) {
				wheelView1.setCurrentItem(4);
			} else if ("IV".equals(takeType)) {
				wheelView1.setCurrentItem(5);
			} else if ("VD".equals(takeType)) {
				wheelView1.setCurrentItem(6);
			}

			dialog_wheelView.show();

			mWheelview1_set_flag = R.id.tv_direction;
			break;
		case R.id.tv_group:
			wheelView1.setViewAdapter(new ArrayWheelAdapter(this, mGroupArray, wheelView1));

			dialog_wheelView.show();

			mWheelview1_set_flag = R.id.tv_group;
			break;
		case R.id.tv_frequency:
			wheelView1.setViewAdapter(new ArrayWheelAdapter(this, mFrequencyArray, wheelView1));

			String frequency = mDrugUse.getFrequency();

			if ("QD".equals(frequency)) {
				wheelView1.setCurrentItem(0);
			} else if ("BID".equals(frequency)) {
				wheelView1.setCurrentItem(1);
			} else if ("TID".equals(frequency)) {
				wheelView1.setCurrentItem(2);
			} else if ("QID".equals(frequency)) {
				wheelView1.setCurrentItem(3);
			} else if ("QH".equals(frequency)) {
				wheelView1.setCurrentItem(4);
			} else if ("Q2H".equals(frequency)) {
				wheelView1.setCurrentItem(5);
			} else if ("Q4H".equals(frequency)) {
				wheelView1.setCurrentItem(6);
			} else if ("Q6H".equals(frequency)) {
				wheelView1.setCurrentItem(7);
			} else if ("Q8H".equals(frequency)) {
				wheelView1.setCurrentItem(8);
			} else if ("QOD".equals(frequency)) {
				wheelView1.setCurrentItem(9);
			} else if ("Q3D".equals(frequency)) {
				wheelView1.setCurrentItem(10);
			} else if ("QN".equals(frequency)) {
				wheelView1.setCurrentItem(11);
			}

			dialog_wheelView.show();

			mWheelview1_set_flag = R.id.tv_frequency;
			break;
		}
	}

	public TreeMap<String, String> parseTreeMapByResID(int stringArrayResourceId) {
		String[] stringArray = getApplication().getResources().getStringArray(stringArrayResourceId);
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		for (String entry : stringArray) {
			String[] splitResult = entry.split("\\|", 2);
			treeMap.put(splitResult[0], splitResult[1]);
		}
		return treeMap;
	}
}