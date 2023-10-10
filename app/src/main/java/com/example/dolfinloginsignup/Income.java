package com.example.dolfinloginsignup;

public class Income {

    private String name;
    private String amount;
    private String date;
    private String imageURL;

    public Income(){

    }

    public Income(String name, String amount,String date, String imageURL) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
