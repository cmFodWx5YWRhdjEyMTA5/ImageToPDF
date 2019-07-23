package nl.changer.polypickerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
//import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BottomActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
AboutFragment.OnFragmentInteractionListener,ContactFragment.OnFragmentInteractionListener{

HomeFragment hf;
AboutFragment af;
ContactFragment cf;

    LinearLayout ll_mergepdf, ll_savedpdf, ll_lockpdf, ll_markpdf, ll_unlockpdf;
    ImageButton ll_createpdf;
    String displayName;
    // List<String> list = new ArrayList<>();
    List<InputStream> list = new ArrayList<InputStream>();
    List<String> pdflist = new ArrayList<>();
    Button btn_viewer, btn_app;
    Dialog d, dialog;
    EditText et_save_pdf_file;
    Button save_pdf_btn;
    String pdf;
    String root, watermarkpath;

    List<String> imagePathList=new ArrayList<>();
    List<String> imagesEncodedList=new ArrayList<>();
    String imageEncoded;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    hf = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, hf).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_dashboard:
                    af = new AboutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, af).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_notifications:
                    cf = new ContactFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, cf).addToBackStack(null).commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        hf = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, hf).addToBackStack(null).commit();







    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }







}
