package com.kid.retro.com.tracerttask.Model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by jaimin on 7/12/15.
 */
public class ItemDetailsModel implements Serializable {

    private String strStatus = "";
    private String strItemId = "";
    private String strBoxId = "";
    private String strItemName = "";
    private String strItemDescription = "";
    private String strItemTags = "";
    private int strItemImage;

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrItemId() {
        return strItemId;
    }

    public void setStrItemId(String strItemId) {
        this.strItemId = strItemId;
    }

    public String getStrBoxId() {
        return strBoxId;
    }

    public void setStrBoxId(String strBoxId) {
        this.strBoxId = strBoxId;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrItemDescription() {
        return strItemDescription;
    }

    public void setStrItemDescription(String strItemDescription) {
        this.strItemDescription = strItemDescription;
    }

    public String getStrItemTags() {
        return strItemTags;
    }

    public void setStrItemTags(String strItemTags) {
        this.strItemTags = strItemTags;
    }

    public int getStrItemImage() {
        return strItemImage;
    }

    public void setStrItemImage(int strItemImage) {
        this.strItemImage = strItemImage;
    }
}
