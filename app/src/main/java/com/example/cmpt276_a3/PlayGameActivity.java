package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;

import model.Board;
import model.Cell;
import model.GameData;
import model.Mine;

public class PlayGameActivity extends AppCompatActivity {

    Button buttons[][];
    Board gameBoard;
    int scans;
    int uncoveredMines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        // Get GameData Singleton
        GameData gameData = GameData.getInstance();
        // TEST
        // gameData.setRows(4);
        // gameData.setCols(6);
        // gameData.setMines(24);
        scans = 0;
        uncoveredMines = 0;

        setFoundScansText();

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getMines());

        populateButtons();
        populateGameBoard(gameBoard);
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForCells);

        // Get GameData Singleton
        GameData gameData = GameData.getInstance();

        buttons = new Button[gameData.getRows()][gameData.getCols()];

        for (int row = 0; row < gameData.getRows(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for (int col = 0; col < gameData.getCols(); col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button btn = new Button(this);
                btn.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                // btn.setText("" + row + "," + col);

                // Make text not clip on small buttons
                btn.setPadding(0,0,0,0);
                btn.setBackground(getDrawable(R.drawable.gradient));
                btn.setTextColor(Color.YELLOW);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });


                tableRow.addView(btn);
                buttons[row][col] = btn;
            }
        }
    }

    private void gridButtonClicked(int row, int col) {
        // Start replacing code
        Button btn = buttons[row][col];
        int[] newScansUncovered = gameBoard.cellClicked(
                row, col,
                scans, uncoveredMines,
                buttons,
                PlayGameActivity.this);

        scans = newScansUncovered[0];
        uncoveredMines = newScansUncovered[1];

        // Lock button sizes
        lockButtonSizes();

        setFoundScansText();

        if (gameBoard.isGameOver(uncoveredMines)) {
            Toast.makeText(this, "You win!",Toast.LENGTH_LONG).show();

        }
    }

    private void lockButtonSizes() {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void setFoundScansText() {
        GameData gameData = GameData.getInstance();

        // Set text for foundMines and scansPerformed
        TextView txtFoundMines = findViewById(R.id.txtFoundMines);
        String found_mines = getString(R.string.found_mines, uncoveredMines, gameData.getMines());
        txtFoundMines.setText(found_mines);

        TextView txtScans = findViewById(R.id.txtScansPerformed);
        String scans_performed = getString(R.string.scans_used, scans);
        txtScans.setText(scans_performed);
    }

    private void populateGameBoard(Board gameBoard) {
        // Setup initial game board
        GameData gameData = GameData.getInstance();
        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getMines());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }
}