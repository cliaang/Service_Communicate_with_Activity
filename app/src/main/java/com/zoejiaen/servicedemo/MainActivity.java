package com.zoejiaen.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_broadcast).setOnClickListener(this);
        findViewById(R.id.btn_SharedFile).setOnClickListener(this);
        findViewById(R.id.btn_IBinder).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_broadcast:
                startActivity(new Intent(MainActivity.this, AtyComByBroadcast.class));
                break;
            case R.id.btn_SharedFile:
                startActivity(new Intent(MainActivity.this, AtyComByFile.class));
                break;
            case R.id.btn_IBinder:
                startActivity(new Intent(MainActivity.this, AtyComByIBinder.class));
                break;
            default:
                break;
        }
    }
}
