package me.esti.morpion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class GameView extends View {
    private  int gameW = 2;

    public void setGameW(int gameW) {
        this.gameW = gameW;
        matrice = new Integer[gameW][gameW];
    }

    String CLASS_NAME = this.getClass().getName();
    Integer[][] matrice = new Integer[gameW][gameW];
    int turn = 1;
    int width;
    Paint paint = new Paint();
    Boolean hasWinner = false;
    Integer winner = null;
    Pair<Integer, Integer> pos = new Pair<>(-1, -1);

    public void reinitialise(){
        matrice = new Integer[gameW][gameW];
        paint = new Paint();
        hasWinner = false;
        winner = null;
        turn = 1;
        invalidate();

    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < matrice.length; i++) {
            Integer[] row = matrice[i];
            for (int j = 0; j < row.length; j++) {
                Point size = new Point();
                getDisplay().getSize(size);
                width = size.x / gameW;
                Integer item = row[j];
                paint.setColor(Color.BLACK);
                // Log.d(CLASS_NAME, String.format("Matrice[%o][%o]: %o", i, j, item));
                int posX = (j) * width;
                int posY = (i) * width;
                Rect rect = new Rect(posX + 5, posY + 5, posX + width - 5, posY + width - 5);
                canvas.drawRect(rect, paint);
                if (item != null) {
                    paint.setColor(item == 1 ? Color.GREEN : Color.RED);
                    paint.setTextSize(width);
                    canvas.drawText(item == 1 ? "x" : "o", posX + (width / 4), posY + width - (width / 6), paint);
                }
            }
        }
        if (pos.first != -1 && pos.second != -1) {
            int row = pos.first;
            int col = pos.second;
            if (row == col) {
                if (drawDiagonalWin(canvas)) return;
            }
            if (row + col == matrice.length) {
                if (drawInvertedDiagonalWin(canvas)) return;
            }
            if (drawRowWin(canvas, row)) return;

            if (drawColumnWin(canvas, col)) return;
        }
        if (hasWinner && winner != null) {
            String textWin = "User " + winner.toString() + " gagne!!";
            paint.setTextSize(70);
            canvas.drawText(textWin, 0, width * (gameW + 1), paint);
        }
    }

    private boolean drawColumnWin(Canvas canvas, int col) {
        Pair<Boolean, Integer> colState = this.colWin(col);
        if (colState.first) {
            hasWinner = true;
            winner = colState.second;
            paint.setColor(winner == 1 ? Color.GREEN : Color.RED);
            canvas.drawLine(col * width + (width / 2), width / 6, col * width + (width / 2), width * 4 - width / 6, paint);
            return true;
        }
        return false;
    }

    private boolean drawRowWin(Canvas canvas, int row) {
        Pair<Boolean, Integer> rowState = this.rowWin(row);
        if (rowState.first) {
            hasWinner = true;
            winner = rowState.second;
            // 0 , i * width
            // width * gameW , i * width
            paint.setColor(winner == 1 ? Color.GREEN : Color.RED);
            canvas.drawLine(width / 6, row * width + (width / 2), width * gameW - width / 6, row * width + (width / 2), paint);
            return true;
        }
        return false;
    }

    private boolean drawInvertedDiagonalWin(Canvas canvas) {
        Pair<Boolean, Integer> diagIState = this.diagWinInverted();
        if (diagIState.first) {
            hasWinner = true;
            winner = diagIState.second;
            // width * gameW , 0
            // 0 , width * gameW
            paint.setColor(winner == 1 ? Color.GREEN : Color.RED);
            canvas.drawLine(width * gameW - width / 6, width / 6, width / 6, width * gameW - width / 6, paint);
            return true;
        }
        return false;
    }

    private boolean drawDiagonalWin(Canvas canvas) {
        Pair<Boolean, Integer> diagState = this.diagWin();
        if (diagState.first) {
            hasWinner = true;
            winner = diagState.second;
            // 0 , 0
            // width * gameW , width * gameW
            paint.setColor(winner == 1 ? Color.GREEN : Color.RED);
            canvas.drawLine(width / 6, width / 6, width * gameW - width / 6, width * gameW - width / 6, paint);
            return true;
        }
        return false;
    }

    public Pair<Integer, Integer> getTouchPosition(int touchX, int touchY) {
        return new Pair<>(touchY / width, touchX / width);
    }

    public Pair<Boolean, Integer> rowWin(int rowIndex) {
        Integer lastElements = matrice[rowIndex][0];
        for (int i = 1; i < matrice[rowIndex].length; i++) {
            if (lastElements == null || matrice[rowIndex][i] == null || !lastElements.equals(matrice[rowIndex][i])) {
                return new Pair<>(false, 0);
            }
        }
        return new Pair<>(true, lastElements);
    }

    public Pair<Boolean, Integer> colWin(int colIndex) {
        Integer lastElements = matrice[0][colIndex];
        for (int i = 1; i < matrice.length; i++) {
            if (lastElements == null || matrice[i][colIndex] == null || !lastElements.equals(matrice[i][colIndex])) {
                return new Pair<>(false, 0);
            }
        }
        return new Pair<>(true, lastElements);
    }

    public Pair<Boolean, Integer> diagWin() {
        Integer lastElements = matrice[0][0];
        for (int i = 1; i < matrice.length; i++) {
            if (lastElements == null || matrice[i][i] == null || !lastElements.equals(matrice[i][i])) {
                return new Pair<>(false, 0);
            }
        }
        return new Pair<>(true, lastElements);
    }

    public Pair<Boolean, Integer> diagWinInverted() {
        Integer lastElements = matrice[0][matrice.length - 1];
        for (int i = 1; i < matrice.length; i++) {
            if (lastElements == null || matrice[i][matrice.length - i - 1] == null || !lastElements.equals(matrice[i][matrice.length - i - 1])) {
                return new Pair<>(false, 0);
            }
        }
        return new Pair<>(true, lastElements);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP && touchY < width * gameW) {
            pos = getTouchPosition(touchX, touchY);
            if (matrice[pos.first][pos.second] != null || hasWinner) {
                return true;
            }
            matrice[pos.first][pos.second] = turn;
            if (turn == 1) {
                turn = 2;
            } else {
                turn = 1;
            }
            this.invalidate();
        }


        return true;
    }
}
