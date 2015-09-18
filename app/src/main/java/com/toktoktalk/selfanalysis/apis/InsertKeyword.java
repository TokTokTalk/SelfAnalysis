package com.toktoktalk.selfanalysis.apis;

import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 18..
 */
public class InsertKeyword {
    private Map create_doc;
    private Map find;

    public InsertKeyword() {
    }

    public InsertKeyword(Map create_doc, Map find) {
        this.create_doc = create_doc;
        this.find = find;
    }

    public Map getCreate_doc() {
        return create_doc;
    }

    public void setCreate_doc(Map create_doc) {
        this.create_doc = create_doc;
    }

    public Map getFind() {
        return find;
    }

    public void setFind(Map find) {
        this.find = find;
    }
}
