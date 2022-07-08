package me.esti.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AcceuilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
    }
    public void launcher3(View v){
        Intent gameIntent = new Intent(this,MainActivity.class);
        gameIntent.putExtra( "gameW", 3 );
        startActivity(gameIntent);


    }
    public void launcher4(View v){
        Intent gameIntent = new Intent(this,MainActivity.class);
        gameIntent.putExtra( "gameW", 4 );
        startActivity(gameIntent);


    }
}