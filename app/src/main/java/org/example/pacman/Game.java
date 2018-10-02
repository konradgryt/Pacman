package org.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.TextView;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.Random;
import java.util.ArrayList;
import android.provider.Settings.Secure;
public class Game {
    public static int h, w; //used for storing our height and width of the view

    //context is a reference to the activity
    public MainActivity context;

    public static int points = 0; //what points do we have
    public static int level = 1;

    public final int baseCoinCount = 5;

    //textview reference to points
    private TextView pointsView;
    private TextView highScoreView;

    //game objects
    public Player player;
    public Enemy enemy;
    private ArrayList<GameObject> staticObjects;

    //a reference to the gameview
    private GameView gameView;

    public Game(MainActivity context, TextView view, TextView view2) {
        player = new Player(120, 1000, BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman));
        enemy = new Enemy(700, 80, BitmapFactory.decodeResource(context.getResources(), R.drawable.bird));
        this.staticObjects = new ArrayList<>();
        this.context = context;
        this.pointsView = view;
        this.highScoreView = view2;
    }

    public void setGameView(GameView view) {
        this.gameView = view;
    }

    //Initializes the referemces to the things that aren't changing
    public void initializeGame(int w, int h) {
        Game.h = h;
        Game.w = w;

        for (int i = 0; i < Game.level * baseCoinCount; i++) {
            staticObjects.add(new Collectable(random(w / 2), random(h / 2), BitmapFactory.decodeResource(context.getResources(), R.drawable.polishgold)));
        }
        Bitmap wallBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);
        for (int i = 0; i < w; i+= 80) {
            //left wall
            staticObjects.add(new GameObject(0,  i, wallBitmap));

            //right wall
            staticObjects.add(new GameObject(w - 100, i, wallBitmap));

            //top wall
            staticObjects.add(new GameObject(i, 0, wallBitmap));

            //bottom wall
            staticObjects.add(new GameObject(i , h - 100, wallBitmap));
        }
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

        SharedPreferences sharedpreferences = context.getSharedPreferences(context.android_id, Context.MODE_PRIVATE);
        if (Game.points > sharedpreferences.getInt("highscore",  0)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("highscore", Game.points);
            editor.apply();
            editor.commit();
        }
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
        highScoreView.setText(String.format(context.getResources().getString(R.string.highscore) + "%d", sharedpreferences.getInt("highscore",0)));
    }

    public void updateMovingGameObjects() {
        if (Player.direction == Direction.LEFT) {
            player.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmanleft));
        } else if (Player.direction == Direction.UP){
            player.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmanup));
        } else if (Player.direction == Direction.DOWN) {
            player.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pacmandown));
        } else {
            player.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman));
        }
        player.update();
    }

    public void death() {
        level = 1;
        Toast.makeText(context,"You died (on level " + Integer.toString(Game.level)+ ")",Toast.LENGTH_LONG).show();
        context.setupGame();
    }

    public void win() {
        context.setupGame();
        Toast.makeText(context,"You advanced to level " + Integer.toString(Game.level),Toast.LENGTH_LONG).show();
    }

    public void newGame() {
        staticObjects = new ArrayList<>();
        points = 0;
        pointsView.setText(String.format(context.getResources().getString(R.string.points) + "%d", points));
        gameView.invalidate();
    }


    public int random(int x) {
        Random rand = new Random();

        int n = rand.nextInt(x) + 100;
        return n;
    }

    public void doCollisionCheck() {
        Rect pacman = player.createRectangle();
        Rect enemy = this.enemy.createRectangle();
        if (Rect.intersects(enemy, pacman)) {
            if (Game.points >= baseCoinCount * level) {
                Game.level++;
                win();
            } else {
                death();
            }
        }
        ArrayList<GameObject> so = this.staticObjects;
        for (int i = 0; i < so.size(); i++) {
            Rect object = so.get(i).createRectangle();
            if (so.get(i) instanceof Collectable) {
                if (!so.get(i).isCollected() && Rect.intersects(object, pacman)) {
                    so.get(i).handleCollision();
                    if (Game.points >= baseCoinCount * level) {
                        Toast.makeText(context,"Eat a bird to advance to level " + Integer.toString(Game.level +1 ) + "!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}