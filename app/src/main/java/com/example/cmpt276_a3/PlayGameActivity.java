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
        gameData.setRows(4);
        gameData.setCols(6);
        gameData.setMines(5);
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), "alien_encounters_solid_bold_italic.ttf");
        btn.setText("" + gameBoard.getIndex(row,col).getHiddenCount());
        btn.setTypeface(typeface);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        // btn.setTextColor(Color.YELLOW);

        // Update button text with new number
        // btn.setText("" + gameBoard.getIndex(row,col).getHiddenCount());
    }

    private void changeButtonBackgroundToMine(Button btn) {
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.skull4_50percent);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
        // btn.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(resource, scaledBitmap), null, null);
        // btn.setCompoundDrawables(null, new BitmapDrawable(resource, scaledBitmap), null, null);
        // btn.setBackgroundColor(Color.TRANSPARENT);

        // taken from stackoverflow
        // https://stackoverflow.com/questions/6590838/calling-setcompounddrawables-doesnt-display-the-compound-drawable
        // probably won't use
        // Drawable image = this.getResources().getDrawable( R.drawable.skull_10percent );
        // int h = image.getIntrinsicHeight() / 2;
        // int w = image.getIntrinsicWidth() / 2;
        // image.setBounds( 0, 0, w, h );
        // btn.setGravity(Gravity.CENTER);
        // btn.setCompoundDrawablePadding(0);
        // btn.setCompoundDrawables( image, null, null, null );
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

        // Get gameData instance
        GameData gameData = GameData.getInstance();

        gameBoard = new Board(gameData.getRows(), gameData.getCols(), gameData.getMines());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }
}