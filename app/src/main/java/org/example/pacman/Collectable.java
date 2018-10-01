package org.example.pacman;

import android.graphics.Bitmap;

public class Collectable extends GameObject {
    boolean collected;

    public Collectable(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
        collected = false;
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public void handleCollision() {
        Game.points++;
        if (Game.points > Game.highScore) {
            Game.highScore = Game.points;
        }
        collected = true;
    }
}
