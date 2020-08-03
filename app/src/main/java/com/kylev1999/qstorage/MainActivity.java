package com.kylev1999.qstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kylev1999.qstorage.model.QRGeoModel;
import com.kylev1999.qstorage.model.QRURLModel;
import com.kylev1999.qstorage.model.QRVCardModel;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllViews(); //Find the views

        //Get perms
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(MainActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        scannerView.resumeCameraPreview(MainActivity.this);
        super.onResume();
    }



    private void findAllViews() {
        scannerView = findViewById(R.id.zxscan);
        result = findViewById(R.id.result_text);

    }

    @Override
    public void handleResult(Result rawResult) {
        //Do something with the result
        processRawResult(rawResult.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addnew:
                startActivity(new Intent(MainActivity.this,create_new_storage.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void processRawResult(String text) {
        if (text.startsWith("BEGIN:")) {
            String[] tokens = text.split("\n");
            QRVCardModel qrvCardModel = new QRVCardModel();
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].startsWith("BEGIN:")) {
                    qrvCardModel.setType(tokens[i].substring("BEGIN:".length())); //remove BEGIN: to get type
                } else if (tokens[i].startsWith("N:")) {
                    qrvCardModel.setName(tokens[i].substring("N:".length()));
                } else if (tokens[i].startsWith("ORG:")) {
                    qrvCardModel.setOrg(tokens[i].substring("ORG:".length()));
                } else if (tokens[i].startsWith("TEL:")) {
                    qrvCardModel.setTel(tokens[i].substring("TEL:".length()));
                } else if (tokens[i].startsWith("URL:")) {
                    qrvCardModel.setUrl(tokens[i].substring("URL:".length()));
                } else if (tokens[i].startsWith("EMAIL:")) {
                    qrvCardModel.setEmail(tokens[i].substring("EMAIL:".length()));
                } else if (tokens[i].startsWith("ADR:")) {
                    qrvCardModel.setAddress(tokens[i].substring("ADR:".length()));
                } else if (tokens[i].startsWith("SUMMARY:")) {
                    qrvCardModel.setSummary(tokens[i].substring("SUMMARY:".length()));
                } else if (tokens[i].startsWith("DTSTART:")) {
                    qrvCardModel.setDrstart(tokens[i].substring("DTSTART:".length()));
                } else if (tokens[i].startsWith("DTEND:")) {
                    qrvCardModel.setDtend(tokens[i].substring("DTEND:".length()));
                }

                result.setText(qrvCardModel.getType());

            }
        }
            else if (text.startsWith("http://") || text.startsWith("https://") || text.startsWith("www.") ){

            QRURLModel qrurlModel = new QRURLModel(text);
            result.setText(qrurlModel.getUrl());
            }
            else if (text.startsWith("geo:")){
            QRGeoModel qrGeoModel = new QRGeoModel();
            String delims = "[ , ?q= ]+";
            String tokens [] = text.split(delims);

            for (int i =0; i < tokens.length; i++)
            {
                if(tokens[i].startsWith(" geo:"))
                {
                    qrGeoModel.setLat(tokens[i].substring("geo:".length()));
                }
            }
            qrGeoModel.setLat(tokens[0].substring("geo:".length()));
            qrGeoModel.setLng(tokens[1]);
            qrGeoModel.setGeo_place(tokens[2]);

            result.setText(qrGeoModel.getLat()+"/"+qrGeoModel.getLng());

        }
            else{
                result.setText(text);
        }
        scannerView.resumeCameraPreview(MainActivity.this); //Restart camera after getting result

    }
}



