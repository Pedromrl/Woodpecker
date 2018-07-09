package com.example.pedrolemos.livrosfinal.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.karan.churi.PermissionManager.PermissionManager;
import com.vlonjatg.progressactivity.ProgressLinearLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;


public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private ProgressLinearLayout qrCameraLayout;
    private String TAG = "Scan Fragment";
    private int flag = 0;

    private PermissionManager permissionManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_scan, container, false);


        qrCameraLayout = fragmentView.findViewById(R.id.qrLayout);
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
      /*  myformat.add(BarcodeFormat.RSS_14);
        myformat.add(BarcodeFormat.CODE_39);
        myformat.add(BarcodeFormat.CODE_93);
        myformat.add(BarcodeFormat.CODE_128);
        myformat.add(BarcodeFormat.ITF);
        myformat.add(BarcodeFormat.CODABAR);
        myformat.add(BarcodeFormat.DATA_MATRIX);
        myformat.add(BarcodeFormat.PDF_417); */

        scannerView.setFormats(myformat);

        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(getActivity());


        return fragmentView;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;

        for (String item : granted)
            doMyAction();

        for (String item : denied)
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_DENIED) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(getContext());
                    //setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                View.OnClickListener errorClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // startInstalledAppDetailsActivity(getActivity());
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                };
                qrCameraLayout.showError(R.drawable.sad, "Oh, no!",
                        "You didn't accept the permission to use the Camera feature", "Try Again", errorClickListener);
            }
        }
    }

   /* public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    private void doMyAction() {
        ZXingScannerView.ResultHandler a = this;
        scannerView.resumeCameraPreview(a);
    }


    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        Intent intent = new Intent(getContext(), LivroActivity.class);
        intent.putExtra("ISBN", scanResult);
        Log.w(TAG, scanResult);
        startActivity(intent);
    }


}
