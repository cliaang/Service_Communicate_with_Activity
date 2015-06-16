package com.zoejiaen.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class DownloadServiceByFile extends Service {
    private static final String TAG = "DownloadServiceByFile";

    private Timer mTimer = new Timer();
    private TimerTask timerTask = new DownloadTask();
    private int curCount = 0;

    public DownloadServiceByFile() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "DownloadServiceByFile.onCreate() called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"DownloadServiceByFile.onStartCommand() called");
        mTimer.schedule(timerTask, 0, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    class DownloadTask extends TimerTask{
        @Override
        public void run() {
            if (curCount == 100){
                curCount =0;
            }
            curCount++;
            SharedPreferences sp = getSharedPreferences(AtyComByFile.SP_NAME,MODE_PRIVATE);
            sp.edit().putInt(AtyComByFile.COUNT_NAME,curCount).apply();
            Log.d(TAG,"save file, curCount = "+curCount);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
