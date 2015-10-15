package com.mdground.yizhida.bean;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mdground.yizhida.util.DateUtils;

public class Anamnesis implements Parcelable, Comparable<Anamnesis> {

	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;

	@SerializedName("DoctorID")
	@Expose
	private int DoctorID;

	@SerializedName("PatientID")
	@Expose
	private int PatientID;

	@SerializedName("DiagnosisName")
	@Expose
	private String DiagnosisName;

	@SerializedName("VisitTime")
	@Expose
	private Date VisitTime;

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

	public String getDiagnosisName() {
		return DiagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		DiagnosisName = diagnosisName;
	}

	public Date getVisitTime() {
		return VisitTime;
	}

	public void setVisitTime(Date visitTime) {
		VisitTime = visitTime;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.ClinicID);
		dest.writeInt(this.DoctorID);
		dest.writeInt(this.PatientID);
		dest.writeString(DiagnosisName);
		dest.writeLong(VisitTime != null ? VisitTime.getTime() : -1);
	}

	public Anamnesis() {
	}

	protected Anamnesis(Parcel in) {
		this.ClinicID = in.readInt();
		this.DoctorID = in.readInt();
		this.PatientID = in.readInt();
		this.DiagnosisName = in.readString();
		this.VisitTime = new Date(in.readLong());
	}

	public static final Parcelable.Creator<Anamnesis> CREATOR = new Parcelable.Creator<Anamnesis>() {
		public Anamnesis createFromParcel(Parcel source) {
			return new Anamnesis(source);
		}

		public Anamnesis[] newArray(int size) {
			return new Anamnesis[size];
		}
	};

	@Override
	public int compareTo(Anamnesis another) {
		return (int) (another.getVisitTime().getTime() - this.getVisitTime().getTime());
	}
}
