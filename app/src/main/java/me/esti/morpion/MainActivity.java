package me.esti.morpion;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import me.esti.morpion.view.GameView;

public class MainActivity extends AppCompatActivity {
    AlertDialog dialog;
    public GameView gameView;
    public TextView winDisplay;
    public TextView countDisplay;
    public TextView turnDisplay;
    public int[] winCount = new int[]{0, 0};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameView = (GameView) findViewById(R.id.gameView);
        this.winDisplay = (TextView) findViewById(R.id.winDisplay);
        this.countDisplay = (TextView) findViewById(R.id.countDisplay);
        this.turnDisplay = (TextView) findViewById(R.id.turnDisplay);
        buildDialogNewGame();
        gameView.setMode(getIntent().getIntExtra("mode",0));
        gameView.setGameW(getIntent().getIntExtra("gameW", 3));
        gameView.getHasWinner().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (gameView.getHasWinner().get()) {
                    winCount[gameView.getWinner() - 1] += 1;
                    countDisplay.setText(winCount[0] + " VS " + winCount[1]);
                    winDisplay.setTextColor(gameView.getWinner() == 1 ? Color.GREEN : Color.RED);
                    winDisplay.setText("Joueur " + gameView.getWinner().toString() + " a gagné");
                } else {
                    winDisplay.setText("");
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameView.getLayoutParams().height = displayMetrics.widthPixels;
        gameView.getTurn().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                turnDisplay.setText("Tour: Joueur " + gameView.getTurn().get());
            }
        });
    }

    private void buildDialogNewGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Nouvelle partie");
        builder.setMessage("Voulez-vous vraiment rejouer la partie ?");
        builder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.reinitialise();
                    }
                });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog = builder.create();
    }


    public void retourPartie(View v) {
        Intent retour = new Intent(this, AcceuilActivity.class);
        startActivity(retour);

    }

    public void newPartie(View v) {
        dialog.show();

    }

}