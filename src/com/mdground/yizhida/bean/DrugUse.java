package com.mdground.yizhida.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DrugUse implements Parcelable {

	private int ClinicID;
	private int DUID;
	private int VisitID;
	private int DoctorID;
	private int PatientID;
	private int DrugID;
	private int GroupNo;
	private String SIG;
	private float Take;  // 单次用量
	private String TakeType;
	private String TakeUnit;
	private String Frequency;
	private int Days;
	private String SaleUnit;
	private int SalePrice;  // 单价	
	private int SaleQuantity;
	private boolean Printed;
	private String Remark;
	private String UpdatedTime;
	private String DrugName;
	private int InventoryCount;

	public DrugUse() {
		this.SIG = "";
		this.Remark = "";
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getDUID() { 
		return DUID;
	}

	public void setDUID(int dUID) {
		DUID = dUID;
	}

	public int getVisitID() {
		return VisitID;
	}

	public void setVisitID(int visitID) {
		VisitID = visitID;
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

	public int getDrugID() {
		return DrugID;
	}

	public void setDrugID(int drugID) {
		DrugID = drugID;
	}

	public int getGroupNo() {
		return GroupNo;
	}

	public void setGroupNo(int groupNo) {
		GroupNo = groupNo;
	}

	public String getSIG() {
		return SIG;
	}

	public void setSIG(String sIG) {
		SIG = sIG;
	}

	public float getTake() {
		return Take;
	}

	public void setTake(float take) {
		Take = take;
	}

	public String getTakeType() {
		return TakeType;
	}

	public void setTakeType(String takeType) {
		TakeType = takeType;
	}

	public String getTakeUnit() {
		return TakeUnit;
	}

	public void setTakeUnit(String takeUnit) {
		TakeUnit = takeUnit;
	}

	public String getFrequency() {
		return Frequency;
	}

	public void setFrequency(String frequency) {
		Frequency = frequency;
	}

	public int getDays() {
		return Days;
	}

	public void setDays(int days) {
		Days = days;
	}

	public String getSaleUnit() {
		return SaleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		SaleUnit = saleUnit;
	}

	public int getSalePrice() {
		return SalePrice;
	}

	public void setSalePrice(int salePrice) {
		SalePrice = salePrice;
	}

	public int getSaleQuantity() {
		return SaleQuantity;
	}

	public void setSaleQuantity(int saleQuantity) {
		SaleQuantity = saleQuantity;
	}

	public boolean getPrinted() {
		return Printed;
	}

	public void setPrinted(boolean printed) {
		Printed = printed;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getUpdatedTime() {
		return UpdatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		UpdatedTime = updatedTime;
	}

	public String getDrugName() {
		return DrugName;
	}

	public void setDrugName(String drugName) {
		DrugName = drugName;
	}

	public int getInventoryCount() {
		return InventoryCount;
	}

	public void setInventoryCount(int inventoryCount) {
		InventoryCount = inventoryCount;
	}

	public DrugUse(Parcel source) {
		this.ClinicID = source.readInt();
		this.DUID = source.readInt();
		this.VisitID = source.readInt();
		this.DoctorID = source.readInt();
		this.PatientID = source.readInt();
		this.DrugID = source.readInt();
		this.GroupNo = source.readInt();
		this.SIG = source.readString();
		this.Take = source.readFloat();
		this.TakeType = source.readString();
		this.TakeUnit = source.readString();
		this.Frequency = source.readString();
		this.Days = source.readInt();
		this.SaleUnit = source.readString();
		this.SalePrice = source.readInt();
		this.SaleQuantity = source.readInt();
		this.Printed = (source.readInt() == 1) ? true : false;  
		this.Remark = source.readString();
		this.UpdatedTime = source.readString();
		this.DrugName = source.readString();
		this.InventoryCount = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ClinicID);
		dest.writeInt(DUID);
		dest.writeInt(VisitID);
		dest.writeInt(DoctorID);
		dest.writeInt(PatientID);
		dest.writeInt(DrugID);
		dest.writeInt(GroupNo);
		dest.writeString(SIG);
		dest.writeFloat(Take);
		dest.writeString(TakeType);
		dest.writeString(TakeUnit);
		dest.writeString(Frequency);
		dest.writeInt(Days);
		dest.writeString(SaleUnit);
		dest.writeInt(SalePrice);
		dest.writeInt(SaleQuantity);
		dest.writeInt(Printed ? 1 : 0);
		dest.writeString(Remark);
		dest.writeString(UpdatedTime);
		dest.writeString(DrugName);
		dest.writeInt(InventoryCount);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<DrugUse> CREATOR = new Parcelable.Creator<DrugUse>() {

		@Override
		public DrugUse createFromParcel(Parcel source) {
			return new DrugUse(source);
		}

		@Override
		public DrugUse[] newArray(int size) {
			return new DrugUse[size];
		}
	};
}
