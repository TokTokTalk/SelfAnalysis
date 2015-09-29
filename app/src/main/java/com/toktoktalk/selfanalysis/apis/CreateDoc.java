package com.toktoktalk.selfanalysis.apis;

/**
 * Created by seogangmin on 2015. 9. 9..
 */
public class CreateDoc {
    private String database;
    private String collection;
    private Object entity;

    public CreateDoc() {
    }

    public CreateDoc(String database, String collection, Object entity) {
        this.collection = collection;
        this.database = database;
        this.entity = entity;
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

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }
}
