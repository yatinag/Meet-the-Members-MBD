package com.example.membersmdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timeCountText;
    TextView scoreCountText;
    ImageView memberImage;
    RadioButton btnOption1;
    RadioButton btnOption2;
    RadioButton btnOption3;
    RadioButton btnOption4;
    RadioGroup btnOptionsGroup;
    Button btnEnd;
    String correctMember;
    int scoreCount;
    TextView hotStreak;
    int streakCount;
    boolean lastCorrect;
    int timeLeft = 5;
    CountDownTimer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeCountText = findViewById(R.id.timeCount);
        scoreCountText = findViewById(R.id.scoreCount);
        memberImage = findViewById(R.id.imageMember);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnOptionsGroup = findViewById(R.id.btnOptionsGroup);
        btnEnd = findViewById(R.id.closeButton);
        hotStreak = findViewById(R.id.hotStreak);

        lastCorrect = false;
        memberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creates a new Intent to insert a contact
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                // Sets the MIME type to match the Contacts Provider
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, correctMember);
                startActivity(intent);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                onPause();
                builder.setTitle("Quit Game");
                builder.setMessage("Are you sure you want to exit the game?");
                builder.setPositiveButton("Exit Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onResume();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buildScreen();

    }

    @Override
    protected void onPause(){
        super.onPause();
        if (myTimer != null) {
            myTimer.cancel();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println(timeLeft);

        if (myTimer != null) {
            myTimer.start();
        }
    }

    public void buildScreen() {
        if (lastCorrect == true) {
            streakCount++;
        }
        scoreCountText.setText("Current Score: " + scoreCount);
        hotStreak.setText("Hot Streak: " + streakCount);
        ArrayList<String> allMembers = new ArrayList<>();
        try {
            allMembers = Utils.readMemberNames(this);
        } catch (IOException e) {
            Toast errorToast = Toast.makeText(this, "Error reading names from file.", Toast.LENGTH_LONG);
            errorToast.show();
        }

        Random rand = new Random(System.currentTimeMillis());
        int memberIndex = rand.nextInt(allMembers.size());

        correctMember = allMembers.get(memberIndex);
        String imageFileName = Utils.getFileNameVersion(correctMember);
        memberImage.setImageDrawable(Utils.getImage(this, imageFileName));

        ArrayList<Integer> randomIndices = new ArrayList<>();
        randomIndices.add(memberIndex);
        for (int i = 0; i<3; i++) {
            int val = rand.nextInt(allMembers.size());
            if (randomIndices.contains(val)) {
                i--;
            } else {
                randomIndices.add(val);
            }
        }

        Collections.shuffle(randomIndices);
        btnOption1.setText(allMembers.get(randomIndices.get(0)));
        btnOption2.setText(allMembers.get(randomIndices.get(1)));
        btnOption3.setText(allMembers.get(randomIndices.get(2)));
        btnOption4.setText(allMembers.get(randomIndices.get(3)));

        final RadioButton btnCorrect;
        if (Utils.getFileNameVersion(btnOption1.getText().toString()).equals(imageFileName)){
            btnCorrect = btnOption1;
        } else if (Utils.getFileNameVersion(btnOption2.getText().toString()).equals(imageFileName)){
            btnCorrect = btnOption2;
        } else if (Utils.getFileNameVersion(btnOption3.getText().toString()).equals(imageFileName)){
            btnCorrect = btnOption3;
        } else {
            btnCorrect = btnOption4;
        }

        myTimer = new CountDownTimer((timeLeft * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                timeCountText.setText("Time: " + ((millisUntilFinished / 1000) + 1) + " Seconds");
                timeLeft = (int) (millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeCountText.setText("Time is up!");
                btnCorrect.setBackgroundColor(Color.RED);
                lastCorrect = false;
                streakCount = 0;
                timeLeft = 5;
                //flash red, with correct answer
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnCorrect.setBackgroundColor(0x00000000);
                        buildScreen();
                    }
                }, 1000);
            }
        };
        myTimer.start();

        btnOptionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                final RadioButton checkedRadioButton = findViewById(checkedId);
                if (btnCorrect == checkedRadioButton) {
                    lastCorrect = true;
                    btnCorrect.setBackgroundColor(Color.GREEN);
                    scoreCount++;
                    timeLeft = 5;
                    myTimer.cancel();
                    btnCorrect.setChecked(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnCorrect.setBackgroundColor(0x00000000);
                            buildScreen();
                        }
                    }, 1000);
                } else {
                    lastCorrect = false;
                    streakCount = 0;
                    timeLeft = 5;
                    btnCorrect.setBackgroundColor(Color.GREEN);
                    checkedRadioButton.setBackgroundColor(Color.RED);
                    myTimer.cancel();
                    final Handler handler = new Handler();
                    checkedRadioButton.setChecked(false);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnCorrect.setBackgroundColor(0x00000000);
                            checkedRadioButton.setBackgroundColor(0x00000000);
                            buildScreen();
                        }
                    }, 1000);
                }
            }
        });
    }

}
