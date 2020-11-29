package com.azoroapps.calcVault.view;

import android.net.Uri;

public class ImageDetails {
    public String name;
    public Uri uri;
    boolean isSelected=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return isSelected;
    }
}