package com.toktoktalk.selfanalysis.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.apache.http.entity.ByteArrayEntity;

/**
 * Created by seogangmin on 2015. 9. 8..
 */
public class HttpClient {

    private Context mCtx;

    public HttpClient(Context context){
        this.mCtx = context;
    }

    public void post(String uri, Object params, final EventRegistration callback){
        AndroidHttpClient httpClient = new AndroidHttpClient(Const.SERVER_DOMAIN);
        httpClient.setMaxRetries(5);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String strJson = gson.toJson(params);

        byte[] jsonByte = null;
        try {
            jsonByte = strJson.getBytes("UTF8");
        }catch (Exception e){
            Toast.makeText(mCtx, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }



        httpClient.post(uri, "application/json", jsonByte, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                callback.doWork(httpResponse);
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                Toast.makeText(mCtx, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void get(String uri, String json, AsyncCallback callback){
        AndroidHttpClient httpClient = new AndroidHttpClient(Const.SERVER_DOMAIN);
        httpClient.setMaxRetries(5);

        ParameterMap params = new ParameterMap();
        params.put("params", json);

        httpClient.get(uri, params, callback);
    }
}
