/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.util.Log;
import android.view.SurfaceHolder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't
 * shown. On devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient
 * mode. The watch face is drawn with less contrast in mute mode.
 */
public class MyWatchFace extends CanvasWatchFaceService {

    public static final String INTENT_FILTER_STRING = "ACTION_WEATHER_CHANGED";


    @Override
    public Engine onCreateEngine() {



        return new Engine();
    }


    private class Engine extends CanvasWatchFaceService.Engine {

        Paint mTextPaint;
        Float mTextXOffsetTime;
        Float mTextXOffsetDate;
        Float mTextXOffsetTemp;
        Long myTime;
        String maxTempString;
        String minTempString;
        byte[]iconBytes;
        Bitmap bitmapWeather;

        @Override

        public void onCreate(SurfaceHolder holder){
            super.onCreate(holder);


            mTextPaint = new Paint();
            mTextPaint.setTextSize(40);
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setAntiAlias(true);


            //for centering the Text position
            mTextXOffsetTime = mTextPaint.measureText("12:12 PM") / 2;
            mTextXOffsetDate = mTextPaint.measureText("Thu,Feb 2, '17") / 2;
            mTextXOffsetTemp = mTextPaint.measureText("18c")/2;


//            mTextYOffsetTime = (mTextPaint.ascent() + mTextPaint.descent()) / 2;
//            mTextYOffsetDate = (mTextPaintDate.ascent() + mTextPaint.descent()) / 2;


            registerWeatherReceiver();


        }

        private void registerWeatherReceiver() {

            Log.d("WATCH_REGISTERED", "Receiver Registered");
            IntentFilter intentFilter = new IntentFilter(INTENT_FILTER_STRING);
            registerReceiver(weatherReceiver,intentFilter);

        }

        private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

//                if(INTENT_FILTER_STRING.equals(intent.getAction())){

                    myTime = intent.getLongExtra("timeExtra",1L);
                    maxTempString = intent.getStringExtra("maxTempExtra");
                    minTempString = intent.getStringExtra("minTempExtra");
                    iconBytes = intent.getByteArrayExtra("iconExtra");

                    if(iconBytes!=null){

                        bitmapWeather = BitmapFactory.decodeByteArray(iconBytes,0,iconBytes.length);
                    }

                    Log.d("WATCH_MESSAGE", myTime.toString());
                    Log.d("WATCH_MESSAGE", minTempString);
                    Log.d("WATCH_MESSAGE", maxTempString);

                    invalidate();
//                }

            }
        };

        @Override

        public void onDraw(Canvas canvas,Rect bounds){


            Log.d("WATCH_MESSAGE", "On Draw Called " + myTime);

            if(myTime==null){

                canvas.drawText("",bounds.centerX(),bounds.centerY(),mTextPaint);

            }else{

                SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm a");
                SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, MMM d, ''yy");
                Date result = new Date(myTime);
                String onlyTime = sdfTime.format(result);
                String onlyDate = sdfDate.format(result);

                String height = Integer.toString(canvas.getHeight());
                String width  = Integer.toString(canvas.getWidth());

                Log.d("WATCH_MESSAGE", height + " " + width);

                canvas.drawText(onlyTime,canvas.getWidth()/2-mTextXOffsetTime,canvas.getHeight()/4,mTextPaint);
                canvas.drawText(onlyDate,canvas.getWidth()/2-mTextXOffsetDate,canvas.getHeight()/2,mTextPaint);
                mTextPaint.setTextSize(50);
                canvas.drawText(maxTempString,canvas.getWidth()/1.8f,canvas.getHeight()/1.25f,mTextPaint);
                mTextPaint.setTextSize(30);
                canvas.drawText(minTempString,canvas.getWidth()/1.7f,canvas.getHeight()/1.1f,mTextPaint);
                canvas.drawBitmap(bitmapWeather,canvas.getWidth()/4.5f,canvas.getHeight()/1.6f,mTextPaint);

            }

        }

        private void unRegisterWeatherReceiver() {

            unregisterReceiver(weatherReceiver);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unRegisterWeatherReceiver();

        }


    }

}

