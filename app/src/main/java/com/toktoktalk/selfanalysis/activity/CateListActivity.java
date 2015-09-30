package com.toktoktalk.selfanalysis.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.apis.CreateDoc;
import com.toktoktalk.selfanalysis.apis.QueryDocs;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.Cache;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.common.SimpleDialog;
import com.toktoktalk.selfanalysis.etc.ScreenService;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.utils.ComPreference;

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

    List<CateItemVo> cateList = new ArrayList<CateItemVo>();

    private SimpleDialog mSimpleDialog;
    private FloatingActionButton btnCateAdd;

    private ScrollView mScrollView;
    private ViewGroup mContainerView;

    private ToggleButton mActiveToggle;

    private ComPreference mPref = new ComPreference(this);


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


        boolean isActiveLockScreen = mPref.getValue(Const.PREF_ACTIVE_LOCKSCREEN, false);

        mActiveToggle = (ToggleButton) findViewById(R.id.btn_active);
        mActiveToggle.setChecked(isActiveLockScreen);

        mActiveToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent = new Intent(CateListActivity.this, ScreenService.class);
                    startService(intent);
                    mPref.put(Const.PREF_ACTIVE_LOCKSCREEN, true);
                    Toast.makeText(CateListActivity.this, "Lockscreen이 활성화 되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(CateListActivity.this, ScreenService.class);
                    stopService(intent);
                    mPref.put(Const.PREF_ACTIVE_LOCKSCREEN, false);
                    Toast.makeText(CateListActivity.this, "Lockscreen이 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        btnCateAdd.hide();

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

        //LinearLayout keyword_container = (LinearLayout) newView.findViewById(R.id.keyword_container);

        TextView tvCateName   = (TextView) newView.findViewById(R.id.tv_cate_name);
        //TextView tvKeywords   = (TextView) newView.findViewById(R.id.tv_keywords);
        tvCateName.setText(item.getCate_name());
        getQueryKeywordItems(item, newView);
        /*

        Map<String, IconVo> keywordItems = item.getKeyword_refs();
        StringBuffer keySb = null;

        int iconCount = keywordItems.keySet().size();

        if(iconCount > 0){
            keySb = new StringBuffer();
            for ( String key : keywordItems.keySet() ) {
                IconVo icon = keywordItems.get(key);

                keySb.append(icon.getKeyword());
                keySb.append(", ");
            }
            tvKeywords.setText(keySb.toString());


            for(int i=0; i<iconCount; i++){

            }

        }

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

        */


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

    private void getQueryKeywordItems(final CateItemVo cateItem, final ViewGroup rootView){

        HttpClient client = new HttpClient(this);
        QueryDocs query = new QueryDocs(Const.DATABASE_NAME, Const.COLLECTION_KEYWORD);
        query.putFind("cate_ref", cateItem.get_id());

        client.get("/database/find", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object body) {
                LinearLayout keyword_container = (LinearLayout) rootView.findViewById(R.id.keyword_container);
                TextView tvKeywords   = (TextView) rootView.findViewById(R.id.tv_keywords);
                Button btnDetail  = (Button) rootView.findViewById(R.id.btn_detail);

                final String keywordJsonArray = body.toString();

                String keywordText = "키워드가 등록되어있지 않습니다.";

                List<KeywordIcon> keywordList = (List<KeywordIcon>)GsonConverter.fromJsonArray(body.toString(), KeywordIcon.class);

                if(keywordList.size() > 0){

                    String savedKeywordJson = mPref.getValue(Const.PREF_SAVED_KEYWORDS, null);
                    Map keywords = (Map)GsonConverter.fromJson(savedKeywordJson, Map.class);
                    keyword_container.removeAllViews();

                    StringBuffer sb = new StringBuffer();
                    for(int i=0; i<keywordList.size(); i++){
                        KeywordIcon item = keywordList.get(i);
                        sb.append(item.getKeyword());
                        if(i < keywordList.size() -1){
                            sb.append(", ");
                        }

                        if(i < 6){
                            View view = LayoutInflater.from(CateListActivity.this).inflate(R.layout.small_icon, null);
                            ImageView imv = (ImageView)view.findViewById(R.id.imv_small_icon);
                            Map mapItem = (Map)keywords.get(item.get_id());
                            String path = Const.ICON_SAVED_FOLDER + "/" + mapItem.get("icon_file_name");
                            Bitmap bm = BitmapFactory.decodeFile(path);
                            imv.setImageBitmap(bm);


                            keyword_container.addView(view);
                        }else if(i == 6){
                            View view = LayoutInflater.from(CateListActivity.this).inflate(R.layout.more_text, null);
                            TextView tvMore = (TextView) view.findViewById(R.id.tv_more);

                            int total = keywordList.size();

                            tvMore.setText((total-5));
                            keyword_container.addView(view);
                        }


                    }

                    keywordText = sb.toString();


                }

                tvKeywords.setText(keywordText);

                btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CateListActivity.this, ChartActivity.class);
                        String json = GsonConverter.toJson(cateItem);
                        i.putExtra("cate_item", json);
                        startActivity(i);
                    }
                });

                keyword_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CateListActivity.this, CateDetailActivity.class);
                        String json = GsonConverter.toJson(cateItem);
                        i.putExtra("cate_item", json);
                        startActivity(i);
                    }
                });

            }
        }));
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
