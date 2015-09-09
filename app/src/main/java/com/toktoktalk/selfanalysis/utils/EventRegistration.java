package com.toktoktalk.selfanalysis.utils;

import com.turbomanage.httpclient.HttpResponse;

/**
 * Created by seogangmin on 2015. 9. 8..
 */
public class EventRegistration {

    private CallbackEvent callbackEvent;

    public EventRegistration(CallbackEvent event){
        callbackEvent = event;
    }

    public void doWork(HttpResponse response){
        callbackEvent.callbackMethod(response);
    }

}
