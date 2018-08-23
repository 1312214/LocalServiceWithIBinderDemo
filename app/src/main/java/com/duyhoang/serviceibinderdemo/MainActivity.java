package com.duyhoang.serviceibinderdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtShowing;
    Button btnStartService, btnStopService, btnBindService, btnUnbindService, btnGetNumber;

    private Intent serviceIntent;
    private MyService myService;
    private boolean isServiceBound;

    // Interface for monitoring the state of an application service.
    private ServiceConnection serviceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(getString(R.string.service_demo_tag), "Main thread UI- thread id:" + Thread.currentThread().getId());
        txtShowing = (TextView)findViewById(R.id.text_showing);
        btnStartService = (Button)findViewById(R.id.button_start_service);
        btnStopService = (Button)findViewById(R.id.button_stop_service);
        btnBindService = (Button)findViewById(R.id.button_bind_service);
        btnUnbindService = (Button)findViewById(R.id.button_unbind_service);
        btnGetNumber = (Button)findViewById(R.id.button_get_randomnumber);

        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnGetNumber.setOnClickListener(this);

        serviceIntent = new Intent(this, MyService.class);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_start_service: startService(serviceIntent);
                break;
            case R.id.button_stop_service: stopService(serviceIntent);
                break;
            case R.id.button_bind_service: bindMyService();
                break;
            case R.id.button_unbind_service: unbindMyService();
                break;
            case R.id.button_get_randomnumber: setRandomNumber();
                break;
        }
    }

    private void setRandomNumber() {
        if(isServiceBound)
            txtShowing.setText("Random number: " + myService.getRandomNumber());
        else
            txtShowing.setText("No content");

    }

    private void bindMyService() {

        if(serviceConnection == null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder)iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindMyService() {
        Log.i(getString(R.string.service_demo_tag), "unbinding service");
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }
}
