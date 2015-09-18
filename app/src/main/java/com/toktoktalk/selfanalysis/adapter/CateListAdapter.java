package com.toktoktalk.selfanalysis.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.activity.CateDetailActivity;
import com.toktoktalk.selfanalysis.model.CateItemVo;
import com.toktoktalk.selfanalysis.model.IconVo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by seogangmin on 2015. 8. 14..
 */
public class CateListAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<CateItemVo> mItems;

    public CateListAdapter(Context context, List<CateItemVo> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cate_list_item, viewGroup, false);
            return new ListItemViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ListItemViewHolder holder = (ListItemViewHolder) viewHolder;
        final CateItemVo item = mItems.get(position);

        StringBuffer sb = new StringBuffer();

        /*
        if(item.getKeyword_refs() != null && item.getKeyword_refs().size() > 0){
            holder.liKeywordContainer.removeAllViews();
            List<IconVo> icons = item.getKeyword_refs();
            for(int i=0; i<icons.size(); i++){
                IconVo icon = icons.get(i);
                sb.append(icon.getKeyword());
                if(i<icons.size()){
                    sb.append(", ");
                }

                if(i<=5){
                    holder.liKeywordContainer.addView(createImageView(icon));
                }else if(i == 6){
                    holder.liKeywordContainer.addView(createTextMore("+"+(icons.size()-6)));
                }

            }
        }else {
            sb.append("");
        }*/



        holder.tvCateName.setText(item.getCate_name());
        holder.tvKeywords.setText(sb.toString());
        holder.liKeywordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String cate = gson.toJson(item).toString();
                Intent i = new Intent(mContext, CateDetailActivity.class);
                i.putExtra("cateItem", cate);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }




    public final static class ListItemViewHolder extends RecyclerView.ViewHolder{

        TextView tvCateName;
        TextView tvKeywords;
        LinearLayout liKeywordContainer;
        TextView tvMore;


        public ListItemViewHolder(View itemView, int viewType) {
            super(itemView);

            tvCateName = (TextView)itemView.findViewById(R.id.tv_cate_name);
            tvKeywords = (TextView)itemView.findViewById(R.id.tv_keywords);
            liKeywordContainer = (LinearLayout)itemView.findViewById(R.id.keyword_container);
            //tvMore     = (TextView)itemView.findViewById(R.id.tv_more);

        }
    }

    private ImageView createImageView(IconVo iconVo){

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(24, 24));
        if(iconVo.getIcoFilePath() != null){
            Bitmap bm = BitmapFactory.decodeFile(iconVo.getIcoFilePath());
            imageView.setImageBitmap(bm);
        }else{
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ico_empty);
            imageView.setImageDrawable(drawable);
        }

        return imageView;
    }

    private TextView createTextMore(String text){
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new LinearLayout.LayoutParams(24, 24));
        tv.setText(text);
        return tv;
    }

}
