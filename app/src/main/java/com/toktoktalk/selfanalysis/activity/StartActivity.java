package com.toktoktalk.selfanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.GsonConverter;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.UserVo;
import com.toktoktalk.selfanalysis.apis.CreateDoc;
import com.toktoktalk.selfanalysis.utils.AsyncFileDownloader;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.Logging;
import com.toktoktalk.selfanalysis.utils.ViewAnimator;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by seogangmin on 2015. 9. 2..
 */
public class StartActivity extends BaseActivity {

    private LinearLayout linearLoading;
    private LinearLayout linearLogin;

    private ViewAnimator viewAnim = new ViewAnimator(this);

    private CallbackManager callbackManager;

    private UserVo mUser;
    private ComPreference prefer = new ComPreference(this);
    private TextView mTextLoading;

    private Handler mHandler = new Handler();

    private final String SERVER_ICONS_PATH  =  Const.RESOURCE_PATH;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        File dir = new File(Const.ICON_SAVED_FOLDER);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }*/



        init();

    }


    private void init(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_start);

        linearLoading = (LinearLayout)findViewById(R.id.linearLoading);
        linearLogin   = (LinearLayout)findViewById(R.id.linearLogin);
        mTextLoading = (TextView) findViewById(R.id.txt_loading);


        if(isSavedUser()){
            setLoading();
            checkResources();
        }else{
            setBtnFbLogin();
        }


    }

    private boolean isSavedUser(){
        boolean isSaved = false;
        String userJson = prefer.getValue(Const.PREF_SAVED_USER, null);
        if(userJson != null){
            isSaved = true;
        }
        return isSaved;
    }


    private void setBtnFbLogin(){
        viewAnim.fadeAnimation(linearLogin, false);
        linearLogin.setVisibility(View.VISIBLE);
        Button btn_fb_login = (Button)findViewById(R.id.btn_fb_login);
        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFBLogin();
            }
        });
    }

    private void setLoading(){
        linearLogin.setVisibility(View.INVISIBLE);
        viewAnim.fadeAnimation(linearLoading, false);
        linearLoading.setVisibility(View.VISIBLE);
    }


    private void onFBLogin(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("email", "user_birthday"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();

                final String userId = loginResult.getAccessToken().getUserId();

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {

                        String user_name = null;
                        UserVo userVo = null;
                        CreateDoc params = null;

                        try {
                            user_name = user.get("name").toString();
                            userVo = new UserVo(userId, user_name);
                            params = new CreateDoc(Const.DATABASE_NAME, Const.COLLECTION_USER, userVo);

                        } catch (Exception e) {
                            Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HttpClient client = new HttpClient(StartActivity.this);

                        client.post("/users/joinOrLogin", params, new EventRegistration(new CallbackEvent() {
                            @Override
                            public void callbackMethod(Object obj) {
                                //Log.d("debug", obj.toString());

                                JSONObject resultObj = null;

                                try {
                                    resultObj = new JSONObject(obj.toString());

                                    String userJson = resultObj.get("user").toString();
                                    String iconsJson = resultObj.get("icon_list").toString();

                                    Log.d("debug", userJson);
                                    Log.d("debug", iconsJson);

                                    prefer.put(Const.PREF_ACTIVE_KEYWORDS, iconsJson);
                                    prefer.put(Const.PREF_SAVED_KEYWORDS, iconsJson);

                                    prefer.put(Const.PREF_SAVED_USER, userJson);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                setLoading();
                                checkResources();
                            }
                        }));


                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(StartActivity.this, "로그인이 취소되었습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.activateApp(this);
    }

    private void moveToMain(){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(StartActivity.this, CateListActivity.class);
                startActivity(i);
                StartActivity.this.finish();
            }
        }, 3000);


    }

    private void checkResources(){
        HttpClient client = new HttpClient(this);

        Map query = new HashMap();
        query.put("file_list", getSavedIconResources());

        client.post("/iconresource/chkResources", query, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                String msg = object.toString();
                String[] filenames = (String[]) GsonConverter.fromJson(msg, String[].class);

                if (filenames.length > 0) {
                    mTextLoading.setText("UPDATE");
                    downloadIconResource(filenames);
                } else {
                    moveToMain();
                }

            }
        }));

    }

    private String[] getSavedIconResources(){
        File dir = new File(Const.ICON_SAVED_FOLDER);
        if(!dir.exists()){
            dir.mkdir();
        }
        return dir.list();
    }

    private void downloadIconResource(String[] filenames){

        Map<String, String> match = new HashMap<String, String>();

        for(int i=0; i<filenames.length; i++){
            String filename = filenames[i];
            String target = SERVER_ICONS_PATH+"/"+filename;
            String dest   = Const.ICON_SAVED_FOLDER + "/" + filename;

         match.put(target, dest);
        }

        AsyncFileDownloader downloader = new AsyncFileDownloader(match, true, new EventRegistration(new CallbackEvent() {
            @Override
            public void callbackMethod(Object object) {
                Logging.d("debug", object.toString());
                moveToMain();
            }
        }));
        downloader.start();
    }

}
