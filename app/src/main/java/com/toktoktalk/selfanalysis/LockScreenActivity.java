package com.toktoktalk.selfanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.toktoktalk.selfanalysis.adapter.IconsGridAdapter;
import com.toktoktalk.selfanalysis.model.IconVo;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.Const;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seogangmin on 2015. 8. 9..
 */
public class LockScreenActivity extends Activity{

    private GridView         iconsContainer;
    private ImageButton      btnLockClose;
    private IconsGridAdapter mGridAdapter;
    private ComPreference    mPrefer = new ComPreference(this);


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

        iconsContainer.setAdapter(new IconsGridAdapter(this, getItems()));

    }

    private List<IconVo> getItems(){
        String savedJson = mPrefer.getValue(Const.PREF_SAVED_KEYWORD,null);

        if(savedJson == null){
            savedDummy();
            savedJson = mPrefer.getValue(Const.PREF_SAVED_KEYWORD,null);
        }

        if(savedJson == null) return null;

        Gson gson = new Gson();

        Type listType = new TypeToken<List<IconVo>>(){}.getType();
        List<IconVo> list = (List<IconVo>) gson.fromJson(savedJson, listType);

        return list;

    }

    private void savedDummy(){

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/toktoktalk";

        List<IconVo> list = new ArrayList<IconVo>();

        File rootFolder = new File(path);

        if(!rootFolder.isDirectory()){
            return;
        }

        String[] fileList = rootFolder.list();


        for (int i=0; i < fileList.length; i++)
        {
            list.add(new IconVo("id_"+i,"keyword_"+i, path+"/"+fileList[i]));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String strJson = gson.toJson(list);

        mPrefer.put(Const.PREF_SAVED_KEYWORD, strJson);

    }

}
