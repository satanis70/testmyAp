package com.vpiska.adModel;

import android.widget.ImageView;

import java.sql.Timestamp;

public class ModelAd {
    String Adres;
    String More_Details;
    String Name_Ads;
    String Phone;
    String Time;
    String adImage;
    String User;


    public ModelAd() {

    }

    public ModelAd(String Name_Ads, String Adres, String adImage, String More_Details, String Phone, String Time, String User){
        this.Adres = Adres;
        this.More_Details = More_Details;
        this.Name_Ads = Name_Ads;
        this.Phone = Phone;
        this.adImage = adImage;
        this.Time = Time;
        this.User = User;
    }
    
    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        User = User;
    }

    public String getAdres() { return Adres; }

    public void setAdres(String Adres) {
        Adres = Adres;
    }

    public String getMore_Details() {
        return More_Details;
    }

    public void setMore_Details(String More_Details) {
        More_Details = More_Details;
    }

    public String getName_Ads() {
        return Name_Ads;
    }

    public void setName_Ads(String Name_Ads) {
        this.Name_Ads = Name_Ads;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        Phone = Phone;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(ImageView adImage) {
        adImage = adImage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }



}
