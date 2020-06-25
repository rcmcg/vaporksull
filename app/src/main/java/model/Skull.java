package model;

// A skull can be contained in a Cell. A skull is either discovered or not discovered through
// scans performed on Cells

public class Skull {
    private boolean discovered;

    public Skull() {
        discovered = false;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
}
