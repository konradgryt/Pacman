package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();

    private ArrayList<GoldCoin> walls = new ArrayList<>();

    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
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

    public void death() {
        thread.interrupt();
        enemyThread.interrupt();
        newGame();
    }

    public void newGame()
    {
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
        coins = new ArrayList<>();
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

    public void initializeCoins(int count, int x, int y)
    {

        if (!coinsInitializedFlag) {
            for (int i = 0; i < count; i++) {
                coins.add(new GoldCoin(random(x), random(y)));
            }
            coinsInitializedFlag = true;
        }
    }

    public void initializeWalls()
    {
        if (!wallsInitializedFlag) {
                walls.add(new GoldCoin(0, 0));
                walls.add(new GoldCoin(80, 0));
                walls.add(new GoldCoin(160, 0));
                walls.add(new GoldCoin(0,  80));
                walls.add(new GoldCoin(80, 80));
            wallsInitializedFlag = true;
        }
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
        for (int i = 0; i < getWalls().size(); i++) {
            Rect wall = new Rect(getWalls().get(i).getCoinx(), getWalls().get(i).getCoiny(), getWalls().get(i).getCoinx() + coinBitmap.getWidth(), getWalls().get(i).getCoiny() + coinBitmap.getHeight());
            if (Rect.intersects(wall,pacman)) {
                direction = Direction.IDLE;
            }
        }
        for (int i = 0; i < getCoins().size(); i++) {
            Rect coin = new Rect(getCoins().get(i).getCoinx(), getCoins().get(i).getCoiny(), getCoins().get(i).getCoinx() + coinBitmap.getWidth(), getCoins().get(i).getCoiny() + coinBitmap.getHeight());

            if (!getCoins().get(i).isCollected() && Rect.intersects(coin,pacman)) {
                getCoins().get(i).handleCollection();
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

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<GoldCoin> getWalls()
    {
        return walls;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }
    public Bitmap getEnemyBitmap()
    {
        return enemyBitmap;
    }
    public Bitmap getCoinBitmap()
    {
        return coinBitmap;
    }
    public Bitmap getWallBitmap()
    {
        return wallBitmap;
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
