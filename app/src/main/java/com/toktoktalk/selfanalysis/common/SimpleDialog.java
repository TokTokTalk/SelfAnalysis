package com.toktoktalk.selfanalysis.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toktoktalk.selfanalysis.R;

/**
 * Created by seogangmin on 2015. 9. 12..
 */
public class SimpleDialog extends Dialog {

    private Context mCtx;
    private TextView mTitleView;
    private EditText mContentView;
    private Button mBtnOk;

    private String mTitle;
    private EventRegistration mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.simple_dialog);

        initComponent();
        setComponentEvent();
        setTitle(mTitle);
    }

    public SimpleDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public SimpleDialog(Context context, String title, EventRegistration callback) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mCallback = callback;
    }

    private void setTitle(String title){
        mTitleView.setText(title);
    }

    private void initComponent(){
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mContentView  = (EditText) findViewById(R.id.ed_cate_name);
        mBtnOk  = (Button) findViewById(R.id.btn_ok);

    }

    private void setComponentEvent(){
        mBtnOk.setOnClickListener(listener);

        mContentView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    mBtnOk.callOnClick();
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    SimpleDialog.this.hide();
                    return true;
                } else{
                    return false;
                }
            }
        });

    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String content = mContentView.getText().toString();

            if(content.equals("") || content == null){
                Toast.makeText(SimpleDialog.this.getContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                mCallback.doWork(content);
                SimpleDialog.this.hide();
            }
        }
    };


}
