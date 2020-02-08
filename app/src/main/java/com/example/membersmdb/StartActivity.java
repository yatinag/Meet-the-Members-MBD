package com.example.membersmdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    Button btnStart;
    ImageView mdbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnStart = findViewById(R.id.btnStart);
        mdbImage = findViewById(R.id.mdbImage);
        TextView headingText = findViewById(R.id.headingText);

        headingText.setPaintFlags(headingText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mdbImage.setImageDrawable(Utils.getImage(this, "mdbicon"));

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
