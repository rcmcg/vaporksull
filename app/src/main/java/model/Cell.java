package model;

public class Cell {
    private Mine mine;
    private boolean scanned;
    private int hiddenCount;

    public Cell(boolean buildMine) {
        if (buildMine) {
            mine = new Mine();
        } else {
            mine = null;
        }
        scanned = false;
        hiddenCount = -1;
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

    public int getHiddenCount() {
        return hiddenCount;
    }

    public void setHiddenCount(int hiddenCount) {
        this.hiddenCount = hiddenCount;
    }
}
