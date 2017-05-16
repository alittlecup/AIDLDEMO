package com.afollestad.aidlexample;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * Created by hbl on 2017/5/16.
 */

public interface IDogManager extends IInterface {
    String DESCRIPTOR = "com.afollestad.aidlexample.IDogManager";

    int TRANSACTION_getDogList= IBinder.FIRST_CALL_TRANSACTION+0;
    int TRANSACTION_addDog= IBinder.FIRST_CALL_TRANSACTION+1;
    List<Dog> getDogList() throws RemoteException;

    void addDog(Dog dog) throws RemoteException;
}
