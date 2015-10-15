package com.mdground.yizhida.activity.doctorlist;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.adapter.SelectDoctorRoomListAdapter;
import com.mdground.yizhida.api.bean.DoctorWaittingCount;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.constant.MemberConstant;

/**
 * 选择医生界面
 * 
 * @author Administrator
 * 
 */

public class DoctorSelectListActivity extends BaseActivity implements OnClickListener, OnItemClickListener, DoctorSelectListView {
	private ImageView TvBack;
	private TextView BtConfirm;
	private ListView doctorRoomListView;
	private SelectDoctorRoomListAdapter roomListAdpater;
	private List<Doctor> doctors = new ArrayList<Doctor>();
	private AppointmentInfo appointmentInfo;
	
	private DoctorSelectListPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_doctor_list);
		findView();
		initView();
		setListener();
		initMemberData();
	}

	@Override
	public void findView() {
		BtConfirm = (TextView) this.findViewById(R.id.comfirm);
		TvBack = (ImageView) this.findViewById(R.id.back);
		doctorRoomListView = (ListView) this.findViewById(R.id.doctor_list);
	}

	@Override
	public void initView() {
		roomListAdpater = new SelectDoctorRoomListAdapter(this);
		roomListAdpater.setDataList(doctors);
		doctorRoomListView.setAdapter(roomListAdpater);
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		if (intent != null) {
			appointmentInfo = intent.getParcelableExtra(MemberConstant.APPOINTMENT);
		}

		presenter = new DoctorSelectListPresenterImpl(this);
		presenter.getDoctorList();
	}

	@Override
	public void setListener() {
		BtConfirm.setOnClickListener(this);
		TvBack.setOnClickListener(this);
		doctorRoomListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		for (int i = 0; i < doctors.size(); i++) {
			Doctor doctor = doctors.get(i);
			if (i == position) {
				doctor.setSelected(true);
			} else {
				doctor.setSelected(false);
			}
		}
		roomListAdpater.notifyDataSetChanged();
	}

	private Doctor getSelectDoctor() {
		Doctor doctor = null;
		for (int i = 0; i < doctors.size(); i++) {
			if (doctors.get(i).isSelected()) {
				doctor = doctors.get(i);
				break;
			}
		}
		return doctor;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comfirm: {
			// 设置选择的医生
			Doctor doctor = getSelectDoctor();
			if (doctor == null) {
				Toast.makeText(this, "请先选择医生", Toast.LENGTH_SHORT).show();
				return;
			}

			if (appointmentInfo.getOPID() == 0) {
				// 挂号save
				appointmentInfo.setDoctorID(doctor.getDoctorID());
				appointmentInfo.setDoctorName(doctor.getDoctorName());
				appointmentInfo.setOPEMR(doctor.getEMRType());
				presenter.saveAppointment(appointmentInfo);
			} else {
				// 分配
				presenter.assignAppointment(appointmentInfo, doctor.getDoctorID());
			}
			break;
		}
		case R.id.back: {
			onBackPressed();
			break;
		}

		}
	}

	@Override
	public void finishResult(int resultCode, AppointmentInfo appointment) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointment);
		setResult(resultCode, intent);
		finish();
	}

	@Override
	public void updateDoctorList(List<Doctor> doctorsList) {
		if (doctorsList != null) {
			doctors.addAll(doctorsList);
		}

		if (appointmentInfo != null && appointmentInfo.getDoctorID() != 0) {
			for (int i = 0; i < doctors.size(); i++) {
				if (doctors.get(i).getDoctorID() == appointmentInfo.getDoctorID()) {
					doctors.remove(i);
					break;
				}
			}
		}
		// 请求医生候诊人数
		presenter.getWaitingCountForDoctorList(doctors);
		roomListAdpater.notifyDataSetChanged();
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
		roomListAdpater.notifyDataSetChanged();

	}

}
