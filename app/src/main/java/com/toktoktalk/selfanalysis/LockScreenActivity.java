package com.toktoktalk.selfanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;

/**
 * Created by seogangmin on 2015. 8. 9..
 */
public class LockScreenActivity extends Activity{

    private ImageButton btnLockClose;
    private GridView iconsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        initComponent();
    }

    private void initComponent(){
        iconsContainer = (GridView) findViewById(R.id.icons_container);
        btnLockClose = (ImageButton) findViewById(R.id.btn_lock_close);

        btnLockClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockScreenActivity.this.finish();
            }
        });

    }
}
