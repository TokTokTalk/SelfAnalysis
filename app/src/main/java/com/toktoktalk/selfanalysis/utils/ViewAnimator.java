package com.toktoktalk.selfanalysis.utils;

import android.content.Context;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;

import com.toktoktalk.selfanalysis.R;

/**
 * Created by seogangmin on 2015. 9. 7..
 */
public class ViewAnimator {

    private Context mCtx;

    public ViewAnimator(Context context){
        this.mCtx = context;
    }

    public void fadeAnimation(final View tv, boolean isfadeOut) {

        final Animation animationFade;

        tv.setAlpha(0f);

        if (isfadeOut) {

            animationFade = AnimationUtils.loadAnimation(mCtx, R.anim.fade_out);

        } else {

            animationFade = AnimationUtils.loadAnimation(mCtx,R.anim.fade_in);

        }

        Handler mhandler = new Handler();

        mhandler.postDelayed(new Runnable() {

            @Override

            public void run() {

                // TODO Auto-generated method stub

                tv.setAlpha(1f);

                tv.startAnimation(animationFade);

            }

        }, 0);

    }
}
