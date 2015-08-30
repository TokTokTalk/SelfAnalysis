package com.toktoktalk.selfanalysis;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.toktoktalk.selfanalysis.model.CategoryActivity;

public class MainActivity extends Activity{

    private Button onBtn, offBtn, nextBtn;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        onBtn = (Button) findViewById(R.id.btn1);
        offBtn = (Button) findViewById(R.id.btn2);
        nextBtn = (Button) findViewById(R.id.btn3);


        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ScreenService.class);

                startService(intent);
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ScreenService.class);
                stopService(intent);
            }
        });

        nextBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

}