package com.duyhoang.serviceibinderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by rogerh on 5/19/2018.
 */

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private static final int MAX = 100;
    private static final int MIN = 0;


    class MyServiceBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }

    private IBinder myBinder = new MyServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("OnStartCommand" , "NNNN: Thread id: " + Thread.currentThread().getId());
        mIsRandomGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberMachine();
            }
        }).start();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(getString(R.string.service_demo_tag), "In onDestroy service - Thread id: " + Thread.currentThread().getId());
        stopRandomNumberMachine();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(getString(R.string.service_demo_tag), "Binding service");
        return myBinder;
    }


    private void startRandomNumberMachine() {
        while(mIsRandomGeneratorOn){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(getString(R.string.service_demo_tag), e.getMessage());
            }
            mRandomNumber = new Random().nextInt(MAX) + MIN;
            Log.i(getString(R.string.service_demo_tag), "In onStartCommand - Thread id: "
                    + Thread.currentThread().getId() + "- Random Number: " + mRandomNumber);
        }
    }

    private void stopRandomNumberMachine(){
        mIsRandomGeneratorOn = false;
    }

    public int getRandomNumber(){
        return mRandomNumber;
    }


}
