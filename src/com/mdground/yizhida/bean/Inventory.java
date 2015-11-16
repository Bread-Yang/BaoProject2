package com.mdground.yizhida.bean;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Inventory implements Parcelable {

	private int InventoryID;
	private int ClinicID;
	private int DrugID;
	private String ProductionBatch;
	private Date ProductionDate;  
	private Date ExpiredDate;  
	private String Provider;
	private String WarehouseNo;
	private String WarehouseUnit;
	private int QuantityWarehouse;
	private String UnitSmall;
	private int QuantitySmall;  // 单价	
	private int UnitConvert;
	private int QuantityUse;
	private int WarehousePrice;
	private int SalePrice;
	private int WarehouseStatus;
	private Date WarehouseDate;
	private int WarehouseUpdater;
	private Date WarehouseUpdatedTime;
	
	public Inventory() {
		
	}

	public int getInventoryID() {
		return InventoryID;
	}

	public void setInventoryID(int inventoryID) {
		InventoryID = inventoryID;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getDrugID() {
		return DrugID;
	}

	public void setDrugID(int drugID) {
		DrugID = drugID;
	}

	public String getProductionBatch() {
		return ProductionBatch;
	}

	public void setProductionBatch(String productionBatch) {
		ProductionBatch = productionBatch;
	}

	public Date getProductionDate() {
		return ProductionDate;
	}

	public void setProductionDate(Date productionDate) {
		ProductionDate = productionDate;
	}

	public Date getExpiredDate() {
		return ExpiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		ExpiredDate = expiredDate;
	}

	public String getProvider() {
		return Provider;
	}

	public void setProvider(String provider) {
		Provider = provider;
	}

	public String getWarehouseNo() {
		return WarehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		WarehouseNo = warehouseNo;
	}

	public String getWarehouseUnit() {
		return WarehouseUnit;
	}

	public void setWarehouseUnit(String warehouseUnit) {
		WarehouseUnit = warehouseUnit;
	}

	public int getQuantityWarehouse() {
		return QuantityWarehouse;
	}

	public void setQuantityWarehouse(int quantityWarehouse) {
		QuantityWarehouse = quantityWarehouse;
	}

	public String getUnitSmall() {
		return UnitSmall;
	}

	public void setUnitSmall(String unitSmall) {
		UnitSmall = unitSmall;
	}

	public int getQuantitySmall() {
		return QuantitySmall;
	}

	public void setQuantitySmall(int quantitySmall) {
		QuantitySmall = quantitySmall;
	}

	public int getUnitConvert() {
		return UnitConvert;
	}

	public void setUnitConvert(int unitConvert) {
		UnitConvert = unitConvert;
	}

	public int getQuantityUse() {
		return QuantityUse;
	}

	public void setQuantityUse(int quantityUse) {
		QuantityUse = quantityUse;
	}

	public int getWarehousePrice() {
		return WarehousePrice;
	}

	public void setWarehousePrice(int warehousePrice) {
		WarehousePrice = warehousePrice;
	}

	public int getSalePrice() {
		return SalePrice;
	}

	public void setSalePrice(int salePrice) {
		SalePrice = salePrice;
	}

	public int getWarehouseStatus() {
		return WarehouseStatus;
	}

	public void setWarehouseStatus(int warehouseStatus) {
		WarehouseStatus = warehouseStatus;
	}

	public Date getWarehouseDate() {
		return WarehouseDate;
	}

	public void setWarehouseDate(Date warehouseDate) {
		WarehouseDate = warehouseDate;
	}

	public int getWarehouseUpdater() {
		return WarehouseUpdater;
	}

	public void setWarehouseUpdater(int warehouseUpdater) {
		WarehouseUpdater = warehouseUpdater;
	}

	public Date getWarehouseUpdatedTime() {
		return WarehouseUpdatedTime;
	}

	public void setWarehouseUpdatedTime(Date warehouseUpdatedTime) {
		WarehouseUpdatedTime = warehouseUpdatedTime;
	}

	public Inventory(Parcel source) {
		InventoryID = source.readInt();
		ClinicID = source.readInt();
		DrugID = source.readInt();
		ProductionBatch = source.readString();
		ProductionDate = 
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

	public static final Parcelable.Creator<Inventory> CREATOR = new Parcelable.Creator<Inventory>() {

		@Override
		public Inventory createFromParcel(Parcel source) {
			return new Inventory(source);
		}

		@Override
		public Inventory[] newArray(int size) {
			return new Inventory[size];
		}
	};
}
