package com.zoejiaen.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


// DownloadService uses a continuous self_increasing integer
// to represent the progress of Download task.
public class DownloadServiceByBroadCast extends Service {
    private static final String TAG = "DdServiceByBroadCast";
    Intent intent;
    Timer mTimer;

    // i represents the progress of Download task
    int i = 0;

    public DownloadServiceByBroadCast() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "DownLoadService.onCreate().id: " + Process.myPid());
        intent = new Intent(AtyComByBroadcast.CustomAction);
        mTimer = new Timer();
        mTimer.schedule(new MyTimerTask(), 0 , 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if(i==100){
                i=0;
            }
            intent.putExtra("CurrentLoading", i);
            sendBroadcast(intent);
            i++;
            Log.e(TAG, "i= "+i);
        }
    }

}
