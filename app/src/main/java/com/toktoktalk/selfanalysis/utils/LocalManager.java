package com.toktoktalk.selfanalysis.utils;

import android.content.Context;

import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.model.KeywordIcon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by seogangmin on 2015. 9. 20..
 */
public class LocalManager {

    public static final String KEYWORDS_KEY = Const.PREF_ACTIVE_KEYWORDS;

    public static Map loadKeyword(Context ctx, String keywordId){
        ComPreference pref  = new ComPreference(ctx);
        String keywords     = pref.getValue(Const.PREF_SAVED_KEYWORDS, null);
        Map keyword = null;
        JSONObject jsonObj  = null;
        try {
            jsonObj = new JSONObject(keywords);
            String keywordJson = jsonObj.getString(keywordId);
            keyword = (Map)GsonConverter.fromJson(keywordJson, Map.class);
        } catch (JSONException e) {
            return null;
        }

        return keyword;
    }


    public static void saveKeyword(Context ctx, Map map){
        ComPreference pref  = new ComPreference(ctx);
        String keywords     = pref.getValue(Const.PREF_SAVED_KEYWORDS, null);
        JSONObject keywordsJsonObj = null;
        try {
            if(keywords != null){
                keywordsJsonObj = new JSONObject(keywords);
            }else{
                keywordsJsonObj = new JSONObject();
            }
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                keywordsJsonObj.put(key, map.get(key));
            }
            pref.put(Const.PREF_SAVED_KEYWORDS, keywordsJsonObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void removeKeyword(Context ctx, String keyId){

        ComPreference pref     = new ComPreference(ctx);
        String keywords        = pref.getValue(Const.PREF_SAVED_KEYWORDS, null);
        JSONObject keywordsJsonObj = null;
        try {
            keywordsJsonObj = new JSONObject(keywords);
            if(keywordsJsonObj != null){
               keywordsJsonObj.remove(keyId);
            }
            pref.put(Const.PREF_SAVED_KEYWORDS, keywordsJsonObj.toString());
        } catch (JSONException e) {
            Logging.e("exception", e.getMessage());
        }
    }



}
