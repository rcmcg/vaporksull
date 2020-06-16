package model;

public class Mine {
    private boolean discovered;

    public Mine() {
        discovered = false;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
}
