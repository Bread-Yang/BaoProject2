package com.mdground.yizhida.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseFragment;
import com.mdground.yizhida.activity.searchpatient.SearchPatientActivity;
import com.mdground.yizhida.activity.waiting.WaitingRomActivity;
import com.mdground.yizhida.adapter.DoctorRoomListAdapter;
import com.mdground.yizhida.api.bean.DoctorWaittingCount;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.constant.MemberConstant;

public class NurseHomeFragment extends BaseFragment implements OnClickListener, OnItemClickListener, NurseHomeView, OnRefreshListener<ListView> {

	private View mainView;

	private RelativeLayout RlMainSearch;
	private DoctorRoomListAdapter doctorListAdapter;

	private ArrayList<Doctor> doctors = new ArrayList<Doctor>();

	private NurseHomePresenter presenter;
	
	private PullToRefreshListView pullToRefreshListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_nurse_waiting_room, null);
		findView();
		initView();
		setListener();
		initData();
		return mainView;
	}

	private void findView() {
		RlMainSearch = (RelativeLayout) mainView.findViewById(R.id.search_layout);
		pullToRefreshListView = (PullToRefreshListView) mainView.findViewById(R.id.main_pull_refresh_view);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		presenter.getDoctorList();
	}

	private void initView() {
		doctorListAdapter = new DoctorRoomListAdapter(getActivity());
		doctorListAdapter.setDataList(doctors);
		pullToRefreshListView.setAdapter(doctorListAdapter);
	}

	private void setListener() {
		RlMainSearch.setOnClickListener(this);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(this);
	}

	/** 初始化数据 **/
	private void initData() {
		// 发起候诊请求数据
		presenter = new NurseHomePresenterImpl(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_layout:
			Intent intent = new Intent(getActivity(), SearchPatientActivity.class);
			getActivity().startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), WaitingRomActivity.class);
		intent.putExtra(MemberConstant.DOCTOR, doctors.get(position - 1));
		intent.putParcelableArrayListExtra(MemberConstant.DOCTOR_LIST, doctors);
		getActivity().startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
	}

	@Override
	public void updateDoctorListView(List<Doctor> doctorsList) {
		if (doctors != null) {
			doctors.clear();
			doctors.addAll(doctorsList);
		}
		presenter.getWaitingCountForDoctorList(doctors);
		doctorListAdapter.notifyDataSetChanged();
	}

	@Override
	public void updateWaitingCount(List<DoctorWaittingCount> waittingCount) {
		if (waittingCount == null || waittingCount.size() == 0) {
			return;
		}
		for (int i = 0; i < waittingCount.size(); i++) {
			DoctorWaittingCount count = waittingCount.get(i);
			for (int j = 0; j < doctors.size(); j++) {
				Doctor doctor = doctors.get(j);
				if (doctor.getDoctorID() == count.getKey()) {
					doctor.setWaittingCount(count.getValue());
				}
			}
		}
		doctorListAdapter.notifyDataSetChanged();
	}

	@Override
	public void refreshComplete() {
		pullToRefreshListView.onRefreshComplete();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		presenter.getDoctorList();
	}

}
