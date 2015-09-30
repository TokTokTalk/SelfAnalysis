package com.toktoktalk.selfanalysis.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.model.KeywordRecord;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.DateUtils;
import com.toktoktalk.selfanalysis.utils.Logging;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 30..
 */
public class LineChartFragment extends Fragment{
    private int mPageNumber;
    private List<KeywordIcon> mKeywords;
    private CateItemVo mCateItem;
    private ArrayList mKeyIds = new ArrayList();
    private LineChart mLineChart;

    private int mColorIdx = 0;


    private int[] mColors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };

    public static LineChartFragment create(int pageNumber) {

        LineChartFragment fragment = new LineChartFragment();
        //Bundle args = new Bundle();
        //args.putInt("page", pageNumber);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPageNumber = getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_linechart, container, false);
        mLineChart = (LineChart) rootView.findViewById(R.id.chart_line);

        String cateItem    = getArguments().getString("cate_item");
        String keywordList = getArguments().getString("keyword_list");

        Logging.d("debug", "keywordList : "+keywordList);

        mCateItem = (CateItemVo)GsonConverter.fromJson(cateItem, CateItemVo.class);
        mKeywords = (List<KeywordIcon>)GsonConverter.fromJsonArray(keywordList, KeywordIcon.class);

        for (int i = 0; i < mKeywords.size(); i++) {
            KeywordIcon icon = mKeywords.get(i);
            mKeyIds.add(icon.get_id());
        }

        getKeywordCountData(mKeyIds);

        return rootView;
    }

    private void getKeywordCountData(ArrayList keyIds){
        HttpClient client = new HttpClient(getActivity());
        Map params = new HashMap();
        params.put("key_ids", keyIds);

        client.get("/database/findRecord", params, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                Logging.d("debug", object.toString());

                Map map = (Map) GsonConverter.fromJson(object.toString(), Map.class);
                setLineChart(map);
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


    private void setLineChart(Map lineData){

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

        try{

            for(int i=0; i<mKeywords.size(); i++){
                KeywordIcon icon = mKeywords.get(i);
                ArrayList<Entry> values = new ArrayList<Entry>();

                for(int j=0; j<xVals.size(); j++){
                    String xVal = xVals.get(j).toString();

                    Map sub1 = (Map)lineData.get(icon.get_id());

                    if(sub1 == null){
                        values.add(new Entry(0, j));
                        continue;
                    }

                    float value = sub1.containsKey(xVal) ? Float.parseFloat(sub1.get(xVal).toString()) : 0;
                    values.add(new Entry(value, j));

                }

                LineDataSet d = new LineDataSet(values, icon.getKeyword());
                d.setLineWidth(4.0f);
                d.setCircleSize(6f);

                int color = ColorTemplate.VORDIPLOM_COLORS[mColorIdx];
                d.setColor(color);
                d.setCircleColor(color);
                dataSets.add(d);
                mColorIdx++;
            }
        }catch (Exception e){

        }


        dataSets.get(0).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        Logging.d("debug", "xVals size : " + xVals.size());
        Logging.d("debug", "dataSets size : " + dataSets.size());

        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        //mLineChart.invalidate();
        mLineChart.animateX(1000);


    }

}
