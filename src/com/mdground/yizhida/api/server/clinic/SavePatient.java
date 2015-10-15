package com.mdground.yizhida.api.server.clinic;

import java.util.Date;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.ClinicRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.utils.DeviceUtils;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Patient;

public class SavePatient extends ClinicRequest {
	private static final String FUNCTION_NAME = "SavePatient";

	public SavePatient(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void savePatient(Patient patientDetail, RequestCallBack requestCallBack) {
		if (patientDetail == null) {
			return;
		}

		setRequestCallBack(requestCallBack);
		
		MdgAppliction appliction = null;
		if (mContext != null && mContext.getApplicationContext() instanceof MdgAppliction) {
			appliction = (MdgAppliction) mContext.getApplicationContext();
		}else{
			throw new RuntimeException("please create MdgAppliction !!!");
		}
		
		if (appliction == null) {
			return;
		}
		
		Employee loginEmployee = appliction.getLoginEmployee();
		PatientData queryObj = new PatientData(patientDetail, loginEmployee.getEmployeeID(), loginEmployee.getEmployeeRole());
		
		PatientQuery patientQuery = new PatientQuery();
		patientQuery.PatientData = queryObj;
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		getData().setQueryData(gson.toJson(patientQuery));
		pocess();
	}

	protected class PatientQuery {
		PatientData PatientData;

		public PatientData getPatientData() {
			return PatientData;
		}

		public void setPatientData(PatientData patientData) {
			PatientData = patientData;
		}

	}

	protected class PatientData {
		String Address;
		Date DOB;
		int CityID;
		int CountryID;
		int DistrictID;
		int ClinicID;
		String CompanyName;
		String Email;
		String EmergencyName;
		String EmergencyPhone;
		String EmergencyRelationship;
		int EnterType;
		String ForeignName;
		int Gender;
		String IDCard;
		int Married;
		String MSN;
		String Nation;
		String PatientCode;
		int PatientGUID;
		int PatientID;
		String PatientName;
		String Phone;
		int PhotoID;
		int ProvinceID;
		String SSN;
		int Status;
		String Street;
		int PhotoSID;
		int CreatedBy;
		int CreatedRole;
		Date RegistrationTime;
		String Title;

		public PatientData(Patient patient, int createBy, int createdRole) {
			this.Address = patient.getAddress();
			this.CityID = patient.getCityID();
			this.ClinicID = patient.getClinicID();
			this.CompanyName = patient.getCompanyName();
			this.CountryID = patient.getCountryID();
			this.CreatedBy = createBy;
			this.CreatedRole = createdRole;
			this.DistrictID = patient.getDistrictID();
			this.DOB = patient.getDOB();
			this.Email = patient.getEmail();
			this.EmergencyName = patient.getEmergencyName();
			this.EmergencyPhone = patient.getEmergencyPhone();
			this.EmergencyRelationship = patient.getEmergencyRelationship();
			this.EnterType = DeviceUtils.getPlatformType(mContext);
			this.ForeignName = patient.getForeignName();
			this.Gender = patient.getGender();
			this.IDCard = patient.getIDCard();
			this.Married = patient.getMarried();
			this.MSN = patient.getMSN();
			this.Nation = patient.getNation();
			this.PatientCode = patient.getPatientCode();
			this.PatientGUID = patient.getPatientGUID();
			this.PatientID = patient.getPatientID();
			this.PatientName = patient.getPatientName();
			this.Phone = patient.getPhone();
			this.PhotoID = patient.getPhotoID();
			this.PhotoSID = patient.getPhotoSID();
			this.ProvinceID = patient.getProvinceID();
			this.RegistrationTime = patient.getRegistrationTime();
			this.SSN = patient.getSSN();
			this.Status = patient.getStatus();
			this.Street = patient.getStreet();
			this.Title = patient.getTitle();
		}
	}
}
