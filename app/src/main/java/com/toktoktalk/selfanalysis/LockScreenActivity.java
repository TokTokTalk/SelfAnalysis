package com.toktoktalk.selfanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by seogangmin on 2015. 8. 9..
 */
public class LockScreenActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
