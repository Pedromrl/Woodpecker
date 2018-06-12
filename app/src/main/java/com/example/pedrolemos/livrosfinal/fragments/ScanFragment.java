package com.example.pedrolemos.livrosfinal.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.BottomViewActivity;
import com.example.pedrolemos.livrosfinal.LivroActivity;
import com.example.pedrolemos.livrosfinal.R;
import com.example.pedrolemos.livrosfinal.model.BookResponse;
import com.example.pedrolemos.livrosfinal.model.Items;
import com.example.pedrolemos.livrosfinal.rest.ApiClient;
import com.example.pedrolemos.livrosfinal.rest.ApiInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;


public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private LinearLayout qrCameraLayout;
    private String TAG = "Scan Fragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView =  inflater.inflate(R.layout.fragment_scan, container, false);
        qrCameraLayout = (LinearLayout) fragmentView.findViewById(R.id.qrLayout);
        scannerView = new ZXingScannerView(getActivity());
        scannerView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        qrCameraLayout.addView(scannerView);

        List<BarcodeFormat> myformat = new ArrayList<>();
        myformat.add(BarcodeFormat.EAN_13);
        myformat.add(BarcodeFormat.EAN_8);
        myformat.add(BarcodeFormat.RSS_14);
        myformat.add(BarcodeFormat.CODE_39);
        myformat.add(BarcodeFormat.CODE_93);
        myformat.add(BarcodeFormat.CODE_128);
        myformat.add(BarcodeFormat.ITF);
        myformat.add(BarcodeFormat.CODABAR);
        myformat.add(BarcodeFormat.DATA_MATRIX);
        myformat.add(BarcodeFormat.PDF_417);

        scannerView.setFormats(myformat);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                Toast.makeText(getContext(), "Permission is granted", Toast.LENGTH_LONG).show();
            }else{
                requestPermission();
            }
        }

        return fragmentView;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA :
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(getContext());
                    //setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void doMyAction(){
        ZXingScannerView.ResultHandler a =  this;
        scannerView.resumeCameraPreview(a);
    }



    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        Intent intent = new Intent(getContext(), LivroActivity.class);
        intent.putExtra("ISBN", scanResult);
        Log.w(TAG, scanResult);
        startActivity(intent);
        //doMyAction();
       // getActivity().finish();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doMyAction();
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });

        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show(); */
    }


}
