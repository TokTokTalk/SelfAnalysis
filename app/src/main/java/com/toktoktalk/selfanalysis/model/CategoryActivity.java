package com.toktoktalk.selfanalysis.model;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.ImageAdapter;

/**
 * Created by YoungKyoung on 2015-08-28.
 */
public class CategoryActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        GridView gridView = (GridView)findViewById(R.id.gridView1);
        gridView.setAdapter(new ImageAdapter(this));
    }
}



