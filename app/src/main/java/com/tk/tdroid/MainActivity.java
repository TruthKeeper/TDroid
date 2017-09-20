package com.tk.tdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Logger.e(getClass().getSimpleName(), "123", true);
//        Logger.json(Logger.Type.E,
//                Logger.getGlobalConfig().newBuilder()
//                        .tag("Json_Test")
//                        .logStackDepth(3),
//                "{\"name\":\"test\"}");
//        Logger.json(Logger.Type.I, null, "{\"name\":\"test\"}");
    }

}
