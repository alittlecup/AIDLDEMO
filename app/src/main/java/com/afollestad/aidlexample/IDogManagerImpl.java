package com.afollestad.aidlexample;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
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

    public static IDogManager adface(IBinder obj) {
        Log.d("TAG", "adInterface: local");
        if (obj == null) return null;
        IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null && iInterface instanceof IDogManager) {
            return (IDogManager) obj;
        }
        return new Proxy(obj);
    }





    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Log.d("TAG", "onTransact: local"+code);
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
                if(data.readInt()!=0){
                    dog=Dog.CREATOR.createFromParcel(data);
                }else{
                    dog=null;
                }
                this.addDog(dog);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy extends IDogManagerImpl {
        IBinder mRemote;

        Proxy(IBinder remote) {
            Log.d("TAG", "Proxy: local"+remote.getClass());
            mRemote = remote;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        @Override
        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public List<Dog> getDogList() throws RemoteException {
            Log.d("TAG", "getDogList: local");
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Dog> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(IDogManager.TRANSACTION_getDogList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Dog.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }

            return result;
        }

        @Override
        public void addDog(Dog dog) throws RemoteException {
            Log.d("TAG", "addDog: local");
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (dog != null) {
                    data.writeInt(1);
                    dog.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(IDogManager.TRANSACTION_addDog, data, reply, 0);
                reply.readException();
            } finally {
                data.recycle();
                reply.recycle();
            }
        }
    }
}
