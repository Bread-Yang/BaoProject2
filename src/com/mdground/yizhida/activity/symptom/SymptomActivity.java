package com.mdground.yizhida.activity.symptom;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.activity.doctorlist.DoctorSelectListActivity;
import com.mdground.yizhida.adapter.CurrentSymptomsAdapter;
import com.mdground.yizhida.bean.AppointmentInfo;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.SymptomDao;

/**
 * 目前症状界面
 * 
 * @author Administrator
 * 
 */

public class SymptomActivity extends BaseActivity implements OnClickListener, OnItemClickListener, SymptomView {
	private ImageView TvBack;
	private TextView BtConfirm;
	private GridView GvCurrentSym;
	private CurrentSymptomsAdapter symptomsAdapter;
	private List<Symptom> symptoms;
	private AppointmentInfo appointmentInfo;

	private SymptomDao mSymptomDao;
	private SymptomPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_symptoms);
		findView();
		initMemberData();
		initView();
		setListener();
	}

	@Override
	public void findView() {
		BtConfirm = (TextView) this.findViewById(R.id.comfirm);
		TvBack = (ImageView) this.findViewById(R.id.back);
		GvCurrentSym = (GridView) this.findViewById(R.id.symptoms_grid);
	}

	@Override
	public void initView() {
		symptomsAdapter = new CurrentSymptomsAdapter(this);
		symptomsAdapter.setDataList(symptoms);
		GvCurrentSym.setAdapter(symptomsAdapter);
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		if (intent != null) {
			appointmentInfo = intent.getParcelableExtra(MemberConstant.APPOINTMENT);
		}
		mSymptomDao = SymptomDao.getInstance(this);
		symptoms = mSymptomDao.getSymptoms();
		presenter = new SymptomPresenterImpl(this);
	}

	@Override
	public void setListener() {
		BtConfirm.setOnClickListener(this);
		TvBack.setOnClickListener(this);
		GvCurrentSym.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comfirm: {
			if (appointmentInfo.getDoctorID() != 0) {
				appointmentInfo.setRemark(getRemark());// 选中的
				presenter.saveAppointment(appointmentInfo);
			} else {
				Intent intent = new Intent(this, DoctorSelectListActivity.class);
				appointmentInfo.setRemark(getRemark());// 选中的
				intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
				startActivityForResult(intent, MemberConstant.APPIONTMENT_REQUEST_CODE);
			}
			break;
		}
		case R.id.back: {
			onBackPressed();
			break;
		}
		}
	}

	private String getRemark() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < symptoms.size(); i++) {
			if (symptoms.get(i).isSelected()) {
				sb.append(symptoms.get(i).getTemplateName());
				sb.append(",");
			}
		}
		
		if (sb.length() == 0) {
			return sb.toString();
		}

		return sb.subSequence(0, sb.length() - 1).toString();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		boolean isSelected = symptoms.get(position).isSelected();
		if (isSelected) {
			symptoms.get(position).setSelected(false);
		} else {
			symptoms.get(position).setSelected(true);
		}
		symptomsAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.APPIONTMENT_REQUEST_CODE && resultCode == MemberConstant.APPIONTMENT_RESOULT_CODE && data != null) {
			setResult(resultCode, data);
			finish();
		}
	}

	@Override
	public void finishResult(int appiontmentResoultCode, AppointmentInfo appointmentInfo) {
		Intent intent = new Intent();
		intent.putExtra(MemberConstant.APPOINTMENT, appointmentInfo);
		setResult(appiontmentResoultCode, intent);
		finish();

	}
}
