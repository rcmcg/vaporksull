package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SkullTest {

    @Test
    public void isDiscovered() {
        Skull skull = new Skull();
        assertEquals(false, skull.isDiscovered());
    }

    @Test
    public void setDiscovered() {
        Skull skull = new Skull();
        skull.setDiscovered(true);
        assertEquals(true, skull.isDiscovered());
    }
}