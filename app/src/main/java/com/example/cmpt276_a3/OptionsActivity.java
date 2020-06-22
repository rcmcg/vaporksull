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

    int[][] boardOptions = new int[][]{ {4,6}, {5,10}, {6,15}};
    int[] mineOptions = new int[]{6, 10, 15, 20 };

    private static final String ROW_PREF_NAME = "Num rows";
    private static final String COL_PREF_NAME = "Num cols";
    private static final String BOARD_PREF_NAME = "BoardSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        int[] savedRowsCols = getNumRowsCols(this);
        // Toast.makeText(this, "Saved rows cols: " + savedRowsCols[0] + "," + savedRowsCols[1], Toast.LENGTH_SHORT)
            // .show();

        // Spinner mineSpinner = findViewById(R.id.game_spinner);
        populateGameSizeSpinner();
        populateMineSpinner();
    }

    private void populateGameSizeSpinner() {
        // Created using the following tutorials
        // https://www.youtube.com/watch?v=urQp7KsQhW8
        // https://www.youtube.com/watch?v=mrcrFY-5c-c

        // Video to make spinners custom
        //https://www.youtube.com/watch?v=GeO5F0nnzAw
        Spinner gameSizeSpinner = findViewById(R.id.game_size_spinner);
        ArrayAdapter<CharSequence> gameSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_arrays,
                R.layout.spinner_style);
        // ArrayAdapter<String> gameSizeAdapter = new ArrayAdapter<>(this,
                // android.R.layout.simple_list_item_1,
                // getResources().getStringArray(R.array.board_size_arrays));
        gameSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSizeSpinner.setAdapter(gameSizeAdapter);

        // The spinner should select the chosen option to be the current gameData
        // needs to save between runs

        // Select default spinner

        for (int i = 0; i < boardOptions.length; i++) {
            if (getNumRowsCols(this)[0] == boardOptions[i][0]) {
                gameSizeSpinner.setSelection(i+1);
            }
        }


        gameSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();

                /*
                if(position == 1) {
                    gameData.setRows(4);
                    gameData.setCols(6);
                } else if (position == 2) {
                    gameData.setRows(5);
                    gameData.setCols(10);
                } else if (position == 3){
                    gameData.setRows(6);
                    gameData.setCols(15);
                }

                 */
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

    private void saveMineSettings(int mines) {
        SharedPreferences prefs = this.getSharedPreferences("Mine settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Num mines", mines);
        editor.apply();
    }

    static public int getNumMines(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Mine settings", MODE_PRIVATE);

        int default_mines = context.getResources().getInteger(R.integer.default_mines);

        int rows = prefs.getInt("Num mines", default_mines);
        return default_mines;
    }

    private void populateMineSpinner() {
        Spinner mineSpinner = findViewById(R.id.mine_spinner);

        // ArrayAdapter<String> mineAdapter = new ArrayAdapter<>(this,
                // android.R.layout.simple_list_item_1,
                // getResources().getStringArray(R.array.mine_size_arrays));
        ArrayAdapter<CharSequence> mineAdapter = ArrayAdapter.createFromResource(this,
                R.array.mine_size_arrays,
                R.layout.spinner_style);
        mineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mineSpinner.setAdapter(mineAdapter);

        // The spinner should select the chosen option to be the current gameData
        // needs to save between runs

        // Select default spinner
        for (int i = 0; i < mineOptions.length; i++) {
            if (getNumMines(this) == mineOptions[i]) {
                mineSpinner.setSelection(i+1);
            }
        }


        mineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();
                if(position != 0) {
                    gameData.setMines(mineOptions[position-1]);
                    saveMineSettings(gameData.getMines());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // GameData gameData = GameData.getInstance();
                // Toast.makeText(OptionsActivity.this, ""+gameData.getRows(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }
}

