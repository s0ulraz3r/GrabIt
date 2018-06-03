package com.example.varun.grabit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.varun.grabit.R;

public class GrabItActivity extends AppCompatActivity {

    private Button buttonDiveIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabit);

        buttonDiveIn = (Button) findViewById(R.id.btnGrabItDiveIn);

        buttonDiveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }
}
