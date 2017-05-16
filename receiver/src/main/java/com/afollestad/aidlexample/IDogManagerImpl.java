package com.afollestad.aidlexample;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

/**
 * Created by hbl on 2017/5/16.
 */

public abstract class IDogManagerImpl extends Binder implements IDogManager {

    public IDogManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }


    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Log.d("TAG", "onTransact: remote" + code);
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case IDogManager.TRANSACTION_getDogList:
                data.enforceInterface(DESCRIPTOR);
                List<Dog> dogList = this.getDogList();
                reply.writeNoException();
                reply.writeTypedList(dogList);
                return true;
            case IDogManager.TRANSACTION_addDog:
                data.enforceInterface(DESCRIPTOR);
                Dog dog;
                if (data.readInt() != 0) {
                    dog = Dog.CREATOR.createFromParcel(data);
                } else {
                    dog = null;
                }
                this.addDog(dog);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }
}
