package com.toktoktalk.selfanalysis.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.toktoktalk.selfanalysis.common.EventRegistration;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by seogangmin on 2015. 9. 20..
 */
public class AsyncFileDownloader extends AsyncTask<String, String, String>{


    private Map<String, String> matchMap;
    private EventRegistration callback;
    private Iterator<String> it;
    private boolean isSkip;
    private JSONArray results = new JSONArray();

    public AsyncFileDownloader() {

    }

    public AsyncFileDownloader(Map<String, String> matchMap, boolean isSkip, EventRegistration callback) {
        this.matchMap = matchMap;
        this.callback = callback;
        this.isSkip = isSkip;
        this.it = matchMap.keySet().iterator();
    }

    public void start(){
        Logging.d("debug", "start download");
        this.execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @Override
    protected String doInBackground(String... aurl) {


        int last    = matchMap.keySet().size();
        int current = 0;

        try {


            for (Map.Entry<String, String> e : matchMap.entrySet()) {

                int count = 0;
                String target = e.getKey();
                String dest = e.getValue();

                Logging.d("debug", target);
                Logging.d("debug", dest);

                //if(new File(dest).exists()) continue;

                URL url = new URL(target);
                URLConnection conn = url.openConnection();
                conn.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conn.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream
                OutputStream output = new FileOutputStream(dest);

                byte data[] = new byte[1024];

                long total = 0;

                Logging.d("debug", "total :::: " + total);

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    Logging.d("debug", "total : " + total);
                    output.write(data, 0, count);
                }

                Logging.d("debug", "dest :::: " + dest);

                results.put(dest);

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
            }

        } catch (IOException e) {
            Logging.e("error", e.getMessage());
            return null;
        }

        return null;

    }
    protected void onProgressUpdate(String... progress) {
        Log.d("debug", progress[0]);
        //mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String unused) {
        //dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        callback.doWork(results.toString());
    }
}
