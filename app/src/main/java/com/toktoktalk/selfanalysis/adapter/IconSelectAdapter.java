package com.toktoktalk.selfanalysis.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 16..
 */
public class IconSelectAdapter extends BaseAdapter{

    private List<IconVo> mItems;
    private Context mCtx;
    private LayoutInflater mInflater;


    public IconSelectAdapter() {
    }

    public IconSelectAdapter(Context mCtx, List<IconVo> mItems) {
        this.mCtx = mCtx;
        this.mInflater = LayoutInflater.from(mCtx);
        this.mItems = mItems;
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
        final ImageView imageView;
        IconVo item = mItems.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.icon_item, null);
            imageView = (ImageView) convertView.findViewById(R.id.ico_view);

            if(item.getIcoFilePath()!=null){
                String filePath = mItems.get(position).getIcoFilePath();
                imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            }

        }

        return convertView;
    }
}
