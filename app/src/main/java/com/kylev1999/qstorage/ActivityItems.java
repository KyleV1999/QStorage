package com.kylev1999.qstorage;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityItems extends AppCompatActivity {

    RecyclerView recyclerItems;
    ArrayList<Items> itemsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        findAllViews();

        LinearLayoutManager layoutManager = new LinearLayoutManager (this, RecyclerView.VERTICAL, false);
        recyclerItems.setLayoutManager(layoutManager);

        itemsArrayList = (ArrayList<Items>) getIntent().getExtras().getSerializable("list");

        recyclerItems.setAdapter(new ItemAdapter(itemsArrayList));

    }


    private void findAllViews() {
        recyclerItems = findViewById(R.id.recyclerView);
    }

}
