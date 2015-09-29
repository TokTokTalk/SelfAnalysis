package com.toktoktalk.selfanalysis.common;

import android.app.Activity;
import android.content.Context;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by seogangmin on 2015. 9. 12..
 */
public class BaseActivity extends Activity{

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }
}
