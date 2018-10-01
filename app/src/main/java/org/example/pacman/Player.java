package org.example.pacman;

import android.graphics.Bitmap;
import android.util.Log;

public class Player extends GameObject {
    Direction direction = Direction.IDLE;
    int speed = 10;

    public Player(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    @Override
    public void update() {
        if (getY() - speed > 0 && direction.equals(Direction.UP)) {
            moveUp(speed);
        } else if (getX() + speed + getBitmap().getWidth() < Game.w && direction.equals(Direction.RIGHT)) {
            moveRight(speed);
        } else if (getY() + speed + getBitmap().getHeight() < Game.h && direction.equals(Direction.DOWN)) {
            moveDown(speed);
        } else if (getX() - speed > 0 && direction.equals(Direction.LEFT)) {
            moveLeft(speed);
        } else {
            direction = Direction.IDLE;
        }
        Log.d("direction",direction.toString());
        Log.d("pacy",Integer.toString(getY()));
        Log.d("pacx",Integer.toString(getX()));
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}


