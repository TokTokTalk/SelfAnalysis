package com.toktoktalk.selfanalysis.model;

import android.graphics.Bitmap;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class IconsVo {
    private String id;
    private String keyword;
    private Bitmap icon;

    public IconsVo(){

    }

    public IconsVo(String id, String keyword, Bitmap icon) {
        this.id = id;
        this.keyword = keyword;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
