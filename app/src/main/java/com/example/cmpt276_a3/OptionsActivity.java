package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.GameData;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


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

        gameSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // GameData gameData = GameData.getInstance();
                // Toast.makeText(OptionsActivity.this, ""+gameData.getRows(), Toast.LENGTH_SHORT).show();
            }
        });
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

        mineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GameData gameData = GameData.getInstance();
                if(position == 1) {
                    gameData.setMines(6);
                } else if (position == 2) {
                    gameData.setMines(10);
                } else if (position == 3){
                    gameData.setMines(15);
                } else if (position == 4){
                    gameData.setMines(20);
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

