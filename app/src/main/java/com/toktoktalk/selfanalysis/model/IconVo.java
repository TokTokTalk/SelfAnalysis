package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class IconVo {
    private String _id;
    private String keyword;
    private String icoFilePath;


    public IconVo() {

    }

    public IconVo(String icoFilePath, String keyword) {
        this.icoFilePath = icoFilePath;
        this.keyword = keyword;
    }

    public IconVo(String _id, String keyword, String icoFilePath) {
        this._id = _id;
        this.icoFilePath = icoFilePath;
        this.keyword = keyword;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIcoFilePath() {
        return icoFilePath;
    }

    public void setIcoFilePath(String icoFilePath) {
        this.icoFilePath = icoFilePath;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
