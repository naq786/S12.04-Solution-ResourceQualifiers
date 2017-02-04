package com.example.android.sunshine.utilities;

import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


/**
 * Created by nafeezq on 1/22/2017.
 */

public class SendToDataLayerThread extends Thread {

    String path;
    DataMap dataMap;
    GoogleApiClient mApiClient;


    public SendToDataLayerThread(String p, DataMap data, GoogleApiClient apiClient){

        path = p;
        dataMap = data;
        mApiClient=apiClient;

    }

    public void run(){

        PutDataMapRequest putDMR = PutDataMapRequest.create(path);
        putDMR.getDataMap().putAll(dataMap);
        PutDataRequest request = putDMR.asPutDataRequest();
        DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mApiClient, request).await();
        if (result.getStatus().isSuccess()) {
            Log.d("WATCH_MESSAGE", "DataMap: " + dataMap + " sent successfully to data layer ");
        }
        else {
            // Log an error
            Log.d("WATCH_MESSAGE", "ERROR: failed to send DataMap to data layer");
        }

    }

}
