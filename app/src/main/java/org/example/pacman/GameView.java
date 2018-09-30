package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {

	Game game;
    int h,w; //used for storing our height and width of the view


	public void setGame(Game game)
	{
		this.game = game;
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public GameView(Context context) {
		super(context);

	}

	public GameView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}


	public GameView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
		h = getHeight();
		w = getWidth();
		//update the size for the canvas to the game.
		game.setSize(h,w);
		Log.d("GAMEVIEW","h = "+h+", w = "+w);
		//Making a new paint object
		Paint paint = new Paint();
		canvas.drawColor(Color.WHITE); //clear entire canvas to white color

		canvas.drawBitmap(game.getPacBitmap(), game.getPacx(),game.getPacy(), paint);
		canvas.drawBitmap(game.getEnemyBitmap(), game.getEnemyx(),game.getEnemyy(), paint);
		game.initializeCoins(10, w, h);
		game.initializeWalls();
		game.doCollisionCheck();
		ArrayList<GoldCoin> coins = game.getCoins();
		for (int i = 0; i < coins.size(); i++) {
			if (!coins.get(i).isCollected()) {
				canvas.drawBitmap(game.getCoinBitmap(), coins.get(i).getCoinx(), coins.get(i).getCoiny(), paint);
			}
		}
		ArrayList<GoldCoin> walls = game.getWalls();
		for (int i = 0; i < walls.size(); i++) {
            canvas.drawBitmap(game.getWallBitmap(), walls.get(i).getCoinx(), walls.get(i).getCoiny(), paint);
		}
		game.updatePoints();
 		super.onDraw(canvas);
	}

}
