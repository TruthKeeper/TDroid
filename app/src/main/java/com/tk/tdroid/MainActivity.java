package com.tk.tdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<A> list = new ArrayList<>();
        list.add(new A(1));
        list.add(new A(4));
        list.add(new A(2));
        list.add(new A(6));
        list.add(new A(-1));
        list.add(new A(3));
        int index = Collections.binarySearch(list, new A(-1), new Comparator<A>() {
            @Override
            public int compare(A o1, A o2) {
                if (o1.i > o2.i) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        A[] as = new A[]{new A(1), new A(4), new A(2), new A(6), new A(-1), new A(3)};
        int index1= Arrays.binarySearch(as,new A(-1));

    }

    public static class A {
        public int i = 0;

        public A(int i) {
            this.i = i;
        }
    }
}
