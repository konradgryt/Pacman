package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
public class GameView extends View {
	Game game;
	boolean gameInitialized = false;

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

	public void setGame(Game game)
	{
		this.game = game;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!gameInitialized) {
			game.initializeGame(getWidth(), getHeight());
			gameInitialized = true;
		}
		game.loop(canvas);
		super.onDraw(canvas);
	}
}
