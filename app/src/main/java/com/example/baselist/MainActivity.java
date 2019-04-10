package com.example.baselist;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences largeTextSharedPref = getSharedPreferences("LargeText", MODE_PRIVATE);
        SharedPreferences.Editor largeTextEditor = largeTextSharedPref.edit();
        largeTextEditor.putString("LARGE_TEXT",getString(R.string.large_text));
        largeTextEditor.apply();

        List<Map<String, String>> mapList = new ArrayList<>();
        String[] arrayContent = largeTextSharedPref.getString("LARGE_TEXT", "").split("\n\n");

        for (String item:arrayContent) {
            int arrayLength = item.length();
            Map<String, String> newMap = new HashMap<>();
            newMap.put("text", item);
            newMap.put("length", Integer.toString(arrayLength));
            mapList.add(newMap);
        }

        ListView list = findViewById(R.id.list);
        List<Map<String, String>> values = mapList;
        SimpleAdapter listContentAdapter = createAdapter(values);
        list.setAdapter(listContentAdapter);
    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> mapList) {
        return new SimpleAdapter(this,
                mapList,
                R.layout.simple_list_items,
                new String[]{"text", "length"},
                new int[]{R.id.text, R.id.length}
        );
    }
}
