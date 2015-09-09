package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 8. 23..
 */
public class UserVo {
    private String fb_id;
    private String name;


    public UserVo() {

    }

    public UserVo(String fb_id, String name) {
        this.fb_id = fb_id;
        this.name = name;
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
