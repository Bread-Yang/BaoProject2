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

@Table(name = "DrugCategory")
public class DrugCategory implements Parcelable {

	@Id
	private int id;
	
	@SerializedName("ClinicID")
	@Expose
	private int ClinicID;
	
	@SerializedName("TypeID")
	@Expose
	private int TypeID;

	@SerializedName("TypeName")
	@Expose
	private String TypeName;
	
	@SerializedName("Description")
	@Expose
	private String Description;
	
	public DrugCategory() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClinicID() {
		return ClinicID;
	}

	public void setClinicID(int clinicID) {
		ClinicID = clinicID;
	}

	public int getTypeID() {
		return TypeID;
	}

	public void setTypeID(int typeID) {
		TypeID = typeID;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public DrugCategory(Parcel source) {
		this.ClinicID = source.readInt();
		this.Description = source.readString();
		this.TypeID = source.readInt();
		this.TypeName = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ClinicID);
		dest.writeString(Description);
		dest.writeInt(TypeID);
		dest.writeString(TypeName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<DrugCategory> CREATOR = new Parcelable.Creator<DrugCategory>() {

		@Override
		public DrugCategory createFromParcel(Parcel source) {
			return new DrugCategory(source);
		}

		@Override
		public DrugCategory[] newArray(int size) {
			return new DrugCategory[size];
		}
	};
}
