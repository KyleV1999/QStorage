package com.kylev1999.qstorage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.function.BiPredicate;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class generate_qr extends AppCompatActivity{

    public String newStorageName;
    ImageView QR_Image;
    TextView storageNameTitle;
    TextView title;
    Button review_button;
    Button save_button;
    Button finish_button;
    ArrayList<Items> itemsArrayList = new ArrayList<>();
    BitmapDrawable Draw_bitmap;
    Bitmap QRCode_bitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        findAllViews();
        itemsArrayList = (ArrayList<Items>) getIntent().getExtras().getSerializable("list");
        newStorageName = getIntent().getExtras().getString("storageName");
        storageNameTitle.setText(newStorageName + " was Created");
        QRCodeGen();


        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (generate_qr.this, ActivityItems.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", itemsArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(generate_qr.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                saveQR();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(generate_qr.this, "Write Permission Denied", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }
                        }).check();
            }
        });

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (generate_qr.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void QRCodeGen(){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode("QStorage\\"+newStorageName, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++){
                for (int y = 0; y < 200; y++){
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            //bitmap = QRcode_bitmap;
            QR_Image.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            storageNameTitle.setText(newStorageName);
            QR_Image.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveQR(){


        Draw_bitmap = (BitmapDrawable) QR_Image.getDrawable();
        QRCode_bitmap = Draw_bitmap.getBitmap();

       FileOutputStream outputStream;
        //File sdCard = Environment.getExternalStorageDirectory(); //TODO: after this works you should find a way to use a more modern function.
       File path = Environment.getExternalStoragePublicDirectory(
                     Environment.DIRECTORY_PICTURES);

        File directory = new File(path.getAbsolutePath() + "/QStore_QRCodes");
        directory.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File (directory, fileName);

        Toast.makeText(generate_qr.this, "QR code saved", Toast.LENGTH_SHORT).show();

        try {
            outputStream = new FileOutputStream(outFile);
            QRCode_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent intent = new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void findAllViews() {
        QR_Image = findViewById(R.id.QRimageView);
        storageNameTitle = findViewById(R.id.storageNameTitle);
        title = findViewById(R.id.title);
        review_button = findViewById(R.id.review_button);
        save_button = findViewById(R.id.save_button);
        finish_button = findViewById(R.id.finish_button);


    }
}