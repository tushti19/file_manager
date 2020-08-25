package com.anddev.myfilemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class GotData extends AppCompatActivity {

    Intent intent;
    ArrayList<Uri> Uriselected;

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_got_data);

        b = findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GotData.this,MainActivity.class);
                startActivityForResult(intent,10);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 10) {

            Log.d("activity", "yes working");

            Bundle uris = data.getExtras();

            Log.d("get result", "yes working");
            Uriselected = uris.getParcelableArrayList("abc");


            if (Uriselected != null) {

                for (Uri u : Uriselected) {
                    Log.d("file uri ttttt", "" + u);
                }

            } else
                Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
    }
}