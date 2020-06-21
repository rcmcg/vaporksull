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
        // gameData.setMines(5);
        scans = 0;
        uncoveredMines = 0;

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getMines());

        populateButtons();
        populateGameBoard(gameBoard);
    }

    private void setButtonImagesToDefault() {
        System.out.println("TRACE: ENTERING SET BUTTON IMAGES TO DEFAULT");
        GameData gameData = GameData.getInstance();
        Button btn;
        /*
        for (int row = 0; row < gameData.getRows(); row++) {
            for( int col = 0; col < gameData.getCols(); col++) {
                btn = buttons[row][col];
                // Maybe use this stack overflow link
                // https://stackoverflow.com/questions/13929877/how-to-make-gradient-background-in-android
                // to set it to the boxes.png gradient, ask Breanna for the start and end color
                // btn.setBackgroundColor(Color.BLUE);
                System.out.println("TRACE: btn.getWidth() " + btn.getWidth());
                System.out.println("TRACE: btn.getHeight() " + btn.getHeight());
            }
        }
         */


        // Change all buttons background to boxes.png background
        /*
        for (int row = 0; row < gameData.getRows(); row++) {
            for (int col = 0; col < gameData.getCols(); col++) {
                btn = buttons[row][col];
                // Turn this into a function?
                int newWidth = btn.getWidth();
                System.out.println("TRACE: btn.getWidth()" + btn.getWidth());
                int newHeight = btn.getHeight();
                System.out.println("TRACE: btn.getHeight()" + btn.getHeight());
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boxes);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
                Resources resource = getResources();
                btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
            }
        }
         */
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForCells);

        // Get GameData Singleton
        GameData gameData = GameData.getInstance();

        buttons = new Button[gameData.getRows()][gameData.getCols()];

        // System.out.println("TRACE: gameData.getRows: " + gameData.getRows());
        // System.out.println("TRACE: gameData.getCols: " + gameData.getCols());

        // App crashes if rows or cols == 0, set an input check

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

                // Maybe use this stack overflow link
                // https://stackoverflow.com/questions/13929877/how-to-make-gradient-background-in-android
                // to set it to the boxes.png gradient, ask Breanna for the start and end color
                // ContextCompat.getDrawable(this, R.drawable.boxes);
                btn.setBackground(ContextCompat.getDrawable(this, R.drawable.boxes_border));

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

        // Toast.makeText(this, "Scans:" + scans, Toast.LENGTH_SHORT).show();
        if (gameBoard.isGameOver(uncoveredMines)) {
            Toast.makeText(this, "You win!",Toast.LENGTH_LONG).show();

        }
    }

    private void lockButtonSizes() {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                // System.out.println("TRACE: in lockButtonSizes button.getWidth() " + width);
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                // System.out.println("TRACE: in lockButtonSizes button.getHeight() " + height);
                button.setMinHeight(height);
                button.setMaxHeight(height);

                /*
                if ((row%2 == 0) & (col%2 == 1)) {
                    button.setBackgroundColor(Color.BLUE);
                } else {
                    button.setBackgroundColor(Color.BLACK);
                }
                 */

            }
        }
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