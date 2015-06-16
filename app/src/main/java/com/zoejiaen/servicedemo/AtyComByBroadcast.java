package com.zoejiaen.servicedemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

// Activity that communicate with Service
// DownloadServiceByBroadCast with Android system Broadcast
// with Action named CustomAction ("my_custom_action")
public class AtyComByBroadcast extends Activity {
    private static final String TAG = "AtyComByBroadcast";
    public static final String CustomAction = "my_custom_action";
    private int curLoad = 0;
    Handler mHandler;
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
                Log.i(TAG, "current loading: "+curLoad);
                if(curLoad<0||curLoad>100){
                    Log.e(TAG, "ERROR: "+curLoad);
                    return;
                }
                mProgressBar.setProgress(curLoad);
                mTextView.setText(curLoad+"%");
            }
        };

    }

    public void onClick(View v) {
        mIntent = new Intent();
        mIntent.setClass(AtyComByBroadcast.this, DownloadServiceByBroadCast.class);
        switch (v.getId()){
            case R.id.btn_start:
                Log.i(TAG, "start button clicked...pid: " + Process.myPid());
                startService(mIntent);
                break;
            case R.id.btn_stop:
                stopService(mIntent);
                break;
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(CustomAction.equals(intent.getAction())){
                Log.i(TAG, "get the broadcast from DownLoadService...");
                int ERROR = -1;
                curLoad = intent.getIntExtra("CurrentLoading", ERROR);
                mHandler.sendMessage(mHandler.obtainMessage());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "register the broadcast receiver...");
        IntentFilter filter = new IntentFilter();
        filter.addAction(CustomAction);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "unregister the broadcast receiver...");
        unregisterReceiver(receiver);
        if (mIntent!=null) {
            stopService(mIntent);
        }
    }

}
