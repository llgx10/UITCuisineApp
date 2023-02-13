package com.example.uitscuisine;

public class Like {
    String recipeID;
    String phoneID;

    public String getRecipeID() {
        return recipeID;
    }

    public Like(String recipeID, String phoneID) {
        this.recipeID = recipeID;
        this.phoneID = phoneID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(String phoneID) {
        this.phoneID = phoneID;
    }

    @Override
    public String toString() {
        return "Like{" +
                "recipeID='" + recipeID + '\'' +
                ", phoneID='" + phoneID + '\'' +
                '}';
    }
}
