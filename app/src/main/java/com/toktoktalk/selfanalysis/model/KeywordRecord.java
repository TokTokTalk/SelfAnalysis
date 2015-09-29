package com.toktoktalk.selfanalysis.model;

/**
 * Created by seogangmin on 2015. 9. 29..
 */
public class KeywordRecord {

    private String _id;
    private int count;
    private String keyword_ref;
    private String record_dt;

    public KeywordRecord() {
    }

    public KeywordRecord(String _id, int count, String keyword_ref, String record_dt) {
        this._id = _id;
        this.count = count;
        this.keyword_ref = keyword_ref;
        this.record_dt = record_dt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getKeyword_ref() {
        return keyword_ref;
    }

    public void setKeyword_ref(String keyword_ref) {
        this.keyword_ref = keyword_ref;
    }

    public String getRecord_dt() {
        return record_dt;
    }

    public void setRecord_dt(String record_dt) {
        this.record_dt = record_dt;
    }
}
