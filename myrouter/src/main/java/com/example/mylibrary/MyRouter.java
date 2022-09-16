package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyRouter {
    //页面路由表
    private static final List<Path> paths = new ArrayList<>();

    //将页面插入到路由表中
    public static void add(String path, Class<? extends Activity> activity) {
        paths.add(new Path(path, activity));
    }

    public static void navigate(Context context, String path, Bundle bundle) {
        //遍历路由表,进行uri的匹配,匹配成功,则启动对面的Activity页面
        for (Path p : paths) {
            if (p.value.equals(path)) {
                Intent intent = new Intent(context, p.getActivity());
                intent.putExtra(path, bundle);
                Log.i("navigate", path+"====="+p.getActivity());
                context.startActivity(intent);
                return;
            }
        }
    }


    /**
     * 自定义路由记录
     */
    public static class Path {
        private final String value;
        private final Class<? extends Activity> activity;

        public Path(String value, Class<? extends Activity> activity) {
            this.value = value;
            this.activity = activity;
        }

        public Class<? extends Activity> getActivity() {
            return activity;
        }
    }

}
