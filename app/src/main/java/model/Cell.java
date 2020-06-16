package model;

public class Cell {
    Mine mine;
    boolean scanned;

    public Cell(boolean hasMine) {
        if (hasMine) {
            mine = new Mine();
        } else {
            mine = null;
        }
        scanned = false;
    }

    public Mine getMine() {
        return mine;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }
}
