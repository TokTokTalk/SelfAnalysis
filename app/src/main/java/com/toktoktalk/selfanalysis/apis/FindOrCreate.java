package com.toktoktalk.selfanalysis.apis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 29..
 */
public class FindOrCreate {
    private String collection;
    private Map find;
    private Map create_doc;

    public FindOrCreate() {
    }

    public FindOrCreate(String collection, Map create_doc, Map find) {
        this.collection = collection;
        this.create_doc = create_doc;
        this.find = find;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Map getCreate_doc() {
        return create_doc;
    }

    public void setCreate_doc(Map create_doc) {
        this.create_doc = create_doc;
    }

    public void putFind(Object key, Object value){
        if(this.find == null){
            this.find = new HashMap();
        }
        this.find.put(key, value);
    }

    public void putCreate_doc(Object key, Object value){
        if(this.create_doc == null){
            this.create_doc = new HashMap();
        }
        this.create_doc.put(key, value);
    }

    public Map getFind() {
        return find;
    }

    public void setFind(Map find) {
        this.find = find;
    }
}
