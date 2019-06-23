package com.example.research;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SelectFields extends AppCompatActivity {

    RecyclerView selectFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_fields);

//        selectFields = findViewById(R.id.selectFields);
//
//        selectFields.setLayoutManager(new LinearLayoutManager(this));
//        selectFields.setAdapter(new FieldsAdapter());

    }

    public void proceed(View view) {
        startActivity(new Intent(SelectFields.this,SearchActivity.class));
        finish();
    }
}
