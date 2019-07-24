package nl.changer.polypickerdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.model.Image;
import nl.changer.polypicker.utils.ImageInternalFetcher;

public class CreatePdfActivity extends AppCompatActivity {

    private static final String TAG = CreatePdfActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
    ImageView image_click;
    Button btn_createe_pdf;
    private Context mContext;
    EditText ed_pdf_name;
    String pdf;
    private ScrollView mScrollView;
    private GridLayout mSelectedImagesContainer;
    Set<Uri> mMedia = new HashSet<Uri>();
    HashSet<Image> mMediaImages = new HashSet<>();
    List<String> fileslist = new ArrayList<String>();
    List<String> imagelist = new ArrayList<String>();
    String p;
    File f, newfile;
    int z, k, sizeimages;
    ImageView iv_backk;
    Button btn_clear;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = CreatePdfActivity.this;
        iv_backk = findViewById(R.id.iv_backk);
        btn_clear = findViewById(R.id.btn_clear);
      //  iv2_click = findViewById(R.id.iv2_click);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        p = Environment.getExternalStorageDirectory() + "/AVI PDF FORMS/";
        File path = new File(p);
        if (!path.exists()) {
            path.mkdirs();
        }


        image_click = findViewById(R.id.iv_click);
        ed_pdf_name = findViewById(R.id.ed_pdf_name);
        btn_createe_pdf = findViewById(R.id.btn_createe_pdf);
        mSelectedImagesContainer = findViewById(R.id.selected_photos_containerr);

        mScrollView = (ScrollView) findViewById(R.id.scroll_vieww);
        mScrollView.setSmoothScrollingEnabled(true);

        image_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNImages();
            }
        });

        btn_createe_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_pdf_name.getText().toString().isEmpty()) {
                    ed_pdf_name.setError("Enter PDF name");
                } else {
                    pdf = ed_pdf_name.getText().toString();
                    try {
                        createPdf(pdf);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        iv_backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedImagesContainer != null) {
                    mSelectedImagesContainer.removeAllViews();
                }

            }
        });

      /*  mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        View getImages = findViewById(R.id.get_images);

        getImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getImages();
            }
        });

        View getNImages = findViewById(R.id.get_n_images);*/

       /* getNImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getNImages();
            }
        });*/


    }

    private void getNImages() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.orange)
                .setSelectionLimit(200)    // set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (resuleCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_N_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                int[] parcelableOrientations = intent.getIntArrayExtra((ImagePickerActivity.EXTRA_IMAGE_ORIENTATIONS));
                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                int[] orientations = new int[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                System.arraycopy(parcelableOrientations, 0, orientations, 0, parcelableOrientations.length);

                if (uris != null) {
                    /*for (Uri uri : uris) {
                        Log.i(TAG, " uri: " + uri);
                        mMedia.add(uri);

                    }*/
                    for (int i = 0; i < orientations.length; i++) {
                        mMediaImages.add(new Image(uris[i], orientations[i]));

                    }


                    Iterator<Image> iteratorr = mMediaImages.iterator();
                    while (iteratorr.hasNext()) {
                        Image imagee = iteratorr.next();
                        if (!imagee.mUri.toString().contains("content://")) {
                            // probably a relative uri

                            f = new File(imagee.mUri.toString());
                            Log.d(TAG, "Result5: " + f);
                            fileslist.add(String.valueOf(f));


                        }
                    }
                    image_click.setVisibility(View.GONE);
                    showMedia();
                }
            }
        }
    }

    private void showMedia() {
        // Remove all views before
        // adding the new ones.

        //mSelectedImagesContainer.removeAllViews();
       int imageadd = R.drawable.addiconnew;
       File ff=new File(String.valueOf(imageadd));
        fileslist.add(String.valueOf(ff));


       /* iv2_click.setVisibility(View.VISIBLE);
        iv2_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNImages();
            }
        });*/
        Iterator<Image> iterator = mMediaImages.iterator();
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        while (iterator.hasNext()) {
            Image image = iterator.next();

            // showImage(uri);
            Log.i(TAG, " uri: " + image);
            if (mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);

            // View removeBtn = imageHolder.findViewById(R.id.remove_media);
            // initRemoveBtn(removeBtn, imageHolder, uri);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            ImageView delete = (ImageView) imageHolder.findViewById(R.id.ivv_delete);
            // FrameLayout layoutframee = (FrameLayout) imageHolder.findViewById(R.id.layoutframee);


            if (!image.mUri.toString().contains("content://")) {
                // probably a relative uri
                image.mUri = Uri.fromFile(new File(image.mUri.toString()));
            }

            imageFetcher.loadImage(image.mUri, thumbnail, image.mOrientation);

            mSelectedImagesContainer.addView(imageHolder);

            for (z = 0; z < fileslist.size(); z++) {
                imageHolder.setOnLongClickListener(new LongPressListener());

            }


            mSelectedImagesContainer.setOnDragListener(new DragListener());

            // set the dimension to correctly
            // show the image thumbnail.
            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) imageHolder.getLayoutParams();
            layoutParams.setMargins(5,5,5,5);
            imageHolder.requestLayout();
//            FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            parentParams.setMargins(5,5,5,5);
//            mSelectedImagesContainer.setLayoutParams(parentParams);




            int childCount = mSelectedImagesContainer.getChildCount();
            for (k = 0; k < childCount; k++) {
                final FrameLayout container = (FrameLayout) mSelectedImagesContainer.getChildAt(k);

                final int index = ((ViewGroup) container.getParent()).indexOfChild(container);

                container.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent i = new Intent(CreatePdfActivity.this, ImageZoom.class);
                        i.putExtra("IMAGEPATH", fileslist.get(index));
                        startActivity(i);
                    }
                });


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // ((LinearLayout) linearLayout.getChildAt(i)).removeViewAt(j);
                        View v = ((FrameLayout) mSelectedImagesContainer.getChildAt(index));
                        mSelectedImagesContainer.removeView(v);


                    }
                });


                //sizeimages=index;
                //  Log.d(TAG, "showMedia76: "+container.getId());
            }

           // thumbnail.setImageResource(Integer.parseInt(fileslist.get(sizeimages+2)));



        }
    }

    public void createPdf(String pdfFileName) throws IOException, DocumentException {

        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(fileslist.get(0));
        Document document = new Document(img);
        PdfWriter.getInstance(document, new FileOutputStream(p + "/" + pdfFileName + ".pdf"));
        document.open();
        for (String image : fileslist) {
            img = com.itextpdf.text.Image.getInstance(String.valueOf(image));
            document.setPageSize(img);
            document.newPage();
            img.setAbsolutePosition(0, 0);
            document.add(img);
        }
        document.close();


        File file = new File(p + ed_pdf_name.getText().toString() + ".pdf");
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(CreatePdfActivity.this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
            }
        }

        //openPDFView(p + "/" + pdfFileName + ".pdf", ed_pdf_name.getText().toString());
        // Toast.makeText(this, "Generate PDF Sucessfully", Toast.LENGTH_SHORT).show();
        ed_pdf_name.setText("");

    }

    private void openPDFView(String path, String name) {
        Log.e("String Path", path);
        Intent i = new Intent(CreatePdfActivity.this, PDFActivity.class);
        i.putExtra("PATH", path);
        i.putExtra("name", name);
        startActivity(i);
    }

    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = mSelectedImagesContainer.getWidth() / mSelectedImagesContainer.getColumnCount();
        final int column = (int) (x / cellWidth);

        // calculate which row to move to
        final float cellHeight = mSelectedImagesContainer.getHeight() / mSelectedImagesContainer.getRowCount();
        final int row = (int) Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * mSelectedImagesContainer.getColumnCount() + column;
        if (index >= mSelectedImagesContainer.getChildCount()) {
            index = mSelectedImagesContainer.getChildCount() - 1;
        }

        return index;
    }

    class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    if (view == v) return true;
                    // get the new list index
                    final int index = calculateNewIndex(event.getX(), event.getY());

                    final int scrollY = mScrollView.getScrollY();
                    final Rect rect = new Rect();
                    mScrollView.getHitRect(rect);

                    if (event.getY() - scrollY > mScrollView.getBottom() - 250) {
                        startScrolling(scrollY, mSelectedImagesContainer.getHeight());
                    } else if (event.getY() - scrollY < mScrollView.getTop() + 250) {
                        startScrolling(scrollY, 0);
                    } else {
                        stopScrolling();
                    }

                    // remove the view from the old position
                    mSelectedImagesContainer.removeView(view);
                    // and push to the new
                    mSelectedImagesContainer.addView(view, index);
                    break;
                case DragEvent.ACTION_DROP:
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        }
    }

    private void startScrolling(int from, int to) {
        if (from != to && mAnimator == null) {
            mIsScrolling.set(true);
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new OvershootInterpolator());
            mAnimator.setDuration(Math.abs(to - from));
            mAnimator.setIntValues(from, to);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mScrollView.smoothScrollTo(0, (int) valueAnimator.getAnimatedValue());
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsScrolling.set(false);
                    mAnimator = null;
                }
            });
            mAnimator.start();
        }
    }

    private void stopScrolling() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }


}
