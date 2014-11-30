package com.example.tasktwo;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{
	private String partDescription;
	private String partNum;

	public Product (String pd, String pn) {
		this.partDescription = pd;
		this.partNum = pn;
	}
	
	public Product (Parcel source) {
		this.partDescription = source.readString();
		this.partNum = source.readString();
	}
	
	String getPartDescription() {
		return partDescription;
	}
	void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}
	String getPartNum() {
		return partNum;
	}
	void setPartNum(String partNum) {
		this.partNum = partNum;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(partDescription);
		dest.writeString(partNum);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Product createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Product(source);
		}

		@Override
		public Product[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Product[size];
		}
	};
}
