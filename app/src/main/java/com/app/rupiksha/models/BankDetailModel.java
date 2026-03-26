package com.app.rupiksha.models;

import android.net.Uri;

public class BankDetailModel {


    private int id;

    private String BankName;

    private String AccountNumber;

    private Uri imageUri;

    private String imageName;

    private String bankIfscCode;

    private String bankType;

    private boolean isOtherDocument;

    public BankDetailModel(int id, String BankName, String AccountNumber, Uri imageUri, String imageName, String bankIfscCode, String bankType, boolean isOtherDocument){

        setId(id);
        setBankName(BankName);
        setAccountNumber(AccountNumber);
        setImageUri(imageUri);
        setImageName(imageName);
        setBankIfscCode(bankIfscCode);
        setBankType(bankType);
        setOtherDocument(isOtherDocument);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public boolean isOtherDocument() {
        return isOtherDocument;
    }

    public void setOtherDocument(boolean otherDocument) {
        isOtherDocument = otherDocument;
    }
}
