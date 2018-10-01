package org.example.pacman;

import android.graphics.Bitmap;
import android.util.Log;

public class Enemy extends GameObject {
    Player target;
    int speed = 1;

    public Enemy(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    @Override
    public void update() {
        if (target.getY() < getY()) {
           moveUp(speed);
        }
        if (target.getY() > getY()) {
            moveDown(speed);
        }
        if (target.getX() < getX()) {
            moveLeft(speed);
        }
        if (target.getX() > getX()) {
            moveRight(speed);
        }

        Log.d("birdy",Integer.toString(getY()));
        Log.d("birdx",Integer.toString(getX()));
    }

    public void updateTarget(Player target) {
        this.target = target;
    }
}
