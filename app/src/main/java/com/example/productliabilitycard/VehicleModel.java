package com.example.productliabilitycard;

import android.os.Parcel;
import android.os.Parcelable;

public class VehicleModel implements Parcelable {

    private String cardNumber;
    private String plateNumber;
    private String vehicleName;
    private String vehicleType;
    private String vehicleYear;
    private String vin;
    private String engineNumber;
    private String ownerName;
    private String ownerPhone;
    private String ownerAddress;
    private String coverageType;
    private String coverageLimit;
    private String issueDate;
    private String expiryDate;
    private String status;

    public VehicleModel(String cardNumber, String plateNumber, String vehicleName,
                        String vehicleType, String vehicleYear, String vin,
                        String engineNumber, String ownerName, String ownerPhone,
                        String ownerAddress, String coverageType, String coverageLimit,
                        String issueDate, String expiryDate, String status) {
        this.cardNumber = cardNumber;
        this.plateNumber = plateNumber;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.vehicleYear = vehicleYear;
        this.vin = vin;
        this.engineNumber = engineNumber;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.ownerAddress = ownerAddress;
        this.coverageType = coverageType;
        this.coverageLimit = coverageLimit;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    protected VehicleModel(Parcel in) {
        cardNumber = in.readString();
        plateNumber = in.readString();
        vehicleName = in.readString();
        vehicleType = in.readString();
        vehicleYear = in.readString();
        vin = in.readString();
        engineNumber = in.readString();
        ownerName = in.readString();
        ownerPhone = in.readString();
        ownerAddress = in.readString();
        coverageType = in.readString();
        coverageLimit = in.readString();
        issueDate = in.readString();
        expiryDate = in.readString();
        status = in.readString();
    }

    public static final Creator<VehicleModel> CREATOR = new Creator<VehicleModel>() {
        @Override
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        @Override
        public VehicleModel[] newArray(int size) {
            return new VehicleModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardNumber);
        dest.writeString(plateNumber);
        dest.writeString(vehicleName);
        dest.writeString(vehicleType);
        dest.writeString(vehicleYear);
        dest.writeString(vin);
        dest.writeString(engineNumber);
        dest.writeString(ownerName);
        dest.writeString(ownerPhone);
        dest.writeString(ownerAddress);
        dest.writeString(coverageType);
        dest.writeString(coverageLimit);
        dest.writeString(issueDate);
        dest.writeString(expiryDate);
        dest.writeString(status);
    }

    public String getCardNumber() { return cardNumber; }
    public String getPlateNumber() { return plateNumber; }
    public String getVehicleName() { return vehicleName; }
    public String getVehicleType() { return vehicleType; }
    public String getVehicleYear() { return vehicleYear; }
    public String getVin() { return vin; }
    public String getEngineNumber() { return engineNumber; }
    public String getOwnerName() { return ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public String getOwnerAddress() { return ownerAddress; }
    public String getCoverageType() { return coverageType; }
    public String getCoverageLimit() { return coverageLimit; }
    public String getIssueDate() { return issueDate; }
    public String getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }
}

