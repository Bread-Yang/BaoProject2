package com.mdground.yizhida.activity.searchdrug;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.DrugCategoryDao;
import com.mdground.yizhida.db.dao.DrugDao;
import com.mdground.yizhida.view.ResizeLinearLayout;
import com.mdground.yizhida.view.ResizeLinearLayout.OnResizeListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

	private DrugAdapter drugAdapter;
	private Employee loginEmployee;
	private List<Drug> drugs = new ArrayList<Drug>();// 搜索结果保存

	List<DrugCategory> drugCategories;

	private DrugDao drugDao;
	private DrugCategoryDao drugCategoryDao;

	private String searchDrugName;
	private int searchDrugTypeID;

	private HashSet<Integer> alreadyAddDrugSet;

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
		this.loginEmployee = app.getLoginEmployee();
		// 请求症状列表
		// searchDrugAdapter = new SearchDetailAdapter<Patient>(this,
		// mAppiontmentCallBack);
		// searchDrugAdapter.setDataList(patients);

		alreadyAddDrugSet = (HashSet<Integer>) getIntent()
				.getSerializableExtra(MemberConstant.PRESCRIPTION_ALREADY_ADD_DRUG_SET);

		drugDao = DrugDao.getInstance(getApplicationContext());
		drugCategoryDao = drugCategoryDao.getInstance(getApplicationContext());

		drugCategories = drugCategoryDao.getDrugCateogriesByClinicID(loginEmployee.getClinicID());

		for (DrugCategory item : drugCategories) {
			RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radiobutton_drug_category,
					null);

			radioButton.setText(item.getTypeName());

			rg_classification.addView(radioButton);

			RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
			params.leftMargin = PxUtil.dip2px(getApplicationContext(), 15);
			radioButton.setLayoutParams(params);
		}

		drugs = drugDao.getAllDrug();

		drugAdapter = new DrugAdapter();
		LvSearchResult.setAdapter(drugAdapter);

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

				searchDrugName = EtSearchKey.getText().toString();

				if (TextUtils.isEmpty(searchDrugName)) {
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
				L.e(SearchDrugActivity.this, "checkedId : " + checkedId);
				if (checkedId != -1) {
					int newTypeID = drugCategories.get(checkedId - 1).getTypeID();
					if (searchDrugTypeID == newTypeID) {
						return;
					}
					searchDrugTypeID = newTypeID;
				} else {
					searchDrugTypeID = -1;
				}
				refreshDrugListView();
			}
		});
	}

	private void refreshDrugListView() {
		drugs.clear();

		int clinicID = loginEmployee.getClinicID();

		if (searchDrugTypeID != -1) {
			if (!TextUtils.isEmpty(searchDrugName)) {
				drugs = drugDao.getDrugByDrugNameAndClinicIDAndTypeID(searchDrugName, clinicID, searchDrugTypeID);
			} else {
				drugs = drugDao.getDrugByClinicIDAndTypeID(clinicID, searchDrugTypeID);
			}
		} else {
			if (!TextUtils.isEmpty(searchDrugName)) {
				drugs = drugDao.getDrugByDrugNameAndClinicID(searchDrugName, clinicID);
			} else {
				drugs = drugDao.getDrugByClinicID(clinicID);
			}
		}

		drugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Drug drug = drugs.get(position);

		if (!alreadyAddDrugSet.contains(drug.getDrugID())) {
			new GetDurgInfoByID(getApplicationContext()).getDurgInfoByID(drug.getDrugID(), new RequestCallBack() {

				@Override
				public void onSuccess(ResponseData response) {
					if (response.getCode() == ResponseCode.Normal.getValue()) {
						Drug drugDetail = response.getContent(Drug.class);

						alreadyAddDrugSet.add(drug.getDrugID());
						drugAdapter.notifyDataSetChanged();

						Intent intent = new Intent();
						intent.putExtra(MemberConstant.PRESCRIPTION_NEW_ADD_DRUG, drugDetail);
						intent.putExtra(MemberConstant.PRESCRIPTION_ALREADY_ADD_DRUG_SET, alreadyAddDrugSet);
						setResult(RESULT_OK, intent);
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
			alreadyAddDrugSet.remove(drug.getDrugID());
			drugAdapter.notifyDataSetChanged();
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

		if (this.drugs.size() == 0) {
			return;
		}

	}

	private class DrugAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return drugs.size();
		}

		@Override
		public Object getItem(int position) {
			return drugs.get(position);
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

			Drug drug = drugs.get(position);

			holder.tv_drug_name.setText(drug.getDrugName());
			holder.tv_drug_description.setText(drug.getSpecification());

			if (alreadyAddDrugSet.contains(drug.getDrugID())) {
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
