package com.toktoktalk.selfanalysis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 3..
 */
public class CateItemVo {
    private String _id;
    private String cate_name;
    private String user_ref;
    private String start_dt;


    public CateItemVo() {
    }

    public CateItemVo(String cate_name, String _id, String user_ref, String start_dt) {
        this.cate_name = cate_name;
        this._id = _id;
        this.user_ref = user_ref;
        this.start_dt = start_dt;

    }

    public void initEmpty(){
        this.cate_name = "";
        this.user_ref = "";
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }


    public String getUser_ref() {
        return user_ref;
    }

    public void setUser_ref(String user_ref) {
        this.user_ref = user_ref;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }
}
