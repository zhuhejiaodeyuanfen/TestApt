package com.example.testapt;

import android.content.Intent;
import android.os.Bundle;

import com.example.mylibrary.MyRouter;
import com.example.routerlib.Router;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.secondmodule.SecondMainActivity;
import com.example.secondmodule.ThirdActivity;
import com.example.testapt.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class FirstMainActivity extends AppCompatActivity {

    private static class MyHandler extends Handler {
        private WeakReference<FirstMainActivity> weakReference;

        //弱引用的方式,在gc时,activity可被回收
        public MyHandler(WeakReference<FirstMainActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

    private ActivityMainBinding binding;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //如果页面没到超时时间却关闭,此时引用链仍然存在，出现内存泄露
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tvShow.setText("测试延时消息");

            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}