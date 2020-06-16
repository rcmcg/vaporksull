package com.example.cmpt276_a3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import model.GameData;

public class PlayGameActivity extends AppCompatActivity {

    Button buttons[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        populateButtons();
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForCells);

        // Get GameData Singleton
        GameData gameData = GameData.getInstance();

        // TEST
        gameData.setRows(6);
        gameData.setCols(15);

        buttons = new Button[gameData.getRows()][gameData.getCols()];

        // System.out.println("TRACE: gameData.getRows: " + gameData.getRows());
        // System.out.println("TRACE: gameData.getCols: " + gameData.getCols());

        // App crashes if rows or cols == 0, set an input check

        for (int row = 0; row < gameData.getRows(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for (int col = 0; col < gameData.getCols(); col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button btn = new Button(this);
                btn.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));
                btn.setText("" + row + "," + col);

                // Make text not clip on small buttons
                btn.setPadding(0,0,0,0);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(btn);
                buttons[row][col] = btn;
            }
        }
    }

    private void gridButtonClicked(int row, int col) {
        Toast.makeText(this, "Button clicked " + row + "," + col,
                Toast.LENGTH_LONG).show();
        Button btn = buttons[row][col];

        // Lock button sizes
        lockButtonSizes();

        // This command does not scale image
        // btn.setBackgroundResource(R.mipmap.blue_spotted_egg);

        // Scale image to button
        // Only works in JellyBean!
        int newWidth = btn.getWidth();
        int newHeight = btn.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.blue_spotted_egg);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        btn.setBackground(new BitmapDrawable(resource, scaledBitmap));

        // Change text on button:
        btn.setText("" + col);

    }

    private void lockButtonSizes() {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayGameActivity.class);
    }
}