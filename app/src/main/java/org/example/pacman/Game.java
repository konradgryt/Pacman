package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import java.util.Random;

import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game {

    private Thread activeThread;

    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have

    //bitmap of the pacman
    private Bitmap pacBitmap;
    private Bitmap coinBitmap;

    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.polishgold);

    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        initializeCoins(3);
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameView.invalidate(); //redraw screen
    }

    public int randomx(int x)
    {
        Random rand = new Random();

        int  n = rand.nextInt(x);
        return n;
    }

    public int randomy(int y)
    {
        Random rand = new Random();

        int  n = rand.nextInt(y);
        return n;
    }

    public void initializeCoins(int count)
    {
        for (int i = 0; i < count; i++) {
            GoldCoin coin = new GoldCoin(55,  200);
            GoldCoin coin2 = new GoldCoin(120,  88);
            GoldCoin coin3 = new GoldCoin(355,  220);
            coins.add(coin);
            coins.add(coin2);
            coins.add(coin3);
        }
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacman(int pixels, Direction direction)
    {
            if (activeThread == null) {
                activeThread = new Thread(new InputThread(pixels, direction));
                activeThread.start();
            } else {
                activeThread.interrupt();
                activeThread = new Thread(new InputThread(pixels, direction));
                activeThread.start();
            }
    }

//    public boolean boundariesCheck(int pixels)
//    {
//        String currentWidth =  Integer.toString(pacx+pixels+pacBitmap.getWidth());
//        String currentHeight = Integer.toString(pacy+pixels+pacBitmap.getHeight());
//        Log.d("currw", currentWidth);
//        Log.d("currh", currentHeight);
//        //return pacy+pixels+pacBitmap.getHeight() < this.h && pacx+pixels+pacBitmap.getWidth() < this.w;
//        return true;
//    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {

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

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap()
    {
        return coinBitmap;
    }

    private class InputThread implements Runnable {
        int pixels;
        Direction direction;

        InputThread(int pixels, Direction direction){
            this.pixels = pixels;
            this.direction = direction;
        }

        public void run() {
            try {
                if (direction.equals(Direction.UP)) {
                    while (getPacy() - pixels > 0) {
                        Thread.sleep(5);
                        pacy = pacy - pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.RIGHT)) {
                    while (getPacx() + pixels + getPacBitmap().getWidth() < w) {
                        Thread.sleep(5);
                        pacx = pacx + pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.DOWN)) {
                    while (getPacy() + pixels + getPacBitmap().getHeight() < h) {
                        Thread.sleep(5);
                        pacy = pacy + pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.LEFT)) {
                    while (getPacx() - pixels > 0) {
                        Thread.sleep(5);
                        pacx = pacx - pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }

}
