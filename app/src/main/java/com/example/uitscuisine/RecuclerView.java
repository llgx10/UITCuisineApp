package com.example.uitscuisine;

public class RecuclerView {
    private String Rimage;
    private String recipeID;
    private String phone;
    public RecuclerView(String rimage, String recipeID) {
        Rimage = rimage;
        this.recipeID = recipeID;
    }

    public RecuclerView(String rimage, String recipeID, String phone) {
        Rimage = rimage;
        this.recipeID = recipeID;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public RecuclerView() {
    }
    public String getRimage() {
        return Rimage;
    }

    @Override
    public String toString() {
        return "RecuclerView{" +
                "Rimage='" + Rimage + '\'' +
                '}';
    }

    public void setRimage(String rimage) {
        this.Rimage = rimage;
    }
}
