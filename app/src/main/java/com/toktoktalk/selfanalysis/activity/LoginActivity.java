package com.toktoktalk.selfanalysis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.toktoktalk.selfanalysis.R;
import com.toktoktalk.selfanalysis.model.UserVo;
import com.toktoktalk.selfanalysis.utils.ComPreference;

/**
 * Created by seogangmin on 2015. 8. 18..
 */
public class LoginActivity extends Activity{

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private UserVo user = new UserVo();
    private ComPreference prefer = new ComPreference(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_activity);

        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);


        callbackManager = CallbackManager.Factory.create();

        String logonUser = prefer.getValue("LOGON_USER", null);

        if(logonUser != null){
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {

                }
            });
        }else{
            
        }



        /*
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
        */




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
