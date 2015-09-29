package com.toktoktalk.selfanalysis.apis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 14..
 */
public class QueryDocs {
    private String database;
    private String collection;
    private Map find;
    private Map sort;
    private int limit;
    private int skip;

    public QueryDocs() {
    }

    public QueryDocs(String database, String collection) {
        this.database = database;
        this.collection = collection;
    }

    public QueryDocs(String database, String collection, Map find, int limit, int skip, Map sort) {
        this.collection = collection;
        this.database = database;
        this.find = find;
        this.limit = limit;
        this.skip = skip;
        this.sort = sort;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Map getFind() {
        return find;
    }

    public void putFind(Object key, Object value) {
        if(this.find == null){
            this.find = new HashMap();
        }
        this.find.put(key, value);
    }

    public void setFind(Map find) {
        this.find = find;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public Map getSort() {
        return sort;
    }

    public void setSort(Map sort) {
        this.sort = sort;
    }

    public void setSort(String sort, int sort_opt) {
        this.sort = new HashMap();
        this.sort.put(sort, sort_opt);
    }
}
