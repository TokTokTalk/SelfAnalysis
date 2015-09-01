package com.toktoktalk.selfanalysis.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.toktoktalk.selfanalysis.R;


/**
 * Created by YoungKyoung on 2015-08-28.
 */
public class CategoryActivity extends Activity{
    private Integer[] mThumblds ={
            R.mipmap.btn_plus_type1
    };
    public class MyAdapter extends BaseAdapter{
        private Context mContext;

        public MyAdapter(Context c){
            mContext = c;
        }

        public int getCount(){
            return mThumblds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        public Object getItem(int arg[]){
            return mThumblds[0];
        }

        public long getItemId(int arg0){
            return arg0;

        }
        public View getView(int position, View convertView, ViewGroup parent){
            View grid;
            if(convertView == null) {
                grid = new View(mContext);
                LayoutInflater inflater = getLayoutInflater();
                grid = inflater.inflate(R.layout.icon_item, parent, false);
            }else{
                grid = (View)convertView;
            }
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridView1);
            TextView textView = (TextView)grid.findViewById(R.id.gridView1);
            imageView.setImageResource(mThumblds[position]);
            textView.setText(String.valueOf(position));

            return grid;
        }
    }

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_category);

        GridView gridView =(GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new MyAdapter(this));


    }

}



