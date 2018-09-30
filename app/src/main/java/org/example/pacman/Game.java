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
    private boolean coinsInitializedFlag = false;
    private Direction direction = Direction.IDLE;
    public int pixels = 1;

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

    public void newGame()
    {
        pacx = 50;
        pacy = 400;
        points = 0;
        Thread t =  new Thread(new InputThread());
        t.start();
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
        for (int i = 0; i < getCoins().size(); i++) {
            if (!getCoins().get(i).isCollected() && Math.abs(getCoins().get(i).getCoinx() - getPacx()) < 140 && Math.abs(getCoins().get(i).getCoiny() - getPacy()) < 140) {
                getCoins().get(i).handleCollection();
                points++;
            }
         }
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

    private class InputThread extends Thread {
        public InputThread() {

        }
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1);
                    if (getPacy() - pixels > 0 && direction.equals(Direction.UP)) {
                        pacy = pacy - pixels;
                        gameView.invalidate();
                    }
                    else if (getPacx() + pixels + getPacBitmap().getWidth() < w &&direction.equals(Direction.RIGHT)) {
                        pacx = pacx + pixels;
                        gameView.invalidate();
                    }
                    else if (getPacy() + pixels + getPacBitmap().getHeight() < h && direction.equals(Direction.DOWN)) {
                        pacy = pacy + pixels;
                        gameView.invalidate();
                    }
                    else if (getPacx() - pixels > 0 && direction.equals(Direction.LEFT)) {
                        pacx = pacx - pixels;
                        gameView.invalidate();
                    } else {
                        direction = Direction.IDLE;
                    }
                }

            } catch (InterruptedException e) {

            }
        }
    }

}
