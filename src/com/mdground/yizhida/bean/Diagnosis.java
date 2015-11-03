package com.mdground.yizhida.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Diagnosis implements Parcelable {

	private int VisitID;
	private int ClinicID;
	private int DoctorID;
	private int PatientID;
	private int DiagnosisID;
	private String ICDCode;
	private String DiagnosisName;
	private boolean IsAllergy;
	private int CancerType;
	
	public Diagnosis() {
	}

	public int getVisitID() {
		return VisitID;
	}

	public void setVisitID(int visitID) {
		VisitID = visitID;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getDoctorID() {
		return DoctorID;
	}

	public void setDoctorID(int doctorID) {
		DoctorID = doctorID;
	}

	public int getPatientID() {
		return PatientID;
	}

	public void setPatientID(int patientID) {
		PatientID = patientID;
	}

	public int getDiagnosisID() {
		return DiagnosisID;
	}

	public void setDiagnosisID(int diagnosisID) {
		DiagnosisID = diagnosisID;
	}

	public String getICDCode() {
		return ICDCode;
	}

	public void setICDCode(String iCDCode) {
		ICDCode = iCDCode;
	}

	public String getDiagnosisName() {
		return DiagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		DiagnosisName = diagnosisName;
	}

	public boolean isIsAllergy() {
		return IsAllergy;
	}

	public void setIsAllergy(boolean isAllergy) {
		IsAllergy = isAllergy;
	}

	public int getCancerType() {
		return CancerType;
	}

	public void setCancerType(int cancerType) {
		CancerType = cancerType;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public Diagnosis(Parcel source) {
		this.VisitID = source.readInt();
		this.ClinicID = source.readInt();
		this.DoctorID = source.readInt();
		this.PatientID = source.readInt();
		this.DiagnosisID = source.readInt();
		this.ICDCode = source.readString();
		this.DiagnosisName = source.readString();
		this.IsAllergy = source.readInt() == 0 ? false : true;
		this.CancerType = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(VisitID);
		dest.writeInt(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeInt(PatientID);
		dest.writeInt(DiagnosisID);
		dest.writeString(ICDCode);
		dest.writeString(DiagnosisName);
		dest.writeInt(IsAllergy ? 1 : 0);
		dest.writeInt(CancerType);
	}
	
	public static final Parcelable.Creator<Diagnosis> CREATOR = new Parcelable.Creator<Diagnosis>() {

		@Override
		public Diagnosis createFromParcel(Parcel source) {
			return new Diagnosis(source);
		}

		@Override
		public Diagnosis[] newArray(int size) {
			return new Diagnosis[size];
		}
	};
}
