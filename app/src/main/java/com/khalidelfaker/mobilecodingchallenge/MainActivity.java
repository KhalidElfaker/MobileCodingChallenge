package com.khalidelfaker.mobilecodingchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("123");
        strings.add("456");
        strings.add("789");
        strings.add("951");
        strings.add("951");
        strings.add("951");
        strings.add("951");
        strings.add("123");
        strings.add("456");
        strings.add("789");
        strings.add("951");
        strings.add("951");
        strings.add("951");
        strings.add("951");

        ListView listView = findViewById(R.id.my_list_view);
        listView.setAdapter(new MyListViewAdapter(this.getApplicationContext(),strings));
    }
}
