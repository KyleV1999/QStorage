package com.kylev1999.qstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class create_new_storage extends AppCompatActivity implements View.OnClickListener {
    public String newStorageName;
    FirebaseAuth mAuth;
    TextView storageNameTitle;
    LinearLayout layoutList;
    Button addButton;
    Button submitButton;
    List<String> numbList = new ArrayList<>(); //List for quantity spinner
    ArrayList<Items> itemsArrayList = new ArrayList<>(); //List for actual items

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_storage);
        findAllViews();
        OpenDialog();
    }


    private void getDatabase() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String path = mAuth.getUid() + "/" + newStorageName;
        myRef = database.getReference(path);
    }

    public void OpenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.new_layout_dialog, null);
        final EditText getStorageName = view.findViewById(R.id.getNewBin);
        final TextView errorMessage = view.findViewById(R.id.errorMsg);


        builder.setView(view)
                .setTitle("Create a New Storage Bin")
                .setCancelable(false) //Reduces use of back button. Maybe implement back button functionality.
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(create_new_storage.this, MainActivity.class ));
                        finish();
                    }
                })
                .setPositiveButton("OK", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getStorageName.getText().toString().isEmpty()){
                    newStorageName = getStorageName.getText().toString();
                    AfterDialog();
                    dialog.dismiss();
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    private void AfterDialog() {
        getDatabase();
        storageNameTitle.setText(newStorageName);
        storageNameTitle.setVisibility(View.VISIBLE);

        addButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

        addButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);


        numbList.add("Item Number");
        numbList.add("1");
        numbList.add("2");
        numbList.add("3");
        numbList.add("4");
        numbList.add("5");
        numbList.add("6");
        numbList.add("7");
        numbList.add("8");
        numbList.add("9");
        numbList.add("10");

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addButton:

                addView();

                break;

            case R.id.submitButton:

                if(validateAndSubmit()){

                    Items itemv;
                    for (int i = 0; i < itemsArrayList.size(); i++){
                      itemv = itemsArrayList.get(i);
                      Log.d("ARRAY", itemv.getName());
                      Log.d("ARRAY", itemv.getQuantity());
                    }

                    WriteData();
                    Toast.makeText(this, "New Bin Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (create_new_storage.this, generate_qr.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", itemsArrayList);
                    intent.putExtras(bundle);
                    intent.putExtra("storageName", newStorageName );
                    startActivity(intent);
                    finish();

                }

                break;

        }
    }


    private void WriteData() {

        Items itemv; //When gotten this is both the name and quantity
        //TODO: Check if everything was successful before going to the next activity.
        for (int i = 0; i < itemsArrayList.size(); i++){
            itemv = itemsArrayList.get(i);
            final String itemn = itemv.getName();
            final String itemq = itemv.getQuantity();
            myRef.push().setValue(itemv).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Database", "Item: " + itemn  + "Quantity: " + itemq + " Pushed To Database!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong! Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            });

        }

     }


    private boolean validateAndSubmit() {
        itemsArrayList.clear();
        boolean result = true;

        for (int i = 0; i < layoutList.getChildCount(); i++){

            View itemView = layoutList.getChildAt(i);
            EditText itemNameText = itemView.findViewById(R.id.getItemName);
            AppCompatSpinner spinnerNumber = itemView.findViewById(R.id.getNumItems);

            Items items = new Items();

            if(!itemNameText.getText().toString().equals("")){
                items.setName(itemNameText.getText().toString());
            } else {
                result = false;
                break;
            }

            if (spinnerNumber.getSelectedItemPosition()!=0){
                items.setQuantity(numbList.get(spinnerNumber.getSelectedItemPosition()));
            } else {
                result = false;
                break;
            }


        //TODO: Check for duplicate items. Notify if they still want to continue. The below works but needs some tweaking
        /*
            String currentString = itemNameText.getText().toString();
            for (int j = i + 1; j < layoutList.getChildCount(); j++){
                View itemView2 = layoutList.getChildAt(j);
                EditText itemNameText2 = itemView2.findViewById(R.id.getItemName);
                if (itemNameText2.getText().toString().equals(currentString)){
                    Toast.makeText(this, "Duplicate", Toast.LENGTH_SHORT).show();
                    result = false;
                    break;
                }
            }

         */


            itemsArrayList.add(items);

        }

        if(itemsArrayList.size()==0){
            result = false;
            Toast.makeText(this, "Add at least one item to the bin!", Toast.LENGTH_SHORT).show();
        } else if (!result) {
            Toast.makeText(this, "Enter items correctly!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }


    private void addView() {

        final View itemView = getLayoutInflater().inflate(R.layout.row_add_item, null, false);
        EditText itemEditText = itemView.findViewById(R.id.getItemName);
        AppCompatSpinner spinnerNumber = itemView.findViewById(R.id.getNumItems);
        ImageView removeImg = itemView.findViewById(R.id.removeImg);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numbList);
        spinnerNumber.setAdapter(arrayAdapter);

        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(itemView);
            }
        });


        layoutList.addView(itemView);
    }

    private void removeView(View view){
        layoutList.removeView(view);

    }


    private void findAllViews() {
        storageNameTitle = findViewById(R.id.storageNameTitle);
        addButton = findViewById(R.id.addButton);
        layoutList = findViewById(R.id.layout_list);
        submitButton = findViewById(R.id.submitButton);

    }
}