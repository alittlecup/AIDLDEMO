package com.afollestad.aidlexample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hbl on 2017/5/16.
 */

public class Dog implements Parcelable {
    private int gender;
    private String name;

    public Dog(){}
    protected Dog(Parcel in) {
        gender = in.readInt();
        name = in.readString();
    }

    public static final Creator<Dog> CREATOR = new Creator<Dog>() {
        @Override
        public Dog createFromParcel(Parcel in) {
            return new Dog(in);
        }

        @Override
        public Dog[] newArray(int size) {
            return new Dog[size];
        }
    };

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gender);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "gender=" + gender +
                ", name='" + name + '\'' +
                '}';
    }
}
