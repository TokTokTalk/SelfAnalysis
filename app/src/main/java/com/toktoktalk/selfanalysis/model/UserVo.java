package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 8. 23..
 */
public class UserVo {
    private String fbId;
    private String name;


    public UserVo() {

    }

    public UserVo(String fbId, String name) {
        this.fbId = fbId;
        this.name = name;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
