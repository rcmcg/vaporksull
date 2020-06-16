package model;

public class GameData {
    private static int rows = 0;
    private static int cols = 0;
    private static int mines = 0;

    // Potentially use this function to save and load information

    private static GameData gameData;

    private GameData() {
        // Private constructor to prevent unwanted instantiation
    }

    public static GameData getInstance() {
        if (gameData == null) {
            gameData = new GameData();
        }
        return gameData;
    }

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        GameData.rows = rows;
    }

    public static int getCols() {
        return cols;
    }

    public static void setCols(int cols) {
        GameData.cols = cols;
    }

    public static int getMines() {
        return mines;
    }

    public static void setMines(int mines) {
        GameData.mines = mines;
    }
}
