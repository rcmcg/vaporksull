package model;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.cmpt276_a3.PlayGameActivity;
import com.example.cmpt276_a3.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Board implements Iterable<Cell>{
    // private int rows;
    // private int cols;

    private static Random rand = new Random();

    private List<List<Cell>> gameBoard = new ArrayList<>();
    // private List<List<Cell>> gameBoard;

    public Board(int rows, int cols, int mines) {
        // Fill gameBoard with Cells,
        for(int row = 0; row < rows; row++) {
            gameBoard.add(new ArrayList<Cell>());
            for (int col = 0; col < cols; col++) {
                gameBoard.get(row).add(new Cell(false));
            }
        }

        // Fill board with mines
        int mineCounter = 0;
        while (mineCounter < mines) {
            int randomRow = rand.nextInt(rows);
            int randomCol = rand.nextInt(cols);
            if (!getIndex(randomRow, randomCol).hasMine()) {
                getIndex(randomRow, randomCol).setMine(true);
                mineCounter++;
            }
        }
    }

    public int[] cellClicked(int row, int col,
                             int scans, int uncoveredMines,
                             Button[][] buttons,
                             Context context) {
        // Returns new scans and uncoveredMines values for PlayGameActivity

        // Get cell and button from grid clicked
        Cell cell = getIndex(row, col);
        Button btn = buttons[row][col];

        if(!cell.isScanned()) {
            if(cell.hasMine()) {
                Mine mine = cell.getMine();
                if (mine.isDiscovered()) {
                    scanCell(row, col, btn, context);
                    scans++;
                } else {
                    revealMine(row, col, buttons, context);
                    uncoveredMines++;
                }
            } else {
                scanCell(row, col, btn, context);
                scans++;
            }
        }
        return new int[]{scans, uncoveredMines};
    }

    public void scanCell(int row, int col, Button btn, Context context) {
        // Change cell to scanned
        getIndex(row, col).setScanned(true);

        int colMines = countMinesInCol(row, col);
        int rowMines = countMinesInRow(row, col);

        // Update cell hiddenCount to total number of mines in row, col
        getIndex(row, col).setHiddenCount(colMines + rowMines);

        AssetManager mngr = context.getAssets();

        // This code taken from
        // https://stackoverflow.com/questions/6372458/setting-button-text-font-in-android
        // Typeface typeface = Typeface.createFromAsset(mngr, "alien_encounters_solid_bold_italic.ttf");
        btn.setText("" + getIndex(row,col).getHiddenCount());
        // btn.setTypeface(typeface);
        // btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
    }

    private void revealMine(int row, int col, Button[][] buttons, Context context) {
        // Update mine to be discovered
        getIndex(row, col).getMine().setDiscovered(true);

        changeButtonBackgroundToMine(buttons[row][col], context);

        updateScannedCountsInCol(row, col, buttons);
        updateScannedCountsInRow(row, col, buttons);
    }

    private void updateScannedCountsInCol(int row, int col, Button[][] buttons) {
        Button btn;
        for (int iterRow = 0; iterRow < getRows(); iterRow++) {
            if (iterRow != row) {
                Cell cell = getIndex(iterRow, col);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[iterRow][col];
                    btn.setText("" + cell.getHiddenCount());
                }
            }
        }
    }

    private void updateScannedCountsInRow(int row, int col, Button[][] buttons) {
        Button btn;
        for (int iterCol = 0; iterCol < getCols(); iterCol++) {
            if (iterCol != col) {
                Cell cell = getIndex(row, iterCol);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[row][iterCol];
                    btn.setText("" + cell.getHiddenCount());
                }
            }
        }
    }

    private void changeButtonBackgroundToMine(Button btn, Context context) {
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.skull4_50percent);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = context.getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private int countMinesInCol(int row, int col) {
        int count = 0;

        for (int iterCol = 0; iterCol < getCols(); iterCol++) {
            if (iterCol != col) {
                Cell cell = getIndex(row, iterCol);
                if (cell.hasMine()) {
                    Mine mine = cell.getMine();
                    if (!mine.isDiscovered()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int countMinesInRow(int row, int col) {
        int count = 0;

        for (int iterRow = 0; iterRow < getRows(); iterRow++) {
            if (iterRow != row) {
                Cell cell = getIndex(iterRow, col);
                // System.out.println("TRACE: " + cell.toString());
                if (cell.hasMine()) {
                    Mine mine = cell.getMine();
                    if (!mine.isDiscovered()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean isGameOver(int uncoveredMines) {
        // Get gamedata instance
        GameData gameData = GameData.getInstance();

        if (uncoveredMines == gameData.getMines()) {
            return true;
        } else {
            return false;
        }
    }

    public int getRows() {
        return gameBoard.size();
    }

    public int getCols() {
        return gameBoard.get(0).size();
    }

    public Cell getIndex(int row, int col) {
        return gameBoard.get(row).get(col);
    }

    @NonNull
    @Override
    public Iterator<Cell> iterator() {
        return null;
    }
}
