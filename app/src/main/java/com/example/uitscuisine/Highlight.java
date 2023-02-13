package com.example.uitscuisine;

public class Highlight {
    private String Hname;
    private String image;
    private String recipeId;
    private String phone;
    public Highlight(String hname, String image, String recipeId) {
        Hname = hname;
        this.image = image;
        this.recipeId = recipeId;
    }

    public Highlight() {

    }

    public Highlight(String hname, String image, String recipeId, String phone) {
        Hname = hname;
        this.image = image;
        this.recipeId = recipeId;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getHname() {
        return Hname;
    }

    public void setHname(String hname) {
        Hname = hname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
