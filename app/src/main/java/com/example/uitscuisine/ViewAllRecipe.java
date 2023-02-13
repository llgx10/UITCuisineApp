package com.example.uitscuisine;

import android.net.Uri;

public class ViewAllRecipe {
    private String neededTime, level, NameRecipe, posterName;
    private String recipeId,category;
    Uri post;
    Uri image;
    private String phone;
    public String getNeededTime() {
        return neededTime;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Uri getPost() {
        return post;
    }

    public void setPost(Uri post) {
        this.post = post;
    }

    public void setNeededTime(String neededTime) {
        this.neededTime = neededTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNameRecipe() {
        return NameRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        NameRecipe = nameRecipe;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category;
    }

    public ViewAllRecipe(String neededTime,
                         String level,
                         String nameRecipe,
                         String posterName,
                         String recipeId,
                         String category,
                         Uri post,
                         Uri image) {
        this.neededTime = neededTime;
        this.level = level;
        NameRecipe = nameRecipe;
        this.posterName = posterName;
        this.recipeId = recipeId;
        this.category = category;
        this.post = post;
        this.image = image;
    }

    public ViewAllRecipe(String neededTime, String level,
                         String nameRecipe, String posterName,
                         String recipeId, String category,
                         Uri post, Uri image, String phone) {
        this.neededTime = neededTime;
        this.level = level;
        NameRecipe = nameRecipe;
        this.posterName = posterName;
        this.recipeId = recipeId;
        this.category = category;
        this.post = post;
        this.image = image;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ViewAllRecipe{" +
                "neededTime='" + neededTime + '\'' +
                ", level='" + level + '\'' +
                ", NameRecipe='" + NameRecipe + '\'' +
                ", posterName='" + posterName + '\'' +
                ", image='" + image + '\'' +
                ", post='" + post + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", category='" + category + '\'' +
                ", post=" + post +
                '}';
    }
}
