package model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Board implements Iterable<Cell>{
    private int rows;
    private int cols;

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
            // int randomRow = ThreadLocalRandom.current().nextInt(0, rows);
            // int randomCol = ThreadLocalRandom.current().nextInt(0, cols);
            int randomRow = rand.nextInt(rows);
            int randomCol = rand.nextInt(cols);
            if (!getIndex(randomRow, randomCol).hasMine()) {
                getIndex(randomRow, randomCol).setMine(true);
                mineCounter++;
            }

        }
        /*
        while (mineCounter < mines) {
            for(int row = 0; row < rows; row++) {
                for(int col = 0; col < cols; col++) {
                    // Each cell has an equal probability of getting a mine
                    int randomNum = ThreadLocalRandom.current().nextInt(1, rows*cols + 1);
                    if (randomNum == 1) {
                        if (mineCounter < mines) {
                            if (!getIndex(row, col).hasMine()) {
                                getIndex(row,col).setMine(true);
                                mineCounter++;
                            }
                        }
                    }
                }
            }
        }
        */
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
    // not sure if this applies since I have a list of lists
    public Iterator<Cell> iterator() {
        return null;
    }
}
