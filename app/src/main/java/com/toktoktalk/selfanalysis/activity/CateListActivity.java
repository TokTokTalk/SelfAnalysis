package com.toktoktalk.selfanalysis.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.CateListAdapter;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 2..
 */
public class CateListActivity extends Activity {

    private ListView mCateListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);

        mCateListView = (ListView)findViewById(R.id.cate_list);

        List<CateItemVo> dummy = dummyCategory();

        mCateListView.setAdapter(new CateListAdapter(this, dummy));
    }


    private List<CateItemVo> dummyCategory(){
        List<CateItemVo> cateList = new ArrayList<CateItemVo>();

        for(int i=0; i<10; i++){
            CateItemVo item = new CateItemVo();
            item.setId(i + "");
            item.setCateName("카테고리" + i);

            List<IconVo> icon = new ArrayList<IconVo>();
            for(int k=0; k<6; k++){
                icon.add(new IconVo(k+"","keyword"+k, Environment.getExternalStorageDirectory().getAbsolutePath()+"/toktoktalk/ico_1.jpeg"));
            }

            item.setIconList(icon);

            cateList.add(item);
        }

        return cateList;
    }

}
