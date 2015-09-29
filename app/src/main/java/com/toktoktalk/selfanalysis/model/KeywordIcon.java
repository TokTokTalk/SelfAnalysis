package com.toktoktalk.selfanalysis.model;

import com.toktoktalk.selfanalysis.common.Const;

/**
 * Created by seogangmin on 2015. 9. 19..
 */
public class KeywordIcon {
    private String _id;
    private String keyword;
    private String ico_file_name;

    public KeywordIcon() {
    }

    public KeywordIcon(String _id, String ico_file_name, String keyword) {
        this._id = _id;
        this.ico_file_name = ico_file_name;
        this.keyword = keyword;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIco_file_path() {
        return Const.ICON_SAVED_FOLDER +"/"+ this.ico_file_name;
    }

    public void setIco_file_path(String ico_file_path) {
        this.ico_file_name = ico_file_path;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
