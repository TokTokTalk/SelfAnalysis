package com.toktoktalk.selfanalysis.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.utils.Logging;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

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

        Logging.i("info", "[REQUEST BODY] >> "+ strJson);

        byte[] jsonByte = null;
        try {
            jsonByte = strJson.getBytes("UTF8");
        }catch (Exception e){
            Toast.makeText(mCtx, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }



        httpClient.post(uri, "application/json", jsonByte, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse response) {

                int resultCode = response.getStatus();

                Logging.i("info", "[RESPONSE RESULT]"+response.toString());

                if(resultCode == 200){
                    String result = getResultFromJson(response.getBodyAsString());
                    callback.doWork(result);
                }else{
                    String msg = "["+resultCode+"]";
                    msg += response.getHeaders();
                    Toast.makeText(mCtx, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                Toast.makeText(mCtx, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void get(String uri, Object query, final EventRegistration callback){
        AndroidHttpClient httpClient = new AndroidHttpClient(Const.SERVER_DOMAIN);
        httpClient.setMaxRetries(5);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(query);

        Logging.i("info", "[REQUEST PARAMS] >> "+json.toString());

        ParameterMap params = new ParameterMap();
        params.put("params", json);

        httpClient.get(uri, params, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse response) {
                int resultCode = response.getStatus();

                Logging.i("info", "[RESPONSE RESULT]"+response.toString());

                if(resultCode == 200){
                    String result = getResultFromJson(response.getBodyAsString());
                    callback.doWork(result);
                }else{
                    String msg = "["+resultCode+"]";
                    msg += response.getHeaders();
                    Toast.makeText(mCtx, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                Toast.makeText(mCtx, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getResultFromJson(String json){
        String result = null;

        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(json);
            result = jsonObj.getString("result");
        } catch (JSONException e) {
            return result;
        }

        return result;
    }
}


