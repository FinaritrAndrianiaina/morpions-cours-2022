package me.esti.morpion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import me.esti.morpion.view.GameView;

public class AcceuilActivity extends AppCompatActivity {
    RadioGroup modeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        modeGroup = (RadioGroup) findViewById(R.id.modeGroup);
    }
    public void setGameMode(Intent gameIntent) {
        switch (modeGroup.getCheckedRadioButtonId()) {
            case R.id.vsIa:
                gameIntent.putExtra("mode", GameView.VS_IA);
                break;
            case R.id.vsUser:
            default:
                gameIntent.putExtra("mode", GameView.VS_USER);
        }
    }
    public void launcher3(View v) {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("gameW", 3);
        setGameMode(gameIntent);
        startActivity(gameIntent);
    }

    public void launcher4(View v) {
        Intent gameIntent = new Intent(this, MainActivity.class);
        gameIntent.putExtra("gameW", 4);
        setGameMode(gameIntent);
        startActivity(gameIntent);
    }
}