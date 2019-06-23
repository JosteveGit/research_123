package com.example.research;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class Viewer extends AppCompatActivity {

    PDFView pdfView;
    TextView pdf;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        pdfView = findViewById(R.id.viewpdf);
        back = findViewById(R.id.back);
        pdf = findViewById(R.id.pdf);
        Intent getdata = getIntent();
        String pdfName = getdata.getStringExtra("filename");

        pdf.setText(pdfName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pdfView.fromAsset("pdfs/"+pdfName+".pdf").load();
    }
}
