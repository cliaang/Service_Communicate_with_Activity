package com.zoejiaen.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


// Activity that communicate with Service DownloadServiceByFile
// with the same SharedPreferences named SP_NAME ("cur_count_sp")

public class AtyComByFile extends Activity {
    private static final String TAG = "AtyComByFile";
    public static final String SP_NAME = "cur_count_sp";
    public static final String COUNT_NAME = "cur_count";

    Handler mHandler;
    FreshThread mThread ;

    ProgressBar mProgressBar;
    TextView mTextView;

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_service);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTextView = (TextView) findViewById(R.id.tv_progress);

        Button start = (Button) findViewById(R.id.btn_start);
        start.setText("Start Service");
        Button stop = (Button) findViewById(R.id.btn_stop);
        stop.setText("Stop Service");

         mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SharedPreferences sp = getSharedPreferences(AtyComByFile.SP_NAME,MODE_PRIVATE);
                int couLoad = sp.getInt(COUNT_NAME, 0);
                mProgressBar.setProgress(couLoad);
                mTextView.setText(couLoad+"%");
            }
        };
        mThread = new FreshThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    private void clean() {
        stopService(mIntent);
        mThread.interrupt();
        SharedPreferences sp = getSharedPreferences(AtyComByFile.SP_NAME,MODE_PRIVATE);
        sp.edit().putInt(COUNT_NAME,0).apply();
    }

    public void onClick(View v) {
        mIntent = new Intent();
        mIntent.setClass(AtyComByFile.this, DownloadServiceByFile.class);
        switch (v.getId()){
            case R.id.btn_start:
                Log.i(TAG, "start button clicked...pid: " + Process.myPid());
                startService(mIntent);
                if(!mThread.isAlive()){
                    mThread = new FreshThread();
                    mThread.start();
                }
                break;
            case R.id.btn_stop:
                clean();
                break;
        }
    }

    class FreshThread extends Thread{
        @Override
        public void run() {
            while(true){
                mHandler.sendMessage(mHandler.obtainMessage());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                if(interrupted()){
                    break;
                }
            }
        }
    }
}
