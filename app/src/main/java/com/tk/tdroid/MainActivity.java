package com.tk.tdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tk.tdroid.utils.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.e(getClass().getSimpleName(), "123", true);
        Logger.json("{\"name\":\"test\"}");
        Logger.json(Logger.I, "this", "{\"name\":\"test\"}");
    }

}
