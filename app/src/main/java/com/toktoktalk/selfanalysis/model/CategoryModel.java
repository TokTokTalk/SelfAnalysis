package com.toktoktalk.selfanalysis.model;


/**
 * Created by seogangmin on 2015. 9. 10..
 */
public class CategoryModel{
    private String id;
    private String name;
    private String[] keyword_ids;
    private int is_active;

    public CategoryModel() {

    }

    public CategoryModel(String id, int is_active, String[] keyword_ids, String name) {
        this.id = id;
        this.is_active = is_active;
        this.keyword_ids = keyword_ids;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }


    public String[] getKeyword_ids() {
        return keyword_ids;
    }

    public void setKeyword_ids(String[] keyword_ids) {
        this.keyword_ids = keyword_ids;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
