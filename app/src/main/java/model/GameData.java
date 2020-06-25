package model;

// Holds the Singleton game settings gameData

public class GameData {
    private static int rows = 0;
    private static int cols = 0;
    private static int skulls = 0;

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

    public static int getSkulls() {
        return skulls;
    }

    public static void setSkulls(int skulls) {
        GameData.skulls = skulls;
    }
}
