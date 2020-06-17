package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void hasMine0() {
        Cell cell = new Cell(false);
        assertEquals(false, cell.hasMine());
    }

    @Test
    public void hasMine1() {
        Cell cell = new Cell(true);
        assertEquals(true, cell.hasMine());
    }

    @Test
    public void getMine() {
        Cell cell = new Cell(true);
        assertEquals(false, cell.getMine() ==  null);
    }

    @Test
    public void setMine0() {
        Cell cell = new Cell(false);
        cell.setMine(true);
        assertEquals(true, cell.hasMine());
    }

    @Test
    public void setMine1() {
        Cell cell = new Cell(true);
        cell.setMine(false);
        assertEquals(false, cell.hasMine());
    }

    @Test
    public void isScanned0() {
        Cell cell = new Cell(false);
        assertEquals(false, cell.isScanned());
    }

    @Test
    public void setScanned() {
        Cell cell = new Cell(false);
        cell.setScanned(true);
        assertEquals(true, cell.isScanned());
    }

    @Test
    public void isScanned1() {
        Cell cell = new Cell(false);
        cell.setScanned(true);
        assertEquals(true, cell.isScanned());
    }
}