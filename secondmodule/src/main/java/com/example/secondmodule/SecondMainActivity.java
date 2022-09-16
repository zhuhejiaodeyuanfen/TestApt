package com.example.secondmodule;

import android.os.Bundle;

import com.example.mylibrary.MyRouter;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import com.example.routerlib.Router;
import com.example.secondmodule.databinding.ActivityMain2Binding;
import com.example.secondmodule.databinding.ActivityMainBinding;


@Router(value = "SecondMainActivity")
public class SecondMainActivity extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setContentView(R.layout.activity_main_2);


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRouter.navigate(SecondMainActivity.this, "MainActivity", null);
            }
        });
    }


}