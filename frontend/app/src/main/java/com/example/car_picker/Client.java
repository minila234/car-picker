package com.example.car_picker;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Client {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if(instance==null){
            instance =new Retrofit.Builder().baseUrl("http://192.168.1.3:3001/").addConverterFactory(ScalarsConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()). build();
            
        }
        return instance;
    }


}
