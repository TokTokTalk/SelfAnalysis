package com.toktoktalk.selfanalysis.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.IconSelectAdapter;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IconSelectActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private GridView mGridView;
    private List<IconVo> mItem;
    private IconVo mSelectedItem;
    private ImageButton mBtnNext;
    private FloatingActionButton fab;

    final String SAVED_ICONS_FOLDER  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/toktoktalk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);

        initComponent();
    }

    private void initComponent(){

        mBtnNext = (ImageButton) findViewById(R.id.imgbtn_arrow_next);

        mGridView = (GridView) findViewById(R.id.saved_img_grid);

        mItem = getSavedIcons();

        fab  = (FloatingActionButton) findViewById(R.id.fab);

        IconSelectAdapter adapter = new IconSelectAdapter(this, mItem);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(this);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem == null) {
                    Toast.makeText(IconSelectActivity.this, "아이콘을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent();
                    i.putExtra("icon_file_path", mSelectedItem.getIcoFilePath());
                    setResult(1, i);
                    IconSelectActivity.this.finish();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IconSelectActivity.this, IconSearchActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedItem = mItem.get(position);
    }

    private List<IconVo> getSavedIcons(){

        List<IconVo> list = new ArrayList<IconVo>();

        File rootFolder = new File(SAVED_ICONS_FOLDER);

        if(!rootFolder.isDirectory()){
            return null;
        }

        String[] fileList = rootFolder.list();
        for (int i=0; i < fileList.length; i++)
        {
            Log.d("debug",fileList[i]);
            list.add(new IconVo(null, null, SAVED_ICONS_FOLDER + "/" + fileList[i]));
        }

        return list;
    }


}
