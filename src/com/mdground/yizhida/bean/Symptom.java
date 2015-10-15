package com.mdground.yizhida.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 症状信息
 * 
 * @author Vincent
 * 
 */
@Table(name = "Symptom")
public class Symptom implements Parcelable {

	@Id(column = "id")
	private int id;

	@SerializedName("ClinicID")
	@Expose
	private Long ClinicID;
	@SerializedName("TemplateID")
	@Expose
	private Long TemplateID;
	@SerializedName("TemplateName")
	@Expose
	private String TemplateName;
	@SerializedName("UpdatedTime")
	@Expose
	private String UpdatedTime;

	// listview中标记是否被选中
	private boolean isSelected = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The ClinicID
	 */
	public Long getClinicID() {
		return ClinicID;
	}

	/**
	 * 
	 * @param ClinicID
	 *            The ClinicID
	 */
	public void setClinicID(Long ClinicID) {
		this.ClinicID = ClinicID;
	}

	/**
	 * 
	 * @return The TemplateID
	 */
	public Long getTemplateID() {
		return TemplateID;
	}

	/**
	 * 
	 * @param TemplateID
	 *            The TemplateID
	 */
	public void setTemplateID(Long TemplateID) {
		this.TemplateID = TemplateID;
	}

	/**
	 * 
	 * @return The TemplateName
	 */
	public String getTemplateName() {
		return TemplateName;
	}

	/**
	 * 
	 * @param TemplateName
	 *            The TemplateName
	 */
	public void setTemplateName(String TemplateName) {
		this.TemplateName = TemplateName;
	}

	/**
	 * 
	 * @return The UpdatedTime
	 */
	public String getUpdatedTime() {
		return UpdatedTime;
	}

	/**
	 * 
	 * @param UpdatedTime
	 *            The UpdatedTime
	 */
	public void setUpdatedTime(String UpdatedTime) {
		this.UpdatedTime = UpdatedTime;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "Symptom [ClinicID=" + ClinicID + ", TemplateID=" + TemplateID + ", TemplateName=" + TemplateName + ", UpdatedTime=" + UpdatedTime
				+ ", isSelected=" + isSelected + "]";
	}

	public Symptom() {
		// TODO Auto-generated constructor stub
	}

	public Symptom(Parcel source) {
		this.ClinicID = source.readLong();
		this.TemplateID = source.readLong();
		this.TemplateName = source.readString();
		this.UpdatedTime = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ClinicID);
		dest.writeLong(TemplateID);
		dest.writeString(TemplateName);
		dest.writeString(UpdatedTime);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Symptom> CREATOR = new Parcelable.Creator<Symptom>() {

		@Override
		public Symptom createFromParcel(Parcel source) {
			return new Symptom(source);
		}

		@Override
		public Symptom[] newArray(int size) {
			return new Symptom[size];
		}
	};

}
