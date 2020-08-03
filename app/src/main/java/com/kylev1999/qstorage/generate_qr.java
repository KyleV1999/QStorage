package com.kylev1999.qstorage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class generate_qr extends AppCompatActivity{

    public String newStorageName;
    private ImageView QR_Image;
    private TextView storageNameTitle;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        findAllViews();

    }

    public void QRCodeButton(){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(newStorageName, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++){
                for (int y = 0; y < 200; y++){
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            QR_Image.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            storageNameTitle.setText(newStorageName);
            QR_Image.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void findAllViews() {
        QR_Image = findViewById(R.id.QRimageView);
        storageNameTitle = findViewById(R.id.storageNameTitle);
        title = findViewById(R.id.title);

    }
}