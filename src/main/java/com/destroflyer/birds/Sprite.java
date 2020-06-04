/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

/**
 *
 * @author carl
 */
public class Sprite {

    public Sprite(String imageFilePath) {
        this(imageFilePath, null, -1);
    }

    public Sprite(String imageFilePath, SpriteTile[] tiles, int durationPerTile) {
        this.imageFilePath = imageFilePath;
        this.tiles = tiles;
        this.durationPerTile = durationPerTile;
    }
    private String imageFilePath;
    private SpriteTile[] tiles;
    private int durationPerTile;

    public String getImageFilePath() {
        return imageFilePath;
    }

    public int getDurationPerTile() {
        return durationPerTile;
    }

    public void setDurationPerTile(int durationPerTile) {
        this.durationPerTile = durationPerTile;
    }
    
    public boolean isTiled() {
        return (tiles != null);
    }
    
    public SpriteTile getCurrentTile() {
        return tiles[getCurrentTileIndex()];
    }
    
    public int getCurrentTileIndex() {
        return (int) ((System.currentTimeMillis() / durationPerTile) % getTilesCount());
    }
    
    public int getFramesCount() {
        return (getTilesCount() * durationPerTile);
    }

    public int getTilesCount() {
        return tiles.length;
    }
}
