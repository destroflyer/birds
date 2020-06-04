/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.util.ArrayList;

/**
 *
 * @author Carl
 */
public class Map implements GameLoopListener {

    public Map(){
        terrain = new Terrain();
        player = new Bird();
        player.setX(START_X);
        player.setY(START_Y);
        addObject(player);
    }
    private static final int START_X = 500;
    private static final int START_Y = 500;
    private float worldGravity = 0.15f;
    private float playerGravity = 0.5f;
    private float accelerationStrength = 500;
    private Terrain terrain;
    private ArrayList<MapObject> objects = new ArrayList<>();
    private MapObject player;
    private boolean isPlayerAccelerating;
    boolean isPlayerOnGround;
    private float playerSlope = 0;

    @Override
    public void onNextFrameCalculation(float lastTimePerFrame) {
        float playerTerrainSlope = terrain.getSlope(player.getX());
        if (isPlayerOnGround) {
            float acceleration = accelerationStrength;
            if (isPlayerAccelerating) {
                acceleration += accelerationStrength;
            }
            player.setSpeed(player.getSpeed() - (lastTimePerFrame * (acceleration * playerTerrainSlope)));
            if (player.getSpeed() < 0) {
                player.setSpeed(0);
            }
        }
        float movedDistance = (lastTimePerFrame * player.getSpeed());
        if (isPlayerOnGround) {
            float newPlayerSlope = (movedDistance * playerTerrainSlope);
            if ((newPlayerSlope < 0) || (newPlayerSlope > playerSlope)) {
                playerSlope = newPlayerSlope;
            }
        } else {
            float gravity = worldGravity;
            if (isPlayerAccelerating) {
                gravity += playerGravity;
            }
            playerSlope -= gravity;
        }
        player.setX(player.getX() + movedDistance);
        player.setY(player.getY() + playerSlope);
        player.setAngle((float) (Math.atan(-1 * playerSlope)));
        isPlayerOnGround = (player.getY() <= terrain.getHeight(player.getX()));
        if (player.getY() <= 0) {
            player.setSpeed(0);
        }
    }

    public int getScore() {
        return (int) (player.getX() - START_X);
    }

    public boolean isGameOver() {
        return (player.getSpeed() <= 0);
    }

    public void addObject(MapObject mapObject) {
        mapObject.setMap(this);
        objects.add(mapObject);
    }

    public void setPlayerAccelerating(boolean playerAccelerating) {
        isPlayerAccelerating = playerAccelerating;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public ArrayList<MapObject> getObjects() {
        return objects;
    }

    public MapObject getPlayer() {
        return player;
    }
}
