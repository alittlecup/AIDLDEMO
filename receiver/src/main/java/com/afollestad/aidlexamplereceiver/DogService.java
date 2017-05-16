package com.afollestad.aidlexamplereceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.afollestad.aidlexample.Dog;
import com.afollestad.aidlexample.IDogManagerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbl on 2017/5/16.
 */

public class DogService extends Service {
    private List<Dog> dogs = new ArrayList<>();
    private IBinder binder = new IDogManagerImpl() {
        @Override
        public List<Dog> getDogList() throws RemoteException {
            return dogs;
        }

        @Override
        public void addDog(Dog dog) throws RemoteException {
            dogs.add(dog);
            Log.d("TAG", "addDog: remote "+dogs.toString());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log("Received binding.");
        return binder;
    }

    private void log(String message) {
        Log.v("MainService", message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Received start command.");
        return START_STICKY;
    }

}
