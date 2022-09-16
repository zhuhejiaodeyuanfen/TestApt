package com.example.secondmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.mylibrary.MyRouter;
import com.example.routerlib.Router;

@Router(value = "ThirdActivity")
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        findViewById(R.id.btnMove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRouter.navigate(ThirdActivity.this, "MainActivity", null);
            }
        });
    }
}