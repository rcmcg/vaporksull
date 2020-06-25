package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import model.GameData;

public class OptionsActivity extends AppCompatActivity {


    int[][] boardOptions = new int[][]{{4,6}, {5,10}, {6,15}};
    int[] skullOptions = new int[]{6, 10, 15, 20 };

    private static final String SKULL_PREF_NAME = "Skull settings";
    private static final String NUM_SKULLS_PREF_NAME = "Num skulls";
    private static final String ROW_PREF_NAME = "Num rows";
    private static final String COL_PREF_NAME = "Num cols";
    private static final String BOARD_PREF_NAME = "BoardSettings";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        int[] savedRowsCols = getNumRowsCols(this);

        populateGameSizeSpinner();
        populateSkullSpinner();
    }

    private void populateGameSizeSpinner() {
        // Created using the following tutorials
        // https://www.youtube.com/watch?v=urQp7KsQhW8
        // https://www.youtube.com/watch?v=mrcrFY-5c-c

        Spinner gameSizeSpinner = findViewById(R.id.game_size_spinner);
        ArrayAdapter<CharSequence> gameSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_arrays,
                R.layout.spinner_style);
        gameSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSizeSpinner.setAdapter(gameSizeAdapter);

        // Choose correct spinner option when activity opens
        for (int i = 0; i < boardOptions.length; i++) {
            if (getNumRowsCols(this)[0] == boardOptions[i][0]) {
                gameSizeSpinner.setSelection(i+1);
            }
        }

        gameSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();
                if(position != 0) {
                    gameData.setRows(boardOptions[position-1][0]);
                    gameData.setCols(boardOptions[position-1][1]);
                    saveBoardSettings(gameData.getRows(), gameData.getCols());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveBoardSettings(int rows, int cols) {
        SharedPreferences prefs = this.getSharedPreferences(BOARD_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ROW_PREF_NAME, rows);
        editor.putInt(COL_PREF_NAME, cols);
        editor.apply();
    }

    static public int[] getNumRowsCols(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BOARD_PREF_NAME, MODE_PRIVATE);

        int default_rows = context.getResources().getInteger(R.integer.default_rows);
        int default_cols = context.getResources().getInteger(R.integer.default_cols);

        int rows = prefs.getInt(ROW_PREF_NAME, default_rows);
        int cols = prefs.getInt(COL_PREF_NAME, default_cols);
        return new int[]{rows,cols};
    }

    private void saveSkullSettings(int skulls) {
        SharedPreferences prefs = this.getSharedPreferences(SKULL_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_SKULLS_PREF_NAME, skulls);
        editor.apply();
    }

    static public int getNumSkulls(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SKULL_PREF_NAME, MODE_PRIVATE);

        int default_skull = context.getResources().getInteger(R.integer.default_skulls);

        return prefs.getInt(NUM_SKULLS_PREF_NAME, default_skull);
    }

    private void populateSkullSpinner() {
        Spinner skullSpinner = findViewById(R.id.skull_spinner);

        ArrayAdapter<CharSequence> skullAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_skull_array,
                R.layout.spinner_style);
        skullAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        skullSpinner.setAdapter(skullAdapter);

        // Choose correct spinner option when activity opens
        for (int i = 0; i < skullOptions.length; i++) {
            if (getNumSkulls(this) == skullOptions[i]) {
                skullSpinner.setSelection(i+1);
            }
        }


        skullSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();
                if(position != 0) {
                    gameData.setSkulls(skullOptions[position-1]);
                    saveSkullSettings(gameData.getSkulls());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }
}

