package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;


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
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameView.invalidate(); //redraw screen
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

//    public void movePacmanUp(int pixels)
//    {
//        Thread t = new Thread(new InputThread(pixels, Direction.UP));
//        t.start();
//    }
//
//    public void movePacmanRight(int pixels)
//    {
//        Thread t = new Thread(new InputThread(pixels, Direction.RIGHT));
//        t.start();
//    }
//
//    public void movePacmanLeft(int pixels)
//    {
//        Thread t = new Thread(new InputThread(pixels, Direction.LEFT));
//        t.start();
//    }
//
//    public void movePacmanDown(int pixels)
//    {
//        Thread t = new Thread(new InputThread(pixels, Direction.DOWN));
//        t.start();
//    }

    public boolean boundariesCheck(int pixels)
    {
        String currentWidth =  Integer.toString(pacx+pixels+pacBitmap.getWidth());
        String currentHeight = Integer.toString(pacy+pixels+pacBitmap.getHeight());
        Log.d("currw", currentWidth);
        Log.d("currh", currentHeight);
        //return pacy+pixels+pacBitmap.getHeight() < this.h && pacx+pixels+pacBitmap.getWidth() < this.w;
        return true;
    }

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
                    while (true) {
                        Thread.sleep(10);
                        pacy = pacy - pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.RIGHT)) {
                    while (true) {
                        Thread.sleep(10);
                        pacx = pacx + pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.DOWN)) {
                    while (true) {
                        Thread.sleep(10);
                        pacy = pacy + pixels;
                        doCollisionCheck();
                        gameView.invalidate();
                    }
                }
                else if (direction.equals(Direction.LEFT)) {
                    while (true) {
                        Thread.sleep(10);
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
