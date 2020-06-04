/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

/**
 *
 * @author Carl
 */
public class SpriteTile {

    public SpriteTile(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    private int x;
    private int y;
    private int width;
    private int height;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
