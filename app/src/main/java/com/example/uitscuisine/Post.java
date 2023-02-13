package com.example.uitscuisine;

import androidx.annotation.NonNull;

public class Post {
    private String recipeName, serves, duration, difficulty,
    category, ingredients, steps, recipeImage, posterPhone, posterName, posterAvatar;
    private int recipeId;

    public Post(String recipeName, String serves, String duration, String difficulty, String category, String ingredients, String steps, String recipeImage, String posterPhone, String posterName, String posterAvatar, int recipeId) {
        this.recipeName = recipeName;
        this.serves = serves;
        this.duration = duration;
        this.difficulty = difficulty;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.recipeImage = recipeImage;
        this.posterPhone = posterPhone;
        this.posterName = posterName;
        this.posterAvatar = posterAvatar;
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getPosterPhone() {
        return posterPhone;
    }

    public void setPosterPhone(String posterPhone) {
        this.posterPhone = posterPhone;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterAvatar() {
        return posterAvatar;
    }

    public void setPosterAvatar(String posterAvatar) {
        this.posterAvatar = posterAvatar;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Posts{" +
                "recipeName='" + recipeName + '\'' +
                ", serves='" + serves + '\'' +
                ", duration='" + duration + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", steps='" + steps + '\'' +
                ", recipeImage='" + recipeImage + '\'' +
                ", posterPhone='" + posterPhone + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", posterName='" + posterName + '\'' +
                ", posterAvatar='" + posterAvatar + '\'' +
                '}';
    }
}
