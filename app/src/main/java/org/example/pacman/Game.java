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

public class Game {

    int h,w; //used for storing our height and width of the view

    public static Direction direction;
    public int pixels = 1;

    //context is a reference to the activity
    private MainActivity context;
    public static int points = 0; //how points do we have

    //bitmap of the pacman
    private Bitmap enemyBitmap;
    private Thread thread;
    private Thread enemyThread;
    //textview reference to points
    private TextView pointsView;

    //PLAYER
    private Player player;

    //ENEMY
    private Enemy enemy;

    //STATIC OBJECTS
    private ArrayList<GameObject> staticObjects;

    //a reference to the gameview
    private GameView gameView;

    public Game(MainActivity context, TextView view)
    {
        this.staticObjects = new ArrayList<>();
        this.context = context;
        this.pointsView = view;
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void initializeGame(int w, int h) {
        setSize(h,w);
        initializeStaticObjects();
        initializePlayer();
    }

    public void loop(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        canvas.drawBitmap(player.getBitmap(), player.getX() , player.getX(), paint);
        canvas.drawBitmap(enemy.getBitmap(), enemy.getX() , enemy.getX(), paint);
        doCollisionCheck();
        ArrayList<GameObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            if (!so.get(i).isCollected()) {
                canvas.drawBitmap(so.get(i).getBitmap(), so.get(i).getX(), so.get(i).getY(), paint);
            }
        }
        updatePoints();
    }

    public void initializePlayer() {
        player = new Player(400, 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman));
        enemy = new Enemy(800, 0, BitmapFactory.decodeResource(context.getResources(), R.drawable.bird));
        direction = Direction.IDLE;
        thread = new Thread(new InputThread());
        enemyThread = new Thread(new EnemyThread());
        thread.start();
        enemyThread.start();

    }

    public void initializeStaticObjects()
    {
     for (int i = 0; i < 10; i++) {
            staticObjects.add(new Collectable(random(w), random(h), BitmapFactory.decodeResource(context.getResources(), R.drawable.polishgold)));
     }
        Bitmap wallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        staticObjects.add(new GameObject(0, 0, wallBitmap));
        staticObjects.add(new GameObject(80, 0,wallBitmap));
        staticObjects.add(new GameObject(160, 0,wallBitmap));
        staticObjects.add(new GameObject(0,  80,wallBitmap));
        staticObjects.add(new GameObject(80, 80,wallBitmap));

    }

    public void death() {
        thread.interrupt();
        enemyThread.interrupt();
        context.setupGame();
    }

    public void newGame()
    {
        staticObjects  = new ArrayList<>();
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
        Rect pacman = player.createRectangle();
        Rect enemy = this.enemy.createRectangle();
        if (Rect.intersects(enemy,pacman)) {
            death();
        }

        ArrayList<GameObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            Rect object = so.get(i).createRectangle();
            if (so.get(i) instanceof Collectable) {
                if (!so.get(i).isCollected() && Rect.intersects(object,pacman)) {
                    so.get(i).handleCollision();
                }
            } else {
               if (Rect.intersects(object ,pacman)) {
                    so.get(i).handleCollision();
                }
            }

        }
    }

    private class InputThread extends Thread {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1);
                    if (player.getY() - pixels > 0 && direction.equals(Direction.UP)) {
                        player.setY(player.getY() - pixels);
                    }
                    else if (player.getX()+ pixels + player.getBitmap().getWidth() < w && direction.equals(Direction.RIGHT)) {
                        player.setX(player.getX() + pixels);
                    }
                    else if (player.getY() + pixels + player.getBitmap().getHeight() < h && direction.equals(Direction.DOWN)) {
                        player.setY(player.getY() + pixels);
                    }
                    else if (player.getX() - pixels > 0 && direction.equals(Direction.LEFT)) {
                        player.setX(player.getX() - pixels);
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
                    if (player.getY() < enemy.getY()) {
                        enemy.setY( enemy.getY() - 1);
                    }
                    if (player.getY() > enemy.getY()) {
                        enemy.setY( enemy.getY() + 1);
                    }
                    if (player.getX() < enemy.getX()) {
                        enemy.setY( enemy.getX() - 1);
                    }
                    if (player.getX() > enemy.getX()) {
                        enemy.setX( enemy.getY() + 1);
                    }
                    gameView.invalidate();
                }
            } catch (InterruptedException e) {

            }
        }
    }

}
