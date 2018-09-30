package org.example.pacman;

import android.graphics.Bitmap;

public class StaticObject extends GameObject {
    boolean collected;

    public StaticObject(int x, int y, Bitmap bitmap) {
        super(x, y, bitmap);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
