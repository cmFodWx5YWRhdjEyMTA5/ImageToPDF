package nl.changer.polypickerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageZoom extends AppCompatActivity {
ImageView iv5_backk,iv_zoom;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        iv_zoom=findViewById(R.id.iv_zoom);
        iv5_backk=findViewById(R.id.iv5_backk);

        Intent i = this.getIntent();
        String imagepath = i.getExtras().getString("IMAGEPATH");

        iv5_backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        File imgFile = new  File(imagepath);
        Picasso.get().load(imgFile).into(iv_zoom);
        //Glide.with(getApplicationContext()).load(imgFile).into(imageview);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            iv_zoom.setScaleX(mScaleFactor);
            iv_zoom.setScaleY(mScaleFactor);
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
}
