package com.example.android.sunshine;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;


/**
 * Created by nafeezq on 1/22/2017.
 */

public class MyWatchListenerService extends WearableListenerService {

    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    Long timeNew;
    String minTempNew;
    String maxTempNew;
    DataMap dataMap;
    byte[] weatherIconByteArrayReceived;



    @Override

    public void onDataChanged(DataEventBuffer dataEvents){

        Log.d("DATA_LAYER", "DATA LAYER CALLED");


                        for(DataEvent event: dataEvents){

                            if (event.getType()==DataEvent.TYPE_CHANGED){

                                String path = event.getDataItem().getUri().getPath();
                                if (path.equals(WEARABLE_DATA_PATH)) {

                                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                                    Log.d("WATCH_MESSAGE", "DataMap received on watch: " + dataMap);

                                    timeNew = dataMap.getLong("WEATHER_TIME");
                                    maxTempNew = dataMap.getString("WEATHER_MAX_TEMP");
                                    minTempNew = dataMap.getString("WEATHER_MIN_TEMP");
                                    weatherIconByteArrayReceived = dataMap.getByteArray("WEATHER_IMAGE_ID");



                                    if(timeNew!=null){

                                        Intent intent = new Intent("ACTION_WEATHER_CHANGED");
                                        intent.putExtra("timeExtra", timeNew);
                                        intent.putExtra("maxTempExtra", maxTempNew);
                                        intent.putExtra("minTempExtra", minTempNew);
                                        intent.putExtra("iconExtra",weatherIconByteArrayReceived);
                                        sendBroadcast(intent);

                                    }


                                }

                            }


                        }


        }


}

