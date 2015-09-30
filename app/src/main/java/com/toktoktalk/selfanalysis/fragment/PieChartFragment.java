package com.toktoktalk.selfanalysis.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.KeywordIcon;
import com.toktoktalk.selfanalysis.utils.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 30..
 */
public class PieChartFragment extends Fragment{
    private int mPageNumber;
    private PieChart mPieChart;
    private List<KeywordIcon> mKeywords;
    private CateItemVo mCateItem;
    private ArrayList mKeyIds = new ArrayList();


    public static PieChartFragment create(int pageNumber) {

        PieChartFragment fragment = new PieChartFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_piechart, container, false);
        mPieChart = (PieChart) rootView.findViewById(R.id.chart_pie);

        String cateItem    = getArguments().getString("cate_item");
        String keywordList = getArguments().getString("keyword_list");
        mCateItem = (CateItemVo)GsonConverter.fromJson(cateItem, CateItemVo.class);
        mKeywords = (List<KeywordIcon>)GsonConverter.fromJsonArray(keywordList, KeywordIcon.class);

        for (int i = 0; i < mKeywords.size(); i++) {
            KeywordIcon icon = mKeywords.get(i);
            mKeyIds.add(icon.get_id());
        }

        getPieChartData(mKeyIds);

        return rootView;
    }

    private void getPieChartData(ArrayList keyIds){
        HttpClient client = new HttpClient(getActivity());
        Map params = new HashMap();
        params.put("key_ids", keyIds);

        client.get("/database/recordAvg", params, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                Logging.d("debug", object.toString());

                Map map = (Map) GsonConverter.fromJson(object.toString(), Map.class);
                int sum = (int)Float.parseFloat(map.get("sum").toString());
                Map data = (Map) map.get("data");
                if (map != null) {
                    setPieChartData(data, sum);
                }
            }
        }));
    }

    private void setPieChartData(Map obj, int sum) {

        Iterator it = obj.keySet().iterator();

        int count = obj.keySet().size();

        if(count > 0){
            float range = sum;

            float mult = range;

            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            ArrayList<String> xVals = new ArrayList<String>();

            //for (int i = 0; i < count + 1; i++)
            //xVals.add("A");

            int idx = 0;

            while (it.hasNext()){
                String key = it.next().toString();
                int value = (int)Float.parseFloat(obj.get(key).toString());

                float yVal = (value/mult)*100;

                yVals1.add(new Entry((float)yVal, idx));

                for(int i =0; i<mKeywords.size(); i++){
                    KeywordIcon icon = mKeywords.get(i);
                    if(key.equals(icon.get_id())){
                        xVals.add(icon.getKeyword());
                        continue;
                    }
                }

                idx++;
            }


            PieDataSet dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            //data.setValueTypeface(tf);
            mPieChart.setData(data);

            // undo all highlights
            mPieChart.highlightValues(null);

            mPieChart.invalidate();
        }

    }
}
