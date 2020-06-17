package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MineTest {

    @Test
    public void isDiscovered() {
        Mine mine = new Mine();
        assertEquals(false,mine.isDiscovered());
    }

    @Test
    public void setDiscovered() {
        Mine mine = new Mine();
        mine.setDiscovered(true);
        assertEquals(true, mine.isDiscovered());
    }
}