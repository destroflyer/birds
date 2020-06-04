/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.awt.Color;

/**
 *
 * @author Carl
 */
public class Skin {

    public Skin(String name, Color[] terrainColors, Color[] skyColors) {
        this.name = name;
        this.terrainColors = terrainColors;
        this.skyColors = skyColors;
    }
    private String name;
    private Color[] terrainColors;
    private Color[] skyColors;

    public String getName() {
        return name;
    }

    public Color[] getTerrainColors() {
        return terrainColors;
    }

    public Color[] getSkyColors() {
        return skyColors;
    }
}
