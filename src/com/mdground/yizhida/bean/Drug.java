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

@Table(name = "Drug")
public class Drug implements Parcelable {

	@Id
	private int id;

	@SerializedName("DrugID")
	@Expose
	private int DrugID;

	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;

	@SerializedName("DrugName")
	@Expose
	private String DrugName;

	@SerializedName("Dosage")
	@Expose
	private float Dosage;

	@SerializedName("DosageSingle")
	@Expose
	private float DosageSingle;

	@SerializedName("DosageUnit")
	@Expose
	private String DosageUnit;

	@SerializedName("Frequency")
	@Expose
	private String Frequency;

	@SerializedName("Specification")
	@Expose
	private String Specification;

	@SerializedName("InventoryQuantity")
	@Expose
	private int InventoryQuantity;

	@SerializedName("InventoryUnit")
	@Expose
	private String InventoryUnit;

	@SerializedName("InvoiceItem")
	@Expose
	private String InvoiceItem;

	@SerializedName("OVPirce")
	@Expose
	private int OVPirce;

	@SerializedName("OVUnit")
	@Expose
	private String OVUnit;

	@SerializedName("SalePirce")
	@Expose
	private int SalePirce;

	@SerializedName("SaleUnit")
	@Expose
	private String SaleUnit;

	@SerializedName("TakeType")
	@Expose
	private String TakeType;

	@SerializedName("TypeID")
	@Expose
	private int TypeID;

	@SerializedName("UnitConvert")
	@Expose
	private int UnitConvert;

	@SerializedName("UnitGeneric")
	@Expose
	private String UnitGeneric;

	@SerializedName("UnitSmall")
	@Expose
	private String UnitSmall;

	public Drug() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDrugID() {
		return DrugID;
	}

	public void setDrugID(int drugID) {
		DrugID = drugID;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public String getDrugName() {
		return DrugName;
	}

	public void setDrugName(String drugName) {
		DrugName = drugName;
	}

	public float getDosage() {
		return Dosage;
	}

	public void setDosage(float dosage) {
		Dosage = dosage;
	}

	public float getDosageSingle() {
		return DosageSingle;
	}

	public void setDosageSingle(float dosageSingle) {
		DosageSingle = dosageSingle;
	}

	public String getDosageUnit() {
		return DosageUnit;
	}

	public void setDosageUnit(String dosageUnit) {
		DosageUnit = dosageUnit;
	}

	public String getFrequency() {
		return Frequency;
	}

	public void setFrequency(String frequency) {
		Frequency = frequency;
	}

	public String getSpecification() {
		return Specification;
	}

	public void setSpecification(String specification) {
		Specification = specification;
	}

	public int getInventoryQuantity() {
		return InventoryQuantity;
	}

	public void setInventoryQuantity(int inventoryQuantity) {
		InventoryQuantity = inventoryQuantity;
	}

	public String getInventoryUnit() {
		return InventoryUnit;
	}

	public void setInventoryUnit(String inventoryUnit) {
		InventoryUnit = inventoryUnit;
	}

	public String getInvoiceItem() {
		return InvoiceItem;
	}

	public void setInvoiceItem(String invoiceItem) {
		InvoiceItem = invoiceItem;
	}

	public int getOVPirce() {
		return OVPirce;
	}

	public void setOVPirce(int oVPirce) {
		OVPirce = oVPirce;
	}

	public String getOVUnit() {
		return OVUnit;
	}

	public void setOVUnit(String oVUnit) {
		OVUnit = oVUnit;
	}

	public int getSalePirce() {
		return SalePirce;
	}

	public void setSalePirce(int salePirce) {
		SalePirce = salePirce;
	}

	public String getSaleUnit() {
		return SaleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		SaleUnit = saleUnit;
	}

	public String getTakeType() {
		return TakeType;
	}

	public void setTakeType(String takeType) {
		TakeType = takeType;
	}

	public int getTypeID() {
		return TypeID;
	}

	public void setTypeID(int typeID) {
		TypeID = typeID;
	}

	public int getUnitConvert() {
		return UnitConvert;
	}

	public void setUnitConvert(int unitConvert) {
		UnitConvert = unitConvert;
	}

	public String getUnitGeneric() {
		return UnitGeneric;
	}

	public void setUnitGeneric(String unitGeneric) {
		UnitGeneric = unitGeneric;
	}

	public String getUnitSmall() {
		return UnitSmall;
	}

	public void setUnitSmall(String unitSmall) {
		UnitSmall = unitSmall;
	}

	public Drug(Parcel source) {
		this.DrugID = source.readInt();
		this.ClinicID = source.readInt();
		this.DrugName = source.readString();
		this.Dosage = source.readFloat();
		this.DosageSingle = source.readFloat();
		this.DosageUnit = source.readString();
		this.Frequency = source.readString();
		this.InventoryQuantity = source.readInt();
		this.InventoryUnit = source.readString();
		this.InvoiceItem = source.readString();
		this.OVPirce = source.readInt();
		this.OVUnit = source.readString();
		this.SalePirce = source.readInt();
		this.SaleUnit = source.readString();
		this.Specification = source.readString();
		this.TakeType = source.readString();
		this.TypeID = source.readInt();
		this.UnitConvert = source.readInt();
		this.UnitGeneric = source.readString();
		this.UnitSmall = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(DrugID);
		dest.writeInt(ClinicID);
		dest.writeString(DrugName);
		dest.writeFloat(Dosage);
		dest.writeFloat(DosageSingle);
		dest.writeString(DosageUnit);
		dest.writeString(Frequency);
		dest.writeInt(InventoryQuantity);
		dest.writeString(InventoryUnit);
		dest.writeString(InvoiceItem);
		dest.writeInt(OVPirce);
		dest.writeString(OVUnit);
		dest.writeInt(SalePirce);
		dest.writeString(SaleUnit);
		dest.writeString(Specification);
		dest.writeString(TakeType);
		dest.writeInt(TypeID);
		dest.writeInt(UnitConvert);
		dest.writeString(UnitGeneric);
		dest.writeString(UnitSmall);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Drug> CREATOR = new Parcelable.Creator<Drug>() {

		@Override
		public Drug createFromParcel(Parcel source) {
			return new Drug(source);
		}

		@Override
		public Drug[] newArray(int size) {
			return new Drug[size];
		}
	};
}
