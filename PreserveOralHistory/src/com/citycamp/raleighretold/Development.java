package com.citycamp.raleighretold;

import android.os.Parcel;
import android.os.Parcelable;

public class Development implements Parcelable{
	private String id;
	private String caseNum;
	private String caseName;
	private String caseYear;
	private double latitude;
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	private double longitude;
	
	public Development (Parcel in) {
        readFromParcel(in);
    }
	
	public static final Parcelable.Creator<Development> CREATOR = new Parcelable.Creator<Development>() {
        public Development createFromParcel(Parcel in) {
            return new Development (in);
        }

        public Development [] newArray(int size) {
            return new Development [size];
        }
    };


	public Development() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getCaseYear() {
		return caseYear;
	}

	public void setCaseYear(String caseYear) {
		this.caseYear = caseYear;
	}

	public void setLat(double double1) {
		this.latitude = double1;		
	}

	public void setLong(double double1) {
		this.longitude = double1;		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(caseName);
		dest.writeString(caseNum);
		dest.writeString(caseYear);
		dest.writeDouble(longitude);
		dest.writeDouble(latitude);
		// TODO Auto-generated method stub
		
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readString();
		caseName = in.readString();
		caseNum = in.readString();
		caseYear = in.readString();
		longitude = in.readDouble();
		latitude = in.readDouble();
	}
	
 }