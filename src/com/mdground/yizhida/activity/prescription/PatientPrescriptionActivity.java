package com.mdground.yizhida.activity.prescription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.searchdrug.SearchDrugActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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

	private List<Fee> mFeeList;

	private List<OfficeVisitFee> mOfficeVisitFeeList = new ArrayList<OfficeVisitFee>();

	private PrescriptionAdapter mAdapter;

	private DrugUse mDrugUseAddTemplate;

	private DrugDao mDrugDao;

	private HashSet<Integer> mAlreadyAddDrugIDSet = new HashSet<Integer>();

	private SparseArray<DrugUse> mDrugUseSparseArray = new SparseArray<DrugUse>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_patient_prescription);

		findViewById();
		init();
		setListener();

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

		llt_fee_button = (LinearLayout) dialog_menu.findViewById(R.id.llt_fee_button);
		btn_cancel = (Button) dialog_menu.findViewById(R.id.btn_cancel);
	}

	private void init() {
		mDrugDao = DrugDao.getInstance(getApplicationContext());

		List<DrugUse> drugUseList = getIntent().getParcelableArrayListExtra(MemberConstant.APPOINTMENT_DRUG_USE_LIST);
		if (drugUseList != null) {
			for (DrugUse item : drugUseList) {
				mDrugUseSparseArray.append(item.getDrugID(), item);
			}
		}

		tv_title.setText(R.string.prescription);
		tv_top_right.setText(R.string.save);

		FeeDao feeDao = FeeDao.getInstance(this);

		mFeeList = feeDao.getFeesByClinicID(((MedicalAppliction) getApplication()).getLoginEmployee().getClinicID());

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

					if (llt_fee_display.findViewById(item.getFTID()) == null) {
						final RelativeLayout rlt_prescription_fee = (RelativeLayout) getLayoutInflater()
								.inflate(R.layout.layout_prescription_fee, null);

						rlt_prescription_fee.setId(item.getFTID());

						llt_fee_display.addView(rlt_prescription_fee);

						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlt_prescription_fee
								.getLayoutParams();
						params.height = PxUtil.dip2px(getApplicationContext(), 50);
						params.topMargin = PxUtil.dip2px(getApplicationContext(), 10);
						rlt_prescription_fee.setLayoutParams(params);

						TextView tv_fee_name = (TextView) rlt_prescription_fee.findViewById(R.id.tv_fee_name);
						tv_fee_name.setText(item.getFeeType());

						EditText et_fee = (EditText) rlt_prescription_fee.findViewById(R.id.et_fee);
						et_fee.setText(String.valueOf(item.getTotalFee() / 100f));
						int position = et_fee.length();
						Editable etext = et_fee.getText();
						Selection.setSelection(etext, position);

						// 只能输入两位小数
						et_fee.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

						final OfficeVisitFee officeFee = new OfficeVisitFee();

						officeFee.setClinicID(item.getClinicID());
						officeFee.setVisitID(mDrugUseAddTemplate.getVisitID());
						officeFee.setDoctorID(item.getDoctorID());
						officeFee.setPatientID(mDrugUseAddTemplate.getPatientID());
						officeFee.setFeeType(item.getFeeType());
						officeFee.setTotalFee(item.getTotalFee());

						mOfficeVisitFeeList.add(officeFee);
						
						calculateAmount();

						et_fee.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								if (!TextUtils.isEmpty(s)) {
									officeFee.setTotalFee((int) (Float.valueOf(s.toString()) * 100));
								} else {
									officeFee.setTotalFee(0);
								}

								calculateAmount();
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
								llt_fee_display.removeView(rlt_prescription_fee);

								mOfficeVisitFeeList.remove(officeFee);

								calculateAmount();
							}
						});
					}

					dialog_menu.dismiss();
				}
			});
		}

		mDrugUseAddTemplate = getIntent().getParcelableExtra(MemberConstant.APPOINTMENT_DRUG_USE_ADD_TEMPLATE);

		mAdapter = new PrescriptionAdapter();
		lv_drug.setAdapter(mAdapter);

		calculateAmount();
	}

	private void setListener() {
		tv_top_right.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		lv_drug.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(PatientPrescriptionActivity.this, PatientDrugDetailActivity.class);
				intent.putExtra(MemberConstant.PRESCRIPTION_DRUG_USE, mDrugUseSparseArray.valueAt(position));
				startActivityForResult(intent, REQUEST_CODE_EDIT_DRUG);
			}
		});
	}

	private void calculateAmount() {
		float amount = 0;

		// 费用
		for (int i = 0; i < mOfficeVisitFeeList.size(); i++) {
			amount += mOfficeVisitFeeList.get(i).getTotalFee() / 100f;
		}

		// 药物
		for (int i = 0; i < mDrugUseSparseArray.size(); i++) {
			DrugUse drugUse = mDrugUseSparseArray.valueAt(i);

			amount += drugUse.getSaleQuantity() * drugUse.getSalePrice() / 100f;
		}

		tv_amount.setText(String.valueOf(amount));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;

		case R.id.tv_top_right:
			if (mFeeList.size() == 0 && mDrugUseSparseArray.size() == 0) {
				Toast.makeText(getApplicationContext(), R.string.no_fee, Toast.LENGTH_SHORT);
				return;
			}

			List<DrugUse> drugUseList = new ArrayList<DrugUse>();
			for (int i = 0; i < mDrugUseSparseArray.size(); i++) {
				drugUseList.add(mDrugUseSparseArray.valueAt(i));
			}

			new SaveDrugUseForOV(getApplicationContext()).saveDrugUseForOV(mDrugUseAddTemplate.getVisitID(),
					drugUseList, mOfficeVisitFeeList, new RequestCallBack() {

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
			dialog_menu.show();
			break;
		case R.id.rlt_drug:
			Intent intent = new Intent(this, SearchDrugActivity.class);
			intent.putExtra(MemberConstant.PRESCRIPTION_ALREADY_ADD_DRUG_SET, mAlreadyAddDrugIDSet);
			startActivityForResult(intent, REQUEST_CODE_ADD_DRUG);
			break;
		case R.id.btn_cancel:
			dialog_menu.dismiss();
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ADD_DRUG:
				mAlreadyAddDrugIDSet = (HashSet<Integer>) data
						.getSerializableExtra(MemberConstant.PRESCRIPTION_ALREADY_ADD_DRUG_SET);

				Drug newDrug = data.getParcelableExtra(MemberConstant.PRESCRIPTION_NEW_ADD_DRUG);

				DrugUse drugUse = new DrugUse();
				drugUse.setClinicID(mDrugUseAddTemplate.getClinicID());
				drugUse.setVisitID(mDrugUseAddTemplate.getVisitID());
				drugUse.setDoctorID(mDrugUseAddTemplate.getDoctorID());
				drugUse.setPatientID(mDrugUseAddTemplate.getPatientID());
				drugUse.setSaleQuantity(1);
				drugUse.setDays(1);
				drugUse.setDrugID(newDrug.getDrugID());
				drugUse.setTake(newDrug.getDosageSingle());
				drugUse.setTakeType(newDrug.getTakeType());
				drugUse.setTakeUnit(newDrug.getDosageUnit());
				drugUse.setFrequency(newDrug.getFrequency());
				drugUse.setSaleUnit(newDrug.getSaleUnit());
				drugUse.setSalePrice(newDrug.getSalePirce());
				drugUse.setDrugName(newDrug.getDrugName());
				drugUse.setInventoryCount(newDrug.getInventoryQuantity());

				mDrugUseSparseArray.append(newDrug.getDrugID(), drugUse);

				mAdapter.notifyDataSetChanged();

				calculateAmount();
				break;

			case REQUEST_CODE_EDIT_DRUG: {
				DrugUse editDrugUse = data.getParcelableExtra(MemberConstant.APPOINTMENT_DRUG_USE_EDIT);
				mDrugUseSparseArray.put(editDrugUse.getDrugID(), editDrugUse);

				mAdapter.notifyDataSetChanged();

				calculateAmount();
			}
				break;
			}
		}
	}

	private class PrescriptionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDrugUseSparseArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mDrugUseSparseArray.valueAt(position);
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

			final DrugUse drugUse = mDrugUseSparseArray.valueAt(position);

			holder.tv_drug_name.setText(drugUse.getDrugName());

			holder.et_quantity.setText(String.valueOf(drugUse.getSaleQuantity()));

			Drug drug = mDrugDao.getDrugByDrugID(drugUse.getDrugID());
			if (drug != null) {
				holder.tv_specification.setText(drug.getSpecification());
			} else {
				holder.tv_specification.setText("");
			}

			holder.tv_unit_price.setText(drugUse.getSalePrice() / 100f + getResources().getString(R.string.yuan));

			holder.tv_single_dosage.setText(drugUse.getTake() + drugUse.getTakeUnit());

			holder.tv_direction.setText(drugUse.getTakeType());

			holder.tv_frequency.setText(drugUse.getFrequency());

			holder.tv_comment.setText(drugUse.getRemark());

			if (drugUse.getGroupNo() == 0) {
				holder.tv_group.setText(R.string.not_yet_group);
			} else {
				holder.tv_group.setText(getResources().getString(R.string.group) + drugUse.getGroupNo());
			}

			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDrugUseSparseArray.removeAt(position);
					mAdapter.notifyDataSetChanged();

					calculateAmount();
				}
			});

			final EditText et_quantity = holder.et_quantity;

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

					calculateAmount();
				}
			});

			holder.iv_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quantity = Integer.valueOf(et_quantity.getText().toString());

					int newQuantity = quantity + 1;

					drugUse.setSaleQuantity(newQuantity);
					et_quantity.setText(String.valueOf(newQuantity));

					calculateAmount();
				}
			});

			holder.rlt_group.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

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
