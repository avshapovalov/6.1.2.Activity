package com.example.baselist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences largeTextSharedPref;
    private SharedPreferences.Editor largeTextEditor;
    private ListView list;
    private List<Map<String, String>> values;
    private SimpleAdapter listContentAdapter;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<Integer> indexes = new ArrayList<>();
    public static final String keyIndexes = "keyIndexes";

    public void updateList(SharedPreferences largeTextSharedPref) {
        values.clear();
        String[] arrayContent = largeTextSharedPref.getString("LARGE_TEXT", "").split("\n\n");
        for (String item : arrayContent) {
            int arrayLength = item.length();
            Map<String, String> newMap = new HashMap<>();
            newMap.put("text", item);
            newMap.put("length", Integer.toString(arrayLength));
            values.add(newMap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        largeTextSharedPref = getSharedPreferences("LargeText", MODE_PRIVATE);
        largeTextEditor = largeTextSharedPref.edit();
        largeTextEditor.putString("LARGE_TEXT", getString(R.string.large_text));
        largeTextEditor.apply();

        list = findViewById(R.id.list);
        values = new ArrayList<>();
        updateList(largeTextSharedPref);
        listContentAdapter = createAdapter(values);
        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                indexes.add(position);
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });

        swipeLayout = findViewById(R.id.swiperefresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // Будет вызван, когда пользователь потянет список вниз
            @Override
            public void onRefresh() {
                updateList(largeTextSharedPref);
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

            }
        });
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(keyIndexes, indexes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        indexes = savedInstanceState.getIntegerArrayList(keyIndexes);
        for (int i = 0; i < indexes.size(); i++) {
            values.remove(indexes.get(i).intValue());
        }
        listContentAdapter.notifyDataSetChanged();
    }
}
