package com.mdground.yizhida.activity.prescription;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.searchdrug.SearchDrugActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetDurgInfoByID;
import com.mdground.yizhida.api.server.clinic.SaveDrugUseForOV;
import com.mdground.yizhida.api.utils.PxUtil;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.bean.Fee;
import com.mdground.yizhida.bean.OfficeVisitFee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.db.dao.FeeDao;
import com.mdground.yizhida.util.DecimalDigitsInputFilter;
import com.mdground.yizhida.util.FeeUtil;
import com.mdground.yizhida.util.StringUtil;
import com.mdground.yizhida.util.ViewUtil;
import com.vanward.ehheater.view.wheelview.OnWheelScrollListener;
import com.vanward.ehheater.view.wheelview.WheelView;
import com.vanward.ehheater.view.wheelview.adapters.AbstractWheelTextAdapter;
import com.vanward.ehheater.view.wheelview.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PatientPrescriptionActivity extends Activity implements OnClickListener {

	public static final int REQUEST_CODE_ADD_DRUG = 0;
	public static final int REQUEST_CODE_EDIT_DRUG = 1;

	private ImageView iv_back;
	private TextView tv_title, tv_top_right, tv_amount;

	private RelativeLayout rlt_fee, rlt_drug;

	private LinearLayout llt_fee_display;

	private ListView lv_drug;

	private Dialog dialog_menu;

	private LinearLayout llt_fee_button;

	private Button btn_cancel;

	private Dialog dialog_wheelView;

	private ImageView iv_close;
	private WheelView wheelView;

	private PrescriptionAdapter mAdapter;

	private DrugDao mDrugDao;

	private int currentClickPosition;

	private String[] mGroupArray = new String[] { "未分组", "分组1", "分组2", "分组3", "分组4", "分组5", "分组6", "分组7", "分组8",
			"分组9" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_prescription);

		findViewById();
		init();
		setListener();

	}

	@Override
	protected void onResume() {
		super.onResume();

		mAdapter.notifyDataSetChanged();

		FeeUtil.calculateAmountAndShowInTextView(tv_amount);
	}

	private void findViewById() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		tv_amount = (TextView) findViewById(R.id.tv_amount);

		rlt_fee = (RelativeLayout) findViewById(R.id.rlt_fee);
		rlt_drug = (RelativeLayout) findViewById(R.id.rlt_drug);
		llt_fee_display = (LinearLayout) findViewById(R.id.llt_fee_display);
		lv_drug = (ListView) findViewById(R.id.lv_drug);

		// 初始化dialog及dialog里面的控件
		dialog_menu = new Dialog(this, R.style.patient_detail_dialog);
		dialog_menu.setContentView(R.layout.dialog_prescription);

		// 设置dialog弹入弹出的动画
		Window window = dialog_menu.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window.setAttributes(wlp);

		// 初始化dialog及dialog里面的控件
		dialog_wheelView = new Dialog(this, R.style.patient_detail_dialog);
		dialog_wheelView.setContentView(R.layout.dialog_wheel_view);

		// 设置dialog弹入弹出的动画
		Window window_wheel = dialog_wheelView.getWindow();
		window_wheel.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
		window_wheel.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
		WindowManager.LayoutParams wlp_wheel = window.getAttributes();
		wlp_wheel.gravity = Gravity.BOTTOM; // 使dialog在底部显示
		window_wheel.setAttributes(wlp_wheel);

		iv_close = (ImageView) dialog_wheelView.findViewById(R.id.iv_close);
		wheelView = (WheelView) dialog_wheelView.findViewById(R.id.wheelview1);

		llt_fee_button = (LinearLayout) dialog_menu.findViewById(R.id.llt_fee_button);
		btn_cancel = (Button) dialog_menu.findViewById(R.id.btn_cancel);

	}

	private void init() {
		tv_title.setText(R.string.prescription);
		tv_top_right.setText(R.string.save);

		wheelView.setViewAdapter(new ArrayWheelAdapter(this, mGroupArray, wheelView));

		mDrugDao = DrugDao.getInstance(getApplicationContext());

		for (OfficeVisitFee item : MedicalAppliction.mOfficeVisitFeeMap.values()) {
			addFeeLayout(item);
		}

		FeeDao feeDao = FeeDao.getInstance(this);

		List<Fee> mFeeList = feeDao
				.getFeesByClinicID(((MedicalAppliction) getApplication()).getLoginEmployee().getClinicID());

		for (int i = 0; i < mFeeList.size(); i++) {
			Button button = (Button) getLayoutInflater().inflate(R.layout.button_prescription_fee, null);

			final Fee item = mFeeList.get(i);

			button.setText(item.getFeeType());

			llt_fee_button.addView(button);

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
			params.height = PxUtil.dip2px(getApplicationContext(), 55);
			if (i != mFeeList.size() - 1) {
				params.bottomMargin = PxUtil.dip2px(getApplicationContext(), -1);
			}
			button.setLayoutParams(params);

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!MedicalAppliction.mOfficeVisitFeeMap.containsKey(item.getFeeType())) {
						OfficeVisitFee officeFee = new OfficeVisitFee();

						officeFee.setClinicID(item.getClinicID());
						officeFee.setVisitID(MedicalAppliction.mDrugUseAddTemplate.getVisitID());
						officeFee.setDoctorID(item.getDoctorID());
						officeFee.setPatientID(MedicalAppliction.mDrugUseAddTemplate.getPatientID());
						officeFee.setFeeType(item.getFeeType());
						officeFee.setTotalFee(item.getTotalFee());

						MedicalAppliction.mOfficeVisitFeeMap.put(officeFee.getFeeType(), officeFee);

						addFeeLayout(officeFee);
					}

				}
			});
		}

		mAdapter = new PrescriptionAdapter();
		lv_drug.setAdapter(mAdapter);

		ViewUtil.setListViewHeightBasedOnChildren(lv_drug);

		FeeUtil.calculateAmountAndShowInTextView(tv_amount);
	}

	private void addFeeLayout(final OfficeVisitFee officeVisitFee) {

		final RelativeLayout rlt_prescription_fee = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.layout_prescription_fee, null);

		llt_fee_display.addView(rlt_prescription_fee);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlt_prescription_fee.getLayoutParams();
		params.height = PxUtil.dip2px(getApplicationContext(), 50);
		params.topMargin = PxUtil.dip2px(getApplicationContext(), 10);
		rlt_prescription_fee.setLayoutParams(params);

		TextView tv_fee_name = (TextView) rlt_prescription_fee.findViewById(R.id.tv_fee_name);
		tv_fee_name.setText(officeVisitFee.getFeeType());

		EditText et_fee = (EditText) rlt_prescription_fee.findViewById(R.id.et_fee);
		et_fee.setText(String.format("%.02f", officeVisitFee.getTotalFee() / 100f));
		int position = et_fee.length();
		Editable etext = et_fee.getText();
		Selection.setSelection(etext, position);

		// 只能输入两位小数
		et_fee.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

		FeeUtil.calculateAmountAndShowInTextView(tv_amount);

		et_fee.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					officeVisitFee.setTotalFee((int) (Float.valueOf(s.toString()) * 100));
				} else {
					officeVisitFee.setTotalFee(0);
				}

				FeeUtil.calculateAmountAndShowInTextView(tv_amount);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		ImageView iv_delete = (ImageView) rlt_prescription_fee.findViewById(R.id.iv_delete);

		iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog myDialog = new AlertDialog.Builder(PatientPrescriptionActivity.this)
						.setMessage(R.string.confirm_to_delete)
						.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						llt_fee_display.removeView(rlt_prescription_fee);

						MedicalAppliction.mOfficeVisitFeeMap.remove(officeVisitFee.getFeeType());

						FeeUtil.calculateAmountAndShowInTextView(tv_amount);
					}
				}).setNeutralButton(R.string.cancle, null).create();
				myDialog.show();

			}
		});

		dialog_menu.dismiss();

	}

	private void setListener() {
		tv_top_right.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		iv_close.setOnClickListener(this);

		lv_drug.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

				final DrugUse drugUse = (DrugUse) MedicalAppliction.mDrugUseMap.values().toArray()[position];

				new GetDurgInfoByID(getApplicationContext()).getDurgInfoByID(drugUse.getDrugID(),
						new RequestCallBack() {

					@Override
					public void onSuccess(ResponseData response) {
						if (response.getCode() == ResponseCode.Normal.getValue()) {
							Drug drugDetail = response.getContent(Drug.class);

							Intent intent = new Intent(PatientPrescriptionActivity.this,
									PatientDrugDetailActivity.class);
							intent.putExtra(MemberConstant.PRESCRIPTION_DRUG_USE, drugUse);
							intent.putExtra(MemberConstant.PRESCRIPTION_DRUG_DETAIL, drugDetail);
							startActivity(intent);
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, String responseString,
							Throwable throwable) {
					}
				});
			}
		});

		wheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String text = ((AbstractWheelTextAdapter) wheel.getViewAdapter()).getItemText(wheel.getCurrentItem())
						.toString();

				DrugUse drugUse = (DrugUse) MedicalAppliction.mDrugUseMap.values().toArray()[currentClickPosition];

				// if (wheel.getCurrentItem() == 0) {
				// text = getResources().getString(R.string.not_yet_group);
				// } else {
				// drugUse.setGroupNo(Integer.valueOf(StringUtil.trimUnit(text)));
				// }
				drugUse.setGroupNo(wheel.getCurrentItem());
				mAdapter.notifyDataSetChanged();
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
			if (MedicalAppliction.mOfficeVisitFeeMap.size() == 0 && MedicalAppliction.mDrugUseMap.size() == 0) {
				Toast.makeText(getApplicationContext(), R.string.no_fee, Toast.LENGTH_SHORT).show();
				return;
			}

			// 处方只添加费用时，费用设置为0时，不能保存成功
			if (MedicalAppliction.mDrugUseMap.size() == 0) {
				boolean isZeroFee = false;
				for (OfficeVisitFee item : MedicalAppliction.mOfficeVisitFeeMap.values()) {
					if (item.getTotalFee() == 0) {
						isZeroFee = true;
						break;
					}
				}
				if (isZeroFee) {
					Toast.makeText(getApplicationContext(), R.string.more_than_zero, Toast.LENGTH_SHORT).show();
					return;
				}
			}

			List<DrugUse> drugUseList = new ArrayList<DrugUse>();
			for (DrugUse item : MedicalAppliction.mDrugUseMap.values()) {
				drugUseList.add(item);
			}

			List<OfficeVisitFee> officeVisitFeeList = new ArrayList<OfficeVisitFee>();
			for (OfficeVisitFee item : MedicalAppliction.mOfficeVisitFeeMap.values()) {
				officeVisitFeeList.add(item);
			}

			if (FeeUtil.getCalculateAmount() == 0) {
				Toast.makeText(getApplicationContext(), R.string.more_than_zero, Toast.LENGTH_SHORT).show();
				return;
			}

			new SaveDrugUseForOV(getApplicationContext()).saveDrugUseForOV(
					MedicalAppliction.mDrugUseAddTemplate.getVisitID(), drugUseList, officeVisitFeeList,
					new RequestCallBack() {

						@Override
						public void onSuccess(ResponseData response) {
							if (response.getCode() == ResponseCode.Normal.getValue()) {
								finish();
							}
						}

						@Override
						public void onStart() {

						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString,
								Throwable throwable) {

						}
					});
			break;
		case R.id.rlt_fee:
			List<Fee> feeList = FeeDao.getInstance(getApplicationContext())
					.getFeesByClinicID(((MedicalAppliction) getApplication()).getLoginEmployee().getClinicID());
			if (feeList == null || feeList.size() == 0) {
				Toast.makeText(getApplicationContext(), R.string.please_sync_data_in_personal_center,
						Toast.LENGTH_SHORT).show();
				return;
			}
			dialog_menu.show();
			break;
		case R.id.rlt_drug:
			List<Drug> drugList = mDrugDao
					.getDrugByClinicID(((MedicalAppliction) getApplication()).getLoginEmployee().getClinicID());
			if (drugList == null || drugList.size() == 0) {
				Toast.makeText(getApplicationContext(), R.string.please_sync_data_in_personal_center,
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(this, SearchDrugActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_cancel:
			dialog_menu.dismiss();
			break;
		case R.id.iv_close:
			dialog_wheelView.dismiss();
			break;
		}
	}

	private class PrescriptionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return MedicalAppliction.mDrugUseMap.size();
		}

		@Override
		public Object getItem(int position) {
			return MedicalAppliction.mDrugUseMap.values().toArray()[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_prescription_drug, null);

				holder = new ViewHolder();
				holder.tv_drug_name = (TextView) convertView.findViewById(R.id.tv_drug_name);
				holder.tv_specification = (TextView) convertView.findViewById(R.id.tv_specification);
				holder.tv_unit_price = (TextView) convertView.findViewById(R.id.tv_unit_price);
				holder.tv_single_dosage = (TextView) convertView.findViewById(R.id.tv_single_dosage);
				holder.tv_direction = (TextView) convertView.findViewById(R.id.tv_direction);
				holder.tv_frequency = (TextView) convertView.findViewById(R.id.tv_frequency);
				holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
				holder.tv_group = (TextView) convertView.findViewById(R.id.tv_group);

				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				holder.iv_minus = (ImageView) convertView.findViewById(R.id.iv_minus);
				holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);

				holder.et_quantity = (EditText) convertView.findViewById(R.id.et_quantity);
				holder.rlt_group = (RelativeLayout) convertView.findViewById(R.id.rlt_group);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final DrugUse drugUse = (DrugUse) MedicalAppliction.mDrugUseMap.values().toArray()[position];

			holder.tv_drug_name.setText(drugUse.getDrugName());

			holder.et_quantity.setText(String.valueOf(drugUse.getSaleQuantity()));

			final Drug drug = mDrugDao.getDrugByDrugID(drugUse.getDrugID());
			if (drug != null) {
				holder.tv_specification.setText(drug.getSpecification());
			} else {
				holder.tv_specification.setText("");
			}

			if ((int) drugUse.getSalePrice() / 100f == drugUse.getSalePrice() / 100) {
				holder.tv_unit_price.setText((int) (drugUse.getSalePrice() / 100f)
						+ getResources().getString(R.string.yuan) + "/" + drugUse.getSaleUnit());
			} else {
				holder.tv_unit_price.setText(String.format("%.02f", drugUse.getSalePrice() / 100f)
						+ getResources().getString(R.string.yuan) + "/" + drugUse.getSaleUnit());
			}

			if ((int) drugUse.getTake() == drugUse.getTake()) {
				holder.tv_single_dosage.setText((int) drugUse.getTake() + drugUse.getTakeUnit());
			} else {
				holder.tv_single_dosage.setText(drugUse.getTake() + drugUse.getTakeUnit());
			}

			holder.tv_direction.setText(drugUse.getTakeType());

			holder.tv_frequency.setText(drugUse.getFrequency());

			holder.tv_comment.setText(drugUse.getRemark());

			if (drugUse.getGroupNo() == 0) {
				holder.tv_group.setText(R.string.not_yet_group);
			} else {
				holder.tv_group.setText(getResources().getString(R.string.group) + drugUse.getGroupNo());
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new GetDurgInfoByID(getApplicationContext()).getDurgInfoByID(drugUse.getDrugID(),
							new RequestCallBack() {

						@Override
						public void onSuccess(ResponseData response) {
							if (response.getCode() == ResponseCode.Normal.getValue()) {
								Drug drugDetail = response.getContent(Drug.class);

								Intent intent = new Intent(PatientPrescriptionActivity.this,
										PatientDrugDetailActivity.class);
								intent.putExtra(MemberConstant.PRESCRIPTION_DRUG_USE, drugUse);
								intent.putExtra(MemberConstant.PRESCRIPTION_DRUG_DETAIL, drugDetail);
								startActivity(intent);
							}
						}

						@Override
						public void onStart() {
						}

						@Override
						public void onFinish() {
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString,
								Throwable throwable) {
						}
					});
				}
			});

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog myDialog = new AlertDialog.Builder(PatientPrescriptionActivity.this)
							.setMessage(R.string.confirm_to_delete)
							.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							MedicalAppliction.mDrugUseMap.remove(String.valueOf(drug.getDrugID()));
							mAdapter.notifyDataSetChanged();
							ViewUtil.setListViewHeightBasedOnChildren(lv_drug);

							FeeUtil.calculateAmountAndShowInTextView(tv_amount);
						}
					}).setNeutralButton(R.string.cancle, null).create();
					myDialog.show();

				}
			});

			final EditText et_quantity = holder.et_quantity;

			ViewUtil.setLoseFocusWhenDoneButtonPress(et_quantity);

			et_quantity.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String text = et_quantity.getText().toString();
					int quantity = 0;
					if (!StringUtil.isEmpty(text)) {
						quantity = Integer.valueOf(et_quantity.getText().toString());
					}

					drugUse.setSaleQuantity(quantity);

					FeeUtil.calculateAmountAndShowInTextView(tv_amount);
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			et_quantity.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						String text = et_quantity.getText().toString();
						int quantity = 0;
						if (!StringUtil.isEmpty(text)) {
							quantity = Integer.valueOf(et_quantity.getText().toString());
						}

						if (quantity == 0) {
							quantity = 1;
							et_quantity.setText(String.valueOf(1));
						}
						drugUse.setSaleQuantity(quantity);

						FeeUtil.calculateAmountAndShowInTextView(tv_amount);
					}
				}
			});

			holder.iv_minus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quantity = Integer.valueOf(et_quantity.getText().toString());

					if (quantity == 1) {
						return;
					}
					int newQuantity = quantity - 1;

					drugUse.setSaleQuantity(newQuantity);
					et_quantity.setText(String.valueOf(newQuantity));

					FeeUtil.calculateAmountAndShowInTextView(tv_amount);
				}
			});

			holder.iv_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quantity = Integer.valueOf(et_quantity.getText().toString());

					int newQuantity = quantity + 1;

					drugUse.setSaleQuantity(newQuantity);
					et_quantity.setText(String.valueOf(newQuantity));

					FeeUtil.calculateAmountAndShowInTextView(tv_amount);
				}
			});

			holder.rlt_group.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog_wheelView.show();

					currentClickPosition = position;
				}
			});

			return convertView;
		}

	}

	class ViewHolder {
		TextView tv_drug_name, tv_specification, tv_unit_price, tv_single_dosage, tv_direction, tv_frequency,
				tv_comment, tv_group;
		ImageView iv_delete, iv_minus, iv_add;
		EditText et_quantity;
		RelativeLayout rlt_group;
	}

}
