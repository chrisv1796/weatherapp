
package com.example.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Condition__ {

    @SerializedName("icon")
    @Expose
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
