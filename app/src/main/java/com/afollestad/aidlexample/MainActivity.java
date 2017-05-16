package com.afollestad.aidlexample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IMainService mService;
    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLog = (TextView) findViewById(R.id.log);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dog dog = new Dog();
                dog.setGender(10);
                dog.setName("tom");
                try {
                    iDogManager.addDog(dog);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Dog> dogList = iDogManager.getDogList();
                    Log.d("TAG", "onClick: local"+dogList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        Intent serviceIntent = new Intent()
                .setComponent(new ComponentName(
                        "com.afollestad.aidlexamplereceiver",
                        "com.afollestad.aidlexamplereceiver.MainService"));
        mLog.setText("Starting service…\n");
        startService(serviceIntent);
        mLog.append("Binding service…\n");
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);

        Intent serviceIntent2 = new Intent()
                .setComponent(new ComponentName(
                        "com.afollestad.aidlexamplereceiver",
                        "com.afollestad.aidlexamplereceiver.DogService"));
        mLog.setText("Starting service…\n");
        startService(serviceIntent2);
        mLog.append("Binding service…\n");
        bindService(serviceIntent2, connection, BIND_AUTO_CREATE);
    }

    public IDogManager iDogManager;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iDogManager = IDogManagerImpl.adface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iDogManager=null;
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mLog.append("Service binded!\n");
            mService = IMainService.Stub.asInterface(service);

            performListing();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            // This method is only invoked when the service quits from the other end or gets killed
            // Invoking exit() from the AIDL interface makes the Service kill itself, thus invoking this.
            mLog.append("Service disconnected.\n");
        }
    };

    private void performListing() {
        mLog.append("Requesting file listing…\n");
        long start = System.currentTimeMillis();
        long end = 0;
        try {
            MainObject[] results = mService.listFiles("/sdcard/testing");
            end = System.currentTimeMillis();
            int index = 0;
            mLog.append("Received " + results.length + " results:\n");
            for (MainObject o : results) {
                if (index > 20) {
                    mLog.append("\t -> Response truncated!\n");
                    break;
                }
                mLog.append("\t -> " + o.getPath() + "\n");
                index++;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mLog.append("File listing took " + (((double) end - (double) start) / 1000d) + " seconds, or " + (end - start) + " milliseconds.\n");
        try {
            mService.exit();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
