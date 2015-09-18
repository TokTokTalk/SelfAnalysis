package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 8. 23..
 */
public class UserVo {
    private String _id;
    private String fb_id;
    private String name;


    public UserVo() {

    }

    public UserVo(String fb_id, String name) {
        this.fb_id = fb_id;
        this.name = name;
    }

    public UserVo(String _id, String fb_id, String name) {
        this._id = _id;
        this.fb_id = fb_id;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
