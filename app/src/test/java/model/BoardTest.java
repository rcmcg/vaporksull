package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testCon() {
        Board board = new Board(2,4, 5);
        int rows = board.getRows();
        int cols = board.getCols();
        assertEquals(true, (rows==2) && (cols== 4));
    }

    @Test
    public void testNumMines() {
        Board board = new Board(2,4,5);
        // Count mines
        int numMines = 0;
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.getIndex(row,col).hasSkull()) {
                    numMines++;
                }
            }
        }
        assertEquals(5,numMines);
    }

    @Test
    public void testNumMines2() {
        Board board = new Board(10,10,5);
        // Count mines
        int numMines = 0;
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.getIndex(row,col).hasSkull()) {
                    numMines++;
                }
            }
        }
        assertEquals(5,numMines);
    }
}