package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

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
        gameData.setRows(4);
        gameData.setCols(4);
        gameData.setMines(5);
        scans = 0;
        uncoveredMines = 0;

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), 5);

        populateButtons();
        populateGameBoard(gameBoard);
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
        // Toast.makeText(this, "Button clicked " + row + "," + col,
                // Toast.LENGTH_LONG).show();
        Button btn = buttons[row][col];
        Cell cell = gameBoard.getIndex(row, col);

        GameData gameData = GameData.getInstance();

        if (!cell.isScanned()) {
            if (cell.hasMine()) {
                Mine mine = cell.getMine();
                if (mine.isDiscovered()) {
                    scanCell(row, col, btn);
                    scans++;
                } else {
                    revealMine(row, col, btn);
                    uncoveredMines++;
                }
            } else {
                scanCell(row, col, btn);
                scans++;
            }
        }

        // Lock button sizes
        lockButtonSizes();
        // This command does not scale image
        // btn.setBackgroundResource(R.mipmap.blue_spotted_egg);
        // Scale image to button
        // Only works in JellyBean!
        // Change button from default to egg
        // changeButtonBackgroundToMine(btn);
        // Change text on button:
        // btn.setText("" + col);

        // Toast.makeText(this, "Scans:" + scans, Toast.LENGTH_SHORT).show();

        if (uncoveredMines == gameData.getMines()) {
            Toast.makeText(this, "You win!",Toast.LENGTH_LONG).show();
        }
    }

    private void revealMine(int row, int col, Button btn) {
        // Set background image to a mine
        changeButtonBackgroundToMine(btn);

        // Get gameData
        GameData gameData = GameData.getInstance();
        int boardRows = gameData.getRows();
        int boardCols = gameData.getCols();

        // Update mine to be discovered
        gameBoard.getIndex(row, col).getMine().setDiscovered(true);

        // Update counts in row
        for (int iterCol = 0; iterCol < boardCols; iterCol++) {
            if (iterCol != col) {
                Cell cell = gameBoard.getIndex(row, iterCol);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[row][iterCol];
                    btn.setText("" + cell.getHiddenCount());
                }
            }
        }

        // Update counts in col
        for (int iterRow = 0; iterRow < boardRows; iterRow++) {
            if (iterRow != row) {
                Cell cell = gameBoard.getIndex(iterRow, col);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[iterRow][col];
                    btn.setText("" + cell.getHiddenCount());
                }
            }
        }
    }

    private void scanCell(int row, int col, Button btn) {
        // Set button text to number of mines in column and row
        // Get gameData
        GameData gameData = GameData.getInstance();
        int boardRows = gameData.getRows();
        int boardCols = gameData.getCols();

        // Update Cell scanned
        gameBoard.getIndex(row, col).setScanned(true);

        int colMines = 0;
        int rowMines = 0;
        // Count in column
        for (int iterCol = 0; iterCol < boardCols; iterCol++) {
            if (iterCol != col) {
                Cell cell = gameBoard.getIndex(row, iterCol);
                if (cell.hasMine()) {
                    Mine mine = cell.getMine();
                    if (!mine.isDiscovered()) {
                        colMines++;
                    }
                }
            }
        }

        // Count in row
        for (int iterRow = 0; iterRow < boardRows; iterRow++) {
            if (iterRow != row) {
                Cell cell = gameBoard.getIndex(iterRow, col);
                if (cell.hasMine()) {
                    Mine mine = cell.getMine();
                    if (!mine.isDiscovered()) {
                        rowMines++;
                    }
                }
            }
        }

        // Update cell hiddenCount to total number of mines in row and col
        gameBoard.getIndex(row, col).setHiddenCount(colMines + rowMines);

        // Update button text with new number
        btn.setText("" + gameBoard.getIndex(row,col).getHiddenCount());
    }

    private void changeButtonBackgroundToMine(Button btn) {
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        // int chosenDimension = Math.min(newHeight, newWidth);
        
        // for testing
        // chosenDimension = newHeight;
        // Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, chosenDimension, chosenDimension, true);
        // btn.setForeground(new BitmapDrawable(resource, scaledBitmap));

        // Original code provided by Dr. Brian Fraser
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.skull_10percent);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
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

    private void populateGameBoard(Board gameBoard) {
        // Setup initial game board

        // Get gameData instance
        GameData gameData = GameData.getInstance();

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getMines());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }
}