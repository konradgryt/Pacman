package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.TextView;
import android.graphics.Rect;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game {

    int h,w; //used for storing our height and width of the view
    private boolean coinsInitializedFlag = false;
    private boolean wallsInitializedFlag = false;

    private Direction direction;
    public int pixels = 1;

    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have

    //bitmap of the pacman
    private Bitmap pacBitmap;
    private Bitmap enemyBitmap;
    private Bitmap coinBitmap;
    private Bitmap wallBitmap;
    private Thread thread;
    private Thread enemyThread;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    private  int enemyx, enemyy;

    //STATIC OBJECTS
    private ArrayList<StaticObject> staticObjects = new ArrayList<>();

    //a reference to the gameview
    private GameView gameView;

    public Game(Context context, TextView view)
    {
        this.staticObjects = new ArrayList<>();
        this.context = context;
        this.pointsView = view;

        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.polishgold);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird);
        wallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void initializeGameObjects(Canvas canvas, int w, int h) {
        setSize(h,w);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        canvas.drawBitmap(getPacBitmap(), getPacx(),getPacy(), paint);
        canvas.drawBitmap(getEnemyBitmap(), getEnemyx(),getEnemyy(), paint);
        initializeStaticObjects();
        doCollisionCheck();
        ArrayList<StaticObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            if (!so.get(i).isCollected()) {
                canvas.drawBitmap(so.get(i).getBitmap(), so.get(i).getX(), so.get(i).getY(), paint);
            }
        }
        updatePoints();
    }

    public void initializeStaticObjects()
    {
        if (!coinsInitializedFlag) {
            for (int i = 0; i < 10; i++) {
                staticObjects.add(new StaticObject(random(w), random(h), coinBitmap));
            }
            coinsInitializedFlag = true;
        }

        if (!wallsInitializedFlag) {
            staticObjects.add(new StaticObject(0, 0, wallBitmap));
            staticObjects.add(new StaticObject(80, 0,wallBitmap));
            staticObjects.add(new StaticObject(160, 0,wallBitmap));
            staticObjects.add(new StaticObject(0,  80,wallBitmap));
            staticObjects.add(new StaticObject(80, 80,wallBitmap));
            wallsInitializedFlag = true;
        }
    }

    public void death() {
        thread.interrupt();
        enemyThread.interrupt();
        newGame();
    }

    public void newGame()
    {
        staticObjects  = new ArrayList<>();
        thread = new Thread(new InputThread());
        enemyThread = new Thread(new EnemyThread());
        thread.start();
        enemyThread.start();
        coinsInitializedFlag = false;
        wallsInitializedFlag = false;
        pacx = 50;
        pacy = 400;
        enemyx = 800;
        enemyy = 0;
        direction = Direction.IDLE;
        points = 0;
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
        gameView.invalidate();
    }

    public void updatePoints() {
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
    }

    public int random(int x)
    {
        Random rand = new Random();

        int  n = rand.nextInt(x);
        return n;
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void changeDirection(Direction direction)
    {
        this.direction = direction;
    }

    public void doCollisionCheck()
    {
        Rect pacman = new Rect(getPacx(), getPacy(), getPacx() + pacBitmap.getWidth(), getPacy() + pacBitmap.getHeight());
        Rect enemy = new Rect(getEnemyx(), getEnemyy(), getEnemyx() + enemyBitmap.getWidth(), getEnemyy() + enemyBitmap.getHeight());
        if (Rect.intersects(enemy,pacman)) {
            death();
        }


        ArrayList<StaticObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            Rect wall = new Rect(so.get(i).getX(), so.get(i).getY(), so.get(i).getX() + so.get(i).getBitmap().getWidth(), so.get(i).getY() + so.get(i).getBitmap().getHeight());
            if (Rect.intersects(wall ,pacman)) {
             //   direction = Direction.IDLE;
            }
            if (!so.get(i).isCollected() && Rect.intersects(wall,pacman)) {
                so.get(i).setCollected(true);
                points++;
            }
        }
    }

    public int getEnemyx()
    {
        return enemyx;
    }

    public int getEnemyy()
    {
        return enemyy;
    }


    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }
    public Bitmap getEnemyBitmap()
    {
        return enemyBitmap;
    }

    private class InputThread extends Thread {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1);
                    if (getPacy() - pixels > 0 && direction.equals(Direction.UP)) {
                        pacy = pacy - pixels;
                    }
                    else if (getPacx() + pixels + getPacBitmap().getWidth() < w && direction.equals(Direction.RIGHT)) {
                        pacx = pacx + pixels;
                    }
                    else if (getPacy() + pixels + getPacBitmap().getHeight() < h && direction.equals(Direction.DOWN)) {
                        pacy = pacy + pixels;
                    }
                    else if (getPacx() - pixels > 0 && direction.equals(Direction.LEFT)) {
                        pacx = pacx - pixels;
                    } else {
                        direction = Direction.IDLE;
                    }
                    gameView.invalidate();
                }
            } catch (InterruptedException e) {

            }
        }
    }

    private class EnemyThread extends Thread {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(50);
                    if (pacy < enemyy) {
                        enemyy--;
                    }
                    if (pacy > enemyy) {
                        enemyy++;
                    }
                    if (pacx < enemyx) {
                        enemyx--;
                    }
                    if (pacx > enemyx) {
                        enemyx++;
                    }
                    gameView.invalidate();
                }
            } catch (InterruptedException e) {

            }
        }
    }

}
