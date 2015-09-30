package com.toktoktalk.selfanalysis.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.apis.QueryDocs;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.fragment.LineChartFragment;
import com.toktoktalk.selfanalysis.fragment.PieChartFragment;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.KeywordIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 30..
 */
public class ChartActivity extends FragmentActivity{

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private List<KeywordIcon> mKeywords;
    private ArrayList mKeyIds;
    private CateItemVo mCateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_pager);

        String cateJson = getIntent().getStringExtra("cate_item").toString();
        mCateItem = (CateItemVo) GsonConverter.fromJson(cateJson, CateItemVo.class);

        getKeywordList(mCateItem);

    }

    private void getKeywordList(CateItemVo cateItem){
        HttpClient client = new HttpClient(this);
        QueryDocs query = new QueryDocs(Const.DATABASE_NAME, Const.COLLECTION_KEYWORD);
        query.putFind("cate_ref", cateItem.get_id());

        client.get("/database/find", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object body) {
                mKeywords = GsonConverter.fromJsonArray(body.toString(), KeywordIcon.class);

                mKeyIds = new ArrayList();

                for (int i = 0; i < mKeywords.size(); i++) {
                    KeywordIcon icon = mKeywords.get(i);
                    mKeyIds.add(icon.get_id());
                }

                init();

            }
        }));
    }

    private void init(){
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // 해당하는 page의 Fragment를 생성합니다.
            Fragment selected = null;
            switch (position){
                case 0 : {
                    selected = LineChartFragment.create(position);
                    break;
                }
                case 1 : {
                    selected = PieChartFragment.create(position);
                    break;
                }
            }

            Bundle bundle = new Bundle();

            bundle.putString("cate_item", GsonConverter.toJson(mCateItem));
            bundle.putString("keyword_list", GsonConverter.toJson(mKeywords));

            selected.setArguments(bundle);

            return selected;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
