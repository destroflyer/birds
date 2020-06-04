/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author Carl
 */
public class ImageCache {

    private static HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    public static BufferedImage getImage(String imageFilePath) {
        return IMAGES.computeIfAbsent(imageFilePath, fp -> Util.getImage(imageFilePath));
    }
}
