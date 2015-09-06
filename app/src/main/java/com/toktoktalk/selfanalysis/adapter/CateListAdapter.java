package com.toktoktalk.selfanalysis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.model.CateItemVo;

import java.util.List;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class CateListAdapter extends BaseAdapter{

    private Context mContext;
    private List<CateItemVo> mItems;
    private LayoutInflater mInflater;


    public CateListAdapter(Context context, List<CateItemVo> items) {
        this.mContext = context;
        this.mItems   = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_cate_list_item, parent, false);
        }

        CateItemVo item = mItems.get(position);

        TextView tvCateName = (TextView)convertView.findViewById(R.id.tv_cate_name);

        tvCateName.setText(item.getCateName());


        return convertView;
    }
}
