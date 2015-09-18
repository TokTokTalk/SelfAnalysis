package com.toktoktalk.selfanalysis.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.CateListAdapter;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.Cache;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.SimpleDialog;
import com.toktoktalk.selfanalysis.apis.CreateDoc;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.apis.QueryDocs;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by seogangmin on 2015. 9. 2..
 */
public class CateListActivity extends BaseActivity {

    //private ListView mCateListView;
    private FrameLayout mLayoutLoading;

    private RecyclerView mRecycleView;
    private CateListAdapter mCateListAdapter;

    List<CateItemVo> cateList = new ArrayList<CateItemVo>();

    private SimpleDialog mSimpleDialog;
    private FloatingActionButton btnCateAdd;

    private ScrollView mScrollView;
    private ViewGroup mContainerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);

        initComponent();
        queryCateList();


    }

    private void queryCateList(){
        showLoading();
        HttpClient client = new HttpClient(this);

        QueryDocs query = new QueryDocs(Const.DATABASE_NAME, Const.COLLECTION_CATEGORY);
        Log.d("debug", Cache.getUser(this).get_id());
        query.putFind("user_ref", Cache.getUser(this).get_id());

        client.get("/database/find", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object body) {
                hideLoading();
                setCateList(body.toString());
            }
        }));
    }


    private void setCateList(String jsonData){
        Type listType = new TypeToken<List<CateItemVo>>(){}.getType();
        cateList = (List<CateItemVo>) new Gson().fromJson(jsonData, listType);
        for(int i=0; i<cateList.size(); i++){
            CateItemVo item = cateList.get(i);
            addItem(item);
        }
    }

    private void initComponent(){
        //mRecycleView = (RecyclerView)findViewById(R.id.cate_list);;
        mLayoutLoading = (FrameLayout)findViewById(R.id.layout_loading);
        showLoading();

        //mRecycleView.setItemAnimator(new DefaultItemAnimator());
        //mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mScrollView = (ScrollView) findViewById(R.id.cate_list_scroll);
        mContainerView = (ViewGroup) findViewById(R.id.cate_list_container);
        btnCateAdd = (FloatingActionButton) findViewById(R.id.fab);

        btnCateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSimpleDialog = new SimpleDialog(CateListActivity.this, "AddCategory", new EventRegistration(new CallbackEvent() {
                    @Override
                    public void callbackMethod(Object body) {
                        createCategory(body.toString());
                        mSimpleDialog.dismiss();
                    }
                }));
                mSimpleDialog.show();

            }
        });

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("debug", "scroll changed!!");
            }
        });

    }

    private void showLoading(){
        mLayoutLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        mLayoutLoading.setVisibility(View.GONE);
    }


    private void addItem(final CateItemVo item) {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_cate_list_item, mContainerView, false);

        TextView tvCateName   = (TextView) newView.findViewById(R.id.tv_cate_name);
        TextView tvKeywords   = (TextView) newView.findViewById(R.id.tv_keywords);
        tvCateName.setText(item.getCate_name());

        Map<String, IconVo> keywordItems = item.getKeyword_refs();

        StringBuffer keySb = new StringBuffer();

        for ( String key : keywordItems.keySet() ) {
            IconVo icon = keywordItems.get(key);

            keySb.append(icon.getKeyword());
            keySb.append(", ");
        }

        tvKeywords.setText(keySb.toString());

        LinearLayout keywordContainer = (LinearLayout) newView.findViewById(R.id.keyword_container);
        keywordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CateListActivity.this, CateDetailActivity.class);
                String json = GsonConverter.toJson(item);
                i.putExtra("cate_item",json);
                startActivity(i);
            }
        });

        ImageButton btnDelete = (ImageButton)newView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                mContainerView.removeView(newView);

                // If there are no rows remaining, show the empty view.
                if (mContainerView.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });


        findViewById(android.R.id.empty).setVisibility(View.GONE);
        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }


    private void createCategory(String cateName){

        HttpClient client = new HttpClient(this);
        CateItemVo category = new CateItemVo();
        category.initEmpty();
        category.setUser_ref(Cache.getUser(this).get_id());
        category.setCate_name(cateName);

        CreateDoc params = new CreateDoc(Const.DATABASE_NAME, Const.COLLECTION_CATEGORY, category);

        client.post("/database/create", params, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object obj) {
                CateItemVo item = new Gson().fromJson(obj.toString(), CateItemVo.class);

                addItem(item);
            }
        }));

    }

}
