package com.tk.tdroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tk.tdroiddemo.aop.annotation.CheckNetwork;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @CheckNetwork
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "HHH", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
