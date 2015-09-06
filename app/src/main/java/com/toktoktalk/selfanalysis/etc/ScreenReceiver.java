package com.toktoktalk.selfanalysis.etc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.toktoktalk.selfanalysis.activity.LockScreenActivity;

/**
 * Created by seogangmin on 2015. 8. 9..
 */
public class ScreenReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent i = new Intent(context, LockScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
