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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void handleCollision() {
        Game.direction = Direction.IDLE;
    }

    public boolean isCollected() {
        return false;
    }

    public Rect createRectangle() {
            Rect rect = new Rect(getX(), getY(), getX() + bitmap.getWidth(), getY() + bitmap.getHeight());
        return rect;
    }
}
