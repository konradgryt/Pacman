package org.example.pacman;

import android.graphics.Bitmap;

public class Player extends GameObject {
    static Direction direction = Direction.IDLE;
    int speed = 1;

    public Player(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    @Override
    public void update() {
        if (getY() - speed > 120 && direction.equals(Direction.UP)) {
            moveUp(speed);
        } else if (getX() + speed + 80 + getBitmap().getWidth() < Game.w && direction.equals(Direction.RIGHT)) {
            moveRight(speed);
        } else if (getY() + speed + 80 + getBitmap().getHeight() < Game.h && direction.equals(Direction.DOWN)) {
            moveDown(speed);
        } else if (getX() - speed > 120 && direction.equals(Direction.LEFT)) {
            moveLeft(speed);
        } else {
           // direction = Direction.IDLE;
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}


