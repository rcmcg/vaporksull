package model;

// Cell makes up the entries of the Board class
// A cell either has a skull or it doesn't
// A cell is either scanned or not scanned and its hiddenCount is a count of the hidden
// skulls in its row and column. Updated when necessary

public class Cell {
    private Skull skull;
    private boolean scanned;
    private int hiddenCount;

    public Cell(boolean buildSkull) {
        if (buildSkull) {
            skull = new Skull();
        } else {
            skull = null;
        }
        scanned = false;
        hiddenCount = -1;
    }

    public boolean hasSkull() {
        return skull != null;
    }

    public Skull getSkull() {
        return skull;
    }

    public void setSkull(boolean buildSkull) {
        if (buildSkull) {
            if (!hasSkull()) {
                skull = new Skull();
            }
        } else {
            skull = null;
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
