package org.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GameObject {

    private int x, y;
    private Bitmap bitmap;

    public GameObject(int x, int y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap =  bitmap;
    }

    public int getX() {
        return x;
    }

    public void moveRight(int speed) {
        this.x+= speed;
    }

    public void moveLeft(int speed) {
        this.x-=speed;
    }

    public void moveUp(int speed) {
        this.y-=speed;
    }

    public void moveDown(int speed) {
        this.y+=speed;
    }

    public int getY() {
        return y;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void handleCollision() {
      // Player.direction = Direction.IDLE;
    }

    public boolean isCollected() {
        return false;
    }

    public Rect createRectangle() {
            Rect rect = new Rect(getX(), getY(), getX() + bitmap.getWidth(), getY() + bitmap.getHeight());
        return rect;
    }

    public void update() {

    }
}
