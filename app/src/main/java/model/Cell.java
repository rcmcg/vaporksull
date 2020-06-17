package model;

public class Cell {
    private Mine mine;
    private boolean scanned;

    public Cell(boolean buildMine) {
        if (buildMine) {
            mine = new Mine();
        } else {
            mine = null;
        }
        scanned = false;
    }

    public boolean hasMine() {
        return mine != null;
    }

    public Mine getMine() {
        return mine;
    }

    public void setMine(boolean bool) {
        if (bool) {
            if (!hasMine()) {
                mine = new Mine();
            }
        } else {
            mine = null;
        }
        return;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }
}
