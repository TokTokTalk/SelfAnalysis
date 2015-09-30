package com.toktoktalk.selfanalysis.common;

import com.toktoktalk.selfanalysis.utils.MemoryStorage;

/**
 * Created by seogangmin on 2015. 8. 31..
 */
public class Const {

    public final static String PREF_SAVED_USER        = "PREF_SAVED_USER";
    public final static String PREF_ACTIVE_KEYWORDS   = "PREF_ACTIVE_KEYWORDS";
    public final static String PREF_SAVED_KEYWORDS   = "PREF_SAVED_KEYWORDS";

    public final static String PREF_ACTIVE_LOCKSCREEN   = "PREF_ACTIVE_LOCKSCREEN";


    public final static String SERVER_DOMAIN = "http://loopyseo-vm.cloudapp.net:1337";
    //public final static String SERVER_DOMAIN = "http://192.168.0.40:1337";    //home
    //public final static String SERVER_DOMAIN   = "http://192.168.1.19:1337"; //cafe


    public final static String RESOURCE_PATH = SERVER_DOMAIN + "/images";

    public final static String DATABASE_NAME   = "toctoktalk-products";
    public final static String COLLECTION_USER = "user";
    public final static String COLLECTION_CATEGORY = "category";
    public final static String COLLECTION_KEYWORD = "keyword";


    public final static String ICON_SAVED_FOLDER = MemoryStorage.getSdCardPath()+"toctoktalk";


}
