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
    private Map<String, IconVo> keyword_refs;

    public CateItemVo() {
    }

    public CateItemVo(String cate_name, Map<String, IconVo> keyword_refs, String _id, String user_ref) {
        this.cate_name = cate_name;
        this.keyword_refs = keyword_refs;
        this._id = _id;
        this.user_ref = user_ref;
    }

    public void initEmpty(){
        this.cate_name = "";
        this.user_ref = "";
        this.keyword_refs = new HashMap<String, IconVo>();
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

    public Map getKeyword_refs() {
        return keyword_refs;
    }

    public void setKeyword_refs(Map<String, IconVo> keyword_refs) {
        this.keyword_refs = keyword_refs;
    }

    public String getUser_ref() {
        return user_ref;
    }

    public void setUser_ref(String user_ref) {
        this.user_ref = user_ref;
    }
}
