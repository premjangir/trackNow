package com.bustracker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class BusStop implements Parcelable {


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<BusStop>(){

        @Override
        public BusStop createFromParcel(Parcel parcel) {
            return new BusStop(parcel);
        }

        @Override
        public BusStop[] newArray(int i) {
            return new BusStop[i];
        }
    };


    public String busNo;
    public String stopName;
    public int stopNo;
    public LatLng latLng;


    public BusStop(String busNo, String stopName, int stopNo) {
        this.busNo = busNo;
        this.stopName = stopName;
        this.stopNo = stopNo;
    }


    public BusStop(String busNo, String stopName, int stopNo, LatLng latLng) {
        this.busNo = busNo;
        this.stopName = stopName;
        this.stopNo = stopNo;
        this.latLng = latLng;
    }

    public BusStop(String stopName, int stopNo, LatLng latLng) {
        this.stopName = stopName;
        this.stopNo = stopNo;
        this.latLng = latLng;
    }

    public BusStop(String stopName, int stopNo) {
        this.stopName = stopName;
        this.stopNo = stopNo;
    }


    public BusStop(Parcel parcel) {
        busNo= parcel.readString();
        stopName =parcel.readString();
        stopNo = parcel.readInt();
        Double lat = parcel.readDouble();
        Double lng = parcel.readDouble();

        latLng = new LatLng(lat,lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(busNo);
        parcel.writeString(stopName);
        parcel.writeInt(stopNo);
        parcel.writeDouble(latLng.latitude);
        parcel.writeDouble(latLng.longitude);
    }
}
