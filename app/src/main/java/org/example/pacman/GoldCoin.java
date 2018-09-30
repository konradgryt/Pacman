package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    private int coinx;
    private int coiny;
    private boolean collected;

    public GoldCoin(int coinx, int coiny) {
        this.coinx = coinx;
        this.coiny = coiny;
        this.collected = false;
    }

    public void handleCollection() {
        this.collected = true;
    }

    public int getCoinx() {
        return coinx;
    }

    public int getCoiny() {
        return coiny;
    }

    public boolean isCollected() {
        return collected;
    }
}
