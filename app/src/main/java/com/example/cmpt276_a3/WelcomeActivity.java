package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private static long welcomeScreenTimeMillis = 5000;

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setupNextButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        Log.i(TAG, "Timer starting");
        // Start a time for 5 seconds after activity is visible to user
        long timeOnStart = System.currentTimeMillis();
        long welcomeScreenTimeEnd = timeOnStart + 5000;
        while ( System.currentTimeMillis() < welcomeScreenTimeEnd ) {
            // do nothing
        }

        // Start MainMenuActivity
        Log.i(TAG, "Timer elapsed - calling advanceToMainMenu()");
        advanceToMainMenu();

        // finish() WelcomeActivity
         */
    }

    private void advanceToMainMenu() {
        // Fill if you add an animation/timer to welcome screen
    }

    private void setupNextButton() {
        // Change activity to main menu, finish() this activity
        Button btn = findViewById(R.id.btnWelcomeNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainMenuActivity
                Intent intent = MainMenuActivity.makeIntent(WelcomeActivity.this);
                startActivity(intent);

                // Finish this activity
                finish();
            }
        });

        // Do this in the XML file
        /*
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boxes6_50percent);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
         */
    }
}