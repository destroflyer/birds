/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Carl
 */
public class Terrain {

    public static int ISLAND_CONTENT_WIDTH = 4000;
    private int HILLS_DISTANCE = 190;
    public static int ISLAND_DISTANCE = 2500;
    private int width;
    private LinkedList<TerrainPoint> terrainPoints = new LinkedList<>();

    public float getHeight(float x) {
        if (x < 0) {
            return 0;
        }
        while (x >= width) {
            int nextPointX = (width + HILLS_DISTANCE);
            int xOnIsland = (nextPointX % (ISLAND_CONTENT_WIDTH + ISLAND_DISTANCE));
            if (xOnIsland > ISLAND_CONTENT_WIDTH) {
                TerrainPoint oldIslandRamp1 = new TerrainPoint(nextPointX, 410);
                TerrainPoint oldIslandRamp2 = new TerrainPoint(nextPointX + 410, 250);
                TerrainPoint oldIslandRamp3 = new TerrainPoint(nextPointX + 490, 250);
                TerrainPoint oldIslandRamp4 = new TerrainPoint(nextPointX + 640, 370);
                TerrainPoint oldIslandEnd = new TerrainPoint(nextPointX + 850, 0);
                TerrainPoint newIslandStart = new TerrainPoint(nextPointX + ISLAND_DISTANCE, 0);
                terrainPoints.add(oldIslandRamp1);
                terrainPoints.add(oldIslandRamp2);
                terrainPoints.add(oldIslandRamp3);
                terrainPoints.add(oldIslandRamp4);
                terrainPoints.add(oldIslandEnd);
                terrainPoints.add(newIslandStart);
                width = newIslandStart.getX();
            } else {
                int pointY = Util.getRandomNumberBetween(200, 450);
                TerrainPoint point = new TerrainPoint(nextPointX, pointY);
                terrainPoints.add(point);
                width = nextPointX;
            }
        }
        TerrainPoint previousPoint = null;
        TerrainPoint point = null;
        Iterator<TerrainPoint> terrainPointIterator = terrainPoints.iterator();
        while (terrainPointIterator.hasNext()) {
            point = terrainPointIterator.next();
            if (point.getX() > x) {
                if (previousPoint == null) {
                    previousPoint = new TerrainPoint(0, 0);
                }
                break;
            }
            previousPoint = point;
        }
        int horizontalDistance = (point.getX() - previousPoint.getX());
        double accommodationFactor = (Math.PI / horizontalDistance);
        double amplitude = (Math.abs(point.getY() - previousPoint.getY()) / 2.0);
        int horizontalShift = (horizontalDistance / 2) * ((point.getY() < previousPoint.getY())?1:3);
        int verticalShift = Math.min(point.getY(), previousPoint.getY());
        x -= previousPoint.getX();
        return (float) (((Math.sin(accommodationFactor * (x + horizontalShift)) * amplitude) + amplitude) + verticalShift);
    }

    public float getSlope(float x){
        return (getHeight(x + 1) - getHeight(x));
    }
}
