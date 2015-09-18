package com.toktoktalk.selfanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.common.BaseActivity;
import com.toktoktalk.selfanalysis.common.CallbackEvent;
import com.toktoktalk.selfanalysis.common.Const;
import com.toktoktalk.selfanalysis.common.EventRegistration;
import com.toktoktalk.selfanalysis.common.HttpClient;
import com.toktoktalk.selfanalysis.model.UserVo;
import com.toktoktalk.selfanalysis.apis.CreateDoc;
import com.toktoktalk.selfanalysis.utils.ComPreference;
import com.toktoktalk.selfanalysis.utils.ViewAnimator;

import org.json.JSONObject;

import java.util.Arrays;


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

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }


    private void init(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_start);

        linearLoading = (LinearLayout)findViewById(R.id.linearLoading);
        linearLogin   = (LinearLayout)findViewById(R.id.linearLogin);

        if(isSaved()){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToMain();
                }
            }, 3000);

        }else{
            showLoginBtn();
        }

    }

    private boolean isSaved(){
        boolean isSaved = false;
        String userJson = prefer.getValue(Const.PREF_SAVED_USER, null);
        if(userJson != null){
            isSaved = true;
        }
        return isSaved;
    }

    private void showLoginBtn(){
        viewAnim.fadeAnimation(linearLoading, true);
        linearLoading.setVisibility(View.INVISIBLE);
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

                        Toast.makeText(StartActivity.this, userId, Toast.LENGTH_SHORT).show();

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
                                Log.d("debug",obj.toString());
                                prefer.put(Const.PREF_SAVED_USER, obj.toString());
                                moveToMain();
                            }
                        }));


                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(StartActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
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
        Intent i = new Intent(this, CateListActivity.class);
        startActivity(i);
        this.finish();
    }

}
