package com.toktoktalk.selfanalysis.activity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.apis.QueryDocs;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.CategoryItem;
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.model.KeywordRecord;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.DateUtils;
import com.toktoktalk.selfanalysis.utils.Logging;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChartActivity extends BaseActivity {

    private LineChart mLineChart;

    private int[] mColors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };

    private CateItemVo mCateItem;
    private List<KeywordIcon> mKeywords;

    private ComPreference mPref = new ComPreference(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        String cateJson = getIntent().getStringExtra("cate_item").toString();
        mCateItem = (CateItemVo) GsonConverter.fromJson(cateJson, CateItemVo.class);

        getKeywordList(mCateItem);


        int colorLength = ColorTemplate.VORDIPLOM_COLORS.length;
        Logging.i("info", "colorLength : " + colorLength);
        //init();

    }

    private void getKeywordList(CateItemVo cateItem){
        HttpClient client = new HttpClient(this);
        QueryDocs query = new QueryDocs(Const.DATABASE_NAME, Const.COLLECTION_KEYWORD);
        query.putFind("cate_ref", cateItem.get_id());

        client.get("/database/find", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object body) {
                mKeywords = GsonConverter.fromJsonArray(body.toString(), KeywordIcon.class);

                ArrayList keyIds = new ArrayList();

                for (int i = 0; i < mKeywords.size(); i++) {
                    KeywordIcon icon = mKeywords.get(i);
                    keyIds.add(icon.get_id());
                }

                getKeywordCountData(keyIds);

            }
        }));
    }


    private void getKeywordCountData(ArrayList keyIds){
        HttpClient client = new HttpClient(this);
        Map params = new HashMap();
        params.put("key_ids", keyIds);

        client.get("/database/findRecord", params, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                Logging.d("debug", object.toString());

                Map map = (Map)GsonConverter.fromJson(object.toString(), Map.class);
                if(map != null){
                    init(map);
                }
            }
        }));
    }

    private ArrayList<String> getLineXval(){

        String currentDt = DateUtils.formatter("yyyyMMdd");
        String startDt   = mCateItem.getStart_dt();

        ArrayList<String> xVals = null;

        try {
            xVals = DateUtils.getBetWeenDateArray(startDt, currentDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return xVals;
    }


    private void init(Map countData){
        mLineChart = (LineChart) findViewById(R.id.chart1);

        mLineChart.setDrawGridBackground(false);
        //mLineChart.setDescription("keyword1, keyword2, keyword3");

        // mChart.setStartAtZero(true);

        // enable value highlighting
        mLineChart.setHighlightEnabled(true);

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);

        Legend l = mLineChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        ArrayList<String> xVals = getLineXval();


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        if(countData.keySet().size() > 0){
            Iterator it = countData.keySet().iterator();

            int idx = 0;
            while(it.hasNext()){

                List<KeywordRecord> recordList = GsonConverter.fromJsonArray(countData.get(it.next().toString()).toString(), KeywordRecord.class);
                ArrayList<Entry> values = new ArrayList<Entry>();

                for(int i=0; i<xVals.size(); i++){
                    String xVal = xVals.get(i).toString();
                    for(int k=0; k<recordList.size(); k++){
                        KeywordRecord rec = recordList.get(k);
                        if(rec.getRecord_dt().equals(xVal)){
                            values.add(new Entry(rec.getCount(), i));
                        }else{
                            values.add(new Entry(0, i));
                        }
                    }
                }

                LineDataSet d = new LineDataSet(values, mKeywords.get(idx).getKeyword());
                d.setLineWidth(4.0f);
                d.setCircleSize(6f);

                int color = mColors[idx % mColors.length];
                d.setColor(color);
                d.setCircleColor(color);
                dataSets.add(d);

                idx++;
            }

            dataSets.get(0).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        }



        /*
        for (int z = 0; z < mKeywords.size(); z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for(int i=0; i<xVals.size(); i++){
                double val = (Math.random() * 10 + 3);
                values.add(new Entry((float)val, i));
            }

            LineDataSet d = new LineDataSet(values, mKeywords.get(z).toString());
            d.setLineWidth(4.0f);
            d.setCircleSize(6f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }
        */


        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        //mLineChart.invalidate();
        mLineChart.animateX(1000);

    }

}
