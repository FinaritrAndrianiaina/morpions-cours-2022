package me.esti.morpion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.esti.morpion.view.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         this.gameView = (GameView) findViewById(R.id.gameView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Nouvelle partie");
        builder.setMessage("Voulez-vous vraiment rejouer la partie ?");
        builder.setPositiveButton("Confirmer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.reinitialise();
                    }
                });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }});
         dialog = builder.create();

        gameView.setGameW(getIntent().getIntExtra("gameW", 3));
    }
    AlertDialog dialog;
    public GameView gameView;
    public void retourPartie(View v){
        Intent retour = new Intent(this,AcceuilActivity.class);
        startActivity(retour);

    }
    public void newPartie(View v){
        dialog.show();

    }

}