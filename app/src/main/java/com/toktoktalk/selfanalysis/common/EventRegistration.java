package com.toktoktalk.selfanalysis.common;

import com.toktoktalk.selfanalysis.common.CallbackEvent;
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
    public void doWork(String result){
        callbackEvent.callbackMethod(result);
    }

}
