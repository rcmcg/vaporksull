package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void hasSkull0() {
        Cell cell = new Cell(false);
        assertEquals(false, cell.hasSkull());
    }

    @Test
    public void hasSkull1() {
        Cell cell = new Cell(true);
        assertEquals(true, cell.hasSkull());
    }

    @Test
    public void getSkull() {
        Cell cell = new Cell(true);
        assertEquals(false, cell.getSkull() ==  null);
    }

    @Test
    public void setSkull0() {
        Cell cell = new Cell(false);
        cell.setSkull(true);
        assertEquals(true, cell.hasSkull());
    }

    @Test
    public void setSkull1() {
        Cell cell = new Cell(true);
        cell.setSkull(false);
        assertEquals(false, cell.hasSkull());
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