package com.example.research;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText searchQuery;
    ImageView search;

    ArrayList<String> pdfs = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> likes = new ArrayList<>();
    ArrayList<String> downloads = new ArrayList<>();
    ArrayList<String> rate = new ArrayList<>();

    ArrayList<Integer> totalRecom = new ArrayList<>();

    private static final int MY_PERMISIION_REQ_STORAGE = 1;


    int likeValue, rateValue, downloadValue;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISIION_REQ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(SearchActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private boolean validateQuery(String query) {
        return !query.trim().equals("");
    }

    boolean resp;
    int i = 0;
    int counter =0;


    private boolean searchQuery(String query) {
        if (validateQuery(query)) {
//            Toast.makeText(this, "Number of pdfs: " + pdfs.size(), Toast.LENGTH_SHORT).show();
            for (i = 0; i < pdfs.size(); i++) {
                if (pdfs.get(i).trim().toLowerCase().contains(query.trim().toLowerCase())) {
                    final String namedPDF = pdfs.get(i).substring(0, pdfs.get(i).length() - 4);
                    name.add(namedPDF);
                }
            }

        }

        if (name.size() > 0) {
            //Show recycler view
//            Toast.makeText(this, "Rec is showing", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.VISIBLE);
            return true;
        } else {
            //Hide recycler view
            recyclerView.setVisibility(View.GONE);
//            Toast.makeText(this, "Rec is not showing", Toast.LENGTH_SHORT).show();
            return false;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Firebase.setAndroidContext(this);
        listAssetFiles("pdfs");


        recyclerView = findViewById(R.id.recyclerView);
        searchQuery = findViewById(R.id.searchQuery);
        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=0;
                likes.clear();
                downloads.clear();
                rate.clear();
                totalRecom.clear();
                name.clear();
                if (searchQuery(searchQuery.getText().toString())) {
//                    Toast.makeText(getApplicationContext(),""+totalRecom,Toast.LENGTH_SHORT).show();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(new FileDocumentAdapter(name,  getAssets(), getApplicationContext(), new FileDocumentAdapter.ItemClickListener() {
                        @Override
                        public void onClick(int index,String name) {
                            startActivityViewer(name);
                        }
                    }));
                }

            }
        });


        if (ContextCompat.checkSelfPermission(SearchActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SearchActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SearchActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISIION_REQ_STORAGE);
            } else {
                ActivityCompat.requestPermissions(SearchActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISIION_REQ_STORAGE);
            }
        }
    }

    private boolean listAssetFiles(String path) {

        String[] list;
        try {
            list = getAssets().list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (!listAssetFiles(path + "/" + file))
                        return false;
                    else {
                        // This is a file
                        // TODO: add file name to an array list
                        pdfs.add(file);
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void startActivityViewer(String filename) {
        Intent my = new Intent(SearchActivity.this, Viewer.class);
        my.putExtra("filename", filename);
        startActivity(my);
    }

}
