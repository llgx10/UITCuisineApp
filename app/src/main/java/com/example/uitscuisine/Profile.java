package com.example.uitscuisine;

import androidx.annotation.NonNull;

public class Profile {
    private String name;
    private String phone;
    private String address;
    private String birthday;
    private String gender;
    private String weight;
    private String height;
    private String imageURL;

    public Profile(String name,String phone, String address, String birthday, String gender, String weight, String height, String imageURL) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.imageURL= imageURL;
    }

    public Profile(String phone){
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getImageURl() {
        return imageURL;
    }

    public void setImageURl(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
