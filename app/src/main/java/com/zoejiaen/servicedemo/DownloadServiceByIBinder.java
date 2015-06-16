package com.zoejiaen.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class DownloadServiceByIBinder extends Service {

    private static final String TAG = "DdServiceByIBinder";

    Timer mTimer;

    // curCount represents the progress of Download task
    int curCount = 0;

    public DownloadServiceByIBinder() {
        mTimer = new Timer();
    }

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        mTimer.schedule(new DownloadTask(),0,1000);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mTimer.cancel();
        return super.onUnbind(intent);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        DownloadServiceByIBinder getService() {
            // Return this instance of DownloadServiceByIBinder so clients can call public methods
            return DownloadServiceByIBinder.this;
        }
    }

    // called by AtyComByIBinder to get the current count
    public int getCurCount() {
        return curCount;
    }

    class DownloadTask extends TimerTask {
        @Override
        public void run() {
            if (curCount == 100){
                curCount =0;
            }
            curCount++;
            Log.i(TAG,"curCount = " + curCount);
        }
    }
}
