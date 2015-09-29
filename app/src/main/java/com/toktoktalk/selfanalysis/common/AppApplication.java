package com.toktoktalk.selfanalysis.common;

import android.app.Application;
import android.content.res.Configuration;

import com.tsengvn.typekit.Typekit;

/**
 * Created by seogangmin on 2015. 9. 12..
 */
public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //set font
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSans-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSans-Bold.ttf"));

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
