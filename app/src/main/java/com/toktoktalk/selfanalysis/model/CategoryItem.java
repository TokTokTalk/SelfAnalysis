package com.toktoktalk.selfanalysis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 19..
 */
public class CategoryItem {
    private String _id;
    private Map<String, KeywordIcon> keywords;

    public CategoryItem() {
    }

    public CategoryItem(String _id, Map<String, KeywordIcon> keywords) {
        this._id = _id;
        this.keywords = keywords;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Map<String, KeywordIcon> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, KeywordIcon> keywords) {
        this.keywords = keywords;
    }

    public void putKeywords(String key, KeywordIcon keywordIcon) {
        if(this.keywords == null){
            this.keywords = new HashMap<String, KeywordIcon>();
        }
        this.keywords.put(key, keywordIcon);
    }
}
