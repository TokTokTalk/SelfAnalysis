package com.toktoktalk.selfanalysis.model;

import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 3..
 */
public class CateItemVo {
    private String id;
    private String cateName;
    private List<IconVo> iconList;

    public CateItemVo() {
    }

    public CateItemVo(String id, String cateName, List<IconVo> iconList) {
        this.id = id;
        this.cateName = cateName;
        this.iconList = iconList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public List<IconVo> getIconList() {
        return iconList;
    }

    public void setIconList(List<IconVo> iconList) {
        this.iconList = iconList;
    }
}
