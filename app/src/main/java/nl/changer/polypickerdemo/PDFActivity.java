package nl.changer.polypickerdemo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PDFActivity extends AppCompatActivity {
    TextView pdf_name;
    PDDocument doc = null;
    ImageView iv_back,iv_share;
    SharedPreferences sp_password;
    SharedPreferences.Editor editor_password;
    ArrayList<Uri> selectedStrings = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdf_name = findViewById(R.id.pdffile_name);
        iv_share = findViewById(R.id.iv_share);
        iv_back = findViewById(R.id.iv_back);



        //SCROLLBAR TO ENABLE SCROLLING
//        ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
//        pdfView.setScrollBar(scrollBar);
        //VERTICAL SCROLLING
//        scrollBar.setHorizontal(false);
        //SACRIFICE MEMORY FOR QUALITY
        //pdfView.useBestQuality(true)

        //UNPACK OUR DATA FROM INTENT
        Intent i = this.getIntent();
        String path = i.getExtras().getString("PATH");
        final String name = i.getExtras().getString("name");
        Log.d("msg", "createWatermark34: "+name);
        Log.d("msg", "createWatermark35: "+path);
        pdf_name.setText(name);

        //GET THE PDF FILE
        File file = new File(path);

        if (file.canRead()) {
            //LOAD IT
            pdfView.fromFile(file).defaultPage(1).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    //Toast.makeText(PDFActivity.this, String.valueOf(nbPages), Toast.LENGTH_LONG).show();
                }
            }).load();

        }
        pdfView.fromFile(file).swipeHorizontal(false);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename =name+".pdf";
                File filelocation = new File(Environment.getExternalStorageDirectory() + "/AVI PDF FORMS/", filename);
                Log.e("File Location", "onClick: " + filelocation);
                Uri path = Uri.fromFile(filelocation);

                Log.e("File Location", "URI PATH: " + path);
                Log.d("msg", "onClick1: " + path);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                String[] to = {"sourav@doozycod.in"};
                shareIntent.putExtra(Intent.EXTRA_EMAIL, to);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CUSTOMER FEEDBACK");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "PLEASE FIND THE ATTACHMENTS");
                shareIntent.putExtra(Intent.EXTRA_STREAM, path);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);

            }
        });

       /* pdfView.fromFile(file)
                .password("password")
                .load();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
