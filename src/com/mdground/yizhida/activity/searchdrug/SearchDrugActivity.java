package com.mdground.yizhida.activity.searchdrug;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetDurgInfoByID;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.api.utils.PxUtil;
import com.mdground.yizhida.bean.Drug;
import com.mdground.yizhida.bean.DrugCategory;
import com.mdground.yizhida.bean.DrugUse;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugCategoryDao;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.view.ResizeLinearLayout;
import com.mdground.yizhida.view.ResizeLinearLayout.OnResizeListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("NewApi")
public class SearchDrugActivity extends BaseActivity
		implements OnItemClickListener, OnEditorActionListener, OnClickListener, OnResizeListener {

	/** 取消搜索 **/
	private TextView TvCancelSearch;
	/** 清除搜索关键字 **/
	private ImageView IvClearSearchKey;
	/** 关键字输入框 **/
	private EditText EtSearchKey;
	/** 搜索分类 **/
	private RadioGroup rg_classification;
	/** 搜索结果 **/
	private ListView LvSearchResult;

	private ResizeLinearLayout searchRootLayout;

	private DrugAdapter mDrugAdapter;
	private Employee mLoginEmployee;
	private List<Drug> mDrugsList = new ArrayList<Drug>();// 搜索结果保存

	private List<DrugCategory> mDrugCategories;

	private DrugDao mDrugDao;
	private DrugCategoryDao mDrugCategoryDao;

	private String mSearchDrugName;
	private int mSearchDrugTypeID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_drug);
		findView();
		initView();
		initMemberData();
		setListener();
	}

	@Override
	public void findView() {
		TvCancelSearch = (TextView) this.findViewById(R.id.cancel);
		IvClearSearchKey = (ImageView) this.findViewById(R.id.clear_edit);
		EtSearchKey = (EditText) this.findViewById(R.id.search_edit);
		rg_classification = (RadioGroup) findViewById(R.id.rg_classification);
		LvSearchResult = (ListView) this.findViewById(R.id.listview_search_result);
		searchRootLayout = (ResizeLinearLayout) findViewById(R.id.layout_root_search_patient);
		searchRootLayout.setOnResizeListener(this);
	}

	@Override
	public void initView() {

		// LvSearchResult.setEmptyView(searchPromptLayout);
	}

	@Override
	public void initMemberData() {
		EtSearchKey.setHint(R.string.search_drug_hint);

		MedicalAppliction app = (MedicalAppliction) getApplication();
		this.mLoginEmployee = app.getLoginEmployee();
		// 请求症状列表
		// searchDrugAdapter = new SearchDetailAdapter<Patient>(this,
		// mAppiontmentCallBack);
		// searchDrugAdapter.setDataList(patients);

		mDrugDao = DrugDao.getInstance(getApplicationContext());
		mDrugCategoryDao = mDrugCategoryDao.getInstance(getApplicationContext());

		mDrugCategories = mDrugCategoryDao.getDrugCateogriesByClinicID(mLoginEmployee.getClinicID());

		for (DrugCategory item : mDrugCategories) {
			RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radiobutton_drug_category,
					null);
			
			radioButton.setId(item.getTypeID());

			radioButton.setText(item.getTypeName());

			rg_classification.addView(radioButton);

			RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
			params.leftMargin = PxUtil.dip2px(getApplicationContext(), 15);
			radioButton.setLayoutParams(params);
		}

		mDrugsList = mDrugDao.getAllDrug();

		mDrugAdapter = new DrugAdapter();
		LvSearchResult.setAdapter(mDrugAdapter);

	}

	@Override
	public void setListener() {
		LvSearchResult.setOnItemClickListener(this);
		EtSearchKey.setOnEditorActionListener(this);
		TvCancelSearch.setOnClickListener(this);
		IvClearSearchKey.setOnClickListener(this);

		EtSearchKey.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				mSearchDrugName = EtSearchKey.getText().toString();

				if (TextUtils.isEmpty(mSearchDrugName)) {
					IvClearSearchKey.setVisibility(View.INVISIBLE);
				} else {
					IvClearSearchKey.setVisibility(View.VISIBLE);
				}

				refreshDrugListView();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		rg_classification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				L.e(SearchDrugActivity.this, "checkedId : " + checkedId);
				if (checkedId != -1) {
					int newTypeID = checkedId;
					if (mSearchDrugTypeID == newTypeID) {
						return;
					}
					mSearchDrugTypeID = newTypeID;
				} else {
					mSearchDrugTypeID = -1;
				}
				refreshDrugListView();
			}
		});
	}

	private void refreshDrugListView() {
		mDrugsList.clear();

		int clinicID = mLoginEmployee.getClinicID();

		if (mSearchDrugTypeID != -1) {
			if (!TextUtils.isEmpty(mSearchDrugName)) {
				mDrugsList = mDrugDao.getDrugByDrugNameAndClinicIDAndTypeID(mSearchDrugName, clinicID,
						mSearchDrugTypeID);
			} else {
				mDrugsList = mDrugDao.getDrugByClinicIDAndTypeID(clinicID, mSearchDrugTypeID);
			}
		} else {
			if (!TextUtils.isEmpty(mSearchDrugName)) {
				mDrugsList = mDrugDao.getDrugByDrugNameAndClinicID(mSearchDrugName, clinicID);
			} else {
				mDrugsList = mDrugDao.getDrugByClinicID(clinicID);
			}
		}

		mDrugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Drug drug = mDrugsList.get(position);

		if (!MedicalAppliction.mDrugUseMap.containsKey(String.valueOf(drug.getDrugID()))) {
			new GetDurgInfoByID(getApplicationContext()).getDurgInfoByID(drug.getDrugID(), new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					if (response.getCode() == ResponseCode.Normal.getValue()) {
						Drug drugDetail = response.getContent(Drug.class);

						DrugUse drugUse = new DrugUse();
						drugUse.setClinicID(MedicalAppliction.mDrugUseAddTemplate.getClinicID());
						drugUse.setVisitID(MedicalAppliction.mDrugUseAddTemplate.getVisitID());
						drugUse.setDoctorID(MedicalAppliction.mDrugUseAddTemplate.getDoctorID());
						drugUse.setPatientID(MedicalAppliction.mDrugUseAddTemplate.getPatientID());
						drugUse.setSaleQuantity(1);
						drugUse.setDays(1);
						drugUse.setDrugID(drugDetail.getDrugID());
						drugUse.setTake(drugDetail.getDosageSingle());
						drugUse.setTakeType(drugDetail.getTakeType());
						drugUse.setTakeUnit(drugDetail.getDosageUnit());
						drugUse.setFrequency(drugDetail.getFrequency());
						drugUse.setSaleUnit(drugDetail.getOVUnit());
						drugUse.setSalePrice(drugDetail.getOVPirce());
						drugUse.setDrugName(drugDetail.getDrugName());
						drugUse.setInventoryCount(drugDetail.getInventoryQuantity());

						MedicalAppliction.mDrugUseMap.put(String.valueOf(drugDetail.getDrugID()), drugUse);
						mDrugAdapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onStart() {
					showProgress();
				}

				@Override
				public void onFinish() {
					hidProgress();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
					hidProgress();
				}
			});
		} else {
			MedicalAppliction.mDrugUseMap.remove(String.valueOf(drug.getDrugID()));
			mDrugAdapter.notifyDataSetChanged();
		}

		// InputMethodManager im = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// im.hideSoftInputFromWindow(view.getWindowToken(),
		// InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// 表示按了键盘，使键盘消失，此时要显示详细的
		if (actionId == EditorInfo.IME_ACTION_SEARCH
				|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			if (EtSearchKey.getText().length() > 0) {
				// presenter.searchPatient(EtSearchKey.getText().toString());
			}
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			onBackPressed();
			break;
		case R.id.clear_edit:
			v.setVisibility(View.GONE);
			EtSearchKey.setText("");
			break;
		}
	}

	// 只传递结果，不处理结果值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE
				&& resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE) {
			setResult(resultCode, data);
			finish();
		}
	}

	@Override
	public void OnResize(int w, final int h, int oldw, final int oldh) {
		int offset = oldh - h;
		if (offset > 0) {
		} else {
		}

		if (this.mDrugsList.size() == 0) {
			return;
		}

	}

	private class DrugAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDrugsList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDrugsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_search_drug, null);

				holder = new ViewHolder();
				holder.iv_medical_insurance_icon = (ImageView) convertView.findViewById(R.id.iv_medical_insurance_icon);
				holder.tv_drug_name = (TextView) convertView.findViewById(R.id.tv_drug_name);
				holder.tv_drug_description = (TextView) convertView.findViewById(R.id.tv_drug_description);
				holder.tv_already_add = (TextView) convertView.findViewById(R.id.tv_already_add);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Drug drug = mDrugsList.get(position);

			holder.tv_drug_name.setText(drug.getDrugName());
			holder.tv_drug_description.setText(drug.getSpecification());

			if (MedicalAppliction.mDrugUseMap.containsKey(String.valueOf(drug.getDrugID()))) {
				holder.tv_already_add.setVisibility(View.VISIBLE);
			} else {
				holder.tv_already_add.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView iv_medical_insurance_icon;
			TextView tv_drug_name, tv_drug_description, tv_already_add;
		}
	}
}
