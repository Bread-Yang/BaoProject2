package com.mdground.yizhida.bean;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mdground.yizhida.util.DateUtils;

@Table(name = "Fee")
public class Fee implements Parcelable {

	@Id
	private int id;
	
	@SerializedName("FTID")
	@Expose
	private int FTID;

	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;
	
	@SerializedName("DoctorID")
	@Expose
	private int DoctorID;

	@SerializedName("FeeType")
	@Expose
	private String FeeType;
	
	@SerializedName("TotalFee")
	@Expose
	private int TotalFee;

	public Fee() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFTID() {
		return FTID;
	}

	public void setFTID(int fTID) {
		FTID = fTID;
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

	public String getFeeType() {
		return FeeType;
	}

	public void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public int getTotalFee() {
		return TotalFee;
	}

	public void setTotalFee(int totalFee) {
		TotalFee = totalFee;
	}

	public Fee(Parcel source) {
		this.ClinicID = source.readInt();
		this.DoctorID = source.readInt();
		this.FeeType = source.readString();
		this.FTID = source.readInt();
		this.TotalFee = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ClinicID);
		dest.writeInt(DoctorID);
		dest.writeString(FeeType);
		dest.writeInt(FTID);
		dest.writeInt(TotalFee);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Fee> CREATOR = new Parcelable.Creator<Fee>() {

		@Override
		public Fee createFromParcel(Parcel source) {
			return new Fee(source);
		}

		@Override
		public Fee[] newArray(int size) {
			return new Fee[size];
		}
	};
}
