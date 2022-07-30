package me.esti.morpion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Range;
import android.util.MutableBoolean;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class GameView extends View {
    public static final int VS_IA = 0;
    public static final int VS_USER = 1;
    private int gameW = 2;
    public int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGameW(int gameW) {
        this.gameW = gameW;
        matrice = new Integer[gameW][gameW];
        this.initSetOfMove();
    }

    Logger log = Logger.getLogger(this.getClass().getName());
    Integer[][] matrice = new Integer[gameW][gameW];
    Set<Pair<Integer, Integer>> setOfMove = new HashSet<>();

    public ObservableBoolean getHasWinner() {
        return hasWinner;
    }

    ObservableInt turn = new ObservableInt(1);

    public ObservableInt getTurn() {
        return turn;
    }

    int width;
    Paint paint;
    ObservableBoolean hasWinner = new ObservableBoolean(false);
    Integer winner = null;

    public Integer getWinner() {
        return winner;
    }

    Pair<Integer, Integer> pos = new Pair<>(-1, -1);

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reinitialise() {
        matrice = new Integer[gameW][gameW];
        this.initPaint();
        hasWinner.set(false);
        winner = null;
        this.initSetOfMove();
        turn.set(1);
        invalidate();

    }

    public void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initSetOfMove() {
        int[] range = IntStream.range(0, gameW).toArray();
        for (int i : range) {
            for (int j : range) {
                log.info(String.format("%d %d", i, j));
                setOfMove.add(new Pair<>(i, j));
            }
        }
    }

    public GameView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initPaint();
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
            if (row + col == matrice.length - 1) {
                if (drawInvertedDiagonalWin(canvas)) return;
            }
            if (drawRowWin(canvas, row)) return;

            if (drawColumnWin(canvas, col)) return;
        }
    }

    private boolean drawColumnWin(Canvas canvas, int col) {
        Pair<Boolean, Integer> colState = this.colWin(col);
        if (colState.first) {
            winner = colState.second;
            hasWinner.set(true);
            paint.setColor(winner == 1 ? Color.GREEN : Color.RED);
            canvas.drawLine(col * width + (width / 2), width / 6, col * width + (width / 2), width * gameW - width / 6, paint);
            return true;
        }
        return false;
    }

    private boolean drawRowWin(Canvas canvas, int row) {
        Pair<Boolean, Integer> rowState = this.rowWin(row);
        if (rowState.first) {
            winner = rowState.second;
            hasWinner.set(true);
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
            winner = diagIState.second;
            hasWinner.set(true);
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
            winner = diagState.second;
            hasWinner.set(true);
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
        setOfMove.clear();
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
            if (matrice[pos.first][pos.second] != null || hasWinner.get() || (GameView.VS_IA == mode && turn.get() == 2)) {
                return true;
            }
            setOfMove.remove(pos);
            matrice[pos.first][pos.second] = turn.get();
            this.invalidate();
            if (turn.get() == 1) {
                turn.set(2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mode == GameView.VS_IA && setOfMove.size() > 0 && !hasWinner.get()) {
                            pos = getTouchFromIa();
                            matrice[pos.first][pos.second] = turn.get();
                            turn.set(1);
                            invalidate();
                        }
                    }
                }, 50);
            } else {
                turn.set(1);
            }
        }
        return true;
    }

    private Pair<Integer, Integer> getTouchFromIa() {
        Random random = new Random();
        List<Pair<Integer, Integer>> arrayOfMove = new ArrayList<>(setOfMove);
        int randomNumber = random.nextInt(arrayOfMove.size());
        Pair<Integer, Integer> item = arrayOfMove.get(randomNumber);
        this.log.info(item.toString());
        setOfMove.remove(item);
        return item;
    }
}
