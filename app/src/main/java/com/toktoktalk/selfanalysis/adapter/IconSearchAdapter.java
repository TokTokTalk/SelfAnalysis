package com.toktoktalk.selfanalysis.adapter;

import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.toktoktalk.selfanalysis.activity.IconSearchActivity;
import com.toktoktalk.selfanalysis.model.ImageResult;

/**
 * Created by seogangmin on 2015. 9. 19..
 */
public class IconSearchAdapter extends BaseAdapter{

    private IconSearchActivity context;
    private int contiguousResults; //Contiguous results count form front of results array
    private SimpleArrayMap<Integer, ImageResult> results;

    public IconSearchAdapter(IconSearchActivity context) {
        this.contiguousResults = 0;
        this.results = new SimpleArrayMap<Integer, ImageResult>();
        this.context = context;
    }

    public int getCount() {
        return Math.min(contiguousResults, context.MAX_RESULT_COUNT);
    }

    public Object getItem(int position) {
        return results.valueAt(position);
    }

    public long getItemId(int position) {
        //not implemented, not needed for this application
        return 0;
    }

    public void addResult(ImageResult result){
        this.results.put(result.resultIndex, result);

        //recalculate contiguous result count
        int i = contiguousResults;
        while(results.get(i) != null){
            i++;
        }

        if(i!=contiguousResults){
            contiguousResults = i;
            notifyDataSetChanged();
        }
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        } else {
            imageView = (ImageView) convertView;
            imageView.setImageResource(android.R.color.transparent);
        }

        String imageUrl = results.get(position).imgUrl;
        imageView.setImageBitmap(context.imageCache.get(imageUrl));

        return imageView;
    }
}
