package com.toktoktalk.selfanalysis.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.apis.InsertKeyword;
import com.toktoktalk.selfanalysis.common.BaseActivity;
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
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 6..
 */
public class CateDetailActivity extends BaseActivity {

    private CateItemVo mCate;

    private List<IconVo> mKeywordList;
    private IconVo mInsertIcon;

    private ImageButton btnInsertIcon;

    private ViewGroup mContainerView;

    private SimpleDialog mSimpleDialog;
    private Handler mHandler = new Handler();

    private final int ICON_INSERT_DELAY = 500;
    private final int RESULT_CODE = 1;

    private ComPreference mPref = new ComPreference(CateDetailActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_detail);

        String cateJson = getIntent().getStringExtra("cate_item");
        mCate = new Gson().fromJson(cateJson, CateItemVo.class);
        mInsertIcon = new IconVo();

        initComponent();
        setComponent();
        queryKeywordList();

    }

    private void initComponent(){

        btnInsertIcon = (ImageButton) findViewById(R.id.imgbtn_plus);
        mContainerView = (ViewGroup) findViewById(R.id.icons_list_container);
    }

    private void setComponent(){

        btnInsertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                mSimpleDialog = new SimpleDialog(CateDetailActivity.this, "AddKeyword", new EventRegistration(new CallbackEvent() {
                    @Override
                    public void callbackMethod(Object body) {
                        Log.d("debug", "inputKeyword"+body.toString());
                        mInsertIcon.setKeyword(body.toString());
                        Intent i = new Intent(CateDetailActivity.this, IconSelectActivity.class);
                        startActivityForResult(i, RESULT_CODE);
                        mSimpleDialog.dismiss();
                    }
                }));
                mSimpleDialog.show();*/

                Toast.makeText(CateDetailActivity.this, "준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addItem(IconVo item) {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.layout_ico_item, mContainerView, false);

        ImageView btnIcon = (ImageView) newView.findViewById(R.id.btn_ico);
        Bitmap bm = BitmapFactory.decodeFile(item.getIcoFilePath());
        btnIcon.setImageBitmap(bm);

        TextView keyword = (TextView) newView.findViewById(R.id.tv_keyword);
        keyword.setText(item.getKeyword());

        ImageButton btnDelete = (ImageButton)newView.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContainerView.removeView(newView);
                if (mContainerView.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        mContainerView.addView(newView, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            Toast.makeText(CateDetailActivity.this, "콜백 성공!", Toast.LENGTH_SHORT).show();
            String iconFilePath = data.getStringExtra("icon_file_path");
            mInsertIcon.setIcoFilePath(iconFilePath);
            createKeyword();
        }
    }

    private void createKeyword(){
        HttpClient client = new HttpClient(this);

        Map createDoc = new HashMap();
        createDoc.put("keyword",mInsertIcon.getKeyword());
        createDoc.put("timestamp", "1442493296");
        createDoc.put("cate_ref", mCate.get_id());

        Map find = new HashMap();
        find.put("keyword", mInsertIcon.getKeyword());
        find.put("cate_ref", mCate.get_id());

        InsertKeyword insertKeyword = new InsertKeyword(createDoc, find);

        client.post("/database/insertKeyword", insertKeyword, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object obj) {
                IconVo item = (IconVo) GsonConverter.fromJson(obj.toString(), IconVo.class);
                item.setKeyword(mInsertIcon.getKeyword());
                item.setIcoFilePath(mInsertIcon.getIcoFilePath());
                mPref.put(item.get_id(), item.getIcoFilePath());
                addItem(item);
            }
        }));
    }

    private void queryKeywordList(){
        HttpClient client = new HttpClient(this);

        QueryDocs query = new QueryDocs(Const.DATABASE_NAME, Const.COLLECTION_KEYWORD);
        query.putFind("cate_ref", mCate.get_id());

        client.get("/database/find", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object body) {
                setKeywordList(body.toString());
            }
        }));
    }


    private void setKeywordList(String jsonData){

        mKeywordList = (List<IconVo>)GsonConverter.fromJsonArray(jsonData, IconVo.class);
        String matched = mPref.getValue(Const.PREF_SAVED_KEYWORDS, null);

        Map savedMap = (Map)GsonConverter.fromJson(matched, Map.class);

        Map matchedKeywords = (Map)GsonConverter.fromJson(matched, Map.class);

        for(int i=0; i<mKeywordList.size(); i++){
            IconVo item = mKeywordList.get(i);
            Map itemMap = (Map)savedMap.get(item.get_id());
            String path = Const.ICON_SAVED_FOLDER + "/" + itemMap.get("icon_file_name");
            item.setIcoFilePath(path);
            addItem(item);
        }
    }
}
