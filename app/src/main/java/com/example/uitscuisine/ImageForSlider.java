package com.example.uitscuisine;

public class ImageForSlider {
    private int resourceID;
    private String sliderText;

    public ImageForSlider(int resourceID, String sliderText) {
        this.resourceID = resourceID;
        this.sliderText = sliderText;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getSliderText() {
        return sliderText;
    }

    public void setSliderText(String sliderText) {
        this.sliderText = sliderText;
    }
}
