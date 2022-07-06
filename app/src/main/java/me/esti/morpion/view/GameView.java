package me.esti.morpion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {
    int gameW = 3;
    String CLASS_NAME = this.getClass().getName();
    int[][] matrice = new int[gameW][gameW];
    Paint paint = new Paint();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0; i<matrice.length;i++) {
            int[] row = matrice[i];
            for (int j=0; j<row.length;j++) {
                int item = row[j];
                Log.d(CLASS_NAME,String.format("Matrice[%o][%o]: %o",i,j,item ));
                int posX = (j) * 301;
                int posY = (i+1) * 301;
                Rect rect = new Rect(posX, posY, posX + 300, posY+300);
                canvas.drawRect(rect, paint);
            }
        }
    }


}
