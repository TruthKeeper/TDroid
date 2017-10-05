package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

public class MainActivity extends AppCompatActivity {
    private AppCompatImageView image;
    private AppCompatTextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (AppCompatImageView) findViewById(R.id.image);
        text = (AppCompatTextView) findViewById(R.id.text);

    }
}
