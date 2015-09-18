package com.toktoktalk.selfanalysis.common;

import android.content.Context;
import android.util.Log;

import com.toktoktalk.selfanalysis.model.UserVo;
import com.toktoktalk.selfanalysis.utils.ComPreference;

/**
 * Created by seogangmin on 2015. 9. 17..
 */
public class Cache {

    public static final UserVo getUser(Context ctx){

        UserVo userVo      = null;
        ComPreference pref = new ComPreference(ctx);
        String userJson = pref.getValue(Const.PREF_SAVED_USER, null).toString();

        Log.d("debug", userJson);

        userVo = (UserVo)GsonConverter.fromJson(userJson, UserVo.class);

        return userVo;
    }
}
