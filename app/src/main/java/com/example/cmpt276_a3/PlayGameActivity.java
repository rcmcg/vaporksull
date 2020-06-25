package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import model.Board;
import model.GameData;

public class PlayGameActivity extends AppCompatActivity {

    Button buttons[][];
    Board gameBoard;
    int scans;
    int uncoveredSkulls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        // Get GameData Singleton
        GameData gameData = GameData.getInstance();

        // Get game settings from shared preferences
        int savedRows = OptionsActivity.getNumRowsCols(this)[0];
        int savedCols = OptionsActivity.getNumRowsCols(this)[1];
        int savedSkulls = OptionsActivity.getNumSkulls(this);

        gameData.setRows(savedRows);
        gameData.setCols(savedCols);
        gameData.setSkulls(savedSkulls);

        scans = 0;
        uncoveredSkulls = 0;

        setFoundScansText();

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getSkulls());

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
        // To check if a scan was performed or if a skull was revealed
        int oldScans = scans;
        int oldUncoveredSkulls = uncoveredSkulls;

        Button btn = buttons[row][col];
        int[] newScansUncovered = gameBoard.cellClicked(
                row, col,
                scans, uncoveredSkulls,
                buttons,
                PlayGameActivity.this);

        scans = newScansUncovered[0];
        uncoveredSkulls = newScansUncovered[1];

        if (scans > oldScans) {
            vibratePhone(10);
        }

        if(uncoveredSkulls > oldUncoveredSkulls) {
            vibratePhone(50);
        }

        // Lock button sizes
        lockButtonSizes();

        setFoundScansText();

        if (gameBoard.isGameOver(uncoveredSkulls)) {
            // Alert code taken from android blog
            // https://developer.android.com/guide/topics/ui/dialogs
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayGameActivity.this).setView(R.layout.victory_dialog);

            vibratePhone(300);

            builder.setMessage(R.string.victory_dialog_message).setTitle(R.string.victory_dialog_title);

            builder.setPositiveButton(R.string.victory_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // This function taken from
    // https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
    private void vibratePhone(int milliseconds) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(milliseconds);
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

        // Set text for foundSkulls and scansPerformed
        TextView txtFoundSkulls = findViewById(R.id.txtFoundSkulls);
        String foundSkulls = getString(R.string.found_skulls, uncoveredSkulls, gameData.getSkulls());
        txtFoundSkulls.setText(foundSkulls);

        TextView txtScans = findViewById(R.id.txtScansPerformed);
        String scans_performed = getString(R.string.scans_used, scans);
        txtScans.setText(scans_performed);
    }

    private void populateGameBoard(Board gameBoard) {
        // Setup initial game board
        GameData gameData = GameData.getInstance();
        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getSkulls());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }
}