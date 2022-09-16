package com.example.testapt;

import android.app.Application;

import com.example.secondmodule.SecondModule;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        //让每个模块的路由都注册到路由表内
        RouterMapping.init();
        SecondModule.init();

    }
}
