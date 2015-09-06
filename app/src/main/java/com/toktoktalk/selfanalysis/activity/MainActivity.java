package com.toktoktalk.selfanalysis.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.etc.ScreenService;

public class MainActivity extends Activity {

    private Button onBtn, offBtn;
    private Activity mActivity;

    private Button btnStartAct;
    private Button btnLockScreenAct;
    private Button btnCateListAct;
    private Button btnCateDetailAct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        //lockscreen service on/off
        //onBtn = (Button) findViewById(R.id.btn1);
        //offBtn = (Button) findViewById(R.id.btn2);

        /*
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(mActivity, ScreenService.class);

                startService(intent);
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(mActivity, ScreenService.class);
                stopService(intent);
            }
        });
        */

        initComoponent();

    }

    private void initComoponent(){
        btnStartAct = (Button)findViewById(R.id.btn_startactivity);
        btnLockScreenAct = (Button)findViewById(R.id.btn_lockscreen);
        btnCateListAct   = (Button)findViewById(R.id.btn_catelistactivity);
        btnCateDetailAct = (Button)findViewById(R.id.btn_catedetailactivity);

        btnStartAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StartActivity.class);
                startActivity(i);
            }
        });

        btnLockScreenAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LockScreenActivity.class);
                startActivity(i);
            }
        });

        btnCateListAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CateListActivity.class);
                startActivity(i);
            }
        });

        btnCateDetailAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CateDetailActivity.class);
                startActivity(i);
            }
        });
    }

}