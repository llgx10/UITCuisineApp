package com.example.uitscuisine;

public class AccountSetting {
    private String settingName;
    private int settingIcon;

    public AccountSetting(String settingName, int settingIcon) {
        this.settingName = settingName;
        this.settingIcon = settingIcon;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public int getSettingIcon() {
        return settingIcon;
    }

    public void setSettingIcon(int settingIcon) {
        this.settingIcon = settingIcon;
    }
}
