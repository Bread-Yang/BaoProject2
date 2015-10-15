package com.mdground.yizhida.bean;

import net.tsz.afinal.annotation.sqlite.Id;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {
	@SerializedName("EnglishName")
	@Expose
	private String EnglishName;
	@SerializedName("IsCity")
	@Expose
	private Boolean IsCity;
	@SerializedName("IsHot")
	@Expose
	private Boolean IsHot;
	@SerializedName("IsValid")
	@Expose
	private Boolean IsValid;
	@SerializedName("LocationCode")
	@Expose
	private long LocationCode;
	@SerializedName("LocationID")
	@Expose
	@Id
	private long LocationID;
	@SerializedName("LocationName")
	@Expose
	private String LocationName;
	@SerializedName("LocationX")
	@Expose
	private float LocationX;
	@SerializedName("LocationY")
	@Expose
	private float LocationY;
	@SerializedName("ParentID")
	@Expose
	private long ParentID;

	/**
	 * 
	 * @return The EnglishName
	 */
	public String getEnglishName() {
		return EnglishName;
	}

	/**
	 * 
	 * @param EnglishName
	 *            The EnglishName
	 */
	public void setEnglishName(String EnglishName) {
		this.EnglishName = EnglishName;
	}

	/**
	 * 
	 * @return The IsCity
	 */
	public Boolean getIsCity() {
		return IsCity;
	}

	/**
	 * 
	 * @param IsCity
	 *            The IsCity
	 */
	public void setIsCity(Boolean IsCity) {
		this.IsCity = IsCity;
	}

	/**
	 * 
	 * @return The IsHot
	 */
	public Boolean getIsHot() {
		return IsHot;
	}

	/**
	 * 
	 * @param IsHot
	 *            The IsHot
	 */
	public void setIsHot(Boolean IsHot) {
		this.IsHot = IsHot;
	}

	/**
	 * 
	 * @return The IsValid
	 */
	public Boolean getIsValid() {
		return IsValid;
	}

	/**
	 * 
	 * @param IsValid
	 *            The IsValid
	 */
	public void setIsValid(Boolean IsValid) {
		this.IsValid = IsValid;
	}

	/**
	 * 
	 * @return The LocationCode
	 */
	public long getLocationCode() {
		return LocationCode;
	}

	/**
	 * 
	 * @param LocationCode
	 *            The LocationCode
	 */
	public void setLocationCode(long LocationCode) {
		this.LocationCode = LocationCode;
	}

	/**
	 * 
	 * @return The LocationID
	 */
	public long getLocationID() {
		return LocationID;
	}

	/**
	 * 
	 * @param LocationID
	 *            The LocationID
	 */
	public void setLocationID(long LocationID) {
		this.LocationID = LocationID;
	}

	/**
	 * 
	 * @return The LocationName
	 */
	public String getLocationName() {
		return LocationName;
	}

	/**
	 * 
	 * @param LocationName
	 *            The LocationName
	 */
	public void setLocationName(String LocationName) {
		this.LocationName = LocationName;
	}

	/**
	 * 
	 * @return The LocationX
	 */
	public float getLocationX() {
		return LocationX;
	}

	/**
	 * 
	 * @param LocationX
	 *            The LocationX
	 */
	public void setLocationX(float LocationX) {
		this.LocationX = LocationX;
	}

	/**
	 * 
	 * @return The LocationY
	 */
	public float getLocationY() {
		return LocationY;
	}

	/**
	 * 
	 * @param LocationY
	 *            The LocationY
	 */
	public void setLocationY(float LocationY) {
		this.LocationY = LocationY;
	}

	/**
	 * 
	 * @return The ParentID
	 */
	public long getParentID() {
		return ParentID;
	}

	/**
	 * 
	 * @param ParentID
	 *            The ParentID
	 */
	public void setParentID(long ParentID) {
		this.ParentID = ParentID;
	}

}
