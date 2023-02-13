package com.example.uitscuisine;

import java.io.Serializable;

public class Category_item implements Serializable {
    String categoryName;
    String ID;
    String img;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Category_item(String categoryName, String ID, String img) {
        this.categoryName = categoryName;
        this.ID = ID;
        this.img = img;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Category_item(String categoryName, String ID) {
        this.categoryName = categoryName;
        this.ID = ID;
    }

    public Category_item(String name) {
        this.categoryName=name;
    }

    public String getname() {
        return categoryName;
    }

    public void setTextView(String name) {
        this.categoryName = name;
    }
}
