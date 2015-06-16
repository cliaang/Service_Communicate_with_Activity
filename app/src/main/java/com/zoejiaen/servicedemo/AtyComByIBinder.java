package com.zoejiaen.servicedemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AtyComByIBinder extends Activity {
    DownloadServiceByIBinder mService;
    boolean mBound = false;


    ProgressBar mProgressBar;
    TextView mTextView;
    Handler mHandler;
    Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_service);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.tv_progress);

        Button start = (Button) findViewById(R.id.btn_start);
        start.setText("Bind Service");
        Button stop = (Button) findViewById(R.id.btn_stop);
        stop.setText("Unbind Service");

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int couLoad = mService.getCurCount();
                mProgressBar.setProgress(couLoad);
                mTextView.setText(couLoad+"%");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    private void clean() {
        if(mBound){
            unbindService(mConnection);
            mBound = false;
        }
        mThread.interrupt();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                Intent intent = new Intent(this,DownloadServiceByIBinder.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_stop:
                clean();
                break;
        }

    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownloadServiceByIBinder.LocalBinder binder = (DownloadServiceByIBinder.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (mThread!=null){
                mThread.interrupt();
            }
            mThread = new FreshThread();
            mThread.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    class FreshThread extends Thread{
        @Override
        public void run() {
            while(true){
                mHandler.sendMessage(mHandler.obtainMessage());
                if(interrupted()){
                    break;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }

            }
        }
    }
}
