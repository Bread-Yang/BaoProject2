package com.mdground.yizhida.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.adapter.LocationAdapter;
import com.mdground.yizhida.bean.Location;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.LocationUtils;

public class AddressActivity extends BaseActivity implements OnClickListener {
	private ImageView TvBack;
	private TextView BtSave;
	private LocationUtils mLocationUtils;

	private RelativeLayout contryLayout;
	private RelativeLayout provinceLayout;
	private RelativeLayout cityLayout;
	private RelativeLayout districtLayout;

	private TextView TvContryValue;
	private TextView TvProvinceValue;
	private TextView TvCityValue;
	private TextView TvDistrictValue;
	private EditText EtStreet;

	private LinearLayout mainLayout;

	private View mPopuView;
	private PopupWindow mPopupWindow;
	private ListView mLocationListView;
	private LocationAdapter locationAdapter;
	private List<Location> locations;

	private int currentItemID = 0;

	private int conturyId = -1;
	private int provinceId = -1;
	private int cityId = -1;
	private int districtId = -1;

	private String street = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_layout);
		initMemberData();
		findView();
		initPopuWindow();
		setListener();
		initView();
	}

	@Override
	public void findView() {
		BtSave = (TextView) this.findViewById(R.id.save);
		TvBack = (ImageView) this.findViewById(R.id.back);
		provinceLayout = (RelativeLayout) this
				.findViewById(R.id.province_layout);
		cityLayout = (RelativeLayout) this.findViewById(R.id.city_layout);
		districtLayout = (RelativeLayout) this
				.findViewById(R.id.district_layout);
		mainLayout = (LinearLayout) this.findViewById(R.id.address_main_layout);
		contryLayout = (RelativeLayout) findViewById(R.id.country_layout);

		TvContryValue = (TextView) findViewById(R.id.country_value);
		TvProvinceValue = (TextView) this.findViewById(R.id.province_value);
		TvCityValue = (TextView) this.findViewById(R.id.city_value);
		TvDistrictValue = (TextView) this.findViewById(R.id.district_value);
		EtStreet = (EditText) this.findViewById(R.id.street);

		mPopuView = LayoutInflater.from(this).inflate(
				R.layout.layout_popoup_location_window, null);

		mLocationListView = (ListView) mPopuView
				.findViewById(R.id.location_listview);

	}

	private void initPopuWindow() {
		locationAdapter = new LocationAdapter(this);
		locationAdapter.setDataList(locations);
		locations = mLocationUtils.getListFromParentId(86);// 获取国家列表
		mLocationListView.setAdapter(locationAdapter);
		mLocationListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (currentItemID) {
				case R.id.country_layout:
					// contryLocation = locations.get(position);
					break;
				case R.id.province_layout:
					if (locations.get(position).getLocationID() != provinceId) {
						provinceId = (int) locations.get(position)
								.getLocationID();
						cityId = -1;
						districtId = -1;
					}
					break;
				case R.id.city_layout:
					if (locations.get(position).getLocationID() != cityId) {
						cityId = (int) locations.get(position).getLocationID();
						districtId = -1;
					}
					break;
				case R.id.district_layout:
					districtId = (int) locations.get(position).getLocationID();
					break;
				default:
					break;
				}
				updateView();
				locations.clear();
				mPopupWindow.dismiss();
			}

		});
	}

	private void updateView() {
		// 设置国家
		String countryText = (conturyId > 0) ? mLocationUtils
				.getNameFromId(conturyId) : "请选择";
		TvContryValue.setText(countryText);
		// 省份
		String provinceText = (provinceId > 0) ? mLocationUtils
				.getNameFromId(provinceId) : "请选择";
		TvProvinceValue.setText(provinceText);
		// 城市
		String cityText = (cityId > 0) ? mLocationUtils.getNameFromId(cityId)
				: "请选择";
		TvCityValue.setText(cityText);
		// 区域
		String districtText = (districtId > 0) ? mLocationUtils
				.getNameFromId(districtId) : "请选择";
		TvDistrictValue.setText(districtText);

		EtStreet.setText(street);
	}

	private void showPopuWindow(int viewId) {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mPopuView,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setAnimationStyle(R.style.AnimBottom);
			mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {

				}
			});

		}

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return;
		} else {

		}
		mPopupWindow.showAtLocation(mainLayout, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Override
	public void initView() {
		updateView();
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		conturyId = intent.getIntExtra(MemberConstant.LOCATION_CONTURY_ID, 86);
		provinceId = intent
				.getIntExtra(MemberConstant.LOCATION_PROVINCE_ID, -1);
		cityId = intent.getIntExtra(MemberConstant.LOCATION_CITY_ID, -1);
		districtId = intent
				.getIntExtra(MemberConstant.LOCATION_DISTRICT_ID, -1);
		street = intent.getStringExtra(MemberConstant.LOCATION_STREET);
		mLocationUtils = LocationUtils.getInstance(this);
	}

	@Override
	public void setListener() {
		BtSave.setOnClickListener(this);
		TvBack.setOnClickListener(this);
		provinceLayout.setOnClickListener(this);
		cityLayout.setOnClickListener(this);
		districtLayout.setOnClickListener(this);
		contryLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save: {
			street = EtStreet.getText().toString();
			StringBuffer sb = new StringBuffer();
			if (provinceId != -1) {
				sb.append(TvProvinceValue.getText().toString());
			}

			if (cityId != -1) {
				sb.append(TvCityValue.getText().toString());
			}

			if (districtId != -1) {
				sb.append(TvDistrictValue.getText().toString());
			}

			sb.append(street);

			Intent intent = new Intent();
			intent.putExtra(MemberConstant.LOCATION_PROVINCE_ID, provinceId);
			intent.putExtra(MemberConstant.LOCATION_CITY_ID, cityId);
			intent.putExtra(MemberConstant.LOCATION_DISTRICT_ID, districtId);
			intent.putExtra(MemberConstant.LOCATION_CONTURY_ID, conturyId);
			intent.putExtra(MemberConstant.LOCATION_STREET, street);
			intent.putExtra(MemberConstant.LOCATION_ADDRESS, sb.toString());
			setResult(MemberConstant.LOCATION_RESOULT_CODE, intent);
			finish();
			break;
		}

		case R.id.back: {
			onBackPressed();
			break;
		}

		case R.id.country_layout: {
			currentItemID = R.id.country_layout;
			locations.clear();
			locations.add(mLocationUtils.getLocationById(86));
			locationAdapter.setDataList(locations);
			showPopuWindow(R.id.country_layout);
			break;
		}

		case R.id.province_layout: {
			currentItemID = R.id.province_layout;
			locations.clear();
			locations = mLocationUtils.getListFromParentId(conturyId);
			locationAdapter.setDataList(locations);
			showPopuWindow(R.id.province_layout);
			break;
		}
		case R.id.city_layout: {
			currentItemID = R.id.city_layout;
			locations.clear();
			locations = mLocationUtils.getListFromParentId(provinceId);
			locationAdapter.setDataList(locations);
			showPopuWindow(R.id.city_layout);
			break;
		}
		case R.id.district_layout: {
			currentItemID = R.id.district_layout;
			locations.clear();
			locations = mLocationUtils.getListFromParentId(cityId);
			locationAdapter.setDataList(locations);
			showPopuWindow(R.id.district_layout);
			break;
		}
		}
	}
}
