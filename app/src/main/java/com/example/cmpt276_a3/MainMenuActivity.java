package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    public static final int LAUNCH_GAME = 0;
    public static final int LAUNCH_OPTIONS = 1;
    public static final int LAUNCH_HELP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupPlayButton();
        setupOptionsButton();
        setupHelpButton();
    }

    private void setupPlayButton() {
        Button btn = findViewById(R.id.btnPlay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayGameActivity.makeIntent(MainMenuActivity.this);
                startActivityForResult(intent, LAUNCH_GAME);
            }
        });
    }

    private void setupOptionsButton() {
        Button btn = findViewById(R.id.btnOptions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeIntent(MainMenuActivity.this);
                startActivityForResult(intent, LAUNCH_OPTIONS);
            }
        });
    }

    private void setupHelpButton() {
        Button btn = findViewById(R.id.btnHelp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainMenuActivity.this);
                startActivityForResult(intent, LAUNCH_HELP);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }
}