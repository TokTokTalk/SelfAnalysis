package com.toktoktalk.selfanalysis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.util.List;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class IconSearchGridAdapter extends BaseAdapter{

    private Context mContext;
    private String[] mItems;
    private LayoutInflater mInflater;


    public IconSearchGridAdapter(Context context, String[] items) {
        this.mContext = context;
        this.mItems   = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            //convertView = mInflater.inflate(R.layout.icon_item, parent, false);
        }


        return convertView;
    }
}
