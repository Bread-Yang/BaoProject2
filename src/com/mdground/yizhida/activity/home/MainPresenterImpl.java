package com.mdground.yizhida.activity.home;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.bean.DoctorAppointmentCount;
import com.mdground.yizhida.api.server.clinic.GetAppointmentCountForDoctor;
import com.mdground.yizhida.api.server.clinic.GetChiefComplaintTemplateList;
import com.mdground.yizhida.api.server.global.GetEmployeeList;
import com.mdground.yizhida.api.server.global.UpdateDeviceToken;
import com.mdground.yizhida.bean.Doctor;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Symptom;
import com.mdground.yizhida.db.dao.EmployeeDao;
import com.mdground.yizhida.db.dao.SymptomDao;

public class MainPresenterImpl implements MainPresenter {
	private static final String TAG = "MainPresenterImpl";

	MainView mView;
	SymptomDao symptomDao;
	EmployeeDao mEmployeeDao;

	public MainPresenterImpl(MainView view) {
		this.mView = view;
		symptomDao = SymptomDao.getInstance((Context) mView);
		mEmployeeDao = EmployeeDao.getInstance((Context) mView);
	}

	@Override
	public void getAppointmentCountForDoctor(List<Doctor> doctorList) {
		GetAppointmentCountForDoctor getApCntDoctor = new GetAppointmentCountForDoctor((Context) mView);
		getApCntDoctor.getAppointmentCountForWaiting(doctorList, new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					DoctorAppointmentCount count = response.getContent(DoctorAppointmentCount.class);
					mView.setAppointmentCount(count);
				}
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
	}

	@Override
	public void getChiefComplaintTemplateList() {
		symptomDao.deleteAll();
		
		GetChiefComplaintTemplateList getChiefComplaint = new GetChiefComplaintTemplateList((Context) mView);
		getChiefComplaint.getChiefComplaintTemplateList(new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Symptom> symptoms = response.getContent(new TypeToken<List<Symptom>>() {
					});
					symptomDao.saveSymptoms(symptoms);
				} else {
					Log.e(TAG, "getChiefComplaintTemplateList failed!!  code=" + response.getCode());
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.e(TAG, "getChiefComplaintTemplateList onFailure!! ");
			}
		});
	}

	@Override
	public void getEmployeeList() {
		mEmployeeDao.deleteAll();
		
		GetEmployeeList getEmloyeeList = new GetEmployeeList((Context) mView);
		getEmloyeeList.getEmployeeList(new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					List<Employee> employees = response.getContent(new TypeToken<List<Employee>>() {
					});
					mEmployeeDao.saveEmployeess(employees);
				} else {
					Log.e(TAG, "getEmployeeList failed!! errorCode " + response.getCode());
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.e(TAG, "getChiefComplaintTemplateList onFailure!! ");
			}
		});

	}

	@Override
	public void updateDeviceToken(String deviceToken) {
		UpdateDeviceToken updateDeviceToken = new UpdateDeviceToken((Context) mView);
		updateDeviceToken.updateDeviceToken(deviceToken, new RequestCallBack() {
			
			@Override
			public void onSuccess(ResponseData response) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
