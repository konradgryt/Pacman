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

    public static int h, w; //used for storing our height and width of the view

    //context is a reference to the activity
    private MainActivity context;
    public static int points = 0; //how points do we have

    //textview reference to points
    private TextView pointsView;

    //game objects
    public Player player;
    public Enemy enemy;
    private ArrayList<GameObject> staticObjects;

    //a reference to the gameview
    private GameView gameView;

    public Game(MainActivity context, TextView view) {
        player = new Player(80, 1300, BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman));
        enemy = new Enemy(700, 80, BitmapFactory.decodeResource(context.getResources(), R.drawable.bird));
        this.staticObjects = new ArrayList<>();
        this.context = context;
        this.pointsView = view;
    }

    public void setGameView(GameView view) {
        this.gameView = view;
    }

    //Initializes the referemces to the things that aren't changing
    public void initializeGame(int w, int h) {
        Game.h = h;
        Game.w = w;

        for (int i = 0; i < 10; i++) {
            staticObjects.add(new Collectable(random(w), random(h), BitmapFactory.decodeResource(context.getResources(), R.drawable.polishgold)));
        }
        Bitmap wallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        staticObjects.add(new GameObject(0, 0, wallBitmap));
        staticObjects.add(new GameObject(80, 0, wallBitmap));
        staticObjects.add(new GameObject(160, 0, wallBitmap));
        staticObjects.add(new GameObject(0, 80, wallBitmap));
        staticObjects.add(new GameObject(80, 80, wallBitmap));
    }

    //Draws the things that are changing
    public void loop(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
        canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), paint);
        doCollisionCheck();
        ArrayList<GameObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            if (!so.get(i).isCollected()) {
                canvas.drawBitmap(so.get(i).getBitmap(), so.get(i).getX(), so.get(i).getY(), paint);
            }
        }
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
    }

    public void updateMovingGameObjects() {
        player.update();
    }

    public void death() {
        Log.d("death","dead");
        context.setupGame();
    }

    public void newGame() {
        staticObjects = new ArrayList<>();
        points = 0;
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
        gameView.invalidate();
    }


    public int random(int x) {
        Random rand = new Random();

        int n = rand.nextInt(x);
        return n;
    }

    public void doCollisionCheck() {
        Rect pacman = player.createRectangle();
        Rect enemy = this.enemy.createRectangle();
        if (Rect.intersects(enemy, pacman)) {
            death();
        }
        ArrayList<GameObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            Rect object = so.get(i).createRectangle();
            if (so.get(i) instanceof Collectable) {
                if (!so.get(i).isCollected() && Rect.intersects(object, pacman)) {
                    so.get(i).handleCollision();
                }
            } else {
                if (Rect.intersects(object, pacman)) {
                    so.get(i).handleCollision();
                }
            }
        }
    }
}