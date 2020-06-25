package model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

import com.example.cmpt276_a3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board{
    private static Random rand = new Random();

    private List<List<Cell>> gameBoard = new ArrayList<>();

    public Board(int rows, int cols, int skulls) {
        // Fill gameBoard with Cells
        for(int row = 0; row < rows; row++) {
            gameBoard.add(new ArrayList<Cell>());
            for (int col = 0; col < cols; col++) {
                gameBoard.get(row).add(new Cell(false));
            }
        }

        // Fill board with skulls
        int skullCounter = 0;
        while (skullCounter < skulls) {
            int randomRow = rand.nextInt(rows);
            int randomCol = rand.nextInt(cols);
            if (!getIndex(randomRow, randomCol).hasSkull()) {
                getIndex(randomRow, randomCol).setSkull(true);
                skullCounter++;
            }
        }
    }

    public int[] cellClicked(int row, int col,
                             int scans, int uncoveredSkulls,
                             Button[][] buttons,
                             Context context) {
        // Returns new scans and uncoveredSkulls values for PlayGameActivity

        // Get cell and button from grid clicked
        Cell cell = getIndex(row, col);
        Button btn = buttons[row][col];

        if(!cell.isScanned()) {
            if(cell.hasSkull()) {
                Skull skull = cell.getSkull();
                if (skull.isDiscovered()) {
                    scanCell(row, col, btn, context);
                    scans++;
                } else {
                    revealSkull(row, col, buttons, context);
                    uncoveredSkulls++;
                }
            } else {
                scanCell(row, col, btn, context);
                scans++;
            }
        }
        return new int[]{scans, uncoveredSkulls};
    }

    public void scanCell(int row, int col, Button btn, Context context) {
        // Change cell to scanned
        getIndex(row, col).setScanned(true);

        int colSkulls = countSkullsInCol(row, col);
        int rowSkulls = countSkullsInRow(row, col);

        // Update cell hiddenCount to total number of skulls in row, col
        getIndex(row, col).setHiddenCount(colSkulls + rowSkulls);

        String string = context.getString(R.string.cell_text, getIndex(row,col).getHiddenCount());
        btn.setText(string);
    }

    private void revealSkull(int row, int col, Button[][] buttons, Context context) {
        // Update skull to be discovered
        getIndex(row, col).getSkull().setDiscovered(true);

        changeButtonBackgroundToSkull(buttons[row][col], context);

        updateScannedCountsInCol(row, col, buttons, context);
        updateScannedCountsInRow(row, col, buttons, context);
    }

    private void updateScannedCountsInCol(int row, int col, Button[][] buttons, Context context) {
        Button btn;
        for (int iterRow = 0; iterRow < getRows(); iterRow++) {
            if (iterRow != row) {
                Cell cell = getIndex(iterRow, col);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[iterRow][col];
                    String string = context.getString(R.string.cell_text, cell.getHiddenCount());
                    btn.setText(string);
                }
            }
        }
    }

    private void updateScannedCountsInRow(int row, int col, Button[][] buttons, Context context) {
        Button btn;
        for (int iterCol = 0; iterCol < getCols(); iterCol++) {
            if (iterCol != col) {
                Cell cell = getIndex(row, iterCol);
                if (cell.isScanned()) {
                    cell.setHiddenCount(cell.getHiddenCount() - 1);
                    btn = buttons[row][iterCol];
                    String string = context.getString(R.string.cell_text, cell.getHiddenCount());
                    btn.setText(string);
                }
            }
        }
    }

    private void changeButtonBackgroundToSkull(Button btn, Context context) {
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.skull4_10percent);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = context.getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    private int countSkullsInCol(int row, int col) {
        int count = 0;

        for (int iterCol = 0; iterCol < getCols(); iterCol++) {
            if (iterCol != col) {
                Cell cell = getIndex(row, iterCol);
                if (cell.hasSkull()) {
                    Skull skull = cell.getSkull();
                    if (!skull.isDiscovered()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int countSkullsInRow(int row, int col) {
        int count = 0;

        for (int iterRow = 0; iterRow < getRows(); iterRow++) {
            if (iterRow != row) {
                Cell cell = getIndex(iterRow, col);
                // System.out.println("TRACE: " + cell.toString());
                if (cell.hasSkull()) {
                    Skull skull = cell.getSkull();
                    if (!skull.isDiscovered()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public boolean isGameOver(int uncoveredSkulls) {
        // Get gamedata instance
        GameData gameData = GameData.getInstance();

        return uncoveredSkulls == gameData.getSkulls();
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
}
