package com.app.rupiksha.models;

import android.net.Uri;

public class TaxFormDetailModel {


    private int id;

    private String title;

    private Uri imageUri;

    private String imageName;

    private String amount;

    private boolean isOtherDocument;

    public TaxFormDetailModel(int id, String title, Uri imageUri, String imageName, String amount, boolean isOtherDocument){

        setId(id);
        setTitle(title);
        setImageUri(imageUri);
        setImageName(imageName);
        setAmount(amount);
        setOtherDocument(isOtherDocument);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isOtherDocument() {
        return isOtherDocument;
    }

    public void setOtherDocument(boolean otherDocument) {
        isOtherDocument = otherDocument;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
