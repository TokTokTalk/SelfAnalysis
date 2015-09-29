package com.toktoktalk.selfanalysis.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.paging.gridview.PagingGridView;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.adapter.IconSearchAdapter;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.model.ImageResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 19..
 */
public class IconSearchActivity extends BaseActivity{
    public final int MAX_RESULT_COUNT = 50;
    private final int MAX_CONCURRENT_REQUESTS = 4;
    private final int PAGE_SIZE = 8;
    private final String SEARCH_ENDPOINT = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=" + PAGE_SIZE;

    private int last_page_num = 0;

    private String searchKeyword;


    private IconSearchAdapter resultsImageAdapter;
    private GridView resultsGrid;
    public LruCache<String, Bitmap> imageCache;

    protected void asyncJson(String url, final int start, final IconSearchAdapter searchResultsAdapter){

        // TODO: Set cache to be big enough to handle 50 images at a time
        // TODO: Figure out if and how that will fail if it exceeds max memory for the app
        //tutorial default cache size - 8th of allowed memory size
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        final AQuery aq = new AQuery(findViewById(R.id.mainLayoutRoot));
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                /*
                 * TODO: Extend AjaxCallback so this line written once and can be reused for other AJAX
                 * queries that should only continue if they are results for the "current" search
                 */
                if (!isAttached(searchResultsAdapter)) return;

                if (json != null) {
                    try {
                        JSONObject responseData = json.getJSONObject("responseData");
                        JSONArray results = responseData.getJSONArray("results");
                        if (last_page_num == 0) {
                            JSONObject cursor = responseData.getJSONObject("cursor");
                            JSONArray pages = cursor.getJSONArray("pages");
                            JSONObject page_item = pages.getJSONObject(pages.length() - 1);
                            last_page_num = Integer.parseInt(page_item.getString("start"));
                        }


                        for (int i = 0; i < results.length(); i++) {
                            final ImageResult imageResult = new ImageResult();
                            imageResult.imgUrl = results.getJSONObject(i).getString("tbUrl");
                            imageResult.resultIndex = start + i;

                            if (imageResult.resultIndex >= MAX_RESULT_COUNT) {
                                continue;
                            }

                            //Load the image for this result in background before adding to adaptor
                            aq.ajax(imageResult.imgUrl, Bitmap.class, new AjaxCallback<Bitmap>() {
                                @Override
                                public void callback(String url, Bitmap bitmap, AjaxStatus status) {

                                    if (!isAttached(searchResultsAdapter)) return;

                                    imageCache.put(url, bitmap);
                                    resultsImageAdapter.addResult(imageResult);
                                    resultsGrid.invalidateViews();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        //TODO: handle errors
                    }
                } else {
                    //TODO: handle errors
                }
            }
        });
    }

    protected boolean isAttached(IconSearchAdapter adapter){
        return resultsGrid.getAdapter()==adapter;
    }

    protected void runSearch(String query){

        //Perform the search
        for(int i=0;i<MAX_RESULT_COUNT;i+=PAGE_SIZE){
            try {
                asyncJson(SEARCH_ENDPOINT + "&q=" + URLEncoder.encode(query, "UTF-8") + "&start=" + i, i, resultsImageAdapter);
            } catch (UnsupportedEncodingException e) {
                // TODO: handle error
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //set the max number of concurrent network connections, default is 4
        AjaxCallback.setNetworkLimit(MAX_CONCURRENT_REQUESTS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_search);

        resultsGrid = (GridView) findViewById(R.id.grid_view);
    }

    public void searchButtonClick(View view){
        final EditText searchInput = (EditText) findViewById(R.id.search_input);

        //Trash the last query results if there was one
        resultsImageAdapter = new IconSearchAdapter(this);
        resultsGrid.setAdapter(resultsImageAdapter);
        resultsGrid.invalidateViews();

        searchKeyword = searchInput.getText().toString();

        new SearchTask(this).execute(searchKeyword);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
    }



}

class SearchTask extends AsyncTask<String, Void, Void> {

    private IconSearchActivity context;

    public SearchTask(IconSearchActivity context){
        this.context = context;
    }

    protected Void doInBackground(String... args) {
        context.runSearch(args[0]);
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {}
    protected void onPostExecute(Long result) {}
}