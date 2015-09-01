package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class IconVo {
    private String id;
    private String keyword;
    private String icoFilePath;


    public IconVo() {

    }

    public IconVo(String id, String keyword, String icoFilePath) {
        this.id = id;
        this.keyword = keyword;
        this.icoFilePath = icoFilePath;
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

    public String getIcoFilePath() {
        return icoFilePath;
    }

    public void setIcoFilePath(String icoFilePath) {
        this.icoFilePath = icoFilePath;
    }
}
