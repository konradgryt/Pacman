package org.example.pacman;

import android.graphics.Bitmap;
import android.util.Log;

public class Enemy extends GameObject {
    Player target;
    int speed = 1;
    Boolean escape = false;

    public Enemy(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    @Override
    public void update() {
        if (target.getY() < getY()) {
            if (!escape) {
                moveUp(speed);
            } else {
                moveDown(speed);
            }
        }
        if (target.getY() > getY()) {
            if (!escape) {
                moveDown(speed);
            } else {
                moveUp(speed);
            }
        }
        if (target.getX() < getX()) {
            if (!escape) {
                moveLeft(speed);
            } else {
                moveRight(speed);
            }
        }
        if (target.getX() > getX()) {
            if (!escape) {
                moveRight(speed);
            } else {
                moveLeft(speed);
            }
        }
    }

    public void updateTarget(Player target) {
        this.target = target;
    }
}
