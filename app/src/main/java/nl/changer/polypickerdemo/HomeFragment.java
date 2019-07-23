package nl.changer.polypickerdemo;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);


        root = Environment.getExternalStorageDirectory() + "/AVI PDF FORMS/";
        File path = new File(root);
        if (!path.exists()) {
            path.mkdirs();
//            waterpath.mkdirs();

        }

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        }
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        ll_createpdf = v.findViewById(R.id.ll_createpdf);
        ll_mergepdf = v.findViewById(R.id.ll_mergepdf);
        ll_savedpdf = v.findViewById(R.id.ll_savedpdf);
        ll_lockpdf = v.findViewById(R.id.ll_lockpdf);
        ll_markpdf = v.findViewById(R.id.ll_markpdf);
        ll_unlockpdf = v.findViewById(R.id.ll_unlockpdf);

        ll_createpdf.setOnClickListener(this);
        ll_mergepdf.setOnClickListener(this);
        ll_savedpdf.setOnClickListener(this);
        ll_lockpdf.setOnClickListener(this);
        ll_markpdf.setOnClickListener(this);
        ll_unlockpdf.setOnClickListener(this);
        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_createpdf:
                startActivity(new Intent(getContext(), CreatePdfActivity.class));
                break;

            case R.id.ll_mergepdf:
                Intent filesIntent;
                filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
                filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
                filesIntent.setType("application/pdf");  //use image/* for photos, etc.
                startActivityForResult(filesIntent, 1);
                break;

            case R.id.ll_savedpdf:
                startActivity(new Intent(getContext(), PdfFilesActivity.class));
                break;

            case R.id.ll_lockpdf:
                startActivity(new Intent(getActivity(), LockPdfActivity.class));
                break;
            case R.id.ll_markpdf:
                startActivity(new Intent(getActivity(), AddWaterMark.class));
                break;

            case R.id.ll_unlockpdf:
                startActivity(new Intent(getActivity(), UnlockPdfActivity.class));

                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
        String path = "";
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                ClipData clipData = data.getClipData();

                //null and not null path
                if (clipData == null) {
                    path += data.getData().toString();
                } else {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        path += uri.toString() + "\n";
                        mArrayUri.add(uri);

                        imagePathList.add(getImagePath(uri));
                    }
                }
            }
        }

        for(int z=0;z<imagePathList.size();z++){
            Log.d("msg", "PDFLISTSS: "+imagePathList.get(z));
        }

        showDialogg(imagePathList);

    }

    public String getImagePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }

        cursor.close();

        return displayName;
    }

    public void showDialogg(final List<String> filepaths) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.save_as_dialog);
        dialog.show();
        et_save_pdf_file = dialog.findViewById(R.id.et_pdf_file_name);
        save_pdf_btn = dialog.findViewById(R.id.pdf_gen_btn);

        save_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_save_pdf_file.getText().toString().equals("")) {
                    et_save_pdf_file.setError("Please Enter Name");
                } else {
                    pdf = et_save_pdf_file.getText().toString();
                    try {
                        startMerge(pdf, filepaths);
                    } catch (Exception e) {
                    }
                    Log.e("PDF", "PDF GENERATED!");
                }

            }
        });
    }

    public void startMerge(String pdf, List<String> filepaths) {
        //Toast.makeText(this, ""+filepaths.get(0), Toast.LENGTH_SHORT).show();

        try {

            for (int i = 0; i < filepaths.size(); i++) {
                Log.d("msg", "startMerge: " + filepaths.get(i));
                list.add(new FileInputStream(new File(root + "/" +filepaths.get(i))));
            }
           /* list.add(new FileInputStream(new File(root + "/" + "ss.pdf")));
            list.add(new FileInputStream(new File(root + "/" + "kk.pdf")));*/
            OutputStream out = null;

            out = new FileOutputStream(new File(root + "/" + pdf + ".pdf"));
            // Resulting pdf

            doMerge(list, out);
            Toast.makeText(getActivity(), "Merge Pdf Successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Toast.makeText(this, "1"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("msg", "startMerge1: "+e.getMessage());
        } catch (DocumentException e) {
            e.printStackTrace();
            // Toast.makeText(this, "2"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("msg", "startMerge2: "+e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "2"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("msg", "startMerge3: "+e.getMessage());

        }
    }

    public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
                document.setPageSize(new Rectangle(page.getWidth(),page.getHeight()) );
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();




    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
